package com.hou.quanlysinhvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.model.MonHoc;

import java.util.ArrayList;
import java.util.List;

public class MonHocAdapter extends RecyclerView.Adapter<MonHocAdapter.MonHocViewHolder> {

    public interface OnMonActionListener {
        void onEdit(MonHoc monHoc);
        void onDelete(MonHoc monHoc);
    }

    private final Context context;
    private List<MonHoc> listMon;
    private final OnMonActionListener listener;

    public MonHocAdapter(Context context, List<MonHoc> listMon, OnMonActionListener listener) {
        this.context = context;
        this.listMon = listMon != null ? listMon : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<MonHoc> newList) {
        this.listMon = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new MonHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonHocViewHolder holder, int position) {
        MonHoc mon = listMon.get(position);
        holder.tvTenMon.setText(mon.getTenMon());
        holder.tvMaMon.setText("Mã môn: " + mon.getMaMon());

        holder.btnEditMon.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(mon);
        });

        holder.btnDeleteMon.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(mon);
        });
    }

    @Override
    public int getItemCount() {
        return listMon.size();
    }

    public static class MonHocViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenMon, tvMaMon;
        ImageView btnEditMon, btnDeleteMon;

        public MonHocViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvMaMon = itemView.findViewById(R.id.tvMaMon);
            btnEditMon = itemView.findViewById(R.id.btnEditMon);
            btnDeleteMon = itemView.findViewById(R.id.btnDeleteMon);
        }
    }
}
