package uthinkuknowme.com.travelagencyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {

    public static String GlobalUser;

    private EditText username;
    private EditText password;

    private Button loginButton;

    private Volley volley;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.LoginUserNameEditText);
        password = findViewById(R.id.LoginPassEditText);

        loginButton = findViewById(R.id.LoginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkCredentials();
//                Toast.makeText(getApplicationContext(),"Successful login !",Toast.LENGTH_LONG).show();
//                Intent goToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(goToMainActivity);
//                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });


        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        editor = pref.edit();

    }


    private void checkCredentials(){


        if(username.getText().toString().equals("") || password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the fields !",Toast.LENGTH_LONG).show();
        } else {
            RequestQueue queue = volley.newRequestQueue(this);
//            String url = "http://isturagen.azurewebsites.net/Service1.svc/Login/timtimov/12345slo";
            String url = "http://isturagen.azurewebsites.net/Service1.svc/Login/" + username.getText().toString() + "/" + password.getText().toString();
            Log.d("toni",url);

//            if (pref.contains("username")) {
//                Log.d("toni contains","YEEE");
//            }
            StringRequest stringRequest =  new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
//                            mTextView.setText("Response is: "+ response.substring(0,500));
                            Log.d("toni", response);
                            if (response.equals("0")){
                                hideKeyboard();
                                Toast.makeText(getApplicationContext(),"Unsuccessful login !",Toast.LENGTH_LONG).show();

                            } else {
                                hideKeyboard();
                                Toast.makeText(getApplicationContext(),"Successful login !",Toast.LENGTH_LONG).show();
                                editor.putString("username", username.getText().toString());
                                editor.commit();
                                Intent goToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(goToMainActivity);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("toni",error.toString());

                }
            });
            queue.add(stringRequest);
        }

    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
