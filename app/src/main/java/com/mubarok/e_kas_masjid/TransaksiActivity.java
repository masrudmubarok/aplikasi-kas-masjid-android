package com.mubarok.e_kas_masjid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TransaksiActivity extends AppCompatActivity {

    public Button mBtn_dashboard, mBtn_transaksitambah;
    HttpResponse httpResponse;
    TextView pemasukanstts, pengeluaranstts, saldostts;
    JSONObject jsonObject = null ;
    String StringHolder = "" ;

    Context context;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private RecyclerView recyclerView;
    ArrayList<HashMap<String , String >> listdata;

    // Adding HTTP Server URL to string variable.
    String HttpURL1 = "http://192.168.43.114/ekasmasjid/transaksiview.php";
    String HttpURL2 = "http://192.168.43.114/ekasmasjid/detailtransaksiview.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        // --------------------
        // Transaction Summary
        // --------------------
        //inisialisasi button dan textview
        mBtn_dashboard = (Button)findViewById(R.id.backbtn);
        mBtn_transaksitambah = (Button)findViewById(R.id.plusbtn);
        pemasukanstts = (TextView)findViewById(R.id.pemasukanstts);
        pengeluaranstts = (TextView)findViewById(R.id.pengeluaranstts);
        saldostts = (TextView)findViewById(R.id.saldostts);

        //function button dan textview
        mBtn_transaksitambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTransaksiTambah = new Intent(getApplicationContext(),TransaksiTambahActivity.class);
                startActivity(iTransaksiTambah);
            }
        });

        new TransaksiActivity.GetDataFromServerIntoTextView(TransaksiActivity.this).execute();

        // --------------------
        // Transaction Details
        // --------------------
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        requestQueue = Volley.newRequestQueue(TransaksiActivity.this);
        listdata = new ArrayList<HashMap<String, String>>();

        stringRequest = new StringRequest(Request.Method.GET, HttpURL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Respon", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("transaksi");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        HashMap<String, String> itemt = new HashMap<String, String>();
                        itemt.put("id_transaksi", json.getString("id_transaksi"));
                        itemt.put("tanggal", json.getString("tanggal"));
                        itemt.put("keterangan", json.getString("keterangan"));
                        itemt.put("pemasukan", json.getString("pemasukan"));
                        itemt.put("pengeluaran", json.getString("pengeluaran"));
                        itemt.put("jenis", json.getString("jenis"));
                        itemt.put("jumlah", json.getString("jumlah"));
                        listdata.add(itemt);
                        RecyclerAdapterTransaksi adapter = new RecyclerAdapterTransaksi(TransaksiActivity.this, listdata);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransaksiActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);


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
            HttpPost httpPost = new HttpPost(HttpURL1);

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
                pemasukanstts.setText(jsonObject.getString("pemasukan"));
                pengeluaranstts.setText(jsonObject.getString("pengeluaran"));
                saldostts.setText(jsonObject.getString("saldo"));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
