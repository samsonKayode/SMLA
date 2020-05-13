package com.unesco.smla;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.smla.sessions.LibraryAdapter;

import java.io.File;

public class LibraryImage extends AppCompatActivity {

    ImageView image;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.library_image);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_photo_library_white_24dp);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(LibraryAdapter.TITLE);

        image = (ImageView) findViewById(R.id.libraryImage);
        note = (TextView) findViewById(R.id.note);

        File imgFile = new File(LibraryPage.cLoc+"/"+LibraryAdapter.WEXT);

        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        image.setImageBitmap(myBitmap);

        note.setText(LibraryAdapter.DESC);


    }
}
