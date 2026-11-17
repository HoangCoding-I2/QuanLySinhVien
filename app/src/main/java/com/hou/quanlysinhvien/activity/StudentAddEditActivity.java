package com.hou.quanlysinhvien.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.database.LopHocDao;
import com.hou.quanlysinhvien.database.SinhVienDao;
import com.hou.quanlysinhvien.model.LopHoc;
import com.hou.quanlysinhvien.model.SinhVien;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StudentAddEditActivity extends AppCompatActivity {

    public static final String EXTRA_MSSV = "EXTRA_MSSV";

    private FrameLayout btnBack;
    private TextView tvTitleAddEdit;
    private EditText etMSSV;
    private EditText etHoTen;
    private EditText etNgaySinh;
    private Spinner spinnerLop;
    private EditText etEmail;
    private AppCompatButton btnSave;
    private AppCompatButton btnCancel;

    private SinhVienDao sinhVienDao;
    private LopHocDao lopHocDao;
    private List<LopHoc> listLop;
    private boolean isEditMode = false;
    private String mssvEdit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_edit);

        sinhVienDao = new SinhVienDao(this);
        lopHocDao = new LopHocDao(this);

        initViews();
        setupClassSpinner();
        checkMode();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitleAddEdit = findViewById(R.id.tvTitleAddEdit);
        etMSSV = findViewById(R.id.etMSSV);
        etHoTen = findViewById(R.id.etHoTen);
        etNgaySinh = findViewById(R.id.etNgaySinh);
        spinnerLop = findViewById(R.id.spinnerLop);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupClassSpinner() {
        listLop = lopHocDao.getAllLopHoc();
        List<String> listTenLop = new ArrayList<>();
        for (LopHoc lop : listLop) {
            listTenLop.add(lop.getTenLop() + " (" + lop.getMaLop() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listTenLop
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(adapter);
    }

    private void checkMode() {
        if (getIntent().hasExtra(EXTRA_MSSV)) {
            isEditMode = true;
            mssvEdit = getIntent().getStringExtra(EXTRA_MSSV);
            tvTitleAddEdit.setText(R.string.title_student_edit);
            etMSSV.setEnabled(false);
            loadStudentData(mssvEdit);
        } else {
            isEditMode = false;
            tvTitleAddEdit.setText(R.string.title_student_add);
        }
    }

    private void loadStudentData(String mssv) {
        SinhVien sv = sinhVienDao.getSinhVienByMSSV(mssv);
        if (sv != null) {
            etMSSV.setText(sv.getMssv());
            etHoTen.setText(sv.getHoTen());
            etNgaySinh.setText(sv.getNgaySinh());
            etEmail.setText(sv.getEmail());

            if (sv.getMaLop() != null && !sv.getMaLop().trim().isEmpty()) {
                for (int i = 0; i < listLop.size(); i++) {
                    if (listLop.get(i) != null && listLop.get(i).getMaLop() != null && listLop.get(i).getMaLop().equalsIgnoreCase(sv.getMaLop())) {
                        spinnerLop.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        etNgaySinh.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveStudent());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = 2004, month = 0, day = 1;

        String currentText = etNgaySinh.getText().toString().trim();
        if (currentText.contains("/")) {
            String[] parts = currentText.split("/");
            if (parts.length == 3) {
                try {
                    day = Integer.parseInt(parts[0]);
                    month = Integer.parseInt(parts[1]) - 1;
                    year = Integer.parseInt(parts[2]);
                } catch (Exception ignored) {}
            }
        }

        DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
            String formattedDate = String.format("%02d/%02d/%d", d, m + 1, y);
            etNgaySinh.setText(formattedDate);
        }, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void saveStudent() {
        String mssv = etMSSV.getText().toString().trim();
        String hoTen = etHoTen.getText().toString().trim().replaceAll("\\s+", " ");
        String ngaySinh = etNgaySinh.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (!isEditMode) {
            if (mssv.isEmpty()) {
                etMSSV.setError("Vui lòng nhập MSSV");
                etMSSV.requestFocus();
                return;
            }
            mssv = mssv.toUpperCase(Locale.ROOT);
            if (!mssv.matches("^[A-Z0-9]{4,15}$")) {
                etMSSV.setError("MSSV phải gồm 4-15 ký tự chữ và số");
                etMSSV.requestFocus();
                return;
            }
        }

        if (hoTen.isEmpty()) {
            etHoTen.setError("Vui lòng nhập họ tên");
            etHoTen.requestFocus();
            return;
        }
        if (!hoTen.matches("^[\\p{L}\\p{M} ]+$") || hoTen.length() < 2 || hoTen.length() > 50) {
            etHoTen.setError("Họ tên từ 2-50 ký tự, chỉ chứa chữ cái và khoảng trắng");
            etHoTen.requestFocus();
            return;
        }

        if (ngaySinh.isEmpty()) {
            etNgaySinh.setError("Vui lòng chọn ngày sinh");
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            etNgaySinh.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không đúng định dạng, ví dụ: ten@hou.edu.vn");
            etEmail.requestFocus();
            return;
        }
        email = email.toLowerCase(Locale.ROOT);

        if (listLop.isEmpty() || spinnerLop.getSelectedItemPosition() < 0 || spinnerLop.getSelectedItemPosition() >= listLop.size()) {
            Toast.makeText(this, "Chưa chọn lớp học hoặc chưa có lớp học nào. Vui lòng tạo lớp học trước!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedLopPos = spinnerLop.getSelectedItemPosition();
        String maLop = listLop.get(selectedLopPos).getMaLop();

        SinhVien sv = new SinhVien(mssv, hoTen, ngaySinh, email, maLop, "");

        if (!isEditMode) {
            if (sinhVienDao.getSinhVienByMSSV(mssv) != null) {
                etMSSV.setError("MSSV này đã tồn tại trong hệ thống!");
                etMSSV.requestFocus();
                return;
            }

            boolean ok = sinhVienDao.themSinhVien(sv);
            if (ok) {
                Toast.makeText(this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi lưu!", Toast.LENGTH_SHORT).show();
            }
        } else {
            boolean ok = sinhVienDao.suaSinhVien(sv);
            if (ok) {
                Toast.makeText(this, "Cập nhật sinh viên thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi cập nhật!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
