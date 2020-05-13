package com.smla.controller;

import android.os.AsyncTask;

import com.unesco.smla.SyncData;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by SAMSON KAYODE on 25-Dec-2017.
 */

public class TestConn extends AsyncTask<Void, Void, Void> {

    Socket socket;

    String ipAdd="";
    int portNo;

    public static int res;


    public TestConn(String ip, int port){
        ipAdd = ip;
        portNo=port;
    }


    @Override
    protected Void doInBackground(Void... serverAdd)
    {

        try{
            socket = new Socket(ipAdd, portNo);

            socket.setSoTimeout(2000);

            res = 1;
            System.out.println("CONNECTED TO SERVER");
            socket.close();
        }
        catch (SocketTimeoutException ste){
            System.out.println("UNABLE TO CONNECT TO SERVER- TIME OUT HOST");
            res=0;
        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO CONNECT TO SERVER- UNKNOWN HOST");
            res=0;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO CONNECT TO SERVER- IO EXCEPTION");
            res=0;
        }
        catch(Exception nn){
            System.out.println("Error connecting to server");

            nn.printStackTrace();
            res =0;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {


        System.out.println("RES DATA IS---"+res);

        super.onPostExecute(aVoid);
    }
}
