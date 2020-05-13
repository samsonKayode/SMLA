package com.unesco.smla;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.smla.database.DatabaseConn;

public class NonFormalVideo extends AppCompatActivity {

    VideoView VW;

    public static String LOC="";

    String LINK="", LESSON="", TERM="";

    DatabaseConn db = new DatabaseConn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_formal_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.nf_video);
        setSupportActionBar(toolbar);

        if(LOC.equals("NFL")){
            setTitle(NonFormalOption.title+"/"+NonFormalList.LNAME[1]);
            LINK = "file:///"+NonFormalList.COURSELOCATION+"/"+NonFormalList.LNAME[1]+".mp4";
            LESSON=NonFormalList.LNAME[1];

            TERM = NonFormalOption.title;

        }else{

            LESSON = DashBoard.lessonName.split("/")[1];
            LINK = DashBoard.video;
            TERM=DashBoard.term;

            setTitle(DashBoard.lessonName);
            System.out.println("VIDEO LINK --"+LINK);

        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_video_label_white_24dp);


        VW = (VideoView) findViewById(R.id.nonformalVideo);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(VW);
        mc.setMediaPlayer(VW);
        Uri video = Uri.parse(LINK);
        VW.setMediaController(mc);
        VW.setVideoURI(video);
        VW.start();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exitTitle))
                .setMessage(getString(R.string.confirmExitLesson))
                .setIcon(R.drawable.confirm_icon)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(NonFormalVideo.this, "You have left the lesson", Toast.LENGTH_SHORT).show();
                        //String CL = term+" - "+coursetitle+" - "+N;
                        String activity = DashBoard.FULLNAME+" exited lesson "+ LESSON;
                        db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);
                        //Update Lesson Time...
                        updateTime();

                        if(LOC.equals("NFL")){
                            NonFormalList.LESSONNAME="";
                            Intent i = new Intent(NonFormalVideo.this, NonFormalList.class);
                            startActivity(i);
                        }else{
                            Intent i = new Intent(NonFormalVideo.this, DashBoard.class);
                            startActivity(i);
                        }



                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    public void updateTime(){

        Cursor c = db.selectTime(DashBoard.STUDENTID, "NON-FORMAL", TERM, LESSON);

        int CID=0;

        int x = c.getCount();

        if(x>0){
            while(c.moveToNext()){

                String a = c.getString(0);
                CID = Integer.parseInt(a);

                System.out.println("CID FOR LESSON IS "+CID);

            }
            c.close();
            db.close();

            db.updateTime(DashBoard.STUDENTID, CID);

            //System.out.println("A ISSSSS- "+a);
            //System.out.println("CID IS "+CID);

        }else{
            System.out.println("NO LESSON TO UPDATE... COUNT IS "+x);
        }


    }
}
