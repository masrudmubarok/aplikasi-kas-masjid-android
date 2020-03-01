package com.mubarok.e_kas_masjid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapterLaporan extends RecyclerView.Adapter<RecyclerAdapterLaporan.ViewHolder> {
    ArrayList<HashMap<String ,String >> listdatal;
    private Context context;

    public RecyclerAdapterLaporan(Context context, ArrayList<HashMap<String, String>> listdatal) {
        this.listdatal = listdatal;
        this.context = context;
    }

    @Override
    public RecyclerAdapterLaporan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_laporan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterLaporan.ViewHolder holder, final int position) {

        holder.id.setText(listdatal.get(position).get("id_transaksi"));
        holder.tanggal.setText(listdatal.get(position).get("tanggal"));
        holder.keterangan.setText(listdatal.get(position).get("keterangan"));
        holder.pemasukan.setText(listdatal.get(position).get("pemasukan"));
        holder.pengeluaran.setText(listdatal.get(position).get("pengeluaran"));
    }

    @Override
    public int getItemCount() {
        return listdatal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayoutLek;
        TextView id, tanggal, keterangan, pemasukan, pengeluaran;
        private Context context;

        public ViewHolder(View view) {
            super(view);

            id = (TextView) view.findViewById(R.id.idL);
            tanggal = (TextView) view.findViewById(R.id.tanggalL);
            keterangan = (TextView) view.findViewById(R.id.keteranganL);
            pemasukan = (TextView) view.findViewById(R.id.pemasukanL);
            pengeluaran = (TextView) view.findViewById(R.id.pengeluaranL);
            relativeLayoutLek = view.findViewById(R.id.relativeLek);

        }
    }
}
