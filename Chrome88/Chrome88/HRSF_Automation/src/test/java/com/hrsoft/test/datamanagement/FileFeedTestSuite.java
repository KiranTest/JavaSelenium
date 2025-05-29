package com.hrsoft.test.datamanagement;

import static com.hrsoft.reports.ExtentLogger.info;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import com.hrsoft.constants.Constants;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import com.hrsoft.gui.cloudadmin.*;
import com.hrsoft.db.UMEIDbHelper;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;
import org.testng.asserts.SoftAssert;

public class FileFeedTestSuite extends WebBaseTest{

    private static final String ResourcesFolderPath =  System.getProperty ("user.dir") + "/src/test/resources/test-input/";
    private static final String HRSOFTCLOUD = "HRSOFTCLOUD";
    private static final String COMPENSATION = "COMPENSATION";
    private static final String ClientName=Constants.custId;
    private static final String CDF_FEED_NAME_FOR_FEED_TEST = "COMMON_"+ClientName.toUpperCase()+"_DEMOGRAPHIC";
    private static final String ESP_FEED_NAME_FOR_FEED_TEST = "COMPENSATION_"+ClientName.toUpperCase()+"_EMP_SPARES";
    private static final String CustGuid = Constants.custGuid;

       //Common functionality for 
       // SM_UMEI_FILE_02, SM_UMEI_FILE_03, SM_UMEI_FILE_04,SM_UMEI_FILE_05, SM_UMEI_FILE_06
       // SM_UMEI_FILE_47, SM_UMEI_FILE_48, SM_UMEI_FILE_49

       private void importFeedFileShouldBeSuccess(String testCaseFileLocation, String product,String url) {
           
           String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/"+testCaseFileLocation);
           String filePath = ResourcesFolderPath+ClientName+"/"+testCaseFileLocation+"/"+fileName;
           String feedName = getFeedNameFromFileName (fileName);
           //String product = "HRSOFTCLOUD";
           
           DataManagementPage dataManagementPage = new CloudAdminPage ()
                   .clickDataManagement (url).defaultConfigurationForFileFeed (",", feedName,product)
                   .selectProduct(product)
                   .selectImportTab();

           if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
               fail("No list item "+feedName+" found for Data File to select : ");

           dataManagementPage.uploadFiles (filePath)
                   .clickImport ()
                   .waitTillJobIsFinished(10);
           
           if(!dataManagementPage.isJobCompletedSuccessFully())
           {
               if(dataManagementPage.isJobFailed ()) {
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
                   fail(" : Job Failed for "+feedName+" from "+product+" product");
               }
               else if(dataManagementPage.isJobCompletedWithErrors ()) {
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
                   fail(" : Job Completed with Errors for "+feedName+" from "+product+" product");
               }
               else
                   fail(" : Failed to import "+feedName+" from "+product+" product");
           }

       }
       
       //Common functionality for negative test cases of 
       // SM_UMEI_FILE_02, SM_UMEI_FILE_03, SM_UMEI_FILE_04,SM_UMEI_FILE_05, SM_UMEI_FILE_06
       // SM_UMEI_FILE_47, SM_UMEI_FILE_48, SM_UMEI_FILE_49	 
       private void importFeedFileShouldFail(String testCaseFileLocation, String product,String url) {

           String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+"/"+testCaseFileLocation);
           String filePath = ResourcesFolderPath+ClientName+"/"+testCaseFileLocation+"/"+fileName;
           String feedName = getFeedNameFromFileName (fileName);

           DataManagementPage dataManagementPage = new CloudAdminPage ()
                   .clickDataManagement (url).defaultConfigurationForFileFeed (",", feedName,product)
                   .selectProduct(product)
                   .selectImportTab();

           if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
           fail("No list item "+feedName+" found for Data File to select : ");

           dataManagementPage.uploadFiles (filePath)
                   .clickImport ()
                   .waitTillJobIsFinished(10);
           
