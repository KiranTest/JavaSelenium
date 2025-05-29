package com.hrsoft.test.datamanagement;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static com.hrsoft.reports.ExtentLogger.info;

import java.util.List;

import org.testng.annotations.Test;
import static org.testng.Assert.fail;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.cloudadmin.CloudAdminPage;
import com.hrsoft.gui.cloudadmin.ConfigureAndManageAttributes;
import com.hrsoft.gui.cloudadmin.DataManagementPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

public class ApiFeedTestSuite extends WebBaseTest {
    private CloudAdminPage               cloudAdmin         = new CloudAdminPage ();
    private DataManagementPage           dataManagementPage = new DataManagementPage ();
    private ConfigureAndManageAttributes configure          = new ConfigureAndManageAttributes ();
    private static final String ClientName=Constants.custId;
    private static final String GENRIC_COMP_ULTI_EMP_SPARES = "COMPENSATION_"+ClientName.toUpperCase()+"_ULTI_EMP_SPARES";
    private static final String GENRIC_ADP_COMP_EMP_SPARES = "ADP_COMPENSATION_"+ClientName.toUpperCase()+"_EMP_SPARES";
    private static final String GENRIC_COMP_ULTI_PERF_RATING_EMP = "COMPENSATION_"+ClientName.toUpperCase()+"_ULTI_PERF_RATING_EMP";
    private static final String GENRIC_COMP_ULTI_PLATFORM_EMP_SPARES = "COMPENSATION_"+ClientName.toUpperCase()+"_ULTI_PLATFORM_EMP_SPARES";

    /*
     * Common Functionality for TestCases SMAPI13 to SMAPI16,SM_API_20, SM_API_22 to SM_API_24,
     * SM_API_28
     * SM_API_30
     */

    private void apiPreviewTest (String feed, String product,String url) {
    	cloudAdmin.clickDataManagement (url);
        doWait (500);
        String[] files = { feed };
        String[] products = { product };
        for (int i = 0; i < files.length; i++) {
            info ("API Feed : " + files[i] + " of Product : " + products[i]);
            dataManagementPage
                              .selectProduct (products[i])
                              .selectImportTab ()
                              .selectOptionFromDropdownAndCloneFeedNotAvailable ("MENU_335629169244049", files[i])
                              .clickPreview ()
                              .waitTillPreviewIsFinished (10);
            if (!dataManagementPage.isAPIFeedPreviewSuccess ()) {
                if (dataManagementPage.errorFileNotSelectedAfterPreviewClick ()) {
                    System.out.println ("API Feed : " + files[i] + " - Not Selected in Dropdown - Product : " + products[i] + " Unable to Preview ");
                    fail ("API Feed : " + files[i] + " - Not Selected in Dropdown - Product : " + products[i] + " Unable to Preview ");
                } else if (dataManagementPage.errorMappingAfterPreviewClick ()) {
                    System.out.println ("API Feed : " + files[i] + " - Feed Configuration Issue - Product : " + products[i] + " Unable to Preview ");
                    fail ("API Feed : " + files[i] + " - Feed Configuration Issue - Product : " + products[i] + " Unable to Preview ");
                } else if (dataManagementPage.isJobAbort ()) {
                    System.out.println ("API Feed : " + files[i] + " - Job Abort - Product : " + products[i] + " Unable to Preview ");
                    fail ("API Feed : " + files[i] + " - Job Abort - Product : " + products[i] + " Unable to Preview ");
                }
            } else {
                System.out.println ("API Feed : " + files[i] + " of Product : " + products[i] + " Preview Successfully");
                info ("API Feed : " + files[i] + " of Product : " + products[i] + " Preview Successfully");
                dataManagementPage.closePopup ();
            }
        }
    }

    /*
     * Common Functionality for TestCases SM_API_17 to SM_API_19, SM_API_21, SM_API_25 to SM_API_27,
     * SM_API_29, SM_API_31
     */

    private void apiImportTest (String feed, String product,String url) {
        cloudAdmin.clickDataManagement (url);

        doWait (500);
        String[] files = { feed };
        String[] products = { product };
        for (int i = 0; i < files.length; i++) {
            info ("API Feed : " + files[i] + " of Product : " + products[i]);
            dataManagementPage
                              .selectProduct (products[i])
                              .selectImportTab ()
                              .selectOptionFromDropdownAndCloneFeedNotAvailable ("MENU_335629169244049", files[i])
                              .clickImport ()
                              .waitTillJobIsFinished (10);
            if (!dataManagementPage.isJobCompletedSuccessFully ()) {
                if (dataManagementPage.isJobFailed ()) {
                	System.out.println (" Import Failed for " + files[i] + " from " + products[i] + " product");
            		dataManagementPage.openDetailedLogs ()
                	.filterErrorMessageInDetailsLogPopup();
                	List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
                	for (String text : filteroptionnew) {
                		info("Details Logs : "+text);
                	}
                	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
                	for (String text : matchedDBError) {
                		info("DB Error : "+text);
                	}
                    fail (" Import Failed for " + files[i] + " from " + products[i] + " product");
                } else if (dataManagementPage.isJobCompletedWithErrors ()) {
                    System.out.println (" Import Completed with Errors for " + files[i] + " from " + products[i] + " product");
            		dataManagementPage.openDetailedLogs ()
                	.filterErrorMessageInDetailsLogPopup();
                	List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
                	for (String text : filteroptionnew) {
                		info("Details Logs : "+text);
                	}
                	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
                	for (String text : matchedDBError) {
                		info("DB Error : "+text);
                	}
                    fail ("Import Completed with Errors for " + files[i] + " from " + products[i] + " product");
                } else {
                    System.out.println (" Failed to import " + files[i] + " from " + products[i] + " product");
                    fail (" Failed to import " + files[i] + " from " + products[i] + " product");
                }
            }
        }
    }
    
