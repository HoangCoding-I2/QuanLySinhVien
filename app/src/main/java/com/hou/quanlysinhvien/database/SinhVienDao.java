package com.hou.quanlysinhvien.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hou.quanlysinhvien.model.SinhVien;

import java.util.ArrayList;
import java.util.List;

public class SinhVienDao {
    private final DatabaseHelper dbHelper;
    private final LopHocDao lopHocDao;

    public SinhVienDao(Context context) {
        dbHelper = new DatabaseHelper(context);
        lopHocDao = new LopHocDao(context);
    }

    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_SINH_VIEN + " ORDER BY " + DatabaseHelper.COL_SV_HOTEN + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                SinhVien sv = mapCursorToSinhVien(cursor);
                list.add(sv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public SinhVien getSinhVienByMSSV(String mssv) {
        if (mssv == null || mssv.trim().isEmpty()) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_SINH_VIEN, null,
                DatabaseHelper.COL_SV_MSSV + "=?", new String[]{mssv}, null, null, null);
        SinhVien sv = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                sv = mapCursorToSinhVien(cursor);
            }
            cursor.close();
        }
        return sv;
    }

    public List<SinhVien> getSinhVienByLop(String maLop) {
        List<SinhVien> list = new ArrayList<>();
        if (maLop == null || maLop.trim().isEmpty()) {
            return list;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_SINH_VIEN, null,
                DatabaseHelper.COL_SV_MALOP + "=?", new String[]{maLop}, null, null, DatabaseHelper.COL_SV_HOTEN + " ASC");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(mapCursorToSinhVien(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    public boolean themSinhVien(SinhVien sv) {
        if (sv == null || sv.getMssv() == null || sv.getMssv().trim().isEmpty()) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_SV_MSSV, sv.getMssv().trim());
        values.put(DatabaseHelper.COL_SV_HOTEN, sv.getHoTen() != null ? sv.getHoTen().trim() : "");
        values.put(DatabaseHelper.COL_SV_NGAYSINH, sv.getNgaySinh() != null ? sv.getNgaySinh().trim() : "");
        values.put(DatabaseHelper.COL_SV_EMAIL, sv.getEmail() != null ? sv.getEmail().trim() : "");
        values.put(DatabaseHelper.COL_SV_MALOP, sv.getMaLop() != null ? sv.getMaLop().trim() : "");
        values.put(DatabaseHelper.COL_SV_AVATAR, sv.getAnhAvatar() != null ? sv.getAnhAvatar().trim() : "");

        long result = db.insert(DatabaseHelper.TABLE_SINH_VIEN, null, values);
        if (result != -1) {
            if (sv.getMaLop() != null && !sv.getMaLop().trim().isEmpty()) {
                lopHocDao.capNhatSiSo(sv.getMaLop());
            }
            return true;
        }
        return false;
    }

    public boolean suaSinhVien(SinhVien sv) {
        if (sv == null || sv.getMssv() == null || sv.getMssv().trim().isEmpty()) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SinhVien svCu = getSinhVienByMSSV(sv.getMssv());
        String maLopCu = svCu != null ? svCu.getMaLop() : null;

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_SV_HOTEN, sv.getHoTen() != null ? sv.getHoTen().trim() : "");
        values.put(DatabaseHelper.COL_SV_NGAYSINH, sv.getNgaySinh() != null ? sv.getNgaySinh().trim() : "");
        values.put(DatabaseHelper.COL_SV_EMAIL, sv.getEmail() != null ? sv.getEmail().trim() : "");
        values.put(DatabaseHelper.COL_SV_MALOP, sv.getMaLop() != null ? sv.getMaLop().trim() : "");
        values.put(DatabaseHelper.COL_SV_AVATAR, sv.getAnhAvatar() != null ? sv.getAnhAvatar().trim() : "");

        int rows = db.update(DatabaseHelper.TABLE_SINH_VIEN, values,
                DatabaseHelper.COL_SV_MSSV + "=?", new String[]{sv.getMssv().trim()});

        if (rows > 0) {
            if (sv.getMaLop() != null && !sv.getMaLop().trim().isEmpty()) {
                lopHocDao.capNhatSiSo(sv.getMaLop());
            }
            if (maLopCu != null && !maLopCu.equalsIgnoreCase(sv.getMaLop())) {
                lopHocDao.capNhatSiSo(maLopCu);
            }
            return true;
        }
        return false;
    }

    public boolean xoaSinhVien(String mssv) {
        if (mssv == null || mssv.trim().isEmpty()) {
            return false;
        }
        SinhVien sv = getSinhVienByMSSV(mssv);
        String maLop = sv != null ? sv.getMaLop() : null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_SINH_VIEN,
                DatabaseHelper.COL_SV_MSSV + "=?", new String[]{mssv.trim()});

        if (rows > 0) {
            if (maLop != null && !maLop.trim().isEmpty()) {
                lopHocDao.capNhatSiSo(maLop);
            }
            return true;
        }
        return false;
    }

    public List<SinhVien> timKiemSinhVien(String keyword, String maLopFilter) {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder query = new StringBuilder("SELECT * FROM " + DatabaseHelper.TABLE_SINH_VIEN + " WHERE 1=1 ");
        List<String> args = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            query.append(" AND (" + DatabaseHelper.COL_SV_MSSV + " LIKE ? OR " +
                    DatabaseHelper.COL_SV_HOTEN + " LIKE ?) ");
            String pattern = "%" + keyword.trim() + "%";
            args.add(pattern);
            args.add(pattern);
        }

        if (maLopFilter != null && !maLopFilter.trim().isEmpty() && !maLopFilter.equalsIgnoreCase("Tất cả lớp")) {
            query.append(" AND " + DatabaseHelper.COL_SV_MALOP + " = ? ");
            args.add(maLopFilter.trim());
        }

        query.append(" ORDER BY " + DatabaseHelper.COL_SV_HOTEN + " ASC");

        Cursor cursor = db.rawQuery(query.toString(), args.toArray(new String[0]));
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(mapCursorToSinhVien(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    public int getTongSinhVien() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_SINH_VIEN, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    private SinhVien mapCursorToSinhVien(Cursor cursor) {
        SinhVien sv = new SinhVien();
        sv.setMssv(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SV_MSSV)));
        sv.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SV_HOTEN)));
        sv.setNgaySinh(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SV_NGAYSINH)));
        sv.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SV_EMAIL)));
        sv.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SV_MALOP)));
        sv.setAnhAvatar(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SV_AVATAR)));
        return sv;
    }
}
