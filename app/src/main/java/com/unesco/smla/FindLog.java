package com.unesco.smla;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.smla.alerts.DialogBoxes;
import com.smla.database.DatabaseConn;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FindLog extends AppCompatActivity {

    EditText sdate, edate, studentid, others;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    DialogBoxes dbox = new DialogBoxes();

    String selectedText="";
    public static String QUERY;

    public static String date_s, date_e;

    DatabaseConn db = new DatabaseConn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_log);

        Toolbar toolbar = (Toolbar) findViewById(R.id.find_log);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_view_list_white_24dp);

        sdate = (EditText) findViewById(R.id.startDate);
        edate = (EditText) findViewById(R.id.endDate);
        studentid = (EditText) findViewById(R.id.studentID);
        others = (EditText) findViewById(R.id.others);


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
    }

    public void cancel(View v){
        finish();
    }

    public void search(View v){

        date_s = sdate.getText().toString();
        date_e = edate.getText().toString();
        String stID = studentid.getText().toString();
        String oth = others.getText().toString();

        String Q1="", Q2="";

        if(date_s.length()<1||date_e.length()<1){
            dbox.showMessageDialog(this, getString(R.string.dialog_title_error),
                    getString(R.string.selectValidDates), R.drawable.error_icon);
        }
        else{

            if(stID.trim().length()>1){
                Q1= " AND ID LIKE '%"+stID+"%' OR NAME LIKE '%"+stID+"%'";
            }else{
                Q1="";
            }

            if(oth.trim().length()>1){
                Q2= " AND ACTIVITY LIKE '%"+oth+"%'";
            }else{
                Q2="";
            }

            QUERY = "SELECT ID, DATE, NAME, ACTIVITY FROM LOGS WHERE DATE BETWEEN ? AND ?" +Q1+Q2;

            Cursor c = db.findLogs(date_s, date_e);
            int x = c.getCount();
            if(x>0){
                System.out.println("DATA FOUND --"+x);

                db.close();

                Intent i = new Intent(FindLog.this, ViewLogs.class);
                startActivity(i);

            }else{
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error),
                        getString(R.string.noRecord), R.drawable.error_icon);
            }



        }


    }

    public void eloadDate(View v){
        selectedText = "ENDDATE";
        loadDate(v);
    }


    public void sloadDate(View v){
        selectedText = "STARTDATE";
        loadDate(v);
    }


    public void updateLabel(){

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(selectedText.equals("STARTDATE")){
            sdate.setText(sdf.format(myCalendar.getTime()));
        }else{
            edate.setText(sdf.format(myCalendar.getTime()));
        }
    }

    public void loadDate(View v){

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

}
