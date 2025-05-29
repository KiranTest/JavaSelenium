package com.hrsoft.test.compensation;

import static com.hrsoft.constants.Constants.FILTER;
import static com.hrsoft.reports.ExtentLogger.info;
import static com.hrsoft.reports.ExtentLogger.pass;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.cloudadmin.CloudAdminPage;
import com.hrsoft.gui.cloudadmin.ManagePlansPage;
import com.hrsoft.gui.compensation.PlanOverviewPage;
import com.hrsoft.gui.compensation.PlanningPage;
import com.hrsoft.gui.compensation.ReviewingPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */
@Test(groups = "smoke")
public class PlanOverviewPageTestSuite extends WebBaseTest {
	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_95_PlanOverViewPage_NavigateToPlanOverview(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserName);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView);
		planOverviewPage.navigatedPlanToOverviewPage();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_96_PlanOverViewPage_SearchAndAcceptGroup(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView);
		planOverviewPage.navigatedPlanToOverviewPage();
		planOverviewPage.clickSearchAndChangeGroup().selectGroup(Constants.GroupName);
		info(planOverviewPage.getDropdowntext());
		assertEquals(planOverviewPage.getDropdowntext(), Constants.GroupName + " Grp");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_97_PlanOverViewPage_CanResetToDefaultgroup(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserName);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView);
		planOverviewPage.navigatedPlanToOverviewPage();
		planOverviewPage.clickReset();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_98_PlanOverViewPage_NavigateToManageFilter(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserName);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView);
		planOverviewPage.navigatedPlanToOverviewPage();
		planOverviewPage.clickManageFilters();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_99_PlanOverViewPage_NavigateToDistributeFundPage(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		if (Constants.reviewManagerUserNameWithDistLink == null)
			throw new org.testng.SkipException("No Manager has Distributed Funds");
		hrSoftPage.proxyAs(Constants.reviewManagerUserNameWithDistLink);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView);
		planOverviewPage.navigatedPlanToOverviewPage();
		planOverviewPage.clickDistibuteFundsLink();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_100_PlanOverViewPage_NavigateToPlanRecomendations(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		if (Constants.reviewManagerUserNameWithDistLink == null)
			throw new org.testng.SkipException("No Manager has Distributed Funds");
		hrSoftPage.proxyAs(Constants.reviewManagerUserNameWithDistLink);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView);
		planOverviewPage.navigatedPlanToOverviewPage();
		planOverviewPage.clickPlanRecomendations();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_101_PlanOverViewPage_NavigateToReviewRecomendations(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		if (Constants.reviewManagerUserNameWithDistLink == null)
			throw new org.testng.SkipException("No Manager has Distributed Funds");
		hrSoftPage.proxyAs(Constants.reviewManagerUserNameWithDistLink);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView);
		planOverviewPage.navigatedPlanToOverviewPage();
		planOverviewPage.clickReviewRecomendations();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_102_PlanOverViewPage_NaviagteToPlanningPageWithSelectedFilter(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		if (Constants.reviewManagerUserNameWithDistLink == null)
			throw new org.testng.SkipException("No Manager has Distributed Funds");
		hrSoftPage.proxyAs(Constants.reviewManagerUserNameWithDistLink);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView);
		planOverviewPage.navigatedPlanToOverviewPage();
		planOverviewPage.selectFilterInSelections(FILTER).clickRollup().clickPlanRecomendations();
		assertTrue(planOverviewPage.isPlanningPageWithSelectedFilter(FILTER));
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_103_PlanOverViewPage_EmployeesUNderReviewTabAndPlanReview(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.TopPlannerUserName);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView);
		planOverviewPage.navigatedPlanToOverviewPage();
		planOverviewPage.clickReviewingInStatusTab();
		String reviewStatusCount = planOverviewPage.getEmployeeCountFromReviewingStatusTab();
		planOverviewPage.clickReviewRecomendations();
		String reviewPageCount = planOverviewPage.getEmployeeCount(Constants.CompManagerFullName + " Grp");
		assertEquals(reviewPageCount, reviewStatusCount);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_104_PlanOverViewPage_ShowEmployeesWithSelectedFilter(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserName);
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView); // SM-95
		planOverviewPage.navigatedPlanToOverviewPage();
		planOverviewPage.clickPlanningStatusInStatusTab();
		String planningStatusCount = planOverviewPage.getEmployeeCountFromPlanningStatusTab();
		planOverviewPage.clickPlanRecomendations();
		String planningPageCount = new PlanningPage().getEmployeeCount();
		assertEquals(planningStatusCount, planningPageCount);// SM-104
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_105_PlanOverView_CascadingBudgetVisible(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView); // SM-95
		planOverviewPage.clickFundingTab();
		assertTrue(planOverviewPage.isCascadingBudgetVisible());
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_106_PlanOverView_RuleBasedBudgets(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		PlanOverviewPage planOverviewPage = hrSoftPage.openCompensationPlanOverviewPage(Constants.compPlanId,
				Constants.PlanForManagerView); // SM-95
		planOverviewPage.clickPlanningBudget();
		assertTrue(planOverviewPage.isRuleBasedBudgets());
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_107_PlanOverView_PageChangeColumnOrder(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());

		hrSoftPage.proxyAs(Constants.ReviewingManagerUserName);
		ReviewingPage reviewingPage = hrSoftPage.openReviewRecommendations(Constants.compPlanId);
		pass("Navigated to Comp reviewing page for " + Constants.TopPlannerGrp);
		reviewingPage.changeColumnOrder("WS_Action", "WS_Alert");
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_13_CompensationPrintCompDocument(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		CloudAdminPage cloudAdmin = hrSoftPage.clickCloudAdmin();
		ManagePlansPage managePlan = cloudAdmin.navigateToManagePlans(data.getApplicationURL());
		managePlan.clickOpenButtonForPlan(Constants.compPlanId);
		managePlan.clickPrintBtn();
		String expectedTitle = "Plan Configuration Requirement";
		String actualTitle = managePlan.getCompensationDocPageTitle();
		System.out.println("Actual Title " + actualTitle);
		assertEquals(actualTitle, expectedTitle);
		doWait(1000);
		managePlan.navigateToTab(0);
		managePlan.navigateBack();
		managePlan.navigateBack();
		hrSoftPage.logOut();
	}
}
