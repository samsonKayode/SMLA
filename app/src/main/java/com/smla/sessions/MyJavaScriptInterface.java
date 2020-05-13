package com.smla.sessions;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.unesco.smla.ViewLesson;

/**
 * Created by SAMSON KAYODE on 12-Sep-2017.
 */

public class MyJavaScriptInterface {

    Context mContext;

    public MyJavaScriptInterface(Context c) {
        mContext = c;
    }

    public void showToast(String toast){
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
