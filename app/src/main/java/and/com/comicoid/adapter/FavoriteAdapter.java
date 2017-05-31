package and.com.comicoid.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import and.com.comicoid.R;
import and.com.comicoid.data.MarvelContract;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 31-05-2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private Cursor cursor;
    private Context context;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(!cursor.moveToPosition(position))
            return;

        holder.title.setText(cursor.getString(cursor.getColumnIndex(MarvelContract.MarvelEntry.COLUMN_TITLE)));

        Glide.with(context).load(cursor.getString(cursor.getColumnIndex(MarvelContract.MarvelEntry.COLUMN_IMAGE)))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (cursor == c) {
            return null;
        }
        Cursor temp = cursor;
        this.cursor = c;
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.share_button)
        Button shareButton;
        @BindView(R.id.like_button)
        Button likeButton;
        @BindView(R.id.download_button)
        Button downloadButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "avenger.ttf");
            title.setTypeface(custom_font);
        }
    }
}
