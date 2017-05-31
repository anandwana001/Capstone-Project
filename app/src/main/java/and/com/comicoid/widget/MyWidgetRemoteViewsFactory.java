package and.com.comicoid.widget;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

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

        Glide
                .with(mContext)
                .load(cursor.getString(cursor.getColumnIndex(MarvelContract.MarvelEntry.COLUMN_IMAGE)))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(100,100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        rv.setImageViewBitmap(R.id.thumbnail, bitmap);
                    }
                });

      /*  new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Looper.prepare();
                try {
                   bitmap = Glide.
                            with(mContext).
                            load(cursor.getString(cursor.getColumnIndex(MarvelContract.MarvelEntry.COLUMN_IMAGE))).
                            asBitmap().
                            into(-1,-1).
                            get();
                } catch (final ExecutionException e) {
                    Log.e(TAG, e.getMessage());
                } catch (final InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void dummy) {
                if (null != bitmap) {
                    rv.setImageViewBitmap(R.id.thumbnail, bitmap);
                };
            }
        }.execute();*/

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