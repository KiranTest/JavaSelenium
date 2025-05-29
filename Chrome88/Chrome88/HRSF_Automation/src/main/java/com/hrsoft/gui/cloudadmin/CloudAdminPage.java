package com.hrsoft.gui.cloudadmin;

import static com.hrsoft.reports.ExtentLogger.pass;
import static java.lang.String.format;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import com.hrsoft.config.ConfigFactory;
import com.hrsoft.constants.Constants;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.utils.seleniumfy.BasePage;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */
public class CloudAdminPage extends BasePage {
	private String loading = "//*[@class='loadingIframeData']//*[text()='Loading...']";
	private String dialogueFrame = "//iframe[@id='dialogFrame']";
	private String pleaseWait = "//h5[text()='Please Wait...']";
	private String managerUserName = "//input[contains(@id,'TEXT_INPUT_1517540313596_copy_245_copy_60')]";
	private String searchButton = "//button[contains(@id,'BUTTON_1517540552356_copy_245_copy_60')]";
	private String confirm = "//button[contains(@class,'btn btn-primary btn-md')]";
	private String okPopUp = "[class='btn btn-secondary btn-md']";
	private String customurl = "content/portal/tccserver/loadApp.htm?wiztype=HRSOFTCLOUD_APPD_MANAGE_DATA_FEEDS_V2&wizinstanceid=";
	private String dataManagementUrl = customurl + "36BAF6B0-3834-4475-8043-D3AADDF24FE0";
	private String apiManagementUrl = customurl + "1DA913C9-455E-4FB6-8235-1AEB0111CA21";
	private String attributesUrl = customurl + "AF35C314-DCD6-4F8A-BA49-25D4279C9FDD";
	private String umeiAttributesUrl = customurl + "9B5FDA36-F23A-40C9-B30F-4C4103408B16";
	private String securityManagementUrl = customurl + "A0CF99D0-DC06-48D3-91BC-A8621BD7B46C";
	private String environmentalOpt = "//div[@itemid='LIST_4187061172736757']//span[text()='Environmental Options']";
	private String closeEnvOptionPopup = "//button//span[@class='ui-button-icon-primary ui-icon ui-icon-closethick']";
	private String navigationView = "//span/div[text()='Refresh Navigation View']";

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions
				.visibilityOfElementLocated(locateBy("[itemid='product_explorer_list_copy_11_copy_102']"));
	}

	public ManageDataObjects clickManageDataObjects() {
//        assertTrue (click ("//li[text()='Data Fields']"));
		DriverManager.getDriver().get(ConfigFactory.getConfig().webAppicationURL()
				+ "content/portal/tccserver/loadApp.htm?wiztype=HRSOFTCLOUD_ADCW_MANAGECLOUDDATAOBJECTS&wizinstanceid=AF9EBF48-0C7C-472C-9219-C06DB2B183A8");
		waitUntilVisible("//div[text()='Manage Data Objects and Data Fields']");
		return new ManageDataObjects();
	}

	public DatasetExplorer clickDatasetExplorer() {
//      assertTrue (click ("//li[text()='Data Fields']"));
		DriverManager.getDriver().get(ConfigFactory.getConfig().webAppicationURL()
				+ "content/portal/tccserver/loadApp.htm?wizinstanceid=C1ADF74F-08C7-4E40-8321-ED6CC20280CB&paramloadpage=%2Fcontent%2Fportal%2Ftccserver%2FloadApp.htm%3Fwiztype%3Dtccapp%26wizinstanceid%3D8EDB1742-F381-4051-BB50-5DA6AE34C18E%26componentmode%3Dtrue%26");
		waitUntilVisible(
				"div[itemid='TEXT_1517352149434_copy_6464111615062_copy_14665317309890_copy_15653949751029'] h2 div");
		return new DatasetExplorer();
	}

	public DataManagementPage clickDataManagement(String url) {
		String fullUrl = url + dataManagementUrl;
		assertTrue(navigateTo(fullUrl));
		waitUntilVisible("//div[@itemid='select_a_aeedd78e']"); // product drop down
		waitUntilVisible("//div[@itemid='NAVIGATION_296057045140243']"); // navigation menu
		return new DataManagementPage();
	}

	public void clickProductExplorer() {
		assertTrue(click("//li[text()='Lookup Products by Client']"));
		navigateToTab(1);
		assertTrue(hasPageLoaded());
	}

	public DataManagementPage clickAPIManagementPage(String url) {
		String fullUrl = url + apiManagementUrl;
		assertTrue(navigateTo(fullUrl));
		return new DataManagementPage();
	}

	public void switchToDialogueFrame() {
		switchToDefaultContent();
		switchToFrame("//iframe[@class='w-100 tcc-admin-popup']");
		switchToFrame(dialogueFrame);
	}

	public CloudAdminPage clickEnvironmentalOptions() {
		assertTrue(click("//span[text()='Environmental Options' and @class='text-label  text-primary']"));
		assertTrue(waitForElementInvisible(loading));
		return this;
	}

	public CloudAdminPage clickPlannerCanReviseTheirOwnLastNote() {
		switchToDialogueFrame();
		assertTrue(click("//td[text()='plannerCanReviseTheirOwnLastNote']//preceding-sibling::td//child::a"));
		return this;
	}

	public CloudAdminPage clickusePromotionAdjustmentScreen() {
		switchToDialogueFrame();
		String xpath = "//td[text()='usePromotionAdjustmentScreen']//preceding-sibling::td//child::a";
		waitForElementVisible(xpath);
		assertTrue(click(xpath));
		return this;
	}

	public CloudAdminPage clickEnableSpreadsheetScrolling() {
		switchToDialogueFrame();
		assertTrue(click("//td[text()='enableSpreadsheetScrolling']//preceding-sibling::td//child::a"));
		return this;
	}

	public CloudAdminPage clickManagePlans() {
		switchToDialogueFrame();
		assertTrue(click("//span[text()='Manage Plans' and @class='text-label  text-primary']"));
		return this;
	}

	public CloudAdminPage clickManageCoPlanners() {
		assertTrue(click("(//div[@class='translatable question_title text-hoverable']//li[text()='Users'])[2]"));
		assertTrue(waitForElementInvisible(pleaseWait));
		return this;
	}

	public CloudAdminPage clickManageUserDefinedSpareFields() {
		assertTrue(click("//span[text()='Manage User-Defined Spare Fields']"));
		// waitForElementInvisible(loadingIndicators);
		return this;
	}

	public CloudAdminPage searchAndEnableSpareFieldForAudit(String spareField) {
		switchToDialogFrame();
		setText(format("//input[@id='txtFilterTable']"), spareField);
		String edit = format("//input[@value='%s' and @name='id']/../../td//a[text()='Edit']", spareField);
		assertTrue(click(edit));
		WebElement x = findElement("//td/input[@type='checkbox']");
		if (!x.isSelected())
			assertTrue(click(x));
		assertTrue(click("//input[@value='Save' and @name='save']"));
		return this;
	}

	public ManageUsersPage clickManageUsers(String url) {
		DriverManager.getDriver().get(url
				+ "content/portal/tccserver/loadApp.htm?mode=RUN&wiztype=TCC_UMEI_APPD_MANAGE_USERS_2&wizinstanceid=18EADEDC-7D23-477D-AE24-1AAD479986CB");
		// assertTrue (click
		// ("[itemid='GRID_LAYOUT_COLUMN_1517094953183_0_copy_173']"));
		assertTrue(waitForElementInvisible(pleaseWait));
		return new ManageUsersPage();
	}

	public CloudAdminPage assignNewRoleToUser(String text) {
		assertTrue(click("//div[@class='webix_ss_left']//descendant::a[@title='Edit']"));
		assertTrue(waitForElementInvisible(pleaseWait));
		assertTrue(click("//button[@class='btn-sm btn btn-primary waves-effect waves-light']"));
		assertTrue(waitForElementInvisible(pleaseWait));
		String withDrawablePlan = format(
				"//h6[text()='Existing Role Membership']/following::div[text()='%s' and @role='gridcell']",
				Constants.PlanningCanEditWithDrawableGroup);
		if (findElement(withDrawablePlan).isDisplayed()) {
			assertTrue(click("//button[contains(@id,'BUTTON_49fb689b')]"));
		} else {
			assertTrue(click("//div[@column='1']//span[@class='webix_excel_filter webix_icon wxi-filter']"));
			assertTrue(setText(
					"//div[@class='webix_view webix_control webix_el_text' and contains(@style,'display: inline-block;')]//input",
					text));
			String role = format("[webix_l_id='%s']", text);
			if (getAttribute(role, "aria-selected").equals(null))
				assertTrue(click(role));
			assertTrue(click("//a[@title='Assign']"));
			assertTrue(click(okPopUp));
			assertTrue(click("//span[text()='Recalculate Security and Close']"));
			assertTrue(click(okPopUp));
		}
		assertTrue(click("//span[text()='Save Changes']"));
		assertTrue(click(okPopUp));
		assertTrue(click(okPopUp));
		assertTrue(click(okPopUp));
		return this;

	}

	public void closePopUp() {
		switchToDefaultContent();
		assertTrue(click("(//span[@class='ui-button-icon-primary ui-icon ui-icon-closethick'])[1]"));
	}

	public void setManager(String manager) {
		assertTrue(click("//span[text()='Manager']"));
		assertTrue(waitForElementInvisible(pleaseWait));
		assertTrue(setText(managerUserName, manager));
		assertTrue(click(searchButton));
		doWait(5000);
		assertTrue(click(
				"//*[text()=' User Name']/../../../../..//following-sibling::div//input[@class='webix_table_checkbox']"));
		assertTrue(click("//span[text()='Select and Exit']"));
	}

	public void setCoPlanner(String coPlanner) {
		assertTrue(click("//span[text()='Co-planner']"));
		assertTrue(waitForElementInvisible(pleaseWait));
		assertTrue(setText(managerUserName, coPlanner));
		assertTrue(click(searchButton));
		doWait(5000);
		assertTrue(click(
				"//*[text()=' User Name']/../../../../..//following-sibling::div//input[@class='webix_table_checkbox']"));
		assertTrue(click("//span[text()='Select and Exit']"));
	}

	public void setManagerAndCoPlanner(String manager, String coPlanner) {
		assertTrue(click("(//button[contains(@class,'btn-sm btn btn-secondary')])[1]"));
		setManager(manager);
		setCoPlanner(coPlanner);
	}

	public void clickSave() {
		assertTrue(click("//span[text()='Save']"));
		assertTrue(click("[class='btn btn-secondary btn-md']"));
		assertTrue(waitForElementInvisible(pleaseWait));
	}

	public void deleteAnvitaCoplanner() {
		String record = "//span[contains(text(),'Eric Dirks')]/following::span[contains(text(),'Anvita Priyam')]/following::a[3]";
		if (isElementPresent(record)) {
			assertTrue(click(record));
			assertTrue(click(confirm));
			assertTrue(click(okPopUp));
		}
	}

	public void recalculateSecurityAndClose() {
		assertTrue(click("//span[text()='Recalculate Security and Close']"));
		assertTrue(waitForElementInvisible(pleaseWait));
		assertTrue(click("[class='btn btn-secondary btn-md']"));
	}

	public void restrictToCompFilter(String filter) {
		assertTrue(click(
				"//*[text()='Restrict to Comp Filter']/../../../../following-sibling::div//input[@class='tcc-unselectable']"));
		assertTrue(click(format("(//div[text()='%s'])[2]", filter)));
	}

	public void selectYesOrNo(String option) {
		switchToDialogueFrame();
		assertTrue(clickAndSelectText("#optionValue", option));
		assertTrue(click("//input[@type='submit' and @class='button' and @value='Save']"));
		closePopUp();
	}

	public ConfigureAndManageAttributes clickAttributes(String url) {
		String fullUrl = url + attributesUrl;
		assertTrue(navigateTo(fullUrl));
		return new ConfigureAndManageAttributes();
	}

	public CloudAdminPage clickRolesAndPermissionManagement(String url) {
		DriverManager.getDriver().get(url
				+ "content/portal/tccserver/loadApp.htm?wiztype=TCC_UMEI_ADCW_ROLE_MANAGEMENT&wizinstanceid=82876017-982A-40F4-9BBA-4185BEBA4307");
		waitUntilVisible("//h2//div[text()='Role Permissions Mappings']");
		return this;
	}

	public CloudAdminPage selectRoleId(String option) {
		String selectXpath = "//div[text()=' Role Id']/../following-sibling::div//select";
		Select s = new Select(findElement(selectXpath));
		s.selectByValue(option);
		return this;
	}

	public CloudAdminPage clickSystemAssigned() {
		assertTrue(click("//a[@title='System assigned']"));
		waitForElementVisible("//*[text()=' Permission']/../following-sibling::div//select");
		return this;
	}

	public CloudAdminPage selectPermissionsAndSave(String option) {
		String selectXpath = "//*[text()=' Permission']/../following-sibling::div//select";
		Select s = new Select(findElement(selectXpath));
		s.selectByValue(option);
		if (!isElementPresent("//i[contains(@class,'fal fa-check-square fa-2x fa-primary-color')]"))
			assertTrue(click("//i[contains(@class,'far fa-stop fa-2x fa-primary-color')]"));
		assertTrue(click("//span[text()='Save Changes']"));
		return this;
	}

	public CloudAdminPage clickSynchronizeUmeiRoles() {
		assertTrue(click(".fal.fa-retweet"));
		return this;
	}

	public CloudAdminPage clickAddNewRoleInRoleAndPermission() {
		assertTrue(click("//i[@class='far fa-plus fa-primary-color_#000000 fa-secondary-color_#000000']"));
		waitForElementInvisible(pleaseWait);
		return this;
	}

	public UmeiAttributes clickUmeiAttribute(String url) {
		String fullUrl = url + umeiAttributesUrl;
		assertTrue(navigateTo(fullUrl));
		return new UmeiAttributes();
	}

	public ManagePlansPage navigateToManagePlans(String url) {
		DriverManager.getDriver()
				.get(url + "content/portal/wizard/startWizard.htm?wiztype=COMPENSATION_PLAN_CONFIG&mode=manage");
		pass("Navigated to Manage plans page");
		return (ManagePlansPage) navigateToPage(ManagePlansPage.class);
	}

	// public void clearNotesForEmployeeSearch (String plan,String group ) {
	// DriverManager.getDriver ()
	// .get (getConfig ().webAppicationURL () +
	// "content/portal/tccserver/loadApp.htm?wizinstanceid=C1ADF74F-08C7-4E40-8321-ED6CC20280CB");
	// clickAndSelectText ("select[name='hierarchyId']", plan);
	//
	// }
	public CloudAdminPage editPromotionAdjustMent() {
		doWait(2000);
		switchToDialogFrame();
		assertTrue(click("//td//a[contains(@href,'usePromotionAdjustmentScreen')]"));
		return this;
	}

	public CloudAdminPage enablePromotionAdjustmentOption(String option) {
		assertTrue(click("//select[@id='optionValue']"));
		clickAndSelectText("//select[@id='optionValue']", option);
		return this;
	}

	public CloudAdminPage savePromotionAdjustmentOption() {
		assertTrue(click("//input[@value='Save' and @name='eventSubmit_doSave']"));
		switchToDefaultContent();
		assertTrue(click(closeEnvOptionPopup));
		return this;
	}

	public SecurityManagement clickSecurityManagement(String url) {
		String fullUrl = url + securityManagementUrl;
		assertTrue(navigateTo(fullUrl));
		waitUntilVisible("//div[text()='Security Management']");
		return new SecurityManagement();
	}

	public CloudAdminPage refreshNavigationView() {
		assertTrue(click(navigationView));
		waitForElementInvisibleCustom("//h5[@class='modal-title' and contains(text(),'Refreshing')]");
		assertTrue(click("//button[@class='btn btn-secondary btn-md' and text()='OK']"));
		refresh();
		return this;
	}

}
