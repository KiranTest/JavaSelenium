package com.hrsoft.test.buiderqa;

import static com.hrsoft.reports.ExtentLogger.info;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.Test;
import com.hrsoft.config.ConfigFactory;
import com.hrsoft.constants.Constants;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.cloudadmin.CloudAdminPage;
import com.hrsoft.gui.cloudadmin.DataManagementPage;
import com.hrsoft.gui.cloudadmin.DatasetExplorer;
import com.hrsoft.gui.cloudadmin.DatobjectRelationships;
import com.hrsoft.gui.cloudadmin.ManageDataObjects;
import com.hrsoft.gui.dataset.DatasetEditor;
import com.hrsoft.gui.dataview.DataViewPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.csvprocessing.CSVFileProcess;
import com.hrsoft.utils.csvprocessing.CSVReader;
import com.hrsoft.utils.files.FileUtils;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */

public class E2eDataObjectCreation extends WebBaseTest {

	private ManageDataObjects manageDataObjects = new ManageDataObjects();
	private CloudAdminPage cloudAdminPage = new CloudAdminPage();
	private DataManagementPage dataManagement = new DataManagementPage();
	private DatasetExplorer datasetExplorer = new DatasetExplorer();
	private DataViewPage dataview = new DataViewPage();
	private HRSoftPage hrSoftPage = new HRSoftPage();
	private List<String> dataObjectNames;
	public String tablePrefix = "LTIP_";
	private List<String> fileFeeds;
	private String productName = ConfigFactory.getConfig().productName();
	private String dataset = ConfigFactory.getConfig().dataSetName();
	private String reportName = RandomStringUtils.randomAlphabetic(4) + " Automation Report";
	private DatobjectRelationships custRelationship = new DatobjectRelationships();
	private DatobjectRelationships relationship = new DatobjectRelationships();
	private ArrayList<String> reportColumns = new ArrayList<>();

	@Test
	public void generateCsvFile() {
		AIQAAutomationBuilder.generatePromptResponse();
	}

	@Test(dependsOnMethods = "generateCsvFile")
	public 	void E2E_CreateDataObject_VerifyDataSet_GenerateReport() {

		CSVFileProcess.splitCsvFileByTableName(Constants.CSV_INIT_DOBJECT_DEF_FILE, Constants.CSV_DOBJECT_FILES_PATH,
				",");
		dataObjectNames = FileUtils.collectDataObjectCSVList(Constants.CSV_DOBJECT_FILES_PATH);
		fileFeeds = generateFileFeedsForTables(dataObjectNames);
//		AIQAAutomationBuilder.createFileFeeds();
		CSVReader csvReader = new CSVReader();
		new LoginPage().loadUrl(ConfigFactory.getConfig().webAppicationURL())
				.enterUsername(ConfigFactory.getConfig().userName()).enterPassword(ConfigFactory.getConfig().password())
				.clickLogin();
		manageDataObjects = cloudAdminPage.clickManageDataObjects();
		manageDataObjects.selectProduct(productName);
		dataObjectNames.forEach(t -> createDataObject(t));
		createRelationshipsBetweenTables("DataObjectRelationships");
		System.out.println("Tb size" + dataObjectNames.size());
		for (int i = 0; i < dataObjectNames.size(); i++) {
			createFileFeedMapUsingCsv(fileFeeds.get(i), dataObjectNames.get(i));
		}
		fileFeeds.forEach(i -> importFileFeed(i));
		List<CSVRecord> csvRecords = csvReader.readCSVData(Constants.CSV_INIT_DOBJECT_DEF_FILE);
		for (CSVRecord record : csvRecords) {
			reportColumns.add(record.get("ColumnName"));
		}
		createDataSet(dataset);
		createReportFromDataset(reportName, dataset, productName);
		System.out.println("E2E Data objects were created and report was generated successfully!");
		openCreatedReport(reportName);
	}

	private void openCreatedReport(String reportName) {

		dataview.hoverOnReport(reportName);
	}

