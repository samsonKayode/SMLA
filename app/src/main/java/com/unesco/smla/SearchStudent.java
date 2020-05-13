package com.unesco.smla;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.smla.alerts.DialogBoxes;
import com.smla.database.DatabaseConn;
import com.smla.model.StudentListModel;
import com.smla.sessions.Utilities;

import java.util.ArrayList;
import java.util.List;

public class SearchStudent extends AppCompatActivity {

    EditText name;

    public static String l_name;

    DialogBoxes dbox = new DialogBoxes();
    DatabaseConn db = new DatabaseConn(this);

    Utilities utx = new Utilities();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_search_white_24dp);

        name = (EditText)findViewById(R.id.name);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void searchStudent(View v){

        Cursor cr;
        List list;

        List<StudentListModel> lst = new ArrayList<StudentListModel>();

        if(name.getText().toString().trim().length()<=2){

            dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.select_name),
                    R.drawable.error_icon );
        }

        else{

            cr = db.avaProfile(name.getText().toString());

            int ax = cr.getCount();

            System.out.println("NO OF NAMES FOUND...."+ax);

            if(ax <=0){

                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.no_name_found),
                        R.drawable.error_icon );

            }else{

                l_name = name.getText().toString();

                Intent i=new Intent(SearchStudent.this, StudentList.class);
                startActivity(i);

            }

        }

    }
}
