package com.hrsoft.utils.csvprocessing;

public class CSVFileManagerExceptions extends RuntimeException {
	
	public CSVFileManagerExceptions(String message) {
        super(message);
    }

    public CSVFileManagerExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
