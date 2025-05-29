package com.hrsoft.db.datadriven.model;

import java.util.List;
import java.util.Objects;

public class Employee {

    private List<Employee> employees;
    private boolean isManager;
    private Employee mgrEmployee;

    private int empId;
    private int mgrEmpId;
    private String mgrGuid;
    private String mgrDisplayName;
    private String mgrNumber;
    private String mgrUserName;
    private String firstName;
    private String lastName;
    private String middleName;
    private String isDefault;
    private String displayName;
    private String emailId;
    private String jobCode;
    private String newJobCode;
    private int partTimeFactor;
    private int currSalaryActual;
    private int periodFactor;
    private int payRateId;
    private int payRateSchemeId;
    private String geoPayId;
    private String currencyCode;
    private int wuid;
    private String employeeNumber;
    private String empUserName;
    private String chgBy;
    private String jobTitle;
    private int jobGrade;
    private String jobGradeName;
    private int jobGradeRank;
    private String jobFamily;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return empId == employee.empId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public Employee getMgrEmployee() {
        return mgrEmployee;
    }

    public void setMgrEmployee(Employee mgrEmployee) {
        this.mgrEmployee = mgrEmployee;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getMgrEmpId() {
        return mgrEmpId;
    }

    public void setMgrEmpId(int mgrEmpId) {
        this.mgrEmpId = mgrEmpId;
    }

    public String getMgrGuid() {
        return mgrGuid;
    }

    public void setMgrGuid(String mgrGuid) {
        this.mgrGuid = mgrGuid;
    }

    public String getMgrDisplayName() {
        return mgrDisplayName;
    }

    public void setMgrDisplayName(String mgrDisplayName) {
        this.mgrDisplayName = mgrDisplayName;
    }

    public String getMgrNumber() {
        return mgrNumber;
    }

    public void setMgrNumber(String mgrNumber) {
        this.mgrNumber = mgrNumber;
    }

    public String getMgrUserName() {
        return mgrUserName;
    }

    public void setMgrUserName(String mgrUserName) {
        this.mgrUserName = mgrUserName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getNewJobCode() {
        return newJobCode;
    }

    public void setNewJobCode(String newJobCode) {
        this.newJobCode = newJobCode;
    }

    public int getPartTimeFactor() {
        return partTimeFactor;
    }

    public void setPartTimeFactor(int partTimeFactor) {
        this.partTimeFactor = partTimeFactor;
    }

    public int getCurrSalaryActual() {
        return currSalaryActual;
    }

    public void setCurrSalaryActual(int currSalaryActual) {
        this.currSalaryActual = currSalaryActual;
    }

    public int getPeriodFactor() {
        return periodFactor;
    }

    public void setPeriodFactor(int periodFactor) {
        this.periodFactor = periodFactor;
    }

    public int getPayRateId() {
        return payRateId;
    }

    public void setPayRateId(int payRateId) {
        this.payRateId = payRateId;
    }

    public int getPayRateSchemeId() {
        return payRateSchemeId;
    }

    public void setPayRateSchemeId(int payRateSchemeId) {
        this.payRateSchemeId = payRateSchemeId;
    }

    public String getGeoPayId() {
        return geoPayId;
    }

    public void setGeoPayId(String geoPayId) {
        this.geoPayId = geoPayId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getWuid() {
        return wuid;
    }

    public void setWuid(int wuid) {
        this.wuid = wuid;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmpUserName() {
        return empUserName;
    }

    public void setEmpUserName(String empUserName) {
        this.empUserName = empUserName;
    }

    public String getChgBy() {
        return chgBy;
    }

    public void setChgBy(String chgBy) {
        this.chgBy = chgBy;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getJobGrade() {
        return jobGrade;
    }

    public void setJobGrade(int jobGrade) {
        this.jobGrade = jobGrade;
    }

    public String getJobGradeName() {
        return jobGradeName;
    }

    public void setJobGradeName(String jobGradeName) {
        this.jobGradeName = jobGradeName;
    }

    public int getJobGradeRank() {
        return jobGradeRank;
    }

    public void setJobGradeRank(int jobGradeRank) {
        this.jobGradeRank = jobGradeRank;
    }

    public String getJobFamily() {
        return jobFamily;
    }

    public void setJobFamily(String jobFamily) {
        this.jobFamily = jobFamily;
    }
}
