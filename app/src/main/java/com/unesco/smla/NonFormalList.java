package com.unesco.smla;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smla.alerts.DialogBoxes;
import com.smla.content.ContentSettings;
import com.smla.database.DatabaseConn;
import com.smla.sessions.CustomListAdapter;

import java.io.File;
import java.util.List;

public class NonFormalList extends AppCompatActivity {

    DialogBoxes dbox = new DialogBoxes();
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    int itemPosition;
    ListView nonFormalList;
    CustomListAdapter adapter;
    ContentSettings cs = new ContentSettings();

    public static String LESSONNAME="";
    public static String COURSELOCATION="";
    String C_LOC="";
    public static String myLoc="";

    public static String[] LNAME;


    DatabaseConn db = new DatabaseConn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_formal_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.nf_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_library_books_white_24dp);

        String a = db.getMySetting();
        if(a.equals("OK")){
            C_LOC = DatabaseConn.CLOC;
            COURSELOCATION=C_LOC+"/NON FORMAL/"+NonFormalOption.title;

            nonFormalList = (ListView)findViewById(R.id.nonFormalList);

            List<String> list = cs.loadNonFormal(COURSELOCATION);

            CustomListAdapter adapter = new CustomListAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            nonFormalList.setAdapter(adapter);
        }

        nonFormalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                LESSONNAME= (String) parent.getItemAtPosition(position);

                itemPosition = parent.getSelectedItemPosition();
                System.out.println("Item Selected Is -"+LESSONNAME);



                /*
                for (int i = 0; i < nonFormalList.getChildCount(); i++) {
                    if(position == i ){
                        nonFormalList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }else{
                        nonFormalList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                */
            }



        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(NonFormalList.this, NonFormalOption.class);
        startActivity(i);
    }

    public void Play(View v){

        if(LESSONNAME.length()>0){

            NonFormalVideo.LOC = "NFL";

            LNAME = LESSONNAME.split("/");
            //Insert Log..
            String activity = DashBoard.FULLNAME+" Accessed Lesson - "+LNAME[1];
            db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);
            //Insert Course View Log..
            db.accessLesson(DashBoard.STUDENTID, "NON-FORMAL", NonFormalOption.title, LNAME[1]);

            Intent i = new Intent(NonFormalList.this, NonFormalVideo.class);
            startActivity(i);

        }else{
            dbox.showMessageDialog(NonFormalList.this, getString(R.string.dialog_title_error), getString(R.string.selectLesson), R.drawable.error_icon);
        }

    }

    public void Pin(View v){

        if(LESSONNAME.length()>0){

            filePin();
        }else{
            dbox.showMessageDialog(NonFormalList.this, getString(R.string.dialog_title_error), getString(R.string.selectLesson), R.drawable.error_icon);
        }

    }

    public void filePin(){

        PinLesson.xLoc="NONFORMAL";
        myLoc = C_LOC+"/PINNED LESSONS/"+DashBoard.STUDENTID+".txt";

        //ActivityCompat.requestPermissions(NonFormalList.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

        File fl = new File(myLoc);

        if(fl.exists()){
            Intent i = new Intent(NonFormalList.this, PinLesson.class);
            startActivity(i);
        }

        else{
            cs.newPinFile(fl);
            Intent i = new Intent(NonFormalList.this, PinLesson.class);
            startActivity(i);

        }
    }
}
