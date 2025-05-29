package com.hrsoft.db.datadriven.model;

import java.util.Objects;

public class Program {
	private CompPlan plan;
	private int id;
	private String name;
	private int order;
	private int typeId;
	private String typeName;
	private String programName;
	private int programId;
	private int subProgramId;
	private String subProgramName;
	private String managerForProgramName;
	private String empWithAwardLink;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Program program = (Program) o;
		return id == program.id && name.equals(program.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	public CompPlan getPlan() {
		return plan;
	}

	public void setPlan(CompPlan plan) {
		this.plan = plan;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/* Written by Mashood */
	public void setProgramName(String program) {
		this.programName = program;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public int getProgramId() {
		return programId;
	}

	public void setSalarySubProgramId(int subProgramId) {
		this.subProgramId = subProgramId;
	}

	public int getSalarySubProgramId() {
		return subProgramId;
	}

	public void setCompProgramSubName(String subProgramName) {
		this.subProgramName = subProgramName;
	}

	public String getCompProgramSubName() {
		return subProgramName;
	}

	public void setManagerForProgram(String managerForProgramName) {
		this.managerForProgramName = managerForProgramName;
	}

	public String getManagerForProgram() {
		return managerForProgramName;
	}
	
	public void setEmpWithAwardLink(String empWithAwardLink) {
		this.empWithAwardLink = empWithAwardLink;
	}

	public String getEmpWithAwardLink() {
		return empWithAwardLink;
	}

}
