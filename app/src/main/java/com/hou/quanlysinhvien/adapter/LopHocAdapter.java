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
import com.hou.quanlysinhvien.model.LopHoc;

import java.util.ArrayList;
import java.util.List;

public class LopHocAdapter extends RecyclerView.Adapter<LopHocAdapter.LopHocViewHolder> {

    public interface OnLopActionListener {
        void onEdit(LopHoc lopHoc);
        void onDelete(LopHoc lopHoc);
    }

    private final Context context;
    private List<LopHoc> listLop;
    private final OnLopActionListener listener;

    public LopHocAdapter(Context context, List<LopHoc> listLop, OnLopActionListener listener) {
        this.context = context;
        this.listLop = listLop != null ? listLop : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<LopHoc> newList) {
        this.listLop = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LopHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class, parent, false);
        return new LopHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LopHocViewHolder holder, int position) {
        LopHoc lop = listLop.get(position);
        holder.tvTenLop.setText(lop.getTenLop());
        holder.tvSubInfoLop.setText(lop.getMaLop() + " · Sĩ số: " + lop.getSiSo());

        holder.btnEditLop.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(lop);
        });

        holder.btnDeleteLop.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(lop);
        });
    }

    @Override
    public int getItemCount() {
        return listLop.size();
    }

    public static class LopHocViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenLop, tvSubInfoLop;
        ImageView btnEditLop, btnDeleteLop;

        public LopHocViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenLop = itemView.findViewById(R.id.tvTenLop);
            tvSubInfoLop = itemView.findViewById(R.id.tvSubInfoLop);
            btnEditLop = itemView.findViewById(R.id.btnEditLop);
            btnDeleteLop = itemView.findViewById(R.id.btnDeleteLop);
        }
    }
}
