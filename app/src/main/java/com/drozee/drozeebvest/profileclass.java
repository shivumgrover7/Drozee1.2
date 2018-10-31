package com.drozee.drozeebvest;

public class profileclass {
    String name,college,year, phone;

    public profileclass(String name, String college, String year, String phone) {
        this.name = name;
        this.college = college;
        this.year = year;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public profileclass(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
