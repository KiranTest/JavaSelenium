package com.hrsoft.gui.compensation;

import static java.lang.String.format;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.reports.ExtentLogger;
import com.hrsoft.utils.seleniumfy.BasePage;

/**
 * @author Mashood khan <a href="mailto:mashood.khan@hrsoft.com</a>
 */

public class CompOverviewPage extends BasePage {

	private PlanOverviewPage overviewPage = new PlanOverviewPage();
	private HRSoftPage hrsoftPage = new HRSoftPage();

	/* Plan OverView Page Elements */

	private String clickAnyMessage = "div.panel-group:last-child[portletid='comp_messages_announcements_portlet_001'] ul li:first-child a";
	private String clickanyTaskLink = "//div[@portletid='comp_tasks_portlet_001']/ul/li[1]/a";
	private String closeAnnouncementpopUp = "//div[text()='Messages and Announcements']";
	private String allPlans = "//div[@class='panel-heading' and @title='Compensation Tasks']/h4/a";
	private String pageTitleAfterClickTask = "//div[@id='pageTitle']/span[contains(text(), 'Plan')]";
	private String expandCollapseLink = "(//div[@id='comp_tasks_portlet_001_body']//h4)[1]";
	private String announcementPopup = "//div[@id='genericPopoverDiv']//div[@id='popoverTitle']";

	/* Manage Filter Elements */

	private String clickAnyCopyBtn = "//table/tbody/tr[1]//td[1]//button";
	private String copyFilterNameInput = "//input[@id='filterName']";
	private String saveBtn = "//input[@elementid='0' and @value='Save']";
	private String tblRows = "//table/tbody/tr";
	private String deleteSelected = "//input[@value='Delete Selected']";

	/* Add New Filter */

	private String addNewFilterBtn = "//input[@value='Add new Filter']";
	private String enterFilterNameInput = "//input[@id='filterName']";
	private String enterFilterDescInput = "//td/input[@id='filterDesc']";
	private String selectParameter = "td[typecode='WS_Filter_Parameter'] select option";
	private String selectOperator = "td[typecode='WS_Filter_Operator'] select option";
	private String lookupValue = "//input[@wsmeta='LookupText']";
	private String closeFilterPopUp = "//button[@title='Close']";
	private String clickSearch = "//input[@value='Search']";
	private String searchResultsFilterTbl = "//table[@id='MatrixTable']";
	private String filtersInSearchResults = "//table[@id='MatrixTable']/tbody/tr";
	private String closeDeleteFilterIcon = "span.ui-button-icon-primary";

