package com.hrsoft.gui.compensation;

import static java.lang.String.format;
import static com.hrsoft.config.ConfigFactory.getConfig;
import static com.hrsoft.driver.DriverManager.getDriver;
import static org.testng.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;

import com.hrsoft.constants.Constants;
import com.hrsoft.utils.seleniumfy.BasePage;
import com.hrsoft.utils.zerocell.TestDataExcel;
import static com.hrsoft.reports.ExtentLogger.*;

/**
 * @author Mashood khan <a href="mailto:mashood.khan@hrsoft.com</a>
 */
public class ReviewingPage extends BasePage {

	private String table = "//table[@id='gridBody']";
	private String clickAdvancedBtn = "//ul/li/a[@id='btn-toggle-drop-down']";
	private String applyBtn = "#btn-apply-selected-filters";
	private String resetBtn = "#btn-reset-selected-filters";
	private String navigateToPlanningScreen = "//button[@wstmtitle='Back to Review']";
	private String closePopup = "img#btnHideIcoClose"; // #btnHideIcoClose
	private String expandGridBtn = "//a[@title='Expand/Collapse']";
	private String closeActionPopupBtn = "//input[@id='actionCloseBtn']";
	private String submitRejectGroupBtnInPopup = "//input[@value='Reject' or @value='Submit'or @value ='Withdraw']";
	private String notesInput = "//body[@id='tinymce']";
	private String saveBtnInPopUp = "//input[@value='Save']";
	private String planningPageHeading = "//div[@class='groupHeading']//span[text()='%s']/../span[text()='Planning']";
	private String reviewingPageHeading = "//div[@id='pageTitle']/span[text()='%s']/../span[text()='Reviewing']";
	/* Budget Chart */
	private String budgetTab = "//ul/li[@role='tab']/a/span[text()='Budget']";
	private String budgetsChartValues = "//td[contains(@class,'budgetchartCaptionValue') and contains(@id,'caption_budget_chart')]";
	private String amountSpentBefore = "//tr//td[@id='amt_spent_budget_chart_0']//span";
	private String remAmountBefore = "//tr//td[@id='amt_remaining_budget_chart_0']//span";
	/* Group chart */
	private String groupTab = "//ul/li[@role='tab']/a/span[text()='Group Status']";
	private String groupValueBefore = "//*[name()='tspan' and contains(text(),'Groups')]";
	protected String pleaseWait = "(//h4[@id='waitWinMessage1'])[1]";
	private String topLevelGroupCount = "//tr[@originalrowpos=0]//td[@typecode='WS_Number_All_Groups']//span";
	private String allgroupsCount = "//tr//td[@typecode='WS_Number_All_Groups']//span";
	private String pleaseWaitWindow = "//div[@id='waitWindow_app_frame'][contains(@style,'display: block;')]//div/*[text()='Please wait...']";
	private String allBudgetsAmount = "//table//tr[@originalrowpos=0]//td[@typecode='WS_Budget_GrpStats_Total_Amount']//span";

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		doWait(500);
		switchToInnerFrame();
		return ExpectedConditions.invisibilityOfElementLocated(locateBy(
				"//div[@id='waitWindow_app_frame'][contains(@style,'display: block;')]//div/*[text()='Please wait...']"));
	}

	public ReviewingPage navigatedToReviewingPage() {
		assertTrue(waitUntilVisible("div[id='sp_planning'][componentid='reviewingGrpSummary']"));
		switchToInnerFrame();
		assertTrue(waitUntilVisible("button#refreshReviewData"));
		return this;
	}

	public String getEmployeeCount(String group) {
		return getText(format("(//a[contains(text(),'%s')]/parent::td/following-sibling::td[3])[1]", group));
	}

	public ReviewingPage changeColumnOrder(String column1, String column2) {
		switchToInnerFrame();
		String editButton = "img#prefs_reviewingGrpSummary";
		if (!isElementPresent(editButton))
			throw new SkipException("Edit preferences button not present in reviewing page for the plan using");
		waitUntilVisible(editButton);
		if (findElement(editButton).isDisplayed())
			assertTrue(click(editButton));
		switchToOuterFrame();
		assertTrue(click("//span[text()='Set My Preferences']"));
		WebElement from = findElement(format(
				"(//div[@typecode='%s']/ancestor::li//div[@class='ui-icon ui-icon-arrowthick-2-n-s drag-handle ui-sortable-handle'])[1]",
				column1));
		WebElement to = findElement(format(
				"(//div[@typecode='%s']/ancestor::li//div[@class='ui-icon ui-icon-arrowthick-2-n-s drag-handle ui-sortable-handle'])[1]",
				column2));
		Actions builder = new Actions(getDriver());
		Action dragAndDrop = builder.clickAndHold(from).moveToElement(to).release(to).build();
		dragAndDrop.perform();
		assertTrue(click("#btnApply"));
		pass(format("changed column order of %s and %s", column1, column2));
		return this;
	}

	/* Search group in Advanced button */

	public ReviewingPage clickAdvancedBtn() {
		assertTrue(click(clickAdvancedBtn));
		return this;
	}

	public ReviewingPage clickApply() {
		assertTrue(click(applyBtn));
		hasPageLoaded();
		doWait(2000);
		return this;
	}

	public ReviewingPage clickReset() {
		assertTrue(click(resetBtn));
		hasPageLoaded();
		assertTrue(waitForElementInvisible(pleaseWait));
		return this;
	}

	public ReviewingPage expandGrid() {
		switchToOuterFrame();
		assertTrue(click(expandGridBtn));
		doWait(2000);
		return this;
	}

	public void goToCompensation() {
		assertTrue(click("//div[text()='Compensation']"));
		assertTrue(waitForElementInvisible(pleaseWait));
		hasPageLoaded();
	}

	public ReviewingPage scrollBottom() {
		switchToInnerFrame();
		hover(table);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,2000)");
		return this;
	}

	public ReviewingPage clickGroupNameLink() {
		String groupNameLink = "//tr[@originalrowpos=1]/td/a";
		assertTrue(click(groupNameLink));
		hasPageLoaded();
		return this;
	}

	public Boolean isNavigatedToPlanningPage(String planName, String mgrName) {
		switchToInnerFrame();
		assertTrue(isElementPresent(format(planningPageHeading, planName, mgrName)));
		return true;
	}

	public ReviewingPage clickPreviousIcon() {
		switchToInnerFrame();
		assertTrue(click(navigateToPlanningScreen));
		hasPageLoaded();
		return this;
	}

	public Boolean isNavigatedToReviewingPage(String planName, String mgrName) {
		switchToInnerFrame();
		assertTrue(isElementPresent(format(reviewingPageHeading, planName, mgrName)));
		return true;
	}

	public ReviewingPage clickAlertIcon() {
		switchToInnerFrame();
		doWait(2000);
		String alertIcon = " (//a[contains(@class,'btn-special-action-alert-hard') or contains(@class,'btn-special-action-alert-soft')])[1]";
		if (!isElementPresent(alertIcon))
			throw new org.testng.SkipException("No Alert Icons found for any group");
		assertTrue(click(alertIcon));
		doWait(2000);
		return this;
	}

	public ReviewingPage closeAlertPopup() {
		switchToOuterFrame();
		if (isElementVisible(closePopup))
			assertTrue(click(closePopup));
		else {
		}
		doWait(2000);
		switchToDefaultContent();
		switchToInnerFrame();
		return this;
	}

	public ReviewingPage addNotes(String typeCode) {
		String notesIcon = format("//tr[@originalrowpos=1]/td[@wsmeta='%s']/span/a", typeCode);
		assertTrue(click(notesIcon));
		switchToOuterFrame();
		switchToFrame("iframe[id='newNotes_ifr']");
		driver.findElement(By.xpath(notesInput)).clear();
		setText(notesInput, "Test Add Notes");
		switchToOuterFrame();
		assertTrue(click(saveBtnInPopUp));
		switchToInnerFrame();
		return this;
	}

	public ReviewingPage drillDownGroup() {
		switchToInnerFrame();
		String drillDownIcon = format("(//td[@typecode='WS_Group_Title']//img[@class='imgdrillable'])[1]");
		assertTrue(click(drillDownIcon));
		return this;
	}

	public void drillUpGroup() {
		assertTrue(click("//tr/td[@typecode='WS_Group_Title']/img[@class='imgtopLevelGroup']"));
		hasPageLoaded();
		doWait(3000);
	}

	public List<WebElement> redAlertGroups() {
		String redAlert = "//table//tbody//tr//td//a[contains(@class,'alert btn-special-action-alert-hard')]//..//following-sibling::td[@typecode='WS_Action']//button";
		if (!isElementPresent(redAlert))
			throw new org.testng.SkipException(
					"No Red Alerts for any groups for this manager :" + Constants.ReviewingManagerFullNameWithRollup);
		return findElements(redAlert);
	}

	public String clickSubmit() {
		String submitXpath = "td[@typecode='WS_Action']/button";
		return submitXpath;
	}

	public ReviewingPage checkRedAlerts() {
		switchToOuterFrame();
		if (isElementPresent("//div[@class='mainDataWrapper']//span[contains(text(),'Group Alerts')]")) {
			info("This Group Has Red Alert ! Unable to Submit/Reject");
			assertTrue(click(closeActionPopupBtn));
			switchToInnerFrame();
		} else {
			info("No Red Alerts Found for the plan " + getConfig().compPlanId());
		}
		return this;

	}

	public ReviewingPage clickSubmitForRedAlert(WebElement row) {
		WebElement submit = row.findElement(By.xpath(clickSubmit()));
		assertTrue(click(submit));
		switchToOuterFrame();
		if (isElementPresent("//span[text()='Group Alerts']")) {
			waitForElementVisible(closeActionPopupBtn);
			assertTrue(click(closeActionPopupBtn));
		} else {
			assertTrue(click(submitRejectGroupBtnInPopup));
			doWait(4000);
			acceptAlertWithoutException();
		}

		return this;
	}

	public List<String> budgetValuesBeforeDrillDown() {
		ArrayList<String> budgetsValues = new ArrayList<>();
		switchToDefaultContent();
		switchToOuterFrame();
		switchToFrame("#supporting_data_frame");
		assertTrue(click(budgetTab));
		List<WebElement> budget = findElements(budgetsChartValues);
		for (WebElement b : budget) {
			String x = getText(b);
			budgetsValues.add(x);
		}
		return budgetsValues;
	}

	public String amountSpentBeforeDrillDown() {
		String amountSpent = findElement(amountSpentBefore).getText();
		return amountSpent;
	}

	public String remainingAmountBeforeDrillDown() {
		String remAmount = findElement(remAmountBefore).getText();
		return remAmount;
	}
	/* After Drill Down group */

	public String meritValueAfterDrillDown() {
		drillDownGroup();
		switchToDefaultContent();
		switchToOuterFrame();
		switchToFrame("#supporting_data_frame");
		assertTrue(click(budgetTab));
		String meritAfter = findElement(budgetsChartValues).getText();
		return meritAfter;
	}

	public String amountSpentAfterDrillDown() {
		String amountSpentafter = findElement(amountSpentBefore).getText();
		return amountSpentafter;
	}

	public String remainingAmountAfter() {
		String remAmountAfter = findElement(remAmountBefore).getText();
		return remAmountAfter;
	}

	public List<String> allBudgetsAmountFromGrid() {
		switchToInnerFrame();
		List<WebElement> budgetAmt = findElements(allBudgetsAmount);
		List<String> b = new ArrayList<>();
		for (WebElement budget : budgetAmt) {
			String budgetValue = getText(budget).replaceAll("\\s", "");
			b.add(budgetValue);
		}
		return b;
	}

	public String getGroupStatusValueBeforeDrillDown() {
		switchToDefaultContent();
		switchToOuterFrame();
		switchToFrame("#supporting_data_frame");
		assertTrue(click(groupTab));
		String groupBefore = findElement(groupValueBefore).getText();
		return groupBefore;
	}

	public String getTopLevelGroupValueCountFromGrid() {
		switchToInnerFrame();
		String x = getText("//tr[@originalrowpos=0]//td[@typecode='WS_Number_All_Groups']//span");
		return x;
	}

	/* After Drill Down group */
	public String getGroupStatusValueAfterDrillDown() {
		drillDownGroup();
		switchToOuterFrame();
		switchToFrame("#supporting_data_frame");
		assertTrue(click(groupTab));
		String groupAfter = findElement(groupValueBefore).getText();
		return groupAfter;

	}

	public ReviewingPage selectAnyGroup() {
		clickAndSelectIndex("select#select-group-filter", 0);
		return this;
	}

	public String getSelectedGroup() {
		String group = getText("//select[@id='select-group-filter']//option[@selected='true']");
		return group;
	}

	public String getTopLevelGroup() {
		waitForElementInvisible(pleaseWait);
		switchToInnerFrame();
		String topLG = getText("//td[contains(@class,'topLevelGroup') and @typecode='WS_Group_Title']//a");
		return topLG;
	}

	public String loggedInGroup() {
		switchToDefaultContent();
		String lg = getText("//div[@itemid='TEXT_995933750063495']//h6/div");
		return lg;
	}

	public int getTopLevelManagerGroupsCount() {
		String count = getText(topLevelGroupCount);
		int topGroupCount = Integer.parseInt(count);
		return topGroupCount;
	}

	public List<Integer> allGroupsCount() {
		List<WebElement> group = findElements(allgroupsCount);
		List<Integer> grp = new ArrayList<>();
		for (WebElement g : group) {
			int totalGroupCount = Integer.parseInt(getText(g));
			grp.add(totalGroupCount);
		}
		return grp;
	}

	public int childGroupsAdditon() {
		int sum = 0;
		List<Integer> childGroupsAddition = allGroupsCount();
		List<Integer> child = childGroupsAddition.subList(1, childGroupsAddition.size());
		for (int no : child) {
			sum += no;
		}
		System.out.println("Sum" + sum);
		return sum;

	}

	public ReviewingPage clickReviewingPageTabs(String tabName) {
		switchToOuterFrame();
		switchToSupportingFrame();
		String tabs = format("//ul[@id='topNavigationPanel']/li/a/span[text()='%s']", tabName);
		assertTrue(click(tabs));
		return this;
	}

	public List<String> getBudgetAmountValues() {
		List<WebElement> budgets = findElements("//tr[1]/td[@typecode='WS_Budget_GrpStats_Total_Amount']//span");
		List<String> budget = new ArrayList<>();
		for (WebElement b : budgets) {
			String budgetValue = getText(b).replaceAll("\\s", "");
			budget.add(budgetValue);
		}
		return budget;
	}

	public List<String> getBudgetAmountsFromBudgetTab() {
		switchToSupportingFrame();
		List<WebElement> budgets = findElements(budgetsChartValues);
		List<String> budgetTabValues = new ArrayList<>();
		for (WebElement b : budgets) {
			String budgetValue = getText(b).replaceAll("\\s", "");
			budgetTabValues.add(budgetValue);
		}
		return budgetTabValues;
	}

	public String getReviewingGroupName() {
		switchToInnerFrame();
		return getText("//div[@class='groupTrail']");
	}

	/* Submit/Reject/Withdraw Rollup Group Methods */

	public ReviewingPage submitRejectRollup() { // Submit and reject 2nd rollup group (not loggedin user)
		String r = "//button[@actiontype='Submit' or @actiontype='Reject' and contains(@url,'R')]";
		List<WebElement> x = findElements(
				"//button[@actiontype='Submit' or @actiontype='Reject' and contains(@url,'R')]");
		if (!isElementPresent(r))
			throw new org.testng.SkipException("No Rollup group found under this manager ");
		if (x.size() > 1) {
			assertTrue(click("(//button[@actiontype='Submit' or @actiontype='Reject' and contains(@url,'R')])[2]"));
		} else {
			assertTrue(click("//button[@actiontype='Submit' or @actiontype='Reject' and contains(@url,'R')]"));
		}
		doWait(3000);
		switchToOuterFrame();
		submitRejectGrpBtnsInPopup();
		return this;
	}

	public ReviewingPage withDrawRollupGroup() {
		List<WebElement> x = findElements(
				"//button[@actiontype='Submit' or @actiontype='Withdraw' and contains(@url,'R')]");
		if (x.size() > 1) {
			assertTrue(click("(//button[@actiontype='Submit' or @actiontype='Withdraw' and contains(@url,'R')])[1]"));
		} else {
			assertTrue(click("//button[@actiontype='Submit' or @actiontype='Withdraw' and contains(@url,'R')]"));
		}
		doWait(3000);
		switchToOuterFrame();
		submitRejectGrpBtnsInPopup();
		return this;
	}

	/* Submit/Reject/Withdraw Direct Group Methods */
	public ReviewingPage submitRejectDirectGrp() { // Submit and reject 2nd rollup group (not loggedin user)
		String SRButtons = "(//tr[contains(@sdurl,'D')]//button[@actiontype='Submit' or @actiontype='Reject'])[1]";
		if (!isElementPresent(SRButtons) && isElementPresent("//td/img[@class='imgdrillable'][1]")) {
			assertTrue(click("//td/img[@class='imgdrillable'][1]"));
		}
		assertTrue(click(SRButtons));
		doWait(3000);
		switchToOuterFrame();
		submitRejectGrpBtnsInPopup();
		return this;
	}

	public ReviewingPage withdrawDirectGrp() {
		assertTrue(click("(//tr[contains(@sdurl,'D')]//button[@actiontype='Submit' or @actiontype='Withdraw'])[1]"));
		doWait(3000);
		switchToOuterFrame();
		submitRejectGrpBtnsInPopup();
		return this;
	}

	public ReviewingPage submitRejectGrpBtnsInPopup() {
		switchToOuterFrame();
		doWait(3000);
		waitUntilVisible(submitRejectGroupBtnInPopup);
		if (isElementPresent(submitRejectGroupBtnInPopup))
			assertTrue(click(submitRejectGroupBtnInPopup));
		acceptAlertWithoutException();
		hasPageLoaded();
		waitForElementInvisible(pleaseWaitWindow);
		switchToInnerFrame();
		doWait(2000);
		return this;
	}

}
