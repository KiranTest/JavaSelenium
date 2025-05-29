package com.hrsoft.utils.csvprocessing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

public class CSVFileProcess {
    private static Collection <String> listOfTables = new LinkedHashSet <String> ();
    public static ArrayList <String>   arrayList    = new ArrayList <> ();

    public static void splitCsvFileByTableName (String csvFilePath, String outputDirectory, String delimiter) {
        try (Reader reader = new FileReader (csvFilePath);
                CSVParser csvParser = createCSVParser (reader, delimiter);) {
            Map <String, List <CSVRecord>> tableRecordsMap = new HashMap <> ();
            String[] header = csvParser.getHeaderMap ().keySet ().toArray (new String[0]);

            Iterable <CSVRecord> records = csvParser.getRecords ();
            for (CSVRecord record : records) {
                String dataObjectName = record.get (0).trim ();
                // Follow the naming convention
                listOfTables.add (dataObjectName);
                String dataObjectFileName = "DO_" + dataObjectName + ".csv";

                tableRecordsMap.computeIfAbsent (dataObjectFileName, key -> new ArrayList <> ())
                               .add (record);
            }
            for (String s : listOfTables) {
                arrayList.add (s);
            }
            // Write records to separate CSV files
            for (Map.Entry <String, List <CSVRecord>> entry : tableRecordsMap.entrySet ()) {
                String dataObjectFileName = entry.getKey ();
                List <CSVRecord> recordsForTable = entry.getValue ();

                // Create and write to the CSV file
                try (CSVPrinter csvPrinter = createDataObjectWriter (outputDirectory, dataObjectFileName, header)) {
                    for (CSVRecord record : recordsForTable) {
                        csvPrinter.printRecord (record);
                    }
                }
            }
        } catch (IOException e) {}
    }

    private static CSVPrinter createDataObjectWriter (String outputDirectory, String dataObjectFileName,
                                                      String[] header) throws IOException {
        File dataObjectFile = new File (outputDirectory, dataObjectFileName);
        dataObjectFile.getParentFile ().mkdirs ();
        FileWriter fileWriter = new FileWriter (dataObjectFile);

        CSVFormat csvFormat = CSVFormat.DEFAULT
                                               .builder ()
                                               .setDelimiter (',')
                                               .setHeader (header)
                                               .setQuoteMode (QuoteMode.NONE)
                                               .setEscape ('\\')
                                               .build ();
        return new CSVPrinter (fileWriter, csvFormat);
    }

    private static CSVParser createCSVParser (Reader reader, String delimiter) {
        CSVFormat csvFormat = CSVFormat.DEFAULT
                                               .builder ()
                                               .setDelimiter (',')
                                               .setHeader ().build ();

        try {
            return csvFormat.parse (reader);
        } catch (IOException e) {
            throw new CSVFileManagerExceptions ("Error creating CSV parser.", e);
        }
    }
		
		// Method to save parameter value to a CSV file
	    public static void saveToCsv(String csvFileName, String paramName, String paramValue) {
	        try {
	            FileWriter fileWriter = new FileWriter(csvFileName, true); // Set true for append mode
	            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

	            csvPrinter.printRecord("ParameterName", "ParameterValue");
	            csvPrinter.printRecord(paramName, paramValue);

	            csvPrinter.flush();
	            csvPrinter.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
