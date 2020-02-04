package com.rajorpay.hex.nmtoolmaster.Models;

public class EmployeeVO {

    private String name;
    private String phoneNr;
    private String password;
    private String designation;
    private int salery;

    public EmployeeVO(String name, String phoneNr, String password, String designation, int salery) {
        this.name = name;
        this.phoneNr = phoneNr;
        this.password = password;
        this.designation = designation;
        this.salery = salery;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getSalery() {
        return salery;
    }

    public void setSalery(int salery) {
        this.salery = salery;
    }
}
