package com.hrsoft.db.datadriven;

/**
 * @author Kalyan komati
 */

public class User {
    private String   userName;
    private String   custGuid;
    private String   custId;
    private String   appId;
    private String   hierarchyGuid;
    private String   actingParticipantGuid;
    private String   targetParticipantGuid;
    private String[] sensitivityList;

//    public enum user{
//        userName,
//        custGuid,
//        custId
//    }
    public User () {

    }

    public String getUserName () {
        return userName;
    }

    public void setUserNmae (String userNmae) {
        this.userName = userNmae;
    }

    public String getCustGuid () {
        return custGuid;
    }

    public void setCustGuid (String custGuid) {
        this.custGuid = custGuid;
    }

    public String getCustId () {
        return custId;
    }

    public void setCustId (String custId) {
        this.custId = custId;
    }

    public String getAppId () {
        return appId;
    }

    public void setAppId (String appId) {
        this.appId = appId;
    }

    public String getHierarchyGuid () {
        return hierarchyGuid;
    }

    public void setHierarchyGuid (String hierarchyGuid) {
        this.hierarchyGuid = hierarchyGuid;
    }

    public String getActingParticipantGuid () {
        return actingParticipantGuid;
    }

    public void setActingParticipantGuid (String actingParticipantGuid) {
        this.actingParticipantGuid = actingParticipantGuid;
    }

    public String gettargetParticipantGuid () {
        return targetParticipantGuid;
    }
    
    public void setTargetParticipantGuid (String targetParticipantGuid) {
        this.targetParticipantGuid = targetParticipantGuid;
    }

    public String[] getSensitivityList () {
        return sensitivityList;
    }

    public void setSensitivityList (String sensitivityCSV) {
        this.sensitivityList = sensitivityCSV.split (",");
    }

    public boolean isSensitivityValid (String[] other) {

        if (other.length != sensitivityList.length)
            return false;

        for (int i = 0; i < sensitivityList.length; i++) {
            if (!sensitivityList[i].equalsIgnoreCase (other[i])) {
                return false;
            }
        }
        return true;

    }
}
