package com.project.christinkcdev.share.shareindia.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.christinkcdev.share.shareindia.R;

public class FileExplorerActivity extends AppCompatActivity {

    public static final String EXTRA_FILE_PATH = "filePath";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
    }
}