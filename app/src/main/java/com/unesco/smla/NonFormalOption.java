package com.unesco.smla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class NonFormalOption extends AppCompatActivity {

    public static String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_formal_option);

        Toolbar toolbar = (Toolbar) findViewById(R.id.nfo_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_library_books_white_24dp);
    }

    public void literacy(View v){

        title ="LITERACY";
        gotoClass();
    }

    public void numeracy(View v){

        title ="NUMERACY";
        gotoClass();
    }

    public void lifeskill(View v){

        title ="LIFE SKILL";
        gotoClass();
    }


    public void gotoClass(){

        Intent i = new Intent(NonFormalOption.this, NonFormalList.class);

        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(NonFormalOption.this, DashBoard.class);
        startActivity(i);
    }

}
