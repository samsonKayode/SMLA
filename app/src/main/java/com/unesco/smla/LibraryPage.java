package com.unesco.smla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.smla.alerts.DialogBoxes;
import com.smla.content.ContentSettings;
import com.smla.database.DatabaseConn;

public class LibraryPage extends AppCompatActivity {

    ContentSettings cs = new ContentSettings();
    DialogBoxes dbox = new DialogBoxes();

    public static String courseTitle="";

    public static String cLoc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.library_bar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_menu_library2);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void maths(View v){

        courseTitle = "MATHEMATICS";
        loadContent();
    }

    public void english(View v){

        courseTitle = "ENGLISH";
        loadContent();
    }

    public void ict(View v){

        courseTitle = "BASIC ICT";
        loadContent();
    }

    public void science(View v){

        courseTitle = "BASIC SCIENCE";
        loadContent();
    }

    public void tech(View v){

        courseTitle = "BASIC TECH";
        loadContent();
    }

    public void phe(View v){

        courseTitle = "PHE";
        loadContent();
    }

    public void nonformal(View v){

        courseTitle = "NON FORMAL";
        loadContent();
    }

    private void loadContent() {

        cLoc = DatabaseConn.CLOC+"/LIBRARY/"+courseTitle;
        int x = cs.countLibrary(cLoc);

        if(x<=0){
            //No data found
            dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.no_data_found), R.drawable.error_icon);
        }
        else{

            Intent i = new Intent(LibraryPage.this, LibraryContent.class);
            startActivity(i);
        }
    }
}
