package com.rajorpay.hex.nmtoolmaster.Models;

public class EmployeeVO {

    private String name;
    private String signUpCode;
    private String phoneNr;
    private String designation;
    private int salery;
    private boolean isArchived = false;
    private String empId;

    public EmployeeVO() {
    }

    public EmployeeVO(String name, String signUpCode, String phoneNr, String designation, int salery, String empId) {
        this.name = name;
        this.signUpCode = signUpCode;
        this.phoneNr = phoneNr;
        this.designation = designation;
        this.salery = salery;
        this.empId = empId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignUpCode() {
        return signUpCode;
    }

    public void setSignUpCode(String signUpCode) {
        this.signUpCode = signUpCode;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
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
