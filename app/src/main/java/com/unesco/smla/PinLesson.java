package com.unesco.smla;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.smla.alerts.DialogBoxes;
import com.smla.content.ContentSettings;
import com.smla.database.DatabaseConn;

public class PinLesson extends AppCompatActivity {

    RadioGroup rGroup;
    String location="";
    RadioButton radioButton;
    public static String xLoc="";
    String myLOC="";

    String ab="";

    RadioButton loc1, loc2, loc3, loc4, loc5, loc6;

    String rpos="NIL", selOpt="";

    DialogBoxes dbox = new DialogBoxes();
    ContentSettings cs = new ContentSettings();
    DatabaseConn db = new DatabaseConn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lesson);

        Toolbar toolbar = (Toolbar) findViewById(R.id.pin_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_location_on_white_24dp);

        rGroup = (RadioGroup) findViewById(R.id.locations);

        loc1 = (RadioButton) findViewById(R.id.loc_1);
        loc2 = (RadioButton) findViewById(R.id.loc_2);
        loc3 = (RadioButton) findViewById(R.id.loc_3);
        loc4 = (RadioButton) findViewById(R.id.loc_4);
        loc5 = (RadioButton) findViewById(R.id.loc_5);
        loc6 = (RadioButton) findViewById(R.id.loc_6);

        if(xLoc.equals("FORMAL")){
            myLOC = CourseList.myLoc;

            String[] abx = CourseList.LESSONNAME.split("/");
            ab = abx[1];

        }else{
            String[] abx = NonFormalList.LESSONNAME.split("/");
            ab = abx[1];
            myLOC =NonFormalList.myLoc;
        }

        String less1 = cs.findExist(myLOC, "LESS1");
        String a1[] = less1.split("#");
        loc1.setText(a1[1]);

        String less2 = cs.findExist(myLOC, "LESS2");
        String a2[] = less2.split("#");
        loc2.setText(a2[1]);

        String less3 = cs.findExist(myLOC, "LESS3");
        String a3[] = less3.split("#");
        loc3.setText(a3[1]);

        String less4 = cs.findExist(myLOC, "LESS4");
        String a4[] = less4.split("#");
        loc4.setText(a4[1]);

        String less5 = cs.findExist(myLOC, "LESS5");
        String a5[] = less5.split("#");
        loc5.setText(a5[1]);

        String less6 = cs.findExist(myLOC, "LESS6");
        String a6[] = less6.split("#");
        loc6.setText(a6[1]);

    }

    public void savePin(View v){

        if(rpos.equals("NIL")){
            dbox.showMessageDialog(this, getString(R.string.dialog_title_error),
                    getString(R.string.selectPosition), R.drawable.error_icon);

        }else{
            int selectedId = rGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            radioButton = (RadioButton) findViewById(selectedId);

            location = radioButton.getText().toString();

            System.out.println("LOCATION IS -"+location);

            int a = writeToFile();

            if(a==0){
                //data saved..
                String activity = DashBoard.FULLNAME+" PINNED "+ab+" to shortcuts.";
                db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);
                dbox.showMessageDialog(this, getString(R.string.info), getString(R.string.dataSaved), R.drawable.info_icon);
                finish();
            }

            else{
                //data not saved..

                dbox.showMessageDialog(this, getString(R.string.dialog_title_error),
                        getString(R.string.dataNotSaved), R.drawable.error_icon);
            }
        }

    }

    public void cancelPin(View v){

        finish();

    }

    public void cpos1(View v){
        rpos="1";
        selOpt="LESS1";
    }

    public void cpos2(View v){
        rpos="2";
        selOpt="LESS2";
    }

    public void cpos3(View v){
        rpos="3";
        selOpt="LESS3";
    }

    public void cpos4(View v){
        rpos="4";
        selOpt="LESS4";
    }

    public void cpos5(View v){
        rpos="5";
        selOpt="LESS5";
    }

    public void cpos6(View v){
        rpos="6";
        selOpt="LESS6";
    }

    public int writeToFile()
    {
        int res =1;
        String xyz="";

        if(xLoc.equals("FORMAL")){

            String[] a = CourseList.LESSONNAME.split("/");

            xyz = selOpt+" #"+FormalCourses.courseTitle+"/"+FormalCourses.term+"/"+a[1];
        }else{

            String[] a = NonFormalList.LESSONNAME.split("/");
            xyz = selOpt+" #"+NonFormalOption.title+"/"+a[1];

        }

        String x = cs.findExist(myLOC, selOpt);

        int xo = cs.replaceText(myLOC, x, xyz);

        if(xo==0)
        {
            res =0;
        }
        else
        {
            res = 1;
        }

        return res;

    }

}
