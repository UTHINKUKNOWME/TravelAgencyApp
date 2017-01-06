package uthinkuknowme.com.travelagencyapp.Utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Antonio on 26.12.2016.
 */

public class OffersProvider extends ContentProvider {

    private static final String AUTHORITY = "com.uthinkuknowme.com.travelagencyapp.offersprovider";
    private static final String BASE_PATH = "tbl_offers";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // Constant to identify the req operation
    private static final int OFFERS = 1;
    private static final int OFFERS_ID = 2;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH,OFFERS);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",OFFERS_ID);

    }

    private DBHelperOffers helperOffers;
    private SQLiteDatabase database;


    @Override
    public boolean onCreate() {

        helperOffers = new DBHelperOffers(getContext());
        database = helperOffers.getWritableDatabase();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return database.query(DBHelperOffers.TABLE_OFFERS,DBHelperOffers.AllColumns,s,strings1,null,null, DBHelperOffers.DATE + " ASC");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = database.insert(DBHelperOffers.TABLE_OFFERS,null,contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return database.delete(DBHelperOffers.TABLE_OFFERS,s,strings);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return database.update(DBHelperOffers.TABLE_OFFERS,contentValues,s,strings);
    }
}
