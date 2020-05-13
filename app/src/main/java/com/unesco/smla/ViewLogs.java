package com.unesco.smla;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.smla.alerts.DialogBoxes;
import com.smla.database.DatabaseConn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewLogs extends AppCompatActivity {

    TableLayout tableLayout;
    DatabaseConn db = new DatabaseConn(this);
    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

    DialogBoxes dbox = new DialogBoxes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_log);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_view_list_white_24dp);

        tableLayout = (TableLayout) findViewById(R.id.tableViewLog);

        //tableLayout.setColumnShrinkable(1, true);
        tableLayout.setColumnShrinkable(3, true);

        loadLogs();

    }

    public void loadLogs() {

        Cursor c = db.findLogs(FindLog.date_s, FindLog.date_e);
        int x = c.getCount();
        if (x > 0) {

            try {

                while (c.moveToNext()) {

                    String ID = c.getString(0);
                    String datex = c.getString(1);
                    String name = c.getString(2);
                    String activity = c.getString(3);


                    Date dates = sdfIn.parse(datex);
                    String date = sdfOut.format(dates);

                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {ID, date, name, activity};

                    for (String text : colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        //tv.setTextSize(16);

                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);

                    }
                    tableLayout.addView(row);

                }

                c.close();
                db.close();
            } catch (Exception nn) {
                System.out.println("Exception retrieving data " + nn);
            }

        } else {

        }
    }

    public void exportData(View v){

        exportDB();
    }

    private void exportDB() {

        File myFile;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String TimeStampDB = sdf.format(cal.getTime());

        try {

            myFile = new File(DatabaseConn.CLOC + "/SYNC/LogData" + TimeStampDB + ".csv");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append("Student ID, Date, Name, Activity");
            myOutWriter.append("\n");

            Cursor c = db.findLogs(FindLog.date_s, FindLog.date_e);
            int x = c.getCount();

            if (c != null) {
                if (c.moveToFirst()) {
                    do {


                        String ID = c.getString(0);
                        String datex = c.getString(1);
                        String name = c.getString(2);
                        String activity = c.getString(3);


                        Date dates = sdfIn.parse(datex);
                        String date = sdfOut.format(dates);

                        myOutWriter.append(ID + "," + date + "," + name + "," + activity);
                        myOutWriter.append("\n");
                    }

                    while (c.moveToNext());
                }

                c.close();
                myOutWriter.close();
                fOut.close();

                dbox.showMessageDialog(this, getString(R.string.info),
                        getString(R.string.file_exported), R.drawable.info_icon);

            }
        } catch (Exception se) {
            Log.e(getClass().getSimpleName(), "Error exporting log file " + se);

            dbox.showMessageDialog(this, getString(R.string.dialog_title_error),
                    getString(R.string.error_exporting) + "/" + se.getMessage(), R.drawable.error_icon);
        }
    }
}
