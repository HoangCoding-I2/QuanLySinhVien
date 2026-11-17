package com.hou.quanlysinhvien.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.model.Diem;

import java.util.ArrayList;
import java.util.List;

public class DiemXemAdapter extends RecyclerView.Adapter<DiemXemAdapter.DiemXemViewHolder> {

    private final Context context;
    private List<Diem> listDiem;

    public DiemXemAdapter(Context context, List<Diem> listDiem) {
        this.context = context;
        this.listDiem = listDiem != null ? listDiem : new ArrayList<>();
    }

    public void updateData(List<Diem> newList) {
        this.listDiem = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiemXemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grade_view, parent, false);
        return new DiemXemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiemXemViewHolder holder, int position) {
        Diem item = listDiem.get(position);

        holder.tvViewStudentName.setText(item.getHoTenSinhVien());
        holder.tvViewStudentMssv.setText(item.getMssv());

        if (item.getDiemTB() >= 0) {
            holder.tvViewDetails.setText("CC: " + formatDiem(item.getDiemCC()) +
                    " | GK: " + formatDiem(item.getDiemGK()) +
                    " | CK: " + formatDiem(item.getDiemCK()));

            holder.tvDiemTB.setText("TB: " + item.getDiemTB());
            holder.tvXepLoai.setVisibility(View.VISIBLE);

            String xepLoai = item.getXepLoai();
            holder.tvXepLoai.setText(xepLoai);

            if ("Giỏi".equals(xepLoai)) {
                holder.tvXepLoai.setTextColor(Color.parseColor("#10B981"));
                holder.tvXepLoai.setBackgroundResource(R.drawable.bg_tag_green);
            } else if ("Khá".equals(xepLoai)) {
                holder.tvXepLoai.setTextColor(Color.parseColor("#1565C0"));
                holder.tvXepLoai.setBackgroundResource(R.drawable.bg_tag_blue);
            } else if ("Trung bình".equals(xepLoai)) {
                holder.tvXepLoai.setTextColor(Color.parseColor("#F59E0B"));
                holder.tvXepLoai.setBackgroundResource(R.drawable.bg_tag_blue);
            } else {
                holder.tvXepLoai.setTextColor(Color.parseColor("#EF4444"));
                holder.tvXepLoai.setBackgroundResource(R.drawable.bg_tag_blue);
            }
        } else {
            holder.tvViewDetails.setText("Chưa có điểm môn này");
            holder.tvDiemTB.setText("TB: --");
            holder.tvXepLoai.setVisibility(View.GONE);
        }
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

    public static class DiemXemViewHolder extends RecyclerView.ViewHolder {
        TextView tvViewStudentName, tvViewStudentMssv, tvViewDetails, tvDiemTB, tvXepLoai;

        public DiemXemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvViewStudentName = itemView.findViewById(R.id.tvViewStudentName);
            tvViewStudentMssv = itemView.findViewById(R.id.tvViewStudentMssv);
            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);
            tvDiemTB = itemView.findViewById(R.id.tvDiemTB);
            tvXepLoai = itemView.findViewById(R.id.tvXepLoai);
        }
    }
}
