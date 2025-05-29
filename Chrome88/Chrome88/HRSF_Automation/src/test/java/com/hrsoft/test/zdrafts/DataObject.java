package com.hrsoft.test.zdrafts;

public class DataObject {
	private String fieldName;
    private String fieldType;

    public void addPrimary(String fieldName) {
        this.fieldName = fieldName;
        this.fieldType = "primary";
        System.out.println("Field Name: " + fieldName + ", Field Type: " + fieldType);
    }

    public void addNormal(String fieldName) {
        this.fieldName = fieldName;
        this.fieldType = "normal";
        System.out.println("Field Name: " + fieldName + ", Field Type: " + fieldType);
    }

    public void addUnique(String fieldName) {
        this.fieldName = fieldName;
        this.fieldType = "unique";
        System.out.println("Field Name: " + fieldName + ", Field Type: " + fieldType);
    }

}
