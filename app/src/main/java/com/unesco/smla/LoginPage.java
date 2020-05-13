package com.unesco.smla;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.smla.alerts.DialogBoxes;
import com.smla.database.DatabaseConn;

import java.util.Locale;

public class LoginPage extends AppCompatActivity {

    DatabaseConn db = new DatabaseConn(this);
    String MESSAGE="";

    public static Spinner lang;

    public static String uname, upass, STATUS;

    public static EditText user, pass;
    String STYPE, ATYPE;

    Locale myLocale;


    ArrayAdapter<String> langAdapter;
    String[] langList ={"en", "ha"};
    DialogBoxes dbox = new DialogBoxes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        lang = (Spinner) findViewById(R.id.language);

        langAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, langList);
        lang.setAdapter(langAdapter);

        lang.setSelection(0);

        STATUS = LandingPage.STATUS;

        if(STATUS.equals("NON FORMAL")){
            lang.setSelection(1);
        }else{
            lang.setSelection(0);
        }

        lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==1){

                    setLocale("ha");

                }else{

                    setLocale("en");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void validateLogin(View v)
    {

        user = (EditText) findViewById(R.id.username);
        uname = user.getText().toString();

        pass = (EditText) findViewById(R.id.password);
        upass = pass.getText().toString();

        String stype = STATUS;

        System.out.println("STYPE::::: "+stype);


    Cursor c = db.validateLogin(uname, upass);

        int a = c.getCount();

        String fullname="", STUDENTID="";

        if(a >0){

            if(c.moveToFirst()){
                String FIRSTNAME = c.getString(c.getColumnIndex("FIRSTNAME"));
                String LASTNAME = c.getString(c.getColumnIndex("LASTNAME"));
                STUDENTID = c.getString(c.getColumnIndex("STUDENTID"));

                 ATYPE = c.getString(c.getColumnIndex("ACCOUNT_TYPE"));
                 STYPE = c.getString(c.getColumnIndex("STYPE"));

                fullname = FIRSTNAME+" "+LASTNAME;
            }

            String activity = fullname+" Logged into the portal ";
            db.close();

            if(ATYPE.equals("STUDENT")&& STYPE.equals(stype)){

                db.insertLog(STUDENTID, activity, fullname);

                Intent i=new Intent(LoginPage.this, DashBoard.class);
                startActivity(i);
            }
            else
            if(ATYPE.equals("LEARNER")&& STYPE.equals(stype)){

                db.insertLog(STUDENTID, activity, fullname);

                Intent i=new Intent(LoginPage.this, DashBoard.class);
                startActivity(i);
            }
            else
            if(ATYPE.equals("TEACHER")&& STYPE.equals(stype)){

                db.insertLog(STUDENTID, activity, fullname);

                Intent i=new Intent(LoginPage.this, DashBoard.class);
                startActivity(i);
            }
            else
            if(ATYPE.equals("FACILITATOR")&& STYPE.equals(stype)){

                db.insertLog(STUDENTID, activity, fullname);

                Intent i=new Intent(LoginPage.this, DashBoard.class);
                startActivity(i);
            }
            else
            if(ATYPE.equals("ADMINISTRATOR")){

                db.insertLog(STUDENTID, activity, fullname);

                Intent i=new Intent(LoginPage.this, DashBoard.class);
                startActivity(i);
            }

            else{
                dbox.showMessageDialog(this ,getString(R.string.dialog_title_error), getString(R.string.invalid_login), R.drawable.error_icon);

            }
            //db.insertLog(STUDENTID, activity, fullname);

            //Intent i=new Intent(LoginPage.this, DashBoard.class);
            //startActivity(i);
        }else{
            if(a==0){
                dbox.showMessageDialog(this ,getString(R.string.dialog_title_error), getString(R.string.invalid_login), R.drawable.error_icon);

            }
            if(a<0){
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.database_error), R.drawable.error_icon);

            }
        }

    }

    public void newProfile(View v) {

        //Go to new Profile page..

        if (STATUS.equals("NON FORMAL")) {

            Intent i = new Intent(LoginPage.this, NonFormalProfile.class);
            startActivity(i);

        } else {

            Intent i = new Intent(LoginPage.this, NewProfile.class);
            startActivity(i);
        }
    }

    public void exitApp(View v){

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exitTitle))
                .setMessage(getString(R.string.confirmExit))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(LoginPage.this, "You have exited the application", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(LoginPage.this, LandingPage.class);
                        startActivity(i);

                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void setLocale(String lang){

        myLocale = new Locale(lang);

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        conf.locale=myLocale;

        res.updateConfiguration(conf, dm);
    }

}
