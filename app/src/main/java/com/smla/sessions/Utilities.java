package com.smla.sessions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;


/**
 * Created by SAMSON KAYODE on 09-Sep-2017.
 */

public class Utilities {

    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public Bitmap getImage(byte[] blob){

        Bitmap bmp= BitmapFactory.decodeByteArray(blob,0,blob.length);

        return (bmp);
    }

    //Get Formated time in HH:mm:ss

    public String getFormattedTime(int secs) {
        // int secs = (int) Math.round((double) milliseconds / 1000); // for millisecs arg instead of secs
        if (secs < 60)
            return secs + "s";
        else {
            int mins = (int) secs / 60;
            int remainderSecs = secs - (mins * 60);
            if (mins < 60) {
                return (mins < 10 ? "0" : "") + mins + "m "
                        + (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
            }
            else {
                int hours = (int) mins / 60;
                int remainderMins = mins - (hours * 60);
                return (hours < 10 ? "0" : "") + hours + "h "
                        + (remainderMins < 10 ? "0" : "") + remainderMins + "m "
                        + (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
            }
        }
    }

    //Strip Extension...

    public String stripExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }

}
