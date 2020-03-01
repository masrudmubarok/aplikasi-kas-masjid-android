package com.mubarok.e_kas_masjid;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class InfoAkunActivity extends AppCompatActivity {

    HttpResponse httpResponse;
    TextView idusertxtakn, namatxtakn, usernametxtakn, passwordtxtakn;
    JSONObject jsonObject = null ;
    String StringHolder = "" ;
    // Adding HTTP Server URL to string variable.
    String HttpURL = "http://192.168.43.114/ekasmasjid/infoakunview.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoakun);

        // Assigning ID's textView
        idusertxtakn = (TextView)findViewById(R.id.idusertxtakn);
        namatxtakn = (TextView)findViewById(R.id.namatxtakn);
        usernametxtakn = (TextView)findViewById(R.id.usernametxtakn);
        passwordtxtakn = (TextView)findViewById(R.id.passwordtxtakn);

        new GetDataFromServerIntoTextView(InfoAkunActivity.this).execute();

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
            HttpPost httpPost = new HttpPost(HttpURL);

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
                idusertxtakn.setText(jsonObject.getString("id_user"));
                namatxtakn.setText(jsonObject.getString("nama_user"));
                usernametxtakn.setText(jsonObject.getString("username"));
                passwordtxtakn.setText(jsonObject.getString("password"));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
