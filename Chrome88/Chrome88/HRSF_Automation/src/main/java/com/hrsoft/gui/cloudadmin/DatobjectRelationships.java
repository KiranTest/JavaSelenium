/**
 *@author : Mashood Khan
 *
 */
package com.hrsoft.gui.cloudadmin;

public class DatobjectRelationships {

	private String key;
	private String table1;
	private String table2;
	private String foreignCardinality;
	private String localCardinality;
	private String type;
	private String lop;

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setTable1(String table1) {
		this.table1 = table1;
	}

	public String getTable1() {
		return table1;
	}

	public void setTable2(String table2) {
		this.table2 = table2;
	}

	public String getTable2() {
		return table2;
	}

	public void setForeignCardinality(String foreignCardinality) {
		this.foreignCardinality = foreignCardinality;
	}

	public String getForeignCardinality() {
		return foreignCardinality;
	}

	public void setLocalCardinality(String localCardinality) {
		this.localCardinality = localCardinality;
	}

	public String getLocalCardinality() {
		return localCardinality;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setLocalOrdinalPosition(String lop) {
		this.lop = lop;
	}

	public String getLocalOrdinalPosition() {
		return type;
	}
	
}
