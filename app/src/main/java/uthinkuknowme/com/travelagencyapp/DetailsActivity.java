package uthinkuknowme.com.travelagencyapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Random;

import uthinkuknowme.com.travelagencyapp.adapters.CustomPagerAdapter;
import uthinkuknowme.com.travelagencyapp.adapters.OffersAdapter;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private CustomPagerAdapter mCustomPagerAdapter;
    private TextView detailsTxt, revAuthorName, revText;
    private ImageView revImage;
    private Toolbar myToolbar;
    private ProgressDialog progressDialog;
    private SharedPreferences pref;

    public static int[] mResources = new int[2];
    private String temp;

    String reqDetailsURL;
    Bitmap img;
    String reformattedStr, destination, agency, agencyReservation, destinationBack, resID;

    static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;

    private static final String[] LondonPlaces = {"ChIJmZuNDMQEdkgRfB9O9456eQc", "ChIJh7wHoqwEdkgR3l-vqQE1HTo", "ChIJgZ24Us4adkgRpDNAwNPO_SY", "ChIJNa8fzlADdkgR3DtGhPk34uk", "ChIJ2dGMjMMEdkgRqVqkuXQkj7c", "ChIJSdtli0MDdkgRLW9aCBpCeJ4", "ChIJc2nSALkEdkgRkuoJJBfzkUI"};
    private static final String[] MadridPlaces = {"ChIJv-yiGoMoQg0Rj1LLgnhKk1o", "ChIJWRLqi24oQg0RqwZ3SsbZah0", "ChIJJ1KGSpooQg0R8YZKFDqLJ5g", "ChIJD_2btoQoQg0RwWUT0PZzK8E", "ChIJcTgMfn4oQg0ResF18FJc7yE"};
    private static final String[] ParisPlaces = {"ChIJATr1n-Fx5kcRjQb6q6cdQDY", "ChIJe2jeNttx5kcRi_mJsGHdkQc", "ChIJLU7jZClu5kcR4PcOOO6p3I0", "ChIJjx37cOxv5kcRPWQuEW5ntdk", "ChIJ442GNENu5kcRGYUrvgqHw88", "ChIJdUyx15R95kcRj85ZX8H8OAU"};
    //private static final String[] LondonPlaces = {""};


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Log.d("toni USERNAME", pref.getString("username", null));
        mCustomPagerAdapter = new CustomPagerAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        detailsTxt = (TextView) findViewById(R.id.detailsText);
        revAuthorName = (TextView) findViewById(R.id.RauthorName1);
        revText = (TextView) findViewById(R.id.Rtext1);
        revImage = (ImageView) findViewById(R.id.Rimage);


        Intent intent = getIntent();

        destination = intent.getStringExtra("destination");
//        destinationBack = intent.getStringExtra("destinationBack");
//        String detailsText = intent.getStringExtra("details");
        final String dateShow = intent.getStringExtra("date");
        agency = intent.getStringExtra("agency");
        final String dateSearch = intent.getStringExtra("dateSearch");
        agencyReservation = intent.getStringExtra("agencyReservation");
        resID = intent.getStringExtra("id");
