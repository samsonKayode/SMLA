package com.unesco.smla;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.smla.content.ContentSettings;
import com.smla.model.LibraryModel;
import com.smla.sessions.LibraryAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class LibraryContent extends AppCompatActivity {

    ListView LV;

    ArrayList<LibraryModel> LM;

    LibraryAdapter LA;

    EditText editsearch;

    ContentSettings cs = new ContentSettings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(LibraryPage.courseTitle);

        setContentView(R.layout.activity_library_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.library_content_bar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_library_books_white_24dp);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LV = (ListView) findViewById(R.id.libraryList);

        editsearch = (EditText) findViewById(R.id.search);

        System.out.println("LPAGE "+LibraryPage.cLoc);

        LM = cs.loadLibrary(LibraryPage.cLoc);

        LA = new LibraryAdapter(this,R.layout.library_row, LM);

        LV.setAdapter(LA);

        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                LA.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }

}
