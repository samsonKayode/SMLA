package com.smla.alerts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.support.v7.app.AlertDialog;

import com.unesco.smla.R;

/**
 * Created by SAMSON KAYODE on 08-Sep-2017.
 */

public class DialogBoxes {

    public void showMessageDialog(Context context, String title, String content, int icon)
    {
        //Image img = new Image("/LOGO_01.png");

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(content);
        alert.setIcon(icon);
        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

    public void correctAnswer(Context context){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.correct_answer);
        dialog.show();
    }

    public void wrongAnswer(Context context){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.wrong_answer);
        dialog.show();
    }
}
