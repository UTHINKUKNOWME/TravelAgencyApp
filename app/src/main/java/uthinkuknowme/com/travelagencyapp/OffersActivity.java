package uthinkuknowme.com.travelagencyapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import at.grabner.circleprogress.CircleProgressView;
import uthinkuknowme.com.travelagencyapp.Utils.Http;
import uthinkuknowme.com.travelagencyapp.Utils.Offer;
import uthinkuknowme.com.travelagencyapp.adapters.OffersAdapter;


@RequiresApi(api = Build.VERSION_CODES.N)
public class OffersActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {
    //LoaderManager.LoaderCallbacks<Cursor> {

    private ImageView mImageView;
    private Toolbar myToolbar;
    private View mListBackgroundView;
    private ObservableListView mListView;
    private int mParallaxImageHeight;
    //    private Cursor mCursor;
    private OffersAdapter mAdapter;
    private TextView emptyTextView;

    String mSelectionClause;
    String[] selectionArgs;
    String destinationSearch, agencySearch;
    String dateSearch, item;
    boolean setEmptyText;

    Intent goDetails;

    private Volley volley;

    private ArrayList<Offer> offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_offers);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        //getSupportLoaderManager().initLoader(0,null,this);


        offers = new ArrayList<>();

        mImageView = (ImageView) findViewById(R.id.image);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        mListView = (ObservableListView) findViewById(R.id.list);
        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        mListBackgroundView = findViewById(R.id.list_background);


        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        new LoadData().execute();