//        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy");
//
//        reformattedStr = "";
//        try {
//            reformattedStr = myFormat.format(fromUser.parse(dateShow));
//        } catch (ParseException e1) {
//            e1.printStackTrace();
//        }

        setSupportActionBar(myToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(destination + " - " + dateShow);
        myToolbar.setTitleTextColor(Color.WHITE);

//        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent goBack = new Intent(DetailsActivity.this, OffersActivity.class);
//                goBack.putExtra("destination", destinationBack);
//                goBack.putExtra("date", dateSearch);
//                goBack.putExtra("agency", agency);
//                startActivity(goBack);
//            }
//        });


        final Random random = new Random();
        int n;

        if (destination.equalsIgnoreCase("london")) {
            mResources = new int[]{
                    R.drawable.london_first,
                    R.drawable.london_second,
            };

            n = random.nextInt(LondonPlaces.length);
            reqDetailsURL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + LondonPlaces[n] + "&key=AIzaSyDSh4vfDCJIRQWOo9w5ZXhLTHaPjahASdw";
        } else if (destination.equalsIgnoreCase("madrid")) {
            mResources = new int[]{
                    R.drawable.madrid_first,
                    R.drawable.madrid_second,
            };
            n = random.nextInt(MadridPlaces.length);
            reqDetailsURL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + MadridPlaces[n] + "&key=AIzaSyDSh4vfDCJIRQWOo9w5ZXhLTHaPjahASdw";

        } else if (destination.equalsIgnoreCase("paris")) {
            mResources = new int[]{
                    R.drawable.paris_first,
                    R.drawable.paris_second,
            };
            n = random.nextInt(ParisPlaces.length);
            reqDetailsURL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + ParisPlaces[n] + "&key=AIzaSyDSh4vfDCJIRQWOo9w5ZXhLTHaPjahASdw";
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int item_count = 0;

            @Override
            public void run() {
                mViewPager.setCurrentItem(item_count);
                if (item_count == mResources.length) {
                    item_count = 0;
                } else {
                    item_count++;
                }
                handler.postDelayed(this, 3000);
            }
        }, 3000);

//        detailsTxt.setText(detailsText);


        new ObtainReview().execute();
        revAuthorName.setVisibility(View.VISIBLE);
        revText.setVisibility(View.VISIBLE);

    }

    private void obtainReview() {

        JsonObjectRequest reviewReq = new JsonObjectRequest(Request.Method.GET, reqDetailsURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Random random = new Random();
                try {
                    JSONObject result = response.getJSONObject("result");
                    String thePlace = result.getString("name");
                    JSONArray reviewss = result.getJSONArray("reviews");
                    JSONObject review1 = reviewss.getJSONObject(random.nextInt(5));
                    String authorName1 = review1.getString("author_name");
                    String text1 = review1.getString("text");
                    JSONArray photos = result.getJSONArray("photos");
                    JSONObject photo1 = photos.getJSONObject(0);
                    temp = photo1.getString("photo_reference");

                    final String imageUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + temp + "&key=AIzaSyDSh4vfDCJIRQWOo9w5ZXhLTHaPjahASdw";

                    new Thread(new Runnable() {
                        public void run() {
                            URL url = null;
                            try {
                                url = new URL(imageUrl);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            HttpURLConnection connection = null;
                            try {
                                connection = (HttpURLConnection) url.openConnection();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            InputStream is = null;
                            try {
                                is = connection.getInputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            img = BitmapFactory.decodeStream(is);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    revImage.setImageBitmap(img);
                                }
                            });

                        }
                    }).start();


                    revAuthorName.setText(authorName1 + "`s review on " + thePlace);
                    revText.setText(text1);

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(reviewReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.make_rezervation) {

            // Handle the click on rez icon

                new Reservation().execute();
//            Intent goReservation = new Intent(DetailsActivity.this, ReservationActivity.class);
//            goReservation.putExtra("destination", destination);
//            goReservation.putExtra("date", reformattedStr);
//            goReservation.putExtra("agency", agencyReservation);
//            startActivity(goReservation);
            //Toast.makeText(this,"U clicked on rezervation",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ObtainReview extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DetailsActivity.this, "Opening offer...", "Please wait...", false, false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            obtainReview();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            revAuthorName.setVisibility(View.VISIBLE);
            revText.setVisibility(View.VISIBLE);
        }
    }

    public class Reservation extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Log.d("toni", resID);
                URL url = new URL("http://isturagen.azurewebsites.net/Service1.svc/Resev"); //Enter URL here
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                httpURLConnection.connect();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", pref.getString("username", null));
                jsonObject.put("offersId", resID);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(jsonObject.toString());
                wr.flush();
                wr.close();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Successful reservation !",Toast.LENGTH_LONG).show();
        }
    }


}

