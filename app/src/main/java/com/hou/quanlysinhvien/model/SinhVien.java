package com.hou.quanlysinhvien.model;

public class SinhVien {
    private String mssv;
    private String hoTen;
    private String ngaySinh;
    private String email;
    private String maLop;
    private String anhAvatar;

    public SinhVien() {
    }

    public SinhVien(String mssv, String hoTen, String ngaySinh, String email, String maLop, String anhAvatar) {
        this.mssv = mssv;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.email = email;
        this.maLop = maLop;
        this.anhAvatar = anhAvatar;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getAnhAvatar() {
        return anhAvatar;
    }

    public void setAnhAvatar(String anhAvatar) {
        this.anhAvatar = anhAvatar;
    }

    @Override
    public String toString() {
        return hoTen + " (" + mssv + ")";
    }
}
