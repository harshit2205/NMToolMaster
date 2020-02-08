package com.rajorpay.hex.nmtoolmaster.Models;

public class Customer {
    private String custId;
    private String name;
    private String boxNumber;
    private String boxType;
    private String phoneNumber;
    private String packageAmount;
    private String locality;
    private String address;
    private String paymentStatus;
    private String paidTill;
    private boolean isHeader = false;
    private boolean isArchived = false;

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public Customer(){
    }

    public Customer(String custId, String name, String boxNumber, String boxType, String phoneNumber,
                    String packageAmount, String locality, String address, String paymentStatus) {
        this.custId = custId;
        this.name = name;
        this.boxNumber = boxNumber;
        this.boxType = boxType;
        this.phoneNumber = phoneNumber;
        this.packageAmount = packageAmount;
        this.locality = locality;
        this.address = address;
        this.paymentStatus = paymentStatus;
    }

    public String getPaidTill() {
        return paidTill;
    }

    public void setPaidTill(String paidTill) {
        this.paidTill = paidTill;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(String packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
