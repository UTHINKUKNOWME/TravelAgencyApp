package uthinkuknowme.com.travelagencyapp.adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

import uthinkuknowme.com.travelagencyapp.R;
import uthinkuknowme.com.travelagencyapp.Utils.Offer;

/**
 * Created by Antonio on 08.12.2016.
 */

public class OffersAdapter extends ArrayAdapter {

    private LayoutInflater cursorInflater;
    private Context context;

    private ImageView image;
    private TextView destination;
    private TextView promotionText;
    private TextView dateText;
    private TextView agencyText;

    private ArrayList<Offer> offers;

    public TextView getDestination() {
        return destination;
    }

    public void setDestination(TextView destination) {
        this.destination = destination;
    }

    public OffersAdapter(Context context, ArrayList<Offer> offers) {
        super(context, -1, offers);
        this.context = context;
        this.offers = offers;

    }

//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return cursorInflater.inflate(R.layout.offers_adapter,parent,false);
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.offers_adapter,parent,false);

        image = (ImageView) view.findViewById(R.id.offerImage);
        destination = (TextView) view.findViewById(R.id.offerDestination);
        promotionText = (TextView) view.findViewById(R.id.offerPrice);
        agencyText = (TextView) view.findViewById(R.id.offerAgency);
        dateText = (TextView) view.findViewById(R.id.offerDate);

        Offer offer = offers.get(position);

        String dest = offer.getDest();

        if(dest.equalsIgnoreCase("london")) {
            image.setImageResource(R.drawable.london1);
        }
        else if(dest.equalsIgnoreCase("paris")){
            image.setImageResource(R.drawable.paris1);
        }
        else if(dest.equalsIgnoreCase("madrid")){
            image.setImageResource(R.drawable.madrid1);

        }

//
        String date = offer.getDate();
        SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy");
        String reformattedStr = "";
        try {
            reformattedStr = myFormat.format(fromUser.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        destination.setText(offer.getDest());
        promotionText.setText(offer.getPrice() + " " + offer.getPromoText());
        agencyText.setText(offer.getAgency());
        dateText.setText(reformattedStr);


        return  view;
        }



//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//
//        image = (ImageView) view.findViewById(R.id.offerImage);
//        destination = (TextView) view.findViewById(R.id.offerDestination);
//        promotionText = (TextView) view.findViewById(R.id.offerPrice);
//        agencyText = (TextView) view.findViewById(R.id.offerAgency);
//        dateText = (TextView) view.findViewById(R.id.offerDate);
//
//        String dest = cursor.getString(cursor.getColumnIndex(DBHelperOffers.DESTINATION));
//
//        if(dest.equalsIgnoreCase("london")) {
//            image.setImageResource(R.drawable.london1);
//        }
//        else if(dest.equalsIgnoreCase("paris")){
//            image.setImageResource(R.drawable.paris1);
//        }
//        else if(dest.equalsIgnoreCase("madrid")){
//            image.setImageResource(R.drawable.madrid1);
//
//        }
//
//        String promoText = cursor.getString(cursor.getColumnIndex(DBHelperOffers.PROMOTION_TXT));
//        String agency = cursor.getString(cursor.getColumnIndex(DBHelperOffers.AGENCY));
//        String date = cursor.getString(cursor.getColumnIndex(DBHelperOffers.DATE));
//        long brojce = cursor.getLong(cursor.getColumnIndex(DBHelperOffers.DATE));
//
//        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy");
//        String reformattedStr = "";
//        try {
//            reformattedStr = myFormat.format(fromUser.parse(date));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        destination.setText(dest);
//        promotionText.setText(promoText);
//        agencyText.setText(agency);
//        dateText.setText(reformattedStr);
//
//    }
}