	@Test // (dependsOnMethods = "E2E_CreateDataObject_VerifyDataSet_GenerateReport")
	public void E2E_DeleteDataObjects() {
		reportName = "Automation";
		dataObjectNames = FileUtils.collectDataObjectCSVList(Constants.CSV_DOBJECT_FILES_PATH);
		fileFeeds = generateFileFeedsForTables(dataObjectNames);
		new LoginPage().loadUrl(ConfigFactory.getConfig().webAppicationURL())
				.enterUsername(ConfigFactory.getConfig().userName()).enterPassword(ConfigFactory.getConfig().password())
				.clickLogin();
		deleteReport(reportName);
		deleteDataset(dataset);
		deleteFileFeeds();
		deleteDataObjects();
		System.out.println("E2E Data objects were deleted successfully!");
	}

	private void createDataObject(String tableName) {
		manageDataObjects.createNewDataObject(tableName).selectDataObject(tableName);
		addDataObjectColumns(tableName);
		manageDataObjects.synchronizeDataObject(tableName);
	}
	
	public static List<String> generateFileFeedsForTables(List<String> dataObjNames) {
        String prefix = "1_test"; // Change the prefix here if needed
        List<String> fileFeeds = IntStream.range(0, dataObjNames.size())
                .mapToObj(i -> prefix + "_" + dataObjNames.get(i)).collect(Collectors.toList());

        return Arrays.asList(fileFeeds.toArray(new String[0]));
    }
	private void createFileFeed(String fileFeed, String tableName) {

		dataManagement = cloudAdminPage.clickDataManagement(ConfigFactory.getConfig().webAppicationURL());
		cloudAdminPage.refresh();
		manageDataObjects.selectProductInDataManagement(productName);
		manageDataObjects.defineNewFileFeed(fileFeed).clickFieldMapping();
		CSVReader csvReader = new CSVReader();
		List<CSVRecord> csvRecords = csvReader
				.readCSVData(Constants.CSV_DOBJECT_FILES_PATH + "\\DO_" + tableName + ".csv");
		for (CSVRecord record : csvRecords) {
			String fieldName = record.get("ColumnName");
			String dataType = record.get("DataType");
			if (dataType.equalsIgnoreCase("varchar"))
				dataType = "String";
			manageDataObjects.addFieldMapping(fieldName, tableName, dataType);
		}
		manageDataObjects.clickvalidate();
		System.out.println("Feed file " + fileFeed + " was created");
	}

	private void createFileFeedMapUsingCsv(String fileFeed, String dataObject) {
		String filePath = Constants.CSV_DOBJECT_FILES_PATH + "\\FileFeeds\\" + fileFeed + ".csv";
		dataManagement = cloudAdminPage.clickDataManagement(ConfigFactory.getConfig().webAppicationURL());
		manageDataObjects.selectProductInDataManagement(productName);
		manageDataObjects.defineNewFileFeed(fileFeed).selectExtensionForFeed("csv").clickFieldMapping()
				.clickMapUsingCsv();
		manageDataObjects.uploadFilesForMapping(filePath, dataObject);
	}

	private void importFileFeed(String fileFeed) {
		String filePath = Constants.CSV_DOBJECT_FILES_PATH + "\\FileFeeds\\" + fileFeed + ".csv";
		cloudAdminPage.clickDataManagement(ConfigFactory.getConfig().webAppicationURL());
		manageDataObjects.selectProductInDataManagement(productName);
		doWait(3000);
		manageDataObjects.importFileFeed(fileFeed);
		dataManagement.selectGivenItemFromDropDownIfAny("Data File", fileFeed);
		dataManagement.uploadFiles(filePath).clickPreview();

		String errorMessage = dataManagement.getPreviewTitleErrorMessage();
		if (errorMessage != null && errorMessage.contains("record(s) have column count mismatch"))
			info(" Preview Title says records have COLUMN COUNT MISMATCH");

		dataManagement.clickValidate().waitTillJobIsFinished(10).closePopup().clickImport().waitTillJobIsFinished(10)
				.closePopup();
		System.out.println("Import of  " + fileFeed + " was done");
		doWait(3000);
	}

