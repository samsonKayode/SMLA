package com.unesco.smla;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.smla.sessions.LibraryAdapter;

import java.io.File;

public class LibraryVideo extends AppCompatActivity {

    VideoView videoView;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.library_video);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_video_label_white_24dp);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(LibraryAdapter.TITLE);

        videoView = (VideoView) findViewById(R.id.videoView);
        note = (TextView) findViewById(R.id.note);

        File imgFile = new File(LibraryPage.cLoc+"/"+LibraryAdapter.WEXT);

        String LINK = "file:///"+LibraryPage.cLoc+"/"+LibraryAdapter.WEXT;
        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        Uri video = Uri.parse(LINK);
        videoView.setMediaController(mc);
        videoView.setVideoURI(video);
        videoView.start();

        note.setText(LibraryAdapter.DESC);


    }
}
