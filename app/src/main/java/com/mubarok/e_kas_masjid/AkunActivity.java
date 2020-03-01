package com.mubarok.e_kas_masjid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.allyants.notifyme.NotifyMe;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;

import static com.mubarok.e_kas_masjid.R.id.logoutbtn;

public class AkunActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public Button mBtn_dashboard, mBtn_infoakun, mBtn_gantipassword, mBtn_aturpengingat, mBtn_logout;
    SessionManager session;

    Calendar calendar = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        session = new SessionManager(getApplicationContext());

        //inisialisasi button
        mBtn_dashboard = (Button)findViewById(R.id.backbtn3);
        mBtn_infoakun = (Button)findViewById(R.id.infoakunbtn);
        mBtn_gantipassword = (Button)findViewById(R.id.gantipasswordbtn);
        mBtn_aturpengingat = (Button)findViewById(R.id.aturpengingatbtn);

        // get current date and set it to DateTimePickerDialog
        dpd = DatePickerDialog.newInstance(
                AkunActivity.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // initialize TimePickerDialog with current time
        tpd = TimePickerDialog.newInstance(
                AkunActivity.this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                false
        );

        mBtn_logout = (Button)findViewById(logoutbtn);
        mBtn_logout.setOnClickListener((View.OnClickListener) this);

        //function button
        mBtn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(iDashboard);
                finish();
            }
        });
        mBtn_infoakun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iInfoakun = new Intent(getApplicationContext(),InfoAkunActivity.class);
                startActivity(iInfoakun);
                finish();
            }
        });
        mBtn_gantipassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGantipassword = new Intent(getApplicationContext(),GantiPasswordActivity.class);
                startActivity(iGantipassword);
                finish();
            }
        });
        mBtn_aturpengingat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(), "DatePickerDialog");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutbtn:
                //Creating
                AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                //Setting Dialog
                alertLogout.setTitle("Keluar");
                alertLogout.setMessage("Apa anda yakin ingin keluar?");
                alertLogout.setIcon(R.drawable.logout);
                alertLogout.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        session.logoutUser();
                        finish();
                    }
                });
                alertLogout.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent iDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
                        startActivity(iDashboard);
                        finish();
                    }
                });
                alertLogout.show();
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tpd.show(getFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title("Weekly Notification")
                .content("Waktunya menghitung kas mingguan!")
                .color(255,0,0,255)
                .led_color(255, 255, 255, 255)
                .time(calendar)
                .addAction(new Intent(getApplicationContext(), DashboardActivity.class), "Action")
                .addAction(new Intent(), "Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true, false)
                .addAction(new Intent(), "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
    }
}

