package com.hou.quanlysinhvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.model.SinhVien;

import java.util.ArrayList;
import java.util.List;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.SinhVienViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SinhVien sinhVien);
    }

    private final Context context;
    private List<SinhVien> listSinhVien;
    private final OnItemClickListener listener;

    public SinhVienAdapter(Context context, List<SinhVien> listSinhVien, OnItemClickListener listener) {
        this.context = context;
        this.listSinhVien = listSinhVien != null ? listSinhVien : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<SinhVien> newList) {
        this.listSinhVien = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SinhVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new SinhVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SinhVienViewHolder holder, int position) {
        SinhVien sv = listSinhVien.get(position);
        holder.tvHoTen.setText(sv.getHoTen());

        String lop = sv.getMaLop() != null && !sv.getMaLop().trim().isEmpty() ? sv.getMaLop() : "Chưa có lớp";
        holder.tvSubInfo.setText(sv.getMssv() + " · [" + lop + "]");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(sv);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSinhVien.size();
    }

    public static class SinhVienViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvSubInfo;

        public SinhVienViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvSubInfo = itemView.findViewById(R.id.tvSubInfo);
        }
    }
}
