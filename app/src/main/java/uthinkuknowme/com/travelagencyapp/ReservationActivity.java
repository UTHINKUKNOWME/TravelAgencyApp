package uthinkuknowme.com.travelagencyapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import uthinkuknowme.com.travelagencyapp.Utils.SendMail;

public class ReservationActivity extends AppCompatActivity {

    private TextView introTv,nameTv,surnameTv,numPassengersTv,addressTv,birthDateTv,mobileNumTv,emailTv;
    private EditText editTextName,editTextSurname,editTextNumberPassengers,editTextAddress,editTextBirthDate,editTextMobileNum,editTextEmail,additionalInfo;
    private Button reservation;
    private Intent intent;
    private Toolbar myToolbar;
    private RelativeLayout relativeLayout;
    String destination,date,agency;
    String[] TO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_reservation);

        introTv = (TextView) findViewById(R.id.introTv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        surnameTv = (TextView) findViewById(R.id.surnameTv);
        numPassengersTv = (TextView) findViewById(R.id.numPassengersTv);
        addressTv = (TextView) findViewById(R.id.addressTv);
        birthDateTv = (TextView) findViewById(R.id.birthDateTv);
        mobileNumTv = (TextView) findViewById(R.id.mobileNumTv);
        emailTv = (TextView) findViewById(R.id.emailTv);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        editTextNumberPassengers = (EditText) findViewById(R.id.editTextNumberPassengers);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextBirthDate = (EditText) findViewById(R.id.editTextBirthDate);
        editTextMobileNum = (EditText) findViewById(R.id.editTextMobileNum);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        additionalInfo = (EditText) findViewById(R.id.additionalInfo);

        reservation = (Button) findViewById(R.id.buttonReservation);

        myToolbar = (Toolbar) findViewById(R.id.toolbarRes);

        intent = getIntent();
        destination = intent.getStringExtra("destination");
        date = intent.getStringExtra("date");
        agency = intent.getStringExtra("agency");

        if(destination.equalsIgnoreCase("london")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.london1));
        }
        else if(destination.equalsIgnoreCase("madrid")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.madrid1));
        }
        else if(destination.equalsIgnoreCase("paris")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.paris1));
        }


        //Log.d("Destiantion : " ,destination);
        //Log.d("Date : ",date);

        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle(destination + " - " + date);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setSubtitleTextColor(Color.WHITE);
        getSupportActionBar().setSubtitle("Traveling with " + agency);

        TO = new String[]{"feeelinger@gmail.com",editTextEmail.getText().toString()};

        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bodyTextAgency = sendNoticeToAgency("agency");
                String bodyTextClient = sendNoticeToAgency("client");

                SendMail smAgency = new SendMail(ReservationActivity.this,"antonio.katarov@yahoo.com","New reservation",bodyTextAgency);
                SendMail smClient = new SendMail(ReservationActivity.this,editTextEmail.getText().toString().trim(),"Reservation confirmation" ,bodyTextClient);

                smAgency.execute();
                smClient.execute();

            }
        });


    }

    @NonNull
    private String sendNoticeToAgency(String toWho) {

        StringBuilder sb = new StringBuilder();

        if(toWho.equalsIgnoreCase("agency")) {
            sb.append("There is a new reservation for the trip to " + destination + " on " + date);
            sb.append("\n");
            sb.append("\n");
            sb.append("Name : " + editTextName.getText().toString());
            sb.append("\n");
            sb.append("\n");
            sb.append("Surname : " + editTextSurname.getText().toString());
            sb.append("\n");
            sb.append("\n");
            sb.append("The address is : " + editTextAddress.getText().toString());
            sb.append("\n");
            sb.append("\n");
            sb.append("Birth date : " + editTextBirthDate.getText().toString());
            sb.append("\n");
            sb.append("\n");
            sb.append("Wants to make a reservation for " + editTextNumberPassengers.getText().toString() + ".");
            sb.append("\n");
            sb.append("\n");
            sb.append("The client mobile number is " + editTextMobileNum.getText().toString()+ ".");
            sb.append("\n");
            sb.append("\n");
            sb.append("And clients email is " + editTextEmail.getText().toString()+ ".");
        }else {
            sb.append("This is a confirmation mail for your reservation for the trip to " + destination + " on " + date);
            sb.append("\n");
            sb.append("\n");
            sb.append("For furthermore information about the the trip we will contact you through the info you provided");
            sb.append("\n");
            sb.append("\n");
            sb.append("Thank you for traveling with " + agency);
        }

        return sb.toString();
    }

}

