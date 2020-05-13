package com.unesco.smla;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.UserDictionary;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.smla.alerts.DialogBoxes;
import com.smla.database.DatabaseConn;
import com.smla.database.GetData;
import com.smla.filebrowser.FileChooser;

import java.io.File;

public class Settings extends AppCompatActivity {

    private static final int REQUEST_PATH=1;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    private static final int REQUEST_CODE_OPEN_DIRECTORY =5;

    EditText content, score, server;
    Spinner lang;
    ArrayAdapter<String> langAdapter;
    String[] langList ={"EN", "HA"};

    DialogBoxes dbox = new DialogBoxes();
    DatabaseConn dbc = new DatabaseConn(this);

    String CLOC, CLANG, SERVER;
    int DSCORE=0;

    GetData dats = new GetData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_settings_white_24dp);

        content = (EditText)findViewById(R.id.content);
        score = (EditText)findViewById(R.id.score);
        server = (EditText) findViewById(R.id.server);
        lang = (Spinner) findViewById(R.id.settingLanguage);

        langAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, langList);
        lang.setAdapter(langAdapter);

        //lang.setSelection(0);

        //Get Settings

        System.out.println( " I AM HERE FOR REALLLLLLLLLLLL");

        //String a = dbc.getMySetting();



        Cursor rs = dbc.getSettings();

        int a = rs.getCount();
        System.out.println("A IS ----"+a);

        if (rs.moveToFirst()) {

            content.setText(rs.getString(rs.getColumnIndex("C_LOC")));
            score.setText(rs.getString(rs.getColumnIndex("SCORE")));
            server.setText(rs.getString(rs.getColumnIndex("SERVER")));
            lang.setSelection(langAdapter.getPosition(rs.getString(rs.getColumnIndex("LANGUAGE"))));
        }
        rs.moveToNext();


    }

    //retrieve Settings Data..



    public void loadDirectory(View v){

        ActivityCompat.requestPermissions(Settings.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

        Intent intent1 = new Intent(this, FileChooser.class);
        startActivityForResult(intent1,REQUEST_PATH);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        if (requestCode == REQUEST_PATH){
            if (resultCode == RESULT_OK) {
                String curFileName = data.getStringExtra("GetPath");
                content.setText(curFileName);
            }
        }
    }

    public void cancelSettings(View v){

        finish();
    }

    public void saveSettings(View v){

        String serv = server.getText().toString();
        String scr = score.getText().toString();
        String loc = content.getText().toString();

        String language = lang.getSelectedItem().toString();

        if(loc.trim().length()<=2){
            dbox.showMessageDialog(this, getString(R.string.dialog_title_error),
                    getString(R.string.fill_in_blank_fields), R.drawable.error_icon );
        }

        else {

            String a = dbc.updateSettings(language, Float.parseFloat(scr), loc, serv);

            if(a.equals("OK")){

                //dbox.showMessageDialog(this, getString(R.string.info),
                //        getString(R.string.dataSaved), R.drawable.info_icon);

                finish();
            }
            else{
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), a, R.drawable.error_icon);
            }

        }

    }



}
