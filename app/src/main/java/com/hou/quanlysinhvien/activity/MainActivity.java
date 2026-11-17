package com.hou.quanlysinhvien.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hou.quanlysinhvien.R;
import com.hou.quanlysinhvien.fragment.ClassSubjectFragment;
import com.hou.quanlysinhvien.fragment.GradeFragment;
import com.hou.quanlysinhvien.fragment.HomeFragment;
import com.hou.quanlysinhvien.fragment.StatisticsFragment;
import com.hou.quanlysinhvien.fragment.StudentListFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private final Fragment homeFragment = new HomeFragment();
    private final Fragment studentListFragment = new StudentListFragment();
    private final Fragment classSubjectFragment = new ClassSubjectFragment();
    private final Fragment gradeFragment = new GradeFragment();
    private final Fragment statisticsFragment = new StatisticsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        if (savedInstanceState == null) {
            loadFragment(homeFragment);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                loadFragment(homeFragment);
                return true;
            } else if (itemId == R.id.menu_student) {
                loadFragment(studentListFragment);
                return true;
            } else if (itemId == R.id.menu_class_subject) {
                loadFragment(classSubjectFragment);
                return true;
            } else if (itemId == R.id.menu_grade) {
                loadFragment(gradeFragment);
                return true;
            } else if (itemId == R.id.menu_stat) {
                loadFragment(statisticsFragment);
                return true;
            }
            return false;
        });
    }

    public void switchTab(int menuItemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(menuItemId);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}
