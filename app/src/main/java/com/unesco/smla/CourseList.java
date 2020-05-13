package com.unesco.smla;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.smla.alerts.DialogBoxes;
import com.smla.content.ContentSettings;
import com.smla.database.DatabaseConn;
import com.smla.sessions.CustomListAdapter;


import java.io.File;
import java.util.List;

import static android.R.id.list;

public class CourseList extends AppCompatActivity {

    public static String C_LOC="";
    public static String LESSONNAME="";
    public static String COURSELOCATION="", COURSELOCATION2;
    int itemPosition;
    ListView courseList;
    CustomListAdapter adapter;
    ContentSettings cs = new ContentSettings();
    public static String myLoc="";
    public static String[] LNAME;

    int xyz=0;

    DialogBoxes dbox = new DialogBoxes();


    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    DatabaseConn db = new DatabaseConn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.course_list_bar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_library_books_white_24dp);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get Content Settings...

        String a = db.getMySetting();
        if(a.equals("OK")){
            C_LOC = DatabaseConn.CLOC;
            COURSELOCATION=C_LOC+"/"+FormalCourses.courseTitle+"/"+FormalCourses.term;
            COURSELOCATION2=C_LOC+"/QUIZZES/"+FormalCourses.courseTitle+"/"+FormalCourses.term;
        }


        courseList = (ListView)findViewById(R.id.courseList);

        List<String> list = cs.loadFiles(COURSELOCATION);

        CustomListAdapter adapter = new CustomListAdapter(this,
                android.R.layout.simple_list_item_1, list);
        courseList.setAdapter(adapter);

        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                LESSONNAME= (String) parent.getItemAtPosition(position);
                xyz++;

                itemPosition = parent.getSelectedItemPosition();
                System.out.println("Item Selected Is -"+LESSONNAME);

                //System.out.println("XYZ IS ----------------"+xyz);
/*
                if(xyz==20){

                    Toast.makeText(CourseList.this, "Chidi Says Hi", Toast.LENGTH_SHORT).show();
                    xyz=0;
                }



                for (int i = 0; i < courseList.getChildCount(); i++) {
                    if(position == i ){
                        courseList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }else{
                        courseList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                */
            }



        });
    }

    public void Play(View v){

        if(LESSONNAME.length()>0){

            //Play Lesson..

            ViewLesson.VLOC="COURSELIST";

            LNAME = LESSONNAME.split("/");
            //Insert Log..
            String CL = FormalCourses.term+" - "+FormalCourses.courseTitle;
            String activity = DashBoard.FULLNAME+" Accessed Lesson "+CL+" - "+LNAME[1];
            db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);
            //Insert Course View Log..
            db.accessLesson(DashBoard.STUDENTID, FormalCourses.courseTitle, FormalCourses.term, LNAME[1]);

            Intent i = new Intent(CourseList.this, ViewLesson.class);
            startActivity(i);


        }else{
         dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.selectLesson), R.drawable.error_icon);
        }

    }

    public void Pin(View v){

        if(LESSONNAME.length()>0){

            System.out.println("LESSON NAME IS ---------"+LESSONNAME);

            filePin();
        }else{
            dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.selectLesson), R.drawable.error_icon);
        }

    }

    //Find Pin..

    public void filePin(){

        PinLesson.xLoc="FORMAL";
        myLoc = C_LOC+"/PINNED LESSONS/"+DashBoard.STUDENTID+".txt";

        //ActivityCompat.requestPermissions(CourseList.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

        File fl = new File(myLoc);

        if(fl.exists()){
            Intent i = new Intent(CourseList.this, PinLesson.class);
            startActivity(i);
        }

        else{
            cs.newPinFile(fl);
            Intent i = new Intent(CourseList.this, PinLesson.class);
            startActivity(i);

        }
    }

}
