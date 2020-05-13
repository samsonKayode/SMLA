package com.unesco.smla;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {

    ArrayAdapter<String> sexAdapter, atypeAdapter, stypeAdapter;
    String[] sexList ={"Please Select","Male", "Female"};
    String[] atypeList ={"Please Select", "STUDENT", "FACILITATOR", "ADMINISTRATOR"};
    String[] stypeList ={"Please Select", "FORMAL (JSS 2)", "NON FORMAL", "NOT APPLICABLE"};

    Bitmap userImg;

    byte[] bimg;

    DialogBoxes dbox = new DialogBoxes();
    Utilities mUtilities = new Utilities();

    Calendar myCalendar = Calendar.getInstance();

    DatabaseConn db = new DatabaseConn(this);

    private static int RESULT_LOAD_IMG = 1;

    EditText studentID, firstname, lastname, username, password, dob;
    Spinner sex, atype, stype;
    ImageView imgView;
    String imgPath;
    String CHANGEIMG="NO";

    DatePickerDialog.OnDateSetListener date;

    public static String FIRSTNAME, LASTNAME, FULLNAME, USERNAME, PASSWORD, STUDENTID, DOB, SEX, STUDENTTYPE, ACCOUNTTYPE;
    public static byte[] STUDENTIMAGE;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_bar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.ic_mode_edit_white_24dp);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        sexAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sexList);
        sex.setAdapter(sexAdapter);

        atypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, atypeList);
        atype.setAdapter(atypeAdapter);

        stypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, stypeList);
        stype.setAdapter(stypeAdapter);



        //Retrieve Profile Data..

        Cursor c = db.getProfile(LoginPage.uname, LoginPage.upass);

        int a = c.getCount();

        System.out.println("CURSOR SIZE FOR EDIT PROFILE IS ---"+a);

        if(a==1){

            if(c.moveToFirst()){
                USERNAME = c.getString(c.getColumnIndex("USERNAME"));
                PASSWORD = c.getString(c.getColumnIndex("PASSWORD"));
                FIRSTNAME = c.getString(c.getColumnIndex("FIRSTNAME"));
                LASTNAME = c.getString(c.getColumnIndex("LASTNAME"));
                DOB = c.getString(c.getColumnIndex("DOB"));
                SEX = c.getString(c.getColumnIndex("SEX"));
                STUDENTTYPE = c.getString(c.getColumnIndex("STYPE"));
                ACCOUNTTYPE = c.getString(c.getColumnIndex("ACCOUNT_TYPE"));
                STUDENTIMAGE = c.getBlob(c.getColumnIndex("U_IMAGE"));
                STUDENTID = c.getString(c.getColumnIndex("STUDENTID"));

                studentID.setText(STUDENTID);
                firstname.setText(FIRSTNAME);
                lastname.setText(LASTNAME);
                username.setText(USERNAME);
                password.setText(PASSWORD);
                dob.setText(DOB);
                sex.setSelection(sexAdapter.getPosition(SEX));
                atype.setSelection(atypeAdapter.getPosition(ACCOUNTTYPE));
                stype.setSelection(stypeAdapter.getPosition(STUDENTTYPE));
                imgView.setImageBitmap(mUtilities.getImage(STUDENTIMAGE));

                studentID.setEnabled(false);
            }

            c.close();
            db.close();

        }
        else{
            dbox.showMessageDialog(this, getString(R.string.dialog_title_error),
                    getString(R.string.errorRetrievingData), R.drawable.error_icon);
        }


        //Date picker..

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

    public void updateLabel(){

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
    }

    public void loadDate(View v){

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void loadImage(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                System.out.println("Image Path --"+imgPath);

                File fls = new File(imgPath);

                double size = fls.length()/1024;

                if(size >100){
                    //Toast.makeText(this, "The image you selected is too large -"+size+"kb. Please select a smaller size",
                      //      Toast.LENGTH_LONG).show();
                    CHANGEIMG = "YES";
                    userImg = reduceMe(fls);
                    imgView.setImageBitmap(userImg);
                }else{

                    // Set the Image in ImageView after decoding the String
                    userImg = BitmapFactory.decodeFile(imgPath);

                    imgView.setImageBitmap(userImg);
                    CHANGEIMG="YES";
                }


            } else {
                Toast.makeText(this, "You did not select an image",
                        Toast.LENGTH_LONG).show();
                CHANGEIMG = "NO";
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void goBack(View v){
        finish();
    }

    //Edit Profile..

    public void editProfile(View v){

        String stID = studentID.getText().toString();
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String uname = username.getText().toString();
        String upass = password.getText().toString();
        String udob = dob.getText().toString();
        String usex = sex.getSelectedItem().toString();
        String uatype = atype.getSelectedItem().toString();
        String ustype = stype.getSelectedItem().toString();


        if(fname.trim().length()==0||lname.trim().length()==0||uname.trim().length()==0||upass.trim().length()==0
                ||udob.trim().length()==0||sex.getSelectedItemPosition()==0||atype.getSelectedItemPosition()==0|| stID.trim().length()==0
                ||stype.getSelectedItemPosition()==0)
        {
            dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.fill_in_blank_fields), R.drawable.error_icon);
        }
        else{

            if(CHANGEIMG.equals("NO")){

                bimg = STUDENTIMAGE;

            }else{
                bimg = mUtilities.getBytes(userImg);
            }

            String x = db.updateProfile(fname, lname, uname, upass, udob,
                    usex, uatype, bimg, ustype, stID, LoginPage.uname, LoginPage.upass);

            if(x.equals("OK")){

                //dbox.showMessageDialog(this, getString(R.string.info), getString(R.string.profileUpdated), R.drawable.info_icon);
                DashBoard.student_name.setText(fname+" "+lname);
                DashBoard.student_status.setText(ustype);

                if(CHANGEIMG.equals("NO")){

                }else{
                    //bimg = mUtilities.getBytes(userImg);
                    DashBoard.student_image.setImageBitmap(mUtilities.getImage(bimg));
                }

                finish();

            }else{
                //Data not inserted..
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.profileUpdatedError), R.drawable.error_icon);
            }

        }
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

        }catch(Exception nn){

            System.out.println("ERROR..."+nn);

            bitmap=null;
        }
        return bitmap;
    }

}
