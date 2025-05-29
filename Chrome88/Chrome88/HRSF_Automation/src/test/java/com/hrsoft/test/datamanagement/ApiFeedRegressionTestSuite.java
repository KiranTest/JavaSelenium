package com.hrsoft.test.datamanagement;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static com.hrsoft.reports.ExtentLogger.info;

import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.Test;

import static org.testng.Assert.*;
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

public class ApiFeedRegressionTestSuite extends WebBaseTest {

	private CloudAdminPage cloudAdmin = new CloudAdminPage();
	private DataManagementPage dataManagementPage = new DataManagementPage();

	private void apiPreviewTest(String feed, String product, String url) {
		cloudAdmin.clickDataManagement(url);
		doWait(500);
		String[] files = { feed };
		String[] products = { product };
		for (int i = 0; i < files.length; i++) {
			info("API Feed : " + files[i] + " of Product : " + products[i]);
			dataManagementPage.selectProduct(products[i]).selectImportTab()
					.selectOptionFromDropdown("MENU_335629169244049", files[i]).clickPreview()
					.waitTillPreviewIsFinished(10);
			if (!dataManagementPage.isAPIFeedPreviewSuccess()) {
				if (dataManagementPage.errorFileNotSelectedAfterPreviewClick()) {
					System.out.println("API Feed : " + files[i] + " - Not Selected in Dropdown - Product : "
							+ products[i] + " Unable to Preview ");
					fail("API Feed : " + files[i] + " - Not Selected in Dropdown - Product : " + products[i]
							+ " Unable to Preview ");
				} else if (dataManagementPage.errorMappingAfterPreviewClick()) {
					System.out.println("API Feed : " + files[i] + " - Feed Configuration Issue - Product : "
							+ products[i] + " Unable to Preview ");
					fail("API Feed : " + files[i] + " - Feed Configuration Issue - Product : " + products[i]
							+ " Unable to Preview ");
				} else if (dataManagementPage.isJobAbort()) {
					System.out.println("API Feed : " + files[i] + " - Job Abort - Product : " + products[i]
							+ " Unable to Preview ");
					fail("API Feed : " + files[i] + " - Job Abort - Product : " + products[i] + " Unable to Preview ");
				}
			} else {
				System.out.println("API Feed : " + files[i] + " of Product : " + products[i] + " Preview Successfully");
				info("API Feed : " + files[i] + " of Product : " + products[i] + " Preview Successfully");
				dataManagementPage.closePopup();
			}
		}
	}

	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_02_UMEI_API_previewToCheckRemoveTermAttributesTest(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL())
				.clickAPITransformAttributes().clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("REMOVE_TERM")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "REMOVE_TERM")
					.clickFirstRowEditPartnerAPITransformAttributes()
					.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "N");
			try {
				apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				if (dataManagementPage.isColumnPresentInPreviewGrid("EMP_STATUS_CODE", "SECTION_627914305915076")) {
					info("EMP_STATUS_CODE Column Present In Grid");
					doWait(500);
					dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_STATUS_CODE");
					List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("EMP_STATUS_CODE",
							"SECTION_627914305915076");
					System.out.println("filteroptionnew : "+filteroptionnew);
					System.out.println("filteroptionnew Size : "+filteroptionnew.size());
					if(filteroptionnew.contains("T")) {
						info("T Found in EMP_STATUS_CODE Column");
					}else {
						fail("Failed Due to T Not Found in EMP_STATUS_CODE Column");
					}
				} else {
					info(" EMP_STATUS_CODE Column Not Found in Grid");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);

				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN")
						.selectTransformAttributeInColumnFilter("REMOVE_TERM")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "REMOVE_TERM")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
						.clickSaveAndCloseEditRecordPopupOfGrid()
						.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}

	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_07_UMEI_API_previewToCheckEmpIdAsUsernameAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL())
				.clickAPITransformAttributes().clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("EMAIL_AS_USERNAME")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMAIL_AS_USERNAME")
					.clickFirstRowEditPartnerAPITransformAttributes()
					.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");

			try {
				apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				if (dataManagementPage.isColumnPresentInPreviewGrid("USER_NAME", "SECTION_627914305915076")) {
					info("USER_NAME Column Present In Grid");
					doWait(500);
					dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "USER_NAME");
					if (hrSoftPage.isElementPresentInColumnOfGrid("USER_NAME", "@", "SECTION_627914305915076")) {
						info("Email Found in USER_NAME Column");
					} else {
						info(" Email Found in USER_NAME Column");
					}
				} else {
					info(" USER_NAME Column Not Found in Grid");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);

				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN")
						.selectTransformAttributeInColumnFilter("EMAIL_AS_USERNAME")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMAIL_AS_USERNAME")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
						.clickSaveAndCloseEditRecordPopupOfGrid()
						.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "N");
			}

		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}

	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_06_UMEI_API_previewToCheckIncludeTermGreaterThenDateAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL())

				.clickAPITransformAttributes().clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("INCLUDE_TERM_GREATER_THEN_DATE")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213",
							"INCLUDE_TERM_GREATER_THEN_DATE")
					.clickFirstRowEditPartnerAPITransformAttributes()
					.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				if (dataManagementPage.isColumnPresentInPreviewGrid("EMP_STATUS_CODE", "SECTION_627914305915076")) {
					info("EMP_STATUS_CODE Column Present In Grid");
					doWait(500);
					dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_STATUS_CODE");
					List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("EMP_STATUS_CODE",
							"SECTION_627914305915076");
					System.out.println("filteroptionnew : "+filteroptionnew);
					System.out.println("filteroptionnew Size : "+filteroptionnew.size());
					if(filteroptionnew.contains("T")) {
						info("T Found in EMP_STATUS_CODE Column");
					}else {
						fail("Failed Due to T Not Found in EMP_STATUS_CODE Column");
					}
				} else {
					info(" EMP_STATUS_CODE Column Not Found in Grid");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);

				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN")
						.selectTransformAttributeInColumnFilter("INCLUDE_TERM_GREATER_THEN_DATE")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213",
								"INCLUDE_TERM_GREATER_THEN_DATE")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
						.clickSaveAndCloseEditRecordPopupOfGrid()
						.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "N");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}

	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_08_UMEI_API_previewToCheckEmpIdAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_ID");
		String empid = dataManagementPage.getFirstRowValueOfAnyColumnInGrid("EMP_ID", "SECTION_627914305915076");
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("EMP_ID")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMP_ID")
					.clickFirstRowEditPartnerAPITransformAttributes()
					.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_ID");
				String empnumber = dataManagementPage.getFirstRowValueOfAnyColumnInGrid("EMP_ID",
						"SECTION_627914305915076");
				assertNotEquals(empid, empnumber);
				info("Emp Id is change to Emp Number");
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);

				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN")
						.selectTransformAttributeInColumnFilter("EMP_ID")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMP_ID")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
						.clickSaveAndCloseEditRecordPopupOfGrid()
						.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "N");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}

	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_10_UMEI_API_previewToCheckEmployeeTypeCodeAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_TYPE_CODE");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("EMP_TYPE_CODE",
				"SECTION_627914305915076");
		System.out.println("filteroption : " + filteroption);
		System.out.println("filteroption : " + filteroption.get(3));
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("EMPLOYEE_TYPE_CODE")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMPLOYEE_TYPE_CODE")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);

			String transformvalue = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtarnsform_attribute_value");
			System.out.println(transformvalue);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.passNewValueInputBoxGridInlineEdit("fldtarnsform_attribute_value", filteroption.get(3))
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y")
					.valuePresentInGivenRowAndColumnOfGrid(2, 1, "GRID_777894586949213", filteroption.get(3));
			try {
				apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_TYPE_CODE");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("EMP_TYPE_CODE",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew : "+filteroptionnew);
				assertEquals(filteroptionnew.size(), 1);
				assertEquals(filteroption.get(3), filteroptionnew.get(0));
				info("Getting Specific EMPLOYEE_TYPE_CODE");
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);

				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN")
						.selectTransformAttributeInColumnFilter("EMPLOYEE_TYPE_CODE")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMPLOYEE_TYPE_CODE")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value",
                                                             transformvalue)
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N")
                        .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", transformvalue);
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_11_UMEI_API_previewToCheckEmployeeTypeCodeExcludeAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_TYPE_CODE");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("EMP_TYPE_CODE",
				"SECTION_627914305915076");
		//System.out.println("filteroption : " + filteroption);
		System.out.println("filteroption 3rd index : " + filteroption.get(3));
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("EMPLOYEE_TYPE_CODE_EXCLUDED")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMPLOYEE_TYPE_CODE_EXCLUDED")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);

			String transformvalue = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtarnsform_attribute_value");
			System.out.println(transformvalue);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.passNewValueInputBoxGridInlineEdit("fldtarnsform_attribute_value", filteroption.get(3))
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y")
					.valuePresentInGivenRowAndColumnOfGrid(2, 1, "GRID_777894586949213", filteroption.get(3));
			try {
				apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_TYPE_CODE");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("EMP_TYPE_CODE",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew : "+filteroptionnew);
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				assertEquals(filteroptionnew.size(), filteroption.size()-1);
				if(!filteroptionnew.contains(filteroption.get(3))) {
					info("List Does not Contain Excluded Emp Type Code");
					info("Getting Specific EMPLOYEE_TYPE_CODE_EXCLUDED");
				}else {
					fail("Failed Due to List Contain Excluded Emp Type Code");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN")
						.selectTransformAttributeInColumnFilter("EMPLOYEE_TYPE_CODE_EXCLUDED")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMPLOYEE_TYPE_CODE_EXCLUDED")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value","RET:SEC")
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N")
                        .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", "RET:SEC");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_12_UMEI_API_previewToCheckEmpIdExcludeAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_ID");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("EMP_ID",
				"SECTION_627914305915076");
		//System.out.println("filteroption : " + filteroption);
		System.out.println("filteroption 3rd index : " + filteroption.get(3));
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("EXCLUDE_EMP_ID")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EXCLUDE_EMP_ID")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);

			String transformvalue = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtarnsform_attribute_value");
			System.out.println(transformvalue);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.passNewValueInputBoxGridInlineEdit("fldtarnsform_attribute_value", filteroption.get(3))
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y")
					.valuePresentInGivenRowAndColumnOfGrid(2, 1, "GRID_777894586949213", filteroption.get(3));
			try {
				apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_ID");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("EMP_ID",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew : "+filteroptionnew);
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				assertEquals(filteroptionnew.size(), filteroption.size()-1);
				if(!filteroptionnew.contains(filteroption.get(3))) {
					info("List Does not Contain EXCLUDE_EMP_ID");
					info("Getting Specific EXCLUDE_EMP_ID");
				}else {
					fail("Failed Due to List Contain Excluded Emp Type Code");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN")
						.selectTransformAttributeInColumnFilter("EXCLUDE_EMP_ID")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EXCLUDE_EMP_ID")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value",
                                                             transformvalue)
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N")
                        .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", transformvalue);
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_13_UMEI_API_previewToCheckExcludeHireDateGreaterAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "HIRE_DATE");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("HIRE_DATE",
				"SECTION_627914305915076");
		//System.out.println("filteroption : " + filteroption);
		System.out.println("filteroption 3rd index : " + filteroption.get(3));
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("EXCLUDE_HIRE_DATE_GREATER")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EXCLUDE_HIRE_DATE_GREATER")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);

			String transformvalue = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtarnsform_attribute_value");
			System.out.println(transformvalue);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.passNewValueInputBoxGridInlineEdit("fldtarnsform_attribute_value", filteroption.get(3))
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y")
					.valuePresentInGivenRowAndColumnOfGrid(2, 1, "GRID_777894586949213", filteroption.get(3));
			try {
				apiPreviewTest("ADP_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "HIRE_DATE");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("HIRE_DATE",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew : "+filteroptionnew);
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		        LocalDate date = LocalDate.parse(filteroption.get(3), dateFormatter);
		        boolean allDatesValid = true;
		        for (String dateStr : filteroptionnew) {
		            LocalDate listItemDate = LocalDate.parse(dateStr, dateFormatter);
		            if (listItemDate.isAfter(date)) {
		                allDatesValid = false;
		                break;
		            }
		        }
		        if (allDatesValid) {
					info("List Does not Contain Date After "+filteroption.get(3));
					info("Getting Specific Date");
				}else {
					fail("Failed Due to List Contain Date After "+filteroption.get(3));
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("ADP_WFN")
						.selectTransformAttributeInColumnFilter("EXCLUDE_HIRE_DATE_GREATER")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EXCLUDE_HIRE_DATE_GREATER")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value",
                                                             transformvalue)
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N")
                        .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", transformvalue);
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_09_UMEI_API_previewToCheckEmpIdAsUserNameAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "USER_NAME");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("USER_NAME",
				"SECTION_627914305915076");
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("EMPID_AS_USERNAME")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMPID_AS_USERNAME")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "USER_NAME");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("USER_NAME",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				int countdiff=0;
				int filtersize=(filteroptionnew.size()>filteroption.size()) ? filteroption.size() : filteroptionnew.size();
				for (int i = 0; i < filtersize; i++) {
		            if (!filteroption .get(i).equals(filteroptionnew.get(i))) {
		            	countdiff++;
		            }
		        }
				if(countdiff>0) {
					info("Emp id change after enable EMPID_AS_USERNAME");
				}else {
					fail("Failed Due to List Contain Same Value Before and After Change in Attributes");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("EMPID_AS_USERNAME")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "EMPID_AS_USERNAME")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_03_UMEI_API_previewToCheckCustomJobCodeAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "JOB_CODE");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("JOB_CODE",
				"SECTION_627914305915076");
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("CUSTOM_JOB_CODE")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "CUSTOM_JOB_CODE")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);

			String transformvalue = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtarnsform_attribute_value");
			System.out.println(transformvalue);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.passNewValueInputBoxGridInlineEdit("fldtarnsform_attribute_value","JOB_CODE:LOCATION")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y")
					.valuePresentInGivenRowAndColumnOfGrid(2, 1, "GRID_777894586949213","JOB_CODE:LOCATION");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "JOB_CODE");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("JOB_CODE",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				assertNotEquals(filteroption.get(0), filteroptionnew.get(0));
				String filterfirstvalue = filteroptionnew.get(0);
				int count = filterfirstvalue.length() - filterfirstvalue.replaceAll("_", "").length();
				System.out.print("Count _ : "+count);
				if(count==1) {
					info("New Job Code Contain Concat Value ");
				}else {
					fail("Failed Due to Job Code does not have _ or Contain same value");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("CUSTOM_JOB_CODE")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "CUSTOM_JOB_CODE")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value","JOB_CODE:LOCATION:ORGLEVEL2CODE_DESCRIPTION:RADFORD_CODE:DEPT_CODE")
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N")
                        .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", "JOB_CODE:LOCATION:ORGLEVEL2CODE_DESCRIPTION:RADFORD_CODE:DEPT_CODE");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_14_UMEI_API_previewToCheckFteHoursWeekNoPartTimeAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "FTE_HOURS_PER_WEEK");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("FTE_HOURS_PER_WEEK",
				"SECTION_627914305915076");
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("FTE_HOURS_WEEK_NO_PART_TIME")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "FTE_HOURS_WEEK_NO_PART_TIME")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "FTE_HOURS_PER_WEEK");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("FTE_HOURS_PER_WEEK",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				int countdiff=0;
				int filtersize=(filteroptionnew.size()>filteroption.size()) ? filteroption.size() : filteroptionnew.size();
				for (int i = 0; i < filtersize; i++) {
		            if (!filteroption .get(i).equals(filteroptionnew.get(i))) {
		            	countdiff++;
		            }
		        }
				if(countdiff>0) {
					info("FTE_HOURS_PER_WEEK change after enable FTE_HOURS_WEEK_NO_PART_TIME");
				}else {
					fail("Failed Due to List Contain Same Value Before and After Change in Attributes ");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("FTE_HOURS_WEEK_NO_PART_TIME")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "FTE_HOURS_WEEK_NO_PART_TIME")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_15_UMEI_API_previewToCheckUseDummyTopUserAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("USE_DUMMY_TOP_USER")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "USE_DUMMY_TOP_USER")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "USER_NAME");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("USER_NAME",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				if(filteroptionnew.contains("dummyTopUser")) {
					info("dummyTopUser Found in USER_NAME Column");
				}else {
					fail("Failed Due to dummyTopUser Not Found in USER_NAME Column");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("USE_DUMMY_TOP_USER")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "USE_DUMMY_TOP_USER")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", transformstatus)
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", transformstatus);
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_16_UMEI_API_previewToCheckFullPartTimeAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "FULLTIMEORPARTTIMECODE");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("FULLTIMEORPARTTIMECODE",
				"SECTION_627914305915076");
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("FULL_PART_TIME")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "FULL_PART_TIME")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "FULLTIMEORPARTTIMECODE");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("FULLTIMEORPARTTIMECODE",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				assertEquals(filteroptionnew.size(),2);
				if(filteroptionnew.contains("F")&&filteroptionnew.contains("P")) {
					info("F and P Found in FULLTIMEORPARTTIMECODE Column");
				}else {
					fail("Failed Due to F and P Not Found in FULLTIMEORPARTTIMECODE Column");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("FULL_PART_TIME")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "FULL_PART_TIME")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_17_UMEI_API_previewToCheckUseDummyTopForEmptyMgrAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("USE_DUMMY_TOP_FOR_EMPTY_MGR")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "USE_DUMMY_TOP_FOR_EMPTY_MGR")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "COMP_MANAGER_ID");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("COMP_MANAGER_ID",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				if(filteroptionnew.contains("dummyTopUser")) {
					info("dummyTopUser Found in COMP_MANAGER_ID Column");
				}else {
					fail("Failed Due to dummyTopUser Not Found in COMP_MANAGER_ID Column");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("USE_DUMMY_TOP_FOR_EMPTY_MGR")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "USE_DUMMY_TOP_FOR_EMPTY_MGR")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", transformstatus)
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", transformstatus);
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_18_UMEI_API_previewToCheckGeoPayIdFromOtherAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "GEO_PAY_ID");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("GEO_PAY_ID",
				"SECTION_627914305915076");
		System.out.println("filteroption Size : " + filteroption.size());
		System.out.println("filteroption : " + filteroption);
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("GEO_PAY_ID_FROM_OTHER")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "GEO_PAY_ID_FROM_OTHER")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "GEO_PAY_ID");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("GEO_PAY_ID",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				System.out.println("filteroptionnew : " + filteroptionnew);
				int countdiff=0;
				int filtersize=(filteroptionnew.size()>filteroption.size()) ? filteroption.size() : filteroptionnew.size();
				for (int i = 0; i < filtersize; i++) {
		            if (!filteroption .get(i).equals(filteroptionnew.get(i))) {
		            	countdiff++;
		            }
		        }
				if(countdiff>0) {
					info("GEO_PAY_ID change after enable GEO_PAY_ID_FROM_OTHER");
				}else {
					fail("Failed Due to List Contain Same Value Before and After Change in Attributes");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("GEO_PAY_ID_FROM_OTHER")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "GEO_PAY_ID_FROM_OTHER")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_19_UMEI_API_previewToCheckHeirarchyNameAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("HIERARCHY_NAME")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "HIERARCHY_NAME")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);
			String transformvalue = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtarnsform_attribute_value");
			System.out.println(transformvalue);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "HIERARCHY_NAME");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("HIERARCHY_NAME",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				if(filteroptionnew.contains(transformvalue)) {
					info(transformvalue+" Found in HIERARCHY_NAME Column");
				}else {
					fail("Failed Due to "+transformvalue+" Not Found in HIERARCHY_NAME Column");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("HIERARCHY_NAME")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "HIERARCHY_NAME")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", transformstatus)
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", transformstatus);
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_20_UMEI_API_previewToCheckHireDateFromOtherAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "HIRE_DATE");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("HIRE_DATE",
				"SECTION_627914305915076");
		System.out.println("filteroption Size : " + filteroption.size());
		System.out.println("filteroption : " + filteroption);
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("HIRE_DATE_FROM_OTHER")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "HIRE_DATE_FROM_OTHER")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "HIRE_DATE");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("HIRE_DATE",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				System.out.println("filteroptionnew : " + filteroptionnew);
				int countdiff=0;
				int filtersize=(filteroptionnew.size()>filteroption.size()) ? filteroption.size() : filteroptionnew.size();
				System.out.println("Lower Value filtersize : " + filtersize);
				for (int i = 0; i < filtersize; i++) {
		            if (!filteroption .get(i).equals(filteroptionnew.get(i))) {
		            	countdiff++;
		            }
		        }
				if(countdiff>0) {
					info("HIRE_DATE change after enable HIRE_DATE_FROM_OTHER");
				}else {
					fail("Failed Due to List Contain Same Value Before and After Change in Attributes");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("HIRE_DATE_FROM_OTHER")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "HIRE_DATE_FROM_OTHER")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_05_UMEI_API_previewToCheckCustomJobGradeAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ULTI_JOB_CODE", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "GRADE");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("GRADE",
				"SECTION_627914305915076");
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("CUSTOM_JOB_GRADE")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "CUSTOM_JOB_GRADE")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);

			String transformvalue = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtarnsform_attribute_value");
			System.out.println(transformvalue);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.passNewValueInputBoxGridInlineEdit("fldtarnsform_attribute_value","LOCATION:ORGLEVEL3CODE_DESCRIPTION")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y")
					.valuePresentInGivenRowAndColumnOfGrid(2, 1, "GRID_777894586949213","LOCATION:ORGLEVEL3CODE_DESCRIPTION");
			try {
				apiPreviewTest("ULTI_JOB_CODE", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "GRADE");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("GRADE",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				assertNotEquals(filteroption.get(0), filteroptionnew.get(0));
				String filterfirstvalue = filteroptionnew.get(0);
				int count = filterfirstvalue.length() - filterfirstvalue.replaceAll("_", "").length();
				System.out.print("Count _ : "+count);
				if(count==1) {
					info("New Job Grade Contain Concat Value ");
				}else {
					fail("Failed Due to Job Code does not have _ or Contain same value");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("CUSTOM_JOB_GRADE")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "CUSTOM_JOB_GRADE")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value","LOCATION:ORGLEVEL3CODE_DESCRIPTION:RADFORD_CODE:ORGLEVEL2CODE")
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N")
                        .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", "LOCATION:ORGLEVEL3CODE_DESCRIPTION:RADFORD_CODE:ORGLEVEL2CODE");
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_04_UMEI_API_previewToCheckIncludeUsersAlawaysAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
				.selectTransformAttributeInColumnFilter("REMOVE_TERM")
				.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "REMOVE_TERM")
				.clickFirstRowEditPartnerAPITransformAttributes()
				.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
				.clickSaveAndCloseEditRecordPopupOfGrid()
				.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "N");
		apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_STATUS_CODE");
		dataManagementPage.filterOptionInPreviewGrid("EMP_STATUS_CODE","SECTION_627914305915076","T");
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_ID");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("EMP_ID","SECTION_627914305915076");
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("INCLUDE_USERS_ALWAYS")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "INCLUDE_USERS_ALWAYS")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);

			String transformvalue = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtarnsform_attribute_value");
			System.out.println(transformvalue);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
					.passNewValueInputBoxGridInlineEdit("fldtarnsform_attribute_value",filteroption.get(0))
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y")
					.valuePresentInGivenRowAndColumnOfGrid(2, 1, "GRID_777894586949213",filteroption.get(0))
					.selectTransformAttributeInColumnFilter("REMOVE_TERM")
    				.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "REMOVE_TERM")
    				.clickFirstRowEditPartnerAPITransformAttributes()
    				.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
    				.clickSaveAndCloseEditRecordPopupOfGrid()
    				.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "Y");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "EMP_ID");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("EMP_ID","SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				if(filteroptionnew.contains(filteroption.get(0))) {
					info(filteroption.get(0)+" Found in EMP_ID Column");
				}else {
					fail("Failed Due to "+filteroption.get(0)+" Not Found in EMP_ID Column");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("INCLUDE_USERS_ALWAYS")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "INCLUDE_USERS_ALWAYS")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
                        .passNewValueInputBoxGridInlineEdit ("fldtarnsform_attribute_value",transformvalue)
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", "N")
                        .valuePresentInGivenRowAndColumnOfGrid (2, 1, "GRID_777894586949213", transformvalue);
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
	
	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.REGRESSION, ComponentGroups.UMEIREGRESSION, "feed_API",
			"generic" }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void RG_API_01_UMEI_API_previewToCheckIgnoreCompanyAttribute(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
		dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "USER_NAME");
		List<String> filteroption = dataManagementPage.getListValueOfFilterInGrid("USER_NAME",
				"SECTION_627914305915076");
		System.out.println("filteroption Size : " + filteroption.size());
		cloudAdmin.clickAPIManagementPage(data.getApplicationURL()).clickAPITransformAttributes()
				.clickRowForPartnerNameEditAPITransformAttributes("Ultimate");
		if (dataManagementPage.isColumnPresentInGrid("GRID_777894586949213", " Active (Y/N)")) {
			info("Active Name is present in Grid");
			doWait(500);
			dataManagementPage.selectTransformAttributeInColumnFilter("IGNORE_COMPANY")
					.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "IGNORE_COMPANY")
					.clickFirstRowEditPartnerAPITransformAttributes();
			String transformstatus = dataManagementPage
					.getOriginalValueOfInputboxGridInlineEdit("fldtransform_attribute_active");
			System.out.println(transformstatus);
			dataManagementPage.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "N")
					.clickSaveAndCloseEditRecordPopupOfGrid()
					.valuePresentInGivenRowAndColumnOfGrid(3, 1, "GRID_777894586949213", "N");
			try {
				apiPreviewTest("ULTI_DEMOGRAPHIC", "HRSOFTCLOUD", data.getApplicationURL());
				dataManagementPage.scrollToSpecifcColumnInGrid("SECTION_627914305915076", "USER_NAME");
				List<String> filteroptionnew = dataManagementPage.getListValueOfFilterInGrid("USER_NAME",
						"SECTION_627914305915076");
				System.out.println("filteroptionnew Size : "+filteroptionnew.size());
				if(!filteroptionnew.contains("4438JO0000K0")&&filteroption.contains("4438JO0000K0")) {
					info("IGNORE_COMPANY Attributes Test Sucessfully");
				}else {
					fail("Failed IGNORE_COMPANY Attributes Test");
				}
			} finally {
				cloudAdmin.clickAPIManagementPage(data.getApplicationURL());
				doWait(500);
				dataManagementPage.clickAPITransformAttributes()
						.clickRowForPartnerNameEditAPITransformAttributes("Ultimate")
						.selectTransformAttributeInColumnFilter("IGNORE_COMPANY")
						.valuePresentInGivenRowAndColumnOfGrid(1, 1, "GRID_777894586949213", "IGNORE_COMPANY")
						.clickFirstRowEditPartnerAPITransformAttributes()
						.passNewValueInputBoxGridInlineEdit("fldtransform_attribute_active", "Y")
                        .clickSaveAndCloseEditRecordPopupOfGrid ()
                        .valuePresentInGivenRowAndColumnOfGrid (3, 1, "GRID_777894586949213", transformstatus);
			}
		} else {
			fail(" Active Column Name Not Present in Grid ");
		}
	}
}
