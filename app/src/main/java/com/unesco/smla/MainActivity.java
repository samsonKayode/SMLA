package com.unesco.smla;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.smla.database.DatabaseConn;

public class MainActivity extends AppCompatActivity {

    DatabaseConn db = new DatabaseConn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //confirm if settings exist()

        int a = confirmSettings();
        if(a<=0){

            System.out.println("Populating this table");

            //populate settings page.
            db.populateTable();
            System.out.println("DATA DOES NOT EXIST");

        }else{
            //Do not populate..
            System.out.println("DATA EXISTS");
        }


        final ImageView iv = (ImageView) findViewById(R.id.imageView);

        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade);

        iv.startAnimation(an);

        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //iv.startAnimation(an);
                finish();
                //startActivity(new Intent(getBaseContext(), LoginPage.class));
                startActivity(new Intent(getBaseContext(), LandingPage.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public int confirmSettings(){
        int r = 0;

        Cursor rs = db.getSettings();
        r = rs.getCount();

        System.out.println("SETTINGS DATA");
        System.out.println("CURSOR SIZE IS ---"+r);

        return r;
    }

}
