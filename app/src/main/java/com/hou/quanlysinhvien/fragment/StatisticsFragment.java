package com.hou.quanlysinhvien.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.database.DiemDao;
import com.hou.quanlysinhvien.database.LopHocDao;
import com.hou.quanlysinhvien.database.MonHocDao;
import com.hou.quanlysinhvien.model.LopHoc;
import com.hou.quanlysinhvien.model.MonHoc;
import com.hou.quanlysinhvien.model.ThongKeResult;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private Spinner spinnerStatLop;
    private Spinner spinnerStatMon;

    private TextView tvStatAvgGrade;
    private TextView tvStatPassRate;
    private TextView tvStatMaxGrade;
    private TextView tvStatMinGrade;

    private ProgressBar pbGioi;
    private ProgressBar pbKha;
    private ProgressBar pbTB;
    private ProgressBar pbYeu;

    private TextView tvCountGioi;
    private TextView tvCountKha;
    private TextView tvCountTB;
    private TextView tvCountYeu;

    private LopHocDao lopHocDao;
    private MonHocDao monHocDao;
    private DiemDao diemDao;

    private List<LopHoc> listLop;
    private List<MonHoc> listMon;

    private String selectedMaLop = "";
    private String selectedMaMon = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initViews(view);
        setupSpinners();
        return view;
    }

    private void initViews(View view) {
        spinnerStatLop = view.findViewById(R.id.spinnerStatLop);
        spinnerStatMon = view.findViewById(R.id.spinnerStatMon);

        tvStatAvgGrade = view.findViewById(R.id.tvStatAvgGrade);
        tvStatPassRate = view.findViewById(R.id.tvStatPassRate);
        tvStatMaxGrade = view.findViewById(R.id.tvStatMaxGrade);
        tvStatMinGrade = view.findViewById(R.id.tvStatMinGrade);

        pbGioi = view.findViewById(R.id.pbGioi);
        pbKha = view.findViewById(R.id.pbKha);
        pbTB = view.findViewById(R.id.pbTB);
        pbYeu = view.findViewById(R.id.pbYeu);

        tvCountGioi = view.findViewById(R.id.tvCountGioi);
        tvCountKha = view.findViewById(R.id.tvCountKha);
        tvCountTB = view.findViewById(R.id.tvCountTB);
        tvCountYeu = view.findViewById(R.id.tvCountYeu);

        lopHocDao = new LopHocDao(requireContext());
        monHocDao = new MonHocDao(requireContext());
        diemDao = new DiemDao(requireContext());
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
        spinnerStatLop.setAdapter(lopAdapter);

        List<String> monNames = new ArrayList<>();
        for (MonHoc mon : listMon) {
            monNames.add(mon.getTenMon() + " (" + mon.getMaMon() + ")");
        }
        ArrayAdapter<String> monAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, monNames);
        monAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatMon.setAdapter(monAdapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSelectedIds();
                loadStatistics();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerStatLop.setOnItemSelectedListener(listener);
        spinnerStatMon.setOnItemSelectedListener(listener);

        updateSelectedIds();
    }

    private void updateSelectedIds() {
        if (!listLop.isEmpty() && spinnerStatLop.getSelectedItemPosition() >= 0) {
            selectedMaLop = listLop.get(spinnerStatLop.getSelectedItemPosition()).getMaLop();
        } else {
            selectedMaLop = "";
        }

        if (!listMon.isEmpty() && spinnerStatMon.getSelectedItemPosition() >= 0) {
            selectedMaMon = listMon.get(spinnerStatMon.getSelectedItemPosition()).getMaMon();
        } else {
            selectedMaMon = "";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatistics();
    }

    private void loadStatistics() {
        if (selectedMaLop.isEmpty() || selectedMaMon.isEmpty()) {
            resetStatsUI();
            return;
        }

        ThongKeResult stat = diemDao.getThongKe(selectedMaLop, selectedMaMon);

        if (stat.getTongSoSV() > 0) {
            tvStatAvgGrade.setText(String.valueOf(stat.getDiemTBLop()));
            tvStatPassRate.setText(stat.getTiLeDat() + "%");
            tvStatMaxGrade.setText(String.valueOf(stat.getCaoNhat()));
            tvStatMinGrade.setText(String.valueOf(stat.getThapNhat()));

            int total = stat.getTongSoSV();

            pbGioi.setProgress((int) ((float) stat.getSoLuongGioi() / total * 100));
            pbKha.setProgress((int) ((float) stat.getSoLuongKha() / total * 100));
            pbTB.setProgress((int) ((float) stat.getSoLuongTrungBinh() / total * 100));
            pbYeu.setProgress((int) ((float) stat.getSoLuongYeu() / total * 100));

            tvCountGioi.setText(stat.getSoLuongGioi() + " SV");
            tvCountKha.setText(stat.getSoLuongKha() + " SV");
            tvCountTB.setText(stat.getSoLuongTrungBinh() + " SV");
            tvCountYeu.setText(stat.getSoLuongYeu() + " SV");
        } else {
            resetStatsUI();
        }
    }

    private void resetStatsUI() {
        tvStatAvgGrade.setText("--");
        tvStatPassRate.setText("--");
        tvStatMaxGrade.setText("--");
        tvStatMinGrade.setText("--");

        pbGioi.setProgress(0);
        pbKha.setProgress(0);
        pbTB.setProgress(0);
        pbYeu.setProgress(0);

        tvCountGioi.setText("0 SV");
        tvCountKha.setText("0 SV");
        tvCountTB.setText("0 SV");
        tvCountYeu.setText("0 SV");
    }
}
