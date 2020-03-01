package com.mubarok.e_kas_masjid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LaporanActivity extends AppCompatActivity {

    public Button mBtn_dashboard;
    HttpResponse httpResponse;
    JSONObject jsonObject = null ;
    String StringHolder = "" ;

    Context context;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private RecyclerView recyclerViewL;
    ArrayList<HashMap<String , String >> listdatal;

    // Adding HTTP Server URL to string variable.
    String HttpURL = "http://192.168.43.114/ekasmasjid/laporanview.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        // inisialisasi button
        mBtn_dashboard = (Button)findViewById(R.id.backbtn2);

        // --------------------
        // Report Details
        // --------------------
        recyclerViewL = (RecyclerView) findViewById(R.id.recyclerViewL);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewL.setLayoutManager(layoutManager);

        requestQueue = Volley.newRequestQueue(LaporanActivity.this);
        listdatal = new ArrayList<HashMap<String, String>>();

        stringRequest = new StringRequest(Request.Method.GET, HttpURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Respon", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("transaksi");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        HashMap<String, String> iteml = new HashMap<String, String>();
                        iteml.put("id_transaksi", json.getString("id_transaksi"));
                        iteml.put("tanggal", json.getString("tanggal"));
                        iteml.put("keterangan", json.getString("keterangan"));
                        iteml.put("pemasukan", json.getString("pemasukan"));
                        iteml.put("pengeluaran", json.getString("pengeluaran"));
                        listdatal.add(iteml);
                        RecyclerAdapterLaporan adapter = new RecyclerAdapterLaporan(LaporanActivity.this, listdatal);
                        recyclerViewL.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LaporanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
