package and.com.comicoid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dell on 29-05-2017.
 */

public class MarvelDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "marvel.db";
    private static final int DATABASE_VERSION = 1;

    public MarvelDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MarvelContract.MarvelEntry.TABLE_NAME + " (" +
                MarvelContract.MarvelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MarvelContract.MarvelEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MarvelContract.MarvelEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MarvelContract.MarvelEntry.COLUMN_IMAGE + " TEXT, " +
                MarvelContract.MarvelEntry.COLUMN_DETAIL + " TEXT);";
        Log.v(MarvelDbHelper.class.getSimpleName(),"onCreate Databse"+ SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MarvelContract.MarvelEntry.TABLE_NAME);
        onCreate(db);
    }
}
