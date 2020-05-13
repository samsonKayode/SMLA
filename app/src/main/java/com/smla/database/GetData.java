package com.smla.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by SAMSON KAYODE on 11-Sep-2017.
 */

public class GetData {


    public void getmySettings(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase("SMLA.db", null, 0);

        Cursor rs = db.rawQuery("SELECT * FROM SETTINGS", null);

        System.out.println("RETRIEVING SETTINGS DATA");

        while (rs.moveToFirst()) {

            String a = rs.getString(rs.getColumnIndex("C_LOC"));
            String b = rs.getString(rs.getColumnIndex("SCORE"));
            String c = rs.getString(rs.getColumnIndex("SERVER"));
            String d =rs.getString(rs.getColumnIndex("LANGUAGE"));

            System.out.println("C_LOC -"+a);
            System.out.println("SCORE -"+b);
            System.out.println("SERVER -"+c);
            System.out.println("LANG -"+d);

        }
        rs.moveToNext();

    }
}
