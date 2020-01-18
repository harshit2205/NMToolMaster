package com.rajorpay.hex.nmtoolmaster.Models;

public class PaymentVO implements Comparable<PaymentVO>{
    private String paymentId;
    private String paidOn;
    private String paidFor;
    private int amount;
    private int dues;

    public PaymentVO() {
    }

    public PaymentVO(String paidOn, String paidFor, int amount, int dues) {
        this.paidOn = paidOn;
        this.paidFor = paidFor;
        this.amount = amount;
        this.dues = dues;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public int getDues() {
        return dues;
    }

    public void setDues(int dues) {
        this.dues = dues;
    }

    public String getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(String paidOn) {
        this.paidOn = paidOn;
    }

    public String getPaidFor() {
        return paidFor;
    }

    public void setPaidFor(String paidFor) {
        this.paidFor = paidFor;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(PaymentVO paymentVO) {
        return paymentVO.paidOn.compareTo(paidOn);
    }
}