	private void createDataSet(String datasetName) {
		new LoginPage().loadUrl(ConfigFactory.getConfig().webAppicationURL());
		cloudAdminPage = hrSoftPage.clickCloudAdmin();
		cloudAdminPage.clickDatasetExplorer();
		CSVReader csvReader = new CSVReader();
		List<CSVRecord> csvRecords = csvReader
				.readCSVData(Constants.CSV_DOBJECT_FILES_PATH + "//" + "DataObjectRelationships" + ".csv");
		System.out.println("Tables for rel " + csvRecords);
		DatasetEditor datasetEditor = datasetExplorer.clickDatasetPlusIcon().createDataset(datasetName);

		List<String> dataObjectList = new ArrayList<>();
		Set<String> uniqueDataObjects = new HashSet<>();

		for (CSVRecord csvRecord : csvRecords) {
			String parentTable = csvRecord.get("ParentTable");
			String childTable = csvRecord.get("ChildTable");

			if (!uniqueDataObjects.contains(parentTable)) {
				dataObjectList.add(parentTable);
				uniqueDataObjects.add(parentTable);
			}

			if (!uniqueDataObjects.contains(childTable)) {
				dataObjectList.add(childTable);
				uniqueDataObjects.add(childTable);
			}
		}

		for (String tableName : dataObjectList) {
			System.out.println(tableName);
		}

		for (int i = 0; i < dataObjectList.size(); i++) {
			String tbl = dataObjectList.get(i);
			if (i == 0) {
				datasetEditor.selectRootDataObjectSource(tbl);
			} else {
				datasetEditor.selectNextDataObjects(tbl);
			}
		}
		datasetEditor.addAllFields().previewData();
		System.out.println("Dataset " + datasetName + " was created");
	}

	private void createReportFromDataset(String reportName, String dataset, String productName,
			ArrayList<String> reportColumns) {
		new LoginPage().loadUrl(ConfigFactory.getConfig().webAppicationURL());
		hrSoftPage = new DataViewPage();
		hrSoftPage.clickDataViewExplorerPage();
		datasetExplorer.createNewReport(reportName);
		datasetExplorer.saveNewReport(reportName).reportDesigner(reportName, dataset, productName, reportColumns);
		System.out.println("Report- " + reportName + "- was created");
	}

	private void createReportFromDataset(String reportName, String dataset, String productName) {
		new LoginPage().loadUrl(ConfigFactory.getConfig().webAppicationURL());
		DataViewPage dataview = new DataViewPage();
		hrSoftPage = new DataViewPage();
		hrSoftPage.clickDataViewExplorerPage();
		datasetExplorer.createNewReport(reportName);
		datasetExplorer.saveNewReport(reportName).reportDesigner(reportName, dataset, productName);
		CloudAdminPage cap = hrSoftPage.clickCloudAdmin();
		cap.refreshNavigationView();
		System.out.println("Report- " + reportName + "- was created");
	}

	private void deleteReport(String reportName) {
		new LoginPage().loadUrl(ConfigFactory.getConfig().webAppicationURL());
		HRSoftPage page = new DataViewPage();
		page.clickDataViewExplorerPage();
		dataview.selectProductForReport("DATAview");
		manageDataObjects.selectCreatedByMe().searchAndDeleteReport(reportName);
	}

	private void deleteDataset(String datasetName) {
		new LoginPage().loadUrl(ConfigFactory.getConfig().webAppicationURL());
		cloudAdminPage = hrSoftPage.clickCloudAdmin();
		cloudAdminPage.clickDatasetExplorer();
		// datasetExplorer.selectClient(productName);
		manageDataObjects.searchAndDeleteDataset(datasetName);
	}

	private void deleteFileFeeds() {
		cloudAdminPage.clickDataManagement(ConfigFactory.getConfig().webAppicationURL());
		manageDataObjects.selectProductInDataManagement(productName);
		manageDataObjects.clickFeedManagement();
		fileFeeds.forEach(f -> manageDataObjects.deleteFileFeed(f));
	}

	private void deleteDataObjects() {
		manageDataObjects = cloudAdminPage.clickManageDataObjects();
		manageDataObjects.selectProduct(productName);
		for (int i = dataObjectNames.size() - 1; i >= 0; i--)
			manageDataObjects.deleteDataObject(dataObjectNames.get(i));
	}

