package com.hrsoft.test.rewardsview;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.db.CompViewDbHelper;
import com.hrsoft.enums.ManagerViewTabsType;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.rewardsview.ManagerViewPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

/**
 * @author Annameni Srinivas <a href=
 *         "mailto:annameni.srinivas@hrsoft.com">annameni.srinivas@hrsoft.com</a>
 */
public class ManagerViewTestSuite extends WebBaseTest {
	private ManagerViewPage managerView = new ManagerViewPage();
	String managerFirstName = Constants.CompManagerFirstName;
	String managerLastName = Constants.CompManagerLastName;

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.REWARDS }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_23_RewardsViewManagerViewShareFunctionalFailIfPlanFails(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.openManagerView();
		managerView.selectPlan(Constants.PlanForManagerView);
		managerView.clickCompensationStatement();
		managerView.clickToOpenSearch();
		managerView.enterManagerAndSearch(managerFirstName, managerLastName);

		managerView.selectMultipleCheckBoxes();
		managerView.clickShareAndConfirm();
        assertTrue (managerView.isErrorPresent ());
        managerView.clickOk ();
        hrSoftPage.logOut ();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.REWARDS }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_20_21_RewardsView_ManagerViewDownloadStatements(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.openManagerView();
		managerView.selectPlan(Constants.PlanForManagerView);
		managerView.clickCompensationStatement();
		managerView.clickToOpenSearch();
		managerView.enterManagerAndSearch(managerFirstName, managerLastName);

		managerView.clickEmployee(Constants.CoPlannerName);
		managerView.clickDownload();
		managerView.isFileDownloaded("_YEAR_END_COMP_STATEMENT");

		managerView.clickEmployee(Constants.CoPlannerName);
		managerView.selectMultipleCheckBoxes().clickBulkDownload();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.REWARDS }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_22_RewardsView_ManagerViewDownloadStatements(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.openManagerView();
		managerView.selectPlan(Constants.PlanForManagerView);
		managerView.clickCompensationStatement();
		managerView.clickToOpenSearch();
		managerView.enterManagerAndSearch(managerFirstName, managerLastName);

		assertTrue(managerView.isCompensationStatementEmployees());
		managerView.clickMyTeamTab();
		assertTrue(managerView.isMyTeamsEmployees());
		managerView.clickCompensationStatement();

		managerView.clickRecentDownload();
		if (managerView.isRecentDownloadsPresent()) {
			managerView.clickDownloadFromRecentDownload();
			managerView.closePopUp();
		} else {
			managerView.closePopUp();
			managerView.selectAllCheckBoxes();
			managerView.clickBulkDownload();
			managerView.clickRecentDownload();
			managerView.clickDownloadFromRecentDownload();
		}
		managerView.isFileDownloaded("_COMP_STATEMENTS");
	}

	@Author(name = QAResources.KIRAN)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.REWARDS }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM29_RewardsView_SearchForManagersGroups(TestDataExcel data) {

		CompViewDbHelper compViewDBHelper = new CompViewDbHelper();
		compViewDBHelper.getEmployeesByManagerGuid("60209E50-85AE-4DD7-8B9D-6E1A9CD166BE",
				"A55837AE-1494-4D9A-9C51-B8727B73C28F", 10, "D");

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ManagerViewPage managerView = hrSoftPage.openManagerView();

		softAssertions.assertThat(managerView.isDefaultTabSelected(ManagerViewTabsType.DASHBOARD))
				.as("Default tab selected is not Dashboard").isTrue();
		softAssertions.assertThat(managerView.checkAllFourTabs()).as("All 4 Tabs displayed").isTrue();
		managerView.selectEachTabAndVerify().selectPlan(getConfig().compPlanName()).clickToOpenSearch()
				.searchForManagerByFieldAndverifyResult(managerFirstName, managerLastName).clickSelectForTheROw()
				.selectMyTeamTab().isMyTeamsEmployees();

		softAssertions.assertAll();
		hrSoftPage.logOut();

	}

	@Author(name = QAResources.KIRAN)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.REWARDS }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM30_RewardsView_SearchForManagerGroup_ThroughEmployee(TestDataExcel data) {

		CompViewDbHelper compViewDBHelper = new CompViewDbHelper();
		compViewDBHelper.getEmployeesByManagerGuid(Constants.custId, Constants.managerGuidForManagerView,
				Constants.compPlanId, "N");

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ManagerViewPage managerView = hrSoftPage.openManagerView();

		softAssertions.assertThat(managerView.isDefaultTabSelected(ManagerViewTabsType.DASHBOARD))
				.as("Default tab selected is not Dashboard").isTrue();
		softAssertions.assertThat(managerView.checkAllFourTabs()).as("All 4 Tabs displayed").isTrue();

		managerView.selectEachTabAndVerify().selectPlan(Constants.compPlanName).clickToOpenSearch().selectEmployeeTab()
				.searchForEmployeeByFieldAndverifyResult(Constants.EmployeeFirstName, Constants.EmployeeLastName)// Search
				// employee
				.clickSelectForTheROwEmployee().selectMyTeamTab().isMyTeamsEmployees();

		softAssertions.assertAll();
		hrSoftPage.logOut();
	}
}