//        // Delete offers
//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long idPrefs) {
//                deleteOffer(idPrefs);
//                return true;
//            }
//        });


    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mListView.getCurrentScrollY(), false, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        myToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, -scrollY / 2);

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mParallaxImageHeight));

    }


    @Override
    public void onDownMotionEvent() {

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy");
        String reformattedStr = "";
        try {
            reformattedStr = myFormat.format(fromUser.parse(dateSearch));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        if (destinationSearch != null) {
            getSupportActionBar().setTitle("Organized trips to: " + destinationSearch);
            myToolbar.setSubtitle("Trips after:  " + reformattedStr);
        } else {
            getSupportActionBar().setTitle("Trips to all destinations");
            myToolbar.setSubtitle("Trips after: " + reformattedStr);

        }
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {


    }
//
//    private void insertOffers(){
//
//
//    }
//
//    private void deleteOffer(final long id) {
//
//        Uri uri = Uri.parse(OffersProvider.CONTENT_URI + "/" + id);
//        getContentResolver().delete(OffersProvider.CONTENT_URI, DBHelperOffers.OFFER_ID + "=" + uri.getLastPathSegment(), null);
//        //restartLoader();
//    }


//
//private void restartLoader() {
//        getSupportLoaderManager().restartLoader(0,null,this);
//    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int idPrefs, Bundle args) {
//        return new CursorLoader(getApplicationContext(),OffersProvider.CONTENT_URI,DBHelperOffers.AllColumns,mSelectionClause,selectionArgs,null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mAdapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        mAdapter.swapCursor(null);
//    }

    public class EmptyListView extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(3500);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // When progress finished, open MainActivity
            Intent intentBack = new Intent(OffersActivity.this, MainActivity.class);
            startActivity(intentBack);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        }
    }

    private void setupListView() {

        Log.d("toni", "some shit");
        myToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        // Set padding view for ListView. This is the flexible space.
        View paddingView = new View(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mParallaxImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);
        mListView.addHeaderView(paddingView);
        mListView.setScrollViewCallbacks(this);

        Intent intent = getIntent();

        destinationSearch = intent.getStringExtra("destination");
        agencySearch = intent.getStringExtra("agency");
        dateSearch = intent.getStringExtra("date");
        Log.d("toni DATA", dateSearch);
        Http http = new Http();
        String url = "http://isturagen.azurewebsites.net/Service1.svc/Offers";
        String json = http.makeServiceCall(url);

        if (json != null) {
            try {
                JSONArray array = new JSONArray(json);
                Log.d("toni", array.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    String id = jsonObject.getString("id");
                    String price = jsonObject.getString("price");
                    String city = jsonObject.getString("city");
                    String agency = jsonObject.getString("agency");
                    String promo = jsonObject.getString("promo");
                    String dateString = jsonObject.getString("date");

                    Offer offer = new Offer(city, promo, dateString, agency, price, id);

                    if ((destinationSearch == null && agencySearch == null)) {

                        if (checkDate(offer)) {
                            offers.add(offer);
                        }

                    } else if (((destinationSearch != null && agencySearch != null)) && destinationSearch.equalsIgnoreCase("any destination") && agencySearch.equalsIgnoreCase("any agency")) {
                        if (checkDate(offer)) {
                            offers.add(offer);
                        }

                    } else if ((destinationSearch == null) || (destinationSearch.equalsIgnoreCase("any destination") && agencySearch != null && !(agencySearch.equalsIgnoreCase("any agency")))) {
                        if (checkAgency(offer)) {
                            offers.add(offer);
                        }

                    } else if ((agencySearch == null) || (agencySearch.equalsIgnoreCase("any agency")) && !destinationSearch.equalsIgnoreCase("Any destination")) {
                        if (checkDest(offer)) {
                            offers.add(offer);
                        }

                    } else if (((destinationSearch != null && agencySearch != null)) && !destinationSearch.equalsIgnoreCase("Any destination") && !agencySearch.equalsIgnoreCase("any agency")) {
                        if (checkDest(offer) && checkAgency(offer)) {
                            offers.add(offer);
                        }

                    } else if (((destinationSearch != null && agencySearch == null) && destinationSearch.equalsIgnoreCase("Any destination")) || ((destinationSearch == null && agencySearch != null) && agencySearch.equalsIgnoreCase("any agency"))) {
                        if (checkDate(offer)) {
                            offers.add(offer);
                        }
                    } else {
                        if (checkDate(offer)) {
                            offers.add(offer);
                        }
                    }

//                    offers.add(offer);

                    Log.d("toni", offer.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        RequestQueue queue = volley.newRequestQueue(this);

//
//        StringRequest stringRequest =  new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
////                            mTextView.setText("Response is: "+ response.substring(0,500));
//                        Log.d("toni", response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("toni",error.toString());
//
//            }
//        });
//        queue.add(stringRequest);


        //Set the header picture accordingly to the destination


//        // Since loading data from database is heavy-duty job, we will load the data in the Thread. If you do not explicitly start CursorAdapter
//        // in its own thread then it will run on the main (UI) thread which may be noticeable as jittery or slow to respond interface by your users.
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//
//        });


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mAdapter = new OffersAdapter(getApplicationContext(), offers);
                if (mAdapter.isEmpty()) {
                    mListView.setAdapter(mAdapter);
                    emptyTextView.setVisibility(View.VISIBLE);
//                    new EmptyListView().execute();
                } else {
                    mListView.setAdapter(mAdapter);
                    if (destinationSearch != null) {
                        if (destinationSearch.equalsIgnoreCase("london")) {
                            mImageView.setImageResource(R.drawable.london2);
                        } else if (destinationSearch.equalsIgnoreCase("madrid")) {
                            mImageView.setImageResource(R.drawable.madrid2);
                        } else if (destinationSearch.equalsIgnoreCase("paris")) {
                            mImageView.setImageResource(R.drawable.paris2);
                        }
                    }
                    mListView.setDividerHeight(0);

                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            goDetails = new Intent(OffersActivity.this, DetailsActivity.class);

                            String date = offers.get(i-1).getDate();
                            SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
                            SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy");
                            String reformattedStr = "";
                            try {
                                reformattedStr = myFormat.format(fromUser.parse(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            goDetails.putExtra("id", offers.get(i-1).getId());
                            goDetails.putExtra("destination", offers.get(i-1).getDest());
                            goDetails.putExtra("date", reformattedStr);
                            goDetails.putExtra("dateSearch", dateSearch);
                            goDetails.putExtra("agency", agencySearch);
                            goDetails.putExtra("destinationBack", destinationSearch);
                            startActivity(goDetails);

                        }
                    });
                }
            }


        });

    }

    boolean checkDate(Offer offer) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat offerDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        Date strDate = null;
        Date oDate = null;
        try {
            strDate = sdf.parse(dateSearch);
            oDate = offerDate.parse(offer.getDate());
            Log.d("toni", strDate.toString());
            Log.d("toni", oDate.toString());
            if (strDate.before(oDate)) {
//                offers.add(offer);
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean checkAgency(Offer offer) {
        if (offer.getAgency().equals(agencySearch) && checkDate(offer)) {
            return true;
//            offers.add(offer);
        }
        return false;
    }

    boolean checkDest(Offer offer) {
        if (offer.getDest().equals(destinationSearch) && checkDate(offer)) {
//            offers.add(offer);
            return true;
        }
        return false;
    }

    public class LoadData extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            try {
//                Thread.sleep(1500);
//            } catch (InterruptedException ie) {
//                ie.printStackTrace();
//            }
//            myToolbar.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
//            mImageView.setVisibility(View.GONE);
//            emptyTextView.setVisibility(View.GONE);

            progressDialog = ProgressDialog.show(OffersActivity.this, "Searching for organized trips", "Please wait...", false, false);
            // Since loading data from database is heavy-duty job, we will load the data in the Thread. If you do not explicitly start CursorAdapter
            // in its own thread then it will run on the main (UI) thread which may be noticeable as jittery or slow to respond interface by your users.


        }

        @Override
        protected Void doInBackground(Void... voids) {

            setupListView();
            try {
                Thread.sleep(3000);

            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            myToolbar.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.VISIBLE);
            if (setEmptyText) {
                emptyTextView.setVisibility(View.VISIBLE);
            }
            progressDialog.dismiss();
        }
    }


}