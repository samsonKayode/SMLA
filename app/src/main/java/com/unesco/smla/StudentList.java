package com.unesco.smla;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smla.database.DatabaseConn;
import com.smla.model.StudentListModel;
import com.smla.sessions.MyCustomAdapter;

import java.util.ArrayList;

public class StudentList extends AppCompatActivity {

    ListView LV;
    MyCustomAdapter myCustomAdapter;

    DatabaseConn db = new DatabaseConn(this);

    ArrayList<StudentListModel> SLM=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.student_list);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_view_list_white_24dp);

        LV = (ListView) findViewById(R.id.studentList);

        SLM = getData();

        myCustomAdapter= new MyCustomAdapter(this,R.layout.list_row, SLM);

        LV.setAdapter(myCustomAdapter);
    }

    public ArrayList<StudentListModel> getData(){

        ArrayList<StudentListModel> lst = new ArrayList<StudentListModel>();

        Cursor cr = db.avaProfile(SearchStudent.l_name);

        while(cr.moveToNext()){

            StudentListModel sList = new StudentListModel();

            String firstname = cr.getString(0);
            String lastname = cr.getString(1);
            String studentID = cr.getString(2);
            String sType = cr.getString(3);
            byte[] STUDENTIMAGE = cr.getBlob(4);

            sList.setName(firstname+" "+lastname);
            sList.setId(studentID);
            sList.setType(sType);
            //sList.setImage(STUDENTIMAGE);
            sList.setImage(STUDENTIMAGE);

            lst.add(sList);
        }

        return lst;
    }

}
