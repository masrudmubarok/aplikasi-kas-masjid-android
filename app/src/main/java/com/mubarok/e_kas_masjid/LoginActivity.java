package com.mubarok.e_kas_masjid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends Activity {

    Button mBtn_login;
    Intent a;
    EditText mTxt_username, mTxt_password;
    String url, success;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());
        Toast.makeText(getApplicationContext(),
                "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        //inisialisasi button
        mBtn_login = (Button) findViewById(R.id.loginbtn);
        mTxt_username = (EditText) findViewById(R.id.UsernameEditText);
        mTxt_password = (EditText) findViewById(R.id.PasswordEditText);

        //function button
        mBtn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                url = "http://192.168.43.114/ekasmasjid/login.php?" + "username=" + mTxt_username.getText().toString() + "&password=" + mTxt_password.getText().toString();
                if (mTxt_username.getText().toString().trim().length() > 0 && mTxt_password.getText().toString().trim().length() > 0) {
                    new Masuk(getApplicationContext()).execute();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Username/password masih kosong gan.!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class Masuk extends AsyncTask<String, String, String> {
        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
        ProgressDialog pDialog;
        android.content.Context context;
        public Masuk (android.content.Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... arg0) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                success = json.getString("success");
                Log.e("error", "nilai sukses=" + success);

                if (success.equals("1")) {
                    JSONArray hasil = json.getJSONArray("login");
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        String nama = c.getString("username").trim();
                        String email = c.getString("password").trim();
                        session.createLoginSession(nama, email);
                        Log.e("ok", " ambil = data");
                    }
                } else {
                    Log.e("erro", "tidak bisa ambil data 0");
                }
            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
                Log.e("erro", "tidak bisa ambil data 1");
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (success!=null && success.equals("1")) {
                a = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(a);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Username/password salah gan.!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
