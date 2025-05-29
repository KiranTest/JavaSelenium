package com.hrsoft.gui.modelview;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.testng.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.utils.seleniumfy.BasePage;

//div[contains(@id,'TCCREPEATERROW')]/div/div/div/label/span[contains(text(),'Copy Default Compensation Calibration Template')]
public class ModelViewPage extends BasePage {
	private String closePopup = "(//button[@class='bootbox-close-button close'])[2]";
	private String editSessionTitleInput = "//div[@itemid='TEXT_INPUT_63e1b5f4']//input";
	private String saveSession = "//span[text()='Save session']";
	private String rateToBaseInput = "//div[@itemid='TEXT_INPUT_445834665385843']/div/div/input";
	private String updateTemplateBtn = "//span[text()='Update Template']";
	private String cloneTemplateBtn = "//span[text()='Clone Template']";
	private String settingsBtn = "//button[@data-content='Settings']";
	private String newSessionBtn = "//label//span[contains(text(),'%s')]/../../../../following-sibling::div[1]//button";
	private String editModelViewSession = "button[data-content='Edit Calibration Session']";
	private String editModelViewDefaultTemplate = "//label//span[contains(text(),'%s')]/../../../../following-sibling::div[2]//button";
	private String templatePopupPageatePopupPage = "//span[text()='Update Template']";
	private String pleaseWait = "//h5[text()='Please Wait...']";
	private String loadingCompensationData = "//h5[contains(text(),'Loading Compensation Data...')]";
	private String newTemplateBtn = "//button[@data-content='New Template']";
	private String createTemplateBtn = "//span[text()='Create Template']";
	private String settingsIcon = "button[data-content='Settings']";
	private String createNewSessionFromTopTemplate = "button[data-content='New Calibration Session']";
	private String bulkLock = "//span[@class='widget-inline-editing-target  text-warning d-none d-md-inline-block text-uppercase' and text()='Bulk Lock']//parent::button[not(@disabled)]";
	private String bulkUnlock = "//span[@class='widget-inline-editing-target  text-warning d-none d-md-inline-block text-uppercase' and text()='Bulk Unlock']//parent::button[not(@disabled)]";
	private String bulkUpdate = "//span[@class='widget-inline-editing-target   d-none d-md-inline-block text-uppercase' and text()='Bulk Update']";
	private String confirmInPopUp = "//button[contains(@class,'btn btn-primary btn-md') and text()='Confirm']";
	private String okInPopUp = "//button[@class='btn btn-secondary btn-md' and text()='OK']";
	private String loadingIndicators = "(//img[@src='/resources/skins/default/portal/new/images/wait-icon.gif'])[2]";
	private String confirmButton = "//button[text()='Confirm']";
	private String filterIconInGrid = "//div[contains(@class,'webix_view webix_control webix_el_text')]/div/input";

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy("div[itemid='MENU_1140788442733994']"));
	}

	public ModelViewPage isNavigatedToModelView() {
		waitForElementVisible("//div[contains(@itemid,'GRID_LAYOUT_COLUMN_1607020077268_1__TCCREPEATERROW0')]");
		return this;
	}

	public ModelViewPage clickOk() {
		waitForElementVisible("//button[text()='OK']");
		assertTrue(click("//button[text()='OK']"));
		return this;
	}

	public ModelViewPage clickNewTemplateButton() {
		assertTrue(click(newTemplateBtn));
		return this;
	}

	public ModelViewPage isTemplatePopupOpened() {
		waitUntilVisible(createTemplateBtn);
		assertTrue(isElementPresent(createTemplateBtn));
		return this;
	}

	public ModelViewPage selectStatus(String status) {
		setValueInDropdowns("MENU_1140788442733994", status);
		waitUntilVisible("//span[text()='Status']");
		return this;
	}

	public String statusList(String status) {
		String statusXpath = format("//span[text()='%s']", status);
		String expectedStatus = getText(statusXpath);
		return expectedStatus;
	}

	public ModelViewPage openSettingsPage() {
		assertTrue(click(settingsIcon));
		waitForElementVisible("//h5[text()='Settings']");
		assertTrue(isElementPresent("//h5[text()='Settings']"));
		return this;
	}

	public ModelViewPage selectSessionType(String sessionType) {
		String type = format("//input[@value='%s']", sessionType);
		assertTrue(click(type));
		return this;
	}

	public ModelViewPage enterSessionTitle(String title) {
		assertTrue(setText("//div[@itemid='TEXT_INPUT_63e1b5f4']/div/div/input", title));
		return this;
	}

	public ModelViewPage clickNewSessionTemplate(String template) {
		assertTrue(click(String.format(newSessionBtn, template)));
		waitForElementInvisible(pleaseWait);
		return this;
	}

	public ModelViewPage saveSession() {
		assertTrue(click("//span[text()='Save session']"));
		assertTrue(click("//button[text()='OK']"));
		if (isElementPresent("//button[text()='OK']")) {
			assertTrue(click("//button[text()='OK']"));
		}
		return this;
	}

	public ModelViewPage saveSessionAndGoToHomePage() {
		assertTrue(click("//span[text()='Save session']"));
		waitForElementVisible("//button[text()='OK']");
		assertTrue(click("//button[text()='OK']"));
		assertTrue(click("[class='far fa-arrow-circle-left   font-icon ']"));
		return this;
	}

	public boolean isSessionAdded(String title) {
		System.out.println("Model view session created with title:" + title);
		return isElementPresent(format("//span[contains(@originalvalue,'%s')]", title));
	}

	public ModelViewPage chooseFilterAndEnterValue(String value, String text) {
		assertTrue(click("//div[@class='webix_inp_static']"));
		assertTrue(click(format("div[webix_l_id='%s'][aria-selected='true']", value)));
		assertTrue(setText("//div[@class='webix_view webix_control webix_el_text']//input", text));
		return this;
	}

	public boolean isCurrentSalaryFilter() {
		doWait(3000);
		return isElementPresent("(//span[@class='custom_renderer_click' and text()='USD 1.00'])[2]");
	}

	public ModelViewPage clickShowUnderManager() {
		assertTrue(click("(//div[@itemid='GRID_LAYOUT_COLUMN_1628074390905_0']//input)[2]"));
		return this;
	}

	public ModelViewPage selectManagerDropdown() {
		assertTrue(click("//input[@placeholder='Select Manager']"));
		return this;
	}

	public ModelViewPage selectSession() {
		selectRandomOptionFromDropdownMenu("MENU_1382394260752165");
		return this;

	}

	public ModelViewPage discardSession() {
		selectOptionFromDropdownMenu("MENU_875064817468682", "Discard");
		return this;
	}

	public ModelViewPage clickClearFilter() {
		assertTrue(click("//span[text()='Clear Filters']"));
		return this;
	}

	public ModelViewPage editModelViewTemplate(String template) {
		assertTrue(click(String.format(editModelViewDefaultTemplate, template)));
		assertTrue(isElementPresent(templatePopupPageatePopupPage));
		return this;
	}

	public ModelViewPage editModelViewSession() {
		List<WebElement> elements = findElements(editModelViewSession);
		if (elements.size() > 0) {
			elements.get(0).click();
		}
		return this;
	}

	public void clickAndSearchEmployeeFilter(String text) {
		assertTrue(click("//div[contains(text(),'Employee Name')]/parent::div/following-sibling::div//span"));
		findElement(
				"//div[@class='webix_view webix_control webix_el_text' and contains(@style,'display: inline-block;')]//input")
				.sendKeys(text, Keys.ENTER);
	}

	public boolean isFilteredEmployee(String employee) {
		return isElementPresent(format("//div[text()='%s' and @role='gridcell']", employee));
	}

	public ModelViewPage clickOpenCalibrationSession(String emp) {
		assertTrue(click(String.format (
				"(//label//span[contains(text(),'%s')]/../following::div//button[@data-content='Open Calibration Session'])[2]", emp)));
		waitForElementInvisible("[src=\"/resources/skins/default/portal/new/images/wait-icon.gif\"]");
		return this;
	}

	public ModelViewPage SelectCurrencyFromDropDown(String currency) {
		assertTrue(click(
				"//span[contains(text(),'Show Data in Currency:')]/../../../../following-sibling::div[1]//input"));
		assertTrue(click("//div[@webix_l_id='" + currency + "']"));
		waitForElementInvisible("(//img[@src='/resources/skins/default/portal/new/images/wait-icon.gif'])[1]");
		return this;
	}

	public String getCurrencyFromPage() {
		doWait(5000);
		return getText("(//div[@class='content mw-100 d-inline-block plain-text text-muted text-center bg-'])[2]")
				.split(" ")[0];
	}

	public ModelViewPage clickBulkLock() {
		if (isElementPresent(bulkUnlock))
			clickBulkUnLock();
		assertTrue(click(bulkLock));
		assertTrue(click(confirmInPopUp));
		waitForElementInvisible(pleaseWait);
		assertTrue(click(okInPopUp));
		return this;
	}

	public ModelViewPage clickBulkUnLock() {
		assertTrue(click(bulkUnlock));
		assertTrue(click(confirmInPopUp));
		waitForElementInvisible(pleaseWait);
		assertTrue(click(okInPopUp));
		return this;
	}

	public ModelViewPage clickBulkUpdate() {
		assertTrue(click(bulkUpdate));
		if (isElementVisible(confirmInPopUp))
			assertTrue(click(confirmInPopUp));
		waitForElementInvisible(pleaseWait);
		return this;
	}

	public boolean isBulkUnlock() {
		return isElementPresent(bulkUnlock);
	}

	public ModelViewPage selectComponent(String value) {
		assertTrue(click("[placeholder='Select Component']"));
		assertTrue(click(format("(//div[text()='%s'])[1]", value)));
		return this;
	}

	public ModelViewPage selectType(String type) {
		doWait(3000);
		assertTrue(click("//input[@placeholder='Select type']/..//span"));
		doWait(3000);
		assertTrue(click(format(
				"//h5[text()='Type']//ancestor::div[@class='modal-dialog']/../following-sibling::div[contains(@style,'display: block;')]//div[text()='%s' ]",
				type)));
		return this;
	}

	public ModelViewPage selectRecommendedGuideline(String type) {
		String dropDown = "//h6[text()='Recommended Guideline']/../..//following-sibling::div//child::div[@class='webix_el_box']//input";
		doWait(3000);
		assertTrue(click(dropDown));
		doWait(3000);
		assertTrue(click(format(
				"//h6[text()='Recommended Guideline']//ancestor::div[@class='modal-dialog']/../following-sibling::div//div[text()='Merit Guideline %s Percent' and @class='webix_list_item']",
				type)));
		return this;
	}

	public ModelViewPage enterNumericValue(String num) {
		findElement("[pattern='[0-9]*'][inputmode='numeric']").sendKeys(num);
		return this;
	}

	public ModelViewPage clickApplyChanges() {
		assertTrue(click(
				"//span[@class='widget-inline-editing-target  text-none d-md-inline-block text-uppercase' and text()='Apply Changes']"));
		assertTrue(click(okInPopUp));
		if (isElementVisible(okInPopUp))
			assertTrue(click(okInPopUp));
		return this;
	}

	public ModelViewPage clickPublish() {
		assertTrue(click("//span[text()='Publish']"));
		assertTrue(click(confirmInPopUp));
		return this;
	}

	public ModelViewPage clickEditTemplateButton(String template) {
		assertTrue(click(String.format("//label//span[contains(text(),'%s')]/../../../../following-sibling::div[2]//button",template)));
		waitForElementVisible("[class='modal-body']");
		return this;
	}

	public ModelViewPage clickFieldMapping() {
		assertTrue(click("//span[@class='menu-item-text d-lg-inline-block wrap-text'][text()='Field Mapping']"));
		waitForElementInvisible(loadingIndicators);
		return this;
	}

	public ModelViewPage clickInlineEditForFieldMapping() {
		assertTrue(click("(//a[@title='Edit Row'])[4]"));
		return this;
	}

	public ModelViewPage clickSaveAndClose() {
		assertTrue(click("//span[text()='Save & Close']"));
		return this;
	}

	public ModelViewPage clickSessions() {
		assertTrue(click("//span[@class='menu-item-text d-lg-inline-block wrap-text'][text()='Sessions']"));
		waitForElementInvisible(loadingIndicators);
		return this;
	}

	public ModelViewPage clickInlineEditForSessions() {
		assertTrue(click("(//i[@class='fas fa-edit fa-primary-color_#000000 fa-secondary-color_#000000'])[2]"));
		waitForElementVisible("(//div[@class='modal-body'])[2]");
		return this;
	}

	public ModelViewPage clickDeleteEditForSessions() {
		assertTrue(click("(//i[@class='fas fa-trash-alt fa-primary-color_#000000 fa-secondary-color_#000000'])[1]"));
		assertTrue(click(confirmButton));
		clickOk();
		return this;
	}

	public ModelViewPage clickSettingsButton() {
		hasPageLoaded();
		waitForElementVisible(settingsBtn);
		assertTrue(click(settingsBtn));
		waitForElementInvisible(pleaseWait);
		return this;

	}

	public ModelViewPage clickEditIconInSettingsPageAndCheckPopupOpens() {
		assertTrue(click(findElement("//button[@data-content='Edit Template']")));
		waitForElementInvisible(pleaseWait);
		waitForElementVisible(updateTemplateBtn);
		assertTrue(isElementPresent(updateTemplateBtn));
		return this;
	}

	public ModelViewPage cloneTemplate(String tempName) {
		assertTrue(click("(//button[@data-content='Clone Template'])[1]"));
		waitForElementInvisible(pleaseWait);
		clear("div[itemid='TEXT_INPUT_557231375346182'] input");
		setText("div[itemid='TEXT_INPUT_557231375346182'] input", tempName);
		assertTrue(click(cloneTemplateBtn));
		clickOk();
		String isCloned = format("//span[contains(text(),'%s')]", tempName);
		System.out.println(isCloned);
		assertTrue(isElementPresent(isCloned));
		return this;
	}

	public ModelViewPage discardClonedTemplate(String tempName) {
		String edit = format(
				"(//div[contains(@id,'TCCREPEATERROW')]/div/div/div/label/span[contains(text(),'%s')]/../../../../following::div[1])[1]//button",
				tempName);
		assertTrue(click(edit));
		waitForElementInvisible(pleaseWait);
		setValueInDropdowns("MENU_561974778824627", "Discarded");
		//selectOptionFromDropdownMenu("MENU_561974778824627", "Discarded");
		assertTrue(click(updateTemplateBtn));
		return this;
	}

	public Boolean isManageCurrencyOpened() {
		hasPageLoaded();
		waitForElementVisible("//span[text()='Manage Currency']");
		return true;
	}

	public ModelViewPage clickManageUnits() {
		assertTrue(click("//button[text()='Manage Units']"));
		waitForElementInvisible(pleaseWait);
		waitForElementVisible("//div[text()='Manage Units']");
		return this;
	}

	public String getRateToBase() {
		String getRateToBase = getText("// div[@column=3]/div[@role='gridcell'][1]");
		System.out.println("Rate : " + getRateToBase);
		return getRateToBase;

	}

	public String clickEditIconInCurrecnyPopupAndEnterRateToBase() {
		assertTrue(click("//div[@column=6]/div[@role='gridcell'][1]/div/a[@title='edit']"));
		waitForElementInvisible(pleaseWait);
		String enterRateToBase = RandomStringUtils.randomNumeric(1);
		System.out.println("Entered " + enterRateToBase);
		clear(rateToBaseInput);
		setText(rateToBaseInput, enterRateToBase);
		assertTrue(click("//span[text()='Save']"));
		waitForElementVisible(okInPopUp);
		clickOk();
		return enterRateToBase;
	}

	public String getStatusValue() {
		String status = findElement("//input[@placeholder='Status']").getAttribute("value");
		return status;
	}

	public String getEmployeeCurrency() {
		return getText("(//div[@aria-rowindex='3']//span[@class='custom_renderer_click'])[1]");
	}

	public ModelViewPage clickShowEmployeeInOwnCurrencyCheckbox() {
		assertTrue(click("//input[@value='SHOW_IN_EMP_CURRENCY']"));
		return this;
	}

	public ModelViewPage clickHideChartButton() {
		findElement(By.cssSelector("div[itemid='BUTTON_1406863259271139'] >div.custom-control.custom-switch.no"))
				.click();
		;
		return this;
	}

	public boolean isChartsAndCards() {
		doWait(3000);
		return findElement("[itemid='GRID_LAYOUT_COLUMN_1606993827180_0']").isDisplayed();
	}

	public ModelViewPage clickShowAllRadioButton() {
		assertTrue(click("(//div[contains(@id,'item-id1142709741951797')]//input)[2]"));
		return this;
	}

	public ModelViewPage clickDirectRadioButton() {
		assertTrue(click("[value='DIRECT GROUP,REGULAR_DIRECT_GROUP']"));
		return this;
	}

	public ModelViewPage clickRollupRadioButton() {
		assertTrue(click("[value='ROLLUP GROUP,REGULAR_ROLLUP_GROUP,DIRECT GROUP,REGULAR_DIRECT_GROUP']"));
		return this;
	}

	public ModelViewPage clickEditSession() {
		assertTrue(click("(//button[@data-content='Edit Calibration Session'])[1]"));
		waitForElementInvisible(pleaseWait);
		return this;
	}

	public ModelViewPage editUserPoolDropDown() {

		selectOptionFromDropdownMenu("MENU_5e0556e7", "DISCARDED");
		doWait(1000);
		assertTrue(click("//div[@class='webix_inp_static']"));
		//assertTrue(click("(//span[@role='checkbox'])[1]"));
		clickOnBlankSpace();
		assertTrue(click(saveSession));
		waitForElementVisible("//div[text()='Data has been updated']");
		assertTrue(isElementPresent("//div[text()='Data has been updated']"));
		clickOk();
		return this;
	}

	public boolean isEmployeesUnderManager() {
		return isElementPresent("[itemid='GRID_LAYOUT_COLUMN_1607079367665_0']");
	}

	public boolean isEmployeesUnderManagerGrid() {
		String managerGrid = "//div[contains(@class,'webix_hcell webix_last')]//div[contains(text(),'Employee Name')]";
		waitUntilVisible(managerGrid);
		return findElement(managerGrid).isDisplayed();
	}

	public boolean isNoSessionData() {
		return isElementPresent("[originalvalue='No Session Data!']");
	}

	public ModelViewPage clickFunnelFilterIconFor(String filterName) {
		assertTrue(click(format("//div[text()=' %s']/parent::div/following-sibling::div//span", filterName)));
		return this;
	}

	public ModelViewPage selectValueFromManagerDropdown(String select) {
		setText("//input[@placeholder='Select Manager']", select);
		assertTrue(click(format("//div[@class='webix_list_item'][text()='%s' and @tabindex='0']", select)));
		return this;
	}

	public ModelViewPage selectStatusInEditSession(String status) {
		selectOptionFromDropdownMenu("MENU_5e0556e7", "DISCARDED");
		return this;
	}

	public ModelViewPage editTitleStatusAndSave(String titleName) {
		clear(editSessionTitleInput);
		setText(editSessionTitleInput, titleName);
		selectStatusInEditSession("DISCARDED");
		saveSession();
		return this;
	}

	public ModelViewPage editRow(String rowText) {
		assertTrue(click("(//a[@title='Edit Row'])[4]"));
		clear("div[itemid='TEXT_INPUT_577527050707639'] input");
		setText("div[itemid='TEXT_INPUT_577527050707639'] input", rowText);
		assertTrue(click("//span[text()='Save & Close']"));
		return this;
	}

	public ModelViewPage clickBrowse() {
		assertTrue(click("//span[text()='Browse']"));
		waitForElementInvisible(pleaseWait);
		return this;
	}

	public String selectApp(String appName) {
		doWait(1000);
		assertTrue(click("//label/h6[text()='Products']/../following-sibling::div//input"));
		assertTrue(click("(//div[@webix_l_id='TCC_CONSTANT_ALL'])[1]"));
		doWait(4000);
		assertTrue(click("//label/h6[text()='Type']/../following-sibling::div//input"));
		assertTrue(click("(//div[@webix_l_id='TCC_CONSTANT_ALL'])[2]"));
		setText("input[placeholder='Template details']", appName);
		clickOnBlankSpace();
		assertTrue(click("//span[text()='SELECT']"));
		return appName;
	}

	public String[] getCalibratedPercentValue() {
		return getText(
				"//div[@aria-rowindex='1' and @aria-colindex='13']//span[@class='text-primary border border-primary']")
				.split(".");
	}

	public ModelViewPage clickInlineAction() {
		assertTrue(click("(//a[@title='User'])[1]"));
		return this;
	}

	public String isAppGotAdded() {
		waitForElementVisible("div[itemid='TEXT_d6ce87d3'] span div");
		String selectedApp = getText("div[itemid='TEXT_d6ce87d3'] span div");
		return selectedApp;

	}

	public ModelViewPage addNewUnit(String unitName, String unitCode) {
		assertTrue(click("//button[text()='Add New Unit']"));
		String unitcode = "//div[text()='Unit Code*']/../../following::div[1]/div/div/input";
		String unitname = "//div[text()='Unit Name']/../../following::div[1]/div/div/input";
		setText(unitcode, unitCode);
		doWait(500);
		setText(unitname, unitName);
		doWait(1500);
		assertTrue(click("//div[@itemid='BUTTON_447268842769370']/button"));// save
		doWait(3000);
		clickOk();
		return this;
	}

	public ModelViewPage deleteAddedUnit(String unitName) {
		assertTrue(
				click("(//div[@row='1' and @column='2']/span[contains(@class,'webix_excel_filter webix_icon')])[2]"));
		clear(filterIconInGrid);
		setText("//div[contains(@class,'webix_view webix_control webix_el_text')]/div/input", unitName);
		clickOnBlankSpace();
		doWait(2000);
		assertTrue(click(
				"(//div[@aria-rowindex='1']//a//i[contains(@class,'fas fa-trash-alt fa-primary-color_#000000 fa-secondary-color_#000000')])[2]"));
		assertTrue(click(confirmButton));
		return this;
	}

	public ModelViewPage openSettingsPageTabs(String tab) {
		assertTrue(click(format("//span[text()='%s']", tab)));
		return this;
	}

	public ModelViewPage UserPoolsUnderProduct() {
		selectOptionFromDropdownMenu("select_a_product_copy_15211743447241", "INCENTview");
		waitForElementInvisible("//h5[text()='Working on it...']");
		List<WebElement> poolsUnderProduct = findElements("//div[@class='webix_column_webix_first']/div");
		for (WebElement pools : poolsUnderProduct) {
			System.out.println("Pools under " + "product : " + (getText(pools)));
		}
		return this;
	}

	public ModelViewPage selectProductToFilterUserPools(String product) {
		assertTrue(click("//div[@itemid='select_a_product_copy_15211743447241']"));
		String productName = format("//div[@webix_l_id='%s']", product);
		WebElement prd = findElement(productName);
		if (prd.isDisplayed()) {
			assertTrue(click(prd));
			waitForElementInvisible("//h5[text()='Working on it...']");
		}
		return this;
	}

	public ModelViewPage clickMemberIcon() {
		assertTrue(click("(//a[@title='Members'])[1]"));
		return this;

	}

	public String getPoolName() {
		waitForElementInvisible(pleaseWait);
		String pool = getAttribute("div[itemid='TEXT_INPUT_2695325971017856'] input", "value");
		return pool;
	}

	public List<String> poolMembers() {
		List<WebElement> poolsUnderProduct = findElements(
				"(//div[@class='webix_ss_center']/div/div[@column='1'])[2]/div[@role='gridcell']");
		List<String> pools = new ArrayList<String>();
		for (WebElement pool : poolsUnderProduct) {
			String poolName = getText(pool);
			pools.add(poolName);
			System.out.println("User Pools Under Product : " + pools);
		}
		return pools;
	}

	public String getPoolGuid() {
		assertTrue(click(closePopup));
		assertTrue(click("(//a[@title='show pool guid'])[1]"));
		String poolGuid = getText("//div[text()='Pool Details']/following-sibling::div");
		clickOk();
		return poolGuid;
	}

	public ModelViewPage addNewRate() {
		assertTrue(click("//button[text()='Add New Rate']"));
		return this;
	}

	public Boolean isManagePoolsOpened() {
		hasPageLoaded();
		waitForElementVisible("//div[text()='User Pools']");
		return true;
	}

	public ModelViewPage createNewCurrencySet(String currencyName) {
		setText("div[itemid='TEXT_INPUT_445838450031797'] input", currencyName);
		assertTrue(click("//div[@itemid='MENU_523040827382469']")); // Currency Code DD
		selectRandomListItemFromOpenWebixList();
		assertTrue(click("//div[@itemid='MENU_523053800713772']"));// Base Currency DD
		selectRandomListItemFromOpenWebixList();
		assertTrue(click("(//span[contains(@class,'wxi-calendar')])[2]"));
		assertTrue(click("(//div[@day='2'])[5]"));
		setText("div[itemid='TEXT_INPUT_445834665385843'] input", "1.0");
		assertTrue(click("//span[text()='Save']"));
		clickOk();
		return this;
	}

	public ModelViewPage deleteAddedCurrencySet(String currencyName) {
		assertTrue(click("//div[@row='1' and @column='0']//span[contains(@class,'webix_excel_filter webix_icon')]"));
		clear(filterIconInGrid);
		setText("//div[contains(@class,'webix_view webix_control webix_el_text')]/div/input", currencyName);
		assertTrue(click("//a/i[contains(@class,'fas fa-trash-alt')]"));
		assertTrue(click(confirmButton));
		return this;

	}

	public boolean isInlineAction() {
		return isElementPresent("[class='bootbox-body']");
	}

	public ModelViewPage clickInlineToLock() {
		assertTrue(click("(//a[@title='unlock'])[1]"));
		assertTrue(click(confirmInPopUp));
		return this;
	}

	public ModelViewPage clickInlineToUnLock() {
		assertTrue(click("(//a[@title='lock'])[1]"));
		assertTrue(click(confirmInPopUp));
		return this;
	}

	public ModelViewPage clickClose() {
		assertTrue(click("[class='bootbox-close-button close']"));
		return this;
	}

	public ModelViewPage clickDownload() {
		assertTrue(click("//span[text()='DOWNLOAD']"));
		assertTrue(click(confirmInPopUp));
		assertTrue(click(confirmInPopUp));
		waitForElementInvisible("//h5[text()='Downloading...']");
		return this;
	}

	public ModelViewPage clickMarkAsDone() {
		assertTrue(click("//span[text()='MARK AS DONE']"));
		assertTrue(click(confirmInPopUp));
		if (isElementVisible(okInPopUp))
			assertTrue(click(okInPopUp));
		return this;
	}

	public String getBackGroundColorOfCalibrationGrid() {
		doWait(3000);
		String color = getCssValue("(//span[@class='custom_renderer_click']/..)[2]", "background-color");
		return Color.fromString(color).asHex();
	}

	public ModelViewPage clickBack() {
		assertTrue(click("//i[@class='far fa-arrow-circle-left   font-icon ']"));
		return new ModelViewPage();
	}

	public void selectPaginationNumber(String value) {
		clickAndSelectValue("//select[@class='webixGridPagerSize']", value);
	}

	public int getNumberOfRowsInPagination() {
		return findElements("(//a[@title='User'])").size();
	}

	public ModelViewPage clickAddPool() {
		assertTrue(click("(//i[@class='fal fa-plus-square'])[1]"));
		waitForElementVisible("//div[text()='Pool Definition']");
		return this;

	}

}
