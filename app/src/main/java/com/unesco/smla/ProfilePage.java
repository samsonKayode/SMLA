package com.unesco.smla;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.smla.content.ContentSettings;
import com.smla.database.DatabaseConn;
import com.smla.model.LibraryModel;
import com.smla.model.StudentListModel;
import com.smla.sessions.LibraryAdapter;
import com.smla.sessions.MyCustomAdapter;
import com.smla.sessions.ProfileAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class ProfilePage extends AppCompatActivity {

    ListView LV;
    EditText editsearch;

    ArrayList<StudentListModel> LM;

    ProfileAdapter myCustomAdapter;

    ContentSettings cs = new ContentSettings();
    DatabaseConn db = new DatabaseConn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_content_bar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.profile_icon);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LV = (ListView) findViewById(R.id.profileList);

        LM = getData();

        myCustomAdapter= new ProfileAdapter(this,R.layout.list_row, LM);

        LV.setAdapter(myCustomAdapter);
    }

    public ArrayList<StudentListModel> getData(){

        ArrayList<StudentListModel> lst = new ArrayList<StudentListModel>();

        Cursor cr = db.getAllProfile();

        while(cr.moveToNext()){

            StudentListModel sList = new StudentListModel();

            String firstname = cr.getString(0);
            String lastname = cr.getString(1);
            String username = cr.getString(2);
            String password = cr.getString(3);
            String aType = cr.getString(4);
            byte[] STUDENTIMAGE = cr.getBlob(5);

            String STID = cr.getString(6);

            sList.setName(firstname+" "+lastname +" - "+aType);
            sList.setUsername(username);
            sList.setPassword("Password-"+password);
            sList.setId(STID);
            sList.setType(password);
            //sList.setImage(STUDENTIMAGE);
            sList.setImage(STUDENTIMAGE);

            lst.add(sList);
        }

        return lst;
    }

}
