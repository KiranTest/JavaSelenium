package com.hrsoft.test.compensation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.compensation.DistributeFundPage;
import com.hrsoft.gui.compensation.PlanningPage;
import com.hrsoft.utils.zerocell.TestDataExcel;

import com.hrsoft.test.setuphelpers.WebBaseTest;
import static com.hrsoft.reports.ExtentLogger.info;
import static com.hrsoft.reports.ExtentLogger.pass;

public class DistributeFundTestSuite extends WebBaseTest {

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_171_172_CompensationDistributionFund_OpenDistFundPageAndEnterBudget(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		if (Constants.reviewManagerUserNameWithDistLink == null)
			throw new org.testng.SkipException("No Manager has Distributed Funds");
		hrSoftPage.proxyAs(Constants.reviewManagerUserNameWithDistLink);
		DistributeFundPage distributeFundPage = hrSoftPage.openDistributedFundLink(Constants.compPlanId,
				"Distribute Funds");
		String prevDistAmt = distributeFundPage.getDistributedAmountFromTopTable();
		Double oldDistAmt = distributeFundPage.convertStringToDouble(prevDistAmt.replaceAll(",", ""));
		info("Old Distributed Fund :" + prevDistAmt);

		String rand = RandomStringUtils.randomNumeric(3);
		String oldBudgetVal = distributeFundPage.getOldBudgetVal(0);
		Double oldBudgetAmt = distributeFundPage.convertStringToDouble(oldBudgetVal);
		info(("Budget Amt before entering for 1st row : " + oldBudgetAmt));

		String newbudgetVal = distributeFundPage.enterNewBudgetVal(0, rand);
		distributeFundPage.applyChanges(0);
		Double newBudgetAmt = distributeFundPage.convertStringToDouble(newbudgetVal);
		info(("New Budget Amt after entering for first row : " + newbudgetVal));
		String newDistAmt = distributeFundPage.getDistributedAmountFromTopTable();
		Double actualDistFunds = distributeFundPage.convertStringToDouble(newDistAmt.replaceAll(",", ""));
		Double expectedDistFunds = oldDistAmt - oldBudgetAmt + newBudgetAmt;
		info("Actual Dist Funds :" + actualDistFunds);
		info("Expected Dist Funds :" + expectedDistFunds);
		assertEquals(expectedDistFunds, actualDistFunds);
		hrSoftPage.logOut();

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_173_CompensationDistributionFund_holdBackAmounttAndDistAmountCalculation(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		if (Constants.reviewManagerUserNameWithDistLink == null)
			throw new org.testng.SkipException("No Manager has Distributed Funds");
		hrSoftPage.proxyAs(Constants.reviewManagerUserNameWithDistLink);
		DistributeFundPage distributeFundPage = hrSoftPage.openDistributedFundLink(Constants.compPlanId,
				"Distribute Funds");
		String budgetAmtFromTopTbl = distributeFundPage.getBudgetAmountFromTopTable();
		String distributedAmountFromTopTable = distributeFundPage.getDistributedAmountFromTopTable();
		String HoldBackAmtFromTopTable = distributeFundPage.getHoldBackAmtFromTopTable();

		Double oldBudgetAmount = distributeFundPage.convertStringToDouble(budgetAmtFromTopTbl.replaceAll(",", ""));
		Double oldDistAmount = distributeFundPage
				.convertStringToDouble(distributedAmountFromTopTable.replaceAll(",", ""));
		Double oldHoldBackAmount = distributeFundPage
				.convertStringToDouble(HoldBackAmtFromTopTable.replaceAll(",", ""));

		info("Previous Budget Amount : " + oldBudgetAmount);
		info("Previous Distribution Amount : " + oldDistAmount);
		info("Previous Holdback Amount : " + oldHoldBackAmount);

		/* Previous budget from bottom table */

		String oldBudgetVal = distributeFundPage.getOldBudgetVal(0);
		Double oldBudgetAmt = distributeFundPage.convertStringToDouble(oldBudgetVal);

		/*
		 * Entering New budget Amount in bottom table and verifying hold back amount
		 * calculation
		 */
		info("Entering Budget Amount for a group!");
		String rand = RandomStringUtils.randomNumeric(3);
		distributeFundPage.enterNewBudgetVal(0, rand);
		String budgetAmtFromTopTbl2 = distributeFundPage.getBudgetAmountFromTopTable();
		String distributedAmountFromTopTable2 = distributeFundPage.getDistributedAmountFromTopTable();
		String HoldBackAmtFromTopTable2 = distributeFundPage.getHoldBackAmtFromTopTable();

		Double newBudgetAmount = distributeFundPage.convertStringToDouble(budgetAmtFromTopTbl2.replaceAll(",", ""));
		Double newDistAmount = distributeFundPage
				.convertStringToDouble(distributedAmountFromTopTable2.replaceAll(",", ""));
		Double newHoldBackAmount = distributeFundPage
				.convertStringToDouble(HoldBackAmtFromTopTable2.replaceAll(",", ""));

		Double expectedHoldBackCalc = newBudgetAmount - newDistAmount;
		info("Expected Calculation : " + expectedHoldBackCalc);
		info("Actual Calculation : " + newHoldBackAmount);
		assertEquals(newHoldBackAmount, expectedHoldBackCalc, 0.1);

		/* New Budget amount , Dist amount and entered budget amount in bottom table */

		String newBudgetVal = distributeFundPage.getOldBudgetVal(0);
		Double newBudgetAmt = distributeFundPage.convertStringToDouble(newBudgetVal);
		System.out.println("New Budget Val" + newBudgetAmt);

		Double expectedDistAmt = oldDistAmount - oldBudgetAmt + newBudgetAmt;
		System.out.println("Expected Dist Amt " + expectedDistAmt);
		System.out.println("Actual Dist Amt " + newDistAmount);
		assertEquals(expectedDistAmt, newDistAmount, 0.1);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_174_CompensationDistributionFund_EnterBudgetAndVerifyByProxying(TestDataExcel data) {

		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		if (Constants.reviewManagerUserNameWithDistLink == null)
			throw new org.testng.SkipException("No Manager has Distributed Funds");
		hrSoftPage.proxyAs(Constants.reviewManagerUserNameWithDistLink);
		DistributeFundPage distributeFundPage = hrSoftPage.openDistributedFundLink(Constants.compPlanId,
				"Distribute Funds");
		List<WebElement> tblRows = distributeFundPage.getTblElements();
		for (WebElement row : tblRows) {
			String groupNames = distributeFundPage.getGroupNames(row);
			System.out.println("Grp Name : " + groupNames);
			if (groupNames.contains(Constants.ReviewingManagerFullName)) {
				distributeFundPage.clickIsVisible(row).clickIsDist(row);
				String rand = RandomStringUtils.randomNumeric(2);
				distributeFundPage.clearBudgetInput(row).setBudgetInput(row, rand);
				doWait(2000);
				int enteredBudget = distributeFundPage.getBudgetInput(row);
				info("Entered Budget : " + enteredBudget);
				distributeFundPage.clickApplyBtn(row).applyBudgetAmt();
				hrSoftPage.cancelProxy();
				distributeFundPage.proxyAsGrpName(Constants.ReviewingManagerUserName,
						Constants.ReviewingManagerFullName);
				hrSoftPage.openDistributedFundLink(Constants.compPlanId, "Distribute Funds");
				info("Opened Dist Fund Page");
				String budget = distributeFundPage.budgetAmtAfterProxy();
				info("Budget Amt after proxy " + budget);
				Double budgetConvert = distributeFundPage.convertStringToDouble(budget);
				int budgetAmtAfterproxy = (int) Math.round(budgetConvert);
				info("budget" + budgetAmtAfterproxy);
				assertEquals(enteredBudget, budgetAmtAfterproxy, 0.1);
				break;
			}
		}
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_175_176_Verify_CascadingBudget_For_Rollup_Group(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		if (Constants.reviewManagerUserNameWithDistLink == null)
			throw new org.testng.SkipException("No Distributed funds are available for any manager");
		hrSoftPage.proxyAs(Constants.reviewManagerUserNameWithDistLink);
		DistributeFundPage distributeFundPage = hrSoftPage.openDistributedFundLink(Constants.compPlanId,
				"Distribute Funds");
		List<WebElement> tblRows = distributeFundPage.getTblElements();
		for (WebElement row : tblRows) {
			String groupNames = distributeFundPage.getGroupNames(row);
			System.out.println("Grp Name : " + groupNames);
			if (groupNames.contains(Constants.ReviewingManagerFullName)) {
				distributeFundPage.uncheckDistributeFurther(row);
				String rand = RandomStringUtils.randomNumeric(3);
				distributeFundPage.clearBudgetInput(row).setBudgetInput(row, rand);
				doWait(2000);
				int enteredBudget = distributeFundPage.getBudgetValueForRollup(row);
				System.out.println("Entered Budget for group : " + enteredBudget);
				distributeFundPage.clickApplyBtn(row).applyBudgetAmt();
				hrSoftPage.cancelProxy();
				distributeFundPage.proxyAsGrpName(Constants.ReviewingManagerUserName,
						Constants.ReviewingManagerFullName);
				assertFalse(hrSoftPage.isDistributedFundVisible(Constants.compPlanId, "Distribute Funds"));
				PlanningPage planningPage = hrSoftPage.openPlanRecommendations(Constants.compPlanId);
				planningPage.clickRollup().clickBudgetTab();
				int cascadeBudget = planningPage.getCascadeBudget();
				System.out.println("cascadeBudget" + cascadeBudget);
				assertEquals(enteredBudget, cascadeBudget);
				break;
			}
		}
		hrSoftPage.logOut();
	}
}