    private void changeHierarchyInAPIManagementAndApiImport(String partner,String hirarchy,String feed, String product,String url) {
    	cloudAdmin.clickAPIManagementPage (url)
        .clickAPITransformAttributes ()
        .clickRowForPartnerNameEditAPITransformAttributes (partner);
		if (dataManagementPage.isColumnPresentInGrid ("GRID_777894586949213", " Active (Y/N)")) {
		  System.out.println ("Active Name is present in Grid");
		  doWait (500);
		  dataManagementPage.selectTransformAttributeInColumnFilter ("HIERARCHY_NAME")
		                    .clickFirstRowEditPartnerAPITransformAttributes ();
		
		  // get original Transform Value
		
		  String transformvalue = dataManagementPage.getOriginalValueOfInputboxGridInlineEdit ("fldtarnsform_attribute_value");
		  System.out.println (transformvalue);
		
		  dataManagementPage.passNewValueInputBoxGridInlineEdit ("fldtransform_attribute_active",
		                                                         "Y")
		
		                    // Pass Dummy Transform Value = hirarchy
		                    .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value",hirarchy)
		
		                    // save Transform Status
		                    .clickSaveAndCloseEditRecordPopupOfGrid ()
		                    // check Dummy Transform Status in Grid
		                    .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "Y")
		
		                    // check Dummy Transform Status in Grid
		                    .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", hirarchy);
		          try {          
		                    apiImportTest(feed, product,url);
		          }finally {
		        	  cloudAdmin .clickAPIManagementPage (url)
		        	  		     .clickAPITransformAttributes ()
		        	  		     .clickRowForPartnerNameEditAPITransformAttributes (partner)
		        	  		   .selectTransformAttributeInColumnFilter ("HIERARCHY_NAME")
		 					.clickFirstRowEditPartnerAPITransformAttributes ()
		
		                    // Pass Original Transform Status
		                    .passNewValueInputBoxGridInlineEdit ("fldtransform_attribute_active",
		                                                         "Y")
		
		                    // Pass Original Transform Status
		                    .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value",
		                                                         transformvalue)
		
		                    // save Original Transform Status
		                    .clickSaveAndCloseEditRecordPopupOfGrid ()
		
		                    // check Original Transform Status in Grid
		                    .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "Y")
		