	private void createRelationshipsBetweenTables(String filename) {
		CSVReader csvReader = new CSVReader();
		List<CSVRecord> csvRecords = csvReader.readCSVData(Constants.CSV_DOBJECT_FILES_PATH + "//" + filename + ".csv");
		System.out.println("Tables for rel " + csvRecords);
		for (CSVRecord csvRecord : csvRecords) {
			custRelationship.setKey("CUST_GUID");
			custRelationship.setTable1(csvRecord.get("ParentTable"));
			custRelationship.setTable2(csvRecord.get("ChildTable"));
			custRelationship.setType("S");
			relationship.setKey(csvRecord.get("ForeignKeyColumn"));
			relationship.setTable1(csvRecord.get("ParentTable"));
			relationship.setTable2(csvRecord.get("ChildTable"));
			relationship.setType("S");

			String relationshipType = csvRecord.get("RelationshipType");
			switch (relationshipType) {
			case "OneToOne":
				custRelationship.setForeignCardinality("1");
				custRelationship.setLocalCardinality("1");

				relationship.setForeignCardinality("1");
				relationship.setLocalCardinality("1");
				break;
			case "ManyToOne":
				custRelationship.setForeignCardinality("N");
				custRelationship.setLocalCardinality("1");

				relationship.setForeignCardinality("N");
				relationship.setLocalCardinality("1");
				break;
			case "ManyToMany":
				custRelationship.setForeignCardinality("N");
				custRelationship.setLocalCardinality("N");

				relationship.setForeignCardinality("N");
				relationship.setLocalCardinality("N");
				break;
			case "OneToMany":
				custRelationship.setForeignCardinality("N");
				custRelationship.setLocalCardinality("1");

				relationship.setForeignCardinality("N");
				relationship.setLocalCardinality("1");
				break;
			}

			manageDataObjects.createCustGuidRelationship(custRelationship);
			manageDataObjects.createRelationship(relationship);
			manageDataObjects.synchronizeDataObject(csvRecord.get("ParentTable"));
		}
	}

	@Test
	public void TestDelMeE2E_CreateDataObject_VerifyDataSet_GenerateReport() {

		new LoginPage().loadUrl(ConfigFactory.getConfig().webAppicationURL())
				.enterUsername(ConfigFactory.getConfig().userName()).enterPassword(ConfigFactory.getConfig().password())
				.clickLogin();
		// hrSoftPage.proxyAs("admin_user");
		manageDataObjects = cloudAdminPage.clickManageDataObjects();
		manageDataObjects.selectProduct(productName);
		dataObjectNames.forEach(t -> createDataObject(t));
		manageDataObjects = cloudAdminPage.clickManageDataObjects();
		manageDataObjects.selectProduct(productName);
		createRelationshipsBetweenTables("DataObjectRelationships");

	}

	private void addDataObjectColumns(String tableName) {
		CSVReader csvReader = new CSVReader();
		List<CSVRecord> csvRecords = csvReader
				.readCSVData(Constants.CSV_DOBJECT_FILES_PATH + "\\DO_" + tableName + ".csv");
		for (CSVRecord record : csvRecords) {
			String fieldName = record.get("ColumnName");
			String dataType = record.get("DataType").toLowerCase();
			String primarykey = record.get("Constraints");
			if ("PrimaryKey".trim().equalsIgnoreCase(primarykey))
				manageDataObjects.addNewPrimaryDataField(fieldName, dataType);
			else if ("VARCHAR".trim().equalsIgnoreCase(dataType)) {
				manageDataObjects.addDataField(fieldName, dataType);
			} else if ("INT".trim().equalsIgnoreCase(dataType)) {
				manageDataObjects.addDataField(fieldName, dataType);
			} else if ("DECIMAL".trim().equalsIgnoreCase(dataType)) {
				manageDataObjects.addDataField(fieldName, dataType);
			} else if ("DATE".trim().equalsIgnoreCase(dataType)) {
				manageDataObjects.addDataField(fieldName, dataType);
			} else if ("TEXT".trim().equalsIgnoreCase(dataType)) {
				manageDataObjects.addDataField(fieldName, "VARCHAR");
			}

		}
	}

	@Test
	public void splitINputTables() {
		CSVFileProcess.splitCsvFileByTableName(Constants.CSV_INIT_DOBJECT_DEF_FILE, Constants.CSV_DOBJECT_FILES_PATH,
				",");
	}

//	public static List<String> generateFileFeedsForTables(List<String> dataObjNames) {
//		String prefix = "1_test"; // Change the prefix here if needed
//		List<String> fileFeeds = IntStream.range(0, dataObjNames.size())
//				.mapToObj(i -> prefix + "_" + dataObjNames.get(i)).collect(Collectors.toList());
//
//		return Arrays.asList(fileFeeds.toArray(new String[0]));
//	}
}
