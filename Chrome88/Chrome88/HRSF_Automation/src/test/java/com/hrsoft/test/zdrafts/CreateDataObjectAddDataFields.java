package com.hrsoft.test.zdrafts;

import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.testng.annotations.Test;

import com.hrsoft.utils.csvprocessing.CSVReader;

public class CreateDataObjectAddDataFields {
	@Test
	public void addDataFields() {

		// Call the method to read CSV data
		CSVReader csvReader = new CSVReader();
		List<CSVRecord> csvRecords = csvReader.readCSVData(
				"C:\\Users\\kiran.yajjala\\SVN\\HRSF_Automation\\src\\test\\resources\\AUTOMATION_EMP_MASTER.csv");

		// Iterate through CSV records
		for (CSVRecord record : csvRecords) {
			// Create a data object
			DataObject dataObject = new DataObject();

			// Access field values
			String fieldName = record.get("FieldName");
			String fieldType = record.get("FieldType");

			// Add fields based on field type
			if ("primary".equalsIgnoreCase(fieldType)) {
				dataObject.addPrimary(fieldName);
			} else if ("normal".equalsIgnoreCase(fieldType)) {
				dataObject.addNormal(fieldName);
			} else if ("unique".equalsIgnoreCase(fieldType)) {
				dataObject.addUnique(fieldName);
			}

		}

	}
}
