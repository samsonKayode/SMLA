package com.unesco.smla;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

import java.util.List;

public class QuizList extends AppCompatActivity {

    public static String C_LOC="", QUIZLOCATION="";
    DatabaseConn db = new DatabaseConn(this);
    ContentSettings cs = new ContentSettings();
    public static String QUIZNAME="", QZNAME="";
    int itemPosition;
    DialogBoxes dbox = new DialogBoxes();
    ListView quizList;
    //public static float DSCORE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.quiz_list_bar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_question_answer_white_24dp);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get Content Settings...

        String a = db.getMySetting();
        if(a.equals("OK")){
            C_LOC = DatabaseConn.CLOC;
            QUIZLOCATION=C_LOC+"/QUIZZES/"+QuizPage.courseTitle+"/"+QuizPage.term;
            //DSCORE = Float.parseFloat(DatabaseConn.SDSCORE);
        }

        quizList = (ListView)findViewById(R.id.quizList);

        System.out.println("QUIZ LOCATION -----"+QUIZLOCATION);

        List<String> list = cs.loadQuizFiles(QUIZLOCATION);

        CustomListAdapter adapter = new CustomListAdapter(this,
                android.R.layout.simple_list_item_1, list);
        quizList.setAdapter(adapter);

        quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                QUIZNAME= (String) parent.getItemAtPosition(position);

                itemPosition = parent.getSelectedItemPosition();
                System.out.println("Item Selected Is -"+QUIZNAME);

            }



        });

    }

    public void startQuiz(View v){

        if(QUIZNAME.length()>0){

            String[] LNAME = QUIZNAME.split("/");

            QZNAME = LNAME[1];
            //Insert Log..

            System.out.println("ST ID -"+DashBoard.STUDENTID);
            System.out.println("COURSE TITLE -"+QuizPage.courseTitle);
            System.out.println("TERM -"+QuizPage.term);
            System.out.println("LESSON -"+QZNAME);

        Cursor cr = db.lessonAccessed(DashBoard.STUDENTID, QuizPage.courseTitle, QuizPage.term, QZNAME);

            int ax = cr.getCount();

            System.out.println("AX - "+ax);

            if(ax==0){

                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.accessLesson),
                        R.drawable.error_icon );
            }

            else{

                StartQuiz.WHERE="LIST";

                String CL = QuizPage.term+" - "+QuizPage.courseTitle;
                String activity = DashBoard.FULLNAME+" Accessed Quiz "+CL+" - "+LNAME[1];
                db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);

                Intent i = new Intent(QuizList.this, StartQuiz.class);
                startActivity(i);
            }

        }else{
            dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.selectQuiz),
                    R.drawable.error_icon);
        }

    }
}
