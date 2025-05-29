package com.hrsoft.test.datamanagement;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static com.hrsoft.reports.ExtentLogger.info;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import java.util.List;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.cloudadmin.*;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.files.FileUtils;
import com.hrsoft.utils.zerocell.TestDataExcel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.regex.Pattern;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static org.testng.Assert.fail;

public class FileFeedRegressionTestSuite extends WebBaseTest {
    static final String ResourcesFolderPath = System.getProperty("user.dir") + "/src/test/resources/test-input/";
    private static final String HRSOFTCLOUD = "HRSOFTCLOUD";
    private static final String COMPENSATION = "COMPENSATION";
    private static final String ClientName = Constants.custId;
    private static final String CDF_FEED_NAME_FOR_FEED_TEST = "COMMON_"+ClientName.toUpperCase()+"_DEMOGRAPHIC";
    private static final String ESP_FEED_NAME_FOR_FEED_TEST = "COMPENSATION_"+ClientName.toUpperCase()+"_EMP_SPARES";

    private void importFeedFileShouldCompleteWithErrors (String testCaseFileLocation, String expectedErrorMessage, TestDataExcel data) {
        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + testCaseFileLocation);
        String filePath = ResourcesFolderPath+ClientName+testCaseFileLocation + "/" +fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
                    .filterErrorMessageInDetailsLogPopup()
                    .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017",expectedErrorMessage);
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" in "+feedName+" from "+product+" product");
        }
        else if(dataManagementPage.isJobFailed()) {
            dataManagementPage.openDetailedLogs ();
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            for (String text : filteroptionnew) {
        		info("Details Logs Error : "+text);
        	}
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import OR Successfully imported instead of Completing with Errors "+feedName+" from "+product+" product");

    }
    public void importFeedFileShouldFail (String testCaseFileLocation, String expectedErrorMessage, TestDataExcel data) {
        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + testCaseFileLocation);
        String filePath = ResourcesFolderPath+ClientName+testCaseFileLocation+"/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobFailed()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
                    .filterErrorMessageInDetailsLogPopup()
                    .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017",expectedErrorMessage);
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" Passed in "+feedName+" from "+product+" product");
        }
        else if(dataManagementPage.isJobCompletedWithErrors()) {
            dataManagementPage.openDetailedLogs ();
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            for (String text : filteroptionnew) {
        		info("Details Logs Error : "+text);
        	}
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            fail(" Job Completed With Error in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Job Completed with Errors OR Successfully imported instead of Failing"+feedName+" from "+product+" product");
    }
    public void importFeedFileShouldCompleteSuccessfully (String testCaseFileLocation, TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath + ClientName + testCaseFileLocation);
        String filePath = ResourcesFolderPath + ClientName + testCaseFileLocation + "/" + fileName;
        String feedName = getFeedNameFromFileName(fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement(data.getApplicationURL()).defaultConfigurationForFileFeed(",", feedName, product)
                .selectProduct(product)
                .selectImportTab();

        if (!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File", feedName))
            fail("No list item " + feedName + " found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (!dataManagementPage.isJobCompletedSuccessFully()) {
            if (dataManagementPage.isJobFailed()) {
                dataManagementPage.openDetailedLogs ();
                List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
                for (String text : filteroptionnew) {
            		info("Details Logs : "+text);
            	}
                List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
                info("Checking DB Error count : "+matchedDBError.size());
            	for (String text : matchedDBError) {
            		info("DB Error : "+text);
            	}
                softAssert.fail(" : Import Failed for " + feedName + " from " + product + " product");
            }
            else if (dataManagementPage.isJobCompletedWithErrors()) {
            	dataManagementPage.openDetailedLogs ();
                List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
                for (String text : filteroptionnew) {
            		info("Details Logs : "+text);
            	}
                List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
                info("Checking DB Error count : "+matchedDBError.size());
            	for (String text : matchedDBError) {
            		info("DB Error : "+text);
            	}
            	softAssert.fail(" : Import Completed with Errors for " + feedName + " from " + product + " product");
            }
            else
                softAssert.fail(" : Failed to import " + feedName + " from " + product + " product");
        }
    }
    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_01_UMEI_importShouldThrowErrorForInvalidHierarchyType (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_01");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_01/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
                    .filterErrorMessageInDetailsLogPopup()
                    .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","supplied are not valid with feedCode")
                    .rowValuePresentInColumnofGrid(3,"GRID_1516768038509_copy_5df68017","Feed File")
                    .rowValuePresentInColumnofGrid(3, "GRID_1516768038509_copy_5df68017", "Hierarchy Type");
            info(" in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import OR Successfully imported "+feedName+" from "+product+" product");
    }

    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_11_UMEI_importShouldThrowErrorCoPlannerIdCheckFailed (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldFail("/RG_UMEI_FILE_11", "Coplanner ID check failed. No employee with given Compensation manager id exists in the system invalid planner", data);
    }

    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_16_UMEI_importShouldThrowErrorForUserWithoutHierarchy (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldCompleteWithErrors("/RG_UMEI_FILE_16", "User with manager and without hierarchy not permitted", data);
    }

    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_17_UMEI_importShouldThrowErrorForSelfReference (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldCompleteWithErrors("/RG_UMEI_FILE_17", "Self Reference", data);
    }

    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_18_UMEI_importShouldThrowErrorForDuplicateEmpId (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        UmeiAttributes umeiAttributesPage = new CloudAdminPage()
                .clickUmeiAttribute(data.getApplicationURL());

        String originalAttributeValue = umeiAttributesPage.attributeValueOf("replace_user_names_or_emp_ids");

        if (!originalAttributeValue.equals("false")) {
            umeiAttributesPage.clickOnInlineEditOfUmeiAttributeGrid("replace_user_names_or_emp_ids")
                    .editAttributeValueInUmeiAttributeTo("false");
        }

        importFeedFileShouldCompleteWithErrors("/RG_UMEI_FILE_18", "Employee ID check failed", data);

        umeiAttributesPage = new CloudAdminPage()
                .clickUmeiAttribute(data.getApplicationURL());

        umeiAttributesPage.clickOnInlineEditOfUmeiAttributeGrid("replace_user_names_or_emp_ids")
                .editAttributeValueInUmeiAttributeTo(originalAttributeValue);
    }

    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_19_UMEI_importShouldThrowErrorForNoTopManager (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldFail("/RG_UMEI_FILE_19", "Aborting File Import. No Top Level Manager found in File and System", data);
    }


    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_02_UMEI_importShouldThrowErrorForNullColumn (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_02");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_02/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
        	info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .filterErrorMessageInDetailsLogPopup()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","we found that its value is null");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("FIRST_NAME we found that its value is null in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_03_UMEI_validateAndImportShouldFailForUserRoleExceding (TestDataExcel data) {
    	SoftAssert softAssert = new SoftAssert();
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/RG_UMEI_FILE_03");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_03/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        
        dataManagementPage.uploadFiles (filePath)
                          .clickValidate ()
                          .waitTillJobIsFinished(10);
        
        if(dataManagementPage.isJobCompletedWithErrors()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .filterErrorMessageInDetailsLogPopup()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","String or binary data would be truncated in table");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" String or binary data would be truncated in table 'tempdb.dbo', column 'ROLE_ID' "+feedName+" from "+product+" product");
        }
        else
            softAssert.fail(" Failed to validate "+feedName+" from "+product+" product");
      //Import
        dataManagementPage.closePopup()
                          .closePopup()
                          .clickCancel()
                          .uploadFiles(filePath)
                          .clickImport()
                          .waitTillJobIsFinished(10);
        if(dataManagementPage.isJobCompletedWithErrors()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","String or binary data would be truncated in table");
            info(" String or binary data would be truncated in table 'tempdb.dbo', column 'ROLE_ID' "+feedName+" from "+product+" product");
        }
        else
            softAssert.fail(" Failed to Import "+feedName+" from "+product+" product");
        softAssert.assertAll();
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_20_UMEI_importShouldThrowErrorForTopManageChangeWithoutNewTopManager (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_20");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_20/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
        	info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .filterErrorMessageInDetailsLogPopup()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Top Level Manager in File and System are Different");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Top Level Manager in File and System are Different in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_04_UMEI_importShouldThrowErrorForInvalidUserName (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_04");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_04/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
        	info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .filterErrorMessageInDetailsLogPopup()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Username check failed.");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Username check failed in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_06_UMEI_importShouldThrowErrorForInvalidJobCode (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_06");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_06/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
        	info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .filterErrorMessageInDetailsLogPopup()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Job_Code check failed");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Job_Code check failed in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_05_UMEI_importShouldThrowErrorForInvalidEmpId (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_05");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_05/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
        	info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .filterErrorMessageInDetailsLogPopup()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Employee ID check failed. A record already exists in the system with same EmpId");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Employee ID check failed. A record already exists in the system with same EmpId in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_07_UMEI_importShouldThrowErrorForInvalidEmpStatus (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_07");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_07/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
        	info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .filterErrorMessageInDetailsLogPopup()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Emp Status check failed. No matching Emp Status code");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Emp Status check failed. No matching Emp Status code in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_08_UMEI_importShouldThrowErrorForInvalidPartTimeFactor (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_08");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_08/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
        	info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .filterErrorMessageInDetailsLogPopup()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","PART_TIME_FACTOR check failed. Part-time factor must be greater than zero.");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("PART_TIME_FACTOR check failed. Part-time factor must be greater than zero. in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_09_UMEI_importShouldThrowErrorForMarkerDataValidationIncorrectCompPlan (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = COMPENSATION;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_09");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_09/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobFailed()) {
        	info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Comp Plan check failed. No matching Comp Plan exists in the system");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Comp Plan check failed. No matching Comp Plan exists in the system in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_22_UMEI_importShouldThrowErrorForInvalidCompPlanName (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = COMPENSATION;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_22");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_22/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobFailed()) {
        	info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Invalid Compensation Plan Name and/or Component Alias");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Invalid Compensation Plan Name and/or Component Alias in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_25_UMEI_importShouldThrowErrorForMarkerDataValidationIncorrectMarkerSet (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = COMPENSATION;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_25");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_25/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobFailed()) {
        	info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Marker Set check failed. No matching Marker Set exists in the system");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Marker Set check failed. No matching Marker Set exists in the system in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_26_UMEI_importShouldThrowErrorForMarkerDataValidationIncorrectGeoPayId (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = COMPENSATION;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_26");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_26/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobFailed()) {
        	info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Geo Pay ID check failed. No matching Geo Pay ID exists in the system");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("No matching Geo Pay ID exists in the system in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_27_UMEI_importShouldThrowErrorForMarkerDataValidationIncorrectCurrencyCode (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = COMPENSATION;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_27");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_27/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobFailed()) {
        	info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Currency Code check failed. No matching Currency Code exists in the system");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("No matching Currency Code exists in the system in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }

    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_12_UMEI_imporShouldThrowErrorForInvalidHierarchyName (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldCompleteWithErrors("/RG_UMEI_FILE_12", "Hierarchy check failed. No matching hierarchy exists in the system Invalid hierarchy", data);
    }

    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_14_importShouldThrowErrorForInvalidCoPlannerData (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldFail("/RG_UMEI_FILE_14", "Coplanner and Manager cannot be the same", data);
    }
    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_15_UMEI_imporShouldThrowErrorForInvalidManagerId (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldCompleteWithErrors("/RG_UMEI_FILE_15", "Manager ID check failed. No employee with given Compensation manager id exists in the system for any Hierarchy. invalid manager", data);
    }

    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_21_UMEI_importShouldThrowErrorForCircularReference (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldFail("/RG_UMEI_FILE_21", "Circular Reference", data);
    }
    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_10_UMEI_importForTerminatingUser (TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/RG_UMEI_FILE_10_T");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_10_T/"+fileName;

        ManageUsersPage manageUsers = new CloudAdminPage()
                .clickManageUsers (data.getApplicationURL ());

        String userName = FileUtils.getCellValueFromCDFFile(filePath, 1, 0);
        String hierarchyName = FileUtils.getCellValueFromCDFFile(filePath, 1, 38);

        manageUsers = new CloudAdminPage().clickManageUsers (data.getApplicationURL ());
        manageUsers.setUserNameInInputField(userName)
                .selectAllStatusUncheckedCheckbox()
                .selectHierarchyFromDropDown(hierarchyName)
                .clickOnSearchButton();

        String originalEmpStatus = manageUsers.clickOnEditOfFirstRowInSearchResults().getEmpStatusOfUserFromEditPopUp();
        if(originalEmpStatus.equalsIgnoreCase("Terminated")) {
            info(" User with username "+ userName +" is already terminated");
        } else {
            //terminating the user
            importFeedFileShouldCompleteSuccessfully ("/RG_UMEI_FILE_10_T", data);

            //verifying if changes were made
            manageUsers = new CloudAdminPage().clickManageUsers (data.getApplicationURL ());
            manageUsers.setUserNameInInputField(userName)
                    .selectAllStatusUncheckedCheckbox()
                    .selectHierarchyFromDropDown(hierarchyName)
                    .clickOnSearchButton();

            if (manageUsers.clickOnEditOfFirstRowInSearchResults().getEmpStatusOfUserFromEditPopUp().equalsIgnoreCase("Terminated")) {
                info(": User with username"+userName+" was TERMINATED successfully");
            } else
                softAssert.fail(": User with username"+userName+" was not terminated");

            //resetting
            importFeedFileShouldCompleteSuccessfully ("/RG_UMEI_FILE_10_A", data);
            //verifying if changes were made
            manageUsers = new CloudAdminPage().clickManageUsers (data.getApplicationURL ());
            manageUsers.setUserNameInInputField(userName)
                    .selectAllStatusUncheckedCheckbox()
                    .selectHierarchyFromDropDown(hierarchyName)
                    .clickOnSearchButton();

            if (manageUsers.clickOnEditOfFirstRowInSearchResults().getEmpStatusOfUserFromEditPopUp().equalsIgnoreCase("Active")) {
                info(": User with username"+userName+" was changed back to ACTIVE EMPSTATUS successfully");
            } else
                softAssert.fail(": User with username"+userName+" was NOT changed back to ACTIVE EMPSTATUS ");
        }
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_23_UMEI_imporShouldThrowErrorForInvalidCompPlanNameForIncorrectStartingPerfRating (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = COMPENSATION;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_23");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_23/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL())
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobFailed()) {
        	info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Invalid Compensation Plan Name and/or Component Alias");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Invalid Compensation Plan Name and/or Component Alias in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_13_UMEI_imporShouldThrowErrorForInvalidFilterPoolName (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_13");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_13/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL())
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors()) {
        	info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Mandatory Filter Pool Name check failed. No matching Filter pool exists in the system");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("No matching Filter pool exists in the system in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_24_UMEI_imporShouldThrowErrorForInvalidCompPlanNameForIncorrectNewPerfRating(TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = COMPENSATION;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_24");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_24/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL())
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobFailed()) {
        	info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Invalid Compensation Plan Name and/or Component Alias");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	System.out.println("matchedDBError Size : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info("Invalid Compensation Plan Name and/or Component Alias in "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to import "+feedName+" from "+product+" product");
    }
    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_36_UMEI_importShouldThrowErrorForEmptyEmpId (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_36");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_36/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickValidate()
                .waitTillJobIsFinished(10);

        if (dataManagementPage.isJobCompletedWithErrors() || dataManagementPage.isJobFailed()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
                    .filterErrorMessageInDetailsLogPopup()
                    .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","During validation of field")
                    .rowValuePresentInColumnofGrid(3,"GRID_1516768038509_copy_5df68017","EMP_ID")
                    .rowValuePresentInColumnofGrid(3, "GRID_1516768038509_copy_5df68017", "we found that its value is null in row");
            info(" in "+feedName+" from "+product+" product");
        }
        else {
            dataManagementPage.printErrorMessagesFromLogsForMessageTypeError()
                    .printErrorMessagesFromLogsForStatusValueError()
                    .openDetailedLogs()
                    .filterErrorMessageInDetailsLogPopup()
                    .printErrorMessagesFromDetailedLogs();
            fail(" : Failed to import OR Successfully imported "+feedName+" from "+product+" product");
        }
    }
    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_37_UMEI_importShouldThrowErrorForBrokenHierarchyDueToInvalidManager(TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldCompleteWithErrors("/RG_UMEI_FILE_37", "User is part of a broken Hierarchy due to invalid manager", data);
    }
    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_35_UMEI_importShouldCompleteSuccessfullyForNewAndExistingHierarchy(TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldCompleteSuccessfully("/RG_UMEI_FILE_35", data);

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_35");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_35/"+fileName;

        String hierarchyName = FileUtils.getCellValueFromCDFFile(filePath, 1, 38);

        SecurityManagement securityManagement = (new CloudAdminPage()).clickSecurityManagement(data.getApplicationURL());
        securityManagement.clickOnEmployeeHierarchies()
                .detachAndRecalculateHierarchy(hierarchyName)
                .deleteHierarchy(hierarchyName);
    }
    
    @Author (name = QAResources.VIVEK)
    @Test (groups = {TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_file", "generic", "positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void RG_UMEI_FILE_34_UMEI_importShouldCompleteSuccessfullyForNewP4PHierarchyType(TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        importFeedFileShouldCompleteSuccessfully("/RG_UMEI_FILE_34", data);

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/RG_UMEI_FILE_34");
        String filePath = ResourcesFolderPath+ClientName+"/RG_UMEI_FILE_34/"+fileName;

        String hierarchyName = FileUtils.getCellValueFromCDFFile(filePath, 1, 15);
        System.out.print("hierarchyName : "+hierarchyName);
        SecurityManagement securityManagement = (new CloudAdminPage()).clickSecurityManagement(data.getApplicationURL());
        securityManagement.clickOnEmployeeHierarchies()
                .detachAndRecalculateHierarchy(hierarchyName)
                .deleteHierarchy(hierarchyName);
    }
    
    private String getAvailableFilePathFromFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                return listOfFiles[i].getName();
            }
        }
        return null;
    }
    private String getFeedNameFromFileName(String fileName) {
        if(fileName != null) {
            int endIndex = fileName.indexOf ('.');
            return fileName.substring (0, endIndex);
        }
        return null;
    }
}
