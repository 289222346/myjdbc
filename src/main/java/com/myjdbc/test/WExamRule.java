package com.myjdbc.test;

public class WExamRule {

    private String id;
    private String ruleName;
    private String userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "WExamRule{" +
                "id='" + id + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
