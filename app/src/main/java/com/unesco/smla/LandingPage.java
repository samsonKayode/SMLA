package com.unesco.smla;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.smla.database.DatabaseConn;

import java.util.Locale;

public class LandingPage extends AppCompatActivity {

    public static String STATUS="";

    Locale myLocale;

    DatabaseConn db = new DatabaseConn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

    }

    public void goToFormal(View v){
        //Load formal Page..

        //LoginPage.lang.setSelection(1);

        STATUS = "FORMAL (JSS 2)";

        setLocale("en");

        Intent i = new Intent(LandingPage.this, LoginPage.class);
        startActivity(i);
    }

    public void goToNonFormal(View v){
        //Load Non Formal Page..

        STATUS="NON FORMAL";

        //LoginPage.lang.setSelection(1);

        setLocale("ha");

        Intent i = new Intent(LandingPage.this, LoginPage.class);
        startActivity(i);
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
