package com.unesco.smla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class QuizResult extends AppCompatActivity {

    TextView passScore, myScore, passText, vector_ques;
    ImageView myImage;

    LinearLayout qLayout;

    DecimalFormat df = new DecimalFormat("###,###,###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.qR_bar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.ic_library_books_white_24dp);


        passScore = (TextView)findViewById(R.id.passingScore);
        myScore = (TextView)findViewById(R.id.yourScore);
        passText = (TextView)findViewById(R.id.passText);
        myImage = (ImageView)findViewById(R.id.passImage);

        qLayout = (LinearLayout)findViewById(R.id.ques_layout);

        double score = StartQuiz.fscore;

        System.out.println("FSCORE:::-- "+score);

        if(StartQuiz.PASS_STATUS.equals("PASS")){

            passText.setText(getString(R.string.youPassed));
            passText.setTextColor(getResources().getColor(R.color.green));
            myScore.setText(getString(R.string.yourScore)+" "+String.valueOf(df.format(score))+"%");
            passScore.setText(getString(R.string.passingScore)+" "+StartQuiz.DSCORE);

        }
        else{
            passText.setText(getString(R.string.youFailed));
            passText.setTextColor(getResources().getColor(R.color.red));
            myScore.setText(getString(R.string.yourScore)+" "+String.valueOf(df.format(score))+"%");
            passScore.setText(getString(R.string.passingScore)+" "+StartQuiz.DSCORE+"%");
            myImage.setImageDrawable(getResources().getDrawable(R.drawable.wrong1));
        }


        //Load Questions..

        loadQuestions();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(StartQuiz.WHERE.equals("LIST"))
        {
            QuizList.QUIZNAME="";
            Toast.makeText(QuizResult.this, getString(R.string.qComplete), Toast.LENGTH_LONG);
            Intent i = new Intent(QuizResult.this, QuizList.class);
            startActivity(i);
        }else{
            ViewLesson.lessonName="";
            Toast.makeText(QuizResult.this, getString(R.string.qComplete), Toast.LENGTH_LONG);
            Intent i = new Intent(QuizResult.this, DashBoard.class);
            startActivity(i);
        }

    }

    public void done(){

        if(StartQuiz.WHERE.equals("LIST"))
        {
            QuizList.QUIZNAME="";
            Toast.makeText(QuizResult.this, getString(R.string.qComplete), Toast.LENGTH_LONG);
            Intent i = new Intent(QuizResult.this, QuizList.class);
            startActivity(i);
        }else{
            ViewLesson.lessonName="";
            Toast.makeText(QuizResult.this, getString(R.string.qComplete), Toast.LENGTH_LONG);
            Intent i = new Intent(QuizResult.this, DashBoard.class);
            startActivity(i);
        }

    }

    public void loadQuestions(){

        TextView label;

        for(int i=0; i<StartQuiz.vector.size(); i++){

            String axx = String.valueOf(StartQuiz.vector.elementAt(i));
            System.out.println("AXX IS "+axx);

            String a = String.valueOf(StartQuiz.vector.elementAt(i));
            String ax[] = a.split("#");

            String ques = ax[0];
            String ans = ax[1];

            label = new TextView(this);

            label.setText(ques);

            label.setPadding(0,5,0,10);

            if(ans.equals("YES")){
                //img = new Image("/unesco/elearning/image/correct_tick.png");

                label.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.correct_tick, 0, 0, 0);

            }else{
                //img = new Image("/unesco/elearning/image/wrong_tick.png");

                label.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.wrong_tick, 0, 0, 0);
            }

            System.out.println(ques);
            qLayout.addView(label);
        }

        Button butt = new Button(this);

        butt.setText(R.string.done);
        butt.setBackgroundColor(getResources().getColor(R.color.buttonColor));
        butt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                done();
            }
        });

        qLayout.addView(butt);
    }
}
