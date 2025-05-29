package com.hrsoft.db.datadriven.model;

import java.util.Objects;

public class Budget {

    private int id;
    private int planId;
    private int sequence;
    private String name;
    private String type;
    private String basis;
    private double availableBudget;
    private String availableBudgetIsPCT;
    private String protectedCurrency;
    private String eligibility;
    private double defaultPCTForEmp;
    private String isVisible;
    private String baseComponentName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget = (Budget) o;
        return id == budget.id && name.equals(budget.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public double getAvailableBudget() {
        return availableBudget;
    }

    public void setAvailableBudget(double availableBudget) {
        this.availableBudget = availableBudget;
    }

    public String getAvailableBudgetIsPCT() {
        return availableBudgetIsPCT;
    }

    public void setAvailableBudgetIsPCT(String availableBudgetIsPCT) {
        this.availableBudgetIsPCT = availableBudgetIsPCT;
    }

    public String getProtectedCurrency() {
        return protectedCurrency;
    }

    public void setProtectedCurrency(String protectedCurrency) {
        this.protectedCurrency = protectedCurrency;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public double getDefaultPCTForEmp() {
        return defaultPCTForEmp;
    }

    public void setDefaultPCTForEmp(double defaultPCTForEmp) {
        this.defaultPCTForEmp = defaultPCTForEmp;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getBaseComponentName() {
        return baseComponentName;
    }

    public void setBaseComponentName(String baseComponentName) {
        this.baseComponentName = baseComponentName;
    }
}
