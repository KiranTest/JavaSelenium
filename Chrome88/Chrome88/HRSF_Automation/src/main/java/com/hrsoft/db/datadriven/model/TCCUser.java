package com.hrsoft.db.datadriven.model;

public class TCCUser {

	private int planId;
	private String planName;
	private String userName;
	private String userGuid;
	private String displayName;
	private String hierarchyGuid;
	private String managerGuid;
	private String custGuid;
	private String firstName;
	private String lastname;

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}
	
	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCustGuid() {
		return custGuid;
	}

	public void setCustGuid(String custGuid) {
		this.custGuid = custGuid;
	}

	public String getHierarchyGuid() {
		return hierarchyGuid;
	}

	public void setHierarchyGuid(String hierarchyGuid) {
		this.hierarchyGuid = hierarchyGuid;
	}

	public String getManagerGuid() {
		return managerGuid;
	}

	public void setManagerGuid(String managerGuid) {
		this.managerGuid = managerGuid;
	}

	public TCCUser() {
	}

	public TCCUser(String userName, String userGuid, String displayName) {
		this.userName = userName;
		this.userGuid = userGuid;
		this.displayName = displayName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
