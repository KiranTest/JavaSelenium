package com.hrsoft.gui.dataview;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static java.lang.String.format;
import static org.testng.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;

import com.hrsoft.constants.Constants;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.cloudadmin.DataManagementPage;

public class DataViewSettingsPage extends HRSoftPage {
	
	private String settingReportSharingTab = "//a[@data=panel-id='Link11637834308472']";
	private String addUsersPoolsButton = "div[itemid='BUTTON_173722619229840'] button";
	private String loadingIcon = "span[class='loadingOverlay']";	
	private String saveAddUsersPoolsButton = "div[itemid='BUTTON_0f14a421'] button";
	private String okButton = ".bootbox.modal_DEFAULT_message.bootbox_SUCCESS button";
	
	private String userDropDown = ".webix_view.webix_window.webix_popup[style*='display: block'] .webix_view.webix_list .webix_list_item";
	
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy("[itemid='NAVIGATION_2746875020394292']"));
	}
			
	public DataViewSettingsPage clickReportSharingTab() {
		assertTrue(click(settingReportSharingTab));
		return this;
	}
	
	public DataViewSettingsPage selectCompensationProduct() {
		assertTrue(click("div[itemid='MENU_becd6679'] div.webix_el_box"));
		return this;
	}
	
	public DataViewSettingsPage clickOnFirstReportCheckBox() {
		assertTrue(click("//div[@itemid='GRID_15582415499282']//child::div[@role='gridcell'][1]/input[1]"));
		waitForElementVisible("div[itemid='REPEATER_1374254975160751']");
		return this;
	}
	
	public DataViewSettingsPage checkDeletePermission() {
		String deleteBtn = "div[itemid='REPEATER_1374254975160751'] div.TCCREPEATERROW:first-child div[orig_itemid='GRID_LAYOUT_COLUMN_1607583806188_5'] label";
		WebElement el = findElement(By.cssSelector(deleteBtn));
		assertTrue(click(el));
		return this;
	}
	
	public DataViewSettingsPage clickAddUsersPoolsButton() {
		assertTrue(click(addUsersPoolsButton));		
		return this;
	}
	
	public DataViewSettingsPage clickOKButton() {
		WebElement el = findElement(By.cssSelector(okButton));
		System.out.println (okButton + "=" + el.getSize());
		assertTrue(click(el));
		return this;
	}
	
	public DataViewSettingsPage clickSaveUsersPoolsButton() {
		assertTrue(click(saveAddUsersPoolsButton));		
		return this;
	}
	
	
	
	public DataViewSettingsPage selectUsersFromDropDown(String value) {
		assertTrue(click("[itemid='MENU_1376829984915319']"));
		assertTrue(click(userDropDown));		
		return this;
	}

}
