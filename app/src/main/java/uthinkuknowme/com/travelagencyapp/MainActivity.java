package uthinkuknowme.com.travelagencyapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {

    private int mYear;
    private int mMonth;
    private int mDay;

    private Button mPickDate, searchOffers;

    static final int DATE_DIALOG_ID = 0;


    MaterialSpinner spinner, spinner1;

    private String destinatoinSearch, agencySearch;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }


        searchOffers = (Button) findViewById(R.id.searchOffers);
        searchOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goSearch = new Intent(MainActivity.this, OffersActivity.class);
                goSearch.putExtra("agency", agencySearch);
                goSearch.putExtra("destination", destinatoinSearch);
                int monthSearch = mMonth + 1;
                String from = mYear + "-" + monthSearch + "-" + mDay;
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                String reformattedStr = "";
                try {
                    reformattedStr = myFormat.format(myFormat.parse(from));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                goSearch.putExtra("date", reformattedStr);
                startActivity(goSearch);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mPickDate = (Button) findViewById(R.id.mDatePicker);

        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDisplay();


        spinner = (MaterialSpinner) findViewById(R.id.spinner1);
        spinner.setItems("Any agency", "SupraTravel", "TonyTravel", "Exploring", "Safari", "DoryTravel");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                agencySearch = spinner.getText().toString();
                //Toast.makeText(MainActivity.this, "U picked " + text, Toast.LENGTH_SHORT).show();
            }
        });


        spinner1 = (MaterialSpinner) findViewById(R.id.spinner2);
        spinner1.setItems("Any destination", "London", "Paris", "Moscow", "Basel", "Salzburg", "Vienna", "Berlin", "Barcelona", "Madrid", "Venice");
        spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                destinatoinSearch = spinner1.getText().toString();
                //Toast.makeText(MainActivity.this, "U picked " + text1, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateDisplay() {
        this.mPickDate.setText(
                new StringBuilder()
                        .append("Date: ")
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(MainActivity.this, "JBG", Toast.LENGTH_SHORT)
                            .show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                // other 'case' lines to check for other
                // permissions this app might request
        }
    }

}
