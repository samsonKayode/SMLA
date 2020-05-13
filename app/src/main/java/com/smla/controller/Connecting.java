package com.smla.controller;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.smla.database.DatabaseConn;
import com.smla.interfaces.AsyncResponse;
import com.unesco.smla.R;
import com.unesco.smla.SyncData;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by SAMSON KAYODE on 25-Dec-2017.
 */

public class Connecting extends AsyncTask<String,Void,String>
{

    public AsyncResponse delegate = null;

    public String FINISH="NO";
    Socket client=null;

    String ipAddress="";
    int portNumber=0;
    String fileName="";

    public static String responses="";

    public static String response="";

    public Connecting(String ip, int portNo, String file){

        ipAddress = ip;
        portNumber = portNo;
        fileName = file;

    }

    @Override
    protected String doInBackground(String... result)
    {
        String filePath = fileName;

        File sdFile = new File(filePath);

        try {

            client = new Socket(ipAddress , portNumber);

            OutputStream outputStream = client.getOutputStream();
            byte[] buffer = new byte[1024];
            FileInputStream in = new FileInputStream(sdFile);
            int rBytes;
            while((rBytes = in.read(buffer, 0, 1024)) != -1)
            {
                outputStream.write(buffer, 0, rBytes);
            }

            responses= "File Sent to server Successfully#SUCCESS";

            outputStream.flush();
            outputStream.close();
            client.close();

            System.out.println("RESPONSE ---"+responses);

            FINISH = "OK";

        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            responses = "UNABLE TO CONNECT TO SERVER "+e.getMessage()+"#ERROR";

            FINISH = "NO";

        }
        catch (ConnectException e) {
            e.printStackTrace();
            responses = "UNABLE TO CONNECT TO SERVER "+e.getMessage()+"#ERROR";
            FINISH = "NO";

        }
        catch (Exception e) {
            e.printStackTrace();

            responses = "Error uploading file to server "+e+"#ERROR";
            FINISH = "NO";

            System.out.println("RESPONSE ---"+responses);

        }

        return FINISH;
    }

    @Override
    public void onPostExecute(String result) {
        //textResponse.setText(response);

        String[] abc = responses.split("#");
        if(abc[1].equals("SUCCESS")){
            System.out.println("File is moved successful!");
            SyncData.t7.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.correct_tick, 0, 0, 0);

            FINISH = "OK";

            //Complete Process..


        }else{

            FINISH = "NO";

            System.out.println("File is NOT moved successful!");
            SyncData.t7.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.wrong_tick, 0, 0, 0);
        }

        SyncData.t7.setText(abc[0]);
        SyncData.startSync.setEnabled(true);
        SyncData.startSync.setBackgroundResource(R.color.green);
        SyncData.startSync.setText("START SYNCHRONIZATION");

        result = FINISH;

        delegate.processFinish(result);
        //super.onPostExecute(result);
    }
}