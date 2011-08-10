package uk.ac.brookes.arnaudbos.luscinia.utils;

import java.io.InputStream;
import java.lang.Thread.State;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

/**
 * This is an object that can convert images from a stream to a bitmap on a thread.
 *
 * Adapted from @author Jeremy Wadsack's ImageThreadLoader class
 */
public class ImageThreadLoader
{
	private final HashMap<String, SoftReference<Bitmap>> cache = new HashMap<String,  SoftReference<Bitmap>>();

	private final class QueueItem
	{
		public String attachment_id;
		public InputStream attachment;
		public ImageLoadedListener listener;
	}
	private final ArrayList<QueueItem> queue = new ArrayList<QueueItem>();

	private final Handler handler = new Handler();
	private Thread thread;
	private QueueRunner runner = new QueueRunner();

	/** Creates a new instance of the ImageThreadLoader */
	public ImageThreadLoader()
	{
		thread = new Thread(runner);
	}

	public interface ImageLoadedListener
	{
		public void imageLoaded(Bitmap imageBitmap );
	}

	private class QueueRunner implements Runnable
	{
		public void run()
		{
			synchronized(this)
			{
				while(queue.size() > 0)
				{
					final QueueItem item = queue.remove(0);

					// If in the cache, return that copy and be done
					if( cache.containsKey(item.attachment_id) && cache.get(item.attachment_id) != null)
					{
						// Use a handler to get back onto the UI thread for the update
						handler.post(new Runnable()
						{
							public void run()
							{
								if( item.listener != null )
								{
									SoftReference<Bitmap> ref = cache.get(item.attachment_id);
									if( ref != null )
									{
										item.listener.imageLoaded(ref.get());
									}
								}
							}
						});
					}
					else
					{
						final Bitmap bmp = BitmapFactory.decodeStream(item.attachment);
						if( bmp != null )
						{
							cache.put(item.attachment_id, new SoftReference<Bitmap>(bmp));

							// Use a handler to get back onto the UI thread for the update
							handler.post(new Runnable()
							{
								public void run()
								{
									if( item.listener != null )
									{
										item.listener.imageLoaded(bmp);
									}
								}
							});
						}
					}
				}
			}
		}
	}

	public Bitmap loadImage( final String attachment_id, final InputStream attachment, final ImageLoadedListener listener) throws MalformedURLException
	{
		// If it's in the cache, just get it and quit it
		if( cache.containsKey(attachment_id)) 
		{
			SoftReference<Bitmap> ref = cache.get(attachment_id);
			if( ref != null )
			{
				return ref.get();
			}
		}

		QueueItem item = new QueueItem();
		item.attachment_id = attachment_id;
		item.attachment = attachment;
		item.listener = listener;
		queue.add(item);

		// start the thread if needed
		if( thread.getState() == State.NEW)
		{
			thread.start();
		}
		else if( thread.getState() == State.TERMINATED)
		{
			thread = new Thread(runner);
			thread.start();
		}
		return null;
	}
}