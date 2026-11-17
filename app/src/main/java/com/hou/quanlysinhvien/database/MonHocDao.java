package com.hou.quanlysinhvien.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hou.quanlysinhvien.model.MonHoc;

import java.util.ArrayList;
import java.util.List;

public class MonHocDao {
    private final DatabaseHelper dbHelper;

    public MonHocDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<MonHoc> getAllMonHoc() {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_MON_HOC + " ORDER BY " + DatabaseHelper.COL_MH_MA + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                MonHoc mon = new MonHoc();
                mon.setMaMon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MH_MA)));
                mon.setTenMon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MH_TEN)));
                list.add(mon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public MonHoc getMonHocByMa(String maMon) {
        if (maMon == null || maMon.trim().isEmpty()) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_MON_HOC, null,
                DatabaseHelper.COL_MH_MA + "=?", new String[]{maMon}, null, null, null);
        MonHoc mon = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mon = new MonHoc();
                mon.setMaMon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MH_MA)));
                mon.setTenMon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MH_TEN)));
            }
            cursor.close();
        }
        return mon;
    }

    public boolean themMonHoc(MonHoc mon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_MH_MA, mon.getMaMon().trim());
        values.put(DatabaseHelper.COL_MH_TEN, mon.getTenMon().trim());
        long result = db.insert(DatabaseHelper.TABLE_MON_HOC, null, values);
        return result != -1;
    }

    public boolean suaMonHoc(MonHoc mon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_MH_TEN, mon.getTenMon().trim());
        int rows = db.update(DatabaseHelper.TABLE_MON_HOC, values,
                DatabaseHelper.COL_MH_MA + "=?", new String[]{mon.getMaMon()});
        return rows > 0;
    }

    public boolean xoaMonHoc(String maMon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_MON_HOC,
                DatabaseHelper.COL_MH_MA + "=?", new String[]{maMon});
        return rows > 0;
    }

    public List<MonHoc> timKiemMonHoc(String keyword) {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_MON_HOC +
                " WHERE " + DatabaseHelper.COL_MH_MA + " LIKE ? OR " +
                DatabaseHelper.COL_MH_TEN + " LIKE ? ORDER BY " + DatabaseHelper.COL_MH_MA;
        String pattern = "%" + keyword.trim() + "%";
        Cursor cursor = db.rawQuery(query, new String[]{pattern, pattern});

        if (cursor.moveToFirst()) {
            do {
                MonHoc mon = new MonHoc();
                mon.setMaMon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MH_MA)));
                mon.setTenMon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MH_TEN)));
                list.add(mon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int getTongSoMon() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_MON_HOC, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
