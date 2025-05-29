 package com.hrsoft.db.datadriven.model;

import java.util.Objects;
import static com.hrsoft.config.ConfigFactory.*;

public class GroupPlanner {
    private String  custId;
    private String  custGuid;
    private String  plannerGuid;
    private String  plannerNumber;
    private String  plannerUserName;
    private String  mgrGuid;
    private String  mgrNumber;
    private String  managerUserName;
    public String getManagerUserName () {
        return managerUserName;
    }

    public void setManagerUserName (String managerUserName) {
        this.managerUserName = managerUserName;
    }

    private String  managerFullName;
    public String getManagerFullName () {
        return managerFullName;
    }

    public void setManagerFullName (String managerFullName) {
        this.managerFullName = managerFullName;
    }

    private String  groupId;
    private String  coPlanner;
    private String  coPlannerUserName;
    
    public String getCoPlannerUserName () {
        return coPlannerUserName;
    }

    public void setCoPlannerUserName (String coPlannerUserName) {
        this.coPlannerUserName = coPlannerUserName;
    }

    public String getCoPlanner () {
        return coPlanner;
    }

    public void setCoPlanner (String coPlanner) {
        this.coPlanner = coPlanner;
    }

    private boolean isCoPlanner;
    private String  relId;
    private String  hierName;
    private Integer mandatoryFilterId;
    private String  limitToEmpIds;
    private String  plannerFirstName;
    private String  plannerLastName;
	private int compPlanId;

    @Override
	public String toString() {
		return "GroupPlanner [custId=" + custId + ", custGuid=" + custGuid + ", plannerGuid=" + plannerGuid
				+ ", plannerNumber=" + plannerNumber + ", plannerUserName=" + plannerUserName + ", mgrGuid=" + mgrGuid
				+ ", mgrNumber=" + mgrNumber + ", groupId=" + groupId + ", isCoPlanner=" + isCoPlanner + ", relId="
				+ relId + ", hierName=" + hierName + ", mandatoryFilterId=" + mandatoryFilterId + ", limitToEmpIds="
				+ limitToEmpIds + ", plannerFirstName=" + plannerFirstName + ", plannerLastName=" + plannerLastName
				+ ", compPlanId=" + compPlanId + "]";
	}

	@Override
    public boolean equals (Object o) {
        if (this == o)
            return true;
        if (o == null || getClass () != o.getClass ())
            return false;
        GroupPlanner that = (GroupPlanner) o;
        return plannerGuid.equals (that.plannerGuid) && mgrGuid.equals (that.mgrGuid);
    }

    @Override
    public int hashCode () {
        return Objects.hash (plannerGuid, mgrGuid);
    }

    public String getCustId () {
        return custId;
    }

    public void setCustId (String custId) {
        this.custId = custId;
    }

    public String getCustGuid () {
        return custGuid;
    }

    public void setCustGuid (String custGuid) {
        this.custGuid = custGuid;
    }

    public String getPlannerGuid () {
        return plannerGuid;
    }

    public void setPlannerGuid (String plannerGuid) {
        this.plannerGuid = plannerGuid;
    }

    public String getPlannerNumber () {
        return plannerNumber;
    }

    public void setPlannerNumber (String plannerNumber) {
        this.plannerNumber = plannerNumber;
    }

    public String getPlannerUserName () {
        return plannerUserName;
    }

    public String getPlannerFirstName () {
        return plannerFirstName;
    }

    public String getPlannerLastName () {
        return plannerLastName;
    }

    public void setPlannerUserName (String plannerUserName) {
        this.plannerUserName = plannerUserName;
    }

    public String getMgrGuid () {
        return mgrGuid;
    }

    public void setMgrGuid (String mgrGuid) {
        this.mgrGuid = mgrGuid;
    }

    public String getMgrNumber () {
        return mgrNumber;
    }

    public void setMgrNumber (String mgrNumber) {
        this.mgrNumber = mgrNumber;
    }

    public String getGroupId () {
        return groupId;
    }
    public String setPlannerFirstName (String firstName) {
        return plannerFirstName=firstName;
    }

    public String setPlannerLastName (String lastName) {
        return plannerLastName=lastName;
    }
    public void setGroupId (String groupId) {
        this.groupId = groupId;
    }

    public boolean isCoPlanner () {
        return isCoPlanner;
    }

    public void setCoPlanner (boolean coPlanner) {
        isCoPlanner = coPlanner;
    }

    public int getCompPlanId () {
        return compPlanId;
    }

    public void setCompPlanId (int compPlanId) {
        this.compPlanId = compPlanId;
    }

    public String getRelId () {
        return relId;
    }

    public void setRelId (String relId) {
        this.relId = relId;
    }

    public String getHierName () {
        return hierName;
    }

    public void setHierName (String hierName) {
        this.hierName = hierName;
    }

    public Integer getMandatoryFilterId () {
        return mandatoryFilterId;
    }

    public void setMandatoryFilterId (Integer mandatoryFilterId) {
        this.mandatoryFilterId = mandatoryFilterId;
    }

    public String getLimitToEmpIds () {
        return limitToEmpIds;
    }

    public void setLimitToEmpIds (String limitToEmpIds) {
        this.limitToEmpIds = limitToEmpIds;
    }
}
