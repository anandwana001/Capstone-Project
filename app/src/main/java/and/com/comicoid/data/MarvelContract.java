package and.com.comicoid.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dell on 29-05-2017.
 */

public class MarvelContract {

    public static final String AUTHORITY = "and.com.android.comicoid";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASK = "marvel";

    public static final class MarvelEntry implements BaseColumns {
        public static final Uri CONTENT_URI=
                BASE_URI.buildUpon().appendPath(PATH_TASK).build();

        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "marvel";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_DETAIL = "description";
    }
}
