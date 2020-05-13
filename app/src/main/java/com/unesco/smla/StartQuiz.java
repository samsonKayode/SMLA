package com.unesco.smla;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.smla.alerts.DialogBoxes;
import com.smla.content.ContentSettings;
import com.smla.database.DatabaseConn;

import java.text.DecimalFormat;
import java.util.Vector;

public class StartQuiz extends AppCompatActivity {

    DatabaseConn db = new DatabaseConn(this);
    ContentSettings cs = new ContentSettings();
    DialogBoxes dbox = new DialogBoxes();

    RadioButton option1, option2, option3, option4;
    TextView QUES, qTitle;

    String ANSWER="", OPT="";

    String sques="", sopta="", soptb="", soptc="", soptd="";
    public static int sn = 1;
    public static double fscore=0.0;
    public static int score=0;

    public static Vector vector = new Vector();

    public static String PASS_STATUS="";

    public static String WHERE="";

    String loc="";

    DecimalFormat df = new DecimalFormat("###");

    public static float DSCORE = Float.parseFloat(DatabaseConn.SDSCORE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        option1 = (RadioButton)findViewById(R.id.option1);
        option2 = (RadioButton)findViewById(R.id.option2);
        option3 = (RadioButton)findViewById(R.id.option3);
        option4 = (RadioButton)findViewById(R.id.option4);

        QUES = (TextView)findViewById(R.id.QUES);
        qTitle = (TextView)findViewById(R.id.quiz_title);

        if(WHERE.equals("LIST")){
            String[] LNAME = QuizList.QUIZNAME.split("/");
            //Insert Log..
            String CL = QuizPage.term+" - "+QuizPage.courseTitle+" - "+LNAME[1];
            qTitle.setText(CL);
        }
        else{

            qTitle.setText(ViewLesson.CL);
        }



        vector.setSize(0);

        loadQuestion(sn);

    }

    public void opt1(View v){
        OPT = option1.getText().toString();
    }

    public void opt2(View v){
        OPT = option2.getText().toString();
    }

    public void opt3(View v){
        OPT = option3.getText().toString();
    }

