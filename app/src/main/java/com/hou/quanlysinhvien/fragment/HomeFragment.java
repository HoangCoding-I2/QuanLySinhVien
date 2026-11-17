package com.hou.quanlysinhvien.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.activity.MainActivity;
import com.hou.quanlysinhvien.database.LopHocDao;
import com.hou.quanlysinhvien.database.SinhVienDao;

public class HomeFragment extends Fragment {

    private TextView tvTotalStudents;
    private TextView tvTotalClasses;

    private LinearLayout cardQuickStudent;
    private LinearLayout cardQuickClass;
    private LinearLayout btnMenuStudent;
    private LinearLayout btnMenuClassSubject;
    private LinearLayout btnMenuGrade;
    private LinearLayout btnMenuStat;

    private SinhVienDao sinhVienDao;
    private LopHocDao lopHocDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setupEvents();
        return view;
    }

    private void initViews(View view) {
        tvTotalStudents = view.findViewById(R.id.tvTotalStudents);
        tvTotalClasses = view.findViewById(R.id.tvTotalClasses);

        cardQuickStudent = view.findViewById(R.id.cardQuickStudent);
        cardQuickClass = view.findViewById(R.id.cardQuickClass);
        btnMenuStudent = view.findViewById(R.id.btnMenuStudent);
        btnMenuClassSubject = view.findViewById(R.id.btnMenuClassSubject);
        btnMenuGrade = view.findViewById(R.id.btnMenuGrade);
        btnMenuStat = view.findViewById(R.id.btnMenuStat);

        sinhVienDao = new SinhVienDao(requireContext());
        lopHocDao = new LopHocDao(requireContext());
    }

    private void setupEvents() {
        if (getActivity() instanceof MainActivity) {
            MainActivity main = (MainActivity) getActivity();

            View.OnClickListener toStudentTab = v -> main.switchTab(R.id.menu_student);
            cardQuickStudent.setOnClickListener(toStudentTab);
            btnMenuStudent.setOnClickListener(toStudentTab);

            View.OnClickListener toClassSubjectTab = v -> main.switchTab(R.id.menu_class_subject);
            cardQuickClass.setOnClickListener(toClassSubjectTab);
            btnMenuClassSubject.setOnClickListener(toClassSubjectTab);

            btnMenuGrade.setOnClickListener(v -> main.switchTab(R.id.menu_grade));

            btnMenuStat.setOnClickListener(v -> main.switchTab(R.id.menu_stat));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadQuickStatistics();
    }

    private void loadQuickStatistics() {
        int totalSv = sinhVienDao.getTongSinhVien();
        int totalLop = lopHocDao.getTongSoLop();

        tvTotalStudents.setText(String.valueOf(totalSv));
        tvTotalClasses.setText(String.valueOf(totalLop));
    }
}
