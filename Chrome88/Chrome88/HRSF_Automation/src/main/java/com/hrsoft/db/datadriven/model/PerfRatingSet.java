package com.hrsoft.db.datadriven.model;

import java.util.Objects;

public class PerfRatingSet {

    private int id;
    private int planId;
    private String name;
    private int priority;
    private String userEditable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerfRatingSet that = (PerfRatingSet) o;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getUserEditable() {
        return userEditable;
    }

    public void setUserEditable(String userEditable) {
        this.userEditable = userEditable;
    }
}
