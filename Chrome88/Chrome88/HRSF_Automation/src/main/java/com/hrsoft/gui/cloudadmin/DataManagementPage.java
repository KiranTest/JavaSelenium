package com.hrsoft.gui.cloudadmin;

import static java.lang.String.format;
import static org.testng.Assert.assertTrue;
import static com.hrsoft.reports.ExtentLogger.info;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.hrsoft.annotations.Author;
import com.hrsoft.constants.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import static com.hrsoft.driver.DriverManager.getDriver;
import com.hrsoft.gui.HRSoftPage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

public class DataManagementPage extends HRSoftPage {

	private String productListDropDown = "//div[@itemid='select_a_aeedd78e']//div[@class='webix_el_box']//input[@role='combobox']";
	private String navTabXPath = "//div[@itemid='NAVIGATION_296057045140243']//ul[@subnavtype='horizontal']//li[@title='%s']//a";
	private String navConfigFeedSubTabXPath = "//div[@itemid='NAVIGATION_297266794250062']//ul[@subnavtype='horizontal']//li[@title='%s']//a";
	private String feedCfgFieldMappingGrid = "//div[@itemid='GRID_413115183171617']//div[@role='grid']";
	private String selectDataTypeFilter = "//div[@class='webix_hs_center']//div[@class='webix_hcolumn']//div[@row='1'][@column='6']//select";
	private String selectFeedFieldFilter = "//div[@class='webix_hs_center']//div[@class='webix_hcolumn']//div[@row='1'][@column='3']//select";
	private String selectFeedFieldDataTypeFilter = "//div[@class='webix_hs_center']//div[@class='webix_hcolumn']//div[@row='1'][@column='6']//select";
	private String firstInlineEditAnchor = "//div[@class='webix_ss_body']//div[@class='webix_ss_left']//div[@column='0']//div[@aria-rowindex='1']//a[@title='Edit']";
	private String saveAndCloseButtonOnEditRecordPopup = "//button[text()='Save & Close']";
	private String dataFileListDropDown = "//div[@itemid='MENU_335629169244049']//input";
	private static final String ClientName = Constants.custId;
	private int defaultMinNonEmptyPercent = 50;

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy(productListDropDown));
	}

	public void clickFiles() {
		assertTrue(click("//li[text()='Files']"));
		navigateToTab(1);
	}

	public DataManagementPage clickFeedManagement() {
		assertTrue(click("//span[text()='Feed Management']"));
		waitForElement("//div[@itemid='GRID_138951714897547']");
		return this;
	}

	public DataManagementPage clickConfigureFeed() {
		click(format(navTabXPath, "Configure Feed"));
		waitForElement("//div[@itemid='MENU_297564209205268']");
		return this;
	}

	public void clickEndpointManagement() {
		waitForElement("//div[@itemid='TEXT_5111522756962333']");
		assertTrue(click("//div[@itemid='TEXT_5111522756962333']"));
		waitForElement("//div[@itemid='GRID_3272879231117616']");
	}

	public DataManagementPage changeDelimiterTo(String delimiter) {
		click("//div[@itemid='MENU_307775497938041']");
		selectGivenWebixItemIdFromOpenWebixList(delimiter);
		doWait(2000l);
		return this;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning String instead PageObject , This is
	 *          written intentionally to roll back changes because of test case as
	 *          well as assert the changes
	 */
	public String getSelectedDelimiter() {
		String existingDelimiter = findElement("//div[@itemid='MENU_307775497938041']//input").getAttribute("value");

		int t_index = existingDelimiter.indexOf('(');
		if (t_index > 0 && existingDelimiter.length() > t_index + 2)
			return existingDelimiter.substring(t_index + 1, t_index + 2);
		return existingDelimiter;
	}

	public DataManagementPage clickAPITransformAttributes() {
		waitForElement("//div[@itemid='TEXT_adebedb8']");
		assertTrue(click("//div[@itemid='TEXT_adebedb8']"));
		waitForElement("//div[@itemid='GRID_776907858256442']");
		return this;
	}

	public DataManagementPage clickTreeCloseOfGrid() {
		doWait(500);
		click("//button[contains(@clickexpr,'ThisObject.Reload ( )')]");
		doWait(500);
		assertTrue(click(
				"//div[@itemid='GRID_3272879231117616']//div[contains(@class,'webix_column  webix_first')]/div[@role='gridcell' and @aria-rowindex='1']/div[@class='webix_tree_close']"));
		doWait(500);
		return this;
	}

	public boolean isEndpointNameInputBoxIsDisable() {
		boolean check = false;
		String inputText = findElement("//input[contains(@id,'fldendpoint_name')]").getAttribute("readonly");
		if (inputText.equals("readonly")) {
			return true;
		}
		return check;
	}

	public DataManagementPage scrollhorizontal(String itemid, int xoffset) {
		doWait(2000);
		String xpath = "//div[@itemid='" + itemid + "']//div[@aria-orientation='horizontal']";
		WebElement slider = findElement("//div[@itemid='" + itemid + "']//div[@aria-orientation='horizontal']");
		if (slider.isDisplayed()) {
			System.out.println("Scroll Present in Field Mapping");
			Actions action = new Actions(getDriver());
			action.clickAndHold(slider).moveByOffset(xoffset, 0).release().perform();
		} else {
			System.out.println("Scroll Not Present in Field Mapping");
		}
		return this;
	}

	public String getOriginalValueOfInputboxGridInlineEdit(String itemid) {
		String xpath = "//input[contains(@id,'" + itemid + "')]";
		String originalvalue = findElement(xpath).getAttribute("value");
		return originalvalue;
	}

	public DataManagementPage passNewValueInputBoxGridInlineEdit(String itemid, String text) {
		String xpath = "//input[contains(@id,'" + itemid + "')]";
		findElement(xpath).clear();
		findElement(xpath).sendKeys(text);
		return this;
	}

	public DataManagementPage clickSaveAndCloseEditRecordPopupOfGrid() {
		doWait(2000);
		System.out.println(findElement("//div[@class='modal-footer ui-draggable-handle']/button[text()='Save & Close']")
				.getSize());
		assertTrue(click("//div[@class='modal-footer ui-draggable-handle']/button[text()='Save & Close']"));
		doWait(2000);
		return this;
	}

	public void isNewEndPointPathPresentInGrid(String text) {
		String xpath = "//div[@aria-rowindex=\"1\"]//span[text()='" + text + "']";
		waitForElement("//div[@itemid='GRID_3272879231117616']");
		scrollTo(xpath);
		assertTrue(isElementPresent(xpath));
	}

	public void switchToNewWindow() {
		navigateToTab(1);
		driver.manage().window().maximize();
	}

	public DataManagementPage closePopup() {
		if (isElementPresent("//button[@class='bootbox-close-button close']")) {
			assertTrue(click("//button[@class='bootbox-close-button close']"));
		}
		return this;

	}

	public void selectFeeds(String text) {
		assertTrue(click(productListDropDown + "[4]"));
		// assertTrue (click (format ("(//div[text()='%s'])[2]", text)));
		assertTrue(selectGivenListItemFromOpenWebixList(text));
	}

	public DataManagementPage chooseFeedsInConfigureFeed(String text) {
		String xpath = "//div[@itemid='MENU_297564209205268']";
		assertTrue(click(xpath));
		// assertTrue(selectGivenListItemFromOpenWebixList(text));
		if (!selectGivenListItemFromOpenWebixList(text)) {
			info("File Feed is not available");
			throw new org.testng.SkipException("Skipping test case because File Feed is not available");
		}
		return this;
	}

	public DataManagementPage clickFieldMapping() {
		click(format(navConfigFeedSubTabXPath, "Field Mapping"));
		waitForElement("//div[@itemid='GRID_413115183171617']");
		return this;
	}

	public DataManagementPage clickDefineFeed() {
		click(format(navConfigFeedSubTabXPath, "Define Feed"));
		waitForElement("//div[@itemid='MENU_998858653979595']");
		return this;
	}

	public DataManagementPage clickFeedClone() {
		doWait(500);
		assertTrue(click("//div[@itemid='BUTTON_138216617040042']//span[text()='Clone Feed']"));
		waitForElement("//div[@class='modal-content']//input[@id='promptBox']");
		return this;
	}

	public boolean clickSubmitOfFeedClonePopup() {
		String xpath = "//div[@class='modal-dialog']//button[text()='Submit']";
		assertTrue(click(xpath));
		doWait(500);
		String feedname = "1_automation_test_Clone_New";
		String duplicate_xpath = "//div[@class='modal-dialog']//div[text()='File feed with this name already exists. Please use a different name.']";
		if (isElementPresent(duplicate_xpath)) {
			assertTrue(click("//div[@class='modal-dialog']//button[text()='OK']"));
			assertTrue(isElementPresentInColumnOfGrid(1, feedname, "GRID_138951714897547"));
			deleteRowForGivenFeedName(feedname);
			return false;
		} else {
			assertTrue(isElementPresent("//div[@class='modal-dialog']//div[text()='File feed cloned successfully!']"));
			assertTrue(click("//div[@class='modal-dialog']//button[text()='OK']"));
			return true;
		}
	}

	public DataManagementPage clickOkButtonOnPopup() {
		click("//div[@class='modal-dialog']//button[text()='OK']");
		return this;
	}

	public boolean isEndPointIdPresent() {
		String xpath = "(//div[text()=' Endpoint Id'])[1]";
		scrollTo(xpath);
		return isElementPresent(xpath);
	}

	public List<String> getElementUnselectedAndSelectedValue(String itemid) {
		String xpath = "//div[@itemid='" + itemid + "']";
		waitForElement(xpath);
		assertTrue(click(xpath));
		List<String> elementwithoutselectValue = getElementSelectedValueAndUnselectedValue();
		return elementwithoutselectValue;
	}

	public DataManagementPage isNewEndpointidValuePresentInFirstRowOfGrid(int colnumber, int rowindex, String itemid,
			String rowvalue) {
		String xpath = "//div[@itemid='" + itemid + "']//div[@column=" + colnumber + "]//div[@aria-rowindex=" + rowindex
				+ " and text()='" + rowvalue + "']";
		scrollTo(xpath);
		assertTrue(isElementPresent(xpath));
		doWait(500);
		return this;
	}

	public DataManagementPage valuePresentInGivenRowAndColumnOfGrid(int colnumber, int rowindex, String itemid,
			String rowvalue) {
		doWait(2000);
		String xpath = "//div[@itemid='" + itemid + "']//div[@column=" + colnumber + "]//div[@aria-rowindex=" + rowindex
				+ " and contains(text(),'" + rowvalue + "')]";
		System.out.println(xpath);
		scrollTo(xpath);
		assertTrue(isElementPresent(xpath));
		return this;
	}

	public void isColumnPresentInPreviewGrid(String colname) {
		String xpath = "//div[@id='datatable1682516166508']//div[@row=0 and text()='" + colname + "']";
		scrollTo(xpath);
		assertTrue(isElementPresent(xpath));
	}

	public void searchAndClickSetIDInAttributeLookupGrid(String rowvalue) {
		String xpath = "//div[@itemid='lookup_attribute_set_list']//div[@column=0]//div[text()='" + rowvalue + "']";
		waitForElement("//div[@itemid='lookup_attribute_set_list']");
		findElement("//div[@itemid='lookup_attribute_set_list']//input[@type=\"text\"]").sendKeys(rowvalue);
		doWait(500);
		scrollTo(xpath);
		assertTrue(isElementPresent(xpath));
		doWait(500);
		assertTrue(click(xpath));
		waitForElement("//div[@itemid='attributes_grid']");
	}

	public void verifyAttributeNamePresentInAttributeLookupGrid(int colnumber, String rowvalue) {
		String xpath = "//div[@itemid='attributes_grid']//div[@column=" + colnumber + "]//div[text()='" + rowvalue
				+ "']";
		scrollTo(xpath);
		assertTrue(isElementPresent(xpath));
	}

	public DataManagementPage selectTransformAttributeInColumnFilter(String TransformAttribute) {
		String xpath = "//div[@itemid='GRID_777894586949213']//div[@row=1 and @column=1]";
		assertTrue(click(xpath));
		doWait(500);
		xpath = xpath + "//option[@value='" + TransformAttribute + "']";
		// System.out.println(xpath);
		scrollTo(xpath);
		assertTrue(click(xpath));
		doWait(500);
		return this;
	}

	public void clickOnInlineEditOfAttributeLookupGrid(int rowindex) {
		String xpath = "//div[@itemid='attributes_grid']//div[@column=4]//div[@aria-rowindex=" + rowindex
				+ "]//i[contains(@class,'fal fa-edit')]";
		assertTrue(click(xpath));
	}

	public DataManagementPage selectProduct(String productName) {
		click(productListDropDown);
		doWait(500);
		selectGivenWebixItemIdFromOpenWebixList(productName);
		doWait(1000);
		waitForElement("//div[@itemid='NAVIGATION_296057045140243']");
		return this;
	}

	public DataManagementPage clickFirstRowFieldMapping() {
		doWait(500);
		assertTrue(click(
				"//div[@itemid='GRID_413115183171617']//h5[@title='Field Mapping']/following::i[@class='fal fa-edit'][1]"));
		waitForElement("//div[@itemid='MENU_1536750226548060']");
		return this;
	}

	public DataManagementPage clickSecondRowEditEndpointAdmin() {
		assertTrue(click("//div[@itemid='GRID_3272879231117616']//div[@aria-rowindex=\"2\"]//a[@title='Edit']"));
		waitForElement("//div[@class='modal-header ui-draggable-handle']//h5[@class='modal-title']");
		return this;
	}

	public DataManagementPage clickRowForPartnerNameEditAPITransformAttributes(String PartnerName) {
		doWait(1000);
		String xpath = "//div[@itemid='GRID_776907858256442']//div[@class='webix_ss_body']//div[@column=1]//div[text()='"
				+ PartnerName + "']";
		if (isElementPresent(xpath)) {
			String index = findElement(xpath).getAttribute("aria-rowindex");
			assertTrue(click("//div[@itemid='GRID_776907858256442']//div[@aria-rowindex=" + index
					+ "]//i[contains(@class,\"fal fa-search-plus fa-primary-color_#000000 fa-secondary-color_#000000\")]"));
			waitForElement("//div[@itemid='GRID_777894586949213']");
		} else {
			info(PartnerName + " is not available");
			throw new org.testng.SkipException("Skipping test case because " + PartnerName + " is not available");
		}
		return this;
	}

	public DataManagementPage clickFirstRowEditPartnerAPITransformAttributes() {
		assertTrue(click("//div[@itemid='GRID_777894586949213']//div[@aria-rowindex=1]//a[@title='Edit']"));
		return this;
	}

	public String getActiveStatusOfTransformAttribute(int columnNumber, int rowIndex) {
		doWait(2000);
		String xpath = "//div[@itemid='GRID_777894586949213']//div[@column=" + columnNumber + "]//div[@aria-rowindex="
				+ rowIndex + "]";
		assertTrue(isElementPresent(xpath));
		return findElement(xpath).getText();
	}

	public boolean isEndPointIdDropDownPresent() {
		String xpath = "//div[@itemid='MENU_1536750226548060']";
		waitForElement(xpath);
		scrollTo(xpath);
		return isElementPresent(xpath);
	}

	public DataManagementPage selectOptionFromDropdownAndCloneFeedNotAvailable(String itemid, String text) {
		String xpath = "//div[@itemid='" + itemid + "']//div[@class='webix_el_box']//input[@role='combobox']";
		doWait(500);
		assertTrue(click(xpath));
		// assertTrue(selectGivenListItemFromOpenWebixList(text));
		if (!selectGivenListItemFromOpenWebixList(text)) {
			// info("File Feed is not available");
			clickFeedManagement();
			selectOptionFromDropdown("MENU_138087251567401", text.replace(ClientName.toUpperCase(), "HRSOFTI"));
			clickFeedClone();
			passNewValueInputBoxGridInlineEdit("promptBox", text);
			clickSubmitOfFeedClonePopup();
			selectImportTab();
			selectGivenItemFromDropDownIfAny("Data File", text);
			// throw new org.testng.SkipException("Skipping test case because File Feed is
			// not
			// available");
		}
		doWait(500);
		return this;
	}

	public boolean isEndPointIdColumnPresent() {
		String xpath = "//div[@itemid='GRID_413115183171617']//div[@row=0 ]//div[text()=' ENDPOINT NAME']";
		scrollTo(xpath);
		return isElementPresent(xpath);
	}

	public boolean isEndPointNameColumnPresent() {
		String xpath = "(//div[@itemid='GRID_3272879231117616']//div[text()=' Endpoint Name'])[1]";
		scrollTo(xpath);
		return isElementPresent(xpath);
	}

	public boolean isColumnPresentInPreviewGrid(String columnlabel, String itemid) {
		String xpath = "//div[@itemid='" + itemid + "']//div[text()='" + columnlabel + "']";
		scrollTo(xpath);
		return isElementPresent(xpath);
	}

	public boolean isEndPointPathColumnPresent() {
		String xpath = "(//div[@itemid='GRID_3272879231117616']//div[text()=' Endpoint Path'])[1]";
		scrollTo(xpath);
		return isElementPresent(xpath);
	}

	public boolean isColumnPresentInGrid(String itemid, String ColName) {
		String xpath = "(//div[@itemid='" + itemid + "']//div[@row=0]//div[text()='" + ColName + "'])[1]";
		scrollTo(xpath);
		return isElementPresent(xpath);
	}

	// This is deprecated, use selectGivenItemFromDropDownIfAny()
	@Deprecated
	public void selectDataFile(String text) {
		assertTrue(click("//*[text()='Data File']/following::div[@class='webix_el_box'][1]"));
		assertTrue(click(format("(//div[text()='%s'])[1]", text)));
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert selection is happened or not
	 */
	public boolean selectGivenItemFromDropDownIfAny(String listName, String itemName) {

		if (!openListBox(listName))
			return false;

		boolean resp = selectGivenListItemFromOpenWebixList(itemName);
		doWait(1000l);
		return resp;

	}

	public DataManagementPage selectImportTab() {
		click(format(navTabXPath, "Import"));
		doWait(1000);
		waitForElement("//div[@itemid='APP_296600158786580']");
		return this;
	}

	public DataManagementPage selectStatusReportsTab() {
		click(format(navTabXPath, "Status Reports"));
		doWait(1000);
		waitForElement("//div[@itemid='SECTION_283190193918705']");
		return this;
	}

	public DataManagementPage searchStatusReports(String feedName) {
		selectGivenItemFromDropDownIfAny("File Name", feedName);
		click("//div[@itemid='SECTION_283190193918705']//div[@itemid='BUTTON_131992007385294']/button");
		doWait(2000);
		waitForElement("//div[@itemid='GRID_137785167886903']");
		return this;
	}

	public int getJobExecutionCount() {
		return findElements(
				"//div[@itemid='GRID_137785167886903']//div[@class='webix_ss_body']//div[@class='webix_ss_left']//div[@column='0']//div[@role='gridcell']//a[@title='Step Log']")
				.size();
	}

	public DataManagementPage showStepLogs() {
		doWait(2000);
		click(findElements(
				"//div[@itemid='GRID_137785167886903']//div[@class='webix_ss_body']//div[@class='webix_ss_left']//div[@column='0']//div[@role='gridcell']//a[@title='Step Log']")
				.get(0));
		doWait(2000);
		waitUntilVisible("//div[@itemid='SECTION_827672055254255']");
		return this;
	}

	public int getStepLogCount() {
		int n = findElements(
				"//div[@itemid='GRID_1516768038509_copy_1c041b49']//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@column='0']//div[@role='gridcell']")
				.size();
		return n;
	}

	public DataManagementPage selectConfigureFeedTab() {
		click(format(navTabXPath, "Configure Feed"));
		doWait(1000); // give 0.5 second pause to execute corresponding java script to load sub
						// application
		waitForElement("//div[@itemid='APP_296632264806282']"); // And the wait till corresponding
																// item is being loaded
		return this;
	}

	public void selectFeedManagementTab() {
		assertTrue(click(format(navTabXPath, "Feed Management")));
		doWait(500); // give 0.5 second pause to execute corresponding java script to load sub
						// application
		waitForElement("//div[@itemid='MENU_138087251567401']"); // And the wait till corresponding
																	// item is being loaded
	}

	public DataManagementPage selectFeedFieldMappingTab() {
		click(format(navConfigFeedSubTabXPath, "Field Mapping"));
		doWait(500); // give 0.5 second pause to execute corresponding java script to load sub
						// application
		waitForElement("//div[@itemid='GRID_413115183171617']"); // And the wait till corresponding
																	// item is being loaded
		return this;
	}

	public void selectFeedValidationsTab() {
		assertTrue(click(format(navConfigFeedSubTabXPath, "Validations")));
		doWait(500); // give 0.5 second pause to execute corresponding java script to load sub
						// application
		waitForElement("//div[@itemid='MENU_138087251567401']"); // And the wait till corresponding
																	// item is being loaded
	}

	public boolean cloneFeed(String clonableFeedName, String clonedFeedName) {
		selectGivenItemFromDropDownIfAny("Cloneable Feeds", clonableFeedName);
		findElement(By.id("promptBox")).sendKeys(clonedFeedName);
		click("//div[@role='dialog']//button[text()='Submit']");
		return false;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert selection is happened or not
	 */
	public boolean selectGivenFeedForConfigureFeed(String itemName) {

		click("//div[@itemid='MENU_297564209205268']//input[@role='combobox']");

		boolean resp = selectGivenListItemFromOpenWebixList(itemName);
		doWait(2000); // do wait for 2 sec to update the widget and apply js based changes to
						// widget

		return resp;
	}

	public DataManagementPage openFieldMapping() {
		click("//div[@itemid='NAVIGATION_297266794250062']//li[@title='Field Mapping']");
		doWait(5000); // wait for 5 sec to load field mapping table for selected feed
		return this;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert job step message based on given
	 *          step name
	 */
	public String getJobStepLogMessage(String stepName) {
		doWait(60000l);
		List<WebElement> stepNameCells = findElements(
				"//div[@itemid='GRID_1516768038509_copy_87702f77']//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][text()='"
						+ stepName + "']");
		if (stepNameCells.isEmpty())
			return null;

		String rowIndex = stepNameCells.get(0).getAttribute("aria-rowindex");
		return findElement(
				"//div[@itemid='GRID_1516768038509_copy_87702f77']//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-rowindex='"
						+ rowIndex + "'][@aria-colindex='2']")
				.getText();

	}

	public DataManagementPage uploadFiles(String filePath) {
	    doWait(1000);
		findElement("//div[@itemid='GRID_LAYOUT_51daf973']//input[@name='uploadFile[]']").sendKeys(filePath);
		return this;
	}

	public DataManagementPage clickPreview() {
		doWait(2000);
		click("//div[@itemid='GRID_LAYOUT_51daf973']//span[text()='Preview']");
		return this;
	}

	public DataManagementPage clickCancel() {
		doWait(2000);
		click("//div[@itemid='BUTTON_1058934131057417']//button//span[text() = 'CANCEL']");
		return this;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert preview is success or not
	 */
	public boolean isFeedPreviewSuccess() {
		waitForElement("//div[@itemid='SECTION_627914305915076']");
		return findElements(
				"//div[@itemid='SECTION_627914305915076']//div[@role='grid']//div[@section='header']//div[@class='webix_hs_center']//div[@class='webix_hcolumn']")
				.size() > 0;
	}

	/**
	 * @author Hera Aijaz
	 * @returns String This method is returning string instead PageObject , This is
	 *          written intentionally to assert preview title contains expected text
	 *          or not
	 */
	public String getPreviewTitleErrorMessage() {
		waitForElement("//div[@itemid='SECTION_627914305915076']");
		try {
			return findElement("//div[@itemid='SECTION_627914305915076']//div[2]").getText();
		} catch (NoSuchElementException noSuchElementException) {
			return null;
		}
	}

	/**
	 * @author Hera Aijaz
	 * @returns Integer This method is returning int instead PageObject , This is
	 *          written intentionally to retrieve no of rows preview contains
	 *          highlighted in red using css selector
	 */
	public int getNoOfErrorCountInPreview() {
		waitForElement("//div[@itemid='SECTION_627914305915076']");
		try {
			return findElements(
					"//div[@itemid='SECTION_627914305915076']//div[@role='grid']//div[@class='webix_ss_center']//div[@column='0']//div[@class='webix_cell highlight_col_count_mismatch']")
					.size();
		} catch (NoSuchElementException noSuchElementException) {
			return 0;
		}
	}

	public boolean isFirstErrorRowOnTopInPreviewHighlightedInRed() {
		waitForElement("//div[@itemid='SECTION_627914305915076']");
		try {
			return (findElement(
					"//div[@itemid='SECTION_627914305915076']//div[@role='grid']//div[@class='webix_ss_center']//div[@aria-rowindex='1' and @class='webix_cell highlight_col_count_mismatch']") != null);
		} catch (NoSuchElementException noSuchElementException) {
			throw noSuchElementException;
		}
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert preview header contains expected
	 *          text or not
	 */
	public boolean isPreviewHeader(String headerName) {
		waitForElement("//div[@itemid='SECTION_627914305915076']");

		return (findElements(
				"//div[@itemid='SECTION_627914305915076']//div[@role='grid']//div[@section='header']//div[@class='webix_hs_center']//div[@class='webix_hcolumn']")
				.size() == 1
				&& findElement(
						"//div[@itemid='SECTION_627914305915076']//div[@role='grid']//div[@section='header']//div[@class='webix_hs_center']//div[@class='webix_hcolumn']//div")
						.getText().equalsIgnoreCase(headerName));
	}

	public boolean isPreviewHeaderNotLegacyHeader(String headerName) {
		waitForElement("//div[@itemid='SECTION_627914305915076']");

		return findElement(
				"//div[@itemid='SECTION_627914305915076']//div[@role='grid']//div[@section='header']//div[@class='webix_hs_center']//div[@class='webix_hcolumn']//div")
				.getText().equalsIgnoreCase(headerName);
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert preview data contains expected
	 *          text or not
	 */
	public boolean isPreviewFirstCellContains(String content) {
		waitForElement("//div[@itemid='SECTION_627914305915076']");
		return (findElements("//div[@itemid='SECTION_627914305915076']//div[@role='gridcell']").size() == 1
				&& findElement("//div[@itemid='SECTION_627914305915076']//div[@role='gridcell']").getText()
						.equalsIgnoreCase(content));
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert preview is empty or not
	 */
	public boolean isFeedPreviewEmpty() {
		waitForElement("//div[@itemid='SECTION_627914305915076']");
		return findElements(
				"//div[@itemid='SECTION_627914305915076']//div[@role='grid']//div[@section='header']//div[@class='webix_hs_center']//div[@class='webix_hcolumn']")
				.size() == 0;
	}

	public DataManagementPage changeFormatTo(String format) {
		click("//div[@itemid='MENU_1556762177044176']");
		selectGivenListItemFromOpenWebixList(format);
		doWait(5000l);
		clickSaveFeedMapping();
		doWait(5000l);
		return this;
	}

	public DataManagementPage clickValidate() {
		doWait(2000);
		click("//div[@itemid='GRID_LAYOUT_51daf973']//span[text()='VALIDATE']");
		doWait(1000);
		return this;
	}

	public DataManagementPage clickSaveFeedMapping() {
		doWait(1000);
		scrollToView("//div[@itemid='BUTTON_181487774321418']");
		assertTrue(click("//div[@itemid='BUTTON_181487774321418']"));
		doWait(3000);
		return this;
	}

	public DataManagementPage clickImport() {
		doWait(2000);
		click("//div[@itemid='GRID_LAYOUT_51daf973']//span[text()='IMPORT']");
		doWait(1000); // do wait for 0.5 sec to update the widget and apply js based changes to
						// widget
		return this;
	}

	/**
	 * @author kalyan.komati
	 * @param maxWaitTimeInMins
	 */
	public DataManagementPage waitTillJobIsFinished(long maxWaitTimeInMins) {
		boolean isJobFinished = false;
		boolean isFeedConfigError = isFeedHasConfigErrors();
		if (isFeedConfigError)
			return this;
		for (int i = 0; i < maxWaitTimeInMins; i++) {
			isJobFinished = isJobCompletedSuccessFully();
			if (isJobFinished)
				return this;
			isJobFinished = isJobFailed();
			if (isJobFinished)
				return this;
			isJobFinished = isJobCompletedWithErrors();
			if (isJobFinished)
				return this;
			doWait(3000);
		}
		return this;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert job is completed successfully or
	 *          not
	 */
	public boolean isJobCompletedSuccessFully() {
		return (findElements("//div[text()='Job Completed Successfully']").size() > 0);
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert config popup error shown or not
	 */
	public boolean isFeedHasConfigErrors() {
		doWait(2000);
		return (findElements("//div[@class='h4 message-box-title text-danger']").size() > 0);
	}

	public DataManagementPage openDetailedLogs() {
		click("//div[@itemid='BUTTON_739266640136822']//button");
		waitForElement("//div[@itemid='GRID_1516768038509_copy_5df68017']");
		return this;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert job step message based on given
	 *          step name
	 */
	public String getJobDetailedStepLogMessage(String stepName) {
		doWait(60000l);
		List<WebElement> stepNameCells = findElements(
				"//div[@itemid='GRID_1516768038509_copy_5df68017']//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][text()='"
						+ stepName + "']");
		if (stepNameCells.isEmpty())
			return null;

		String rowIndex = stepNameCells.get(0).getAttribute("aria-rowindex");
		return findElement(
				"//div[@itemid='GRID_1516768038509_copy_5df68017']//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-rowindex='"
						+ rowIndex + "'][@aria-colindex='4']")
				.getText();

	}

	/**
	 * @author Hera
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert job step message based on given
	 *          step name and a specific row number in case of multiple messages
	 */
	public String getJobDetailedStepLogMessage(String stepName, int rowNumber) {
		List<WebElement> stepNameCells = findElements(
				"//div[@itemid='GRID_1516768038509_copy_5df68017']//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][text()='"
						+ stepName + "']");
		if (stepNameCells.isEmpty())
			return null;

		String rowIndex = stepNameCells.get(rowNumber).getAttribute("aria-rowindex");
		return findElement(
				"//div[@itemid='GRID_1516768038509_copy_5df68017']//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-rowindex='"
						+ rowIndex + "'][@aria-colindex='4']")
				.getText();

	}

	/**
	 * @author hera.aijaz
	 * @returns an ArrayList Of products visible in the product dropdown from the
	 *          rendered UI
	 */
	public List<String> getProductListFromDropDown() {
		click(productListDropDown);
		waitForElement(
				"//div[@class='webix_view webix_window webix_popup' and contains(@style,'display: block;')]//div[@role='listbox']");

		ListIterator<WebElement> productsWebElements = findElements(
				"//div[@class='webix_view webix_window webix_popup' and contains(@style,'display: block;')]//div[@role='listbox']//strong")
				.listIterator();
		List<String> productsList = new ArrayList<String>();

		while (productsWebElements.hasNext()) {
			productsList.add(productsWebElements.next().getText().toUpperCase());
		}

		return productsList;
	}

	/**
	 * @author hera.aijaz
	 * @returns an ArrayList of data files' names available in the data file
	 *          dropdown from the rendered UI
	 */
	public List<String> getDataFileListFromDropDown() {
		click(dataFileListDropDown);
		waitForElement(
				"//div[@class='webix_view webix_window webix_popup' and contains(@style,'display: block;')]//div[@role='listbox']");

		ListIterator<WebElement> dataFilesWebElements = findElements(
				"//div[@class='webix_view webix_window webix_popup' and contains(@style,'display: block;')]//div[@role='listbox']//div[@class='webix_list_item']")
				.listIterator();
		List<String> datafilesList = new ArrayList<String>();

		while (dataFilesWebElements.hasNext()) {
			datafilesList.add(dataFilesWebElements.next().getText().toUpperCase());
		}

		return datafilesList;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert job is completed with errors or
	 *          not
	 */
	public boolean isJobCompletedWithErrors() {
		// waitForElementsVisible ("//div[text()='Job Completed with Errors']");
		return (findElements("//div[text()='Job Completed with Errors']").size() > 0);
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert job is failed or not
	 */
	public boolean isJobFailed() {
		// waitForElementsVisible ("//div[text()='Job Failed']");
		return (findElements("//div[text()='Job Failed']").size() > 0);
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert preview has error message or not
	 */
	public boolean previewHasError() {
		waitForElement("//div[@itemid='SECTION_627914305915076']");
		return !findElement("//div[@itemid='SECTION_627914305915076']//div[2]").getText().isEmpty();
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert error popup shown or not
	 */
	public boolean isErrorPopupShown(String text) {
		return isElementPresent("//div[text()='" + text + "']");
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert date format as per expected
	 *          format or not
	 */
	public boolean verifyDateFormat(String dateFormat) {
		Select selectDataType = new Select(findElement(feedCfgFieldMappingGrid + selectDataTypeFilter));
		selectDataType.selectByValue("Date");
		doWait(500);
		WebElement feedCfgGrid = findElement(feedCfgFieldMappingGrid);
		List<WebElement> targetFormatField = feedCfgGrid.findElements(
				By.xpath(".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@column='8']//div"));
		for (WebElement element : targetFormatField) {
			if (!element.getText().equalsIgnoreCase(dateFormat))
				return false;
		}
		return true;
	}

	public boolean isAPIFeedPreviewSuccess() {
		String xpath = "//div[@itemid='SECTION_627914305915076']//div[@role='grid']//div[@section='header']//div[@class='webix_hs_center']//div[@class='webix_hcolumn']";
		return findElements(xpath).size() > 0;
	}

	/**
	 * @param feedFieldName
	 */
	public boolean filterByFeedField(String feedFieldName) {
		Select selectFeedField = new Select(findElement(feedCfgFieldMappingGrid + selectFeedFieldFilter));
		selectFeedField.selectByValue(feedFieldName);
		doWait(1000);
		return true;
	}

	/**
	 * @param feedFieldDataType
	 */
	public boolean filterByFeedFieldDataType(String feedFieldDataType) {
		Select selectFeedField = new Select(findElement(feedCfgFieldMappingGrid + selectFeedFieldDataTypeFilter));
		selectFeedField.selectByValue(feedFieldDataType);
		doWait(500);
		return true;
	}

	public String getFirstFeedFieldNameByDataType(String feedFieldDataType) {
		filterByFeedFieldDataType(feedFieldDataType);
		WebElement feedCfgGrid = findElement(feedCfgFieldMappingGrid);
		List<WebElement> targetFeedField = feedCfgGrid.findElements(
				By.xpath(".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@column='3']//div"));
		for (WebElement element : targetFeedField) {
			return element.getText();
		}
		return null;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning String instead PageObject , This is
	 *          written intentionally to assert value of format column
	 */
	public String getFormatOfFeedField(String feedFieldName) {
		filterByFeedField(feedFieldName);
		WebElement feedCfgGrid = findElement(feedCfgFieldMappingGrid);
		List<WebElement> targetFormatField = feedCfgGrid.findElements(
				By.xpath(".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@column='8']//div"));
		for (WebElement element : targetFormatField) {
			return element.getText();
		}
		return null;
	}

	public DataManagementPage editFirstFieldMappingFromGrid() {
		click(feedCfgFieldMappingGrid + firstInlineEditAnchor);
		doWait(5000l);
		return this;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert validation rule exists or not
	 */
	public boolean isValidationExists(String feedField, String validationType) {

		waitForElement("//div[@itemid='GRID_298871053265401']");
		WebElement validationsGrid = findElement("//div[@itemid='GRID_298871053265401']");
		List<WebElement> feedFieldNameCells = validationsGrid.findElements(By.xpath(
				".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-colindex='2'][text()='"
						+ feedField + "']"));
		for (WebElement we : feedFieldNameCells) {
			String rowIndex = we.getAttribute("aria-rowindex");
			if (validationsGrid.findElement(By.xpath(
					".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-rowindex='"
							+ rowIndex + "'][@aria-colindex='3']"))
					.getText().equalsIgnoreCase(validationType))
				return true;
		}

		return false;
	}

	/**
	 * @param feedField
	 * @param validationType
	 */
	public DataManagementPage addValidation(String feedField, String validationType) {
		if (isValidationExists(feedField, validationType))
			return this;
		click("//div[@itemid='GRID_298871053265401']//div[@class='buttonHolder']//div[@role='group']//button//i[@class='fal fa-plus']");
		doWait(1000l);

		setText("//div[@datafield='feed_field']//div[@class='webix_el_box']//input", feedField);
		doWait(1000l);

		setText("//div[@datafield='validation_id']//div[@class='webix_el_box']//input", validationType);
		doWait(1000l);

		setText("//div[@datafield='param_a']//input", "Y");

		setText("//div[@datafield='enabled']//div[@class='webix_el_box']//input", "No");
		doWait(1000l);

		clickSaveAndCloseEditRecordPopupOfGrid();
		doWait(1000l);

		return this;
	}

	/**
	 * @param feedField
	 * @param validationType
	 */
	public void openEditPopupForValidation(String feedField, String validationType) {
		waitForElement("//div[@itemid='GRID_298871053265401']");
		WebElement validationsGrid = findElement("//div[@itemid='GRID_298871053265401']");
		List<WebElement> feedFieldNameCells = validationsGrid.findElements(By.xpath(
				".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-colindex='2'][text()='"
						+ feedField + "']"));
		for (WebElement we : feedFieldNameCells) {
			String rowIndex = we.getAttribute("aria-rowindex");
			if (validationsGrid.findElement(By.xpath(
					".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-rowindex='"
							+ rowIndex + "'][@aria-colindex='3']"))
					.getText().equalsIgnoreCase(validationType)) {
				click(validationsGrid.findElement(By.xpath(
						".//div[@class='webix_ss_body']//div[@class='webix_ss_left']//div[@role='gridcell'][@aria-rowindex='"
								+ rowIndex + "'][@aria-colindex='1']//a[@title='Edit']")));
				return;
			}
		}

	}

	/**
	 * @param feedField
	 * @param validationType
	 */
	public DataManagementPage enableValidation(String feedField, String validationType) {
		if (isValidationEnabled(feedField, validationType))
			return this;
		openEditPopupForValidation(feedField, validationType);
		click("//div[@datafield='enabled']//div[@class='webix_el_box']");
		selectGivenWebixItemIdFromOpenWebixList("Y");
		clickSaveAndCloseEditRecordPopupOfGrid();
		doWait(1000l);
		return this;
	}

	/**
	 * @param feedField
	 * @param validationType
	 */
	public DataManagementPage disableValidation(String feedField, String validationType) {
		if (!isValidationEnabled(feedField, validationType))
			return this;
		openEditPopupForValidation(feedField, validationType);
		openEditPopupForValidation(feedField, validationType);
		click("//div[@datafield='enabled']//div[@class='webix_el_box']");
		selectGivenWebixItemIdFromOpenWebixList("N");
		clickSaveAndCloseEditRecordPopupOfGrid();
		doWait(1000l);
		return this;
	}

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert validation changes
	 */
	public boolean isValidationEnabled(String feedField, String validationType) {
		waitForElement("//div[@itemid='GRID_298871053265401']");
		WebElement validationsGrid = findElement("//div[@itemid='GRID_298871053265401']");
		List<WebElement> feedFieldNameCells = validationsGrid.findElements(By.xpath(
				".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-colindex='2'][text()='"
						+ feedField + "']"));
		for (WebElement we : feedFieldNameCells) {
			String rowIndex = we.getAttribute("aria-rowindex");
			if (validationsGrid.findElement(By.xpath(
					".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-rowindex='"
							+ rowIndex + "'][@aria-colindex='3']"))
					.getText().equalsIgnoreCase(validationType)) {
				return validationsGrid.findElement(By.xpath(
						".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-rowindex='"
								+ rowIndex + "'][@aria-colindex='7']"))
						.getText().equalsIgnoreCase("Y");
			}
		}

		return false;

	}

	/**
	 * @param feedField
	 * @param validationType
	 */
	public DataManagementPage removeValidation(String feedField, String validationType) {
		waitForElement("//div[@itemid='GRID_298871053265401']");
		WebElement validationsGrid = findElement("//div[@itemid='GRID_298871053265401']");
		List<WebElement> feedFieldNameCells = validationsGrid.findElements(By.xpath(
				".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-colindex='2'][text()='"
						+ feedField + "']"));
		for (WebElement we : feedFieldNameCells) {
			String rowIndex = we.getAttribute("aria-rowindex");
			if (validationsGrid.findElement(By.xpath(
					".//div[@class='webix_ss_body']//div[@class='webix_ss_center']//div[@role='gridcell'][@aria-rowindex='"
							+ rowIndex + "'][@aria-colindex='3']"))
					.getText().equalsIgnoreCase(validationType)) {
				click(validationsGrid.findElement(By.xpath(
						".//div[@class='webix_ss_body']//div[@class='webix_ss_left']//div[@role='gridcell'][@aria-rowindex='"
								+ rowIndex + "'][@aria-colindex='1']//a[@title='Delete']")));
				doWait(500);
				clickConfirmInPopup();
				doWait(500);
				return this;
			}
		}
		doWait(1000l);
		return this;
	}

	private String webixListBox = "//div[@class='webix_view webix_window webix_popup']//div[@role='listbox']";
	private String webixListItem = ".//div[@role='option']";
	private String webixSelectedvalue = ".//div[@class='webix_list_item  webix_selected']";
	private String webixListItemByValue = ".//div[@role='option'][text()='%s']";
	private String webixListItemByWebixId = ".//div[@role='option'][@webix_l_id='%s']";

	/**
	 * @author kalyan
	 * @returns boolean This method is returning boolean instead PageObject , This
	 *          is written intentionally to assert selection is happened or not
	 */
	public boolean selectGivenListItemFromOpenWebixList(String listItemName) {
		List<WebElement> webixPopupBoxForList = findElements(webixListBox);
		for (WebElement element : webixPopupBoxForList) {
			if (element.isDisplayed()) {
				List<WebElement> listItems = element.findElements(By.xpath(format(webixListItemByValue, listItemName)));
				if (listItems.size() > 0) {
					int rand = new Random().nextInt(listItems.size());
					return click(listItems.get(rand));
				}
			}
		}
		return false;
	}

	// get select value and Unselected value in dropdown (Vivek Pandey)
	public List<String> getElementSelectedValueAndUnselectedValue() {
		List<WebElement> webixPopupBoxForList = findElements(webixListBox);
		List<String> elementsList = new ArrayList<String>();
		List<String> NewList = new ArrayList<String>();
		for (WebElement element : webixPopupBoxForList) {
			if (element.isDisplayed()) {
				List<WebElement> listItems = element.findElements(By.xpath(webixListItem)).stream()
						.filter(e -> !"TCC_NULL".equalsIgnoreCase(e.getAttribute("webix_l_id")))
						.collect(Collectors.toList());
				String selectedvalue = element.findElement(By.xpath(webixSelectedvalue)).getText();
				if (listItems.size() > 0) {
					for (WebElement value : listItems) {
						if (value.getText().equals(selectedvalue)) {
							continue;
						}
						elementsList.add(value.getText());
					}

				}
				NewList.add(selectedvalue);
				NewList.add(elementsList.get(0));
			}
		}

		return NewList;
	}

	public boolean selectGivenWebixItemIdFromOpenWebixList(String webixId) {
		List<WebElement> webixPopupBoxForList = findElements(webixListBox);
		for (WebElement element : webixPopupBoxForList) {
			if (element.isDisplayed()) {
				List<WebElement> listItems = element.findElements(By.xpath(format(webixListItemByWebixId, webixId)));
				if (listItems.size() > 0) {
					int rand = new Random().nextInt(listItems.size());
					return click(listItems.get(rand));
				} else {
				}
			}
		}
		return false;
	}

	public boolean openListBox(String name) {
		if (!hasDropDown(name))
			return true;
		return click(format(namedDropDown, name));
	}

	private String namedDropDown = "//div[@class='form-group']/label/h6[text()='%s']/../..//input[@role='combobox']";

	private Boolean hasDropDown(String listName) {
		List<WebElement> webElements = waitForElements(format(dropDownLabel, listName));
		return !webElements.isEmpty() && webElements.get(0).isDisplayed();
	}

	private String dropDownLabel = "//div[@class='form-group']/label/h6[text()='%s']";

	public DataManagementPage clickConfirmInPopup() {
		String xpath = "//div[@class='modal-dialog']//button[text()='Confirm']";
		click(xpath);
		doWait(500);
		return this;
	}

	public DataManagementPage clickProceedInPopup() {
		String xpath = "//div[@class='modal-dialog']//button[text()='Proceed']";
		click(xpath);
		doWait(500);
		return this;
	}

	public void deleteRowForGivenFeedName(String FeedName) {
		String xpath = "//div[@itemid='GRID_138951714897547']//div[@class='webix_ss_body']//div[@column=1]//div[text()='"
				+ FeedName + "']";
		String index = findElement(xpath).getAttribute("aria-rowindex");
		assertTrue(click("//div[@itemid='GRID_138951714897547']//div[@aria-rowindex=" + index
				+ "]//i[contains(@class,'fal fa-trash')]"));
		doWait(500);
		clickConfirmInPopup();
		doWait(500);
		waitForElement("//div[@class='modal-dialog']//div[text()='File feed deleted successfully']");
		doWait(500);
		assertTrue(click("//div[@class='modal-dialog']//button[text()='OK']"));
	}

	public DataManagementPage selectUsingSendkeyFromComboboxDropdown(String itemid, String text) {
		String xpath = "//div[@itemid='" + itemid + "']//div[@class='webix_el_box']//input[@role='combobox']";
		doWait(500);
		assertTrue(click(xpath));
		findElement(xpath).clear();
		findElement(xpath).sendKeys(text);
		return this;
	}

	public boolean errorMappingAfterPreviewClick() {
		String error_mapping_xpath = "//div[@class='modal-dialog']//div[contains(text(),'One or more mapping row exists with a target data object')]";
		return findElements(error_mapping_xpath).size() > 0;
	}

	public boolean errorFileNotSelectedAfterPreviewClick() {
		String file_not_select_xpath = "//div[@class='modal-dialog']//div[contains(text(),'File Not selected')]";
		return findElements(file_not_select_xpath).size() > 0;
	}

	public boolean isJobAbort() {
		String xpath = "//div[@class='modal-dialog']//h3[contains(text(),'Aborting Job')]";
		return findElements(xpath).size() > 0;
	}

	public DataManagementPage waitTillPreviewIsFinished(long maxWaitTimeInMins) {
		boolean isPreviewFinished = false;
		for (int i = 0; i < maxWaitTimeInMins; i++) {
			isPreviewFinished = errorFileNotSelectedAfterPreviewClick();
			if (isPreviewFinished)
				return this;
			isPreviewFinished = errorMappingAfterPreviewClick();
			if (isPreviewFinished)
				return this;
			isPreviewFinished = isAPIFeedPreviewSuccess();
			if (isPreviewFinished)
				return this;

			isPreviewFinished = isJobAbort();
			if (isPreviewFinished)
				return this;

			doWait(6000l);
		}
		return this;
	}

	public void clickPartnerRegistration() {
		waitForElement("//div[@itemid='TEXT_5110490700696010']");
		assertTrue(click("//div[@itemid='TEXT_5110490700696010']"));
		waitForElement("//div[@itemid='GRID_3016046435652668']");
	}

	public boolean isMainApiUrlColumnPresent() {
		String xpath = "(//div[@itemid='GRID_3016046435652668']//div[text()=' Main API URL'])[1]";
		scrollToView(xpath);
		return isElementPresent(xpath);
	}

	public DataManagementPage clickFirstRowEditApiRegistration() {
		doWait(2000);
		String xpath = "//div[@itemid='GRID_3016046435652668']//div[@row=1 and @column=1]";
		assertTrue(click(xpath));
		doWait(500);
		xpath = xpath + "//option";
		List<WebElement> elements = findElements(xpath);
		List<String> filteroptionnew = new ArrayList<>();
		for (WebElement element : elements) {
			String text = element.getText();
			if (text.length() > 0)
				filteroptionnew.add(text);
		}
		List<String> valuesToRemove = new ArrayList<>();
		valuesToRemove.add("ADP_CAN");
		valuesToRemove.add("ADP_WFN");
		valuesToRemove.add("Ultimate");
		List<String> newList = filteroptionnew.stream().filter(value -> !valuesToRemove.contains(value))
				.collect(Collectors.toList());
		System.out.println("filteroptionnew : " + newList);
		xpath = xpath + "[@value='" + newList.get(1) + "']";
		// System.out.println(xpath);
		scrollTo(xpath);
		assertTrue(click(xpath));
		doWait(500);
		assertTrue(click("//div[@itemid='GRID_3016046435652668']//div[@aria-rowindex=1]//a[@title='Edit']"));
		waitForElement("//div[@class='modal-header ui-draggable-handle']//h5[@class='modal-title']");
		return this;
	}

	public DataManagementPage clickReloadGrid() {
		doWait(10000);
		assertTrue(click("//button[contains(@clickexpr,'ThisObject.RefreshData')]"));
		doWait(2000);

		return this;
	}

	public DataManagementPage showDetailLogs() {
		click(findElements(
				"//div[@itemid='GRID_137785167886903']//div[@class='webix_ss_body']//div[@class='webix_ss_left']//div[@column='0']//div[@role='gridcell']//a[@title='Detailed Log']")
				.get(0));
		doWait(1000);
		waitUntilVisible("//div[@itemid='SECTION_827627803156184']");
		return this;
	}

	public DataManagementPage rowValuePresentInColumnofGrid(int column, String itemid, String rowvalue) {
		String xpath = "//div[@itemid='" + itemid + "']//div[@class='webix_ss_body']//div[@column=" + column
				+ "]//div[contains(text(),'" + rowvalue + "')]";
		assertTrue(isElementPresent(xpath));
		return this;
	}

	public DataManagementPage rowValuePresentInMultipleRowsInColumnOfGrid(int column, String itemid, String rowvalue) {
		String xpath = "//div[@itemid='" + itemid + "']//div[@class='webix_ss_body']//div[@column=" + column
				+ "]//div[contains(text(),'" + rowvalue + "')]";
		assertTrue(findElements(xpath).size() >= 1);
		return this;
	}

	public int getValueOfMinNonEmptyColumn() {
		String Value = findElement("//div[@itemid='TEXT_INPUT_3febd8c4']//input").getAttribute("value");
		int MinNonEmpty = Integer.parseInt(Value);
		System.out.println(MinNonEmpty);
		return MinNonEmpty;
	}

	public DataManagementPage clickFieldMappingGridRefresh() {
		String xpath = "//div[@itemid='GRID_413115183171617']//button[contains(@clickexpr,'ThisObject.ReloadAndWait')]";
		assertTrue(click(xpath));
		doWait(3000);
		return this;
	}

	public DataManagementPage setConfigurationForFileFeedTo(String separator, String feedName, String product,
			int minNonEmptyColumnPercent) {
		selectProduct(product);
		clickConfigureFeed();
		chooseFeedsInConfigureFeed(feedName);
		clickDefineFeed();
		String existingDelimiter = getSelectedDelimiter();
		System.out.println(existingDelimiter);
		if (!separator.equalsIgnoreCase(existingDelimiter)) {
			changeDelimiterTo(separator);
			doWait(2000);
		}
		int MinNonEmpty = getValueOfMinNonEmptyColumn();

		if (MinNonEmpty != minNonEmptyColumnPercent) {
			findElement("//div[@itemid='TEXT_INPUT_3febd8c4']//input")
					.sendKeys(Integer.toString(minNonEmptyColumnPercent));
			doWait(2000);
			clickDefineFeed();
			if (minNonEmptyColumnPercent < defaultMinNonEmptyPercent)
				clickProceedInPopup();
		}
		return this;
	}

	public DataManagementPage defaultConfigurationForFileFeed(String separator, String feedName, String product) {
		selectProduct(product);
		clickConfigureFeed();
		chooseFeedsInConfigureFeed(feedName);
		clickDefineFeed();
		String existingDelimiter = getSelectedDelimiter();
		System.out.println(existingDelimiter);
		if (!separator.equalsIgnoreCase(existingDelimiter)) {
			changeDelimiterTo(separator);
			doWait(2000);
		}
		int MinNonEmpty = getValueOfMinNonEmptyColumn();
		if (MinNonEmpty != defaultMinNonEmptyPercent) {
			findElement("//div[@itemid='TEXT_INPUT_3febd8c4']//input")
					.sendKeys(Integer.toString(defaultMinNonEmptyPercent));
			doWait(2000);
		}
		// clickFieldMapping();
		// clickFieldMappingGridRefresh().refresh();
		return this;
	}

	public DataManagementPage filterErrorMessageInDetailsLogPopup() {
		String xpath = "//div[@itemid='GRID_1516768038509_copy_5df68017']//div[@row=1 and @column=4]//span";
		assertTrue(click(xpath));
		doWait(2000);
		String buttonText = findElement(
				"//div[@class='webix_view webix_layout_line']//div[contains(@class,'webix_el_toggle')]//button")
				.getText();

		if (!buttonText.equalsIgnoreCase("Unselect all")) {
			clickOnSelectAllInMessageFilterInDetailedLogs();
		}
		assertTrue(click("//button[text()='Unselect all']"));
		doWait(2000);
		assertTrue(click(
				"//div[@class='webix_view webix_layout_line']//div[@class='webix_view webix_list webix_multilist']//div[@webix_l_id='ERROR']"));
		doWait(2000);
		return this;
	}

	public DataManagementPage scrollToSpecifcColumnInGrid(String itemid, String colname) {
		doWait(2000);
		String xpath = "//div[@itemid='" + itemid + "']//div[@row=0 and text()='" + colname + "']";
		try {
			WebElement element = findElement(xpath);
			scrollToView(xpath);
		} catch (NoSuchElementException e) {
			info(colname + " Column not present in Preview Grid");
			throw new org.testng.SkipException("Skipping because " + colname + " Column not present in Preview Grid");
		}
		return this;
	}

	public String getFirstRowValueOfAnyColumnInGrid(String columnlabel, String itemid) {
		doWait(2000);
		String index = findElement("//div[@itemid='" + itemid + "']//div[text()='" + columnlabel + "']")
				.getAttribute("column");
		System.out.println("Index : " + index);
		String firstrowvalue = findElement("//div[@itemid='" + itemid + "']//div[@class='webix_ss_body']//div[@column="
				+ index + "]//div[@aria-rowindex=1]").getText();
		System.out.println("1st Row Value : " + firstrowvalue);
		return firstrowvalue;
	}

	public List<String> getListValueOfFilterInGrid(String columnlabel, String itemid) {
		doWait(2000);
		String webixListBox = "//div[contains(@class,'webix_popup')][contains(@style,'display: block;')]//div[@class='webix_list_item']";
		String index = findElement("//div[@itemid='" + itemid + "']//div[text()='" + columnlabel + "']")
				.getAttribute("column");
		System.out.println("Index : " + index);
		assertTrue(click("//div[@itemid='" + itemid + "']//div[@row=0 and @column='" + index + "']//span"));
		List<WebElement> elements = findElements(webixListBox);
		// Create a list to store the text of each element.
		List<String> textList = new ArrayList<>();

		// Iterate through each element and get its text.
		for (WebElement element : elements) {
			String text = element.getText();
			textList.add(text);
		}
		return textList;
	}

	public void closeDetailedLogPopUp() {
		assertTrue(click("//div[contains(@class,'modal_MAX_window')]//button[@class='bootbox-close-button close']"));
	}

	/**
	 * @author hera.aijaz prints error messages displayed on the UI via Log PopUP
	 *         upon Job Completion
	 */
	public DataManagementPage printErrorMessagesFromLogsForStatusValueError() {
		String xpathForRowsWithStatusValueError = "//div[@itemid='GRID_1516768038509_copy_87702f77']//div[@column='3']//div[@aria-colindex='4' and text()='ERROR']";
		List<WebElement> errorMessagesWebElements;
		errorMessagesWebElements = findElements(xpathForRowsWithStatusValueError);
		for (WebElement element : errorMessagesWebElements) {
			String ariaRowIndex = element.getAttribute("aria-rowindex");
			String xpathForErrorMessage = "//div[@itemid='GRID_1516768038509_copy_87702f77']//div[@column='1']//div[@aria-rowindex='"
					+ ariaRowIndex + "']";
			info("Status Value Error : " + findElement(xpathForErrorMessage).getText());
		}
		return this;
	}

	/**
	 * @author hera.aijaz prints error messages displayed on the UI via Log PopUP
	 *         upon Job Completion
	 */
	public DataManagementPage printErrorMessagesFromLogsForMessageTypeError() {
		String xpathForRowsWithMessageTypeError = "//div[@itemid='GRID_1516768038509_copy_87702f77']//div[@column='2']//div[@aria-colindex='3' and text()='ERROR']";
		List<WebElement> errorMessagesWebElements;
		errorMessagesWebElements = findElements(xpathForRowsWithMessageTypeError);
		for (WebElement element : errorMessagesWebElements) {
			String ariaRowIndex = element.getAttribute("aria-rowindex");
			String xpathForErrorMessage = "//div[@itemid='GRID_1516768038509_copy_87702f77']//div[@column='1']//div[@aria-rowindex='"
					+ ariaRowIndex + "']";
			info("Message Type Error : " + findElement(xpathForErrorMessage).getText());
		}
		return this;
	}

	/**
	 * @author hera.aijaz prints error messages displayed on the UI via Detailed Log
	 *         PopUP upon Job Completion
	 */
	// call openDetailedLog() and filterErrorMessageInDetailsLogPopup() methods
	// before calling this method in script
	public DataManagementPage printErrorMessagesFromDetailedLogs() {
		String xpathForErrorMessages = "//div[@itemid='GRID_1516768038509_copy_5df68017']//div[@aria-colindex='4']";
		List<WebElement> errorMessagesWebElements = findElements(xpathForErrorMessages);
		for (WebElement element : errorMessagesWebElements) {
			String message = element.getText();
			info("Details logs error : " + message);
		}
		closeDetailedLogPopUp();
		return this;
	}

	/**
	 * @author hera.aijaz
	 */
	public DataManagementPage clickOnSelectAllInMessageFilterInDetailedLogs() {
		String textOnFilterButton = findElement(
				"//div[@class='webix_view webix_layout_line']//div[contains(@class,'webix_el_toggle')]//button")
				.getText();
		String xpathForSelectAllButton = "//button[text()='" + textOnFilterButton + "']";
		if (textOnFilterButton.equalsIgnoreCase("Select all")) {
			assertTrue(click(xpathForSelectAllButton));
		}
		return this;
	}

	public DataManagementPage filterOptionInPreviewGrid(String columnlabel, String itemid, String option) {
		String index = findElement("//div[@itemid='" + itemid + "']//div[text()='" + columnlabel + "']")
				.getAttribute("column");
		System.out.println("Index : " + index);
		assertTrue(click("//div[@itemid='" + itemid + "']//div[@row=0 and @column='" + index + "']//span"));
		doWait(2000);
		String buttonText = findElement(
				"//div[@class='webix_view webix_layout_line']//div[contains(@class,'webix_el_toggle')]//button")
				.getText();

		if (!buttonText.equalsIgnoreCase("Unselect all")) {
			clickOnSelectAllInMessageFilterInDetailedLogs();
		}
		assertTrue(click("//button[text()='Unselect all']"));
		doWait(2000);
		assertTrue(click(
				"//div[@class='webix_view webix_layout_line']//div[@class='webix_view webix_list webix_multilist']//div[@webix_l_id='"
						+ option + "']"));
		doWait(2000);
		return this;
	}

	public DataManagementPage selectOptionFromDropdown(String itemid, String text) {
		String xpath = "//div[@itemid='" + itemid + "']//div[@class='webix_el_box']//input[@role='combobox']";
		doWait(500);
		assertTrue(click(xpath));
		assertTrue(selectGivenListItemFromOpenWebixList(text));
		return this;
	}

	public List<String> getListValueOfFilterInGridColumn(String columnlabel, String itemid) {
		doWait(2000);
		String webixListBox = "//div[contains(@class,'webix_popup')][contains(@style,'display: block;')]//div[@class='webix_list_item']";
		String index = findElement("//div[@itemid='" + itemid + "']//div[text()='" + columnlabel + "']/parent::div")
				.getAttribute("column");
		System.out.println("Index : " + index);
		assertTrue(click("//div[@itemid='" + itemid + "']//div[@row=1 and @column='" + index + "']//span"));
		List<WebElement> elements = findElements(webixListBox);
		// Create a list to store the text of each element.
		List<String> textList = new ArrayList<>();

		// Iterate through each element and get its text.
		for (WebElement element : elements) {
			String text = element.getText();
			textList.add(text);
		}
		return textList;
	}

	public List<String> checkDBErrorPresentInDetailsLogs(List<String> errorMessageList) {
		List<String> textList = new ArrayList<>();
		List<Pattern> patterns = new ArrayList<>();
		patterns.add(Pattern.compile("Retrieve Field .+ is NULL in the Lookup SQL"));
		patterns.add(Pattern.compile(".+ is not valid for the this type, please validate input"));
		patterns.add(
				Pattern.compile(".+ is not valid, as a record with this unique key already exists in the data object"));
		patterns.add(Pattern
				.compile("Unable to .+ this record in the data object .+ , it has references which may need to be"));

		for (String text : errorMessageList) {
			for (Pattern pattern : patterns) {
				Matcher matcher = pattern.matcher(text);
				if (matcher.matches()) {
					textList.add(text);
				}
			}
		}
		return textList;
	}
}
