package and.com.comicoid.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import and.com.comicoid.R;
import and.com.comicoid.activity.MainActivity;
import and.com.comicoid.data.MarvelContract;
import and.com.comicoid.fragment.SlideshowDialogFragment;
import and.com.comicoid.model.Image;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 10-10-2016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private List<Image> images;
    private Context mContext;
    private Bitmap bitmap;

    public GalleryAdapter(Context mContext, List<Image> images) {
        this.mContext = mContext;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Image image = images.get(position);

        holder.title.setText(image.getName());

        Glide.with(mContext).load(image.getThumbnail())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("imagesList", (ArrayList<? extends Parcelable>) images);
                bundle.putInt("position", position);

                FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        });

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, image.getName() + "\n" + image.getThumbnail() + "\n" + image.getDescription());
                sendIntent.setType("text/plain");
                mContext.startActivity(Intent.createChooser(sendIntent, "Send to..."));
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkfav(image.getId())) {
                    removeFav(image.getId());
                    Toast.makeText(mContext, "Remove From Favourite List", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues testValues = new ContentValues();
                    testValues.put(MarvelContract.MarvelEntry.COLUMN_ID, image.getId());
                    testValues.put(MarvelContract.MarvelEntry.COLUMN_TITLE, image.getName());
                    testValues.put(MarvelContract.MarvelEntry.COLUMN_IMAGE, image.getThumbnail());
                    testValues.put(MarvelContract.MarvelEntry.COLUMN_DETAIL, image.getDescription());
                    Uri uri = mContext.getContentResolver().insert(MarvelContract.MarvelEntry.CONTENT_URI, testValues);
                    if (uri != null) {
                        Toast.makeText(mContext, "Added to Favourite List", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL imageurl = null;
                try {
                    imageurl = new URL(image.getThumbnail());

                    Glide
                            .with(mContext)
                            .load(images.get(position).getThumbnail())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>(100,100) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    saveImage(bitmap,position);
                                }
                            });

                  /*  new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Looper.prepare();
                            try {
                                bitmap = Glide.
                                        with(mContext).
                                        load(images.get(position).getThumbnail()).
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
                                Log.v(mContext.getClass().getSimpleName(),"image bitmap = "+bitmap.toString());
                                saveImage(bitmap,position);
                            };
                        }
                    }.execute();*/

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            });
    }

    private void removeFav(long id) {
        int number = mContext.getContentResolver().delete(MarvelContract.MarvelEntry.CONTENT_URI, String.valueOf(id), null);
    }

    private boolean chkfav(int id) {
        Cursor cursor = mContext.getContentResolver().query(MarvelContract.MarvelEntry.CONTENT_URI, null, MarvelContract.MarvelEntry.COLUMN_ID + " = " + id, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private String saveImage(Bitmap image, int position) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + images.get(position).getName() + ".jpg";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/Comicoid");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(mContext, "savedMessage", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    @Override
    public int getItemCount() {
        return images.size();
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

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "avenger.ttf");
            title.setTypeface(custom_font);
        }
    }

}