
package com.hrsoft.test.dataview;

import static com.hrsoft.reports.ExtentLogger.info;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.driver.Driver;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.dataview.DataViewPage;
import com.hrsoft.gui.dataview.DataViewSettingsPage;
import com.hrsoft.test.setuphelpers.RetryAnalyzer;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

@Test(groups = "smoke")
public class DataViewTestSuite extends WebBaseTest {


	private String report = "Autom" + RandomStringUtils.randomAlphabetic(4);
	private String rand = RandomStringUtils.randomNumeric(1);
	private String dataviewReportName = Constants.dataviewReportName;
	private String dataviewRenameReportName = Constants.dataviewRenameReportName;
	private String budgetRelatedReportName = "Budget Allocation by Leader and Division";

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_222_DataviewReportCloning(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		doWait(2000);
		String reportNameForCopy = RandomStringUtils.randomAlphabetic(4) + "CopyTestforReport";
		if (Integer.parseInt(rand) <= 0) {
			rand = RandomStringUtils.randomNumeric(1);
		}
		dataviewPage.openReport(Constants.dataviewReportName);
		dataviewPage.clickTopEllipsisAndSave();
		dataviewPage.copyReport(reportNameForCopy);
		dataviewPage.selectCreatedByMeOption();
		dataviewPage.openReport(reportNameForCopy);
		dataviewPage.removeCopiedReport();
		// dataviewPage.clickOptionsOfReport (rand);
//        String reportRowIndex = rand;
//        dataviewPage.clickSaveAsNew (reportRowIndex).copyReport (reportNameForCopy);
		// String copiedReport = dataviewPage.selectCreatedByMeOption
		// (reportNameForCopy);
//        System.out.println ("Copied Report Index : " + copiedReport);
//        dataviewPage.clickOptionsOfReport (copiedReport);
//
//        dataviewPage.removeCopiedReport (copiedReport);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_35_DataviewReportCloning(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.clickCreateNewReport().clickFirstBuiltInTemplate().clickSaveAsReport();
		dataviewPage.enterReportName(report);
		dataviewPage.clickConfirmReport();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_36_Dataview_EditInDesignerPage(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.clickOptionsOfReport("1").clickDesignInTheReportOptionsAndVerify();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_37_Dataview_EditInDesignerPage(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.clickOptionsOfReport("1").clickTopEllipsisAndSave();
		dataviewPage.enterReportName(report).clickConfirmReport();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_51_52_DataviewReport_FeaturedAndUnpublished(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.selectValueFromDropdown("Featured").clickOptionsOfReport("1");
		assertTrue(dataviewPage.isReportPublishedAndFeatured());
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_58_DataviewReport_QueryBuilder(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.clickOnreport(dataviewReportName).clickRun();
		dataviewPage.clickAddFilterButton();
		dataviewPage.chooseFilterAndEnterValue("contains", "usd");
		dataviewPage.clickApply();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_57_DataviewReport_Group_By_Dropdown_in_the_Grid(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.clickOnreport(budgetRelatedReportName).selectPlan(Constants.compPlanName)
				.selectGroupType("Roll up").clickRun().changeGroupFilter();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_53_DataviewReport_RunReportAndRefresh(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		doWait(2000);
		dataviewPage.clickOnreport(dataviewReportName);
		dataviewPage.selectGroupType("Direct").clickRun().clickRefreshButton();
		hrSoftPage.logOut();

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_54_55_DataviewReport_DownloadReportWithPrintOptions(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.clickOnreport(dataviewReportName);
		dataviewPage.selectGroupType("Roll up").clickRun();
		dataviewPage.clickChoosePrintOptions().choosePrintOptions("export grid as pdf");
		dataviewPage.isFileDownloaded(dataviewReportName);
		dataviewPage.choosePrintOptions("export grid to excel");
		dataviewPage.isFileDownloaded(dataviewReportName);
		dataviewPage.choosePrintOptions("export grid to csv");
		dataviewPage.isFileDownloaded(dataviewReportName);
		Driver.quitDriver();
	}

	@Author(name = QAResources.TANVI)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_56_DataviewReport_FieldListInTheGrid(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.clickOnreport(dataviewReportName).clickRun().clickSettingsButton();
		dataviewPage.isDesignerPageOpened();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM46_49_Dataview_DataviewExplorer_SearchAnyGroupAndReset(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.clickOnreport(dataviewReportName).selectPlan(Constants.compPlanName);
		dataviewPage.clickSearchGroupButton();
		String searchGrp = dataviewPage.searchForGroup(Constants.GroupName + " Grp");
		String searchedGrp = dataviewPage.checkGroupGotAddedInGrid();
		assertEquals(searchGrp, searchedGrp);
		dataviewPage.clickReset().clickClose();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM50_Dataview_DataviewExplorer_SearchGroupAndVerifyWithDropdownValue(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		dataviewPage.clickOnreport(dataviewReportName);
		dataviewPage.selectPlan(Constants.compPlanName).clickSearchGroupButton();
		String searchGrp = dataviewPage.searchForGroup(Constants.groupNameForGroupFilter);
		dataviewPage.selectAndAddSearchedGroup();
		String dropDownValue = dataviewPage.groupDropdownValue();
		System.out.println(searchGrp);
		System.out.println(dropDownValue);
		assertEquals(dropDownValue.equalsIgnoreCase(searchGrp), true);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.VIVEK)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM40To45_DataviewReport_runEmployeeListingReport(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataview = hrSoftPage.clickDataViewExplorerPage().clickOnreport(dataviewReportName);
		hrSoftPage.setValueInDropdowns("MENU_a14b334c", Constants.compPlanName);
		hrSoftPage.setValueInDropdowns("MENU_5c576f1e", Constants.groupNameForGroupFilter);
		hrSoftPage.setValueInDropdowns("MENU_87025967", "Direct");
		dataview.clickRun();

		info("Select Plan, Select Group and Select Group Type is working Fine ");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.HERA)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_223_224_DataviewReport_PublishingAndSharing_Individual_And_Verification(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		doWait(2000);
		dataviewPage.selectValueFromDropdown("-- Created by Me");
		String reportNameForCopy = RandomStringUtils.randomAlphabetic(4) + "Publish and Share";
		if (Integer.parseInt(rand) <= 0) {
			rand = RandomStringUtils.randomNumeric(1);
		}
		dataviewPage.clickOptionsOfReport(rand);
		String reportRowIndex = rand;
		dataviewPage.clickSaveAsNew(reportRowIndex).copyReport(reportNameForCopy);
		String copiedReportIndex = dataviewPage.selectCreatedByMeOption(reportNameForCopy);

		dataviewPage.clickOptionsOfReport(copiedReportIndex);
		dataviewPage.clickPublishInTheReportOptionsAndConfirm(copiedReportIndex);
		doWait(5000);

		dataviewPage.clickOptionsOfReport(copiedReportIndex);
		dataviewPage.clickShareInTheReportOptions(copiedReportIndex).clickSearchButtonOnSharePopUp()
				.clickOnSearchAndSelectUserByUserNameInSearchPopUp(Constants.TopPlannerUserName)
				.clickAddUserForSharingReport();

		System.out.println("SM-223 completed. Starting script for SM-224");
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		dataviewPage.clickDataViewExplorerPage();
		doWait(2000);
		dataviewPage.selectValueFromDropdown("-- Shared With Me");

		dataviewPage.selectReportByReportName(reportNameForCopy).clickRun();

		System.out.println("Verification for SM-223 completed. Deleting the cloned file from admin user now.");

		dataviewPage.cancelProxy();
		dataviewPage.clickDataViewExplorerPage();
		doWait(2000);
		dataviewPage.selectValueFromDropdown("-- Created by Me");

		String reportIndexForSelectedReport = dataviewPage.getReportIndexForReportWithName(reportNameForCopy);

		dataviewPage.clickOptionsOfReport(reportIndexForSelectedReport);
		dataviewPage.removeCopiedReport(reportIndexForSelectedReport);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.HERA)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_225_DataViewReport_PublishingAndMarkingAsFeatured_Individual(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();
		doWait(2000);
		dataviewPage.selectValueFromDropdown("-- Created by Me");
		String reportNameForCopy = RandomStringUtils.randomAlphabetic(4) + "CloneforFeaturing";
		if (Integer.parseInt(rand) <= 0) {
			rand = RandomStringUtils.randomNumeric(1);
		}
		dataviewPage.clickOptionsOfReport(rand);
		String reportRowIndex = rand;
		dataviewPage.clickSaveAsNew(reportRowIndex).copyReport(reportNameForCopy);
		String copiedReportIndex = dataviewPage.selectCreatedByMeOption(reportNameForCopy);
		dataviewPage.clickOptionsOfReport(copiedReportIndex);
		dataviewPage.clickPublishInTheReportOptionsAndConfirm(copiedReportIndex);
		doWait(5000);
		dataviewPage.clickOptionsOfReport(copiedReportIndex);
		dataviewPage.clickFeatureInTheReportOptionsAndConfirm(copiedReportIndex);
		dataviewPage.selectValueFromDropdown("-- Created by Me");
		String reportIndexForCopiedReport = dataviewPage.getReportIndexForReportWithName(reportNameForCopy);
		dataviewPage.clickOptionsOfReport(reportIndexForCopiedReport);
		dataviewPage.removeCopiedReport(reportIndexForCopiedReport);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.RAJEEV)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_355_Dataview_DataviewExplorer_RenameReport(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		System.out.println(data.getUserName() + ":" + data.getPassword());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();

		dataviewPage.clickOnreport(dataviewReportName);
		dataviewPage.clickRenameTagInTheReportOptions();
		dataviewPage.renameReport(dataviewRenameReportName);

		// Revert to Original
		dataviewPage.renameReport(dataviewReportName);

		hrSoftPage.logOut();
	}

	@Author(name = QAResources.RAJEEV)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_356_Dataview_DataviewExplorer_ReportDetails(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logInAndRefresh(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();

		dataviewPage.clickOnreport(dataviewReportName);
		dataviewPage.clickReportDetailsInTheReportOptions();

		hrSoftPage.logOut();
	}

	@Author(name = QAResources.RAJEEV)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_357_Dataview_DataviewExplorer_ShowScheduleEmailDeliveryWindow(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewPage dataviewPage = hrSoftPage.clickDataViewExplorerPage();

		dataviewPage.clickOnreport(dataviewReportName);
		dataviewPage.clickScheduleEmailDeliveryInTheReportOptions();

		hrSoftPage.logOut();
	}

	@Author(name = QAResources.RAJEEV)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_353_Dataview_DataviewExplorer_AccessControlToEditReportOwnership(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		System.out.println(data.getUserName() + ":" + data.getPassword());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewSettingsPage dataViewSettingsPage = hrSoftPage.clickDataViewSettingPage();
		dataViewSettingsPage.clickOnFirstReportCheckBox();
		dataViewSettingsPage.checkDeletePermission();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.RAJEEV)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.DATA_VIEW }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_350_Dataview_DataviewExplorer_AccessControlToEditReportPermission(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		System.out.println(data.getUserName() + ":" + data.getPassword());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DataViewSettingsPage dataViewSettingsPage = hrSoftPage.clickDataViewSettingPage();
		dataViewSettingsPage.clickOnFirstReportCheckBox();
		dataViewSettingsPage.clickAddUsersPoolsButton();
		dataViewSettingsPage.selectUsersFromDropDown("Admin User");
		dataViewSettingsPage.clickSaveUsersPoolsButton();
		dataViewSettingsPage.clickOKButton();
		hrSoftPage.logOut();
	}

}
