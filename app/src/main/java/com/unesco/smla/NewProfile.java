package com.unesco.smla;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smla.alerts.DialogBoxes;
import com.smla.database.DatabaseConn;
import com.smla.sessions.IDGenerator;
import com.smla.sessions.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewProfile extends AppCompatActivity {

    public static EditText studentID, firstname, lastname, username, password, dob;
    Spinner sex, atype, stype;
    ImageView imgView;
    String imgPath;
    String SELIMG="NO";

    Uri file;

    Bitmap userImg;

    Button tkImg;

    DialogBoxes dbox = new DialogBoxes();
    Utilities mUtilities = new Utilities();

    Calendar myCalendar = Calendar.getInstance();

    DatabaseConn db = new DatabaseConn(this);

    private static int RESULT_LOAD_IMG = 1;

    ArrayAdapter<String> sexAdapter, atypeAdapter, stypeAdapter;
    String[] sexList ={"Please Select","Male", "Female"};
    String[] atypeList ={"Please Select", "STUDENT", "TEACHER"};
    String[] stypeList ={ "FORMAL (JSS 2)"};

    DatePickerDialog.OnDateSetListener date;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    private static final int CAMERA_CODE = 100;

    public static String mCurrentPhotoPath="";

    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        imgView = (ImageView)findViewById(R.id.profileImage);
        studentID = (EditText) findViewById(R.id.new_studentID);
        firstname = (EditText) findViewById(R.id.new_firstname);
        lastname = (EditText) findViewById(R.id.new_lastname);
        username = (EditText) findViewById(R.id.new_username);
        password = (EditText) findViewById(R.id.new_password);
        dob = (EditText) findViewById(R.id.new_dob);
        sex = (Spinner) findViewById(R.id.new_sex);
        atype = (Spinner) findViewById(R.id.new_atype);
        stype = (Spinner) findViewById(R.id.new_stype);

        tkImg = (Button) findViewById(R.id.takeIm);



        sexAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sexList);
        sex.setAdapter(sexAdapter);

        atypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, atypeList);
        atype.setAdapter(atypeAdapter);

        stypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, stypeList);
        stype.setAdapter(stypeAdapter);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA )!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(NewProfile.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CODE );

        }
        else{

        }


        //tkImg.setEnabled(false);

        //Create a date picker dialog..

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

    //go back..

    public void goBack(View v)
    {


        //Intent i=new Intent(NewProfile.this, LoginPage.class);
        //startActivity(i);
        LoginPage.STATUS="FORMAL (JSS 2)";
        finish();
        LoginPage.STATUS="FORMAL (JSS 2)";
    }

    //save profile..
    public void registerProfile(View v) {

        String stID = studentID.getText().toString();
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String uname = username.getText().toString();
        String upass = password.getText().toString();
        String udob = dob.getText().toString();
        String usex = sex.getSelectedItem().toString();
        String uatype = atype.getSelectedItem().toString();
        String ustype = stype.getSelectedItem().toString();

        /*
        if (uatype.equals("TEACHER") && ustype.equals("NOT APPLICABLE")) {

            dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.invalid_account_type), R.drawable.error_icon);
            return;
        } else {

            */

            if (atype.getSelectedItem() == null) {

            } else {
                if (uatype.equals("TEACHER") || uatype.equals("ADMINISTRATOR")) {
                    stID = IDGenerator.nextSessionId().toUpperCase();
                }
            }

            if (fname.trim().length() == 0 || lname.trim().length() == 0 || uname.trim().length() == 0 || upass.trim().length() == 0
                    || udob.trim().length() == 0 || sex.getSelectedItemPosition() == 0 || atype.getSelectedItemPosition() == 0 || stID.trim().length() == 0
                    || SELIMG.equals("NO")) {
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.fill_in_blank_fields), R.drawable.error_icon);
            } else {

                byte[] bimg = mUtilities.getBytes(userImg);

                String x = db.insertProfile(fname, lname, uname, upass, udob, usex, uatype, bimg, ustype, stID);

                if (x.equals("GOOD")) {

                    dbox.showMessageDialog(this, getString(R.string.info), getString(R.string.profile_created), R.drawable.info_icon);


                    finish();

                } else {
                    //Data not inserted..
                    dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.profileError), R.drawable.error_icon);
                }

            }
        }

    public void takeImage (View v) throws IOException{

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                tkImg.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            userImg = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(userImg);

            SELIMG="YES";
            LoginPage.STATUS="FORMAL (JSS 2)";
            }
    }

    //load date picker..

    public void loadDate(View v){

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    //Update date label..

    public void updateLabel(){

            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dob.setText(sdf.format(myCalendar.getTime()));
    }

    public Bitmap reduceMe(File fl){

        Bitmap bitmap=null;

        try{
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds=true;

            BitmapFactory.decodeStream(new FileInputStream(fl), null, o);
            final int REQUIRED_SIZE=150;

            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();

            o2.inSampleSize=scale;
            bitmap= BitmapFactory.decodeStream(new FileInputStream(fl), null, o2);
            LoginPage.STATUS="FORMAL (JSS 2)";

        }catch(Exception nn){

            System.out.println("ERROR..."+nn);

            bitmap=null;
            LoginPage.STATUS="FORMAL (JSS 2)";
        }
        return bitmap;
    }

}
