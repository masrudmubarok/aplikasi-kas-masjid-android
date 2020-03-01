package com.mubarok.e_kas_masjid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private Button mBtn_transaksi, mBtn_laporan, mBtn_akun, mBtn_tentang;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);

        //inisialisasi button
        mBtn_transaksi = (Button)findViewById(R.id.transaksibtn);
        mBtn_laporan = (Button)findViewById(R.id.laporanbtn);
        mBtn_akun = (Button)findViewById(R.id.akunbtn);
        mBtn_tentang = (Button)findViewById(R.id.tentangbtn);

        //function button
        mBtn_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTransaksi = new Intent(getApplicationContext(),TransaksiActivity.class);
                startActivity(iTransaksi);
            }
        });
        mBtn_laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLaporan = new Intent(getApplicationContext(),LaporanActivity.class);
                startActivity(iLaporan);
            }
        });
        mBtn_akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAkun = new Intent(getApplicationContext(),AkunActivity.class);
                startActivity(iAkun);
            }
        });
        mBtn_tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTentang= new Intent(getApplicationContext(),TentangActivity.class);
                startActivity(iTentang);
            }
        });

    }
}
