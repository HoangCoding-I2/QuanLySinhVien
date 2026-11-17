package com.hou.quanlysinhvien.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hou.quanlysinhvien.model.Diem;
import com.hou.quanlysinhvien.model.ThongKeResult;

import java.util.ArrayList;
import java.util.List;

public class DiemDao {
    private final DatabaseHelper dbHelper;

    public DiemDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Diem> getBangDiemByLopVaMon(String maLop, String maMon) {
        List<Diem> list = new ArrayList<>();
        if (maLop == null || maLop.trim().isEmpty() || maMon == null || maMon.trim().isEmpty()) {
            return list;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT sv." + DatabaseHelper.COL_SV_MSSV + ", sv." + DatabaseHelper.COL_SV_HOTEN + ", " +
                "sv." + DatabaseHelper.COL_SV_MALOP + ", " +
                "d." + DatabaseHelper.COL_DIEM_CC + ", d." + DatabaseHelper.COL_DIEM_GK + ", " +
                "d." + DatabaseHelper.COL_DIEM_CK + ", d." + DatabaseHelper.COL_DIEM_TB + " " +
                "FROM " + DatabaseHelper.TABLE_SINH_VIEN + " sv " +
                "LEFT JOIN " + DatabaseHelper.TABLE_DIEM + " d " +
                "ON sv." + DatabaseHelper.COL_SV_MSSV + " = d." + DatabaseHelper.COL_DIEM_MSSV + " AND d." + DatabaseHelper.COL_DIEM_MAMON + " = ? " +
                "WHERE sv." + DatabaseHelper.COL_SV_MALOP + " = ? " +
                "ORDER BY sv." + DatabaseHelper.COL_SV_HOTEN + " ASC";

        Cursor cursor = db.rawQuery(query, new String[]{maMon, maLop});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Diem item = new Diem();
                    item.setMssv(cursor.getString(0));
                    item.setHoTenSinhVien(cursor.getString(1));
                    item.setMaLop(cursor.getString(2));
                    item.setMaMon(maMon);

                    if (!cursor.isNull(3)) {
                        item.setDiemCC(cursor.getFloat(3));
                        item.setDiemGK(cursor.getFloat(4));
                        item.setDiemCK(cursor.getFloat(5));
                        item.setDiemTB(cursor.getFloat(6));
                    } else {
                        item.setDiemCC(-1);
                        item.setDiemGK(-1);
                        item.setDiemCK(-1);
                        item.setDiemTB(-1);
                    }
                    list.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    public boolean luuDiem(Diem diem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_DIEM_MSSV, diem.getMssv());
        values.put(DatabaseHelper.COL_DIEM_MAMON, diem.getMaMon());
        values.put(DatabaseHelper.COL_DIEM_CC, diem.getDiemCC());
        values.put(DatabaseHelper.COL_DIEM_GK, diem.getDiemGK());
        values.put(DatabaseHelper.COL_DIEM_CK, diem.getDiemCK());

        float dtb = Diem.tinhTrungBinh(diem.getDiemCC(), diem.getDiemGK(), diem.getDiemCK());
        values.put(DatabaseHelper.COL_DIEM_TB, dtb);

        long result = db.insertWithOnConflict(DatabaseHelper.TABLE_DIEM, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public boolean luuDanhSachDiem(List<Diem> listDiem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Diem d : listDiem) {
                if (d.getDiemCC() >= 0 && d.getDiemGK() >= 0 && d.getDiemCK() >= 0) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COL_DIEM_MSSV, d.getMssv());
                    values.put(DatabaseHelper.COL_DIEM_MAMON, d.getMaMon());
                    values.put(DatabaseHelper.COL_DIEM_CC, d.getDiemCC());
                    values.put(DatabaseHelper.COL_DIEM_GK, d.getDiemGK());
                    values.put(DatabaseHelper.COL_DIEM_CK, d.getDiemCK());
                    float dtb = Diem.tinhTrungBinh(d.getDiemCC(), d.getDiemGK(), d.getDiemCK());
                    values.put(DatabaseHelper.COL_DIEM_TB, dtb);
                    db.insertWithOnConflict(DatabaseHelper.TABLE_DIEM, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                }
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public ThongKeResult getThongKe(String maLop, String maMon) {
        ThongKeResult result = new ThongKeResult();
        List<Diem> list = getBangDiemByLopVaMon(maLop, maMon);

        int countValid = 0;
        int countDat = 0;
        float tongTB = 0;
        float maxTB = -1;
        float minTB = 11;
        int gioi = 0, kha = 0, tb = 0, yeu = 0;

        for (Diem d : list) {
            if (d.getDiemTB() >= 0) {
                countValid++;
                float diemTB = d.getDiemTB();
                tongTB += diemTB;

                if (diemTB > maxTB) maxTB = diemTB;
                if (diemTB < minTB) minTB = diemTB;

                if (diemTB >= 4.0f) {
                    countDat++;
                }

                if (diemTB >= 8.0f) {
                    gioi++;
                } else if (diemTB >= 6.5f) {
                    kha++;
                } else if (diemTB >= 5.0f) {
                    tb++;
                } else {
                    yeu++;
                }
            }
        }

        result.setTongSoSV(countValid);
        if (countValid > 0) {
            result.setDiemTBLop((float) (Math.round((tongTB / countValid) * 10.0) / 10.0));
            result.setTiLeDat((float) (Math.round(((float) countDat / countValid * 100.0f) * 10.0) / 10.0));
            result.setCaoNhat(maxTB >= 0 ? maxTB : 0);
            result.setThapNhat(minTB <= 10 ? minTB : 0);
        } else {
            result.setDiemTBLop(0);
            result.setTiLeDat(0);
            result.setCaoNhat(0);
            result.setThapNhat(0);
        }

        result.setSoLuongGioi(gioi);
        result.setSoLuongKha(kha);
        result.setSoLuongTrungBinh(tb);
        result.setSoLuongYeu(yeu);

        return result;
    }
}
