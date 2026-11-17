package com.hou.quanlysinhvien.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hou.quanlysinhvien.model.LopHoc;

import java.util.ArrayList;
import java.util.List;

public class LopHocDao {
    private final DatabaseHelper dbHelper;

    public LopHocDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<LopHoc> getAllLopHoc() {
        List<LopHoc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LOP_HOC + " ORDER BY " + DatabaseHelper.COL_LOP_MA + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                LopHoc lop = new LopHoc();
                lop.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOP_MA)));
                lop.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOP_TEN)));
                lop.setSiSo(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOP_SISO)));
                list.add(lop);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public LopHoc getLopHocByMa(String maLop) {
        if (maLop == null || maLop.trim().isEmpty()) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_LOP_HOC, null,
                DatabaseHelper.COL_LOP_MA + "=?", new String[]{maLop}, null, null, null);
        LopHoc lop = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                lop = new LopHoc();
                lop.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOP_MA)));
                lop.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOP_TEN)));
                lop.setSiSo(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOP_SISO)));
            }
            cursor.close();
        }
        return lop;
    }

    public boolean themLopHoc(LopHoc lop) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_LOP_MA, lop.getMaLop().trim());
        values.put(DatabaseHelper.COL_LOP_TEN, lop.getTenLop().trim());
        values.put(DatabaseHelper.COL_LOP_SISO, lop.getSiSo());
        long result = db.insert(DatabaseHelper.TABLE_LOP_HOC, null, values);
        return result != -1;
    }

    public boolean suaLopHoc(LopHoc lop) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_LOP_TEN, lop.getTenLop().trim());
        values.put(DatabaseHelper.COL_LOP_SISO, lop.getSiSo());
        int rows = db.update(DatabaseHelper.TABLE_LOP_HOC, values,
                DatabaseHelper.COL_LOP_MA + "=?", new String[]{lop.getMaLop()});
        return rows > 0;
    }

    public boolean xoaLopHoc(String maLop) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_LOP_HOC,
                DatabaseHelper.COL_LOP_MA + "=?", new String[]{maLop});
        return rows > 0;
    }

    public List<LopHoc> timKiemLopHoc(String keyword) {
        List<LopHoc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_LOP_HOC +
                " WHERE " + DatabaseHelper.COL_LOP_MA + " LIKE ? OR " +
                DatabaseHelper.COL_LOP_TEN + " LIKE ? ORDER BY " + DatabaseHelper.COL_LOP_MA;
        String pattern = "%" + keyword.trim() + "%";
        Cursor cursor = db.rawQuery(query, new String[]{pattern, pattern});

        if (cursor.moveToFirst()) {
            do {
                LopHoc lop = new LopHoc();
                lop.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOP_MA)));
                lop.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOP_TEN)));
                lop.setSiSo(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOP_SISO)));
                list.add(lop);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void capNhatSiSo(String maLop) {
        if (maLop == null || maLop.isEmpty()) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_SINH_VIEN +
                " WHERE " + DatabaseHelper.COL_SV_MALOP + "=?", new String[]{maLop});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_LOP_SISO, count);
        db.update(DatabaseHelper.TABLE_LOP_HOC, values, DatabaseHelper.COL_LOP_MA + "=?", new String[]{maLop});
    }

    public int getTongSoLop() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_LOP_HOC, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