           if(!dataManagementPage.isJobFailed())
           {
               if(dataManagementPage.isJobCompletedWithErrors ())
                   fail(" : Job Completed with Errors for "+feedName+" from "+product+" product");
               else if(dataManagementPage.isJobCompletedSuccessFully ())
                   fail(" : Job Completed Successfully for "+feedName+" from "+product+" product");
               else
                   fail(" : Failed to show error for invalid "+feedName+" from "+product+" product");
           }
           

       }
       
      @Author(name = QAResources.KALYAN)
	  @Test (priority = -2, groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	  public void SM_UMEI_FILE_02_UMEI_validCommonDemographicFileShouldBeImportedSuccessfully (TestDataExcel data) {
    	  LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
  		  login.logIn(data.getUserName(), data.getPassword());
    	  importFeedFileShouldBeSuccess ("SM_UMEI_FILE_02_A", HRSOFTCLOUD,data.getApplicationURL());
	  }

      @Author(name = QAResources.KALYAN)
      @Test (priority = -1, groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
      public void SM_UMEI_FILE_47_UMEI_validP4PDemographicFileShouldBeImportedSuccessfully (TestDataExcel data) {
//        String feedName = "COMMON_HRSOFTI_DEMOGRAPHIC";
          LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
          login.logIn(data.getUserName(), data.getPassword());
          importFeedFileShouldBeSuccess ("SM_UMEI_FILE_47_A", HRSOFTCLOUD,data.getApplicationURL());
      }
	  
      @Author(name = QAResources.KALYAN)
      @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	  public void SM_UMEI_FILE_03_UMEI_validHierInputFileShouldBeImportedSuccessfully (TestDataExcel data) {
          LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
          login.logIn(data.getUserName(), data.getPassword());
          importFeedFileShouldBeSuccess ("SM_UMEI_FILE_03_A", HRSOFTCLOUD,data.getApplicationURL());
      }
      
      @Author(name = QAResources.KALYAN)
      @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
      public void SM_UMEI_FILE_48_UMEI_validFunctionalHierInputFileShouldBeImportedSuccessfully (TestDataExcel data) {
          LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
          login.logIn(data.getUserName(), data.getPassword());
          importFeedFileShouldBeSuccess ("SM_UMEI_FILE_48_A", HRSOFTCLOUD,data.getApplicationURL());
      }
      
      @Author(name = QAResources.KALYAN)
      @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
      public void SM_UMEI_FILE_04_UMEI_validCoPlannerFileShouldBeImportedSuccessfully (TestDataExcel data) {
          LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
          login.logIn(data.getUserName(), data.getPassword());
          importFeedFileShouldBeSuccess ("SM_UMEI_FILE_04_A", HRSOFTCLOUD,data.getApplicationURL());
      }

      @Author(name = QAResources.KALYAN)
      @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
      public void SM_UMEI_FILE_49_UMEI_validFunctionalCoPlannerFileShouldBeImportedSuccessfully (TestDataExcel data) {
          LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
          login.logIn(data.getUserName(), data.getPassword());
          importFeedFileShouldBeSuccess ("SM_UMEI_FILE_49_A", HRSOFTCLOUD,data.getApplicationURL());
      }

      @Author(name = QAResources.KALYAN)
      @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
      public void SM_UMEI_FILE_05_UMEI_validEmpSparesFileShouldBeImportedSuccessfully (TestDataExcel data) {
          LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
          login.logIn(data.getUserName(), data.getPassword());
          String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_05_A");
          String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_05_A/"+fileName;
          String feedName = getFeedNameFromFileName (fileName);

          final String product = COMPENSATION;


          DataManagementPage dataManagementPage = new CloudAdminPage ()
                  .clickDataManagement (data.getApplicationURL())
                  .setConfigurationForFileFeedTo(",", feedName,product, 1)
                  .selectProduct(product)
                  .selectImportTab();

          if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
              fail("No list item "+feedName+" found for Data File to select : ");

          dataManagementPage.uploadFiles (filePath)
                  .clickImport ()
                  .clickProceedInPopup()
                  .waitTillJobIsFinished(10);

          if(!dataManagementPage.isJobCompletedSuccessFully())
          {
              if(dataManagementPage.isJobFailed ()) {
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
                  fail(" : Job Failed for "+feedName+" from "+product+" product");
              }
              else if(dataManagementPage.isJobCompletedWithErrors ()) {
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
                  fail(" : Job Completed with Errors for "+feedName+" from "+product+" product");
              }
              else
                  fail(" : Failed to import "+feedName+" from "+product+" product");
          }
      }

      @Author(name = QAResources.KALYAN)
      @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
      public void SM_UMEI_FILE_06_UMEI_demographicFileWithEmptyHierarchyShouldBeImportedSuccessfully (TestDataExcel data) {
          LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
          login.logIn(data.getUserName(), data.getPassword());
          importFeedFileShouldBeSuccess ("SM_UMEI_FILE_06_A", HRSOFTCLOUD,data.getApplicationURL());
      }

	   /**
       * @author Kalyan Komati
       * @stlc.requirements SM_UMEI_FILE_07_A
       * 
       * File located under src/test/resources/SM_UMEI_FILE_07_A has data format as MM/DD/YYY
       * This file should get previewed, validated and uploaded without fail
       *
       * Note : make sure, data format in feed cfg is having MM/DD/YYYY, otherwise update the format
       */
      @Author(name = QAResources.KALYAN)
      @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
      public void SM_UMEI_FILE_07_UMEI_previewValidateAndImportShouldSuccessForValidDateFormat (TestDataExcel data) {

          LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
          login.logIn(data.getUserName(), data.getPassword());
          
          String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_07_A");
          String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_07_A/"+fileName;
          String feedName = getFeedNameFromFileName (fileName);

          final String product = HRSOFTCLOUD;
          DataManagementPage dataManagementPage = new CloudAdminPage ()
                  .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                  .selectProduct(product)
                  .selectConfigureFeedTab();

          if(!dataManagementPage.selectGivenFeedForConfigureFeed(feedName))
              fail("No list item "+feedName+" found for Feed to select : ");
          dataManagementPage.openFieldMapping();

          //verify date format is DD/MM/YYYY
          if(!dataManagementPage.verifyDateFormat("MM/DD/YYYY"))
              fail("Invalid date format configured for feed, expected MM/DD/YYYY");

          dataManagementPage.refresh ();
            
          dataManagementPage.selectProduct(product)
                  .selectImportTab();

          if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
              fail("No list item "+feedName+" found for Data File to select : ");
            
          dataManagementPage.uploadFiles (filePath)
                  .clickPreview ();
            
          if(!(dataManagementPage.isFeedPreviewSuccess() && !dataManagementPage.previewHasError()))
              fail(" : Unable to preview "+feedName+" from "+product+" product");

          dataManagementPage.clickValidate ()
                  .waitTillJobIsFinished(10);

          if(!dataManagementPage.isJobCompletedSuccessFully())
          {
              if(dataManagementPage.isJobFailed ())
                  fail(" : Validation Job Failed for "+feedName+" from "+product+" product");
              else if(dataManagementPage.isJobCompletedWithErrors ())
                  fail(" : Validation Job Completed with Errors for "+feedName+" from "+product+" product");
              else
                  fail(" : Failed to validate "+feedName+" from "+product+" product");
          }

          dataManagementPage.closePopup().refresh();

          dataManagementPage.selectProduct(product)
                  .selectImportTab();

          if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
              fail("No list item "+feedName+" found for Data File to select : ");

          dataManagementPage.uploadFiles (filePath)
                  .clickImport ()
                  .waitTillJobIsFinished(10);

          if(!dataManagementPage.isJobCompletedSuccessFully()) {
              if(dataManagementPage.isJobFailed ())
                  fail(" : Job Import Failed for "+feedName+" from "+product+" product");
              else if(dataManagementPage.isJobCompletedWithErrors ())
                  fail(" : Job Import Completed with Errors for "+feedName+" from "+product+" product");
              else
                  fail(" : Failed to import "+feedName+" from "+product+" product");
          }
          

      }
      
      /**
      * @author Kalyan Komati
      * @stlc.requirements SM_UMEI_FILE_07_B
      * 
      * File located under src/test/resources/SM_UMEI_FILE_07_B has data format otherthan MM/DD/YYYY
      * This file should get previewed but validation and import should fail
      * 
      * Note : make sure, data format in feed cfg is having MM/DD/YYYY, otherwise update the format
      */
     @Author(name = QAResources.KALYAN)
     @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
     public void SM_UMEI_FILE_07_UMEI_previewShouldPassButValidateAndImportShouldFailForInValidDateFormat (TestDataExcel data) {

         LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
         login.logIn(data.getUserName(), data.getPassword());

         String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_07_B");
         String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_07_B/"+fileName;
         String feedName = getFeedNameFromFileName (fileName);

         final String product = HRSOFTCLOUD;

         DataManagementPage dataManagementPage = new CloudAdminPage ()
                 .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                 .selectProduct(product)
                 .selectConfigureFeedTab();

         if(!dataManagementPage.selectGivenFeedForConfigureFeed(feedName))
             fail( "No list item "+feedName+" found for Feed to select : ");
         dataManagementPage.openFieldMapping();

         if(!dataManagementPage.verifyDateFormat("MM/DD/YYYY"))
               fail("Invalid date format configured for feed, expected MM/DD/YYY");

         dataManagementPage.refresh ();

         dataManagementPage.selectProduct(product)
                 .selectImportTab();

           if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
               fail( "No list item "+feedName+" found for Data File to select : ");
           
         dataManagementPage.uploadFiles (filePath)
                 .clickPreview ();
           
           if(!(dataManagementPage.isFeedPreviewSuccess() && !dataManagementPage.previewHasError()))
               fail(" : Unable to preview "+feedName+" from "+product+" product");

         dataManagementPage.clickValidate ()
                 .waitTillJobIsFinished(10);

           if(dataManagementPage.isJobCompletedSuccessFully() || dataManagementPage.isJobCompletedWithErrors ())
              fail(" : Failed to show error for invalid date for  "+feedName+" from "+product+" product");

         dataManagementPage.closePopup().refresh();

         dataManagementPage.selectProduct(product)
                 .selectImportTab();

         if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
             fail("No list item "+feedName+" found for Data File to select : ");
           
         dataManagementPage.uploadFiles (filePath)
                 .clickImport ()
                 .waitTillJobIsFinished(10);

         if(dataManagementPage.isJobCompletedSuccessFully() || dataManagementPage.isJobCompletedWithErrors ())
               fail(" : Failed to show error for invalid date for "+feedName+" from "+product+" product");

         //TODO check for error message properly, not just Job Failed at validation and import

     }
     
     /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_10_A
     * 
     * File located under src/test/resources/SM_UMEI_FILE_10_A is an empty file
     * This file should fail for previewe, validation and import
     * 
     */
    @Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_10_UMEI_previewValidateAndImportShouldFailForEmptyFile (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_10_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_10_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        final String product = HRSOFTCLOUD;

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");
          
        dataManagementPage.uploadFiles (filePath)
                .clickPreview ();
          
        if(!(dataManagementPage.isFeedPreviewEmpty() && !dataManagementPage.previewHasError()))
              fail(" : Failed to show empty preview for empty file for  "+feedName+" from "+product+" product");

        dataManagementPage.clickValidate ();

        if(!dataManagementPage.isErrorPopupShown("Invalid file format OR faulty records in the file."))
              fail(" : Failed to show error for empty file for  "+feedName+" from "+product+" product");

        dataManagementPage.closePopup().refresh();

        dataManagementPage.selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail( "No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles (filePath)
                .clickImport ();

        if(!dataManagementPage.isErrorPopupShown("Invalid file format OR faulty records in the file."))
              fail(" : Failed to show error for empty file for  "+feedName+" from "+product+" product");
        
    }
    
    /**
    * @author Kalyan Komati
    * @stlc.requirements SM_UMEI_FILE_10_B
    * 
    * File located under src/test/resources/SM_UMEI_FILE_10_B contains only header
    * This file should fail for previewe, validation and import
    * 
    */
   @Author(name = QAResources.KALYAN)
   @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
   public void SM_UMEI_FILE_10_UMEI_previewValidateAndImportShouldFailForFileWithHeaderOnly (TestDataExcel data) {
       LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
       login.logIn(data.getUserName(), data.getPassword());

       String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_10_B");
       String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_10_B/"+fileName;
       String feedName = getFeedNameFromFileName (fileName);

       final String product = HRSOFTCLOUD;

       DataManagementPage dataManagementPage = new CloudAdminPage ()
               .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
               .selectProduct(product)
               .selectImportTab();

       if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
           fail("No list item "+feedName+" found for Data File to select : ");
       dataManagementPage.uploadFiles (filePath)
               .clickPreview ();
         
       if(!(dataManagementPage.isFeedPreviewSuccess() && !dataManagementPage.previewHasError()))
             fail(" : Failed to show error for empty file for  "+feedName+" from "+product+" product");
         if(!(dataManagementPage.isPreviewHeader("ERROR") && dataManagementPage.isPreviewFirstCellContains("Empty File Recieved / No Records Found")))
             fail(" : Failed to show error for empty file for  "+feedName+" from "+product+" product");

       dataManagementPage.clickValidate ()
               .waitTillJobIsFinished(10);
       if(!(dataManagementPage.isJobFailed() && "Empty File Recieved / No Records Found".equalsIgnoreCase (dataManagementPage.getJobStepLogMessage("File Format Validation"))))
             fail(" : Failed to show error for empty file for  "+feedName+" from "+product+" product");

       dataManagementPage.closePopup().refresh();

       dataManagementPage.selectProduct(product)
               .selectImportTab();

       if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
           fail( "No list item "+feedName+" found for Data File to select : ");

       dataManagementPage.uploadFiles (filePath)
               .clickImport ()
               .waitTillJobIsFinished(10);

         if(!(dataManagementPage.isJobFailed() && "Empty File Recieved / No Records Found".equalsIgnoreCase (dataManagementPage.getJobStepLogMessage("File Format Validation"))))
             fail(" : Failed to show error for empty file for  "+feedName+" from "+product+" product");

   }
   
   /**
   * @author Kalyan Komati
   * @stlc.requirements SM_UMEI_FILE_11
   * 
   * File located under src/test/resources/SM_UMEI_FILE_11_A should fail for non utf-8 file
   * This file should fail for previewe, validation and import
   * 
   */
  @Author(name = QAResources.KALYAN)
  @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
  public void SM_UMEI_FILE_11_UMEI_previewValidateAndImportShouldFailForNonUTF8File (TestDataExcel data) {

      LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
      login.logIn(data.getUserName(), data.getPassword());
      String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_11_A");
      String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_11_A/"+fileName;
      String feedName = getFeedNameFromFileName (fileName);

      final String product = HRSOFTCLOUD;
      DataManagementPage dataManagementPage = new CloudAdminPage ()
              .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
              .selectProduct(product)
              .selectImportTab();

      if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
          fail("No list item "+feedName+" found for Data File to select : ");
        
      dataManagementPage.uploadFiles (filePath)
              .clickPreview ();
        
      if(!(dataManagementPage.isFeedPreviewSuccess() && !dataManagementPage.previewHasError()))
            fail(" : File not processed  "+feedName+" from "+product+" product");
        
      if(!(dataManagementPage.isPreviewHeader("ERROR") && dataManagementPage.isPreviewFirstCellContains("Uploaded file has non UTF-8 character(s) or file is not UTF-8 encoded, Please check file content")))
            fail(" : Failed to show error for non UTF-8 file for  "+feedName+" from "+product+" product");

      dataManagementPage.clickValidate ()
              .waitTillJobIsFinished(10);
        if(!(dataManagementPage.isJobFailed() && "Uploaded file has non UTF-8 character(s) or file is not UTF-8 encoded, Please check file content".equalsIgnoreCase (dataManagementPage.getJobStepLogMessage("UTF-8 Encoding Validation"))))
            fail(" : Failed to show error for non UTF-8 file for  "+feedName+" from "+product+" product");

      dataManagementPage.closePopup().refresh();

      dataManagementPage.selectProduct(product)
              .selectImportTab();

      if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
          fail("No list item "+feedName+" found for Data File to select : ");

      dataManagementPage.uploadFiles (filePath)
                .clickImport ()
                .waitTillJobIsFinished(10);

      if(!(dataManagementPage.isJobFailed() && "Uploaded file has non UTF-8 character(s) or file is not UTF-8 encoded, Please check file content".equalsIgnoreCase (dataManagementPage.getJobStepLogMessage("UTF-8 Encoding Validation"))))
            fail(" : Failed to show error for non UTF-8 file for  "+feedName+" from "+product+" product");
      
      }
      
      /**
      * @author Kalyan Komati
      * @stlc.requirements SM_UMEI_FILE_12_A
      * 
      * File located under src/test/resources/SM_UMEI_FILE_12_A contains mismatching header
      * This file should fail for previewe, validation and import
      * 
      */
  	 @Author(name = QAResources.KALYAN)
     @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
     public void SM_UMEI_FILE_12_UMEI_previewValidateAndImportShouldFailForFileWithHeaderColumnMismatch (TestDataExcel data) {

         LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
         login.logIn(data.getUserName(), data.getPassword());
     
     String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_12_A");
     String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_12_A/"+fileName;
     String feedName = getFeedNameFromFileName (fileName);

     final String product = HRSOFTCLOUD;
         DataManagementPage dataManagementPage = new CloudAdminPage ()
                 .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                 .selectProduct(product)
                 .selectImportTab();

       if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
           fail("No list item "+feedName+" found for Data File to select : ");
       
       dataManagementPage.uploadFiles (filePath)
                 .clickPreview ();
       
       if(!(dataManagementPage.isFeedPreviewSuccess() && dataManagementPage.previewHasError()))
           fail(" : Failed to show header mismatch error for file "+feedName+" from "+product+" product");

         dataManagementPage.clickValidate ()
               .waitTillJobIsFinished(10);
       if(!(dataManagementPage.isJobFailed() && "Header Validation Failed".equalsIgnoreCase (dataManagementPage.getJobStepLogMessage("File Format Validation"))))
           fail(" : Failed to show header mismatch error for file "+feedName+" from "+product+" product");

         dataManagementPage.closePopup().refresh();

         dataManagementPage.selectProduct(product)
                 .selectImportTab();

       if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
           fail("No list item "+feedName+" found for Data File to select : ");
       
       dataManagementPage.uploadFiles (filePath)
                 .clickImport ()
                 .waitTillJobIsFinished(10);

       if(!(dataManagementPage.isJobFailed() && "Header Validation Failed".equalsIgnoreCase (dataManagementPage.getJobStepLogMessage("File Format Validation"))))
           fail(" : Failed to show header mismatch error for file "+feedName+" from "+product+" product");
     

     }

     /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_13_A
     * 
     * File located under src/test/resources/SM_UMEI_FILE_13_A has file and with sub folder that contains invalid filename
     * This file should fail for previewe, validation and import
     * 
     */
  	@Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_13_UMEI_previewValidateAndImportShouldFailForFileNameMismatch (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
    String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_13_A");
    String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_13_A/"+fileName;
    String feedName = getFeedNameFromFileName (fileName);

    fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_13_A/incorrectFileName");
    filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_13_A/incorrectFileName/"+fileName;

    final String product = HRSOFTCLOUD;
        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

      if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
          fail("No list item "+feedName+" found for Data File to select : ");
      
      dataManagementPage.uploadFiles (filePath)
              .clickPreview ();
      
      if(!(dataManagementPage.isErrorPopupShown ("Data Feed and File name sould be same.")))
          fail(" : Failed to show Name mismatch error file for  "+feedName+" from "+product+" product");

        dataManagementPage.clickOkButtonOnPopup()
                .clickValidate ();
      
      if(!(dataManagementPage.isErrorPopupShown ("Data Feed and File name sould be same.")))
          fail(" : Failed to show Name mismatch error file for  "+feedName+" from "+product+" product");

        dataManagementPage.clickOkButtonOnPopup()
                .clickImport ();
      
      if(!(dataManagementPage.isErrorPopupShown ("Data Feed and File name sould be same."))) {
          fail(" : Failed to show Name mismatch error file for  " + feedName + " from " + product + " product");
      }

        dataManagementPage.clickOkButtonOnPopup();
    }
    
    /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_14
     */ 
  	@Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_cfg","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_14_UMEI_feedConfigurationShouldBeEditable (TestDataExcel data) {

        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        final String product = COMPENSATION;
        String feedName = ESP_FEED_NAME_FOR_FEED_TEST;

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectConfigureFeedTab ();

        if(!dataManagementPage.selectGivenFeedForConfigureFeed (feedName))
            fail( "No list item "+feedName+" found for Data File to select : ");

        String existingDelimiter = dataManagementPage.getSelectedDelimiter();
        String newDelimiter ;
        if(",".equalsIgnoreCase (existingDelimiter)) {
            dataManagementPage.changeDelimiterTo (";").refresh ();
            newDelimiter = ";";
        }
        else{
            dataManagementPage.changeDelimiterTo (",").refresh ();
            newDelimiter = ",";
        }

        dataManagementPage.selectProduct(product)
                .selectConfigureFeedTab ();

        if(!dataManagementPage.selectGivenFeedForConfigureFeed (feedName))
            fail( "No list item "+feedName+" found for Data File to select : ");

        if(!newDelimiter.equalsIgnoreCase (dataManagementPage.getSelectedDelimiter()))
            fail( "Unable to save Feed Delimiter");

        dataManagementPage.changeDelimiterTo(existingDelimiter).refresh ();

        dataManagementPage.selectProduct(product)
                .selectConfigureFeedTab ();

        if(!dataManagementPage.selectGivenFeedForConfigureFeed (feedName))
            fail( "No list item "+feedName+" found for Data File to select : ");


        if(!existingDelimiter.equalsIgnoreCase (dataManagementPage.getSelectedDelimiter()))
            fail( "Unable to revert changes for delimiter");
    } 
    
    /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_15
     */ 
  	@Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_cfg","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_15_UMEI_feedFieldMappingShouldBeEditable (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        final String product = COMPENSATION;
    
        String feedName = ESP_FEED_NAME_FOR_FEED_TEST;

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectConfigureFeedTab ();
        if(!dataManagementPage.selectGivenFeedForConfigureFeed (feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.selectFeedFieldMappingTab ();

        String targetFeedFieldName = dataManagementPage.getFirstFeedFieldNameByDataType("Number");
        String existingFormat = dataManagementPage.getFormatOfFeedField(targetFeedFieldName);
        String newFormat = "";
        dataManagementPage.editFirstFieldMappingFromGrid ();
        
        if("#.###".equals(existingFormat))
        {
            dataManagementPage.changeFormatTo("#.####").refresh ();
            newFormat = "#.####";
        }
        else
        {
            dataManagementPage.changeFormatTo("#.###").refresh ();
            newFormat = "#.###";
        }

        dataManagementPage.selectProduct(product)
                .selectConfigureFeedTab ();

        if(!dataManagementPage.selectGivenFeedForConfigureFeed (feedName))
            fail("No list item "+feedName+" found for Feed to select : ");


        dataManagementPage.selectFeedFieldMappingTab ();
        
        if(!newFormat.equalsIgnoreCase (dataManagementPage.getFormatOfFeedField(targetFeedFieldName)))
            fail( "Unable to save Feed Field Format");

    } 
    
    /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_16
     */
  	@Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_cfg","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_16_UMEI_feedValidationsShouldBeEditable (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
    
        final String product = COMPENSATION;
        String feedName = ESP_FEED_NAME_FOR_FEED_TEST;

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectConfigureFeedTab ();
        if(!dataManagementPage.selectGivenFeedForConfigureFeed (feedName))
            fail("No list item "+feedName+" found for Feed to select : ");
        dataManagementPage.selectFeedFieldMappingTab ();

        String targetFeedFieldName = dataManagementPage.getFirstFeedFieldNameByDataType("String");

        dataManagementPage.selectFeedValidationsTab ();
    
        boolean lastNameValidationExists = dataManagementPage.isValidationExists(targetFeedFieldName,"MANDATORY");
        if(!lastNameValidationExists)
        {
            dataManagementPage.addValidation(targetFeedFieldName,"MANDATORY");
        }

        boolean lastNameValidationEnabled = dataManagementPage.isValidationEnabled(targetFeedFieldName,"MANDATORY");
        if(!lastNameValidationEnabled)
            dataManagementPage.enableValidation(targetFeedFieldName,"MANDATORY");
        else
            dataManagementPage.disableValidation(targetFeedFieldName,"MANDATORY");
    

        assertTrue(lastNameValidationEnabled != dataManagementPage.isValidationEnabled(targetFeedFieldName,"MANDATORY"),"Unable to enable / disable for Last_Name with Mandatory validation");

        //reset validation enable or disabled value
        if(lastNameValidationEnabled)
            dataManagementPage.enableValidation(targetFeedFieldName,"MANDATORY");
        else
            dataManagementPage.disableValidation(targetFeedFieldName,"MANDATORY");

        assertTrue(lastNameValidationEnabled == dataManagementPage.isValidationEnabled(targetFeedFieldName,"MANDATORY"),"Unable to enable / disable for Last_Name with Mandatory validation");

        if(!lastNameValidationExists) // if validation is cretated by test script, delete the validation
            dataManagementPage.removeValidation(targetFeedFieldName,"MANDATORY");

        assertTrue(lastNameValidationExists == dataManagementPage.isValidationExists(targetFeedFieldName,"MANDATORY"));

    } 
    
    /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_17
     */ 
  	@Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_cfg","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_17_UMEI_previewShouldBeSuccessForIncrementalMode (TestDataExcel data) {

        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_17_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_17_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        //String product = "HRSOFTCLOUD";

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail( "No list item "+feedName+" found for Data File to select : ");
        
        dataManagementPage.uploadFiles (filePath)
                .clickPreview ();
        
        if(!(dataManagementPage.isFeedPreviewSuccess() && !dataManagementPage.previewHasError()))
            fail( " : Unable to preview "+feedName+" from "+product+" product");
    } 

    /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_18
     */
    @Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_cfg","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_18_UMEI_validateShouldBeSuccessForIncrementalMode (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_18_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_18_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        //String product = "HRSOFTCLOUD";

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles (filePath)
                .clickValidate ()
                .waitTillJobIsFinished (10);

        if(!dataManagementPage.isJobCompletedSuccessFully()) {
            if(dataManagementPage.isJobFailed ())
                fail(" : Validate Failed for "+feedName+" from "+product+" product");
            else if(dataManagementPage.isJobCompletedWithErrors ())
                fail(" : Validate Completed with Errors for "+feedName+" from "+product+" product");
            else
                fail(" : Failed to validate "+feedName+" from "+product+" product");
        }
    } 
    /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_19
     */ 
    @Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_cfg","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_19_UMEI_validateShouldFailForPartialModeFileWithNewUserError (TestDataExcel data) {
        
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_19_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_19_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        //String product = "HRSOFTCLOUD";

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail( "No list item "+feedName+" found for Data File to select : ");

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Mode","Partial Load"))
            fail( "No list item Partial Load found for Mode to select : ");

        dataManagementPage.uploadFiles (filePath)
                .clickValidate ()
                .waitTillJobIsFinished (10)
                .openDetailedLogs();

        if(!(dataManagementPage.isJobFailed() && "New User found while importing Partial File".equalsIgnoreCase (dataManagementPage.getJobDetailedStepLogMessage("Data Validations Step")))) {
            fail(" : Failed to show new user found error for file "+feedName+" from "+product+" product");
        }

    }

    /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_52
     */ 
    @Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_cfg","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_52_UMEI_importShouldContinueForPartialModeFileWithNewUserError (TestDataExcel data) {

        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_52_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_52_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        //String product = "HRSOFTCLOUD";

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Mode","Partial Load"))
            fail("No list item Partial Load found for Mode to select : ");
        
        dataManagementPage.uploadFiles (filePath)
                .clickImport ()
                .clickConfirmInPopup()
                .waitTillJobIsFinished (10);

        if(!dataManagementPage.isJobCompletedWithErrors()) {
            if(dataManagementPage.isJobFailed ())
                fail(" : Import Failed for "+feedName+" from "+product+" product");
            else if(dataManagementPage.isJobCompletedSuccessFully ())
                fail(" : Import Completed successfully for "+feedName+" from "+product+" product");
            else
                fail(" : Failed to validate "+feedName+" from "+product+" product");
        }
    }

    /**
     * @author Kalyan Komati
     * @stlc.requirements SM_UMEI_FILE_20
     */
    @Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_cfg","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_20_UMEI_stepLogsShouldAppearForPreviouslyRunFiles (TestDataExcel data) {

        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;
        String feedName = CDF_FEED_NAME_FOR_FEED_TEST;

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectStatusReportsTab()
                .searchStatusReports(feedName);

        if(!(dataManagementPage.getJobExecutionCount() > 0))
            fail("No job logs found");

        dataManagementPage.showStepLogs();

        if(!(dataManagementPage.getStepLogCount() > 0))
            fail("No step log found");


    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_23_UMEI_validateShouldFailForDuplicateUsers (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_23_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_23_A/"+fileName;
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
        
        if(dataManagementPage.isJobCompletedWithErrors ()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Duplicate Rows");
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" Verified Duplicate Users Present in "+feedName+" from "+product+" product");
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
            fail(" Verified Duplicate Users Present in "+feedName+" from "+product+" product");
        }
        else
            fail(" Failed to Test this Case "+feedName+" from "+product+" product");
    }

    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_51_UMEI_validateShouldFailForFILTERPOOLNAMEFilter (TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_51_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_51_A/"+fileName;
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
        
        if(dataManagementPage.isJobCompletedWithErrors ()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Filter Pool Name check failed");
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" Mandatory Filter Pool Name check failed in "+feedName+" from "+product+" product");
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
        	softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
        	softAssert.fail(" Failed to Test this Case "+feedName+" from "+product+" product");
      //Import
        dataManagementPage.closePopup()
                          .closePopup()
                          .clickCancel()
                          .uploadFiles(filePath)
                          .clickImport()
                          .waitTillJobIsFinished(10);
        if(dataManagementPage.isJobCompletedWithErrors ()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Filter Pool Name check failed");
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
        	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" Mandatory Filter Pool Name check failed in "+feedName+" from "+product+" product");
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
        	softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
        	softAssert.fail(" Failed to Test this Case "+feedName+" from "+product+" product");
        softAssert.assertAll();
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_50_UMEI_validateShouldFailForPartialModeNewUserError (TestDataExcel data) {

        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = COMPENSATION;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_50_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_50_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        
        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL())
                .setConfigurationForFileFeedTo(",", feedName,product, 1)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");
        
        doWait(1000);
        dataManagementPage.selectOptionFromDropdown ("MENU_335658533542049","Partial Load")
                          .uploadFiles (filePath)
                          .clickValidate ()
                          .waitTillJobIsFinished(10);
        
        if(dataManagementPage.isJobCompletedWithErrors ()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Emp ID check failed");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" EmpId=newUser doesn`t exists in the system in "+feedName+" from "+product+" product");
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
            fail(" Failed to Test this Case "+feedName+" from "+product+" product");
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_43_UMEI_validateShouldFailForUserRoleFileInvalidRole (TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_43_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_43_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        
        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        //Validate
        doWait(1000);
        dataManagementPage.uploadFiles (filePath)
                          .clickValidate ()
                          .waitTillJobIsFinished(10);
        
        if(dataManagementPage.isJobCompletedWithErrors ()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","User Role supplied are not valid");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" User Role supplied are not valid in "+feedName+" from "+product+" product");
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
        	softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
            softAssert.fail(" Failed to Test this Case "+feedName+" from "+product+" product");
        //Import
        dataManagementPage.closePopup()
                          .closePopup()
                          .clickCancel()
                          .uploadFiles(filePath)
                          .clickImport()
                          .waitTillJobIsFinished(10);
        if(dataManagementPage.isJobCompletedWithErrors ()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","User Role supplied are not valid");
			List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" User Role supplied are not valid in "+feedName+" from "+product+" product");
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
        	softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
            softAssert.fail(" Failed to Test this Case "+feedName+" from "+product+" product");
        softAssert.assertAll();
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_46_UMEI_lastNameWithNoEmptyValuesShouldBeImportedSuccessfully (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        importFeedFileShouldBeSuccess ("SM_UMEI_FILE_46_A", HRSOFTCLOUD,data.getApplicationURL());
    }

    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_22_UMEI_importShouldBeSuccessForPartialMode ( TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        login.logIn (data.getUserName (), data.getPassword ());

        ManageUsersPage manageUsers = new CloudAdminPage()
                .clickManageUsers (data.getApplicationURL ());

        String userName = "edward.zhang";

        manageUsers.setUserNameInInputField(userName)
                .selectAllStatusUncheckedCheckbox()
                .selectHierarchyFromDropDown("HRSoft_2023")
                .clickOnSearchButton();

        //checking original emp status
        String originalEmpStatus = manageUsers.clickOnEditOfFirstRowInSearchResults().getEmpStatusOfUserFromEditPopUp();

        String modifiedEmpStatus;
        String folderNameForOriginal;
        String folderNameForModified;

        if(originalEmpStatus.equalsIgnoreCase("Terminated")) {
            folderNameForOriginal = "/SM_UMEI_FILE_22_T";
            modifiedEmpStatus = "Active";
            folderNameForModified = "/SM_UMEI_FILE_22_A";
        }
        else if (originalEmpStatus.equalsIgnoreCase("Leave of Absence")) {
            folderNameForOriginal = "/SM_UMEI_FILE_22_L";
            modifiedEmpStatus = "Active";
            folderNameForModified = "/SM_UMEI_FILE_22_A";
        }
        else {
            folderNameForOriginal = "/SM_UMEI_FILE_22_A";
            modifiedEmpStatus = "Terminated";
            folderNameForModified = "/SM_UMEI_FILE_22_T";
        }

        //modifying emp status by partial mode
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ folderNameForModified);
        String filePath = ResourcesFolderPath +ClientName+ folderNameForModified+"/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        //importing the file based on the original emp status
        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Mode","Partial Load"))
            fail("No list item Partial Load found for Mode to select : ");

        dataManagementPage.uploadFiles (filePath)
                .clickPreview()
                .clickImport ()
                .clickConfirmInPopup()
                .waitTillJobIsFinished (10);

        if(!dataManagementPage.isJobCompletedSuccessFully()) {

            if(dataManagementPage.isJobFailed ())
                softAssert.fail(" : Import Failed for "+feedName+" from "+product+" product");
            else if(dataManagementPage.isJobCompletedWithErrors ())
                softAssert.fail(" : Import Completed with Errors for "+feedName+" from "+product+" product");
            else
                softAssert.fail(" : Failed to import "+feedName+" from "+product+" product");

            dataManagementPage.printErrorMessagesFromLogsForMessageTypeError()
                    .printErrorMessagesFromLogsForStatusValueError()
                    .openDetailedLogs()
                    .filterErrorMessageInDetailsLogPopup()
                    .printErrorMessagesFromDetailedLogs();
        }

        //verifying if changes were made
        manageUsers = new CloudAdminPage().clickManageUsers (data.getApplicationURL ());
        manageUsers.setUserNameInInputField(userName)
                .selectAllStatusUncheckedCheckbox()
                .selectHierarchyFromDropDown("HRSoft_2023")
                .clickOnSearchButton();

        if (manageUsers.clickOnEditOfFirstRowInSearchResults().getEmpStatusOfUserFromEditPopUp().equalsIgnoreCase(modifiedEmpStatus)) {
            info(": Changes made by Partial Import were verfied successfully");
        } else
            softAssert.fail(": Changes made by Partial Import are not being reflected");

        //changing the emp status back to original
        fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ folderNameForOriginal);
        filePath = ResourcesFolderPath +ClientName+ folderNameForOriginal+"/"+fileName;
        feedName = getFeedNameFromFileName (fileName);
        dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Mode","Partial Load"))
            fail("No list item Partial Load found for Mode to select : ");

        dataManagementPage.uploadFiles (filePath)
                .clickPreview()
                .clickImport ()
                .clickConfirmInPopup()
                .waitTillJobIsFinished (10);

        if(!dataManagementPage.isJobCompletedSuccessFully()) {
            if(dataManagementPage.isJobFailed ())
                fail(" : Resetting Import Failed for "+feedName+" from "+product+" product");
            else if(dataManagementPage.isJobCompletedWithErrors ())
                fail(" : Resetting Import Completed with Errors for "+feedName+" from "+product+" product");
            else
                fail(" : Resetting Failed to import "+feedName+" from "+product+" product");

            dataManagementPage.printErrorMessagesFromLogsForMessageTypeError()
                    .printErrorMessagesFromLogsForStatusValueError()
                    .openDetailedLogs()
                    .filterErrorMessageInDetailsLogPopup()
                    .printErrorMessagesFromDetailedLogs();
        }
        //verifying if resetting was done
        manageUsers = new CloudAdminPage().clickManageUsers (data.getApplicationURL ());
        manageUsers.setUserNameInInputField(userName)
                .selectAllStatusUncheckedCheckbox()
                .selectHierarchyFromDropDown("HRSoft_2023")
                .clickOnSearchButton();
        if (manageUsers.clickOnEditOfFirstRowInSearchResults().getEmpStatusOfUserFromEditPopUp().equalsIgnoreCase(originalEmpStatus)) {
            info(": Resetting changes made by Partial Import were verfied successfully");
        } else
            fail(": Resetting changes made by Partial Import are not being reflected");
    }

    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_24
     */
    @Author (name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_24_UMEI_dataValidationShouldThrowParseError (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_24");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_24/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage()
                .clickDataManagement(data.getApplicationURL())
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickValidate()
                .waitTillJobIsFinished(10);

        if(!dataManagementPage.isJobCompletedWithErrors()) {
            if(dataManagementPage.isJobFailed ()) {
                dataManagementPage.printErrorMessagesFromLogsForMessageTypeError()
                        .printErrorMessagesFromLogsForStatusValueError()
                        .openDetailedLogs()
                        .filterErrorMessageInDetailsLogPopup()
                        .printErrorMessagesFromDetailedLogs();
                fail(" Job Failed instead of Completing with Errors for "+feedName+" from "+product+" product");
            }
            else if(dataManagementPage.isJobCompletedSuccessFully())
                fail(" Job Completed successfully instead of Completing with Errors for "+feedName+" from "+product+" product");
        }
        else {
            info(" Job Completed with Errors for " + feedName + " from " + product + " product");
            dataManagementPage.printErrorMessagesFromLogsForMessageTypeError()
                    .printErrorMessagesFromLogsForStatusValueError();

            dataManagementPage.openDetailedLogs();
            if("FTE_MONTHS_PER_YEAR|Couldn't parse field [Number] with value [ten], format [#.####] on data row [1].".equalsIgnoreCase(dataManagementPage.getJobDetailedStepLogMessage("Data Validations Step", 1))) {
                info(" Parse Error encountered for " + feedName + " from " + product + " product");
                dataManagementPage.filterErrorMessageInDetailsLogPopup()
                        .printErrorMessagesFromDetailedLogs();
            }
            else
                fail(" Parse Error did not encounter for " + feedName + " from " + product + " product");
        }
    }

    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_27
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_27_UMEI_validateShouldNotFail(TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());


        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_27");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_27/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickPreview()
                .clickValidate()
                .waitTillJobIsFinished(10);

        if(dataManagementPage.isJobFailed())
            fail(" Job Failed for "+feedName+" from "+product+" product");
        else
            info(" Validate job did not finish with fail status");
    }

    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_28
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_28_UMEI_importShouldNotFail(TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());


        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_28");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_28/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickPreview()
                .clickImport()
                .waitTillJobIsFinished(10);

        if(dataManagementPage.isJobFailed())
            fail(" Job Failed for "+feedName+" from "+product+" product");
        else
            info(" Import job did not finish with fail status");
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_25_UMEI_validateShouldFailForTopManagerMissing (TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_25_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_25_A/"+fileName;
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
        
        if(dataManagementPage.isJobCompletedWithErrors ()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","No Top Level Manager found in File and System");
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" No Top Level Manager found in File and System in "+feedName+" from "+product+" product");
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
            softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
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
        if(dataManagementPage.isJobFailed ()) {
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","No Top Level Manager found in File and System");
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" No Top Level Manager found in File and System in "+feedName+" from "+product+" product");
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
            softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
            softAssert.fail(" Failed to Import "+feedName+" from "+product+" product");
        softAssert.assertAll();
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_26_UMEI_validateShouldFailForMANDATORYFILTERNAME_Filter (TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_26_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_26_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        
        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

      //Validate
        dataManagementPage.uploadFiles (filePath)
                          .clickValidate ()
                          .waitTillJobIsFinished(10);
        
        if(dataManagementPage.isJobFailed()) {
            info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
                              .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Mandatory Filter Pool Name");
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" Mandatory Filter Name check failed "+feedName+" from "+product+" product");
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
            softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
            softAssert.fail(" Failed to Validate "+feedName+" from "+product+" product");
        
        //Import
        dataManagementPage.closePopup()
                          .closePopup()
                          .clickCancel()
                          .uploadFiles(filePath)
                          .clickImport()
                          .waitTillJobIsFinished(10);
        if(dataManagementPage.isJobFailed()) {
            info(" Job Failed for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
                              .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Mandatory Filter Pool Name");
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" Mandatory Filter Name check failed "+feedName+" from "+product+" product");
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
            softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
            softAssert.fail(" Failed to Import "+feedName+" from "+product+" product");
        softAssert.assertAll();
        
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_45_UMEI_periodFactorFormatValuesShouldBeImportedSuccessfully (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        importFeedFileShouldBeSuccess ("SM_UMEI_FILE_45_A", HRSOFTCLOUD,data.getApplicationURL());
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_44_defineFeedPipeDelimeterChangeShouldBeImportedSuccessfully (TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_44_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_44_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        
        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed ("|", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
        	softAssert.fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles (filePath)
                .clickImport ()
                .waitTillJobIsFinished(10);
        
        if(!dataManagementPage.isJobCompletedSuccessFully())
        {
        	if(dataManagementPage.isJobFailed ()) {
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
	            softAssert.fail(" : Job Failed for "+feedName+" from "+product+" product");
            }
            else if(dataManagementPage.isJobCompletedWithErrors ()) {
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
	            softAssert.fail(" : Job Completed with Errors for "+feedName+" from "+product+" product");
            }
            else
            	softAssert.fail(" : Failed to import "+feedName+" from "+product+" product");
        }
        dataManagementPage
                .closePopup ()
                .clickConfigureFeed ()
                .chooseFeedsInConfigureFeed (feedName)
                .clickDefineFeed ()
                .selectOptionFromDropdown ("MENU_307775497938041", "Comma(,)");
        doWait(2000);
        softAssert.assertAll();
    }
    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_29
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_29_UMEI_previewAndImportShouldWorkIfValidationErrorIsLessThanThresholdElseAbort(TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_29");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_29/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();


        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickPreview()
                .clickImport()
                .waitTillJobIsFinished(10);

        String errorMessage = dataManagementPage.getJobStepLogMessage("Data Validations Step");

        //if validation error count is equal to or more than the max error threshold
        if(dataManagementPage.isJobFailed() && errorMessage != null && errorMessage.contains("exceeded the max error threshold"))
            info(" : Job Aborted because the VALIDATION ERROR COUNT exceeded the MAX ERROR THRESHOLD for " + feedName + " from " + product + " product");
        else if(dataManagementPage.isJobCompletedSuccessFully())
            info(" Job Completed Successfully for "+feedName+" from "+product+" product");
        else if(dataManagementPage.isJobCompletedWithErrors())
            info(" Job Completed with Errors for "+feedName+" from "+product+" product");
        else
            fail(" Job Failed for "+feedName+" from "+product+" product");

    }

    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_30
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_30_UMEI_legacyFilesHeadersShouldBeReplacedWithFeedConfigOnImporting ( TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());


        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_30");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_30/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        if(dataManagementPage.uploadFiles(filePath)
                .clickPreview()
                .isPreviewHeaderNotLegacyHeader("USER_NAME")) {
            dataManagementPage.clickImport().waitTillJobIsFinished(10);

            if(!dataManagementPage.isJobCompletedSuccessFully()) {
                if(dataManagementPage.isJobCompletedWithErrors())
                    info(" Job Completed with Errors for "+feedName+" from "+product+" product");
                else if (dataManagementPage.isJobFailed()) {
                    info(" Job Failed for "+feedName+" from "+product+" product");
                }
            }
            else
                info("Job Completed Successfully for " +feedName+" from "+product+" product");
        }
        else
            fail(" Failed to replace legacy file header with the feed configuration header");
    }
    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_36
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_36_UMEI_feedFileShouldThrowErrorForMissingRowData (TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_36");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_36/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        //preview
        dataManagementPage.uploadFiles(filePath)
                        .clickPreview();

        String errorMessage = dataManagementPage.getPreviewTitleErrorMessage();
        if(errorMessage != null && errorMessage.contains("record(s) have column count mismatch"))
            info(" Preview Title says records have COLUMN COUNT MISMATCH");
        else
            softAssert.fail(" Preview Title did not THROW ERROR for COLUMN MISMATCH");

        //validate
        dataManagementPage.clickValidate()
                .waitTillJobIsFinished(10);

        errorMessage = dataManagementPage.getJobStepLogMessage("File Format Validation");

        if(dataManagementPage.isJobFailed() && errorMessage != null && errorMessage.contains("Row Data Validation Failed for") && errorMessage.contains("records"))
            info("Validation Job Aborted because ROW DATA VALIDATION FAILED for ## records for " + feedName + " from " + product + " product");
        else if(dataManagementPage.isJobCompletedSuccessFully())
            softAssert.fail("Validation Job Completed Successfully for "+feedName+" from "+product+" product");
        else if(dataManagementPage.isJobCompletedWithErrors())
            softAssert.fail("Validation Job Completed with Errors for "+feedName+" from "+product+" product");
        else
            softAssert.fail("Validation Job Failed for "+feedName+" from "+product+" product");

        dataManagementPage.closePopup().clickCancel();

        //import
        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        errorMessage = dataManagementPage.getJobStepLogMessage("File Format Validation");

        if(dataManagementPage.isJobFailed() && errorMessage != null && errorMessage.contains("Row Data Validation Failed for") && errorMessage.contains("records"))
            info("Import Job Aborted because ROW DATA VALIDATION FAILED for ## records for " + feedName + " from " + product + " product");
        else if(dataManagementPage.isJobCompletedSuccessFully())
            softAssert.fail("Import Job Completed Successfully for "+feedName+" from "+product+" product");
        else if(dataManagementPage.isJobCompletedWithErrors())
            softAssert.fail("Import Job Completed with Errors for "+feedName+" from "+product+" product");
        else
            softAssert.fail("Import Job Failed for "+feedName+" from "+product+" product");

        softAssert.assertAll();
    }

    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_42_UMEI_userRoleFileShouldBeImportedSuccessfully (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        importFeedFileShouldBeSuccess ("SM_UMEI_FILE_42_A", HRSOFTCLOUD,data.getApplicationURL());
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_33_UMEI_validateShouldFailForMultipleTopLevelManagers (TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_33_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_33_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        
        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

      //Validate
        dataManagementPage.uploadFiles (filePath)
                          .clickValidate ()
                          .waitTillJobIsFinished(10);
        
        if(dataManagementPage.isJobCompletedWithErrors ()) {
            info(" Job Completed With Error for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            				  .filterErrorMessageInDetailsLogPopup()
                              .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Multi Top Level Managers for hierarchy");
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            for (String text : filteroptionnew) {
        		info("Details Logs Error : "+text);
        	}
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" Multi Top Level Managers for hierarchy "+feedName+" from "+product+" product");
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
            softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
            softAssert.fail(" Failed to Validate "+feedName+" from "+product+" product");
        
        //Import
        dataManagementPage.closePopup()
                          .closePopup()
                          .clickCancel()
                          .uploadFiles(filePath)
                          .clickImport()
                          .waitTillJobIsFinished(10);
        if(dataManagementPage.isJobCompletedWithErrors ()) {
            info(" Job Completed With Error for "+feedName+" from "+product+" product");
            dataManagementPage.openDetailedLogs ()
            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Multi Top Level Managers for hierarchy");
            List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
            List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
            info("Checking DB Error count : "+matchedDBError.size());
        	for (String text : matchedDBError) {
        		info("DB Error : "+text);
        	}
            info(" Multi Top Level Managers for hierarchy "+feedName+" from "+product+" product");
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
            softAssert.fail(" Job Failed in "+feedName+" from "+product+" product");
        }
        else
            softAssert.fail(" Failed to Import "+feedName+" from "+product+" product");
        softAssert.assertAll();
        
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_35_importShouldFailForAbortImportFiveOrMoreHierarchyFound (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_35_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_35_A/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);
        
        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles (filePath)
                .clickImport ()
                .waitTillJobIsFinished(10);
        
        if(dataManagementPage.isJobFailed ())
        {
            info(" Job Failed for "+feedName+" from "+product+" product");
          dataManagementPage.openDetailedLogs ()
                            .rowValuePresentInColumnofGrid (3,"GRID_1516768038509_copy_5df68017","Five or More Hierarchy found in File" + 
                                    "");
          List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
          List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
          info("Checking DB Error count : "+matchedDBError.size());
	      	for (String text : matchedDBError) {
	      		info("DB Error : "+text);
	      	}
          info(" Abort Import. Five or More Hierarchy found in File "+feedName+" from "+product+" product");
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
          fail(" Failed to Import "+feedName+" from "+product+" product");
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_40_userAbleToSeeDetailsLogGridInStatusReports (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL())
                .selectProduct(product)
                .selectStatusReportsTab()
                .searchStatusReports (CDF_FEED_NAME_FOR_FEED_TEST)
                .showDetailLogs ();
    }
    
    @Author(name = QAResources.VIVEK)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_41_editUmeiAttribute (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        UmeiAttributes umeiconfigure          = new UmeiAttributes ();
        UmeiAttributes dataManagementPage = new CloudAdminPage ()
                .clickUmeiAttribute (data.getApplicationURL());
        umeiconfigure.clickOnInlineEditOfUmeiAttributeGrid("use_consolidated_cdf");
        ConfigureAndManageAttributes configure  = new ConfigureAndManageAttributes ();
        String orginalvalue = configure.getOriginalValueOfInputboxGridInlineEdit ("fldattribute_value");
        System.out.println (orginalvalue);
        String dummyvalue = "true";
        try {
        configure.passNewValueInputBoxGridInlineEdit ("fldattribute_value",dummyvalue)
                 .clickSaveAndCloseEditRecordPopupOfGrid ();
        umeiconfigure.verifyAttributeValuePresentInUmeiAttribute ("use_consolidated_cdf", dummyvalue);
        }finally {
        umeiconfigure.clickOnInlineEditOfUmeiAttributeGrid("use_consolidated_cdf");
        configure.passNewValueInputBoxGridInlineEdit ("fldattribute_value",orginalvalue)
                 .clickSaveAndCloseEditRecordPopupOfGrid ();
        umeiconfigure.verifyAttributeValuePresentInUmeiAttribute ("use_consolidated_cdf", orginalvalue);
        }
    }
    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_37
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_37_UMEI_feedFileShouldThrowErrorForMaxColumnPercentThreshold (TestDataExcel data) {
        SoftAssert softAssert = new SoftAssert();
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_37");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_37/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickValidate()
                .waitTillJobIsFinished(10);

        String errorMessage = dataManagementPage.getJobStepLogMessage("File Format Validation");

        //validate
        if(dataManagementPage.isJobFailed() && errorMessage != null
                && errorMessage.contains("Incremental data validation failed. The file mode was selected as \"Incremental\" and more than")
                && errorMessage.contains("of mapped columns are blank for")
                && errorMessage.contains("or more rows. Please check the file for empty fields or go back to previous screen and select mode as Partial"))
            info("Validation Job Aborted because FILE FORMAT VALIDATION FAILED for " + feedName + " from " + product + " product");
        else if(dataManagementPage.isJobCompletedSuccessFully())
            softAssert.fail("Validation Job Completed Successfully for "+feedName+" from "+product+" product");
        else if(dataManagementPage.isJobCompletedWithErrors())
            softAssert.fail("Validation Job Completed with Errors for "+feedName+" from "+product+" product");
        else
            softAssert.fail("Validation Job Failed for "+feedName+" from "+product+" product");

        dataManagementPage.closePopup().clickCancel();

        //import
        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        errorMessage = dataManagementPage.getJobStepLogMessage("File Format Validation");

        if(dataManagementPage.isJobFailed() && errorMessage != null
                && errorMessage.contains("Incremental data validation failed. The file mode was selected as \"Incremental\" and more than")
                && errorMessage.contains("of mapped columns are blank for")
                && errorMessage.contains("or more rows. Please check the file for empty fields or go back to previous screen and select mode as Partial"))
            info("Import Job Aborted because FILE FORMAT VALIDATION FAILED for " + feedName + " from " + product + " product");
        else if(dataManagementPage.isJobCompletedSuccessFully())
            softAssert.fail("Import Job Completed Successfully for "+feedName+" from "+product+" product");
        else if(dataManagementPage.isJobCompletedWithErrors())
            softAssert.fail("Import Job Completed with Errors for "+feedName+" from "+product+" product");
        else
            softAssert.fail("Import Job Failed for "+feedName+" from "+product+" product");

        softAssert.assertAll();
    }

    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_38
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_38_UMEI_previewShouldContainErrorRowsOnTop (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_38");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_38/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickPreview();

        String errorMessage = dataManagementPage.getPreviewTitleErrorMessage();

        System.out.println("NoOfErrorCountInPreview "+dataManagementPage.getNoOfErrorCountInPreview());
        System.out.println("NoOfErrorCountInPreview error message "+Integer.parseInt(errorMessage.split(" ", 2)[0]) );
        if (errorMessage != null) {
            if (!dataManagementPage.isFirstErrorRowOnTopInPreviewHighlightedInRed())
                fail(" ERRORS ENCOUNTERED in the file but the top row is NOT HIGHLIGHTED IN RED");
            else if(!(dataManagementPage.getNoOfErrorCountInPreview() > 0))
                fail(" ERRORS ENCOUNTERED in the file but the rows are NOT HIGHLIGHTED IN RED");
                //errors are there and count of errors in title is not equal to the count of highlighted rows
            else if (Integer.parseInt(errorMessage.split(" ", 2)[0]) != dataManagementPage.getNoOfErrorCountInPreview())
                fail(" ERRORS COUNT displayed in PREVIEW TITLE is NOT EQUAL to the number of HIGHLIGHTED ROWS");
            else
                info(" ERRORS ENCOUNTERED are displayed on top");
        } else {
            //errors are not there in the file
            fail(" The uploaded file for " +feedName+" from "+product+" product" + " does not contain any rows with column count mismatch");
        }
    }
    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_34
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_34_UMEI_importShouldBeAbortedForAllInvalidRecords (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_34");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_34/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);

        if(!dataManagementPage.isJobFailed()) {
            if(dataManagementPage.isJobCompletedWithErrors ())
                fail(" Job Completed with Errors instead of failing for "+feedName+" from "+product+" product");
            else if(dataManagementPage.isJobCompletedSuccessFully())
                fail(" Job Completed successfully instead of failing for "+feedName+" from "+product+" product");
        }
        else {
            info(" Job Aborted for " + feedName + " from " + product + " product");
            dataManagementPage.openDetailedLogs();
            if("FTE_MONTHS_PER_YEAR|Couldn't parse field [Number] with value [ten], format [#.####] on data row [1].".equalsIgnoreCase(dataManagementPage.getJobDetailedStepLogMessage("Data Validations Step")))
                info(" Invalid Rows! Parse Error encountered for " + feedName + " from " + product + " product");
            else
                fail(" Valid Rows! Parse Error did not encounter for " + feedName + " from " + product + " product");
        }
    }

    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_39
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_39_UMEI_importShouldBeSuccessAndItShouldFillDisplayName ( TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_39");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_39/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickPreview()
                .clickImport()
                .waitTillJobIsFinished(10);

        if(!dataManagementPage.isJobCompletedSuccessFully())
        {
            if(dataManagementPage.isJobFailed ())
                fail(" : Job Failed for "+feedName+" from "+product+" product");
            else if(dataManagementPage.isJobCompletedWithErrors ())
                fail(" : Job Completed with Errors for "+feedName+" from "+product+" product");
            else
                fail(" : Failed to import "+feedName+" from "+product+" product");
        }
    }

    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_31
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"ui","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_31_UMEI_productDropDownListShouldBeClientSpecific ( TestDataExcel data) throws SQLException {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL());

        UMEIDbHelper umeiDbHelper = new UMEIDbHelper();
        List<String> listFromUI = dataManagementPage.getProductListFromDropDown();
        List<String> listFromDB = umeiDbHelper.getProductListByCustGuid(CustGuid);

        Assertions.assertThat(listFromDB.size() == listFromUI.size() && listFromDB.containsAll(listFromUI))
                .as("Verifying ").withFailMessage(" Lists from UI and DB are DIFFERENT").isTrue();
    }
    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_32
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"ui","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_32_UMEI_dataFileDropDownListShouldBeProductSpecific ( TestDataExcel data) throws SQLException {

        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL());

        UMEIDbHelper umeiDbHelper = new UMEIDbHelper();
        List<String> listFromUI = dataManagementPage.getDataFileListFromDropDown();
        List<String> listFromDB = umeiDbHelper.getDataFileByCustGuidAndAppId(CustGuid, HRSOFTCLOUD);

        Assertions.assertThat(listFromDB.size() == listFromUI.size() && listFromDB.containsAll(listFromUI))
                .as("Verifying ").withFailMessage(" Lists from UI and DB are DIFFERENT").isTrue();

    }

    /**
     * @author Hera Aijaz
     * @stlc.requirements SM_UMEI_FILE_01
     */
    @Author(name = QAResources.HERA)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","positive"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_01_UMEI_validCommonDemographicFileShouldBePreviwedValidatedImportedSuccessfully (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());

        String product = HRSOFTCLOUD;

        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath+ClientName + "/SM_UMEI_FILE_01");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_01/"+fileName;
        String feedName = getFeedNameFromFileName (fileName);

        DataManagementPage dataManagementPage = new CloudAdminPage ()
                .clickDataManagement (data.getApplicationURL()).defaultConfigurationForFileFeed (",", feedName,product)
                .selectProduct(product)
                .selectImportTab();

        if(!dataManagementPage.selectGivenItemFromDropDownIfAny("Data File",feedName))
            fail("No list item "+feedName+" found for Data File to select : ");

        dataManagementPage.uploadFiles(filePath)
                .clickPreview();

        if(!(dataManagementPage.isFeedPreviewSuccess() && !dataManagementPage.previewHasError()))
            fail(" : Unable to preview "+feedName+" from "+product+" product");

        dataManagementPage.clickValidate ()
                .waitTillJobIsFinished(10);

        if(!dataManagementPage.isJobCompletedSuccessFully())
        {
            if(dataManagementPage.isJobFailed ())
                fail(" : Validation Job Failed for "+feedName+" from "+product+" product");
            else if(dataManagementPage.isJobCompletedWithErrors ())
                fail(" : Validation Job Completed with Errors for "+feedName+" from "+product+" product");
            else
                fail(" : Failed to validate "+feedName+" from "+product+" product");
        }

        dataManagementPage.closePopup()
                .clickImport ()
                .waitTillJobIsFinished(10);

        if(!dataManagementPage.isJobCompletedSuccessFully()) {
            if(dataManagementPage.isJobFailed ())
                fail(" : Job Import Failed for "+feedName+" from "+product+" product");
            else if(dataManagementPage.isJobCompletedWithErrors ())
                fail(" : Job Import Completed with Errors for "+feedName+" from "+product+" product");
            else
                fail(" : Failed to import "+feedName+" from "+product+" product");
        }
    }

    @Author(name = QAResources.KALYAN)
    @Test (groups = {TestGroups.SMOKE, ComponentGroups.UMEI,"feed_file","generic","negative"}, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_UMEI_FILE_53_UMEI_validateAndImportShouldFailForMaxThresholdError (TestDataExcel data) {
        LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
        login.logIn(data.getUserName(), data.getPassword());
        String product = HRSOFTCLOUD;
        String fileName = getAvailableFilePathFromFolder(ResourcesFolderPath +ClientName+ "/SM_UMEI_FILE_53_A");
        String filePath = ResourcesFolderPath+ClientName+"/SM_UMEI_FILE_53_A/"+fileName;
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

        String expectedErrorMessage = "Data validations Step for the feed file failed with errors! , total errors [0-9]+ has exceeded the max error threshold [0-9]+ and the file upload process was aborted.";
        if(dataManagementPage.isJobFailed ()) {
            info(" Job Failed for "+feedName+" from "+product+" product");
            String msg = dataManagementPage.getJobStepLogMessage ("Data Validations Step");
            if(Pattern.matches(expectedErrorMessage,msg))
                info("Expected error message : "+expectedErrorMessage+" found in "+feedName+" from "+product+" product");
            else
                fail(" : Failed to show max threshold error for "+feedName+" from "+product+" product");
            }
        else
            fail(" : Failed to abort the validate job with max threshold error for "+feedName+" from "+product+" product");
        //Import
        dataManagementPage.closePopup()
                .clickCancel()
                .uploadFiles(filePath)
                .clickImport()
                .waitTillJobIsFinished(10);
        if(dataManagementPage.isJobFailed ()) {
            info(" Job failed for "+feedName+" from "+product+" product");
            String msg = dataManagementPage.getJobStepLogMessage ("Data Validations Step");
            if(Pattern.matches(expectedErrorMessage,msg))
                info("Expected error message : "+expectedErrorMessage+" found in "+feedName+" from "+product+" product");
            else
                fail(" : Failed to show max threshold error for "+feedName+" from "+product+" product");
        }
        else
            fail(" : Failed to abort the validate job with max threshold error for "+feedName+" from "+product+" product");
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