package uthinkuknowme.com.travelagencyapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import uthinkuknowme.com.travelagencyapp.R;
import uthinkuknowme.com.travelagencyapp.Utils.DBHelperOffers;

/**
 * Created by Antonio on 08.12.2016.
 */

public class OffersAdapter extends CursorAdapter{

    private LayoutInflater cursorInflater;

    private ImageView image;
    private TextView destination;
    private TextView promotionText;
    private TextView dateText;
    private TextView agencyText;

    public TextView getDestination() {
        return destination;
    }

    public void setDestination(TextView destination) {
        this.destination = destination;
    }

    public OffersAdapter(Context context, Cursor c) {
        super(context, c);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.offers_adapter,parent,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        image = (ImageView) view.findViewById(R.id.offerImage);
        destination = (TextView) view.findViewById(R.id.offerDestination);
        promotionText = (TextView) view.findViewById(R.id.offerPrice);
        agencyText = (TextView) view.findViewById(R.id.offerAgency);
        dateText = (TextView) view.findViewById(R.id.offerDate);

        String dest = cursor.getString(cursor.getColumnIndex(DBHelperOffers.DESTINATION));
        if(dest.equalsIgnoreCase("london")) {
            image.setImageResource(R.drawable.london1);
        }
        else if(dest.equalsIgnoreCase("paris")){
            image.setImageResource(R.drawable.paris1);
        }
        else if(dest.equalsIgnoreCase("madrid")){
            image.setImageResource(R.drawable.madrid1);

        }

        String promoText = cursor.getString(cursor.getColumnIndex(DBHelperOffers.PROMOTION_TXT));
        String agency = cursor.getString(cursor.getColumnIndex(DBHelperOffers.AGENCY));
        String date = cursor.getString(cursor.getColumnIndex(DBHelperOffers.DATE));
        long brojce = cursor.getLong(cursor.getColumnIndex(DBHelperOffers.DATE));
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy");
        String reformattedStr = "";
        try {
            reformattedStr = myFormat.format(fromUser.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        destination.setText(dest);
        promotionText.setText(promoText);
        agencyText.setText(agency);
        dateText.setText(reformattedStr);

    }
}
