package com.mubarok.e_kas_masjid;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransaksiEditActivity extends AppCompatActivity {

    String HttpURL = "http://192.168.43.114/ekasmasjid/transaksiedit.php";
    EditText Tanggaltxt, Keterangantxt, Jumlahtxt;
    Button mBtn_calendar, mBtn_simpan, mBtn_batal;
    RadioButton rb_pemasukan, rb_pengeluaran, rb_button;
    RadioGroup rg_trans;
    String TempTanggal, TempKeterangan, TempJumlah, TempJenis, Id, jenis;

    Calendar c;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksiedit);

        //inisialisasi
        Tanggaltxt = (EditText)findViewById(R.id.tanggaltext2);
        Keterangantxt = (EditText) findViewById(R.id.keterangantext2);
        Jumlahtxt = (EditText)findViewById(R.id.jumlahtext2);
        mBtn_calendar = (Button)findViewById(R.id.tanggalbtn2);
        mBtn_simpan = (Button)findViewById(R.id.simpanbtn2);
        mBtn_batal = (Button)findViewById(R.id.batalbtn2);
        rg_trans = (RadioGroup)findViewById(R.id.transbtn2);
        rb_pemasukan = (RadioButton)findViewById(R.id.pemasukanbtn2);
        rb_pengeluaran = (RadioButton)findViewById(R.id.pengeluaranbtn2);

        // Receive Data from TransaksiActivity
        Id = getIntent().getStringExtra("id_transaksi");
        Tanggaltxt.setText(getIntent().getStringExtra("tanggal"));
        Keterangantxt.setText(getIntent().getStringExtra("keterangan"));
        Jumlahtxt.setText(getIntent().getStringExtra("jumlah"));
        jenis = getIntent().getStringExtra("jenis");

        if (jenis.equalsIgnoreCase("pemasukan")) {
            rb_pemasukan.setChecked(true);
        } else if (jenis.equalsIgnoreCase("pengeluaran")) {
            rb_pengeluaran.setChecked(true);
        }

        //function
        mBtn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(TransaksiEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        Tanggaltxt.setText(mDay + "/" + (mMonth+1) + "/" + mYear);
                    }

                } , day, month, year);
                dpd.show();
            }

        });

        mBtn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetData();
                UpdateData(Id, TempTanggal, TempKeterangan, TempJenis, TempJumlah);
                reset();
            }

        });

        mBtn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reset();
            }

        });
    }

    public void GetData() {

        // get selected radioButton from radioGroup
        int selectedId =  rg_trans.getCheckedRadioButtonId();
        // find the radioButton by returned id
        rb_button = (RadioButton) findViewById(selectedId);

        TempTanggal = Tanggaltxt.getText().toString();
        TempKeterangan = Keterangantxt.getText().toString();
        if (rb_button.getText().toString().equalsIgnoreCase("Pemasukan")){
            TempJenis = "pemasukan";
        } else {
            TempJenis = "pengeluaran";
        }
        TempJumlah = Jumlahtxt.getText().toString();

    }

    public void UpdateData(final String id, final String tanggal, final String keterangan, final String jenis, final String jumlah) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            @Override
            protected String doInBackground(String... params) {

                String IdHolder = id;
                String TanggalHolder = tanggal;
                String KeteranganHolder = keterangan;
                String JenisHolder = jenis;
                String JumlahHolder = jumlah;

                Log.d(TAG, "doInBackground: IdHolder"+id);
                Log.d(TAG, "doInBackground: TanggalHolder"+tanggal);
                Log.d(TAG, "doInBackground: KeteranganHolder"+keterangan);
                Log.d(TAG, "doInBackground: JenisHolder"+jenis);
                Log.d(TAG, "doInBackground: JumlahHolder"+jumlah);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                if (rb_button.getText().toString().equalsIgnoreCase("Pemasukan")){
                    nameValuePairs.add(new BasicNameValuePair("pemasukan", JumlahHolder));
                    nameValuePairs.add(new BasicNameValuePair("pengeluaran","0"));
                } else {
                    nameValuePairs.add(new BasicNameValuePair("pengeluaran", JumlahHolder));
                    nameValuePairs.add(new BasicNameValuePair("pemasukan", "0"));
                }
                nameValuePairs.add(new BasicNameValuePair("id", IdHolder));
                nameValuePairs.add(new BasicNameValuePair("tanggal", TanggalHolder));
                nameValuePairs.add(new BasicNameValuePair("keterangan", KeteranganHolder));
                nameValuePairs.add(new BasicNameValuePair("jenis", JenisHolder));
                nameValuePairs.add(new BasicNameValuePair("jumlah", JumlahHolder));
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(HttpURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    httpResponse.getEntity();
                    Log.d(TAG, "doInBackground: "+httpResponse.toString());
                    Log.d(TAG, "doInBackground: "+httpResponse.getParams());
                    Log.d(TAG, "doInBackground: "+httpResponse.getAllHeaders().toString());
                    Log.d(TAG, "doInBackground: "+httpResponse.getAllHeaders());
                    Log.d(TAG, "doInBackground: "+httpResponse.getEntity().getContent());

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                Log.d(TAG, "onPostExecute: "+result);
                Toast.makeText(TransaksiEditActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(id, tanggal, keterangan, jenis, jumlah);
    }

    private static final String TAG = "TransaksiEditActivity";
    public void reset(){
        Tanggaltxt.setText("DD/MM/YYYY");
        Keterangantxt.setText("");
        Jumlahtxt.setText("");
    }
}
