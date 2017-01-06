package uthinkuknowme.com.travelagencyapp.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Antonio on 26.12.2016.
 */

public class DBHelperOffers extends SQLiteAssetHelper {


    //private static String DB_PATH = "/data/data/uthinkuknowme.com.travelagencyapp/databases/";
    final static String DB_NAME = "offers.db";
    //public static SQLiteDatabase database;
    public final static int DB_VERSION = 1;

    private final Context context;

    public static String ID;

    public DBHelperOffers(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.context = context;
    }

    // Create table names and fields
    public static final String TABLE_OFFERS       = "tbl_offers";
    public static final String OFFER_ID           = "_id";
    public static final String DESTINATION        = "destination";
    public static final String PROMOTION_TXT      = "promotion_text";
    public static final String AGENCY             = "agency";
    public static final String DATE               = "date_offer";
    public static final String DETAILS            = "details_text";

    //public static final String[] AllColumns = {OFFER_ID,DESTINATION,PROMOTION_TXT,AGENCY,DATE};
    public static final String[] AllColumns = {OFFER_ID,DESTINATION,PROMOTION_TXT,AGENCY,DATE,DETAILS};

//    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_OFFERS + " (" + OFFER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + DESTINATION + " TEXT, " + PROMOTION_TXT + " TEXT, " + AGENCY + " TEXT, " + DATE + " TEXT, " + DETAILS + " TEXT " + ")";
//
////    @Override
////    public void onCreate(SQLiteDatabase sqLiteDatabase) {
////        sqLiteDatabase.execSQL(TABLE_CREATE);
////    }
////
////    @Override
////    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
////        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERS);
////        onCreate(sqLiteDatabase);
////    }
}
