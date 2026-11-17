package com.hou.quanlysinhvien.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.database.LopHocDao;
import com.hou.quanlysinhvien.database.SinhVienDao;
import com.hou.quanlysinhvien.model.LopHoc;
import com.hou.quanlysinhvien.model.SinhVien;

public class StudentDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MSSV = "EXTRA_MSSV";

    private FrameLayout btnBackDetail;
    private ImageView ivDetailAvatar;
    private TextView tvDetailMSSV;
    private TextView tvDetailHoTen;
    private TextView tvDetailNgaySinh;
    private TextView tvDetailLop;
    private TextView tvDetailEmail;
    private AppCompatButton btnEditDetail;
    private AppCompatButton btnDeleteDetail;

    private SinhVienDao sinhVienDao;
    private LopHocDao lopHocDao;
    private String mssv = "";
    private SinhVien currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        sinhVienDao = new SinhVienDao(this);
        lopHocDao = new LopHocDao(this);

        initViews();
        getIntentData();
        setupEvents();
    }

    private void initViews() {
        btnBackDetail = findViewById(R.id.btnBackDetail);
        ivDetailAvatar = findViewById(R.id.ivDetailAvatar);
        tvDetailMSSV = findViewById(R.id.tvDetailMSSV);
        tvDetailHoTen = findViewById(R.id.tvDetailHoTen);
        tvDetailNgaySinh = findViewById(R.id.tvDetailNgaySinh);
        tvDetailLop = findViewById(R.id.tvDetailLop);
        tvDetailEmail = findViewById(R.id.tvDetailEmail);
        btnEditDetail = findViewById(R.id.btnEditDetail);
        btnDeleteDetail = findViewById(R.id.btnDeleteDetail);
    }

    private void getIntentData() {
        if (getIntent().hasExtra(EXTRA_MSSV)) {
            mssv = getIntent().getStringExtra(EXTRA_MSSV);
            loadStudentDetail();
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin sinh viên!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadStudentDetail() {
        currentStudent = sinhVienDao.getSinhVienByMSSV(mssv);
        if (currentStudent != null) {
            tvDetailMSSV.setText(currentStudent.getMssv());
            tvDetailHoTen.setText(currentStudent.getHoTen());
            tvDetailNgaySinh.setText(currentStudent.getNgaySinh() != null && !currentStudent.getNgaySinh().isEmpty() ? currentStudent.getNgaySinh() : "--");

            String maLop = currentStudent.getMaLop();
            LopHoc lop = lopHocDao.getLopHocByMa(maLop);
            if (lop != null) {
                tvDetailLop.setText(lop.getTenLop() + " (" + lop.getMaLop() + ")");
            } else if (maLop != null && !maLop.trim().isEmpty()) {
                tvDetailLop.setText(maLop);
            } else {
                tvDetailLop.setText("Chưa có lớp");
            }

            tvDetailEmail.setText(currentStudent.getEmail() != null && !currentStudent.getEmail().isEmpty() ? currentStudent.getEmail() : "--");
        } else {
            Toast.makeText(this, "Sinh viên không tồn tại hoặc đã bị xóa!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupEvents() {
        btnBackDetail.setOnClickListener(v -> finish());

        btnEditDetail.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDetailActivity.this, StudentAddEditActivity.class);
            intent.putExtra(StudentAddEditActivity.EXTRA_MSSV, mssv);
            startActivity(intent);
        });

        btnDeleteDetail.setOnClickListener(v -> showDeleteConfirmDialog());
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sinh viên " + (currentStudent != null ? currentStudent.getHoTen() : "") + " (MSSV: " + mssv + ") không?\n\nLưu ý: Bảng điểm liên quan của sinh viên này cũng sẽ bị xóa.")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean ok = sinhVienDao.xoaSinhVien(mssv);
                    if (ok) {
                        Toast.makeText(StudentDetailActivity.this, "Đã xóa sinh viên thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(StudentDetailActivity.this, "Xóa sinh viên thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentDetail();
    }
}
