package com.myjdbc.test;


public class WExamTrain {

  private String id;
  private String orgCode;
  private java.sql.Date trainStartDate;
  private java.sql.Date trainEndDate;
  private String trainAddres;
  private String trainTeacher;
  private String trainPeopleNumber;
  private String trainLevel;
  private String trainStyle;
  private String trainContent;
  private String trainState;
  private String operatorId;
  private java.sql.Date operatorDatetime;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getOrgCode() {
    return orgCode;
  }

  public void setOrgCode(String orgCode) {
    this.orgCode = orgCode;
  }


  public java.sql.Date getTrainStartDate() {
    return trainStartDate;
  }

  public void setTrainStartDate(java.sql.Date trainStartDate) {
    this.trainStartDate = trainStartDate;
  }


  public java.sql.Date getTrainEndDate() {
    return trainEndDate;
  }

  public void setTrainEndDate(java.sql.Date trainEndDate) {
    this.trainEndDate = trainEndDate;
  }


  public String getTrainAddres() {
    return trainAddres;
  }

  public void setTrainAddres(String trainAddres) {
    this.trainAddres = trainAddres;
  }


  public String getTrainTeacher() {
    return trainTeacher;
  }

  public void setTrainTeacher(String trainTeacher) {
    this.trainTeacher = trainTeacher;
  }


  public String getTrainPeopleNumber() {
    return trainPeopleNumber;
  }

  public void setTrainPeopleNumber(String trainPeopleNumber) {
    this.trainPeopleNumber = trainPeopleNumber;
  }


  public String getTrainLevel() {
    return trainLevel;
  }

  public void setTrainLevel(String trainLevel) {
    this.trainLevel = trainLevel;
  }


  public String getTrainStyle() {
    return trainStyle;
  }

  public void setTrainStyle(String trainStyle) {
    this.trainStyle = trainStyle;
  }


  public String getTrainContent() {
    return trainContent;
  }

  public void setTrainContent(String trainContent) {
    this.trainContent = trainContent;
  }


  public String getTrainState() {
    return trainState;
  }

  public void setTrainState(String trainState) {
    this.trainState = trainState;
  }


  public String getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }


  public java.sql.Date getOperatorDatetime() {
    return operatorDatetime;
  }

  public void setOperatorDatetime(java.sql.Date operatorDatetime) {
    this.operatorDatetime = operatorDatetime;
  }


  @Override
  public String toString() {
    return "WExamTrain{" +
            "id='" + id + '\'' +
            ", orgCode='" + orgCode + '\'' +
            ", trainStartDate=" + trainStartDate +
            ", trainEndDate=" + trainEndDate +
            ", trainAddres='" + trainAddres + '\'' +
            ", trainTeacher='" + trainTeacher + '\'' +
            ", trainPeopleNumber='" + trainPeopleNumber + '\'' +
            ", trainLevel='" + trainLevel + '\'' +
            ", trainStyle='" + trainStyle + '\'' +
            ", trainContent='" + trainContent + '\'' +
            ", trainState='" + trainState + '\'' +
            ", operatorId='" + operatorId + '\'' +
            ", operatorDatetime=" + operatorDatetime +
            '}';
  }
}
