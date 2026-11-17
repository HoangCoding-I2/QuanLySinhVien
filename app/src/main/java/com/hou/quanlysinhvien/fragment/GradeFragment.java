package com.hou.quanlysinhvien.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.adapter.DiemNhapAdapter;
import com.hou.quanlysinhvien.adapter.DiemXemAdapter;
import com.hou.quanlysinhvien.database.DiemDao;
import com.hou.quanlysinhvien.database.LopHocDao;
import com.hou.quanlysinhvien.database.MonHocDao;
import com.hou.quanlysinhvien.model.Diem;
import com.hou.quanlysinhvien.model.LopHoc;
import com.hou.quanlysinhvien.model.MonHoc;

import java.util.ArrayList;
import java.util.List;

public class GradeFragment extends Fragment {

    private TextView btnTabGradeInput;
    private TextView btnTabGradeView;
    private Spinner spinnerGradeLop;
    private Spinner spinnerGradeMon;
    private RecyclerView rvGradeInput;
    private RecyclerView rvGradeView;
    private TextView tvEmptyGrade;
    private AppCompatButton btnSaveGradeList;

    private LopHocDao lopHocDao;
    private MonHocDao monHocDao;
    private DiemDao diemDao;

    private List<LopHoc> listLop;
    private List<MonHoc> listMon;

    private DiemNhapAdapter nhapAdapter;
    private DiemXemAdapter xemAdapter;

