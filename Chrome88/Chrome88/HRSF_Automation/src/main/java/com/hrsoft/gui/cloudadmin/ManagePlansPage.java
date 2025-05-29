package com.hrsoft.gui.cloudadmin;

import static com.hrsoft.reports.ExtentLogger.pass;
import static java.lang.String.format;
import static org.testng.Assert.assertTrue;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import com.hrsoft.utils.seleniumfy.BasePage;

public class ManagePlansPage extends BasePage {
	private String loadingIndicators = "img[alt='loading...']";
	private String saveBtnInSpareField = "//input[@value='Save' and @name='save']";
	private String printBtn = "input#btnPrint";

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy(".modal-title"));
	}

	public ManagePlansPage clickOpenButtonForPlan(int plan) {
		switchToManageCpcFrame();
		assertTrue(click(format("//button[contains(@planid,%d) and text()='Open']", plan)));
		pass("Opened manage plans for " + plan);
		switchToDefaultContent();
		return this;
	}

	public ManagePlansPage switchToManageCpcFrame() {
		switchToDefaultContent();
		switchToFrame("//iframe[@id='manageCPC']");
		return this;
	}

	public ManagePlansPage clickProgramsTab() {
		switchToDefaultContent();
		assertTrue(click("//li//a[text()='Programs' and @pageid='WZ_CPC_Programs']"));
		return this;
	}

	public ManagePlansPage clickComponentsTab() {
		switchToDefaultContent();
		assertTrue(click("//li//a[text()='Components' and @pageid='WZ_CPC_Components']"));
		return this;
	}

	public ManagePlansPage clickStagingTab() {
		switchToDefaultContent();
		assertTrue(click("//li//a[text()='Staging' and @pageid='WZ_CPC_Staging']"));
		if (isAlertPresent())
			alertAccept();
		return this;
	}

	public ManagePlansPage clickSpareFieldsTab() {
		switchToDefaultContent();
		assertTrue(click("//li/a[text()='Spare Fields' and @pageid='WZ_CPC_SpareFields']"));
		waitForElementInvisible(loadingIndicators);
		return this;
	}

	public ManagePlansPage clickEditButtonForSalary() {
		switchToWzCpcProgramsSummarySectionFrame();
		assertTrue(click(
				"//table[@id='ShowData']//following::td[text()='Salary']//preceding-sibling::td//button[text()='Edit']"));
		return this;
	}

	public ManagePlansPage clickCheckBoxShowOnlyEligibleEmployeesInProgramViews() {
		switchToWzCpcProgramsGeneralDetailsSectionFrame();
		String checkbox = "//table[@id='Table2']//following::input[@tooltipsrc='showOnlyEligibleEmployees-tooltip' and @name='showOnlyEligibleEmployees']";
		if (!findElement(checkbox).isSelected())
			assertTrue(click(checkbox));
		return this;
	}

	public ManagePlansPage clickSave() {
		switchToDefaultContent();
		assertTrue(click("//input[@id='btnSave' or text()='Save']"));
		return this;
	}

	public ManagePlansPage clickSaveInPopUp() {
		switchToWzCpcComponentsRoundingAndEligibiltySection();
		assertTrue(click("//input[@name='eventSubmit_doSave']"));
		return this;
	}

	public ManagePlansPage clickCheckStatus() {
		switchToWzCpcComponentsRoundingAndEligibiltySection();
		assertTrue(click("//input[@name='eventSubmit_doValidate']"));
		return this;
	}

	public String getStatusText() {
		switchToWzCpcComponentsRoundingAndEligibiltySection();
		return getText("textarea#Textarea1");
	}

	public ManagePlansPage selectSalaryFromSelectProgram() {
		switchToDefaultContent();
		clickAndSelectText(
				"[parentid='itemWZ_CPC_Components_programSelectionSection'][name='itemWZ_CPC_Components_selectProgram']",
				"Salary");
		return this;
	}

	public List<String> getListOfComponents() {
		switchToWzCpcComponentsSummarySectionFrame();
		return getTextList("//table[@id='ShowData']//tbody//child::tr[@class='backgroundWhite']//td[2]");
	}

	public ManagePlansPage clickEditButtonFor(String program) {
		switchToWzCpcComponentsSummarySectionFrame();
		assertTrue(click(format(
				"//table[@id='ShowData']//following::td[text()='%s']//preceding-sibling::td//button[text()='Edit']",
				program)));
		waitUntilVisible("//*[text()='Eligibility for this Component']");
		return this;
	}

	public ManagePlansPage clickEnterFormulaRadioButton() {
		switchToWzCpcComponentsRoundingAndEligibiltySection();
		String radioBtn = "//td[contains(text(),'Enter Formula')]//input[@name='eligibility']";
		if (!findElement(radioBtn).isSelected())
			assertTrue(click(radioBtn));
		return this;
	}

	public ManagePlansPage clickAlwaysEligibleRadioButton() {
		switchToWzCpcComponentsRoundingAndEligibiltySection();
		String radioBtn = "//td[contains(text(),'Always Eligible')]//input[@name='eligibility']";
		if (!findElement(radioBtn).isSelected())
			assertTrue(click(radioBtn));
		return this;
	}

	public ManagePlansPage clickEnterFormulaButton() {
		// switchToFrame ("//iframe[@name='WZ_CPC_Components_baseAmountSection']");
		switchToWzCpcComponentsRoundingAndEligibiltySection();
		assertTrue(click(
				"//legend[text()='Eligibility for this Component']//following::td[contains(text(),'Enter Formula')]//following-sibling::td//nobr//input[@id='FormulaEditor']"));
		return this;
	}

	public ManagePlansPage enterFormulaInFormulaTextBox(String formula) {
		switchToWzCpcComponentsRoundingAndEligibiltySection();
		String field = "//textarea[@name='formula']";
		clear(field);
		setText(field, formula);
		return this;
	}

	public ManagePlansPage clickProgramsCheckBoxInComponentSection() {
		switchToCalcEngineFrame();
		String programCheckBox = "//input[@id='selectAllButton']";
		waitUntilVisible(programCheckBox);
		doWait(3000);
		if (!findElement(programCheckBox).isSelected())
			assertTrue(click(programCheckBox));
		return this;
	}

	public ManagePlansPage clickCalculateSelectedComponentsAndClickOk() {
		switchToCalcEngineFrame();
		assertTrue(click("//input[@id='StartButton']"));
		if (isAlertPresent()) {
			alertAccept();
			clickProgramsCheckBoxInComponentSection();
		}
		doWait(60000);
		waitForElementInvisible("//h1[contains(text(),'Calc Engine will continue to run in the background')]");
		waitUntilVisible("input#OkButton");
		assertTrue(click("input#OkButton"));
		return this;
	}

	public ManagePlansPage switchToWzCpcProgramsSummarySectionFrame() {
		switchToDefaultContent();
		switchToFrame("//iframe[@name='WZ_CPC_Programs_summarySection']");
		return this;
	}

	public ManagePlansPage switchToCalcEngineFrame() {
		doWait(2000);
		switchToDefaultContent();
		switchToFrame("//iframe[@name='WZ_CPC_Staging_summarySection']");
		switchToFrame("//iframe[@id='calcEngine_frame']");
		return this;
	}

	public ManagePlansPage switchToSpareFieldsSectionFrame() {
		switchToDefaultContent();
		switchToFrame("//iframe[@name='WZ_CPC_SpareFields_empSpareFieldsSection']");
		return this;
	}

	public ManagePlansPage switchToWzCpcComponentsRoundingAndEligibiltySection() {
		switchToDefaultContent();
		switchToFrame("//iframe[@name='WZ_CPC_Components_roundingAndEligibiltySection']");
		return this;
	}

	public ManagePlansPage switchToWzCpcComponentsSummarySectionFrame() {
		switchToDefaultContent();
		switchToFrame("//iframe[@name='WZ_CPC_Components_summarySection']");
		return this;
	}

	public ManagePlansPage switchToWzCpcProgramsGeneralDetailsSectionFrame() {
		switchToDefaultContent();
		switchToFrame("//iframe[@name='WZ_CPC_Programs_generalDetailsSection']");
		return this;
	}

	public List<WebElement> getSpareFieldsTableData() {
		switchToSpareFieldsSectionFrame();
		return findElements("//table[@class='backgroundTan']/tbody/tr");
	}

	public String getSpareFieldNames(WebElement row) {
		return row.findElement(By.xpath("td[3]")).getText();
	}

	public String getSpareFields(WebElement row) {
		return row.findElement(By.xpath("td[2]")).getText();
	}

	public ManagePlansPage selectAllUsersOption(String spare) {
		driver.switchTo().frame("WZ_CPC_SpareFields_empSpareFieldsSection");
		// switchToFrame("WZ_CPC_SpareFields_empSpareFieldsSection");
		// WebElement dropdown = row.findElement(By.xpath("td[6]/select"));
		String selectAll = format("//td[2][text()='%s']//../td[6]//select", spare);
		WebElement x = findElement(selectAll);
		Select select = new Select(x);
		select.selectByVisibleText("ALL EMPLOYEES");
		return this;
	}

	public ManagePlansPage plannerCanEdit(String spare) {
		String edit = format("//td[2][text()='%s']//..//td[7]//input", spare);
		WebElement x = findElement(edit);
		if (!x.isSelected())
			assertTrue(click(x));
		else {
		}
		return this;
	}

	public ManagePlansPage saveEmpSpareFields() {
		assertTrue(click(saveBtnInSpareField));
		navigateToTab(0);
		return this;
	}

	public ManagePlansPage clickPrintBtn() {
		assertTrue(click(printBtn));
		return this;
	}

	public String getCompensationDocPageTitle() {
		navigateToTab(1);
		String title = driver.getTitle();
		return title;
	}

	public String getPlanningStage() {
		return getText("//img[@class='default-item']/../following-sibling::td[2]");
	}

	public void clickRevertInStaging() {
		assertTrue(click("input[id$='stageBackwardButton']"));
	}

	public void switchToStagingFrame() {
		switchToDefaultContent();
		switchToFrame("//iframe[@name='frame_staging']");
		if (isAlertPresent())
			alertAccept();
	}
}
