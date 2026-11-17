package com.hou.quanlysinhvien.activity;

import android.os.Bundle;
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
import com.hou.quanlysinhvien.database.MonHocDao;
import com.hou.quanlysinhvien.model.LopHoc;
import com.hou.quanlysinhvien.model.MonHoc;

public class ClassSubjectAddEditActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_IS_EDIT = "EXTRA_IS_EDIT";
    public static final String EXTRA_CODE = "EXTRA_CODE";

    public static final String TYPE_LOP = "LOP";
    public static final String TYPE_MON = "MON";

    private FrameLayout btnBackClassSubject;
    private TextView tvTitleClassSubjectAddEdit;
    private Spinner spinnerType;
    private TextView tvLabelCode;
    private EditText etCode;
    private TextView tvLabelName;
    private EditText etName;
    private AppCompatButton btnSaveClassSubject;
    private AppCompatButton btnCancelClassSubject;

    private LopHocDao lopHocDao;
    private MonHocDao monHocDao;

    private boolean isEdit = false;
    private String type = TYPE_LOP;
    private String codeEdit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_subject_add_edit);

        lopHocDao = new LopHocDao(this);
        monHocDao = new MonHocDao(this);

        initViews();
        setupSpinnerType();
        getIntentData();
        setupEvents();
    }

    private void initViews() {
        btnBackClassSubject = findViewById(R.id.btnBackClassSubject);
        tvTitleClassSubjectAddEdit = findViewById(R.id.tvTitleClassSubjectAddEdit);
        spinnerType = findViewById(R.id.spinnerType);
        tvLabelCode = findViewById(R.id.tvLabelCode);
        etCode = findViewById(R.id.etCode);
        tvLabelName = findViewById(R.id.tvLabelName);
        etName = findViewById(R.id.etName);
        btnSaveClassSubject = findViewById(R.id.btnSaveClassSubject);
        btnCancelClassSubject = findViewById(R.id.btnCancelClassSubject);
    }

    private void setupSpinnerType() {
        String[] types = new String[]{"Lớp học", "Môn học"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void getIntentData() {
        isEdit = getIntent().getBooleanExtra(EXTRA_IS_EDIT, false);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        if (type == null) type = TYPE_LOP;

        if (isEdit) {
            codeEdit = getIntent().getStringExtra(EXTRA_CODE);
            tvTitleClassSubjectAddEdit.setText(type.equals(TYPE_LOP) ? "Sửa lớp học" : "Sửa môn học");
            etCode.setEnabled(false);
            spinnerType.setEnabled(false);

            if (type.equals(TYPE_LOP)) {
                spinnerType.setSelection(0);
                LopHoc lop = lopHocDao.getLopHocByMa(codeEdit);
                if (lop != null) {
                    etCode.setText(lop.getMaLop());
                    etName.setText(lop.getTenLop());
                }
            } else {
                spinnerType.setSelection(1);
                MonHoc mon = monHocDao.getMonHocByMa(codeEdit);
                if (mon != null) {
                    etCode.setText(mon.getMaMon());
                    etName.setText(mon.getTenMon());
                }
            }
        } else {
            tvTitleClassSubjectAddEdit.setText(type.equals(TYPE_LOP) ? "Thêm lớp học" : "Thêm môn học");
            spinnerType.setSelection(type.equals(TYPE_LOP) ? 0 : 1);
        }
    }

    private void setupEvents() {
        btnBackClassSubject.setOnClickListener(v -> finish());
        btnCancelClassSubject.setOnClickListener(v -> finish());
        btnSaveClassSubject.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        String code = etCode.getText().toString().trim();
        String name = etName.getText().toString().trim();

        if (code.isEmpty()) {
            etCode.setError("Vui lòng nhập mã");
            etCode.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            etName.setError("Vui lòng nhập tên");
            etName.requestFocus();
            return;
        }

        boolean isLop = spinnerType.getSelectedItemPosition() == 0;

        if (isLop) {
            LopHoc lop = new LopHoc(code, name, 0);
            if (!isEdit) {
                if (lopHocDao.getLopHocByMa(code) != null) {
                    etCode.setError("Mã lớp này đã tồn tại!");
                    etCode.requestFocus();
                    return;
                }
                boolean ok = lopHocDao.themLopHoc(lop);
                if (ok) {
                    Toast.makeText(this, "Thêm lớp thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean ok = lopHocDao.suaLopHoc(lop);
                if (ok) {
                    Toast.makeText(this, "Cập nhật lớp thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            MonHoc mon = new MonHoc(code, name);
            if (!isEdit) {
                if (monHocDao.getMonHocByMa(code) != null) {
                    etCode.setError("Mã môn này đã tồn tại!");
                    etCode.requestFocus();
                    return;
                }
                boolean ok = monHocDao.themMonHoc(mon);
                if (ok) {
                    Toast.makeText(this, "Thêm môn thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean ok = monHocDao.suaMonHoc(mon);
                if (ok) {
                    Toast.makeText(this, "Cập nhật môn thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
