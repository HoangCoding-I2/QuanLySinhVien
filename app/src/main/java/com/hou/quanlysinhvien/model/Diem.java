package com.hou.quanlysinhvien.model;

public class Diem {
    private String mssv;
    private String maMon;
    private float diemCC;
    private float diemGK;
    private float diemCK;
    private float diemTB;

    private String hoTenSinhVien;
    private String tenMonHoc;
    private String maLop;

    public Diem() {
    }

    public Diem(String mssv, String maMon, float diemCC, float diemGK, float diemCK, float diemTB) {
        this.mssv = mssv;
        this.maMon = maMon;
        this.diemCC = diemCC;
        this.diemGK = diemGK;
        this.diemCK = diemCK;
        this.diemTB = diemTB;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public float getDiemCC() {
        return diemCC;
    }

    public void setDiemCC(float diemCC) {
        this.diemCC = diemCC;
    }

    public float getDiemGK() {
        return diemGK;
    }

    public void setDiemGK(float diemGK) {
        this.diemGK = diemGK;
    }

    public float getDiemCK() {
        return diemCK;
    }

    public void setDiemCK(float diemCK) {
        this.diemCK = diemCK;
    }

    public float getDiemTB() {
        return diemTB;
    }

    public void setDiemTB(float diemTB) {
        this.diemTB = diemTB;
    }

    public String getHoTenSinhVien() {
        return hoTenSinhVien;
    }

    public void setHoTenSinhVien(String hoTenSinhVien) {
        this.hoTenSinhVien = hoTenSinhVien;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public static float tinhTrungBinh(float cc, float gk, float ck) {
        float tb = cc * 0.1f + gk * 0.4f + ck * 0.5f;
        return (float) (Math.round(tb * 10.0) / 10.0);
    }

    public String getXepLoai() {
        if (diemTB >= 8.0f) {
            return "Giỏi";
        } else if (diemTB >= 6.5f) {
            return "Khá";
        } else if (diemTB >= 5.0f) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }

    public boolean isDat() {
        return diemTB >= 4.0f && diemCK >= 3.0f;
    }
}
