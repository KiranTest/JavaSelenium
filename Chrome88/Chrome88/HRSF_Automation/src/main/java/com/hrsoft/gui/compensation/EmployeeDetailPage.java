package com.hrsoft.gui.compensation;

import static java.lang.String.format;
import static org.testng.Assert.assertTrue;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.hrsoft.constants.Constants;
import com.hrsoft.utils.seleniumfy.BasePage;

/**
 * @author Mashood khan <a href="mailto:mashood.khan@hrsoft.com</a>
 */
public class EmployeeDetailPage extends BasePage {

	private String clickEmpDropDownList = "//select[@id='employeeSelector']";
	private String selectAnyEmpFromDropDownList = "//select[@id='employeeSelector']/option";
	private String previousIcon = "//i[@title='Previous']";
	private String nextIcon = "//i[@title='Next']";
	private String selectedEmployee = "//select[@id='employeeSelector']/option[@selected='selected']";
	private String NameUnderEmpData = "//tr/td/span[@dictionarykey='WS_Display_Name']/following::td[1]";
	private String loading = "//div[@class='loadingData']/img";
	private String notesIcon = "//span[@id='indvData']/span/a[@wstmtitle='Add/Edit Notes']";
	private String notesInput = "//body[@id='tinymce']";
	private String saveNotesBtn = "//input[@value='Save' and contains(@class,'btnSaveNotes')]";
	private String closeEmpDetailsPopup = "//input[@value='Close']";
	private String notesStatusClose = "#statusClose";
	private String calculationDetailsPopUp = "//span[text()='Calculation Details']";
	private String closeCalcPopUp = "//*[text()='Close']";
	private String loadingIcon = "//*[@class='compAppFrame']//*[text()='Loading...']";
	private String openAnyEmpDetailPage = "//table/tbody/tr[1]/td[@typecode='WS_Display_Name']/a";
	private String component = "//td[@typecode='WS_Component@" + Constants.salaryProgramId + "'" + "]//a";
	private String empDataPreferencesIcon = "//div[@id='empData']//span/img[@title='Preferences']";
	private String closePopupBtn = "//input[@value='Close']";

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy("div.scopeAndFilters"));
	}

	public void clickSummaryTab() {
		switchToOuterFrame();
		assertTrue(click("//ul[@id='navPanel']//a[text()='Summary']"));
	}

	public EmployeeDetailPage clickEmpDataPreferencesIcon() {
		assertTrue(click(empDataPreferencesIcon));
		return this;
	}

	public void clickPrevEmployee() {
		waitForElementVisible(previousIcon);
		assertTrue(click(previousIcon));
	}

	/* Next Icon */

	public EmployeeDetailPage clickNextEmployee() {
		waitForElementVisible(nextIcon);
		assertTrue(click(nextIcon));
		return this;
	}
	/* select any emp from dropdown */

	public void selectAnyEmpFromDropDown() {
		List<WebElement> li = findElements("//select[@id='employeeSelector']/option");
		if (li.size() > 1) {
			assertTrue(click("//select[@id='employeeSelector']/option[2]"));
		} else {
			assertTrue(click("//select[@id='employeeSelector']/option[1]"));
		}
	}

	public String getEmpNameWithPrevIcon() {
		assertTrue(waitForElementInvisible(loading));
		return getText(format(selectedEmployee).trim());
	}

	public String getPrevIconEmpInEmpDataSection() {
		assertTrue(waitForElementInvisible(loading));
		return getText(format(NameUnderEmpData)).trim();
	}

	public String getEmpNameWithNextIcon() {
		assertTrue(waitForElementInvisible(loading));
		return getText(format(selectedEmployee)).trim();
	}

	public String getNextIconEmpInEmpDataSection() {
		assertTrue(waitForElementInvisible(loading));
		return getText(format(NameUnderEmpData)).trim();
	}

	public String getEmpNameSelectedFromDropDown() {
		assertTrue(waitForElementInvisible(loading));
		return getText(format(selectedEmployee)).trim();
	}

	public String getEmpNameInDataSectionForSelectedDropDown() {
		assertTrue(waitForElementInvisible(loading));
		return getText(format(NameUnderEmpData)).trim();
	}

	public EmployeeDetailPage addIndividualNote(String addNotes) {
		assertTrue(click(notesIcon));
		switchToDefaultContent();
		switchToOuterFrame();
		switchToFrame("iframe[id='newNotes_ifr']");
		waitForElementVisible(notesInput);
		doWait(2000);
		findElement(By.xpath(notesInput)).clear();
		findElement(By.xpath(notesInput)).sendKeys(addNotes);
		switchToOuterFrame();
		assertTrue(click(saveNotesBtn));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public String setPerfRatingFromPopup() {
		switchToInnerFrame();
		switchToOuterFrame();
		waitForElementVisible(previousIcon);
		String p = clickAndSelectIndex("//select[@class='perfselectInput']", 1);
		System.out.println(p);
		return p;
	}

	public void clickNewPerfRate() {
		switchToDefaultContent();
		switchToInnerFrame();
		assertTrue(click(openAnyEmpDetailPage));
		switchToOuterFrame();
		waitForElementVisible(previousIcon);
	}

	public String getPerfRating(int option) {
		switchToOuterFrame();
//		String perfRatingxpath = format("//td/select[@id='1_sp_list']/option[%d]",
//				option);
		clickAndSelectIndex("//select[@id='1_sp_list']", 2);
		doWait(2000);
		String selectedPerfRate = "//td/select[@id='1_sp_list']/option[@selected]";
		System.out.println("Selected Perf Rate : " + getText(selectedPerfRate));
		doWait(2000);
		return getText(selectedPerfRate);
	}

	public String selectedPerfShownInGrid() {
		assertTrue(click(closePopupBtn));
		hasPageLoaded();
		switchToInnerFrame();
		waitForElementInvisible(loadingIcon);
		String selectedPerf = format(
				"(//tr[@originalrowpos=0]/td[contains(@typecode,'WS_New_Performance')])[1]/select/option[@selected]");
		return getText(selectedPerf);
	}

	/* Salary Tab Methods */
	public EmployeeDetailPage selectSalaryProgram(String program) {
		switchToOuterFrame();
		String programTab = format("//ul[@id='navPanel']/li/a[text()='%s']", program);
		waitForElementVisible(programTab);
		assertTrue(click(programTab));
		return this;
	}

	public EmployeeDetailPage clickComponentTab(String component) {
		switchToOuterFrame();
		String componentTab = format("//a[text()='%s']", component);
		assertTrue(click(componentTab));
		doWait(2000);
		return this;
	}

	public EmployeeDetailPage closeEmpDetailsPopup() {
		switchToOuterFrame();
		assertTrue(click(closeEmpDetailsPopup));
		return this;
	}

	public EmployeeDetailPage closeCalcPopup() {
		List<WebElement> el = findElements(closeCalcPopUp);
		if (!el.isEmpty()) {
			el.get(1).click();
		}
		return this;
	}

	public String previousActualAmountValue(WebElement row) {
		WebElement awardAmtValue = row.findElement(By.xpath("td[@wsmeta='Amount']/input"));
		return getAttribute(awardAmtValue, "value");
	}

	public EmployeeDetailPage changeAwardAmtPercent(String changeAwardValue) {
		String awardPercentValue = format(
				"//div[@componentid='planningEmpDetail_components_9']/div/div/div[@class='gridBodyDiv']/div/table/tbody/tr/td[@typecode='WS_Actual_Percent@9']/input");
		clear(awardPercentValue);
		setText(awardPercentValue, changeAwardValue);
		clickOnBlankSpace();
		doWait(2000);
		return this;
	}

	public String newActualAmountValue() {
		WebElement awardAmtValue = findElement(
				"//div[contains(@class,'programTab') and contains(@id,'componentdata_9')]/div/div/div/div[@class='gridBodyDiv']/div/table/tbody/tr/td[3]/input");
		return getAttribute(awardAmtValue, "value");
	}

	/* New */

	public String getActualAmountValue(int rowNum, int id) {
		switchToOuterFrame();
		String xpath = format("//tr[@originalrowpos=%d]/td[contains(@typecode,'WS_Actual_Amount@%d')]//input", rowNum,
				id);
		String amount = getAttribute(xpath, "value");
		System.out.println(amount);
		return amount;
	}

	public EmployeeDetailPage enterActualAmtPercent(int rowNum, int id, String inputValue) {
		String xpath = format("//tr[@originalrowpos=%d]/td[contains(@typecode,'WS_Actual_Percent@%d')]//input", rowNum,
				id);
		clear(xpath);
		setText(xpath, inputValue);
		clickOnBlankSpace();
		doWait(3000);
		return this;
	}

	public EmployeeDetailPage clickComponent() {
		switchToOuterFrame();
		System.out.println("component Xpath :" + component);
		waitForElementVisible(component);
		assertTrue(click(component));
		return this;
	}

	public Boolean isCalculationPopupPresent() {
		assertTrue(isElementPresent("//span[text()='Calculation Details']"));
		return true;
	}

}
