
package com.hrsoft.utils.files;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hrsoft.config.ConfigFactory;
import com.hrsoft.utils.csvprocessing.CSVFileManagerExceptions;

public class FileUtils {

    // Method to move files to the archive directory
    public static void moveFilesToArchive () {
        File reportDirectory = new File (System.getProperty ("user.dir") + "/reports");
        File archiveDirectory = new File (System.getProperty ("user.dir") + "/reports/archive");
        File[] files = reportDirectory.listFiles ();

        if (files == null) {
            return;
        }

        Date threeDaysAgo = Date.from (LocalDate.now ().minusDays (3).atStartOfDay ().atZone (ZoneId.systemDefault ())
                                                .toInstant ());

        Arrays.stream (files)
              .filter (file -> file.lastModified () < threeDaysAgo.getTime ())
              .forEach (file -> {
                  File destinationFile = new File (archiveDirectory, file.getName ());
                  if (file.renameTo (destinationFile)) {
                      System.out.println ("File moved to archive: " + destinationFile.getAbsolutePath ());
                  } else {
                      System.out.println ("Failed to move file: " + file.getAbsolutePath ());
                  }
              });
    }

    public void Util_DeleteOldFilesInArchive () {
        try {
            File folder = new File (System.getProperty ("user.dir") + "/reports/Archive");
            for (File file : folder.listFiles ())
                if (!file.isDirectory ())
                    file.delete ();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }

    }

    public static String getCellValueFromCDFFile (String filePath, int rowIndex, int columnIndex) {
        File file = new File (filePath);
        try {
            FileReader fileReader = new FileReader (file);
            try (BufferedReader bufferedReader = new BufferedReader (fileReader)) {
                String line = " ";
                String delimiter = ",";
                String[] tempArr;
                int count = 0;
                while ( (line = bufferedReader.readLine ()) != null && count <= rowIndex) {
                    tempArr = line.split (delimiter);
                    if (rowIndex == count)
                        return tempArr[columnIndex];
                    else
                        ++count;
                }
                bufferedReader.close ();
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return "";
    }


    public static List <String> collectDataObjectCSVList (String csvFolderPath) {
        List <String> dataObjectFileNames = new ArrayList <> ();

        try {
            validateCsvFolderPath (csvFolderPath);
            try (DirectoryStream <Path> dirStream = Files.newDirectoryStream (Paths.get (csvFolderPath), "DO_*.csv")) {
                for (Path path : dirStream) {
                    String coreFileName = transformFileName (path.getFileName ().toString ());
                    dataObjectFileNames.add (coreFileName);
                    // System.out.println("File collected: "+coreFileName);
                }
            }
        } catch (CSVFileManagerExceptions e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return dataObjectFileNames;
    }

    private static void validateCsvFolderPath (String csvFolderPath) throws CSVFileManagerExceptions {
        if (csvFolderPath == null || csvFolderPath.isEmpty ()) {
            throw new CSVFileManagerExceptions ("CSV folder path is not configured.");
        }

        File folder = new File (csvFolderPath);
        if (!folder.isDirectory ()) {
            throw new CSVFileManagerExceptions ("CSV folder path is not a valid directory: " + csvFolderPath);
        }
    }

    private static String transformFileName (String fileName) {
        return fileName.replaceAll ("^DO_(.*?)\\.csv$", "$1");
    }
}
