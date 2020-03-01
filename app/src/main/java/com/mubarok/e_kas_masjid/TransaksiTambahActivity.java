package com.mubarok.e_kas_masjid;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransaksiTambahActivity extends AppCompatActivity {

    String HttpURL = "http://192.168.43.114/ekasmasjid/transaksiinsert.php";
    String HttpURL2 = "http://192.168.43.114/ekasmasjid/transaksiview.php";
    EditText Tanggaltxt, Keterangantxt, Jumlahtxt;
    Button mBtn_calendar, mBtn_simpan, mBtn_batal;
    RadioButton  rb_pemasukan, rb_pengeluaran, rb_button;
    RadioGroup rg_trans;
    String TempTanggal, TempKeterangan, TempJumlah, TempJenis, TempMasuk;
    HttpResponse httpResponse;
    TextView pemasukansttst, pengeluaransttst;
    JSONObject jsonObject = null ;
    String StringHolder = "" ;


    Calendar c;
    DatePickerDialog dpd;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksitambah);

        //inisialisasi
        Tanggaltxt = (EditText)findViewById(R.id.tanggaltext);
        Keterangantxt = (EditText) findViewById(R.id.keterangantext);
        Jumlahtxt = (EditText)findViewById(R.id.jumlahtext);
        mBtn_calendar = (Button)findViewById(R.id.tanggalbtn);
        mBtn_simpan = (Button)findViewById(R.id.simpanbtn);
        mBtn_batal = (Button)findViewById(R.id.batalbtn);
        rg_trans = (RadioGroup)findViewById(R.id.transbtn);
        rb_pemasukan = (RadioButton)findViewById(R.id.pemasukanbtn);
        rb_pengeluaran = (RadioButton)findViewById(R.id.pengeluaranbtn);
        pemasukansttst = (TextView)findViewById(R.id.pemasukansttst);
        pengeluaransttst = (TextView)findViewById(R.id.pengeluaransttst);

        // get selected radioButton from radioGroup
        int selectedId =  rg_trans.getCheckedRadioButtonId();

        // find the radioButton by returned id
        rb_button = (RadioButton) findViewById(selectedId);

        // Get Data From Server Into TextView
        new TransaksiTambahActivity.GetDataFromServerIntoTextView(TransaksiTambahActivity.this).execute();

        //function
        mBtn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(TransaksiTambahActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                InsertData(TempTanggal, TempKeterangan, TempJenis, TempJumlah);
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
        TempMasuk = pemasukansttst.getText().toString();

    }

    public void InsertData(final String tanggal, final String keterangan,  final String jenis, final String jumlah) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            @Override
            protected String doInBackground(String... params) {

                String TanggalHolder = tanggal;
                String KeteranganHolder = keterangan;
                String JenisHolder = jenis;
                String JumlahHolder = jumlah;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                if (rb_button.getText().toString().equalsIgnoreCase("Pemasukan")){
                    nameValuePairs.add(new BasicNameValuePair("pemasukan", JumlahHolder));
                    nameValuePairs.add(new BasicNameValuePair("pengeluaran","0"));
                } else {
                    nameValuePairs.add(new BasicNameValuePair("pengeluaran", JumlahHolder));
                    nameValuePairs.add(new BasicNameValuePair("pemasukan", "0"));

                }
                nameValuePairs.add(new BasicNameValuePair("tanggal", TanggalHolder));
                nameValuePairs.add(new BasicNameValuePair("keterangan", KeteranganHolder));
                nameValuePairs.add(new BasicNameValuePair("jenis", JenisHolder));
                nameValuePairs.add(new BasicNameValuePair("jumlah", JumlahHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(HttpURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(TransaksiTambahActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(tanggal, keterangan, jenis, jumlah);
    }



    public void reset(){
        Tanggaltxt.setText("DD/MM/YYYY");
        Keterangantxt.setText("");
        Jumlahtxt.setText("");
    }

    // Declaring GetDataFromServerIntoTextView method with AsyncTask.
    public class GetDataFromServerIntoTextView extends AsyncTask<Void, Void, Void>
    {
        // Declaring CONTEXT.
        public Context context;


        public GetDataFromServerIntoTextView(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {

            HttpClient httpClient = new DefaultHttpClient();

            // Adding HttpURL to my HttpPost oject.
            HttpPost httpPost = new HttpPost(HttpURL2);

            try {
                httpResponse = httpClient.execute(httpPost);

                StringHolder = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                // Passing string holder variable to JSONArray.
                JSONArray jsonArray = new JSONArray(StringHolder);
                jsonObject = jsonArray.getJSONObject(0);


            } catch ( JSONException e) {
                e.printStackTrace();
            }

            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result)
        {
            try {

                // Adding JSOn string to textview after done loading.
                pemasukansttst.setText(jsonObject.getString("pemasukan"));
                pengeluaransttst.setText(jsonObject.getString("pengeluaran"));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
