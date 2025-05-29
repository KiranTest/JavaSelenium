# File Processing and Data Object Creation Workflow

In this guide, we'll walk through the complete data processing workflow, which includes creating data objects (tables), uploading data, generating datasets, applying filters, and generating reports.

## Initial Input Base CSV File

Before we begin, ensure you have the following file in your designated path:

- **File Name**: `base_dataObject_definitions.csv`
- **Path**: csvBaseDobjectsDefPath in FrameworkCOnfigFile

This file serves as the foundational input for our data processing workflow.

## Creating Data Objects

1. **Input CSV**: Start with an input CSV file that contains rows for various data objects (tables) required for your application.

2. **Data Object Creation**: Implement logic to read the input CSV file line by line. Extract the table name from each row to organize data entries into separate data structures for each data object.

3. **Creating Separate CSVs**: For each data object, create a new CSV file and write the corresponding data entries to it. Adhere to specific file naming conventions, such as using a prefix like `DO_` for data objects.

   - Example File Names: `DO_Employee.csv`, `DO_Product.csv`

4. **Output**: After processing, you'll have separate CSV files, each containing data for a specific data object.
   - **Path**: csvDataObjectsPath in FrameworkCOnfigFile

## Uploading Data

1. **Data Upload**: Implement a data upload process that reads the CSV files and uploads data into your application or database.

## Generating Datasets

1. **Dataset Creation**: Create datasets from the data objects using the uploaded data. Define the structure and criteria for each dataset.

## Generating Reports

1. **Report Generation**: Generate reports based on the filtered datasets. Define report formats and content.
