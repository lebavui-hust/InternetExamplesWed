package com.example.internetexamples;

public class StudentModel {
    private String mssv;
    private String hoten;
    private String address;

    public StudentModel(String mssv, String hoten, String address) {
        this.mssv = mssv;
        this.hoten = hoten;
        this.address = address;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
