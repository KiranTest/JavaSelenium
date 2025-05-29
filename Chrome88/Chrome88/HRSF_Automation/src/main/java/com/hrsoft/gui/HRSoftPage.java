package com.hrsoft.gui;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static com.hrsoft.driver.DriverManager.getDriver;
import static com.hrsoft.reports.ExtentLogger.info;
import static com.hrsoft.reports.ExtentLogger.pass;
import static java.lang.String.format;
import static org.testng.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;
import com.hrsoft.config.ConfigFactory;
import com.hrsoft.constants.Constants;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.gui.cloudadmin.CloudAdminPage;
import com.hrsoft.gui.cloudadmin.ConfigureAndManageAttributes;
import com.hrsoft.gui.compensation.CompOverviewPage;
import com.hrsoft.gui.compensation.DistributeFundPage;
import com.hrsoft.gui.compensation.PlanOverviewPage;
import com.hrsoft.gui.compensation.PlanningPage;
import com.hrsoft.gui.compensation.ReviewingPage;
import com.hrsoft.gui.dataview.DataViewPage;
import com.hrsoft.gui.dataview.DataViewSettingsPage;
import com.hrsoft.gui.modelview.ModelViewPage;
import com.hrsoft.gui.rewardsview.ManagerViewPage;
import com.hrsoft.utils.seleniumfy.BasePage;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 **/
public class HRSoftPage extends BasePage {
	private String homepageIcon = "//div//i[@class='fal font-icon tcc_product_icon fa-2x tcc_product_hrsoftcloud']";
	private String proxyAs = "//span[text()='Proxy As..']";
	private String settingsBtn = "div[itemid='BUTTON_996295427193974'] button";
	private String proxyUserId = "#promptBox";
	private String proxyLoginBtn = "//button[text()='Submit']";
	private String proxyConfirmLogin = "//span[text()='Confirm']";
	private String dataViewIcon = ".question_title  .font-icon.tcc_product_dataview";
	private String dataViewBtn = "//button//i[@class='fal font-icon tcc_product_icon fa-2x tcc_product_dataview']";
	private String searchbox = "(//div[@class='webix_el_box']//input[@role='combobox'])";
	private String cloudAdmin = "//span[text()='Cloud Admin']";
	private String compOverview = "//ul/li[text()='Overview']";
	private String manageFilter = "//ul/li[text()='Manage Filters']";
	private String myTaskSubmitButton = "//p[contains(text(),'Submit Compensation Plan ')]";
	private String pleaseWait = "//h5[text()='Please Wait...']";
	private String loadingIndicators = "(//img[@src='/resources/skins/default/portal/new/images/wait-icon.gif'])[2]";
	private String loadingText = "//*[text()='Loading...' and @class='iblink']";
	private String dropDownLabel = "//div[@class='form-group']/label/h6[text()='%s']";
	private String namedDropDown = "//div[@class='form-group']/label/h6[text()='%s']/../..//input[@role='combobox']";
	private String webixListBox = "//div[contains(@class,'webix_popup')][contains(@style,'display: block;')]";
	private String webixListItem = ".//div[@role='option']";
	private String webixSelectedvalue = ".//div[@class='webix_list_item  webix_selected']";
	private String webixListItemByValue = ".//div[@role='option'][text()='%s']";
	private String webixListItemByWebixId = ".//div[@role='option'][@webix_l_id='%s']";
	private String clickSetupFilters = "//span[contains(text(),'Setup Filters and Eligibility Groups')]";
	private String distFundLinkxpath = "//ul[@class='flyout-list']/li[@planid='%s']/following::ul[@class='flyout-compplan']/li[contains(text(),'%s')]";
	private String cancelProxyWithIcon = "//button[@data-content='Cancel Proxy']";
	private String planDistributionWidget = "div[id$='GRID_LAYOUT_COLUMN_1606738814160_0']";
	private String planBudgetWidget = "div[id$='GRID_LAYOUT_COLUMN_1606829593999_0']";
	private String continueLogout = "//button[text()='Continue']";
	private String logOut = "//span[text()='Logout']";
	private String cancellingProxy = "//h5[contains(text(),'Cancelling Proxy Session. It might take time to clear the session. Please be patient...')]";