    private boolean isInputTab = true;
    private String selectedMaLop = "";
    private String selectedMaMon = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        initViews(view);
        setupRecyclerViews();
        setupSpinners();
        setupEvents();
        return view;
    }

    private void initViews(View view) {
        btnTabGradeInput = view.findViewById(R.id.btnTabGradeInput);
        btnTabGradeView = view.findViewById(R.id.btnTabGradeView);
        spinnerGradeLop = view.findViewById(R.id.spinnerGradeLop);
        spinnerGradeMon = view.findViewById(R.id.spinnerGradeMon);
        rvGradeInput = view.findViewById(R.id.rvGradeInput);
        rvGradeView = view.findViewById(R.id.rvGradeView);
        tvEmptyGrade = view.findViewById(R.id.tvEmptyGrade);
        btnSaveGradeList = view.findViewById(R.id.btnSaveGradeList);

        lopHocDao = new LopHocDao(requireContext());
        monHocDao = new MonHocDao(requireContext());
        diemDao = new DiemDao(requireContext());
    }

    private void setupRecyclerViews() {
        rvGradeInput.setLayoutManager(new LinearLayoutManager(requireContext()));
        nhapAdapter = new DiemNhapAdapter(requireContext(), new ArrayList<>());
        rvGradeInput.setAdapter(nhapAdapter);

        rvGradeView.setLayoutManager(new LinearLayoutManager(requireContext()));
        xemAdapter = new DiemXemAdapter(requireContext(), new ArrayList<>());
        rvGradeView.setAdapter(xemAdapter);
    }

    private void setupSpinners() {
        listLop = lopHocDao.getAllLopHoc();
        listMon = monHocDao.getAllMonHoc();

        List<String> lopNames = new ArrayList<>();
        for (LopHoc lop : listLop) {
            lopNames.add(lop.getTenLop() + " (" + lop.getMaLop() + ")");
        }
        ArrayAdapter<String> lopAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, lopNames);
        lopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGradeLop.setAdapter(lopAdapter);

        List<String> monNames = new ArrayList<>();
        for (MonHoc mon : listMon) {
            monNames.add(mon.getTenMon() + " (" + mon.getMaMon() + ")");
        }
        ArrayAdapter<String> monAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, monNames);
        monAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGradeMon.setAdapter(monAdapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSelectedIds();
                loadGradeData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerGradeLop.setOnItemSelectedListener(listener);
        spinnerGradeMon.setOnItemSelectedListener(listener);

        updateSelectedIds();
    }

    private void updateSelectedIds() {
        if (!listLop.isEmpty() && spinnerGradeLop.getSelectedItemPosition() >= 0) {
            selectedMaLop = listLop.get(spinnerGradeLop.getSelectedItemPosition()).getMaLop();
        } else {
            selectedMaLop = "";
        }

        if (!listMon.isEmpty() && spinnerGradeMon.getSelectedItemPosition() >= 0) {
            selectedMaMon = listMon.get(spinnerGradeMon.getSelectedItemPosition()).getMaMon();
        } else {
            selectedMaMon = "";
        }
    }

    private void setupEvents() {
        btnTabGradeInput.setOnClickListener(v -> {
            if (!isInputTab) {
                isInputTab = true;
                updateTabUI();
                loadGradeData();
            }
        });

        btnTabGradeView.setOnClickListener(v -> {
            if (isInputTab) {
                isInputTab = false;
                updateTabUI();
                loadGradeData();
            }
        });

        btnSaveGradeList.setOnClickListener(v -> saveGrades());
    }

    private void updateTabUI() {
        if (isInputTab) {
            btnTabGradeInput.setBackgroundResource(R.drawable.bg_tab_active);
            btnTabGradeInput.setTextColor(getResources().getColor(R.color.white));
            btnTabGradeView.setBackgroundResource(android.R.color.transparent);
            btnTabGradeView.setTextColor(getResources().getColor(R.color.text_secondary));

            rvGradeInput.setVisibility(View.VISIBLE);
            rvGradeView.setVisibility(View.GONE);
            btnSaveGradeList.setVisibility(View.VISIBLE);
        } else {
            btnTabGradeView.setBackgroundResource(R.drawable.bg_tab_active);
            btnTabGradeView.setTextColor(getResources().getColor(R.color.white));
            btnTabGradeInput.setBackgroundResource(android.R.color.transparent);
            btnTabGradeInput.setTextColor(getResources().getColor(R.color.text_secondary));

            rvGradeInput.setVisibility(View.GONE);
            rvGradeView.setVisibility(View.VISIBLE);
            btnSaveGradeList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGradeData();
    }

    private void loadGradeData() {
        if (selectedMaLop.isEmpty() || selectedMaMon.isEmpty()) {
            tvEmptyGrade.setVisibility(View.VISIBLE);
            tvEmptyGrade.setText("Vui lòng thêm Lớp học và Môn học trước!");
            rvGradeInput.setVisibility(View.GONE);
            rvGradeView.setVisibility(View.GONE);
            btnSaveGradeList.setVisibility(View.GONE);
            return;
        }

        List<Diem> list = diemDao.getBangDiemByLopVaMon(selectedMaLop, selectedMaMon);

        if (list.isEmpty()) {
            tvEmptyGrade.setVisibility(View.VISIBLE);
            tvEmptyGrade.setText("Lớp này hiện chưa có sinh viên nào!");
            rvGradeInput.setVisibility(View.GONE);
            rvGradeView.setVisibility(View.GONE);
            btnSaveGradeList.setVisibility(View.GONE);
        } else {
            tvEmptyGrade.setVisibility(View.GONE);
            if (isInputTab) {
                rvGradeInput.setVisibility(View.VISIBLE);
                rvGradeView.setVisibility(View.GONE);
                btnSaveGradeList.setVisibility(View.VISIBLE);
                nhapAdapter.updateData(list);
            } else {
                rvGradeInput.setVisibility(View.GONE);
                rvGradeView.setVisibility(View.VISIBLE);
                btnSaveGradeList.setVisibility(View.GONE);
                xemAdapter.updateData(list);
            }
        }
    }

    private void saveGrades() {
        List<Diem> currentList = nhapAdapter.getListDiem();
        if (currentList == null || currentList.isEmpty()) {
            Toast.makeText(requireContext(), "Không có dữ liệu điểm để lưu!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean ok = diemDao.luuDanhSachDiem(currentList);
        if (ok) {
            Toast.makeText(requireContext(), "Đã lưu bảng điểm thành công!", Toast.LENGTH_SHORT).show();
            loadGradeData();
        } else {
            Toast.makeText(requireContext(), "Lưu bảng điểm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
