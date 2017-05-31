package and.com.comicoid.widget;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;

import and.com.comicoid.R;
import and.com.comicoid.data.MarvelContract;

/**
 * Created by dell on 31-05-2017.
 */

public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor cursor;
    private Bitmap bitmap;


    public MyWidgetRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        cursor = mContext.getContentResolver().query(MarvelContract.MarvelEntry.CONTENT_URI, null, MarvelContract.MarvelEntry.COLUMN_ID, null, null);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.marvel_widget_item);
        cursor.moveToPosition(position);

        Uri imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex(MarvelContract.MarvelEntry.COLUMN_IMAGE)));

        try {
            Bitmap bitmap = Picasso.with(mContext).load(imageUri).get();
            rv.setImageViewBitmap(R.id.thumbnail, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}