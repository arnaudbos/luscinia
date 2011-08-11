package uk.ac.brookes.arnaudbos.luscinia.widget;

import uk.ac.brookes.arnaudbos.luscinia.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * View representing a Luscinia document, with an icon and a title
 * @author arnaudbos
 */
public class DocumentView extends RelativeLayout
{
    private LayoutInflater inflater;
    private RelativeLayout documentView;
    private ImageView documentPicture;
    private TextView documentText;

    /**
     * Create a new DocumentView
     * @param context The context to create the view
     * @param icon The icon that will be displayed
     * @param text The title of the document
     * @param listener The listener that will be used when the DocumentView is clicked
     */
	public DocumentView(Context context, Drawable icon, String text, OnClickListener listener)
	{
        super(context);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        documentView = (RelativeLayout) inflater.inflate(R.layout.folder_document_item, null);
        addView(documentView);

        documentPicture = (ImageView) documentView.findViewById(R.id.document_picture);
        documentPicture.setImageDrawable(icon);

        documentText = (TextView) documentView.findViewById(R.id.document_name);
        documentText.setText(text);
        
        if(listener!=null)
        {
        	documentView.setOnClickListener(listener);
        }
    }
}
