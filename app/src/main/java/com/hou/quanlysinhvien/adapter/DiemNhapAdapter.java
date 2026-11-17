package com.hou.quanlysinhvien.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.model.Diem;

import java.util.ArrayList;
import java.util.List;

public class DiemNhapAdapter extends RecyclerView.Adapter<DiemNhapAdapter.DiemNhapViewHolder> {

    private final Context context;
    private List<Diem> listDiem;

    public DiemNhapAdapter(Context context, List<Diem> listDiem) {
        this.context = context;
        this.listDiem = listDiem != null ? listDiem : new ArrayList<>();
    }

    public void updateData(List<Diem> newList) {
        this.listDiem = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<Diem> getListDiem() {
        return listDiem;
    }

    @NonNull
    @Override
    public DiemNhapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grade_input, parent, false);
        return new DiemNhapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiemNhapViewHolder holder, int position) {
        Diem item = listDiem.get(position);

        holder.tvStudentNameMssv.setText(item.getHoTenSinhVien() + " · " + item.getMssv());

        holder.removeWatchers();

        holder.etDiemCC.setText(item.getDiemCC() >= 0 ? formatDiem(item.getDiemCC()) : "");
        holder.etDiemGK.setText(item.getDiemGK() >= 0 ? formatDiem(item.getDiemGK()) : "");
        holder.etDiemCK.setText(item.getDiemCK() >= 0 ? formatDiem(item.getDiemCK()) : "");

        holder.addWatchers(item);
    }

    private String formatDiem(float d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format("%s", d);
        }
    }

    @Override
    public int getItemCount() {
        return listDiem.size();
    }

    public static class DiemNhapViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentNameMssv;
        EditText etDiemCC, etDiemGK, etDiemCK;
        TextWatcher watcherCC, watcherGK, watcherCK;

        public DiemNhapViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentNameMssv = itemView.findViewById(R.id.tvStudentNameMssv);
            etDiemCC = itemView.findViewById(R.id.etDiemCC);
            etDiemGK = itemView.findViewById(R.id.etDiemGK);
            etDiemCK = itemView.findViewById(R.id.etDiemCK);
        }

        public void removeWatchers() {
            if (watcherCC != null) etDiemCC.removeTextChangedListener(watcherCC);
            if (watcherGK != null) etDiemGK.removeTextChangedListener(watcherGK);
            if (watcherCK != null) etDiemCK.removeTextChangedListener(watcherCK);
        }

        public void addWatchers(final Diem item) {
            watcherCC = new SimpleTextWatcher() {
                @Override
                public void onTextChanged(String s) {
                    try {
                        item.setDiemCC(s.isEmpty() ? -1 : Float.parseFloat(s));
                    } catch (Exception e) {
                        item.setDiemCC(-1);
                    }
                }
            };
            etDiemCC.addTextChangedListener(watcherCC);

            watcherGK = new SimpleTextWatcher() {
                @Override
                public void onTextChanged(String s) {
                    try {
                        item.setDiemGK(s.isEmpty() ? -1 : Float.parseFloat(s));
                    } catch (Exception e) {
                        item.setDiemGK(-1);
                    }
                }
            };
            etDiemGK.addTextChangedListener(watcherGK);

            watcherCK = new SimpleTextWatcher() {
                @Override
                public void onTextChanged(String s) {
                    try {
                        item.setDiemCK(s.isEmpty() ? -1 : Float.parseFloat(s));
                    } catch (Exception e) {
                        item.setDiemCK(-1);
                    }
                }
            };
            etDiemCK.addTextChangedListener(watcherCK);
        }
    }

    private static abstract class SimpleTextWatcher implements TextWatcher {
        public abstract void onTextChanged(String text);

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextChanged(s.toString().trim());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }
}
