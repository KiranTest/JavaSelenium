package com.hrsoft.db.datadriven.model;

import java.util.Objects;

public class PerfRating {

    private int id;
    private int planId;
    private String name;
    private int order;
    private Double tgtDistribution;
    private String includeInDistribution;
    private String isDefault;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerfRating that = (PerfRating) o;
        return id == that.id && name.equals(that.name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Double getTgtDistribution() {
        return tgtDistribution;
    }

    public void setTgtDistribution(Double tgtDistribution) {
        this.tgtDistribution = tgtDistribution;
    }

    public String getIncludeInDistribution() {
        return includeInDistribution;
    }

    public void setIncludeInDistribution(String includeInDistribution) {
        this.includeInDistribution = includeInDistribution;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
