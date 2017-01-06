package uthinkuknowme.com.travelagencyapp.Utils;

/**
 * Created by Antonio on 04.1.2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import uthinkuknowme.com.travelagencyapp.MainActivity;

/**
 * Created by Antonio on 04/01/2017.
 */


public class SendMail extends AsyncTask<Void,Void,Void> {


    private Context context;
    private Session session;

    //Information to send email
    private String email;
    private String subject;
    private String message;


    private ProgressDialog progressDialog;


    public SendMail(Context context, String email, String subject, String message){

        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(context,"Making reservation","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Reservation has been successful",Toast.LENGTH_LONG).show();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ie) {
//            ie.printStackTrace();
//        }
        Intent goHome = new Intent(context,MainActivity.class);
        //context.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        context.startActivity(goHome);

    }

    @Override
    protected Void doInBackground(Void... params) {

        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailConfig.EMAIL, mailConfig.PASSWORD);
                    }
                });

        try {

            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(mailConfig.EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(message);

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
