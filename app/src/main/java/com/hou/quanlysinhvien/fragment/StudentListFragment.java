package com.hou.quanlysinhvien.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.activity.StudentAddEditActivity;
import com.hou.quanlysinhvien.activity.StudentDetailActivity;
import com.hou.quanlysinhvien.adapter.SinhVienAdapter;
import com.hou.quanlysinhvien.database.LopHocDao;
import com.hou.quanlysinhvien.database.SinhVienDao;
import com.hou.quanlysinhvien.model.LopHoc;
import com.hou.quanlysinhvien.model.SinhVien;

import java.util.ArrayList;
import java.util.List;

public class StudentListFragment extends Fragment {

    private FrameLayout btnAddStudent;
    private EditText etSearchStudent;
    private Spinner spinnerFilterLop;
    private RecyclerView rvStudents;
    private TextView tvEmptyStudent;

    private SinhVienDao sinhVienDao;
    private LopHocDao lopHocDao;
    private SinhVienAdapter adapter;

    private String currentKeyword = "";
    private String currentMaLopFilter = "Tất cả lớp";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        initViews(view);
        setupRecyclerView();
        setupSpinner();
        setupEvents();
        return view;
    }

    private void initViews(View view) {
        btnAddStudent = view.findViewById(R.id.btnAddStudent);
        etSearchStudent = view.findViewById(R.id.etSearchStudent);
        spinnerFilterLop = view.findViewById(R.id.spinnerFilterLop);
        rvStudents = view.findViewById(R.id.rvStudents);
        tvEmptyStudent = view.findViewById(R.id.tvEmptyStudent);

        sinhVienDao = new SinhVienDao(requireContext());
        lopHocDao = new LopHocDao(requireContext());
    }

    private void setupRecyclerView() {
        rvStudents.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SinhVienAdapter(requireContext(), new ArrayList<>(), sinhVien -> {
            Intent intent = new Intent(requireContext(), StudentDetailActivity.class);
            intent.putExtra(StudentDetailActivity.EXTRA_MSSV, sinhVien.getMssv());
            startActivity(intent);
        });
        rvStudents.setAdapter(adapter);
    }

    private void setupSpinner() {
        List<LopHoc> listLop = lopHocDao.getAllLopHoc();
        List<String> listLopNames = new ArrayList<>();
        listLopNames.add("Tất cả lớp");
        if (listLop != null) {
            for (LopHoc lop : listLop) {
                if (lop != null && lop.getMaLop() != null && !lop.getMaLop().trim().isEmpty()) {
                    listLopNames.add(lop.getMaLop());
                }
            }
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listLopNames
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterLop.setAdapter(spinnerAdapter);

        spinnerFilterLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < listLopNames.size()) {
                    currentMaLopFilter = listLopNames.get(position);
                    filterStudents();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupEvents() {
        btnAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), StudentAddEditActivity.class);
            startActivity(intent);
        });

        etSearchStudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentKeyword = s.toString().trim();
                filterStudents();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setupSpinner();
        filterStudents();
    }

    private void filterStudents() {
        List<SinhVien> result = sinhVienDao.timKiemSinhVien(currentKeyword, currentMaLopFilter);
        adapter.updateData(result);

        if (result.isEmpty()) {
            tvEmptyStudent.setVisibility(View.VISIBLE);
            rvStudents.setVisibility(View.GONE);
        } else {
            tvEmptyStudent.setVisibility(View.GONE);
            rvStudents.setVisibility(View.VISIBLE);
        }
    }
}
