package com.mubarok.e_kas_masjid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class RecyclerAdapterTransaksi extends RecyclerView.Adapter<RecyclerAdapterTransaksi.ViewHolder> {
    ArrayList<HashMap<String ,String >> listdata;
    private Context context;
    private ProgressDialog dialog;
    private String Id;
    String HttpURL = "http://192.168.43.114/ekasmasjid/transaksidelete.php";

//    private int lastSelectedPosition = -1;

    public RecyclerAdapterTransaksi(Context context, ArrayList<HashMap<String , String >> listdata) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_transaksi, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.id.setText(listdata.get(position).get("id_transaksi"));
        holder.tanggal.setText(listdata.get(position).get("tanggal"));
        holder.keterangan.setText(listdata.get(position).get("keterangan"));
        holder.jenis_trans.setText(listdata.get(position).get("jenis"));
        holder.jumlah.setText(listdata.get(position).get("jumlah"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final ProgressDialog dialog = new ProgressDialog(v.getContext());
                dialog.setMessage("Loading delete data");
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent iTransaksiedit = new Intent(v.getContext(), TransaksiEditActivity.class);
                                iTransaksiedit.putExtra("id_transaksi", listdata.get(position).get("id_transaksi"));
                                iTransaksiedit.putExtra("tanggal", listdata.get(position).get("tanggal"));
                                iTransaksiedit.putExtra("keterangan", listdata.get(position).get("keterangan"));
                                iTransaksiedit.putExtra("jenis", listdata.get(position).get("jenis"));
                                iTransaksiedit.putExtra("jumlah", listdata.get(position).get("jumlah"));
                                v.getContext().startActivity(iTransaksiedit);
                                break;
                            case 1:
                                AlertDialog.Builder builderDel = new AlertDialog.Builder(v.getContext());
                                builderDel.setTitle("Hapus Data");
                                builderDel.setMessage("Apakah anda yakin ingin menghapus data ini?");
                                builderDel.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Id = listdata.get(position).get("id_transaksi");
                                        DeleteData(Id);
                                        Toast.makeText(v.getContext(),"Data deleted successfully",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builderDel.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builderDel.create().show();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    public void DeleteData(final String id) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {
                String IdHolder = id;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", IdHolder));
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
                return "Data Deleted Successfully";
            }

        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(id);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        CardView cv;
        TextView id, jenis_trans, tanggal, keterangan, jumlah;
        ImageView imgView;
        RadioButton selectionTrans;
        private Context context;

        public ViewHolder(View v) {
            super(v);

            id = (TextView) v.findViewById(R.id.txtidtrans);
            jenis_trans = (TextView) v.findViewById(R.id.txtjnstrans);
            tanggal = (TextView) v.findViewById(R.id.txttanggal);
            keterangan = (TextView) v.findViewById(R.id.txtketerangan);
            jumlah = (TextView) v.findViewById(R.id.txtjumlah);
            relativeLayout = v.findViewById(R.id.relative);

        }
    }


}
