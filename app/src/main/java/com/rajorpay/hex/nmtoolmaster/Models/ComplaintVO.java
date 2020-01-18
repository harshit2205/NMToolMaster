package com.rajorpay.hex.nmtoolmaster.Models;

public class ComplaintVO implements Comparable<ComplaintVO>{
    private String complaintId;
    private String boxNumber;
    private String stbType;
    private String name;
    private String assignedTo;
    private String status;
    private String completedBy;
    //complaint related
    private String errornumber;
    private String description;
    private String complaintType;
    private String inTime;
    private String outTime;

    private String userId;

    public ComplaintVO(){

    }

    public ComplaintVO(String userId, String complaintId, String boxNumber, String name, String status
            , String errornumber, String description, String complaintType, String assignedTo
            ,String inTime) {
        this.userId = userId;
        this.complaintId = complaintId;
        this.boxNumber = boxNumber;
        this.name = name;
        this.status = status;
        this.errornumber = errornumber;
        this.description = description;
        this.complaintType = complaintType;
        this.assignedTo = assignedTo;
        this.inTime = inTime;
    }

    public String getStbType() {
        return stbType;
    }

    public void setStbType(String stbType) {
        this.stbType = stbType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getErrornumber() {
        return errornumber;
    }

    public void setErrornumber(String errornumber) {
        this.errornumber = errornumber;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    @Override
    public int compareTo(ComplaintVO complaintVO) {
        return complaintVO.getInTime().compareTo(this.getInTime());
    }
}
