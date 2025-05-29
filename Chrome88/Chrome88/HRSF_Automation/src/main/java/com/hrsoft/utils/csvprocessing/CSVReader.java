package com.hrsoft.utils.csvprocessing;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.*;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

	public List<CSVRecord> readCSVData(String filePath) {
		List<CSVRecord> csvRecords = new ArrayList<>();

		try (FileReader fileReader = new FileReader(filePath);
				CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			// Parse CSV records and add them to the list
			for (CSVRecord record : csvParser) {
				csvRecords.add(record);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return csvRecords;
	}
}