	public HRSoftPage navigateToHomePage() {
		waitForElementVisible(homepageIcon);
		assertTrue(isElementPresent(homepageIcon));
		assertTrue(hasPageLoaded());
		return this;
	}

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions
				.visibilityOfElementLocated(locateBy("//div//i[contains(@class,'tcc_product_hrsoftcloud')]"));
	}

	public LoginPage logOut() {
		switchToDefaultContent();
		assertTrue(click(settingsBtn));
		assertTrue(click(logOut));
		assertTrue(click(continueLogout));
		return (LoginPage) navigateToPage(LoginPage.class);
	}

	public HRSoftPage scrollToTop() {
		WebElement el = findElement("//div[@itemid='GRID_LAYOUT_36077728713015']");
		scrollToView(el);
		return this;
	}

	public HRSoftPage clickNavigationBar() {
		assertTrue(click("//i[@class='fal fa-align-justify font-icon  ']"));
		return this;
	}

	public boolean navigationBarExpansion() {
		return isElementVisible("//*[text()='Portal']");
	}

	public HRSoftPage clickProxyAs() {
	    doWait (3000);
		waitForElementClickable(settingsBtn);
		assertTrue(click(settingsBtn));
		assertTrue(click(proxyAs));
		return this;
	}

	public HRSoftPage clickProxyAsAndEnterUserId(String user) {
		assertTrue(click(settingsBtn));
		assertTrue(click(proxyAs));
		assertTrue(setText(proxyUserId, user));
		return this;
	}

	public HRSoftPage enterProxyUser(String user) {
		clickProxyAs();
		assertTrue(setText(proxyUserId, user));
		return this;
	}

	public HRSoftPage clickProxySubmitButton() {
		assertTrue(click(proxyLoginBtn));
		assertTrue(click(proxyConfirmLogin));
		assertTrue(hasPageLoaded());
		return this;
	}

	public HRSoftPage proxyAsAdminUser() {
		clickProxyAs();
		assertTrue(setText(proxyUserId, ConfigFactory.getConfig().proxyUser()));
		clickProxySubmitButton();
		assertTrue(hasPageLoaded());
		info("proxied as admin user");
		return new HRSoftPage();
	}

	public HRSoftPage proxyAs(String proxyuser, String custId) {
		clickProxyAs();
		assertTrue(setText(proxyUserId, proxyuser));
		if (custId != null && !custId.isEmpty()) {
			assertTrue(click(searchbox + "[4]"));
			assertTrue(click(format("//div[@webix_l_id='%s']", custId)));
		}
		clickProxySubmitButton();
		assertTrue(hasPageLoaded());
		pass(format("proxied as %s user", proxyuser));
		return this;
	}

	public HRSoftPage proxyAs(String proxyuser) {
		refresh();
		closeBurgermenu();
		clickProxyAs();
		assertTrue(setText(proxyUserId, proxyuser));
		clickProxySubmitButton();
		assertTrue(hasPageLoaded());
		pass(format("proxied as %s user", proxyuser));
		doWait(5000);
		return this;
	}

	public HRSoftPage clickCompensationPlanDropdown() {
		assertTrue(click("//input[@placeholder='Select Plan' and @role='combobox']"));
		return this;
	}

	public HRSoftPage selectCompensationPlan(String planName) {
		assertTrue(waitForElementInvisible(loadingIndicators));
		setValueInDropdowns("MENU_522458153913713", planName);
		assertTrue(waitForElementInvisible(loadingIndicators));
		info(format("selected %s plan", planName));
		return this;
	}

	public HRSoftPage selectCompensationGroup(String planName) {
		assertTrue(waitForElementInvisible(loadingIndicators));
		setValueInDropdowns("MENU_522463974194506", planName);
		assertTrue(waitForElementInvisible(loadingIndicators));
		info(format("selected %s group", planName));
		return this;
	}

	public HRSoftPage clickPlanStatusDistribution() {
		String s = "//div[text()='Plan Status Distribution']";
		if (!isElementPresent(s))
			throw new SkipException("No plan status ditribution chart");
		assertTrue(click(s));
		assertTrue(waitForElementInvisible(pleaseWait));
		return this;
	}

	public boolean planStatusReportPopUp() {
		doWait(3000);
		navigateToTab(1);
		String xpath = "//div[@itemid='TEXT_85c4a531']";
		return isElementPresent(xpath);
	}

	public boolean isSshowChart() {
		doWait(2000);
		navigateToTab(0);
		String xpath = "//label[contains(text(),'Show Chart')]";
		if (!isElementPresent(xpath))
			throw new SkipException("Charts not present for the client");
		return isElementVisible(xpath);
	}

	public HRSoftPage clickShowChart() {
		String xpath = "//label[contains(text(),'Show Chart')]";
		assertTrue(click(xpath));
		return this;
	}

	public HRSoftPage hoverOnComp() {
		if (findElement("[itemid='GRID_LAYOUT_COLUMN_280461285985933']").isDisplayed())
			assertTrue(click("//div[@itemid='BUTTON_41306125433864']//button"));
		String comp = "//button//i[contains(@class,'tcc_product_compensation')]";
		doWait(3000);
		hover(comp);
		return this;
	}

	public HRSoftPage closeBurgermenu() {
		if (findElement("[itemid='GRID_LAYOUT_COLUMN_280461285985933']").isDisplayed())
			assertTrue(click("//div[@itemid='BUTTON_41306125433864']//button"));
		return this;
	}

	public HRSoftPage hoverOnRewards() {
		if (findElement("[itemid='GRID_LAYOUT_COLUMN_280461285985933']").isDisplayed())
			assertTrue(click("//div[@itemid='BUTTON_41306125433864']//button"));
		doWait(3000);
		String managerView = "//button//i[contains(@class,'tcc_product_rewards')]";
		if (!isElementPresent(managerView))
			throw new SkipException("Manager view is not present for he plan using");
		assertTrue(click(managerView));
		return this;
	}

	public HRSoftPage clickClearFilters() {
		assertTrue(click("//span[text()='Clear Filters']"));
		return this;
	}

	public HRSoftPage clickPdf() {
		assertTrue(click("//span[text()='pdf']"));
		doWait(3000);
		// navigateToTab (1);
		return this;
	}

	public HRSoftPage clickExcel() {
		assertTrue(click("//span[text()='Excel ']"));
		doWait(2000);
		return this;
	}

	public String printSettingPagePopUp() {
		String css = "#container";
		return css;
	}

	public HRSoftPage clickPrint() {
		assertTrue(click("//span[text()='print']/parent::button"));
		return this;
	}

	public HRSoftPage saveAsPdf() { // issue with web elements in diff browsers
		assertTrue(click("//*[contains(text(),'Brother DCP-L2540DW series')]"));
		assertTrue(click("//span[@class='c0193']//*[text()='Save as PDF']"));
		assertTrue(click("//button[@class='c01123 c01154 c01124']"));

		return this;
	}

	public ReviewingPage openOverviewScreen() {
		hoverOnComp();
		String overview = "//ul[@class='flyout-list']//li[@flyout-itemid='plan_overview']";
		assertTrue(click(overview));
		hasPageLoaded();
		return (ReviewingPage) navigateToPage(ReviewingPage.class);

	}

	public PlanOverviewPage openCompensationPlanOverviewPage(int id, String planName) {
		hoverOnComp();
		String planOverView = format("//ul[@class='flyout-list']//li[@planid='%s'][text()='%s']", id, planName);
		assertTrue(click(planOverView));
		hasPageLoaded();
		info("Navigated to plan over view page as: " + planName);
		return new PlanOverviewPage();
	}

	public ManagerViewPage openManagerView() {
		hoverOnRewards();
		//assertTrue(click("(//li[text()='Manager View'])[1]"));
		hasPageLoaded();
		pass("Opened manager view page");
		return new ManagerViewPage();
	}

	public PlanningPage openPlanningPageFor(int id, String plannerName) {
		switchToDefaultContent();
		hoverOnComp();
		String comp = format("//ul[@class='flyout-compplan']//li[@planid='%s'][contains(text(),'PLAN for %s')]", id,
				plannerName);
		System.out.println("planner : " + comp);
		assertTrue(click(comp));
		if (!waitForElementInvisible(comp))
			clickUsingJs(findElement(locateBy(comp)));
		else {
		}
		info("Clicked on link to navigate to Comp planning page for " + plannerName);
		doWait(1000);
		return (PlanningPage) navigateToPage(PlanningPage.class);
	}

	public PlanningPage openPlanningPageForRestricted(int id, String plannerName) {
		switchToDefaultContent();
		doWait(5000);
		hoverOnComp();
		String comp = format("//ul[@class='flyout-compplan']//li[@planid='%s'][contains(text(),'%s')]", id,
				plannerName);
		assertTrue(click(comp));
		info("Clicked on link to navigate to Comp planning page for " + plannerName);
		doWait(1000);
		return (PlanningPage) navigateToPage(PlanningPage.class);
	}

	public PlanningPage openPlanRecommendations(int id) {
		switchToDefaultContent();
		hoverOnComp();
		String recom = format(
				"//ul[@class='flyout-compplan']//li[@planid='%s'][contains(text(),'PLAN Recommendations')]", id);
		assertTrue(click(recom));
		pass("Navigated to Comp planning page for the corresponding proxy user");
		return (PlanningPage) navigateToPage(PlanningPage.class);
	}

	public ReviewingPage openReviewRecommendations(int id) {
		switchToDefaultContent();
		hoverOnComp();
		String recom = format(
				"//ul[@class='flyout-compplan']//li[@planid='%s'][contains(text(),'REVIEW Recommendations')]", id);
		assertTrue(click(recom));
		pass("Navigated to Comp Reviewing page for the corresponding proxy user");
		return (ReviewingPage) navigateToPage(ReviewingPage.class);
	}

	public boolean openPlanningScreenForGeneric(int id, String groupId) {
		switchToDefaultContent();
		hoverOnComp();
		boolean flag = false;
		String comp = format("//li[@planid='%s'][contains(@flyout-itemid,'/planning/%s/%s?')]", id, id, groupId);
		if (isElementVisible(comp)) {
			assertTrue(click(comp));
			hasPageLoaded();
			flag = true;
		}

		return flag;
	}

	public ReviewingPage openReviewScreen(int id, String planName) {
		hoverOnComp();
		String comp = format("//ul[@class='flyout-compplan']//li[@planid='%s'][text()='REVIEW for %s']", id, planName);
		assertTrue(click(comp));
		hasPageLoaded();
		return (ReviewingPage) navigateToPage(ReviewingPage.class);

	}

	public boolean openReviewScreenForGeneric(int id, String groupId) {
		hoverOnComp();
		boolean flag = false;
		String comp = format("//li[@planid='%s'][contains(@flyout-itemid,'/rs/reviewing/summary/%s/%s/%s?')]", id, id,
				groupId, groupId);
		if (isElementVisible(comp)) {
			assertTrue(click(comp));
			hasPageLoaded();
			flag = true;
		}
		return flag;
	}

	public HRSoftPage cancelProxy() {
		switchToDefaultContent();
		assertTrue(click(settingsBtn));
		assertTrue(click("//span[text()='Cancel Proxy']"));
		waitForElementInvisible("//div[@class='progress_circle']");
		return this;
	}

	public boolean isElementPresentInColumnOfGrid(String columnlabel, String rowvalue, String itemid) {
		boolean isElementFound = false;
		int maxOffset = 100; // maximum offset value that will keep the element in bounds
		int currentOffset = 0; // current offset value
		int scrollcount = 0;
		String index = findElement("//div[@itemid='" + itemid + "']//div[text()='" + columnlabel + "']")
				.getAttribute("column");
		System.out.println("Index : " + index);
		System.out.println("Xpath for Index : " + "//div[@itemid='" + itemid + "']//div[text()='" + columnlabel + "']");
		while (!isElementFound) {
			try {
				// Find the element you're looking for
				System.out.println("Checking Element");
				WebElement element = findElement(
						"//div[@itemid='" + itemid + "']//div[@class='webix_ss_body']//div[@column=" + index
								+ "]//div[contains(text(),'" + rowvalue + "')]");
				System.out.println(element.getText());
				isElementFound = true;
				System.out.println("Found Element");
			} catch (NoSuchElementException e) {
				// If the element is not found, scroll down by a certain amount
				WebElement slider = findElement("//div[@itemid='" + itemid + "']//div[@aria-orientation='vertical']");
				int sliderWidth = slider.getSize().getWidth();
				int newOffset = Math.min(currentOffset + maxOffset, sliderWidth - 1); // calculate
																						// the new
																						// offset
																						// value
				Actions action = new Actions(getDriver());
				action.clickAndHold(slider).moveByOffset(newOffset, 0).release().perform();
				currentOffset = newOffset; // update the current offset value
				isElementFound = false;
				scrollcount++;
				if (scrollcount > 100) {
					return isElementFound;
				}
				System.out.println("No of Scroll : " + scrollcount);
			}
		}
		return isElementFound;
	}

	public boolean isCloudAdminPresent() {
		return isElementPresent(cloudAdmin);
	}

	public boolean isLoginFailedAsProxy() {
		return isElementVisible("//div[text()='Operation Failed']");
	}

	public HRSoftPage clickLoginFailedOK() {
		assertTrue(click("//button[text()='OK']"));
		return this;
	}

	public CloudAdminPage clickCloudAdmin() {
		switchToDefaultContent();
		assertTrue(click(cloudAdmin));
		hasPageLoaded();
		return new CloudAdminPage();
	}

	public HRSoftPage clickSetupFilters() {
		switchToDefaultContent();
		assertTrue(click(clickSetupFilters));
		hasPageLoaded();
		waitForElementInvisible(loadingText);
		switchToFrame("//iframe[contains(@class,'tcc-admin-popup')]");
		switchToFrame("//iframe[@id='dialogFrame']");
		return this;
	}

	public DataViewPage clickDataViewExplorerPage() {
		if (findElement("[itemid='GRID_LAYOUT_COLUMN_280461285985933']").isDisplayed())
			assertTrue(click("//div[@itemid='BUTTON_41306125433864']//button"));
		doWait(3000);
		if (!isElementPresent(dataViewIcon))
			throw new SkipException("DataView is not present for the plan using");
		// hover (dataViewIcon);
		assertTrue(click("//button//i[@class='fal font-icon tcc_product_icon fa-2x tcc_product_dataview']"));
		waitForElementInvisible("span.loadingOverlay");
		return (DataViewPage) navigateToPage(DataViewPage.class);
	}
	
	public CompOverviewPage clickCompOverview() {
		doWait(500);
		hoverOnComp();
		assertTrue(click(compOverview));
		hasPageLoaded();
		return new CompOverviewPage();
	}

	public CompOverviewPage clickManageFilter() {
		doWait(1500);
		hoverOnComp();
		if (!isElementPresent(manageFilter)) {
			throw new org.testng.SkipException("Manage Filter Page is Missing ");
		}
		assertTrue(click(manageFilter));
		hasPageLoaded();
		switchToInnerFrame();
		return new CompOverviewPage();
	}

	public HRSoftPage clickMyTasksDropDown() {
		assertTrue(click("//*[text()='My Tasks']/following::a[@xhref='#'][1]"));
		return this;
	}

	public boolean myTaskSubmitButton() {
		String xpath = format("//a/div[@class='d-flex w-100 justify-content-between']/h6[contains(text(),'%s')]",
				Constants.compPlanName);
		return isElementVisible(xpath);
	}

	public HRSoftPage clickCloneFeedButton() {
		assertTrue(click("//div[@itemid='BUTTON_138216617040042']//button"));
		return this;
	}

	public HRSoftPage clickMyTasksSubmitButton() {
		if (!isElementPresent(myTaskSubmitButton))
			throw new SkipException("No tasks precent in the home page");
		assertTrue(click(myTaskSubmitButton));
		assertTrue(waitForElementInvisible(pleaseWait));
		assertTrue(hasPageLoaded());
		return this;
	}

	private Boolean hasDropDown(String listName) {
		List<WebElement> webElements = waitForElements(format(dropDownLabel, listName));
		return !webElements.isEmpty() && webElements.get(0).isDisplayed();
	}

	public boolean openListBox(String name) {
		if (!hasDropDown(name))
			return true;
		return click(format(namedDropDown, name));
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

	// scroll slider for any Widget (Vivek Pandey)

	public ConfigureAndManageAttributes clickAndMoveSlider() {
		doWait(2000);
		String xpath = "//div[@itemid='attributes_grid']//div[@class='webix_ss_hscroll webix_vscroll_x' and @aria-orientation='horizontal']";
		WebElement slider = findElement(xpath);
		Actions action = new Actions(getDriver());
		action.clickAndHold(slider).moveByOffset(130, 0).release().perform();
		return new ConfigureAndManageAttributes();
	}

	public HRSoftPage clickConfirmInPopup() {
		String xpath = "//div[@class='modal-dialog']//button[text()='Confirm']";
		click(xpath);
		doWait(500);
		return this;
	}

	// public boolean selectRandomListItemFromOpenWebixList () {
	// List <WebElement> webixPopupBoxForList = findElements (webixListBox);
	// for (WebElement element : webixPopupBoxForList) {
	// if (element.isDisplayed ()) {
	// List <WebElement> listItems = element.findElements (By.xpath (webixListItem))
	// .stream ()
	// .filter (e -> !"TCC_NULL".equalsIgnoreCase (e.getAttribute ("webix_l_id")))
	// .collect (Collectors.toList ());
	// if (listItems.size () > 0) {
	// int rand = new Random ().nextInt (listItems.size ());
	// return click (listItems.get (rand));
	// }
	// }
	// }
	// return false;
	// }
	//
	// public boolean selectGivenListItemFromOpenWebixList (String listItemName) {
	// List <WebElement> webixPopupBoxForList = findElements (webixListBox);
	// for (WebElement element : webixPopupBoxForList) {
	// if (element.isDisplayed ()) {
	// List <WebElement> listItems = element.findElements (By.xpath (format
	// (webixListItemByValue,
	// listItemName)));
	// if (listItems.size () > 0) {
	// int rand = new Random ().nextInt (listItems.size ());
	// return click (listItems.get (rand));
	// }
	// }
	// }
	// return false;
	// }

	// public boolean selectGivenWebixItemIdFromOpenWebixList (String webixId) {
	// List <WebElement> webixPopupBoxForList = findElements (webixListBox);
	// for (WebElement element : webixPopupBoxForList) {
	// if (element.isDisplayed ()) {
	// List <WebElement> listItems = element.findElements (By.xpath (format
	// (webixListItemByWebixId,
	// webixId)));
	// if (listItems.size () > 0) {
	// int rand = new Random ().nextInt (listItems.size ());
	// return click (listItems.get (rand));
	// }
	// }
	// }
	// return false;
	// }

	public HRSoftPage closePopup() {
		assertTrue(click("//button[@class='bootbox-close-button close']"));
		return this;
	}

	public DistributeFundPage openDistributedFundLink(int planId, String distFundLink) {
		HRSoftPage hrSoftPage = new HRSoftPage();
		switchToDefaultContent();
		hoverOnComp();
		String distFunds = format(distFundLinkxpath, planId, distFundLink);
		assertTrue(click(distFunds));
		hasPageLoaded();
		switchToInnerFrame();
		return (DistributeFundPage) navigateToPage2(DistributeFundPage.class, hrSoftPage);
	}

	public void cancelProxyViaIcon() {
		switchToDefaultContent();
		assertTrue(click(cancelProxyWithIcon));
		waitForElementInvisible(cancellingProxy);
		assertTrue(hasPageLoaded());
	}

	public Boolean isDistributedFundVisible(int planId, String distFundLink) {
		switchToDefaultContent();
		hoverOnComp();
		String distFunds = format(distFundLinkxpath, planId, distFundLink);
		return isElementPresent(distFunds);
	}

	public void openPlan(int plan) {
		// String planName = format ("//button[@planname='%d']", plan);
		// assertTrue (click (planName));
		// waitForElementVisible ("//div[contains(@class,'module_title')]");
		driver.get(
				"https://tcc2200-sqa.cloud.hrsoft.com/content/portal/wizard/startWizard.htm?wiztype=COMPENSATION_PLAN_CONFIG&mode=manage");
		hasPageLoaded();
	}

	public String printCompDoc() {
		driver.get(
				"https://tcc2200-sqa.cloud.hrsoft.com/content/portal/main_config/printables/comp_plan_requirement_index.htm?planId=10");
		waitForElementVisible("//span[text()='CONFIGURATION REQUIREMENTS']");
		return driver.getTitle();
	}

	public HRSoftPage hoverOnModelView() {
		if (findElement("[itemid='GRID_LAYOUT_COLUMN_280461285985933']").isDisplayed())
			assertTrue(click("//div[@itemid='BUTTON_41306125433864']//button"));
		doWait(3000);
		String modelView = "button i[class*='tcc_product_calibration']";
		if (!isElementPresent(modelView))
			throw new SkipException("Model view is not present for this plan");
		hover(modelView);
		return this;
	}

	public ModelViewPage clickModelView() {
		hoverOnModelView();
		assertTrue(click("button i[class*='tcc_product_calibration']"));
		// waitUntilVisible("(//div[contains(@class,'TCCREPEATERBODY ')])[3]");
		return new ModelViewPage();
	}

	public DataViewSettingsPage clickDataViewSettingPage() {
		if (findElement("[itemid='GRID_LAYOUT_COLUMN_280461285985933']").isDisplayed())
			assertTrue(click("//div[@itemid='BUTTON_41306125433864']//button"));
		doWait(3000);
		if (!isElementPresent(dataViewIcon))
			throw new SkipException("DataView is not present for the plan using");
		 hover (dataViewIcon);
		assertTrue(click("//li[@flyout-itemid='TEXT_2746363840134260']"));
		waitForElementInvisible("span.loadingOverlay");
		return (DataViewSettingsPage) navigateToPage(DataViewSettingsPage.class);
	}

}
