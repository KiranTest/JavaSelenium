package com.hrsoft.test.compensation;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static com.hrsoft.constants.Constants.CSV_SUMMARY_FILE;
import static com.hrsoft.reports.ExtentLogger.info;
import static com.hrsoft.reports.ExtentLogger.pass;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.config.ConfigFactory;
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
import com.hrsoft.gui.compensation.EmployeeDetailPage;
import com.hrsoft.gui.compensation.PlanningPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

/**
 * @author Annameni Srinivas <a href=
 *         "mailto:annameni.srinivas@hrsoft.com">annameni.srinivas@hrsoft.com</a>
 */
public class PlanningPageTestSuite extends WebBaseTest {

	private EmployeeDetailPage empDetailPage = new EmployeeDetailPage();
	String rand = RandomStringUtils.randomNumeric(2);
	String meritActualPercent = "WS_Component_Actual_Percent@" + Constants.componentId;
	String actualAmount = "WS_Component_Actual_Amount@" + Constants.componentId;

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_108_PlanningPage_NavigateToPlanningPage(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_109_PlanningPage_SwitchBetweenRollupAndDirect(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.clickRollup().clickDirect();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_110_111_PlanningPage_SearchGroupInAdvanced(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.clickAdvanced().clickSearchGroup().searchGroupByName(Constants.GroupName).clickApply()
				.clickAdvanced().clickReset();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_112_113_PlanningPage_CanSubmitAndWithDrawPlan(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.clickSubmit().clickWithDraw();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_114_PlanningPage_CanResetAwards(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.getcolumnToNumber("5", meritActualPercent);
		planningPage.clickResetAwards();
		String before = planningPage.getTotalAwardActualPercent();
		planningPage.enterMeritActualAmountPercent("1000");
		planningPage.clickResetAwards();
		String after = planningPage.getTotalAwardActualPercent();
		assertEquals(before, after);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_115_PlanningPage_CanDownloadCsv(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		// planningPage.clickSetPreferencesInPlanningPage().enablePrint();
		planningPage.downloadCsv();
		doWait(2000);
		assertTrue(planningPage.isFileDownloaded(CSV_SUMMARY_FILE));
		pass(CSV_SUMMARY_FILE + " file downloaded");
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_116_PlanningPageTest_ChangeColumnOrder(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		info("Logged in and proxied as admin user");
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.clickPlanningGroupEditButton().changeColumnOrder("WS_Display_Name", "WS_Alert");
		info("changed column orders");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_117_PlanningPage_IsTopAlertPresent(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.clickAlertOnTop().isalertPopUpPresent();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_118_PlanningPage_CanEnterGroupNotes(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.clickGroupNotes().enterGroupNotesAndSave("automation test");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_119_PlanningPage_CanShowAndHideCurrency(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.clickCurrencyPayRate().clickHideAndSaveCurrency();
		assertFalse(planningPage.isCurrencyRateVisible());
		planningPage.clickCurrencyPayRate().clickShowAndSaveCurrency();
		assertTrue(planningPage.isCurrencyRateVisible());
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_120_PlanningPage_CanSelectFilter(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.clickAdvanced().selectGroup().clickApply();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_121_To_SM_123_PlanningGrid_Verifications(TestDataExcel data) {
		String columnNameTypeCode = "WS_Display_Name";
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.getcolumnToNumber("1", columnNameTypeCode);

		planningPage.clickFilterIcon(columnNameTypeCode).sortByDescending(columnNameTypeCode);
		info("sorted column in descending order");

		planningPage.clickFilterIcon(columnNameTypeCode).sortByAscending(columnNameTypeCode);
		info("sorted column in Ascending order");// SM-121
		planningPage.clickFilterIcon(columnNameTypeCode).setFilter(columnNameTypeCode, "A%");// SM-122
		info("applied filter");

		planningPage.clickFilterIcon(columnNameTypeCode).removeFilter(); // SM-123
		info("removed filter");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_124_To_SM_126_PlanningGrid_Verifications(TestDataExcel data) {
		String columnNameTypeCode = "WS_Display_Name";
		String newColumnName = "Name-test";
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.clickSummaryTab();
		planningPage.rightClickOnColumnHeader(columnNameTypeCode).editColumnNameTo(newColumnName) // SM-124
				.refreshInnerFrame();
		info("edited column name");
		planningPage.rightClickOnColumnHeader(columnNameTypeCode).resetColumnNameToDefault().refreshInnerFrame();
		info("column name set to default");
		planningPage.clickSummaryTab();
		planningPage.clickEmployeeNameLink(Constants.CoPlannerName).isEmployeeDetailsPage(); // SM-125
		info("Employee details pop is visible");
		planningPage.clickFirstTab();
		planningPage.getcolumnToNumber("2", "WS_Notes");
		planningPage.doWait(2000);
		planningPage.clickSummaryTab();
		planningPage.clickIndividualNotes().enterIndividualNotes("individual notes test");
		info("entered individual notes"); // SM-126
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_127_PlanningGrid_TccCompensationHistory(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.doWait(2000);
		planningPage.getCompensationHistoryTCCTo("3").clickCompensationHistoryTCC(Constants.CompManagerFullName)
				.isCompensationHistoryTcc();
		info("compensation history TCC is visible");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_128_PlanningGrid_NonTccCompensationHistory(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.withdrawIfPlanIsSubmitted();

		planningPage.doWait(2000);
		planningPage.getCompensationHistorynonTCCTo("5").clickCompensationHistoryNonTCC().isCompensationHistoryNonTCC();
		info("compensation history non-TCC is visible");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_129_PlanningGrid_NonTccCompensationHistory(TestDataExcel data) {// not done yet
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.withdrawIfPlanIsSubmitted();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_130_PlanningGrid_SpareFieldHistoryChange(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.withdrawIfPlanIsSubmitted();

		planningPage.clickSpareFieldChange(Constants.ReviewingManagerFullName).isSpareFieldChangeAudit(); // SM-130
		info("spare field changes are visible");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_131_PlanningGrid_JobSearchAndPromotionPage(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.getcolumnToNumber("11", "WS_Job_Code").clickInlineJob().isInlineJobSearch();
		info("Inline job is visible");// SM-131
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_132_PlanningGrid_InlineResetAwardIcon(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.getcolumnToNumber("14", "WS_Emp_Action").clickResetAwardsAction();
		info("Reset awards section");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_133_PlanningGrid_AwardEntryAudit(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		hrSoftPage.proxyAs(Constants.CoPlannerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.getcolumnToNumber("12", "WS_Transaction_Audit_TCC").clickAwardEntryHistory().isAwardEntryAudit();
		info("Award entry audit is visible");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_134_SM_135_PlanningGrid_MeritActualAmountAndPercent(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.clickResetAwardsAction().getcolumnToNumber("14", meritActualPercent);
		String totalMeritPercentBefore = planningPage.getTotalAwardActualPercent();
		String totalMeritValueBefore = planningPage.getTotalAwardActualValue();
		String meritActualBefore = planningPage.getAwardActualAmount();
		info("merit actual value before:" + meritActualBefore);
		info("total merit actual percent before:" + totalMeritPercentBefore);
		info("total merit actual value before:" + totalMeritValueBefore);
		planningPage.enterMeritActualAmountPercent("1000"); // SM-134

		doWait(3000);
		String totalMeritPercentAfter = planningPage.getTotalAwardActualPercent();
		String totalMeritValueAfter = planningPage.getTotalAwardActualValue();
		String meritActualAfter = planningPage.getAwardActualAmount();
		info("merit actual value after:" + meritActualAfter);
		info("total merit actual percent after:" + totalMeritPercentAfter);
		info("total merit actual value after:" + totalMeritValueAfter);
		assertNotEquals(totalMeritValueBefore, totalMeritValueAfter);
		assertNotEquals(totalMeritPercentBefore, totalMeritPercentAfter);
		assertNotEquals(meritActualBefore, meritActualAfter); // SM-135
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_136_PlanningGrid_Verifications(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.getcolumnToNumber("14", actualAmount);
		planningPage.clickResetAwards();
		String budgetMeritBefore = planningPage.getBudgetMeritPercentageAmountSpent();
		info("budget merit before value" + budgetMeritBefore);
		planningPage.enterAwardActualAmountValue("100");
		doWait(3000);
		String budgetMeritAfter = planningPage.getBudgetMeritPercentageAmountSpent();
		info("budget merit after value" + budgetMeritAfter);
		assertNotEquals(budgetMeritBefore, budgetMeritAfter);// SM-136
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_137_PlanningGrid_Verifications(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.getcolumnToNumber("14", actualAmount).clickMeritActualHistoryIcon() // SM-137
				.isAuditAwardHistory();
		info("Audit award history is visible");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_138_PlanningGrid_ColorChangeToGrey(TestDataExcel data) { // SM-138
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.getcolumnToNumber("15", meritActualPercent);
		planningPage.enterMeritActualAmountPercent("3");
		hrSoftPage.cancelProxy();
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId).getcolumnToNumber("15",
				meritActualPercent);
		info(planningPage.getCssMeritActual());
		assertEquals(planningPage.getCssMeritActual(), "#dc3545");
		planningPage.enterMeritActualAmountPercent("5");
		info(planningPage.getCssMeritActual());
		planningPage.doWait(3000);
		assertEquals(planningPage.getCssMeritActual(), "#d8d8d8");
		planningPage.clickMeritActualHistoryIcon().isAuditAwardHistory();
		hrSoftPage.logOut();
	}

	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_139_PlanningGrid_Verifications(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.getcolumnToNumber("15", "WS_Component_Calculated_Amount_Base@" + Constants.componentId)
				.clickMeritCalculatedAmount().isCalculationDetails(); // SM-139
		hrSoftPage.logOut();
	}

	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_140_PlanningGrid_Verifications(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.clickResetAwards().enterAwardActualAmountValue("1000").getcolumnToNumber("14", "WS_Alert")
				.clickAlert().isalertPopUpPresent(); // SM-140
		info("Alert is visible");
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_16_CompViewPlanningPage_PlanningPaginationAndScrolling(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		CloudAdminPage cloudAdmin = hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickEnvironmentalOptions().clickEnableSpreadsheetScrolling().selectYesOrNo("No");

		hrSoftPage.proxyAs(Constants.TopPlannerUserName);

		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.clickExpandOrCollapseButotn().clickAdvanced().selectGroup().clickApply();
		assertTrue(planningPage.isPaginationPresent());
		hrSoftPage.cancelProxy();
		hrSoftPage = planningPage.goToHrSoftPage();
		cloudAdmin = hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickEnvironmentalOptions().clickEnableSpreadsheetScrolling().selectYesOrNo("Yes");

		planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.clickExpandOrCollapseButotn().clickAdvanced().selectGroup().clickApply();
		assertFalse(planningPage.isPaginationPresent());
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_141_143_CompensationEmployeeDetails_VerifyingEmployeeDetails(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		hrSoftPage.proxyAs(Constants.CompManagerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		EmployeeDetailPage empDetailsPage = planningPage.clickFirstEmployee();
		empDetailsPage.clickSummaryTab();
		empDetailPage.clickEmpDataPreferencesIcon();
		planningPage.enableColumnFromPreferences("WS_Display_Name");
		empDetailsPage.clickPrevEmployee();
		String prevEmpName = empDetailsPage.getEmpNameWithPrevIcon(); // for prev icon
		String prevEmpInDataSection = empDetailsPage.getPrevIconEmpInEmpDataSection();
		assertEquals(prevEmpName, prevEmpInDataSection);
		info("Verified Emp Data with Previous Next Icon");
		doWait(2000); // For Next Icon
		empDetailPage.clickNextEmployee();
		String nextEmpName = empDetailsPage.getEmpNameWithNextIcon();
		String nextEmpInDataSection = empDetailsPage.getNextIconEmpInEmpDataSection();
		assertEquals(nextEmpName, nextEmpInDataSection);
		info("Verified Emp Data with Next Icon");
		doWait(2000);
		empDetailsPage.selectAnyEmpFromDropDown();// For DropDown
		String dropDownEmpName = empDetailsPage.getEmpNameSelectedFromDropDown();
		String DropDownEmpInDS = empDetailsPage.getEmpNameInDataSectionForSelectedDropDown();
		assertEquals(dropDownEmpName, DropDownEmpInDS);
		System.out.println("Verified Emp Data with Dropdown");
		empDetailPage.addIndividualNote("Test Add Notes").closeEmpDetailsPopup();

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_144_verifyPerfRating(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		hrSoftPage.proxyAs(Constants.CompManagerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		EmployeeDetailPage empDetailsPage = planningPage.clickFirstEmployee();
		empDetailsPage.clickSummaryTab();
		empDetailPage.clickEmpDataPreferencesIcon();
		planningPage.enableColumnFromPreferences("WS_Display_Name");
		empDetailPage.clickNewPerfRate();
		String actualPerfRate = empDetailPage.getPerfRating(1);
		String expectedPerfRate = empDetailPage.selectedPerfShownInGrid();
		assertEquals(actualPerfRate, expectedPerfRate);
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM146CompensationEmployeeDetails_VerifyCalculationDetailPopupIsVisible(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.refresh();
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId2);
		planningPage.withdrawIfPlanIsSubmitted();
		System.out.println("Employee With Salary Component : " + Constants.employeeWithAwardLink);
		if (Constants.employeeWithAwardLink == null)
			throw new org.testng.SkipException("There is no employee who has a salary component for the manager :"
					+ Constants.managerwithAwardLink);
		EmployeeDetailPage empDetailsPage = planningPage.clickAnyEmployee(Constants.employeeWithAwardLink);
		System.out.println(empDetailsPage);
		empDetailsPage.selectSalaryProgram(Constants.salaryProgramName);
		empDetailsPage.clickComponent().isCalculationPopupPresent();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM147_CompensationEmployeeDetails_VerifyingEmployeeAwardAmountValue(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId2);
		planningPage.withdrawIfPlanIsSubmitted();
		System.out.println("Employee With Salary Component : " + Constants.employeeWithAwardLink);
		if (Constants.employeeWithAwardLink == null)
			throw new org.testng.SkipException(
					"There is no employee who has a salary component for the manager :" + Constants.TopPlannerUserName);
		EmployeeDetailPage empDetailsPage = planningPage.clickFirstEmployee();
		System.out.println(empDetailsPage);
		empDetailsPage.selectSalaryProgram(Constants.salaryProgramName);
		String previousActualAmount = empDetailsPage.getActualAmountValue(0, Constants.salaryProgramId);
		System.out.println("previousActualAmount " + previousActualAmount);
		empDetailsPage.enterActualAmtPercent(0, Constants.salaryProgramId, rand);
		String newActualAmount = empDetailsPage.getActualAmountValue(0, Constants.salaryProgramId);
		System.out.println("newActualAmount : " + newActualAmount);
		assertNotEquals(previousActualAmount, newActualAmount);
		hrSoftPage.logOut();

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_148_CompensationEmployeeDetails_IsEmpSpareHistoryReportVisible(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.CompManagerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.clickSetPreferencesInPlanningPage();
		planningPage.enableColumnFromPreferences("WS_Emp_Spares_Audit_TCC");
		List<WebElement> spareHistory = planningPage.empSpareHistory();
		for (int i = 0; i < spareHistory.size(); i++) {
			if (i == 4) {
				break;
			}
			if (!spareHistory.isEmpty()) {
				WebElement ele = spareHistory.get(i);
				if (ele.isDisplayed()) {
					ele.click();
					planningPage.isHistoryPresent();
					doWait(2000);
					planningPage.closeEmpSpareHistoryPopup();
					break;
				}
			}
		}
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_149_CompensationJobSearch_SearchJobsBasedOnCriterias(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted().clickSetPreferencesInPlanningPage();
		planningPage.enableColumnFromPreferences("WS_Job_Code");
		planningPage.openJobsearchAndPromotionPage().searchJobWithSearchBtn().closePopup();
		int rand = planningPage.getRandomValue();
		String selectedJobFamily = planningPage.searchWithJobFamily(rand);
		String verifyJobFamily = planningPage.verifyJobFamily();
		info("selected Job Family : " + selectedJobFamily);
		if (selectedJobFamily.contains("<Any>")) {
			verifyJobFamily = "UNASSIGNED";
		}
		assertEquals(selectedJobFamily, verifyJobFamily);
		String selectedJobGrade = planningPage.searchJobWithJobGrade(rand);
		String verifyJobGrade = planningPage.verifyJobGrade();
		System.out.println("selectedJobGrade : " + selectedJobGrade);
		System.out.println("verifyJobGrade : " + verifyJobGrade);
		assertEquals(selectedJobGrade, verifyJobGrade);
		planningPage.searchJobWithJobTitle(Constants.JobRecommendation);
		String verifyJobTitle = planningPage.verifyJobtitle();
		info("Job Title verified " + verifyJobTitle);
		assertEquals(Constants.JobRecommendation.trim(), verifyJobTitle.trim());
		hrSoftPage.logOut();
	}

	private void enablePromotionAdjustmentScreen() {
		HRSoftPage hrSoftPage = new HRSoftPage();
		CloudAdminPage cloudAdmin = hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickEnvironmentalOptions().editPromotionAdjustMent().enablePromotionAdjustmentOption("Yes")
				.savePromotionAdjustmentOption();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_150_152_CompensationJobRecommendation_VerifyJobRecommendationAndRevertingFunctionality(
			TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		enablePromotionAdjustmentScreen();
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted().clickSetPreferencesInPlanningPage();
		planningPage.enableColumnFromPreferences("WS_Job_Code");
		planningPage.openJobsearchAndPromotionPage().searchJobWithSearchBtn();
		List<WebElement> jobNamesInResultGrid = planningPage.getJobGridResultTbl();
		for (WebElement row : jobNamesInResultGrid) {
			String selectedJobName = planningPage.selectedJobName(row);
			if (selectedJobName.contains(Constants.JobRecommendation)) {
				info("Selected Job Name :" + selectedJobName);
				planningPage.selectJob(row).closeJobPromotionPopup().openJobsearchAndPromotionPage();
				String proposedJob = planningPage.getProposedJob();
				info("Proposed Job " + proposedJob);
				assertEquals(selectedJobName, proposedJob);
				planningPage.revertPromotion();
				break;
			}
		}
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_155_Compensation_displayCorrectReportsAtSupportingDataSection(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		// hrSoftPage.proxyAs(Constants.HRAdminUserName);
		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.switchToSupportingFrame();

		planningPage.clickCompaRatioTab();
		planningPage.isChartXaxisPresent();
		planningPage.isChartYaxisPresent();
		planningPage.clickPerformanceDistributionTab();
		planningPage.isChartXaxisPresent();
		planningPage.isChartYaxisPresent();
		planningPage.clickSalaryComparisonTab();
		planningPage.isChartXaxisPresent();
		planningPage.isChartYaxisPresent();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_31_CompViewPlanningPage_ShowOnlyEligibleEmployees(TestDataExcel data) {
		int planId = 153;
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		hrSoftPage.proxyAs(Constants.managerwithAwardLink);
		PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.clickSalaryTab(Constants.salaryProgramName);
		planningPage.getcolumnToNumber("6", "Job");
		String inlineJobCode = planningPage.getInlineJobCode();

		ManagePlansPage managePlans = new CloudAdminPage().navigateToManagePlans(data.getApplicationURL());
		managePlans.clickOpenButtonForPlan(153).clickProgramsTab().clickEditButtonForSalary()
				.clickCheckBoxShowOnlyEligibleEmployeesInProgramViews().clickSave().clickComponentsTab()
				.selectSalaryFromSelectProgram();
		List<String> listOfComponents = managePlans.getListOfComponents();
		for (String eachComponent : listOfComponents) {
			managePlans.clickEditButtonFor(eachComponent).clickEnterFormulaRadioButton().clickEnterFormulaButton()
					.enterFormulaInFormulaTextBox(String.format("EMP_JOB_CODE = \"%s\"", inlineJobCode))
					.clickCheckStatus();
			if (managePlans.getStatusText().equals("OK"))
				managePlans.clickSaveInPopUp().clickSave();
		}
		managePlans.clickStagingTab().clickProgramsCheckBoxInComponentSection()
				.clickCalculateSelectedComponentsAndClickOk();
		DriverManager.getDriver().get(getConfig().webAppicationURL());
		hrSoftPage.openPlanningPageFor(planId, Constants.managerwithAwardLink);
		planningPage.clickSalaryTab(Constants.salaryProgramName);
		hrSoftPage.logOut();
	}

	// private void revertChangesForSM_31 () {
	// new CloudAdminPage ().navigateToManagePlans ();
	// ManagePlansPage managePlans = new ManagePlansPage ().clickOpenButtonForPlan
	// ("Edward 2022
	// Plan")
	// .clickProgramsTab ()
	// .clickEditButtonForSalary ()
	// .clickCheckBoxShowOnlyEligibleEmployeesInProgramViews ()
	// .clickSave ()
	// .clickComponentsTab ()
	// .selectSalaryFromSelectProgram ();
	// List <String> RevertListOfComponents = managePlans.getListOfComponents ();
	// for (String eachComponent : RevertListOfComponents) {
	// managePlans.clickEditButtonFor (eachComponent)
	// .clickAlwaysEligibleRadioButton ()
	// .clickSave ();
	// }
	// managePlans.clickStagingTab ()
	// .clickProgramsCheckBoxInComponentSection ()
	// .clickCalculateSelectedComponentsAndClickOk ();
	//
	// }

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_129_Compensation_VerifyEmpHistoryColumnAndHistoryAreVisible(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		CloudAdminPage cloudAdmin = new CloudAdminPage();
		ManagePlansPage managePlan = cloudAdmin.navigateToManagePlans(data.getApplicationURL());
		managePlan.clickOpenButtonForPlan(Constants.compPlanId);
		managePlan.clickSpareFieldsTab();
		managePlan.selectAllUsersOption(Constants.empSpareId).plannerCanEdit(Constants.empSpareId).saveEmpSpareFields();
		hrSoftPage.navigateTo(data.getApplicationURL()
				+ "content/portal/tccserver/loadApp.htm?wizinstanceid=C1ADF74F-08C7-4E40-8321-ED6CC20280CB&paramhash=F90E70DE-34B9-4803-9220-0A455585E645");
		hrSoftPage.refresh();
		hrSoftPage.clickCloudAdmin().clickManageUserDefinedSpareFields()
				.searchAndEnableSpareFieldForAudit(Constants.empSpareId).refresh();
		System.out.println(Constants.compPlanId);
		System.out.println(Constants.TopPlannerFullName);
		PlanningPage planningPage = hrSoftPage.openPlanningPageFor(Constants.compPlanId, Constants.TopPlannerFullName);
		planningPage.withdrawIfPlanIsSubmitted();
		planningPage.clickPlanningGroupEditButton().clickSetMyPreferences()
				.checkIfAddedEmpSpareFieldIsEnabled(Constants.empSpareName).clickSummaryTab()
				.isSelectedEmpSpareColumnVisible(Constants.empSpareId);
		planningPage.enterValueForHistory(Constants.empSpareId, rand).clickHistoryIcon(Constants.empSpareId)
				.closeHistoryPopup();
		hrSoftPage.logOut();

	}
	@Test
	public void test() {
		new LoginPage().loadUrl(ConfigFactory.getConfig().webAppicationURL())
		.enterUsername(ConfigFactory.getConfig().userName()).enterPassword(ConfigFactory.getConfig().password())
		.clickLogin();
	}
}
