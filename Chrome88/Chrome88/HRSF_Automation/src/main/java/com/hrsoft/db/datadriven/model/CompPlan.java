package com.hrsoft.db.datadriven.model;

import java.util.List;
import java.util.Objects;

public class CompPlan {

    private int    id;
    private String name;
    private String relId;
    private String hierName;
    private int    planStageId;
    private String baseCurrencyCode;
    private String custguid;
    private String hierarchyGuid;
    private String topPlannerFullName;
    private String topPlannerUserName;
    private String empSpareName;
    private String empSpareId;
    private String managerToSearchGroup;
    
    public String getTopPlannerFullName () {
        return topPlannerFullName;
    }

    public void setTopPlannerFullName (String topPlannerFullName) {
        this.topPlannerFullName = topPlannerFullName;
    }

    public String getTopPlannerUserName () {
        return topPlannerUserName;
    }

    public void setTopPlannerUserName (String topPlannerUserName) {
        this.topPlannerUserName = topPlannerUserName;
    }
    
    public String getManagerToSearchGroup () {
        return managerToSearchGroup;
    }
    
    public void setManagerToSearchGroup (String managerToSearchGroup) {
        this.managerToSearchGroup=managerToSearchGroup;
    }

    public String getCustguid () {
        return custguid;
    }

    public void setCustguid (String custguid) {
        this.custguid = custguid;
    }

    public String getHierarchyGuid () {
        return hierarchyGuid;
    }

    public void setHierarchyGuid (String hierarchyGuid) {
        this.hierarchyGuid = hierarchyGuid;
    }

    public List <Program> getPrograms () {
        return programs;
    }

    public void setPrograms (List <Program> programs) {
        this.programs = programs;
    }
    public CompPlan () {}
    public Employee getTopManager () {
        return topManager;
    }

    public void setTopManager (Employee topManager) {
        this.topManager = topManager;
    }

    private String         groupName;
    private List <Program> programs;

    private Employee       topManager;

    @Override
    public boolean equals (Object o) {
        if (this == o)
            return true;
        if (o == null || getClass () != o.getClass ())
            return false;
        CompPlan compPlan = (CompPlan) o;
        return id == compPlan.id && name.equals (compPlan.name);
    }

    @Override
    public int hashCode () {
        return Objects.hash (id, name);
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
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

    public String getGroupName () {
        return groupName;
    }

    public void setHierName (String hierName) {
        this.hierName = hierName;
    }

    public int getPlanStageId () {
        return planStageId;
    }

    public void setPlanStageId (int planStageId) {
        this.planStageId = planStageId;
    }

    public void setGroupName (String groupNames) {
        this.groupName = groupNames;
    }

    public String getBaseCurrencyCode () {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode (String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }
    
    public void setempSpareName(String program) {
		this.empSpareName = program;
	}

	public String getempSpareName() {
		return empSpareName;
	}

	public void setempSpareId(String spareId) {
		this.empSpareId = spareId;
	}

	public String getempSpareId() {
		return empSpareId;
	}
}
