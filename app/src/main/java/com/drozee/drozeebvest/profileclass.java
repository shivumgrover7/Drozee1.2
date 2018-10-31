package com.drozee.drozeebvest;

public class profileclass {
    String name,college,year;

    public profileclass(String name, String college, String year) {
        this.name = name;
        this.college = college;
        this.year = year;
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