		                    // check Original Transform Value in Grid
		                    .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", transformvalue);
		          }
		} else {
		  fail (" Active Column Name Not Present in Grid ");
		}
    	
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI13_UMEI_API_apiFeedPreviewTest_UltiDemographic (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest ("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI14_UMEI_API_apiFeedPreviewTest_AdpDemographic (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest ("ADP_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI15_UMEI_API_apiFeedPreviewTest_UltiJobCode (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest ("ULTI_JOB_CODE", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI16_UMEI_API_apiFeedPreviewTest_AdpJobCode (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest ("ADP_JOB_CODE", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI20_UMEI_API_apiFeedPreviewTest_UltiP4PDemographic (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest ("ULTI_P4P_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI22_UMEI_API_apiFeedPreviewTest_UltiEmpSpare (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest (GENRIC_COMP_ULTI_EMP_SPARES, "COMPENSATION",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI46_UMEI_API_apiFeedPreviewTest_AdpEmpSpare (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest (GENRIC_ADP_COMP_EMP_SPARES, "COMPENSATION",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI23_UMEI_API_apiFeedPreviewTest_AdpMarkerData (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest ("ADP_COMPENSATION_MARKER_DATA", "COMPENSATION",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI24_UMEI_API_apiFeedPreviewTest_UltiPerfRatingEmp (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest (GENRIC_COMP_ULTI_PERF_RATING_EMP, "COMPENSATION",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI30_UMEI_API_apiFeedPreviewTest_UltiPlatformEmpSpares (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiPreviewTest (GENRIC_COMP_ULTI_PLATFORM_EMP_SPARES, "COMPENSATION",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI12_UMEI_API_apiEndpointIdEditTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickDataManagement (data.getApplicationURL ());
        doWait (500);
        String files = "ULTI_DEMOGRAPHIC";
        String products = "HRSOFTCLOUD";

        dataManagementPage
                          .selectProduct (products)
                          .clickConfigureFeed ()
                          .chooseFeedsInConfigureFeed (files)
                          .clickFieldMapping ()
                          .scrollhorizontal ("GRID_413115183171617", 130);
        if (dataManagementPage.isEndPointIdColumnPresent ()) {
            dataManagementPage.clickFirstRowFieldMapping ();
            doWait (500);
            if (dataManagementPage.isEndPointIdDropDownPresent ()) {
                System.out.println ("Endpoint Id Dropdown Present in Popup");
                List <String> endpointidselectandunselect = dataManagementPage.getElementUnselectedAndSelectedValue ("MENU_1536750226548060");
                System.out.println ("endpointidselectandunselect " + endpointidselectandunselect);
                // Set Other Value
                try {
                dataManagementPage
                                  .selectUsingSendkeyFromComboboxDropdown ("MENU_1536750226548060",
                                                                           endpointidselectandunselect.get (1))
                                  .selectUsingSendkeyFromComboboxDropdown ("MENU_181253905524841", "UserName")
                                  .clickSaveFeedMapping ()
                                  .clickFieldMappingGridRefresh ()
                                  .scrollhorizontal ("GRID_413115183171617", 130)
                                  .isNewEndpointidValuePresentInFirstRowOfGrid (10,
                                                                                1,
                                                                                "GRID_413115183171617",
                                                                                endpointidselectandunselect.get (1));
                }finally {
                                  // Reset Original Value
                dataManagementPage.clickFirstRowFieldMapping ()
                                  .selectUsingSendkeyFromComboboxDropdown ("MENU_1536750226548060",
                                                                           endpointidselectandunselect.get (0))
                                  .selectUsingSendkeyFromComboboxDropdown ("MENU_181253905524841", "UserName")
                                  .clickSaveFeedMapping ()
                                  .clickFieldMappingGridRefresh ()
                                  .scrollhorizontal ("GRID_413115183171617", 130)
                                  .isNewEndpointidValuePresentInFirstRowOfGrid (10,
                                                                                1,
                                                                                "GRID_413115183171617",
                                                                                endpointidselectandunselect.get (0));
                }
                } else {
                info (" Endpoint Id Dropdown Not Present " + files + " from " + products + " product");
            }
        } else {
            fail (" Endpoint Id Column Not Present " + files + " from " + products + " product");
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI09_UMEI_API_isEndpointNameDisableTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAPIManagementPage (data.getApplicationURL ());
        doWait (500);

        dataManagementPage.clickEndpointManagement ();
        if (dataManagementPage.isEndPointNameColumnPresent ()) {
            info ("EndPoint Name is present in grid");
            dataManagementPage.clickTreeCloseOfGrid ()
                              .clickSecondRowEditEndpointAdmin ();

            // Checking Endpoint is Disable or Enable
            if (!dataManagementPage.isEndpointNameInputBoxIsDisable ()) {
                info ("EndpointName Input box is Disable");
            } else {
                info (" EndpointName Input box is Enable ");
            }
        } else {
            fail (" Endpoint Name Not Present in Grid ");
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI08_UMEI_API_editEndpointPathTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAPIManagementPage (data.getApplicationURL ())
                  .clickEndpointManagement ();
        if (dataManagementPage.isEndPointPathColumnPresent ()) {
            System.out.println ("EndPath Name is present in Grid");
            dataManagementPage.clickTreeCloseOfGrid ()
                              .clickSecondRowEditEndpointAdmin ();
            // get original endpoint path
            String orginalvalue = dataManagementPage.getOriginalValueOfInputboxGridInlineEdit ("fldendpoint_path");
            System.out.println (orginalvalue);
            // concat of Orginal value and Pass Dummy Endpoint Path = xyz
            String dummyvalue = orginalvalue + "xyz";
            try {
            dataManagementPage
                              .passNewValueInputBoxGridInlineEdit ("fldendpoint_path",
                                                                   dummyvalue)
                              // save endpoint path
                              .clickSaveAndCloseEditRecordPopupOfGrid ()
                              .clickTreeCloseOfGrid ()
                              // check Dummy endpoint path in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (3, 2, "GRID_3272879231117616", dummyvalue);
            }finally {                 
            dataManagementPage                  
                              .clickSecondRowEditEndpointAdmin ()

                              // Pass Original Endpoint Path
                              .passNewValueInputBoxGridInlineEdit ("fldendpoint_path",
                                                                   orginalvalue)

                              // save Original endpoint path
                              .clickSaveAndCloseEditRecordPopupOfGrid ()
                              .clickTreeCloseOfGrid ()

                              // check Original endpoint path in Grid - method name given row and
                              // column
                              .valuePresentInGivenRowAndColumnOfGrid (3, 2, "GRID_3272879231117616", orginalvalue);
            } 
        } else {
            fail (" Endpoint Path Name Not Present in Grid ");
        }

    }

    /**
     * @stlc.requirements SM_API_10
     * 
     * @author Vivek Pandey
     */
    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI10_UMEI_API_editTransformActiveStatusTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAPIManagementPage (data.getApplicationURL ())
                  .clickAPITransformAttributes ()
                  .clickRowForPartnerNameEditAPITransformAttributes ("ADP_WFN");
        if (dataManagementPage.isColumnPresentInGrid ("GRID_777894586949213", " Active (Y/N)")) {
            System.out.println ("Active Column is present in Grid");
            dataManagementPage.selectTransformAttributeInColumnFilter ("REMOVE_TERM")
                              .clickFirstRowEditPartnerAPITransformAttributes ();

            // get original Transform Status
            String orginalvalue = dataManagementPage.getOriginalValueOfInputboxGridInlineEdit ("fldtransform_attribute_active");
            System.out.println (orginalvalue);

            String dummyvalue = (orginalvalue.equals ("Y")) ? "N" : "Y";
            try {
            // Pass Dummy Transform Status = Q
            dataManagementPage
                              .passNewValueInputBoxGridInlineEdit ("fldtransform_attribute_active",
                                                                   dummyvalue)
                              // save Transform Status
                              .clickSaveAndCloseEditRecordPopupOfGrid ()
                              // check Dummy Transform Status in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", dummyvalue);

            apiPreviewTest ("ADP_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
            }finally {
            cloudAdmin.clickAPIManagementPage (data.getApplicationURL ())
                      .clickAPITransformAttributes ()
                      .clickRowForPartnerNameEditAPITransformAttributes ("ADP_WFN")
                      .selectTransformAttributeInColumnFilter ("REMOVE_TERM")
                      .clickFirstRowEditPartnerAPITransformAttributes ()
                      // Pass Original Transform Status
                      .passNewValueInputBoxGridInlineEdit ("fldtransform_attribute_active",
                                                           orginalvalue)

                      // save Original Transform Status
                      .clickSaveAndCloseEditRecordPopupOfGrid ()

                      // check Original Transform Status in Grid
                      .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", orginalvalue);
            }
            } else {
            fail (" Active Column Name Not Present in Grid ");
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI04_UMEI_API_checkingUltiSetIdAttributeTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAttributes (data.getApplicationURL ());
        doWait (500);

        String[] setid = { "ULTI_SET_ID" };
        String[] attributename = { "Password", "Username", "US-Customer-Api-Key" };

        configure.searchAndClickSetIDInAttributeLookupGrid (setid[0]);
        doWait (500);
        for (int i = 0; i < attributename.length; i++) {
            // Check AttributeName
            System.out.println ("Checking : " + attributename[i]);
            configure.verifyAttributeNamePresentInAttributeLookupGrid (1, attributename[i]);
            System.out.println ("Found : " + attributename[i]);
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI05_UMEI_API_editUltiSetIdAttributeTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());  
        cloudAdmin
                  .clickAttributes (data.getApplicationURL ())
                  .searchAndClickSetIDInAttributeLookupGrid ("ULTI_SET_ID")
                  .clickAndMoveSlider ()
                  .clickOnInlineEditOfAttributeLookupGrid ("Username");
        doWait (500);
        hrSoftPage.clickConfirmInPopup ();/// ClickConfirmInPopup
        // get original Attribute Value
        String orginalvalue = configure.getOriginalValueOfAttributesEditPopup ("TEXT_INPUT_1151450258386897");
        System.out.println (orginalvalue);

        // concat of Orginal value and Pass Dummy Endpoint Path = xyz

        String dummyvalue = orginalvalue + "xyz";
        // Pass Dummy Attribute Value = xyz
        try {
        configure.passNewValueInputBoxAttributesEditPopup ("TEXT_INPUT_1151450258386897",
                                                      dummyvalue)
                 // save Attribute Value

                 .clickSaveAndCloseEditRecordPAttributesEditPopup ()
                 .verifyAttributeValuePresentInAttributeLookupGrid (2, dummyvalue);
        }finally {
        configure.clickOnInlineEditOfAttributeLookupGrid ("Username");
        hrSoftPage.clickConfirmInPopup ();
        // Pass Original Attribute Value
        configure.passNewValueInputBoxAttributesEditPopup ("TEXT_INPUT_1151450258386897",
                                                      orginalvalue)

                 // save Attribute Value
                 .clickSaveAndCloseEditRecordPAttributesEditPopup ()
                 .verifyAttributeValuePresentInAttributeLookupGrid (2, orginalvalue);
        }
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI07_UMEI_API_editADPSetIdAttributeTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());

        cloudAdmin
                  .clickAttributes (data.getApplicationURL ())
                  .searchAndClickSetIDInAttributeLookupGrid ("ADP_SET_ID")
                  .clickAndMoveSlider ()
                  .clickOnInlineEditOfAttributeLookupGrid ("Bearer_Header");
        doWait (500);
        hrSoftPage.clickConfirmInPopup ();/// ClickConfirmInPopup
        // get original Attribute Value
        String orginalvalue = configure.getOriginalValueOfAttributesEditPopup ("TEXT_INPUT_1151450258386897");
        System.out.println (orginalvalue);

        // concat of Orginal value and Pass Dummy Endpoint Path = xyz

        String dummyvalue = orginalvalue + "xyz";
        try {
        // Pass Dummy Attribute Value = xyz
        configure.passNewValueInputBoxAttributesEditPopup ("TEXT_INPUT_1151450258386897",
                                                      dummyvalue)
                 // save Attribute Value

                 .clickSaveAndCloseEditRecordPAttributesEditPopup ()
                 .verifyAttributeValuePresentInAttributeLookupGrid (2, dummyvalue);
        }finally {
        configure.clickOnInlineEditOfAttributeLookupGrid ("Bearer_Header");
        hrSoftPage.clickConfirmInPopup ();
        // Pass Original Attribute Value
        configure.passNewValueInputBoxAttributesEditPopup ("TEXT_INPUT_1151450258386897",
                                                      orginalvalue)

                 // save Attribute Value
                 .clickSaveAndCloseEditRecordPAttributesEditPopup ()
                 .verifyAttributeValuePresentInAttributeLookupGrid (2, orginalvalue);
        }
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI11_UMEI_API_editTransformValueAndActiveStatusTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAPIManagementPage (data.getApplicationURL ())
                  .clickAPITransformAttributes ()
                  .clickRowForPartnerNameEditAPITransformAttributes ("Ultimate");
        if (dataManagementPage.isColumnPresentInGrid ("GRID_777894586949213", " Active (Y/N)")) {
            System.out.println ("Active Name is present in Grid");
            doWait (500);
            dataManagementPage.selectTransformAttributeInColumnFilter ("CUSTOM_JOB_CODE")
                              .clickFirstRowEditPartnerAPITransformAttributes ();

            // get original Transform Status and Value
            String transformstatus = dataManagementPage.getOriginalValueOfInputboxGridInlineEdit ("fldtransform_attribute_active");
            System.out.println (transformstatus);

            String transformvalue = dataManagementPage.getOriginalValueOfInputboxGridInlineEdit ("fldtarnsform_attribute_value");
            System.out.println (transformvalue);
            try {
            // Pass Dummy Transform Status = Q
            dataManagementPage.passNewValueInputBoxGridInlineEdit ("fldtransform_attribute_active",
                                                                   "Q")

                              // Pass Dummy Transform Value = xyz
                              .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value",
                                                                   "xyz")

                              // save Transform Status
                              .clickSaveAndCloseEditRecordPopupOfGrid ()
                              // check Dummy Transform Status in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "Q")

                              // check Dummy Transform Status in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", "xyz")

                              .clickFirstRowEditPartnerAPITransformAttributes ();
            }finally {
                              // Pass Original Transform Status
            dataManagementPage.passNewValueInputBoxGridInlineEdit ("fldtransform_attribute_active",
                                                                   transformstatus)

                              // Pass Original Transform Status
                              .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value",
                                                                   transformvalue)

                              // save Original Transform Status
                              .clickSaveAndCloseEditRecordPopupOfGrid ()

                              // check Original Transform Status in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", transformstatus)

                              // check Original Transform Value in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", transformvalue);
            }
        } else {
            fail (" Active Column Name Not Present in Grid ");
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI06_UMEI_API_checkingADPSetIdAttributeTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAttributes (data.getApplicationURL ());
        doWait (500);

        String[] setid = { "ADP_SET_ID" };
        String[] attributename = {
                "Bearer_Header", "Client_Secret", "Grant_Type", "Auth_Server_Url", "Client_ID",
                "AUTH_ERROR_REDIRECT_URL", "scope", "Client_Cert_Key_Password", "Client_Cert_File_Path",
                "Client_Cert_Store_Password", "SubscriberOID" };

        doWait (500);
        configure.searchAndClickSetIDInAttributeLookupGrid (setid[0]);
        doWait (500);
        for (int i = 0; i < attributename.length; i++) {
            // Check AttributeName
            configure.verifyAttributeNamePresentInAttributeLookupGrid (1, attributename[i]);
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI37_UMEI_API_previewGridAndCheckForTransformValueOfPartTimeFactorTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAPIManagementPage (data.getApplicationURL ())

                  .clickAPITransformAttributes ()
                  .clickRowForPartnerNameEditAPITransformAttributes ("ADP_WFN");
        if (dataManagementPage.isColumnPresentInGrid ("GRID_777894586949213", " Tranform Value")) {
            System.out.println (" Tranform Value is present in Grid");
            doWait (500);
			dataManagementPage.selectTransformAttributeInColumnFilter("PART_TIME_FACTOR")

					// Filter PART_TIME_FACTOR in Grid
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "PART_TIME_FACTOR")
					.valuePresentInGivenRowAndColumnOfGrid(2, 1, "GRID_777894586949213", "1");

            apiPreviewTest ("ADP_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
            doWait (500);
            if (dataManagementPage.isColumnPresentInPreviewGrid ("PART_TIME_FACTOR", "SECTION_627914305915076")) {
                info ("PART_TIME_FACTOR Column Present In Grid");
                doWait (500);
                dataManagementPage.scrollToSpecifcColumnInGrid ("SECTION_627914305915076", "PART_TIME_FACTOR");
                if (hrSoftPage.isElementPresentInColumnOfGrid ("PART_TIME_FACTOR", "1.0", "SECTION_627914305915076")) {
                    info ("1.0 Found in PART_TIME_FACTOR Column");
                } else {
                    info (" 1.0 Not Found in PART_TIME_FACTOR Column");
                }
            } else {
                info (" PART_TIME_FACTOR Column Not Found in Grid");
            }

        } else {
            fail (" Active Column Name Not Present in Grid ");
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI38_UMEI_API_previewGridAndCheckForTransformValueOfCurrencyCodeTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAPIManagementPage (data.getApplicationURL ())

                  .clickAPITransformAttributes ()
                  .clickRowForPartnerNameEditAPITransformAttributes ("ADP_WFN");
        if (dataManagementPage.isColumnPresentInGrid ("GRID_777894586949213", " Tranform Value")) {
            info (" Tranform Value is present in Grid");
            doWait (500);
            dataManagementPage.selectTransformAttributeInColumnFilter ("DEFAULT_CURRENCY_CODE")

                              // Filter DEFAULT_CURRENCY_CODE in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (1,
                                                                      1,
                                                                      "GRID_777894586949213",
                                                                      "DEFAULT_CURRENCY_CODE")
                              .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", "USD");

            apiPreviewTest ("ADP_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
            if (dataManagementPage.isColumnPresentInPreviewGrid ("CURRENCY_CODE", "SECTION_627914305915076")) {
                info ("CURRENCY_CODE Column Present In Grid");
                doWait (500);
                dataManagementPage.scrollToSpecifcColumnInGrid ("SECTION_627914305915076", "CURRENCY_CODE");
                if (hrSoftPage.isElementPresentInColumnOfGrid ("CURRENCY_CODE", "USD", "SECTION_627914305915076")) {
                    info ("USD Found in CURRENCY_CODE Column");
                } else {
                    info (" USD Not Found in CURRENCY_CODE Column");
                }
            } else {
                info (" CURRENCY_CODE Column Not Found in Grid");
            }

        } else {
            fail (" Active Column Name Not Present in Grid ");
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI41_UMEI_API_previewGridForActiveStatusOfRemoveTermTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAPIManagementPage (data.getApplicationURL ())
                  .clickAPITransformAttributes ()
                  .clickRowForPartnerNameEditAPITransformAttributes ("ADP_WFN");
        if (dataManagementPage.isColumnPresentInGrid ("GRID_777894586949213", " Active (Y/N)")) {
        	try {
            info ("Active Name is present in Grid");
            doWait (500);
            dataManagementPage.selectTransformAttributeInColumnFilter ("REMOVE_TERM")

                              // Filter REMOVE_TERM in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (1, 1, "GRID_777894586949213", "REMOVE_TERM")

                              .clickFirstRowEditPartnerAPITransformAttributes ()
                              // Pass N Transform Status
                              .passNewValueInputBoxGridInlineEdit ("fldtransform_attribute_active",
                                                                   "N")
                              // save Original Transform Status
                              .clickSaveAndCloseEditRecordPopupOfGrid ()
                              // check Original Transform Status in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N");

            apiPreviewTest ("ADP_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
            if (dataManagementPage.isColumnPresentInPreviewGrid ("EMP_STATUS_CODE", "SECTION_627914305915076")) {
                info ("EMP_STATUS_CODE Column Present In Grid");
                doWait (500);
                dataManagementPage.scrollToSpecifcColumnInGrid ("SECTION_627914305915076", "EMP_STATUS_CODE");
                if (hrSoftPage.isElementPresentInColumnOfGrid ("EMP_STATUS_CODE", "T", "SECTION_627914305915076")) {
                    info ("T Found in EMP_STATUS_CODE Column");
                } else {
                    info (" T Not Found in EMP_STATUS_CODE Column");
                }
            } else {
                fail (" EMP_STATUS_CODE Column Not Found in Grid");
            }
        }finally {
            cloudAdmin.clickAPIManagementPage (data.getApplicationURL ());
            doWait (500);

            dataManagementPage
                              .clickAPITransformAttributes ()
                              .clickRowForPartnerNameEditAPITransformAttributes ("ADP_WFN")
                              .selectTransformAttributeInColumnFilter ("REMOVE_TERM")

                              // Filter REMOVE_TERM in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (1, 1, "GRID_777894586949213", "REMOVE_TERM")

                              .clickFirstRowEditPartnerAPITransformAttributes ()
                              // Pass N Transform Status
                              .passNewValueInputBoxGridInlineEdit ("fldtransform_attribute_active",
                                                                   "Y")
                              // save Original Transform Status
                              .clickSaveAndCloseEditRecordPopupOfGrid ()

                              // check Original Transform Status in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "Y");
        }
        } else {
            info (" Active Column Name Not Present in Grid ");
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI39_UMEI_API_feedCloneInFeedMangementTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickDataManagement (data.getApplicationURL ());

        String[] files = { "ULTI_P4P_DEMOGRAPHIC" };
        String[] products = { "HRSOFTCLOUD" };
        String feedname = "1_automation_test_Clone_New";

        for (int i = 0; i < files.length; i++) {
            dataManagementPage.refresh ();
            dataManagementPage
                              .selectProduct (products[i])
                              .clickFeedManagement ()
                              .selectOptionFromDropdown ("MENU_138087251567401", files[i])
                              .clickFeedClone ()
                              .passNewValueInputBoxGridInlineEdit ("promptBox", feedname);

            if (!dataManagementPage.clickSubmitOfFeedClonePopup ()) {
                dataManagementPage
                                  .selectOptionFromDropdown ("MENU_138087251567401", files[i])
                                  .clickFeedClone ()
                                  .passNewValueInputBoxGridInlineEdit ("promptBox", feedname)
                                  .clickSubmitOfFeedClonePopup ();
            }
            if (hrSoftPage.isElementPresentInColumnOfGrid (1, feedname, "GRID_138951714897547")) {
                info ("automation_test Feed Found in Grid");
                dataManagementPage.selectImportTab ()
                                  .selectUsingSendkeyFromComboboxDropdown ("MENU_335629169244049", files[i])
                                  .clickFeedManagement ();
                if (hrSoftPage.isElementPresentInColumnOfGrid (1, feedname, "GRID_138951714897547")) {
                    doWait (500);
                    dataManagementPage.deleteRowForGivenFeedName (feedname);
                }

            } else {
                fail (" automation_test Feed Not Found in Grid");
            }
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI40_UMEI_API_apiDataProviderFeedConfigureTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickDataManagement (data.getApplicationURL ());

        // Get api feed list from dataManagementPage page

        String[] files = { "ADP_DEMOGRAPHIC" };
        String[] products = { "HRSOFTCLOUD" };
        String feedname = "1_automation_test_Clone_New";

        for (int i = 0; i < files.length; i++) {
            dataManagementPage.refresh ();
            dataManagementPage.selectProduct (products[i])
                              .clickFeedManagement ()
                              .selectOptionFromDropdown ("MENU_138087251567401", files[i])
                              .clickFeedClone ()
                              .passNewValueInputBoxGridInlineEdit ("promptBox", feedname);

            if (!dataManagementPage.clickSubmitOfFeedClonePopup ()) {
                dataManagementPage
                                  .selectOptionFromDropdown ("MENU_138087251567401", files[i])
                                  .clickFeedClone ()
                                  .passNewValueInputBoxGridInlineEdit ("promptBox", feedname)
                                  .clickSubmitOfFeedClonePopup ();
            }
            if (hrSoftPage.isElementPresentInColumnOfGrid (1, feedname, "GRID_138951714897547")) {
                info ("automation_test Feed Found in Grid");
                dataManagementPage.clickConfigureFeed ()
                                  .selectOptionFromDropdown ("MENU_297564209205268", feedname)
                                  .clickDefineFeed ()
                                  .selectOptionFromDropdown ("MENU_998858653979595", "ADP_WFN")
                                  .selectImportTab ()
                                  .selectOptionFromDropdown ("MENU_335629169244049", feedname)
                                  .clickPreview ()
                                  .waitTillPreviewIsFinished (10);
                if (!dataManagementPage.isAPIFeedPreviewSuccess ()) {
                    if (dataManagementPage.errorFileNotSelectedAfterPreviewClick ()) {
                        System.out.println ("API Feed : " + files[i] + " - Not Selected in Dropdown - Product : " + products[i] + " Unable to Preview ");
                        fail ("API Feed : " + files[i] + " - Not Selected in Dropdown - Product : " + products[i] + " Unable to Preview ");
                    } else if (dataManagementPage.errorMappingAfterPreviewClick ()) {
                        System.out.println ("API Feed : " + files[i] + " - Feed Configuration Issue - Product : " + products[i] + " Unable to Preview ");
                        fail ("API Feed : " + files[i] + " - Feed Configuration Issue - Product : " + products[i] + " Unable to Preview ");
                    } else if (dataManagementPage.isJobAbort ()) {
                        System.out.println ("API Feed : " + files[i] + " - Job Abort - Product : " + products[i] + " Unable to Preview ");
                        fail ("API Feed : " + files[i] + " - Job Abort - Product : " + products[i] + " Unable to Preview ");
                    }
                } else {
                    System.out.println ("API Feed : " + files[i] + " of Product : " + products[i] + " Preview Successfully");
                    dataManagementPage.closePopup ();
                }
                doWait (500);
                dataManagementPage.clickFeedManagement ();
                doWait (500);
                if (hrSoftPage.isElementPresentInColumnOfGrid (1, feedname, "GRID_138951714897547")) {
                    doWait (500);
                    dataManagementPage.deleteRowForGivenFeedName (feedname);

                }
            } else {
                fail (" automation_test Feed Not Found in Grid");
            }

        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI32_UMEI_API_isTransformNameCannotBeModifiedTest (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAPIManagementPage (data.getApplicationURL ())

                  .clickAPITransformAttributes ()
                  .clickRowForPartnerNameEditAPITransformAttributes ("Ultimate");
        if (dataManagementPage.isColumnPresentInGrid ("GRID_777894586949213", " Transform Attribute")) {
            info (" Transform Attribute Column is present in Grid");
            doWait (500);
            dataManagementPage.clickFirstRowEditPartnerAPITransformAttributes ();

            // get original Transform Attribute
            String orginalvalue = dataManagementPage.getOriginalValueOfInputboxGridInlineEdit ("fldtransform_attribute_name");
            System.out.println (orginalvalue);

            String dummyvalue = orginalvalue + "xyz";

            // Pass Dummy Transform Attribute
            dataManagementPage.passNewValueInputBoxGridInlineEdit ("fldtransform_attribute_name",
                                                                   dummyvalue)
                              // save Transform Attribute
                              .clickSaveAndCloseEditRecordPopupOfGrid ()
                              // check Original Transform Attribute in Grid Because New Value will
                              // not save
                              .valuePresentInGivenRowAndColumnOfGrid (1, 1, "GRID_777894586949213", orginalvalue);

        } else {
            fail ("  Transform Attribute Column Name Not Present in Grid ");
        }

    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI17_UMEI_API_apiFeedImportTest_UltiDemographic (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        changeHierarchyInAPIManagementAndApiImport("Ultimate", "123_plan_Automat_API_Hier","ULTI_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI18_UMEI_API_apiFeedImportTest_AdpDemographic (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiImportTest ("ADP_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI19_UMEI_API_apiFeedImportTest_UltiJobCode (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiImportTest ("ULTI_JOB_CODE", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI44_UMEI_API_apiFeedImportTestAdpJobCode (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiImportTest ("ADP_JOB_CODE", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI21_UMEI_API_apiFeedImportTestUltiP4PDemographic (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        changeHierarchyInAPIManagementAndApiImport("Ultimate", "123_func_Automat_API_Hier","ULTI_P4P_DEMOGRAPHIC", "HRSOFTCLOUD",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI25_UMEI_API_apiFeedImportTest_UltiEmpSpare (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiImportTest (GENRIC_COMP_ULTI_EMP_SPARES, "COMPENSATION",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI45_UMEI_API_apiFeedImportTest_AdpEmpSpares (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiImportTest (GENRIC_ADP_COMP_EMP_SPARES, "COMPENSATION",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI26_UMEI_API_apiFeedImportTest_AdpMarkerData (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiImportTest ("ADP_COMPENSATION_MARKER_DATA", "COMPENSATION",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI27_UMEI_API_apiFeedImportTest_UltiPerfRatingEmp (TestDataExcel data) {
        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        //Max-Allowed Error - 200
        cloudAdmin.clickDataManagement (data.getApplicationURL ());
        doWait (500);
        String[] files = { GENRIC_COMP_ULTI_PERF_RATING_EMP };
        String[] products = { "COMPENSATION" };
        for (int i = 0; i < files.length; i++) {
            info ("API Feed : " + files[i] + " of Product : " + products[i]);
            dataManagementPage
                              .selectProduct (products[i])
                              .selectImportTab ()
                              .selectOptionFromDropdownAndCloneFeedNotAvailable ("MENU_335629169244049", files[i])
                              .clickImport ()
                              .waitTillJobIsFinished (10);
            if (!dataManagementPage.isJobCompletedSuccessFully ()) {
                if (dataManagementPage.isJobFailed ()) {
                	System.out.println (" Import Failed for " + files[i] + " from " + products[i] + " product");
            		dataManagementPage.openDetailedLogs ()
                	.filterErrorMessageInDetailsLogPopup();
                	List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
                	for (String text : filteroptionnew) {
                		info("Details Logs Error : "+text);
                	}
                	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
                	for (String text : matchedDBError) {
                		info("DB Error : "+text);
                	}
                    fail (" Import Failed for " + files[i] + " from " + products[i] + " product");
                } else if (dataManagementPage.isJobCompletedWithErrors ()) {
                    System.out.println (" Import Completed with Errors for " + files[i] + " from " + products[i] + " product");
            		dataManagementPage.openDetailedLogs ()
                	.filterErrorMessageInDetailsLogPopup();
                	List<String> filteroptionnew=dataManagementPage.getListValueOfFilterInGridColumn(" Message","GRID_1516768038509_copy_5df68017");
                	for (String text : filteroptionnew) {
                		info("Details Logs Error : "+text);
                	}
                	List<String> matchedDBError=dataManagementPage.checkDBErrorPresentInDetailsLogs(filteroptionnew);
                	for (String text : matchedDBError) {
                		info("DB Error : "+text);
                	}
                    info("Import Completed with Errors for " + files[i] + " from " + products[i] + " product");
                } else {
                    System.out.println (" Failed to import " + files[i] + " from " + products[i] + " product");
                    fail (" Failed to import " + files[i] + " from " + products[i] + " product");
                }
            }
        }
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI31_UMEI_API_apiFeedImportTest_UltiPlaformEmpSpares (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        apiImportTest (GENRIC_COMP_ULTI_PLATFORM_EMP_SPARES, "COMPENSATION",data.getApplicationURL ());
    }

    @Author (name = QAResources.VIVEK)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.UMEI, "feed_API", "generic" },
            dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SMAPI02_UMEI_API_editPartnerCfgMainApiUrl (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        cloudAdmin.clickAPIManagementPage (data.getApplicationURL ())
                  .clickPartnerRegistration ();
        if (dataManagementPage.isMainApiUrlColumnPresent ()) {
            System.out.println ("Main API Url Column is present in Grid");
            dataManagementPage.clickFirstRowEditApiRegistration ();
            // get original Main API URL
            String orginalvalue = dataManagementPage.getOriginalValueOfInputboxGridInlineEdit ("fldmain_api_url");
            System.out.println (orginalvalue);
            // concat of Orginal value and Pass Dummy Main API URL = xyz
            String dummyvalue = orginalvalue + "xyz";
            try {
            dataManagementPage
                              .passNewValueInputBoxGridInlineEdit ("fldmain_api_url",
                                                                   dummyvalue)
                              // save Main API URL
                              .clickSaveAndCloseEditRecordPopupOfGrid ()
                              //.clickReloadGrid ()
                              // check Dummy Main API URL in Grid
                              .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_3016046435652668", dummyvalue);
            }finally {              
            dataManagementPage.clickFirstRowEditApiRegistration ()

                              // Pass Original Main API URL
                              .passNewValueInputBoxGridInlineEdit ("fldmain_api_url",
                                                                   orginalvalue)

                              // save Original Main API URL
                              .clickSaveAndCloseEditRecordPopupOfGrid ()
                              //.clickReloadGrid ()

                              // check Original Main API URL in Grid - method name given row and
                              // column
                              .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_3016046435652668", orginalvalue);
            }
        } else {
            fail (" Main APi URL Column Not Present in Grid ");
        }
    }
}
