package com.mubarok.e_kas_masjid;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TentangActivity extends AppCompatActivity {

    public Button mBtn_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        //inisialisasi button
        mBtn_dashboard = (Button)findViewById(R.id.backbtn4);
    }
}
