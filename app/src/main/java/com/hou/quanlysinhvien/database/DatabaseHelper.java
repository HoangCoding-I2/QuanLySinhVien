package com.hou.quanlysinhvien.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "QuanLySinhVien.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_LOP_HOC = "LOP_HOC";
    public static final String COL_LOP_MA = "MaLop";
    public static final String COL_LOP_TEN = "TenLop";
    public static final String COL_LOP_SISO = "SiSo";

    public static final String TABLE_SINH_VIEN = "SINH_VIEN";
    public static final String COL_SV_MSSV = "MSSV";
    public static final String COL_SV_HOTEN = "HoTen";
    public static final String COL_SV_NGAYSINH = "NgaySinh";
    public static final String COL_SV_EMAIL = "Email";
    public static final String COL_SV_MALOP = "MaLop";
    public static final String COL_SV_AVATAR = "AnhAvatar";

    public static final String TABLE_MON_HOC = "MON_HOC";
    public static final String COL_MH_MA = "MaMon";
    public static final String COL_MH_TEN = "TenMon";

    public static final String TABLE_DIEM = "DIEM";
    public static final String COL_DIEM_MSSV = "MSSV";
    public static final String COL_DIEM_MAMON = "MaMon";
    public static final String COL_DIEM_CC = "DiemCC";
    public static final String COL_DIEM_GK = "DiemGK";
    public static final String COL_DIEM_CK = "DiemCK";
    public static final String COL_DIEM_TB = "DiemTB";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLopHoc = "CREATE TABLE " + TABLE_LOP_HOC + " (" +
                COL_LOP_MA + " TEXT PRIMARY KEY, " +
                COL_LOP_TEN + " TEXT NOT NULL, " +
                COL_LOP_SISO + " INTEGER DEFAULT 0);";
        db.execSQL(createLopHoc);

        String createSinhVien = "CREATE TABLE " + TABLE_SINH_VIEN + " (" +
                COL_SV_MSSV + " TEXT PRIMARY KEY, " +
                COL_SV_HOTEN + " TEXT NOT NULL, " +
                COL_SV_NGAYSINH + " TEXT, " +
                COL_SV_EMAIL + " TEXT, " +
                COL_SV_MALOP + " TEXT, " +
                COL_SV_AVATAR + " TEXT, " +
                "FOREIGN KEY(" + COL_SV_MALOP + ") REFERENCES " + TABLE_LOP_HOC + "(" + COL_LOP_MA + ") ON DELETE SET NULL);";
        db.execSQL(createSinhVien);

        String createMonHoc = "CREATE TABLE " + TABLE_MON_HOC + " (" +
                COL_MH_MA + " TEXT PRIMARY KEY, " +
                COL_MH_TEN + " TEXT NOT NULL);";
        db.execSQL(createMonHoc);

        String createDiem = "CREATE TABLE " + TABLE_DIEM + " (" +
                COL_DIEM_MSSV + " TEXT, " +
                COL_DIEM_MAMON + " TEXT, " +
                COL_DIEM_CC + " REAL, " +
                COL_DIEM_GK + " REAL, " +
                COL_DIEM_CK + " REAL, " +
                COL_DIEM_TB + " REAL, " +
                "PRIMARY KEY(" + COL_DIEM_MSSV + ", " + COL_DIEM_MAMON + "), " +
                "FOREIGN KEY(" + COL_DIEM_MSSV + ") REFERENCES " + TABLE_SINH_VIEN + "(" + COL_SV_MSSV + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_DIEM_MAMON + ") REFERENCES " + TABLE_MON_HOC + "(" + COL_MH_MA + ") ON DELETE CASCADE);";
        db.execSQL(createDiem);

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_LOP_HOC + " VALUES ('22CT1', 'Công nghệ thông tin 1', 3);");
        db.execSQL("INSERT INTO " + TABLE_LOP_HOC + " VALUES ('24A1', 'Kỹ thuật phần mềm 1', 2);");
        db.execSQL("INSERT INTO " + TABLE_LOP_HOC + " VALUES ('24A2', 'Hệ thống thông tin 2', 1);");

        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " VALUES ('LTDD', 'Lập trình thiết bị di động');");
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " VALUES ('CSDL', 'Cơ sở dữ liệu');");
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " VALUES ('JAVA', 'Lập trình Java');");
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " VALUES ('CTDL', 'Cấu trúc dữ liệu & Giải thuật');");

        db.execSQL("INSERT INTO " + TABLE_SINH_VIEN + " VALUES ('24A1001D0159', 'Lại Trần Hiếu', '15/03/2004', 'hieu.lt@hou.edu.vn', '22CT1', '');");
        db.execSQL("INSERT INTO " + TABLE_SINH_VIEN + " VALUES ('24A1001D0257', 'Nguyễn Thị Diệu Linh', '20/08/2004', 'linh.ntd@hou.edu.vn', '22CT1', '');");
        db.execSQL("INSERT INTO " + TABLE_SINH_VIEN + " VALUES ('22A1001D1043', 'Trần Đức Hoàng', '10/11/2003', 'hoang.td@hou.edu.vn', '22CT1', '');");
        db.execSQL("INSERT INTO " + TABLE_SINH_VIEN + " VALUES ('SV001', 'Nguyễn Văn A', '05/01/2004', 'vana@hou.edu.vn', '24A1', '');");
        db.execSQL("INSERT INTO " + TABLE_SINH_VIEN + " VALUES ('SV002', 'Trần Thị B', '12/04/2004', 'thib@hou.edu.vn', '24A1', '');");
        db.execSQL("INSERT INTO " + TABLE_SINH_VIEN + " VALUES ('SV003', 'Lê Văn C', '22/09/2004', 'vanc@hou.edu.vn', '24A2', '');");

        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('24A1001D0159', 'LTDD', 9.0, 8.5, 9.0, 8.8);");
        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('24A1001D0257', 'LTDD', 10.0, 9.0, 9.5, 9.4);");
        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('22A1001D1043', 'LTDD', 8.5, 8.0, 8.5, 8.3);");
        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('SV001', 'LTDD', 8.0, 7.5, 7.0, 7.3);");
        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('SV002', 'LTDD', 7.0, 6.0, 5.5, 5.9);");
        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('SV003', 'LTDD', 6.0, 4.5, 3.5, 4.2);");

        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('24A1001D0159', 'CSDL', 8.5, 8.0, 8.5, 8.3);");
        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('24A1001D0257', 'CSDL', 9.5, 9.0, 9.0, 9.1);");
        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('22A1001D1043', 'CSDL', 8.0, 7.5, 8.0, 7.8);");
        db.execSQL("INSERT INTO " + TABLE_DIEM + " VALUES ('SV001', 'CSDL', 7.5, 7.0, 6.5, 6.8);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SINH_VIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MON_HOC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOP_HOC);
        onCreate(db);
    }
}
