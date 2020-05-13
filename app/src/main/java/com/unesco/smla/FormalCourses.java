package com.unesco.smla;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class FormalCourses extends AppCompatActivity {

    Dialog dialog;

    public static String courseTitle="", term="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formal_courses);

        Toolbar toolbar = (Toolbar) findViewById(R.id.course_bar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.ic_library_books_white_24dp);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void maths(View v){

        courseTitle = "MATHEMATICS";
        loadTerm();
    }

    public void english(View v){

        courseTitle = "ENGLISH";
        loadTerm();
    }

    public void ict(View v){

        courseTitle = "BASIC ICT";
        loadTerm();
    }

    public void science(View v){

        courseTitle = "BASIC SCIENCE";
        loadTerm();
    }

    public void tech(View v){

        courseTitle = "BASIC TECH";
        loadTerm();
    }

    public void phe(View v){

        courseTitle = "PHE";
        loadTerm();
    }

    public void loadTerm(){

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_term);
        dialog.setTitle("Title...");

        final Button term1 = (Button) dialog.findViewById(R.id.term1);
        final Button term2 = (Button) dialog.findViewById(R.id.term2);
        final Button term3 = (Button) dialog.findViewById(R.id.term3);

        term1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1st term selected

                term = term1.getText().toString();
                Intent i = new Intent(FormalCourses.this, CourseList.class);
                startActivity(i);

            }
        });

        term2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2nd term selected
                term = term2.getText().toString();
                Intent i = new Intent(FormalCourses.this, CourseList.class);
                startActivity(i);
            }
        });

        term3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3rd term selected..

                term = term3.getText().toString();
                Intent i = new Intent(FormalCourses.this, CourseList.class);
                startActivity(i);
            }
        });


        dialog.show();

    }



}
