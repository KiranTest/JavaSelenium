package com.hrsoft.gui.compensation;

import static com.hrsoft.reports.ExtentLogger.info;
import static com.hrsoft.reports.ExtentLogger.pass;
import static java.lang.String.format;
import static org.testng.Assert.assertTrue;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import com.hrsoft.constants.Constants;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.utils.seleniumfy.BasePage;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */

public class PlanningPage extends BasePage {
	private String submit = "input[actiontype='Submit']";
	private String withDraw = "input[actiontype='Withdraw']";
	private String confirm = "#btnSubmitWitdrawAction";
	private String pleaseWait = "//h5[text()='Please Wait...']";
	private String loading = "//*[@class='compAppFrame']//*[text()='Loading...']";
	private String sortBy = "//div[@class='iconcontainer']/following::td[@typecode='%s']/preceding::div//div[@class='filter-sort-dropdown']";
	private String popUpClose = "[class='ui-button-icon ui-icon ui-icon-closethick']";
	private String secondPopupClose = "(//span[@class='ui-button-icon ui-icon ui-icon-closethick'])[2]";
	private String meritActualAmount = format(
			"(//td[@typecode='WS_Component_Actual_Amount@%s']//input[@type='text'][@showtooltip='true'])[1]",
			Constants.componentId);
	private String meritActualPercent = format(
			"(//td[@typecode='WS_Component_Actual_Percent@%s']//input[@type='text'][@showtooltip='true'])[1]",
			Constants.componentId);
	private String columnHeader = "(//div[@typecode='%s']/ancestor::li//div[@class='sp_cell dragHandle121 columnOrder input-cell']//input)";
	private String filter = "#select-filter";
	protected String pleaseWaitWin = "(//h4[@id='waitWinMessage1'])[1]";
	private String pencilIcon = "img#prefs_supportingDataViews";
	private String setMyPref = "a#userPrefsLink";
	private String budgetTab = "//ul[@id='topNavigationPanel']/li/a/span[text()='Budget']";
	private String chartSection = "//table[@class='tblBudgetContainer']/tbody/tr/td";
	private String empDetailsTbl = "//table[@id='gridBody']/tbody/tr";
	private String confirmApply = "//input[@value='Apply']";
	private String jobSearchBtn = "td.jobSearchFiltersActionsCell button";
	private String jobGrid = "//table[@componentid='planningEmpJobSearchResult']";
	private String jobResultGrid = "//table[@componentid='planningEmpJobSearchResult']//tbody//tr";
	private String closePopup = "//button[@id='i18n_dialog_close']";
	private String closeJobPromotionPopup = "//input[@id='btnJobPromotionDialogClose']";
	private String selectBtnFromTbl = "//table[@componentid='planningEmpJobSearchResult']/tbody/tr/td[5]/span//preceding::td/button[@title='Select']";
	private String jobRecomTitle = "span.ui-dialog-title";
	private String proposedJob = "//table[@componentid='planningEmpDetail_proposedJob']/tbody/tr/td[2]/span[1]";
	private String revertPromotionBtn = "button.btnRevertPromotion";
	private String inlineJob = "(//a[@data-type-code='WS_Job_Code'])[1]";
	private String empSparesHistory = "//a[contains(@class,'btn-tcc-emp-spares-audit')]";
	private String empSparePopUp = "//div[@role='dialog' and @aria-describedby='COM_EMP_SPARE_AUDIT_Prototype']/div[@id='COM_EMP_SPARE_AUDIT_Prototype']";
	private String closeEmpSpareHistoryPopup = "//button[@title='Close']";
	private String chartXaxis = "div[class='supportingData compa-ratio-chart'] span svg g:nth-child(6)";
	private String chartYaxis = "svg g g:nth-child(4) g text";
	private String setMyPreferenceBtn = "img[componentid^='planningGrpProgram'], [componentid^='planningGrpSummary'] [title='Preferences']";

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		doWait(500);
		switchToInnerFrame();
		return ExpectedConditions.invisibilityOfElementLocated(locateBy(
				"//div[@id='waitWindow_app_frame'][contains(@style,'display: block;')]//div/*[text()='Please wait...']"));
	}

	public PlanningPage navigatedToPlanningPage() {
		assertTrue(waitUntilVisible("div[id='sp_planning'][componentid='planningGrpSummary']"));
		switchToInnerFrame();
		assertTrue(waitUntilVisible("div.scopeAndFilters"));
		return this;
	}

	public PlanningPage clickAdvanced() {
		switchToInnerFrame();
		assertTrue(click("#btn-toggle-drop-down"));
		return this;
	}

	public String getCompPlanTitle() {
		return findElement("span.com-plan-name").getText();
	}

	public PlanningPage selectGroup() {
		clickAndSelectIndex("select#select-group-filter", 0);
		return this;
	}

	public PlanningPage selectFilter(String filterName) {
		assertTrue(clickAndSelectText(filter, filterName));
		return this;
	}

	public int getNumberOfItemsInFilter() {
		int optionCount = 0;
		switchToInnerFrame();
		List<WebElement> optionElements = findElements(filter);
		for (WebElement option : optionElements) {
			if (!option.getAttribute("value").equalsIgnoreCase("none")) {
				optionCount++;
			}
		}
		return optionCount;
	}

	public String getFilterText() {
		return getText(filter);
	}

	public PlanningPage clickRollup() {
		switchToInnerFrame();
		assertTrue(click("(//label[text()='Rollup'])[1]"));
		assertTrue(waitForElementInvisible(pleaseWaitWin));
		return this;
	}

	public PlanningPage clickRollupInAdvanced() {
		assertTrue(click("//input[@id='scope2']/parent::label"));
		hasPageLoaded();
		return this;
	}

	public PlanningPage clickDirect() {
		switchToInnerFrame();
		assertTrue(click("//input[@id='scope_inline1']/parent::label"));
		assertTrue(waitForElementInvisible(pleaseWaitWin));
		info("Switched to direct");
		return this;
	}

	public PlanningPage clickReset() {
		switchToInnerFrame();
		assertTrue(click("#btn-reset-selected-filters"));
		assertTrue(waitForElementInvisible(pleaseWait));
		pass("Reset to default group");
		return this;
	}

	public PlanningPage clickApply() {
		switchToInnerFrame();
		assertTrue(click("//button[@id='btn-apply-selected-filters']"));
		assertTrue(waitForElementInvisible(pleaseWait));
		hasPageLoaded();
		return this;
	}

	public PlanningPage clickFilterIcon(String columName) {
		doWait(5000);
		switchToInnerFrame();
		hover(format(sortBy, columName));
		assertTrue(click(format(sortBy, columName)));
		info("Clicked on filter icon");
		return this;
	}

	public PlanningPage sortByAscending(String columName) {
		assertTrue(click(format(sortBy, columName) + "//a[@data-sort-direc='asc']//i"));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public PlanningPage sortByDescending(String columName) {
		assertTrue(click(format(sortBy, columName) + "//a[@data-sort-direc='desc']//i"));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public String getPlanningGroupName() {
		switchToInnerFrame();
		return getText("//div[@id='pageTitle']/following-sibling::a");
	}

	public PlanningPage clickSubmit() {
		switchToInnerFrame();
		if (findElement(submit).isEnabled())
			assertTrue(click(submit));
		else {
			clickWithDraw();
			assertTrue(click(submit));
		}
		if (isAlertPresent())
			alertAccept();
		doWait(2000);
		switchToOuterFrame();
		assertTrue(click(confirm));
		if (isAlertPresent())
			alertAccept();
		switchToInnerFrame();
		assertTrue(waitForElementInvisible(pleaseWait));
		assertTrue(isElementVisible("//span[@id='groupStatus']//span[contains(text(),'Submitted')]"));
		return new PlanningPage();
	}

	public PlanningPage clickSubmitRollUp() {
		switchToInnerFrame();
		if (findElement(submit).isEnabled())
			assertTrue(click(submit));
		else {
			clickWithDraw();
			assertTrue(click(submit));
		}
		acceptAlertWithoutException();
		doWait(2000);
		switchToOuterFrame();
		assertTrue(click(confirm));
		acceptAlertWithoutException();
		switchToInnerFrame();
		assertTrue(waitForElementInvisible(pleaseWait));
		return new PlanningPage();
	}

	public boolean isWithDrawPresent() {
		return findElement(withDraw).isEnabled();
	}

	public PlanningPage clickWithDraw() {
		switchToInnerFrame();
		if (isWithDrawPresent())
			assertTrue(click(withDraw));
		doWait(2000);
		switchToOuterFrame();
		assertTrue(click(confirm));
		acceptAlertWithoutException();
		switchToInnerFrame();
		assertTrue(waitForElementInvisible(pleaseWait));
		assertTrue(isElementVisible("//span[@id='groupStatus']//span[contains(text(),'Withdrawn')]"));
		return this;
	}

	public String getPlanStatus() {
		return getText("span#groupStatus .labelText");
	}

	public String getEmployeeCount() {
		return getText("//span[@id='groupSize']//span[@class='labelText']");
	}

	public PlanningPage applyFiltersForRollup(String group, String filter) {
		clickAdvanced();
		selectGroup();
		selectFilter(filter);
		clickRollupInAdvanced();
		clickApply();
		assertTrue(waitForElementInvisible(pleaseWaitWin));
		return this;
	}

	public PlanningPage changeGroup(String group) {
		clickAdvanced();
		assertTrue(click("a#btn-group-search"));
		switchToOuterFrame();
		setGroupName(group);
		info("changed group to " + group);
		return this;
	}

	public PlanningPage clickSearchGroup() {
		switchToInnerFrame();
		assertTrue(click("a[title=\"Search Group\"]"));
		return this;
	}

	public PlanningPage clickSearchButtoninGroupSearch() {
		switchToOuterFrame();
		assertTrue(click("//div[@id='WS_Group_Search']//input[@class='seachAction pbtn']"));
		return this;
	}

	public PlanningPage searchGroupByName(String group) {
		switchToOuterFrame();
		assertTrue(setText("input#txt_WS_Group_Name", group));
		clickSearchButtoninGroupSearch();
		assertTrue(click("//td[@id='sp_cell']//input[@class='sp_checkbox']"));
		assertTrue(click("//input[@class='acceptGroups pbtn']"));
		return this;
	}

	public PlanningPage searchGroup(String empName) {
		assertTrue(click("#btn-toggle-drop-down"));
		clickAndSelectText("#select-group-filter", empName);
		assertTrue(click("//*[contains(text(),'Apply')]"));
		waitForElementVisible("#planningGroup");
		return this;

	}

	public PlanningPage downloadCsv() {
		switchToInnerFrame();
		assertTrue(click("a#exportToCSV"));
		// if(isAlertPresent ())
		// throw new SkipException("") ;
		return this;
	}

	public void clickSalaryTab(String salaryProgramName) {
		switchToInnerFrame();
		String xpath = format("//li[@title='%s']/a", salaryProgramName);
		assertTrue(click(xpath));
	}

	public PlanningPage enterAwardActualAmountValue(String text) {
		clickResetAwards();
		switchToInnerFrame();
		assertTrue(clear(meritActualAmount));
		assertTrue(setText(meritActualAmount, text));
		// findElement (meritActualAmount).sendKeys (Keys.ENTER);
		assertTrue(click(format("//*[@dictionarykey='WS_Component_Actual_Percent@%s']", Constants.componentId)));
		switchToOuterFrame();
		String popUp = "#statusClose";
		if (isElementVisible(popUp))
			assertTrue(click(popUp));
		return this;
	}

	public PlanningPage enterMeritActualAmountPercent(String text) {
		String key = "WS_Component_Actual_Percent@" + Constants.componentId;
		switchToInnerFrame();
		assertTrue(clear(meritActualPercent));
		assertTrue(setText(meritActualPercent, text));
		findElement(meritActualPercent).sendKeys(Keys.ENTER);
		assertTrue(click(format("//span[@dictionarykey='%s']", key)));
		switchToOuterFrame();
		String popUp = "#statusClose";
		if (isElementVisible(popUp))
			assertTrue(click(popUp));
		return this;
	}

	public PlanningPage enterNewSalary(String text) {
		clickResetAwards();
		switchToInnerFrame();
		String salary = "(//td[@typecode='WS_New_Salary']//input[@type='text'][@showtooltip='false'])[1]";
		assertTrue(clear(salary));
		assertTrue(setText(salary, text));
		assertTrue(click("//span[text()='New Salary']"));
		switchToOuterFrame();
		String popUp = "#statusClose";
		if (isElementVisible(popUp))
			assertTrue(click(popUp));
		return this;
	}

	public boolean isAwardField() {
		switchToInnerFrame();
		return isElementPresent(meritActualAmount);
	}

	public PlanningPage clickMeritCalculatedAmount() {
		switchToInnerFrame();
		assertTrue(click("(//span[@wstypecode='WS_Component_Calculated_Amount_Base@"+Constants.componentId+"'])[1]"));
		return this;
	}

	public PlanningPage clickResetAwards() {
		switchToInnerFrame();
		assertTrue(click("//input[@actiontype='ResetGroupAwards']"));
		alertAccept();
		assertTrue(waitForElementInvisible(pleaseWait));
		assertTrue(waitForElementInvisible(loading));
		info("Reset rewards");
		return this;
	}

	public String getBudgetMeritPercentageAmountSpent() {
		switchToOuterFrame();
		switchToFrame("#supporting_data_frame");
		assertTrue(click("[dictionarykey=\"WS_Budget_View\"]"));
		String el = "(//span[starts-with(@class,'budget_percent graphSubCaptionColor')])[1]";
		if (!isElementPresent(el))
			throw new SkipException("Budget percent not present");
		return getText(el);
	}

	public String getBudgetMeritAmountSpent() {

		switchToOuterFrame();
		switchToFrame("#supporting_data_frame");
		assertTrue(click("[dictionarykey=\"WS_Budget_View\"]"));
		String el = "(//span[starts-with(@class,'budget_amount graphSubCaptionColor')])[2]";
		if (!isElementPresent(el))
			throw new SkipException("Budget amount graph calculator");
		doWait(3000);
		return getText(el).split(" ")[0];
	}

	public String getTotalAwardActualPercent() {
		switchToInnerFrame();
		return getText(format("//td[@class='totaler_cell numberCell'][@typecode='WS_Component_Actual_Percent@%s']",
				Constants.componentId));
	}

	public String getTotalAwardActualValue() {
		switchToInnerFrame();
		return getText(format("//td[@class='totaler_cell numberCell'][@typecode='WS_Component_Actual_Amount@%s']",
				Constants.componentId));
	}

	public String getAwardActualAmount() {
		switchToInnerFrame();
		return getAttribute(meritActualAmount, "value").split(" ")[0];
	}

	public int getEmployeeCountUndermanager() {
		int no = getTextList("//div[@id='sp_planning']//tr[@class='sp_row_locked']").size();
		return no;
	}

	public String getCssMeritActual() {
		switchToInnerFrame();
		String color = getCssValue("(//td[@typecode='WS_Component_Actual_Percent@2']//i[@class='fa fa-history'])[1]",
				"color");
		return Color.fromString(color).asHex();
	}

	public PlanningPage clickAlert() {
		doWait(2000);
		switchToInnerFrame();
		assertTrue(click("//i[starts-with(@class,'fa fa-exclamation-triangle alert-icon fa-lg fa-fw fa')]"));
		return this;
	}

	public PlanningPage clickAlertOnTop() {
		String alert = "//a[@wstmtitle='Group Alert']//i";
		doWait(2000);
		switchToInnerFrame();
		if (!isElementPresent(alert))
			throw new SkipException("No alert present for this plan or user");
		assertTrue(click(alert));
		return this;
	}

	public PlanningPage clickPlanningGroupEditButton() {
		switchToInnerFrame();
		if (!isElementPresent(setMyPreferenceBtn))
			throw new org.testng.SkipException("Edit Preferences button is not allowed for this user");
		assertTrue(click(setMyPreferenceBtn));
		if (!isElementPresent("//span[text()='Set My Preferences']"))
			// if(!findElement("//span[text()='Set My Preferences']").isDisplayed ())
			clickUsingJs(findElement(setMyPreferenceBtn));
		// clickUsingJs(findElement("#prefs_planningGrpSummary"));
		return this;
	}

	public void enterJobTitle() {

	}

	public PlanningPage changeColumnOrder(String column1, String column2) {
		switchToOuterFrame();
		assertTrue(click("//span[text()='Set My Preferences']"));
		WebElement from = findElement(format(
				"(//div[@typecode='%s']/ancestor::li//div[@class='ui-icon ui-icon-arrowthick-2-n-s drag-handle ui-sortable-handle'])[1]",
				column1));
		WebElement to = findElement(format(
				"(//div[@typecode='%s']/ancestor::li//div[@class='ui-icon ui-icon-arrowthick-2-n-s drag-handle ui-sortable-handle'])[1]",
				column2));
		Actions builder = new Actions(DriverManager.getDriver());
		Action dragAndDrop = builder.clickAndHold(from).moveToElement(to).release(to).build();
		dragAndDrop.perform();
		assertTrue(click("#btnApply"));
		return this;
	}

	public PlanningPage getcolumnToNumber(String number, String columnName) {
		clickPlanningGroupEditButton();
		switchToOuterFrame();
		assertTrue(click("//span[text()='Set My Preferences']"));
		String checkBox = format(
				"(//div[@typecode='%s']/../div//input[@class='chkVisibility'])[2]",
				columnName);
		if (!isElementPresent(checkBox))
			throw new SkipException(columnName + " is not present");
		if (getAttribute(checkBox, "orig-value").equals("false"))
			assertTrue(click(checkBox));
		String header = columnHeader + "[1]";
		scrollTo(format(header, columnName));
		assertTrue(clear(format(header, columnName)));
		assertTrue(setText(format(header, columnName), number));
		assertTrue(click("#btnApply"));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public PlanningPage getCompensationHistoryTCCTo(String number) {
		clickPlanningGroupEditButton();
		switchToOuterFrame();
		assertTrue(click("//span[text()='Set My Preferences']"));
		String checkBox = "(//div[@typecode='WS_Emp_Comp_History_TCC']/ancestor::li//div[@class='sp_cell dragHandle121 columnOrder input-cell']/following-sibling::div[@class='sp_cell columnVisibility column-visible']//input)[1]";
		if (getAttribute(checkBox, "orig-value").equals("false"))
			assertTrue(click(checkBox));
		String header1 = "(//div[@typecode='WS_Emp_Comp_History_TCC']//ancestor::li//div[@class='sp_cell dragHandle121 columnOrder input-cell']//input)[1]";
		scrollTo(header1);
		assertTrue(clear(format(header1)));
		assertTrue(setText(format(header1), number));
		assertTrue(click("#btnApply"));
		return this;
	}

	public PlanningPage getCompensationHistorynonTCCTo(String number) {
		clickPlanningGroupEditButton();
		switchToOuterFrame();
		assertTrue(click("//span[text()='Set My Preferences']"));
		String checkBox = "(//div[@typecode='WS_Emp_Has_Comp_History']/ancestor::li//div[@class='sp_cell dragHandle121 columnOrder input-cell']/following-sibling::div[@class='sp_cell columnVisibility column-visible']//input)[1]";
		if (getAttribute(checkBox, "orig-value").equals("false"))
			assertTrue(click(checkBox));
		String header1 = "(//div[@typecode='WS_Emp_Has_Comp_History']//ancestor::li//div[@class='sp_cell dragHandle121 columnOrder input-cell']//input)[1]";
		scrollTo(header1);
		assertTrue(clear(format(header1)));
		assertTrue(setText(format(header1), number));
		assertTrue(click("#btnApply"));
		return this;
	}

	public PlanningPage setFilter(String columnName, String text) {
		assertTrue(setText(format(sortBy, columnName) + "//input[@class='txt-search-term']", text));
		String applyFilter = "//div[@class='iconcontainer']/following::span[@dictionarykey='WS_Display_Name']/preceding::button[@class='btn-find-results pbtn']";
		assertTrue(click(applyFilter));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public PlanningPage removeFilter() {
		switchToInnerFrame();
		String removeFilter = "//div[@class='iconcontainer']/following::span[@dictionarykey='WS_Display_Name']/preceding::a[@class='wstmLink clear-filter-criteria clear-item']";
		assertTrue(click(removeFilter));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public PlanningPage rightClickOnColumnHeader(String columnName) {
		switchToInnerFrame();
		Actions actions = new Actions(driver);
		doWait(5000);
		hover(format("//span[@dictionarykey='%s']", columnName));
		doWait(5000);
		actions.contextClick(findElement(format("//span[@dictionarykey='%s']", columnName))).build().perform();
		return this;
	}

	public PlanningPage editColumnNameTo(String text) {
		switchToOuterFrame();
		assertTrue(clear("#inlineTokenId"));
		assertTrue(setText("#inlineTokenId", text));
		doWait(5000);
		assertTrue(click("#saveInlineTokenId"));
		assertTrue(waitUntilVisible("#btnOk"));
		assertTrue(click("#btnOk"));
		return this;
	}

	public PlanningPage resetColumnNameToDefault() {
		switchToOuterFrame();
		doWait(5000);
		assertTrue(click("#setDefaultTokenValue"));
		assertTrue(waitUntilVisible("#btnOk"));
		assertTrue(click("#btnOk"));
		return this;
	}

	public PlanningPage clickEmployeeNameLink(String emp) {
		switchToInnerFrame();
		assertTrue(click("//tr[@originalrowpos='0']/td[@typecode='WS_Display_Name']/a"));
		return this;
	}

	public PlanningPage isEmployeeDetailsPage() {
		switchToOuterFrame();
		assertTrue(isElementVisible("//*[text()='Employee Details']"));
		assertTrue(click(popUpClose));
		return this;
	}

	public PlanningPage isCompensationHistoryTcc() {
		switchToOuterFrame();
		assertTrue(isElementVisible("#COM_HISTORY_Prototype"));
		assertTrue(click(popUpClose));
		return this;
	}

	public PlanningPage isCompensationHistoryNonTCC() {
		switchToOuterFrame();
		assertTrue(isElementVisible("#EMPCOMPHISTORY_Prototype"));
		assertTrue(click(popUpClose));
		return this;
	}

	public PlanningPage isSpareFieldChangeAudit() {
		switchToOuterFrame();
		assertTrue(isElementVisible("#COM_EMP_SPARE_AUDIT_Prototype"));
		assertTrue(click(popUpClose));
		return this;
	}

	public PlanningPage isInlineJobSearch() {
		switchToOuterFrame();
		assertTrue(isElementVisible("#SEARCHJOB_Prototype"));
		assertTrue(click(popUpClose));
		return this;
	}

	public PlanningPage isAwardEntryAudit() {
		switchToOuterFrame();
		assertTrue(isElementVisible("#COM_AWARD_ENTRY_HISTORY_Prototype"));
		assertTrue(click(popUpClose));
		return this;
	}

	public PlanningPage isCalculationDetails() {
		switchToOuterFrame();
		assertTrue(isElementVisible("#CALCDETAILS_Prototype"));
		assertTrue(click(popUpClose));
		return this;
	}

	public PlanningPage isAuditAwardHistory() {
		switchToOuterFrame();
		assertTrue(isElementVisible("#COM_EMP_SPARE_AUDIT_Prototype"));
		assertTrue(click(popUpClose));
		return this;
	}

	public PlanningPage isalertPopUpPresent() {
		switchToOuterFrame();
		if (isElementVisible("#popoverContent"))
			;
		assertTrue(click("img#btnHideIcoClose"));
		return this;
	}

	public PlanningPage clickGroupNotes() {
		switchToInnerFrame();
		assertTrue(click("span#groupNotesContainer"));
		return this;
	}

	public PlanningPage clickIndividualNotes() {
		switchToInnerFrame();
		assertTrue(click("(//a[@wstmtitle='Add/Edit Notes'])[1]"));
		return this;
	}

	public PlanningPage clickCompensationHistoryNonTCC() {
		switchToInnerFrame();
		String nontcc = "(//a[@wstmtitle='View Comp History'])[1]";
		if (!isElementPresent(nontcc))
			throw new SkipException("Comp history non TCC is not present");
		assertTrue(click(nontcc));
		return this;
	}

	public PlanningPage clickCompensationHistoryTCC(String emp) {
		switchToInnerFrame();
		String el = "//td[@typecode='WS_Emp_Comp_History_TCC']//i[@class='tcc-comp-history fa fa-chart-line']";
		if (!isElementPresent(el))
			throw new SkipException("Comp history not present");
		assertTrue(click(format(el, emp)));
		return this;

	}

	public PlanningPage clickSpareFieldChange(String emp) {
		switchToInnerFrame();
		String el = format("//a[@empname='%s']//i[@class='tcc-emp-spares-audit fa fa-history']", emp);
		if (!isElementPresent(el))
			throw new SkipException("Spare field is not present");
		assertTrue(click(el));
		return this;
	}

	public PlanningPage clickAwardEntryHistory() {
		switchToInnerFrame();
		assertTrue(click(format("(//i[@class='tcc-comp-audit-history fa fa-history'])[1]")));
		return this;
	}

	public PlanningPage clickResetAwardsAction() {
		switchToInnerFrame();
		assertTrue(click("(//button[@title='Reset Awards' and @actiontype='ResetEmpAwards'])[1]//i"));
		if (isAlertPresent())
			alertAccept();
		assertTrue(waitForElementInvisible(pleaseWait));
		return this;
	}

	public PlanningPage clickMeritActualHistoryIcon() {
		switchToInnerFrame();
		String el = "(//td[@typecode='WS_Component_Actual_Percent@2']//i[@class='fa fa-history'])[1]";
		if (!isElementPresent(el))
			throw new SkipException("Merit history not present");
		assertTrue(click(el));
		return this;
	}

	public PlanningPage closePopUp() {
		switchToOuterFrame();
		assertTrue(click("//input[@class='sbtn btnClose']"));
		return this;
	}

	public PlanningPage enterIndividualNotes(String notes) {
		switchToOuterFrame();
		switchToFrame("//iframe[@id='newNotes_ifr']");
		// doWait (2000);
		// findElement ("#tinymce").clear ();
		doWait(2000);
		assertTrue(setText("#tinymce", notes));
		switchToOuterFrame();
		assertTrue(click("[class='btnSaveNotes pbtn']"));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public PlanningPage clickInlineJob() {
		switchToInnerFrame();
		if (!isElementPresent(inlineJob))
			throw new SkipException("Inline job not present");
		assertTrue(click(inlineJob));
		return this;
	}

	public PlanningPage clickCurrencyPayRate() {
		switchToInnerFrame();
		assertTrue(click("#currencyPayRate>i"));
		return this;
	}

	public PlanningPage refreshInnerFrame() {
		executeJScript("document.getElementById('app_frame').contentWindow.location.reload();");
		return this;
	}

	public PlanningPage clickHideAndSaveCurrency() {
		switchToOuterFrame();
		assertTrue(click("//label[@for='opthideCurrency']/preceding-sibling::input"));
		assertTrue(click("[class='btnSave pbtn']"));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public PlanningPage clickShowAndSaveCurrency() {
		switchToOuterFrame();
		assertTrue(click("//label[@for='optshowCurrencyName']/preceding-sibling::input"));
		assertTrue(click("[class='btnSave pbtn']"));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public boolean isCurrencyRateVisible() {
		switchToInnerFrame();
		return isElementVisible("//td//nobr[contains(text(),'USD')]");
	}

	public String isNotesHistory() {
		switchToOuterFrame();
		return getText("#oldNotes");

	}

	public PlanningPage clickExpandOrCollapseButotn() {
		switchToOuterFrame();
		String el = "//div[contains(@componentid,'GrpSummary') and @id='supporting-data-panel']//child::a[@title='Expand/Collapse']";
		if (!isElementPresent(el))
			throw new SkipException("Expand/collapse button not present");
		assertTrue(click(el));
		return this;
	}

	public boolean isPaginationPresent() {
		switchToInnerFrame();
		return isElementPresent("#paginationList");
	}

	public PlanningPage enterGroupNotesAndSave(String text) {
		String noteField = "[id='tinymce'][data-id='newNotes']>p";
		switchToOuterFrame();
		switchToFrame("iframe[id='newNotes_ifr']");
		assertTrue(clear(noteField));
		assertTrue(setText(noteField, text));
		switchToOuterFrame();
		assertTrue(click("[class='btnSaveNotes pbtn']"));
		pass("Entered group notes");
		return this;
	}

	public PlanningPage setGroupName(String groupName) {
		assertTrue(setText("#txt_WS_Group_Name", groupName));
		assertTrue(click("(//td[text()='Group Name']/following::input[@class='seachAction pbtn'])[1]"));
		assertTrue(waitForElementInvisible("//td[text()='Loading...']"));
		assertTrue(click(format("//span[contains(text(),'%s')]/preceding::input[@class='sp_checkbox']", groupName)));
		assertTrue(click("//input[@value='Accept']"));
		assertTrue(waitForElementInvisible(pleaseWait));
		return this;
	}

	public EmployeeDetailPage clickAnyEmployee(String empName) {
		switchToInnerFrame();
		String emp = format("//table/tbody/tr/td[@typecode='WS_Display_Name']/a[contains(text(),'%s')]", empName);
		assertTrue(click(emp));
		switchToOuterFrame();
		return new EmployeeDetailPage();
	}

	/* Plan Supporting Data Section */
	public PlanningPage clickPencilIcon() {
		switchToSupportingFrame();
		assertTrue(click(pencilIcon));
		return this;
	}

	public EmployeeDetailPage clickFirstEmployee() {
		switchToInnerFrame();
		String em = format("//table/tbody/tr[1]/td[@typecode='WS_Display_Name']//a");
		assertTrue(click(em));
		switchToOuterFrame();
		return new EmployeeDetailPage();
	}

	public PlanningPage clickSetMyPreferences() {
		switchToOuterFrame();
		assertTrue(click(setMyPref));
		return this;
	}

	public PlanningPage clickBudgetTab() {
		switchToOuterFrame();
		switchToSupportingFrame();
		assertTrue(click(budgetTab));
		return this;
	}

	public List<WebElement> checkChartsVisible() {
		return findElements(chartSection);
	}

	public boolean areChartsPresent() {
		List<WebElement> charts = findElements(chartSection);
		return areElementsVisible(charts);
	}

	public List<WebElement> getEmpTblFromDistFundPage() {
		doWait(3000);
		switchToSupportingFrame();
		return findElements(empDetailsTbl);
	}

	public String getGroupName(WebElement row) {
		WebElement name = row.findElement(By.xpath("td[@typecode='WS_Group_Title']/span"));
		return getText(name);
	}

	public PlanningPage clearBudgetAmtForGrp(WebElement row) {
		WebElement enterBudgetAmt = row.findElement(By.xpath("td[@wsmeta='Amount']/input"));
		clear(enterBudgetAmt);
		return this;
	}

	public PlanningPage setBudgetAmtForGrp(WebElement row) {
		WebElement enterBudgetAmt = row.findElement(By.xpath("td[@wsmeta='Amount']/input"));
		String rand = RandomStringUtils.randomNumeric(2);
		setText(enterBudgetAmt, rand);
		clickOnBlankSpace();
		return this;
	}

	public PlanningPage clickApplyBtn(WebElement row) {
		WebElement applyBudget = row.findElement(By.xpath("td[@typecode='WS_Action']/button"));
		waitForElementClickable(applyBudget);
		click(applyBudget);
		return this;
	}

	public boolean cascadeBudgetPresent() {
		switchToOuterFrame();
		switchToSupportingFrame();
		return isElementPresent("//td[contains(text(),'Cascading Budget') or contains(text(),'Cas Budget')]");
	}

	public PlanningPage applyBudgetAmt() {
		switchToOuterFrame();
		doWait(5000);
		assertTrue(click(confirmApply));
		doWait(1000);
		acceptAlertWithoutException();
		assertTrue(hasPageLoaded());
		doWait(2000);
		return this;
	}

	public int getCascadeBudget() {
		String cascadeBudgetVal = getText(
				"//td[contains(text(),'Cascading Budget') or contains(text(),'Cas Budget')]/following::td[1]");
		String removeUSD = cascadeBudgetVal.substring(0, cascadeBudgetVal.length() - 9);
		System.out.println(" RU " + removeUSD);
		int x = Integer.parseInt(removeUSD) / 1;
		return x;

	}

	/* Job Search and Promotion Page */
	public PlanningPage selectJob(WebElement row) {
		assertTrue(click(row.findElement(By.xpath("td[@typecode='WS_Job_Select']/button"))));
		waitForElementVisible(jobResultGrid);
		switchToOuterFrame();
		waitForElementVisible(jobRecomTitle);
		assertTrue(isElementPresent(jobRecomTitle));
		String jobRecommendation = findElement(jobRecomTitle).getText();
		System.out.println("Job Recommendation : " + jobRecommendation);
		switchToInnerFrame();
		return this;
	}

	public String selectedJobName(WebElement row) {
		WebElement name = row.findElement(By.xpath("td[@typecode='WS_Job_Title']/span"));
		return getText(name);
	}

	public PlanningPage openJobsearchAndPromotionPage() {
		switchToInnerFrame();
		assertTrue(click("//tr[@originalrowpos=0]//a[@data-type-code='WS_Job_Code']"));
		return this;
	}

	public PlanningPage searchJobWithSearchBtn() {
		switchToOuterFrame();
		assertTrue(click(jobSearchBtn));
		waitForElementVisible(jobGrid);
		assertTrue(isElementPresent(jobGrid));
		return this;
	}

	public PlanningPage closePopup() {
		assertTrue(click(closePopup));
		return this;
	}

	public String searchWithJobFamily(int index) {
		switchToInnerFrame();
		openJobsearchAndPromotionPage();
		switchToOuterFrame();
		String jobFamily = format("//select[@id='jobFamilySelect']/option[%d]", index);
		String selectedJobFamily = getAttribute(jobFamily, "value");
		System.out.println("Selected Job Family Main : " + selectedJobFamily);
		assertTrue(click(jobFamily));
		assertTrue(click(jobSearchBtn));
		return selectedJobFamily;
	}

	public String verifyJobFamily() {

		String jobFamilyInGrid = "//table[@componentid='planningEmpJobSearchResult']/tbody/tr[1]/td[@typecode='WS_Job_Family']/span";
		waitForElementVisible(jobFamilyInGrid);
		String verifyJobFamily = getText(jobFamilyInGrid);
		System.out.println("verify Job Family method : " + verifyJobFamily);
		assertTrue(click(closePopup));
		return verifyJobFamily;
	}

	public String searchJobWithJobGrade(int index) {
		switchToInnerFrame();
		openJobsearchAndPromotionPage();
		switchToOuterFrame();
		String jobGrade = format("//select[@id='jobGradeSelect']/option[%d]", index);
		assertTrue(click(jobGrade));
		String selectedJobGrade = getAttribute(jobGrade, "value");
		System.out.println("Selected Job Grade Main : " + selectedJobGrade);
		assertTrue(click(jobSearchBtn));
		return selectedJobGrade;

	}

	public String verifyJobGrade() {
		String jobGradeInGrid = format(
				"//table[@componentid='planningEmpJobSearchResult']/tbody/tr[1]/td[@typecode='WS_Job_Grade']/span");
		waitForElementVisible(jobGradeInGrid);
		String verifyJobGrade = getText(jobGradeInGrid);
		assertTrue(click(closePopup));
		return verifyJobGrade;
	}

	public PlanningPage searchJobWithJobTitle(String jobName) {
		openJobsearchAndPromotionPage();
		switchToOuterFrame();
		setText("input#txtJobTitle", jobName);
		assertTrue(click(jobSearchBtn));
		waitForElementVisible(jobGrid);
		info("Opened Job search and promotion pop up and searched job");
		return this;
	}

	public PlanningPage clickSelect() {
		switchToOuterFrame();
		assertTrue(click(
				"(//td[@typecode='WS_Job_Select']/button)[1]"));
		waitForElementInvisible(loading);
		return this;
	}

	public boolean selectJobAndAssertIfElementsDisplayed() {

		clickSelect();
		if (isElementPresent("//*[contains(text(),'Job Promotion Recommendation for')]")) {
			assertTrue(isElementPresent("//*[text()='Current Job :']"));
			assertTrue(isElementPresent("//*[text()='Proposed Job :']"));
			assertTrue(isElementPresent("#sp_planning[componentid='planningEmpPromotionAdjustment']"));
			assertTrue(isElementPresent("#btnJobPromotionDialogClose[value='Close']"));
			return true;
		} else
			return false;
	}

	public String verifyJobtitle() {
		String jobTitleInGrid = format(
				"//table[@componentid='planningEmpJobSearchResult']/tbody/tr[1]/td[@typecode='WS_Job_Title']/span");
		waitForElementVisible(jobTitleInGrid);
		String verifyJobTitle = getText(jobTitleInGrid).trim();
		assertTrue(click(closePopup));
		return verifyJobTitle;
	}

	public List<WebElement> getJobGridResultTbl() {
		switchToOuterFrame();
		return findElements(jobResultGrid);
	}

	public boolean selectJobInResultGrid() {

		return click(selectBtnFromTbl);

	}

	public PlanningPage closeJobPromotionPopup() {
		switchToOuterFrame();
		assertTrue(click(closeJobPromotionPopup));
		switchToInnerFrame();
		return this;
	}

	public String getProposedJob() {
		switchToOuterFrame();
		waitForElementVisible(proposedJob);
		String proposedJobInGrid = getText(proposedJob);
		return proposedJobInGrid;
	}

	public PlanningPage revertPromotion() {
		switchToOuterFrame();
		assertTrue(click(revertPromotionBtn));
		switchToInnerFrame();
		return this;
	}

	public boolean isRevertPromotion() {
		switchToOuterFrame();
		return isElementPresent(revertPromotionBtn);

	}

	public int getRandomValue() {
		return generateRandomInt(5, 9);
	}

	/* Spare Field Change History */
	public List<WebElement> empSpareHistory() {
		switchToInnerFrame();
		assertTrue(isElementPresent(empSparesHistory));
		List<WebElement> el = findElements(empSparesHistory);
		return el;
	}

	public PlanningPage employeeSpareHistory() {
		empSpareHistory();
		return this;
	}

	public PlanningPage isHistoryPresent() {
		switchToOuterFrame();
		waitForElementVisible(empSparePopUp);
		assertTrue(isElementPresent(empSparePopUp));
		doWait(2000);
		return this;
	}

	public PlanningPage closeEmpSpareHistoryPopup() {
		assertTrue(click(closeEmpSpareHistoryPopup));
		doWait(2000);
		switchToInnerFrame();
		return this;
	}

	public PlanningPage clickPerformanceDistributionTab() {
		String pd = format("//ul[@id='topNavigationPanel']/li/a/span[text()='Performance Distribution']");
		if (!isElementPresent(pd))
			throw new org.testng.SkipException("Compa Ratio Related Tab is not present");
		assertTrue(click(pd));
		return this;
	}

	public PlanningPage clickCompaRatioTab() {
		String cr = format("//ul[@id='topNavigationPanel']/li/a/span[text()='Compa Ratio']");
		if (!isElementPresent(cr))
			throw new org.testng.SkipException("Compa Ratio Related Tab is not present");
		assertTrue(click(cr));
		return this;
	}

	public PlanningPage clickSalaryComparisonTab() {
		String sc = format("//ul[@id='topNavigationPanel']/li/a/span[text()='Salary Comparison']");
		if (!isElementPresent(sc))
			throw new org.testng.SkipException("Compa Ratio Related Tab is not present");
		assertTrue(click(sc));
		return this;
	}

	public Boolean isChartXaxisPresent() {
		assertTrue(isElementPresent(chartXaxis));
		List<WebElement> empNames = findElements(chartXaxis);
		for (WebElement emp : empNames) {
			info("Emp Names in X axis  " + emp.getText());
		}
		return true;
	}

	public Boolean isChartYaxisPresent() {
		assertTrue(isElementPresent(chartYaxis));
		List<WebElement> empNames = findElements(chartXaxis);
		for (WebElement emp : empNames) {
			info("Ratio No Y axis  " + emp.getText());
		}
		return true;
	}

	public String getInlineJobCode() {
		switchToInnerFrame();
		return getText(inlineJob);
	}

	public Boolean isSelectedEmpSpareColumnVisible(String empSpare) {
		switchToInnerFrame();
		String checkCol = format("//th[contains(@typecode,'%s')]", empSpare);
		assertTrue(isElementPresent(checkCol));
		return true;
	}

	public List<WebElement> getEmpHistoryInputs() {
		List<WebElement> x = findElements("//td[@typecode='WS_Spare_BONUS_ELIGIBLE@1']/input");
		return x;
	}

	public PlanningPage enterValueForHistory(String spare, String bonus) {
		String sp = format("(//td[contains(@typecode,'%s')])[1]//input", spare);
		clear(sp);
		setText(sp, bonus);
		clickOnBlankSpace();
		doWait(3000);
		return this;
	}

	public PlanningPage clickHistoryIcon(String spare) {
		assertTrue(click(format("(//td[contains(@typecode,'%s')]//img)[1]  | (//td[contains(@typecode,'%s')]//a)[1]",
				spare, spare)));
		return this;
	}

	public PlanningPage closeHistoryPopup() {
		switchToOuterFrame();
		String closeHistory = "//button[@title='Close']";
		String selectDate = "//td//a[@data-date='9']";
		if (isElementPresent(closeHistory))
			assertTrue(click(closeHistory));
		else {
			doWait(500);
			waitForElementVisible(selectDate);
			assertTrue(click(selectDate));
		}

		switchToInnerFrame();
		return this;
	}

	public PlanningPage withdrawIfPlanIsSubmitted() {
		switchToInnerFrame();
		WebElement w = findElement(withDraw);
		WebElement s = findElement(submit);
		if (w.isEnabled()) {
			assertTrue(click(withDraw));
			doWait(3000);
			switchToOuterFrame();
			assertTrue(click(confirm));
			acceptAlertWithoutException();
			return this;
		} else if (!w.isEnabled() && !s.isEnabled()) {
			return this;
		} else {
			return this;
		}
	}

	public PlanningPage checkIfAddedEmpSpareFieldIsEnabled(String empSpare) {
		WebElement empSpareCol = findElement(
				format("(//div[@typename='%s']/../div//input[@class='chkVisibility'])[1]", empSpare));
		System.out.println(empSpareCol);
		if (!empSpareCol.isSelected()) {
			assertTrue(click(empSpareCol));
			assertTrue(click(confirmApply));
			return this;
		} else {
			assertTrue(click(popUpClose));
			return this;
		}

	}

	public PlanningPage clickSetPreferencesInPlanningPage() {
	    clickPlanningGroupEditButton();
        switchToOuterFrame();
        assertTrue(click("//span[text()='Set My Preferences']"));
		return this;
	}

	public PlanningPage enableColumnFromPreferences(String columnTypecode) {
		switchToOuterFrame();
		String spare = format("//div[@typecode='%s']/../div//input[@class='chkVisibility']", columnTypecode);
		if (!isElementPresent(spare))
			throw new org.testng.SkipException("Emp Spare History Column is not Present for the plan " + Constants.compPlanName);
		WebElement colXpath = findElement(
				format("//div[@typecode='%s']/../div//input[@class='chkVisibility']", columnTypecode));
		if (!colXpath.isSelected()) {
			assertTrue(click(colXpath));
			assertTrue(click(confirmApply));
			return this;
		}
		assertTrue(click(confirmApply));
		return this;
	}

	public PlanningPage clickSummaryTab() {
		switchToInnerFrame();
		assertTrue(click("//li[@title='Summary']"));
		return this;
	}

	public PlanningPage clickFirstTab() {
		switchToInnerFrame();
		assertTrue(click("//body[@class='compAppFrame']//ul[@id='topNavigationPanel']//li[1]"));
		return this;
	}

	public PlanningPage clickEditIconForSupportingSection() {
		switchToSupportingFrame();
		assertTrue(click("//div/img[@id='prefs_supportingDataViews']"));
		return this;
	}

	public PlanningPage enableBudgetTab() {
		clickSetMyPreferences();
		enableColumnFromPreferences("WS_Budget_View");
		return this;
	}

	public PlanningPage enableCompaRatioTab() {
		clickSetMyPreferences();
		enableColumnFromPreferences("WS_Compa_Ratio_View");
		return this;
	}

	public PlanningPage enablePerformanceDistributionTab() {
		clickSetMyPreferences();
		enableColumnFromPreferences("WS_Performance_Distri_View");
		return this;
	}

        public PlanningPage enablePrint () {
            String checkBox = "(//*[@id='wstmPreferenceListLeft']//*[@dictionarykey='ui_prefs_printable' and text()='Printable']/..//input)[2]";
            if (!findElement (checkBox).isSelected ())
                assertTrue (click (checkBox));
            assertTrue(click("#btnApply"));
            assertTrue(waitForElementInvisible(loading));
            return this;
        }

}
