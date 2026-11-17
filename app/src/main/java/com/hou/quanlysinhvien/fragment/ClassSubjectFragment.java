package com.hou.quanlysinhvien.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.activity.ClassSubjectAddEditActivity;
import com.hou.quanlysinhvien.adapter.LopHocAdapter;
import com.hou.quanlysinhvien.adapter.MonHocAdapter;
import com.hou.quanlysinhvien.database.LopHocDao;
import com.hou.quanlysinhvien.database.MonHocDao;
import com.hou.quanlysinhvien.model.LopHoc;
import com.hou.quanlysinhvien.model.MonHoc;

import java.util.ArrayList;
import java.util.List;

public class ClassSubjectFragment extends Fragment {

    private FrameLayout btnAddClassSubject;
    private TextView btnTabLop;
    private TextView btnTabMon;
    private EditText etSearchClassSubject;
    private RecyclerView rvClassSubject;
    private TextView tvEmptyClassSubject;

    private LopHocDao lopHocDao;
    private MonHocDao monHocDao;

    private LopHocAdapter lopAdapter;
    private MonHocAdapter monAdapter;

    private boolean isTabLop = true;
    private String currentKeyword = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_subject, container, false);
        initViews(view);
        setupRecyclerView();
        setupEvents();
        return view;
    }

    private void initViews(View view) {
        btnAddClassSubject = view.findViewById(R.id.btnAddClassSubject);
        btnTabLop = view.findViewById(R.id.btnTabLop);
        btnTabMon = view.findViewById(R.id.btnTabMon);
        etSearchClassSubject = view.findViewById(R.id.etSearchClassSubject);
        rvClassSubject = view.findViewById(R.id.rvClassSubject);
        tvEmptyClassSubject = view.findViewById(R.id.tvEmptyClassSubject);

        lopHocDao = new LopHocDao(requireContext());
        monHocDao = new MonHocDao(requireContext());
    }

    private void setupRecyclerView() {
        rvClassSubject.setLayoutManager(new LinearLayoutManager(requireContext()));

        lopAdapter = new LopHocAdapter(requireContext(), new ArrayList<>(), new LopHocAdapter.OnLopActionListener() {
            @Override
            public void onEdit(LopHoc lopHoc) {
                Intent intent = new Intent(requireContext(), ClassSubjectAddEditActivity.class);
                intent.putExtra(ClassSubjectAddEditActivity.EXTRA_TYPE, ClassSubjectAddEditActivity.TYPE_LOP);
                intent.putExtra(ClassSubjectAddEditActivity.EXTRA_IS_EDIT, true);
                intent.putExtra(ClassSubjectAddEditActivity.EXTRA_CODE, lopHoc.getMaLop());
                startActivity(intent);
            }

            @Override
            public void onDelete(LopHoc lopHoc) {
                showDeleteLopDialog(lopHoc);
            }
        });

        monAdapter = new MonHocAdapter(requireContext(), new ArrayList<>(), new MonHocAdapter.OnMonActionListener() {
            @Override
            public void onEdit(MonHoc monHoc) {
                Intent intent = new Intent(requireContext(), ClassSubjectAddEditActivity.class);
                intent.putExtra(ClassSubjectAddEditActivity.EXTRA_TYPE, ClassSubjectAddEditActivity.TYPE_MON);
                intent.putExtra(ClassSubjectAddEditActivity.EXTRA_IS_EDIT, true);
                intent.putExtra(ClassSubjectAddEditActivity.EXTRA_CODE, monHoc.getMaMon());
                startActivity(intent);
            }

            @Override
            public void onDelete(MonHoc monHoc) {
                showDeleteMonDialog(monHoc);
            }
        });

        rvClassSubject.setAdapter(lopAdapter);
    }

    private void setupEvents() {
        btnTabLop.setOnClickListener(v -> {
            if (!isTabLop) {
                isTabLop = true;
                updateTabUI();
                rvClassSubject.setAdapter(lopAdapter);
                loadData();
            }
        });

        btnTabMon.setOnClickListener(v -> {
            if (isTabLop) {
                isTabLop = false;
                updateTabUI();
                rvClassSubject.setAdapter(monAdapter);
                loadData();
            }
        });

        btnAddClassSubject.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ClassSubjectAddEditActivity.class);
            intent.putExtra(ClassSubjectAddEditActivity.EXTRA_TYPE, isTabLop ? ClassSubjectAddEditActivity.TYPE_LOP : ClassSubjectAddEditActivity.TYPE_MON);
            intent.putExtra(ClassSubjectAddEditActivity.EXTRA_IS_EDIT, false);
            startActivity(intent);
        });

        etSearchClassSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentKeyword = s.toString().trim();
                loadData();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateTabUI() {
        if (isTabLop) {
            btnTabLop.setBackgroundResource(R.drawable.bg_tab_active);
            btnTabLop.setTextColor(getResources().getColor(R.color.white));
            btnTabMon.setBackgroundResource(android.R.color.transparent);
            btnTabMon.setTextColor(getResources().getColor(R.color.text_secondary));
        } else {
            btnTabMon.setBackgroundResource(R.drawable.bg_tab_active);
            btnTabMon.setTextColor(getResources().getColor(R.color.white));
            btnTabLop.setBackgroundResource(android.R.color.transparent);
            btnTabLop.setTextColor(getResources().getColor(R.color.text_secondary));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (isTabLop) {
            List<LopHoc> list;
            if (currentKeyword.isEmpty()) {
                list = lopHocDao.getAllLopHoc();
            } else {
                list = lopHocDao.timKiemLopHoc(currentKeyword);
            }
            lopAdapter.updateData(list);
            tvEmptyClassSubject.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            rvClassSubject.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
        } else {
            List<MonHoc> list;
            if (currentKeyword.isEmpty()) {
                list = monHocDao.getAllMonHoc();
            } else {
                list = monHocDao.timKiemMonHoc(currentKeyword);
            }
            monAdapter.updateData(list);
            tvEmptyClassSubject.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            rvClassSubject.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    private void showDeleteLopDialog(LopHoc lop) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa lớp")
                .setMessage("Bạn có chắc chắn muốn xóa lớp " + lop.getTenLop() + " (" + lop.getMaLop() + ") không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean ok = lopHocDao.xoaLopHoc(lop.getMaLop());
                    if (ok) {
                        Toast.makeText(requireContext(), "Đã xóa lớp thành công!", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(requireContext(), "Xóa lớp thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showDeleteMonDialog(MonHoc mon) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa môn học")
                .setMessage("Bạn có chắc chắn muốn xóa môn " + mon.getTenMon() + " (" + mon.getMaMon() + ") không?\nBảng điểm liên quan cũng sẽ bị xóa.")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean ok = monHocDao.xoaMonHoc(mon.getMaMon());
                    if (ok) {
                        Toast.makeText(requireContext(), "Đã xóa môn học thành công!", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(requireContext(), "Xóa môn học thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