    public void opt4(View v){
        OPT = option4.getText().toString();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exitTitle))
                .setMessage(getString(R.string.confirmExitQuiz))
                .setIcon(R.drawable.confirm_icon)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(StartQuiz.this, "You have ended the quiz ", Toast.LENGTH_SHORT).show();

                        if(WHERE.equals("LIST")){

                            String[] LNAME = QuizList.QUIZNAME.split("/");
                            //Insert Log..
                            String CL = QuizPage.term+" - "+QuizPage.courseTitle;
                            String activity = DashBoard.FULLNAME+" Ended Quiz "+CL+" - "+LNAME[1]+" Without completing.";
                            db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);

                            QuizList.QUIZNAME="";

                            Intent i = new Intent(StartQuiz.this, QuizList.class);
                            startActivity(i);

                        }else{

                            CourseList.LESSONNAME="";
                            String activity = DashBoard.FULLNAME+" Ended Quiz "+ViewLesson.CL+" - "+" Without completing.";
                            db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);

                            Intent i = new Intent(StartQuiz.this, DashBoard.class);
                            startActivity(i);

                        }


                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    public void nextQuestion(View v){

        if(OPT.length()<=0){

            dbox.showMessageDialog(this, getString(R.string.dialog_title_error),
                    getString(R.string.selectOption), R.drawable.error_icon);

        }else{

            sn = sn+1;

            //check if answer is correct..

            System.out.println("ANSWER IS - "+ANSWER);
            System.out.println("selANSWER IS - "+OPT);

            if(OPT.equals(ANSWER)){
                System.out.println("Answer correct");

                score = score +1;

                System.out.println("Score is "+score);

                //dbox.correctAnswer(this);

                vector.add(sn-1+". "+sques+"#YES");

                loadQuestion(sn);

            }
            else{
                //Answer not correct
                System.out.println("Answer Not correct");

                //dbox.wrongAnswer(this);

                vector.add(sn-1+". "+sques+"#NO");

                loadQuestion(sn);
            }

        }

    }

    //Load Questions..

    public void loadQuestion(int i) {

        if(WHERE.equals("LIST")){
            loc =QuizList.QUIZLOCATION+"/"+QuizList.QZNAME+".txt";
        }

        else{
            loc = CourseList.COURSELOCATION2+"/"+ViewLesson.LESSONNAME+".txt";
        }

        //String loc =QuizList.QUIZLOCATION+"/"+QuizList.QZNAME+".txt";

        System.out.println("QUIZ LOCATION "+loc);

        //Question
        sques = cs.translate(loc, "QUES" + String.valueOf(i));
        sopta = cs.translate(loc, "OPTA" + String.valueOf(i));
        soptb = cs.translate(loc, "OPTB" + String.valueOf(i));
        soptc = cs.translate(loc, "OPTC" + String.valueOf(i));
        soptd = cs.translate(loc, "OPTD" + String.valueOf(i));

        ANSWER = cs.translate(loc, "ANS" + String.valueOf(i));

        if(sques.length()==0){

            String a="";
            //End of Quiz..
            int fsn = sn-1;
            fscore = (Float.parseFloat(String.valueOf(score))/fsn)*100;

            if(fscore>=DSCORE){

                PASS_STATUS="PASS";

                if(WHERE.equals("LIST")){
                    String CL = QuizPage.term+" - "+QuizPage.courseTitle;
                    String activity = DashBoard.FULLNAME+" Completed Quiz "+CL+" - "+QuizList.QZNAME+". PASSED";
                    db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);

                    a = db.insertQuiz(DashBoard.STUDENTID, QuizPage.courseTitle, QuizPage.term, QuizList.QZNAME, Float.parseFloat(df.format(fscore)));
                }

                else{
                    String activity = DashBoard.FULLNAME+" Completed Quiz "+ViewLesson.CL+" - "+". PASSED";
                    db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);

                    a = db.insertQuiz(DashBoard.STUDENTID, ViewLesson.coursetitle, ViewLesson.term, ViewLesson.lessonName, Float.parseFloat(df.format(fscore)));
                }
                //Quiz Passed..

                if(a.equals("OK")){
                            System.out.println("QUIZ DATA SAVED");

                    Intent act = new Intent(StartQuiz.this, QuizResult.class);
                    startActivity(act);

                }
            }
            else{

                PASS_STATUS="FAIL";
                //Quiz Failed..

                if(WHERE.equals("LIST")){
                    String CL = QuizPage.term+" - "+QuizPage.courseTitle;
                    String activity = DashBoard.FULLNAME+" Completed Quiz "+CL+" - "+QuizList.QZNAME+". FAILED";
                    db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);
                    String ab = db.insertQuiz(DashBoard.STUDENTID, QuizPage.courseTitle, QuizPage.term, QuizList.QZNAME,Float.parseFloat(df.format(fscore)));
                }else{

                    String activity = DashBoard.FULLNAME+" Completed Quiz "+ViewLesson.CL+" - "+". FAILED";
                    db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);
                    String ab = db.insertQuiz(DashBoard.STUDENTID, ViewLesson.coursetitle, ViewLesson.term, ViewLesson.lessonName,Float.parseFloat(df.format(fscore)));
                }

                //Go to Result Page..

                Intent act = new Intent(StartQuiz.this, QuizResult.class);
                startActivity(act);
            }
            sn = 1;
            score = 0;
        }else{

            option1.setChecked(false);
            option2.setChecked(false);
            option3.setChecked(false);
            option4.setChecked(false);

            OPT="";

            QUES.setText(i+". "+ sques);
            option1.setText(sopta);
            option2.setText(soptb);
            option3.setText(soptc);
            option4.setText(soptd);

        }


    }
}