	/* Plan OverView Page Methods */

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions
				.visibilityOfElementLocated(locateBy("(//div[@portletid='comp_tasks_portlet_001'])[1]"));
	}

	public CompOverviewPage expandCollapseCompTasksLink() {
		switchToInnerFrame();
		assertTrue(click(expandCollapseLink));
		doWait(2000);
		return this;
	}

	public CompOverviewPage clickMinimize() {
		switchToInnerFrame();
		assertTrue(click("a[title='Minimize']"));
		return this;
	}

	public CompOverviewPage clickMaximize() {
		assertTrue(click("a[title='Maximize']"));
		return this;
	}

	public CompOverviewPage clickOnAnyTaskLink() {
		List<WebElement> tasks = findElements(clickanyTaskLink);
		if (!tasks.isEmpty()) {
			tasks.get(0).click();
			hasPageLoaded();
		} else {
			ExtentLogger.info("Failed!Element not Found");
		}
		return this;
	}

	public Boolean pageTitle() {
		return isElementPresent(pageTitleAfterClickTask);
	}

	public CompOverviewPage clickCompAnnouncementLink() {
		switchToInnerFrame();
		String compAnnouncement = "(//h4[@class='panel-title']//a[contains(text(),'Announcements')]/../../../../div//a)[2]";
		if (!isElementPresent(compAnnouncement))
			throw new org.testng.SkipException("No Announcement Available!");
		assertTrue(click(compAnnouncement));
		return this;
	}

	public CompOverviewPage checkPopupPresent() {
		doWait(2000);
		assertTrue(isElementPresent(announcementPopup));
		return this;
	}

	public CompOverviewPage closePopup() {
		switchToInnerFrame();
		assertTrue(click(closeAnnouncementpopUp));
		return this;
	}

	public CompOverviewPage clickCompMessageLink() {
		doWait(500);
		switchToInnerFrame();
		assertTrue(click(clickAnyMessage));
		doWait(3000);
		return this;
	}

	public CompOverviewPage numberOfItemsForEachPlan() {
		List<WebElement> noOfPlans = findElements(allPlans);
		int size = noOfPlans.size();
		System.out.println("Total Plans : " + size);
		for (WebElement plan : noOfPlans) {
			String planName = plan.getText();
			System.out.println("Plan Name :" + planName);
		}
		return this;
	}
	/* Manage Filter Page Methods */

	public CompOverviewPage saveCopiedItem(String copiedName) {
		switchToInnerFrame();
		assertTrue(click(clickAnyCopyBtn));
		overviewPage.switchToOuterFrame();
		clear(copyFilterNameInput);
		enterFilterName(copiedName);
		assertTrue(click(saveBtn));
		acceptAlertWithoutException();
		hasPageLoaded();
		doWait(3000);
		return this;
	}

	public CompOverviewPage enterFilterName(String name) {
		setText(copyFilterNameInput, name);
		return this;
	}

	public CompOverviewPage deleteCopiedItem(String copiedName) {
		overviewPage.switchToInnerFrame();
		List<WebElement> totalTableRows = findElements(tblRows);
		int size = totalTableRows.size();
		System.out.println("No of Rows : " + size);
		for (WebElement trow : totalTableRows) {
			String filterName = trow.findElement(By.xpath("td[3]/a[@data-type-code='WS_Filter']")).getText();
			if (filterName.trim().equals(copiedName)) {
				trow.findElement(By.xpath("td[2]")).click();
				acceptAlertWithoutException();
				acceptAlertWithoutException();
				hasPageLoaded();
				break;
			}
		}
		return this;
	}

	public CompOverviewPage addNewFilter(String name) {
		overviewPage.switchToInnerFrame();
		assertTrue(click(addNewFilterBtn));
		overviewPage.switchToOuterFrame();
		setText(enterFilterNameInput, name);
		doWait(2000);
//		String x = "(//select[contains(@class,'statusSelectEmp')])[1]";
//		clickAndSelectValue(x,"=");
//		String y = "(//select[contains(@class,'statusSelectEmp')])[2]";
//		clickAndSelectValue(y,"Current Job Code");
//		doWait(1000);
		assertTrue(click("//td[@typecode='WS_Value']/img"));
		doWait(2000);
		clickAndSelectIndex("//select[@id='FilterParamAvailableValues']",1);
		assertTrue(click("//input[@actiontype='r']"));
		doWait(1000);
		assertTrue(click("//input[@value='Update']"));
//		List<WebElement> parameter = findElements(selectParameter);
//		List<WebElement> operator = findElements(selectOperator);
//		for (WebElement p : parameter) {
//			if (p.getText().trim().equals(param)) {
//				p.click();
//				break;
//			}
//		}
//		for (WebElement o : operator) {
//			if (o.getText().trim().equals(oper)) {
//				o.click();
//				break;
//			}
//		}
		doWait(2000);
//		setText(lookupValue, lookupVal);
		assertTrue(click(saveBtn));
		acceptAlertWithoutException();
		hasPageLoaded();
		doWait(3000);
		return this;
	}

	public CompOverviewPage clickShareButton(String name) {
		overviewPage.switchToInnerFrame();
		List<WebElement> totalTableRows = findElements(tblRows);
		for (WebElement trow : totalTableRows) {
			String filterName = trow.findElement(By.xpath("td/a[@data-type-code='WS_Filter']")).getText();
			System.out.println("Names : " + filterName);
			if (filterName.contains(name)) {
				trow.findElement(By.xpath("td/button[@title='Share']")).click();
				acceptAlertWithoutException();
				hasPageLoaded();
				break;
			}
		}
		doWait(3000);
		return this;
	}

	public String AssertType(String name) {
		overviewPage.switchToInnerFrame();
		String actualType = "";
		List<WebElement> tblRowsForType = findElements(tblRows);
		for (WebElement trow : tblRowsForType) {
			String filterName = trow.findElement(By.xpath("td/a[@data-type-code='WS_Filter']")).getText();
			if (filterName.contains(name)) {
				actualType = trow.findElement(By.xpath("td[@typecode='WS_Filter_Type']/span")).getText().trim();
				break;
			}
		}
		hasPageLoaded();
		doWait(3000);
		System.out.println(actualType);
		return actualType;
	}

	public CompOverviewPage clickAddedLink(String name) {
		switchToInnerFrame();
		waitForElementVisible(tblRows);
		List<WebElement> tableRows = findElements(tblRows);
		for (WebElement trow : tableRows) {
			String addedfilterName = trow.findElement(By.xpath("td[3]/a[@data-type-code='WS_Filter']")).getText()
					.trim();
			if (addedfilterName.trim().contains(name)) {
				String el = trow.findElement(By.xpath("td[3]/a")).getText();
				trow.findElement(By.xpath("td[3]/a")).click();
				System.out.println("Clicked Filter :" + el);
				hasPageLoaded();
				overviewPage.switchToOuterFrame();
				assertTrue(click(closeFilterPopUp));
				hasPageLoaded();
				break;
			}
		}
		return this;
	}

	/* Reverting back changes* : deleting newly added filter */

	public CompOverviewPage deleteAddedFilter(String name) {
		hrsoftPage.clickCloudAdmin();
		hrsoftPage.clickSetupFilters();
		assertTrue(click(clickSearch));
		switchToDefaultContent();
		switchToFrame("//iframe[contains(@class,'tcc-admin-popup')]");
		switchToFrame("//iframe[@id='dialogFrame']");
		waitForElementVisible(searchResultsFilterTbl);
		List<WebElement> tblRowsToGetSFilterNamesFromSearchResults = findElements(filtersInSearchResults);
		for (WebElement trow : tblRowsToGetSFilterNamesFromSearchResults) {
			String filterName = trow.findElement(By.xpath("td[2]")).getText();
			if (filterName.contains(name)) {
				System.out.println("Delete Filter Name : " + name);
				WebElement selectFilter = trow.findElement(By.xpath("td/input[@name='selectionButton']"));
				if (!selectFilter.isSelected()) {
					selectFilter.click();
				}
				doWait(1000);
				assertTrue(click(deleteSelected));
				doWait(3000);
				acceptAlertWithoutException();
				hasPageLoaded();
				switchToDefaultContent();
				assertTrue(click(closeDeleteFilterIcon));
				System.out.println("Deleted filter successfully!");
				break;
			}
		}
		return this;
	}

	public List<WebElement> compTasksLinks() {
		doWait(500);
		switchToInnerFrame();
		List<WebElement> x = findElements("div[id='comp_tasks_portlet_001_body'] li a");
		return x;
	}

	public CompOverviewPage clickTaskLink() {
		assertTrue(click("a[class='message wstmLink dictionaryItem']"));
		waitForElementVisible("div[id='pageTitle']");
		switchToDefaultContent();
		return this;
	}

}
