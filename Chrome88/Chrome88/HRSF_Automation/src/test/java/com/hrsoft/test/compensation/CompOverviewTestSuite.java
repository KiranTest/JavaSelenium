package com.hrsoft.test.compensation;

import static com.hrsoft.reports.ExtentLogger.info;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.cloudadmin.CloudAdminPage;
import com.hrsoft.gui.cloudadmin.ManagePlansPage;
import com.hrsoft.gui.compensation.CompOverviewPage;
import com.hrsoft.gui.compensation.PlanningPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

public class CompOverviewTestSuite extends WebBaseTest {
	private CloudAdminPage cloudAdmin = new CloudAdminPage();
	private PlanningPage planningPage = new PlanningPage();

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_14_CompViewPlanningVerifyCoPlannerField(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickManageCoPlanners();
		cloudAdmin.setManagerAndCoPlanner(Constants.restrictedManagerUserName, Constants.restrictedCoplannerUserName);
		cloudAdmin.restrictToCompFilter(Constants.FILTER);
		cloudAdmin.clickSave();
		cloudAdmin.recalculateSecurityAndClose();

		hrSoftPage.proxyAs(Constants.restrictedCoplannerUserName);
		hrSoftPage.refresh();
		hrSoftPage.openPlanningPageForRestricted(Constants.compPlanId, Constants.restrictedManagerUserName);
		planningPage.clickAdvanced();
		int list = planningPage.getNumberOfItemsInFilter();
		assertEquals(list, 1);
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_17_CompViewPlanningEnterNoteBasedOnEnvironmentRule(TestDataExcel data) {
		String notesHeader = "WS_Notes";
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickEnvironmentalOptions();
		;
		cloudAdmin.clickPlannerCanReviseTheirOwnLastNote();
		cloudAdmin.selectYesOrNo("Yes");

		hrSoftPage.proxyAs(Constants.CompManagerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.getcolumnToNumber("2", notesHeader);
		planningPage.doWait(2000);
		planningPage.clickIndividualNotes();
		String notes = planningPage.isNotesHistory();
		planningPage.enterIndividualNotes("test");
		info("entered individual notes");

		planningPage.clickIndividualNotes();
		String notesAfter = planningPage.isNotesHistory();
		planningPage.closePopUp();
		assertEquals(notes.length(), notesAfter.length());
		info("Notes didn't save");

		hrSoftPage.cancelProxy();
		planningPage.goToHrSoftPage();
		hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickEnvironmentalOptions();
		cloudAdmin.clickPlannerCanReviseTheirOwnLastNote();
		cloudAdmin.selectYesOrNo("No");
		hrSoftPage.proxyAs(Constants.CompManagerUserName);
		hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.getcolumnToNumber("2", notesHeader);
		planningPage.doWait(2000);
		planningPage.clickIndividualNotes();
		String str1 = planningPage.isNotesHistory();
		planningPage.enterIndividualNotes("test");
		info("entered individual notes");

		planningPage.clickIndividualNotes();
		planningPage.enterIndividualNotes("test");
		planningPage.clickIndividualNotes();
		String str2 = planningPage.isNotesHistory();
		planningPage.closePopUp();
		assertNotEquals(str1.length(), str2.length());
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_18_CompViewPlannerCanResetEnteredAwardAndChangedJob(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		cloudAdmin.clickRolesAndPermissionManagement(data.getApplicationURL()).selectRoleId("COMP_MGR")
				.clickSystemAssigned().selectPermissionsAndSave("PLANNING_CAN_SUBMIT_AND_WITHDRAW")
				.clickSynchronizeUmeiRoles();
		login.loadUrl(data.getApplicationURL());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.getcolumnToNumber("4", "WS_Component_Actual_Amount@"+Constants.componentId);
		planningPage.clickResetAwards();
		String before = planningPage.getAwardActualAmount();
		planningPage.clickSetPreferencesInPlanningPage();
		planningPage.enableColumnFromPreferences("WS_Job_Code");
		planningPage.searchJobWithJobTitle("Quality").clickSelect().openJobsearchAndPromotionPage();
		assertTrue(planningPage.isRevertPromotion());
		planningPage.closePopup().enterAwardActualAmountValue("40").clickResetAwards();
		String after = planningPage.getAwardActualAmount();
		planningPage.openJobsearchAndPromotionPage();
		assertFalse(planningPage.isRevertPromotion());
		assertEquals(before, after);
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_19_CompView_PlannerCanResetEnteredAwardAndChangedJob(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		if (planningPage.isWithDrawPresent())
			planningPage.clickWithDraw();
		assertTrue(planningPage.isAwardField());
		planningPage.clickRollup();
		planningPage.clickSubmitRollUp();
		assertFalse(planningPage.isAwardField());
		hrSoftPage.cancelProxy();
		CloudAdminPage cloudAdmin = hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickManageUsers(data.getApplicationURL()).searchNameInManageUsers(Constants.managerwithAwardLink);
		cloudAdmin.assignNewRoleToUser(Constants.PlanningCanEditWithDrawableGroup);
		DriverManager.getDriver().get(data.getApplicationURL());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink).openPlanRecommendations(Constants.compPlanId).clickAdvanced()
				.selectGroup().clickApply();
		assertTrue(planningPage.isAwardField());

	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_24_CompView_ChangeJob(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.clickCloudAdmin().clickEnvironmentalOptions().clickusePromotionAdjustmentScreen()
				.selectYesOrNo("Yes");
		hrSoftPage.proxyAs(Constants.CompManagerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.clickSetPreferencesInPlanningPage();
		planningPage.enableColumnFromPreferences("WS_Job_Code");
		planningPage.searchJobWithJobTitle(Constants.JobRecommendation);
		assertTrue(planningPage.selectJobAndAssertIfElementsDisplayed());
		planningPage.closeJobPromotionPopup();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_25_CompView_ChangeJob(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickEnvironmentalOptions().clickusePromotionAdjustmentScreen().selectYesOrNo("No");
		hrSoftPage.proxyAs(Constants.CompManagerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.clickSetPreferencesInPlanningPage();
		planningPage.enableColumnFromPreferences("WS_Job_Code");
		planningPage.searchJobWithJobTitle("Quality");
		assertFalse(planningPage.selectJobAndAssertIfElementsDisplayed());
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_38_39_CompView_PlannerCanResetEnteredAwardAndChangedJob(TestDataExcel data) {
		String value = "0.2";
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword())
		        .closeBurgermenu ();
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.clickSalaryTab(Constants.salaryProgramName);
		planningPage.getcolumnToNumber("5", "WS_Component_Actual_Amount@"+Constants.componentId);
		planningPage.clickResetAwards();
		String oldvalue = planningPage.getAwardActualAmount();
		String beforeMeritActual = planningPage.getBudgetMeritAmountSpent();
		planningPage.enterAwardActualAmountValue(value);
		String afterMeritActual = planningPage.getBudgetMeritAmountSpent();
//		assertEquals(Float.valueOf(afterMeritActual),
//				(Float.valueOf(beforeMeritActual) + Float.valueOf(value) - Float.valueOf(oldvalue)), 0.1);
		planningPage.clickResetAwards();
		String MeritActual = planningPage.getBudgetMeritAmountSpent();
		assertEquals(beforeMeritActual, MeritActual);
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_82_83_Compensation_CompOverview_expandCollapseCompensationTaskLinks(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		CompOverviewPage overviewPage = hrSoftPage.clickCompOverview();
		overviewPage.numberOfItemsForEachPlan().expandCollapseCompTasksLink().expandCollapseCompTasksLink()
				.clickMinimize().clickMaximize();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_85_Compensation_CompOverview_openAnnouncementsPopup(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		CompOverviewPage overviewPage = hrSoftPage.clickCompOverview();
		overviewPage.clickCompAnnouncementLink().closePopup();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_86_Compensation_CompOverview_openMessagesPopup(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		CompOverviewPage overviewPage = hrSoftPage.clickCompOverview();
		overviewPage.clickCompMessageLink().closePopup();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_87_CompensationCompOverview_OpenCompTasksLinks(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		//hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		CompOverviewPage overviewPage = hrSoftPage.clickCompOverview();
		List<WebElement> taskLinks = overviewPage.compTasksLinks();
		for (int i = 0; i < taskLinks.size(); i++) {
			System.out.println(taskLinks.size());
			if (i == 2) {
				break;
			}
			overviewPage.clickTaskLink();
			hrSoftPage.clickCompOverview();
		}
		hrSoftPage.logOut();

	}

//	@Author(name = QAResources.SRINIVAS)
//	@Test(groups = { TestGroups.SMOKE,
//			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
//	public void SM_09C_CompViewPlanning_CanRevertPLanToConfigureStage(TestDataExcel data) {
//		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
//		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
//		hrSoftPage.clickCloudAdmin();
//		ManagePlansPage managePlans = new CloudAdminPage().navigateToManagePlans(data.getApplicationURL());
//		managePlans.clickStagingTab().clickProgramsCheckBoxInComponentSection();
////        managePlans.clickOpenButtonForPlan ("Edward 2022 Plan");
//		// if (managePlans.getPlanningStage ().equalsIgnoreCase ("planning")) {
//		// managePlans.clickRevertInStaging ();
//		// }
//
//	}
}
