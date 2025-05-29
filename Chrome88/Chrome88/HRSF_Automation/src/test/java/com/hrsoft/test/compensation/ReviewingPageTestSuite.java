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
import com.hrsoft.db.CompViewDbHelper;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.cloudadmin.CloudAdminPage;
import com.hrsoft.gui.compensation.PlanningPage;
import com.hrsoft.gui.compensation.ReviewingPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */
public class ReviewingPageTestSuite extends WebBaseTest {

	private PlanningPage planningPage = new PlanningPage(); // For switching to frames
	private CloudAdminPage cloudAdmin = new CloudAdminPage();
	private CompViewDbHelper dbHelper = new CompViewDbHelper();

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_156_To_SM_158_Compensation_SearchGroupFromAdvanceFiltersAndRevert(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserNameWithRollup);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		reviewPage.clickAdvancedBtn().selectAnyGroup();
		reviewPage.clickApply();
		reviewPage.clickAdvancedBtn();
		String selectedGrp = reviewPage.getSelectedGroup() + " Grp";
		String topLevelGrp = reviewPage.getTopLevelGroup();
		assertEquals(selectedGrp, topLevelGrp);
		System.out.println("Top Level Grp : " + topLevelGrp);
		reviewPage.clickAdvancedBtn().clickReset();
		String topLevelGrpAfterReset = reviewPage.getTopLevelGroup();
		String loggedinGrp = reviewPage.loggedInGroup() + " Grp";
		System.out.println("Loggedin  Grp" + loggedinGrp);
		System.out.println("Top Level Grp After Reset : " + topLevelGrpAfterReset);
		assertEquals(loggedinGrp, topLevelGrpAfterReset);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_159_Compensation_ClickAlertIconToVerifyPopupAppears(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserNameWithRollup);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		reviewPage.clickAlertIcon().closeAlertPopup();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_163_To_SM_165_Compensation_AddNotesAndVerifyLinksNavigatingToCorrectPages(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserNameWithRollup);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		reviewPage.expandGrid().scrollBottom().addNotes("Notes");
		reviewPage.clickGroupNameLink();
		reviewPage.isNavigatedToPlanningPage(Constants.planNameForReviewingManagereWithRollup, Constants.ReviewingManagerFullNameWithRollup);
		reviewPage.clickPreviousIcon();
		reviewPage.isNavigatedToReviewingPage(Constants.planNameForReviewingManagereWithRollup, Constants.ReviewingManagerFullNameWithRollup);
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_166_CompensationReview_perfromDrillDownDrillupOperations(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		if (Constants.ReviewingManagerUserNameWithRollup == null)
			throw new org.testng.SkipException("No Manager is present who has rollup group");
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserNameWithRollup);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		reviewPage.drillDownGroup().drillUpGroup();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_160_CompensationReview_CannotSubmitGroupWhenThereIsRedAlert(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		hrSoftPage.proxyAs(Constants.ReviewingManagerUserNameWithRollup);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		List<WebElement> redAlerts = reviewPage.redAlertGroups();
		for (int i = 0; i < redAlerts.size(); i++) {
			if (i == 2) {
				break;
			}
			WebElement x = redAlerts.get(i);
			assertTrue(reviewPage.click(x));
			reviewPage.checkRedAlerts();
		}
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM161161B_Compensation_CompensationReview_SubmitRejectRollupGroup(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		String managerWithDirectAndRollupGrp = Constants.ReviewingManagerUserNameWithRollup;
		hrSoftPage.proxyAs(managerWithDirectAndRollupGrp);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		reviewPage.submitRejectRollup().submitRejectRollup();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD) // Not working
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM161A_CompensationReview_WithdrawRollupGroupFunctionality(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		String managerWithDirectAndRollupGrp = Constants.ReviewingManagerUserNameWithRollup;
		hrSoftPage.proxyAs(managerWithDirectAndRollupGrp);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		reviewPage.withDrawRollupGroup().withDrawRollupGroup();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM162162B_CompensationReview_SubmitRejectDirectGroupFunctionality(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		String managerWithDirectAndRollupGrp = Constants.ReviewingManagerUserNameWithRollup;
		hrSoftPage.proxyAs(managerWithDirectAndRollupGrp);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		reviewPage.submitRejectDirectGrp().submitRejectDirectGrp();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM162A_CompensationReview_WithdrawDirectGroupFunctionality(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		String managerWithDirectAndRollupGrp = Constants.ReviewingManagerUserNameWithRollup;
		hrSoftPage.proxyAs(managerWithDirectAndRollupGrp);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		reviewPage.withdrawDirectGrp();
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_169_CompensationReview_VerifyBudgetValuesShouldChangeOnGroupsSwitch(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		if (Constants.ReviewingManagerUserNameWithRollup == null)
			throw new org.testng.SkipException("No Manager is present who has rollup group");
		String managerWithDirectAndRollupGrp = Constants.ReviewingManagerUserNameWithRollup;
		hrSoftPage.proxyAs(managerWithDirectAndRollupGrp);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		reviewPage.drillDownGroup();
		List<String> budgetAmountsFromGrid = reviewPage.getBudgetAmountValues();
		List<String> budgetAmountsFromBelowSection = reviewPage.getBudgetAmountsFromBudgetTab();
		info("Budget Amounts From Below Section " + budgetAmountsFromBelowSection);
		assertEquals(budgetAmountsFromGrid.size(), budgetAmountsFromBelowSection.size());
		for (int i = 0; i < budgetAmountsFromGrid.size(); i++) {
			String element1 = budgetAmountsFromGrid.get(i);
			String element2 = budgetAmountsFromBelowSection.get(i);
			info("list 1 : " + element1);
			info("list 2 : " + element2);
			assertEquals(element1, element2);
		}
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_170_CompensationReview_VerifyGroupValuesShouldChangeOnGroupsSwitch(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		if (Constants.ReviewingManagerUserNameWithRollup == null)
			throw new org.testng.SkipException("No Manager is present who has rollup group");
		String managerWithDirectAndRollupGrp = Constants.ReviewingManagerUserNameWithRollup;
		hrSoftPage.proxyAs(managerWithDirectAndRollupGrp);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		String groupBefore = reviewPage.getGroupStatusValueBeforeDrillDown();
		String groupAfter = reviewPage.getGroupStatusValueAfterDrillDown();
		System.out.println("grp before " + groupBefore);
		System.out.println("grp After " + groupAfter);
		/* Verifying group status changed */
		assertNotEquals(groupBefore, groupAfter);
		/* Verifying group value count by below chart in group status tab */
		String topGrpCount = "Groups " + reviewPage.getTopLevelGroupValueCountFromGrid();
		info("Top Group Count in grid : " + topGrpCount);
		info("Group Count in group status tab chart : " + groupAfter);
		assertEquals(topGrpCount, groupAfter);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_16_CompViewReviewPage_PlanningPaginationAndScrolling(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickEnvironmentalOptions();
		cloudAdmin.clickEnableSpreadsheetScrolling();
		cloudAdmin.selectYesOrNo("No");

		hrSoftPage.proxyAs(Constants.ReviewingManagerUserName);
		hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManager);
		hrSoftPage.switchToInnerFrame();
		planningPage.clickExpandOrCollapseButotn();
		planningPage.clickAdvanced();
		planningPage.selectGroup();
		planningPage.clickApply();
		assertTrue(planningPage.isPaginationPresent());

		planningPage.goToHrSoftPage();
		hrSoftPage.cancelProxy();
		hrSoftPage.clickCloudAdmin();
		cloudAdmin.clickEnvironmentalOptions();
		cloudAdmin.clickEnableSpreadsheetScrolling();
		cloudAdmin.selectYesOrNo("Yes");

		hrSoftPage.proxyAs(Constants.ReviewingManagerUserName);
		hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManager);
		planningPage.clickExpandOrCollapseButotn();
		planningPage.clickAdvanced();
		planningPage.selectGroup();
		planningPage.clickApply();
		assertFalse(planningPage.isPaginationPresent());
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_167_CompensationVerifyTopGrpValueEqualsToSumOfBelowGrpValues(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		String managerWithDirectAndRollupGrp = Constants.ReviewingManagerUserNameWithRollup;
		hrSoftPage.proxyAs(managerWithDirectAndRollupGrp);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		int topLevelManagerCount = reviewPage.getTopLevelManagerGroupsCount();
		info("Top Level Manager Count : " + topLevelManagerCount);
		int childGroupsCount = reviewPage.childGroupsAdditon();
		info("Child Group Count : " + childGroupsCount);
		assertEquals(topLevelManagerCount, childGroupsCount);
		hrSoftPage.logOut();

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_168_CompensationCompareBudgetValuesWithBelowSection(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		login.refresh();
		String managerWithDirectAndRollupGrp = Constants.ReviewingManagerUserNameWithRollup;
		hrSoftPage.proxyAs(managerWithDirectAndRollupGrp);
		ReviewingPage reviewPage = hrSoftPage.openReviewRecommendations(Constants.planIdForReviewingManagereWithRollup);
		reviewPage.switchToInnerFrame();
		List<String> budgetAmountsFromGrid = reviewPage.getBudgetAmountValues();
		info("Budget List " + budgetAmountsFromGrid);
		reviewPage.clickReviewingPageTabs("Budget");
		List<String> budgetAmountsFromBelowSection = reviewPage.getBudgetAmountsFromBudgetTab();
		info("Budget Amounts From Below Section " + budgetAmountsFromBelowSection);
		assertEquals(budgetAmountsFromGrid.size(), budgetAmountsFromBelowSection.size());
		for (int i = 0; i < budgetAmountsFromGrid.size(); i++) {
			String element1 = budgetAmountsFromGrid.get(i);
			String element2 = budgetAmountsFromBelowSection.get(i);
			info("list 1 : " + element1);
			info("list 2 : " + element2);
			assertEquals(element1, element2);
		}
		hrSoftPage.logOut();
	}

}
