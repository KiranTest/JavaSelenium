package com.hrsoft.gui.cloudadmin;

import static java.lang.String.format;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.hrsoft.constants.Constants;
import com.hrsoft.db.HrSoftDb;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.gui.dataset.ValidateAll;
import com.hrsoft.gui.dataset.DatasetEditor;
import com.hrsoft.utils.seleniumfy.BasePage;

public class DatasetExplorer extends BasePage {

	private String clientDropdown = "//div[@itemid='client_copy_6716004416558']//input";
	private String productDropdown = "//div[@itemid='MENU_579224975474757']//input[@role='combobox']";
	private String datasetGridId = "GRID_6522871416908";
	private String datasetIcon = "button[data-content='Create New Dataset']";
	private String datasetNameInput = "div[itemid='TEXT_INPUT_1463303125818777'] input";
	private String createdatasetIcon = "//div[@itemid='BUTTON_6434787019185']//button";
	private String createdatasetConfirm = "//div[@itemid='BUTTON_1463757666343450']//span[text()='Create']/..";
	private String validateAllButton = "//div[@itemid='BUTTON_693f2bc2']//button";
	// private String auditChangesButton = "//div[@itemid='" + datasetGridId +
	// "']//div[contains(@class,'btn-group-container')]//button[text()='&nbsp;Audit
	// Changes']";
	private String pleaseWait = "//h5[@class='modal-title' and text()='Please Wait...']";
	private String filterIconForDSName = "//div[@Itemid='" + datasetGridId
			+ "']//div[@column=1]//span[@class='webix_excel_filter webix_icon wxi-filter']";
	/* DataView Report */
	private String saveAsReportBtn = "//button//span[text()='Save As Report']";
	private String reportInput = "//div[@itemid='TEXT_INPUT_10132273422594585']//input";
	private String confirmBtn = "//div[@itemid='BUTTON_10133020055660628']//button";
	private String loadingIcon = "//span[@class='loadingOverlay']";
	private String fieldsButton = "//div[@data-cat-text='Fields']/h3/i";
	private String expandLeftMenu = "div[data-target='widget-hierarchy-widget']";
	private String runBtn = "//div[@id='previewParamsModal' and contains(@style,'display: block;')]//button[text()='RUN']";
	private String clearFiltersBtn = "div[itemid='BUTTON_1809148031750408'] button";

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy(""));
	}

	public DatasetExplorer selectClient(String client) {
		assertTrue(click(clientDropdown));
		assertTrue(click(String.format(
				"//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@role='option' and text()='%s']",
				client)));
		return this;
	}

	public DatasetExplorer verifySelectedClient(String custId) {
		waitForElement(clientDropdown);
		assertTrue(getAttribute(clientDropdown, "value").equals(custId));
		return this;
	}

	// private ManageDataObjects dataobject = new ManageDataObjects();
	// private DataViewPage dataview = new DataViewPage();

	public DatasetExplorer clickDatasetPlusIcon() {
		assertTrue(click(datasetIcon));
		return this;
	}

	public DatasetExplorer verifyEditDataset() {
		List<String> datasets = new ArrayList<String>();
		datasets = getColumnValuesOfGrid(1, datasetGridId);
		assertTrue(datasets.size() > 0);

		Random rand = new Random();
		int testIndex = rand.nextInt(datasets.size() + 1);
		String dsName = datasets.get(testIndex);
		clickInlineAction(datasetGridId, testIndex + 1, " Edit");
		doWait(5000);
		navigateToTab(1);
		DatasetEditor editor = new DatasetEditor();
		assertTrue(editor.confirmName(dsName));
		return this;
	}

	public DatasetExplorer validateCreateDsAndCleanup() {
		String testDsName = "Automated_Test_Ds_Delme";
		DatasetEditor datasetEditor = createDataset(testDsName);
		assertTrue(datasetEditor.confirmName(testDsName));
		closeCurrentTab();
		deleteDataset(testDsName);
		return this;
	}

	public DatasetExplorer vaidateMultiProductCreate(List<String> products) {
		selectProducts(products);
		assertTrue(click(createdatasetIcon));
		assertTrue(findElement("//div[contains(@class,'text-danger')]") != null);
		clickPopupBtn("Ok");
		return this;
	}

	public DatasetEditor createDataset(String dsName) {
		assertTrue(click(createdatasetIcon));
		enterDatasetName(dsName);
		selectSensitivity("PUBLIC WITHIN ORGANIZATION-hrsofti");
		assertTrue(click(createdatasetConfirm));
		doWait(5000);
		navigateToTab(1);
		return new DatasetEditor();
	}

	public DatasetEditor editDataset(String dsName) {
		searchDataset(dsName);
		List<String> datasets = new ArrayList<String>();
		datasets = getColumnValuesOfGrid(1, datasetGridId);
		int testIndex = datasets.indexOf(dsName);
		clickInlineAction(datasetGridId, testIndex + 1, " Edit");
		doWait(1000);
		navigateToTab(1);
		return new DatasetEditor();
	}

	public DatasetExplorer deleteDataset(String dsName) {
		searchDataset(dsName);
		doWait(1000);
		List<String> datasets = new ArrayList<String>();
		datasets = getColumnValuesOfGrid(1, datasetGridId);
		int testIndex = datasets.indexOf(dsName);

		clickInlineAction(datasetGridId, testIndex + 1, " Delete");
		clickPopupBtn("Confirm");
		return this;
	}

	public DatasetExplorer enterDatasetName(String dsName) {
		setText(datasetNameInput, dsName);
		return this;
	}

	public DatasetExplorer selectSensitivity(String sensitvity) {
		doWait(2000);
		assertTrue(click("div[itemid='MENU_1463336046028434'] input"));
		doWait(2000);
		assertTrue(click(String.format(
				"//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@role='option'and text()='%s']",
				sensitvity)));
		return this;
	}

	public ValidateAll clickValidateAll() {
		assertTrue(click(validateAllButton));
		clickPopupBtn("Confirm");
		navigateToTab(1);
		assertTrue(hasPageLoaded());
		return new ValidateAll();
	}

	public DatasetExplorer deleteDataSet(String datasetName) {
		searchDataset(datasetName);
		List<WebElement> li = findElements("//a[@title=' Delete']");
		for (int i = 0; i <= li.size(); i++) {
			li.forEach(del -> {
				del.click();
				clickPopupBtn("Confirm");
			});
		}
		return this;
	}

	public DatasetExplorer reportDesigner(String reportName, String datasetName, String product,
			ArrayList<String> columnNames) { /* Without Autoconfigure column select */
		String el = String.format(
				"//p[text()='Report Data Section']/../../following-sibling::li//div[@class='designer-nested-item']//p[contains(text(),'%s')]",
				reportName);
		assertTrue(click("//span[@title='Widget Hierarchy']//i"));
		assertTrue(rightClick(el));
		assertTrue(click(
				el + "/..//a[@class='editWidgetProperties widget-inline-action']//i[@class='fad fa-edit fa-fw']"));
		waitForElementInvisible("//*[text()='Loading properties...']");
		assertTrue(click("//h3[@class='cat-header' and contains(text(),'Map Dataset')]//i"));
		assertTrue(click("//label[@title='Select dataset to be mapped']/..//button[@title='Select Dataset']"));
		waitUntilVisible("//div[@itemid='GRID_LAYOUT_COLUMN_1594889986581_ab424719']");
		waitForElementInvisible("//*[text()='Loading...']");
		switchToDatasetIFrame();
		assertTrue(click(clearFiltersBtn));
		selectProduct("HRsoft Cloud");
		searchDataset(datasetName);
		assertTrue(click(String.format("//div[@aria-rowindex='1' and text()='%s']", datasetName)));
		switchToDefaultContent();
		assertTrue(click("//button[@class='btn btn-primary' and text()='Select Dataset']"));
		if (isElementPresent("//button[@class='btn btn-warning bootbox-accept']"))
			assertTrue(click("//button[@class='btn btn-warning bootbox-accept']"));
		/* With auto configure */
//		assertTrue(setText("input[id='property_search']", "Auto-configure"));
//		String radioBtn = "//label[text()=' Auto-configure']/..//div[@class='custom-control custom-switch']";
//		if (!findElement(radioBtn).isSelected())
//			assertTrue(click(radioBtn));

		/* Custom column select */
		expandFieldsButton();
		selectFields(columnNames);
		assertTrue(click("//button[@data-target='tcc-designer-navBar-play-list' and @title='Run']"));
		assertTrue(click("//li[@id='btn-preview']//p"));
		if (!findElement("//label[@for='runWithoutParameters']/..//input").isEnabled())
			assertTrue(click("//label[@for='runWithoutParameters']/..//input"));
		clickRun();
		navigateToTab(3);
		assertTrue(waitUntilVisible("//div[@itemid='GRID_LAYOUT_COLUMN_8310079516754596']"));
		doWait(5000);
		assertTrue(click("//div[@itemid='BUTTON_7044203639573549']//button"));
		assertTrue(click("//button//i[contains(text(),'Choose Print Options')]"));
		assertTrue(click("//span[contains(text(),'export grid as pdf')]"));
		doWait(5000);
		return this;
	}

	public DatasetExplorer reportDesigner(String reportName, String datasetName,
			String product) {/* With Autoconfigure column select */
		String el = String.format(
				"//p[text()='Report Data Section']/../../following-sibling::li//div[@class='designer-nested-item']//p[contains(text(),'%s')]",
				reportName);
		assertTrue(click("//span[@title='Widget Hierarchy']//i"));
		assertTrue(rightClick(el));
		assertTrue(click(
				el + "/..//a[@class='editWidgetProperties widget-inline-action']//i[@class='fad fa-edit fa-fw']"));
		waitForElementInvisible("//*[text()='Loading properties...']");
		assertTrue(click("//h3[@class='cat-header' and contains(text(),'Map Dataset')]//i"));
		assertTrue(click("//label[@title='Select dataset to be mapped']/..//button[@title='Select Dataset']"));
		waitUntilVisible("//div[@itemid='GRID_LAYOUT_COLUMN_1594889986581_ab424719']");
		waitForElementInvisible("//*[text()='Loading...']");
		switchToDatasetIFrame();
		if (isElementVisible(clearFiltersBtn))
			assertTrue(click(clearFiltersBtn));
		selectProduct(product);
		searchDataset(datasetName);
		assertTrue(click(String.format("//div[@aria-rowindex='1' and text()='%s']", datasetName)));
		switchToDefaultContent();
		assertTrue(click("//button[@class='btn btn-primary' and text()='Select Dataset']"));
		if (isElementPresent("//button[@class='btn btn-warning bootbox-accept']"))
			assertTrue(click("//button[@class='btn btn-warning bootbox-accept']"));
		/* With auto configure */
		assertTrue(setText("input[id='property_search']", "Auto-configure"));
		String radioBtn = "//label[text()=' Auto-configure']/..//div[@class='custom-control custom-switch']";
		if (!findElement(radioBtn).isSelected())
			assertTrue(click(radioBtn));
		/* Enable show in menu */
		enableShowInMenuOption(reportName);
		assertTrue(click("//button[@data-target='tcc-designer-navBar-play-list' and @title='Run']"));
		assertTrue(click("//li[@id='btn-preview']//p"));
		if (!findElement("//label[@for='runWithoutParameters']/..//input").isEnabled())
			assertTrue(click("//label[@for='runWithoutParameters']/..//input"));
		clickRun();
		navigateToTab(3);
		assertTrue(waitUntilVisible("//div[@itemid='GRID_LAYOUT_COLUMN_8310079516754596']"));
		doWait(5000);
		assertTrue(click("//div[@itemid='BUTTON_7044203639573549']//button"));
		assertTrue(click("//button//i[contains(text(),'Choose Print Options')]"));
		assertTrue(click("//span[contains(text(),'export grid as pdf')]"));
		doWait(5000);
		navigateToTab(0);
		return this;
	}

	public DatasetExplorer enableShowInMenuOption(String report) {
		assertTrue(click(expandLeftMenu));
		String el = format("//p[text()='%s']", report);
		assertTrue(rightClick(el));
		doWait(1000);
		assertTrue(click(String.format(
				el + "/..//a[@class='editWidgetProperties widget-inline-action']//i[@class='fad fa-edit fa-fw']",
				report)));
		doWait(2000);
		setText("input[id='property_search']", "show in menu");
		if (!findElement("//label[text()=' Show in Menu']/..//div[@class='custom-control custom-switch']").isSelected())
			assertTrue(click("//label[text()=' Show in Menu']/..//div[@class='custom-control custom-switch']"));
		doWait(2000);
		return this;
	}

	public DatasetExplorer clickRun() {
		assertTrue(click(runBtn));
		return this;
	}

	public DatasetExplorer clearProduct() {
		List<WebElement> products = findElements(
				"//div[@itemid='product_copy_6721701898937']//span[@class='webix_multicombo_delete']");
		while (products.size() > 0) {
			assertTrue(click(products.get(0)));
			doWait(100);
			products = findElements(
					"//div[@itemid='product_copy_6721701898937']//span[@class='webix_multicombo_delete']");
		}
		return this;
	}

	public DatasetExplorer selectProduct(String productName) {
		List<WebElement> webixPopupBoxForList = findElements("//div[contains(@class,'webix_popup')]");
		boolean invisibleFlag = true;
		for (WebElement e : webixPopupBoxForList) {
			if (e.isDisplayed()) {
				invisibleFlag = false;
				break;
			}
		}
		if (invisibleFlag) {
			assertTrue(click(productDropdown));
		}
		assertTrue(click(String.format("//div[text()='%s']", productName)));
		return this;
	}

	public DatasetExplorer selectProducts(List<String> products) {
		clearProduct();
		for (String p : products) {
			selectProduct(p);
		}
		return this;
	}

	public List<String> getProductOptions() {
		return (getDropdownValues("product_copy_6721701898937"));
	}

	public DatasetExplorer verifyProductFilter(HrSoftDb db, String productName) {
		ArrayList<String> products = new ArrayList<String>();
		products.add(productName);
		return verifyProductFilter(db, products);
	}

	public DatasetExplorer verifyProductFilter(HrSoftDb db, List<String> productNames) {
		clearProduct();
		for (String p : productNames) {
			selectProduct(p);
			doWait(100);
		}

		// Get returned DS names and their products
		List<String> returnedDsNames = new LinkedList<>();
		returnedDsNames = getColumnValuesOfGrid(1, datasetGridId);// grid-e7afb319");

		// info(Integer.toString(returnedDsNames.size())+" datasets found in grid.");

		if (returnedDsNames.size() > 0) {
			List<Map<String, Object>> DsProducts;
			DsProducts = db.executeSelect("SELECT DISTINCT NAME " + "FROM (SELECT * FROM ( "
					+ "		SELECT	__overlay_source.[cust_guid], " + "			__overlay_source.[participant_guid], "
					+ "			__overlay_source.[set_id], " + "			__overlay_source.[TCC_INACTIVE], "
					+ "			__overlay_source.[app_id], " + "			__overlay_source.[name], "
					+ "			c.[app_name], " + "			ROW_NUMBER() OVER(PARTITION BY __overlay_source.[set_id] "
					+ "			ORDER BY __participant_overlay_hierarchy.[overlay_priority] ASC) AS __partition_rn "
					+ "		FROM tcc_rad_set AS __overlay_source "
					+ "     INNER JOIN tcc_app_cfg c ON __overlay_source.app_id = c.app_id "
					+ "		INNER JOIN tcc_f_getparticipantoverlayhierarchy((select cust_guid from tcc_cust_cfg where cust_id = '"
					+ Constants.custId + "'), " + "			(select cust_guid from tcc_cust_cfg where cust_id = '"
					+ Constants.custId + "'), null, null) AS __participant_overlay_hierarchy "
					+ "			ON __overlay_source.participant_guid = __participant_overlay_hierarchy.participant_guid "
					+ "		) AS __inheritance_mq "
					+ "	WHERE __partition_rn = 1 AND ISNULL(TCC_INACTIVE,'N') <> 'Y') AS S " + "WHERE APP_NAME IN ("
					+ db.getListAsSqlValues(productNames) + ") AND NAME IN (" + db.getListAsSqlValues(returnedDsNames)
					+ ")" + "GROUP BY [NAME]");

			// info(Integer.toString(DsProducts.size())+" datasets matched the expected
			// product.");

			// Assert no sets shown belong to a different product
			assertEquals(returnedDsNames.size(), DsProducts.size(),
					"SM-228: Mismatch between selected product and the sets presented :");
		}
		return this;
	}

	public void switchToDatasetIFrame() {
		doWait(2000);
		DriverManager.getDriver().switchTo().frame("datasetIframe");
	}

	public DatasetExplorer searchDataset(String dataset) {
		doWait(2000);
		assertTrue(click("//div[@itemid='BUTTON_c8e2ffcb']//i[@class='fas fa-search   font-icon  ']"));
		DriverManager.getDriver().findElement(By.xpath("//div[@itemid='TEXT_INPUT_1e22506d']//input")).sendKeys(dataset,
				Keys.ENTER);
		return this;
	}

//	public DatasetExplorer searchDataset(String dsName) {
//		assertTrue(click(filterIconForDSName));
//		setText("//div[contains(@class,'webix_view webix_control webix_el_text')]/div/input", dsName);
//		return this;
//	}

	/* Create New Report */

	public DatasetExplorer createNewReport(String template) {
		clickReportPlusButton();
		selectReportTemplate(template);
		return this;
	}

	public DatasetExplorer clickReportPlusButton() {
		assertTrue(click("//button[@data-content='Create New Report']"));
		waitForElementVisible("//div[text()='Create Report   ']");
		return this;
	}

	public DatasetExplorer selectReportTemplate(String template) {
		assertTrue(click(String.format("//div[@rendertype='label']//span[text()='Employee Listing']", template)));
		return this;
	}

	public DatasetExplorer saveNewReport(String reportName) {
		assertTrue(click(saveAsReportBtn));
		waitForElementInvisible(pleaseWait);
		clear(reportInput);
		setText(reportInput, reportName);
		doWait(1000);
		selectProductForReport("DATAview");
		assertTrue(click(confirmBtn));
		waitForElementInvisible(pleaseWait);
		doWait(3000);
		navigateToTab(2);
		waitForElementVisible(format("//p[contains(text(),'%s')]", reportName));
		return this;
	}

	public DatasetExplorer selectProductForReport(String product) {
		clear("div[itemid='MENU_5e021b09'] input");
		selectOptionFromDropdownMenu("MENU_5e021b09", product);
		return this;
	}

	public DatasetExplorer selectCreatedByMeOption() {
		selectOptionFromDropdownMenu("MENU_86466648522318", "      -- Created by Me");
		waitForElementInvisible(loadingIcon);
		return this;
	}

	public DatasetExplorer clickDesignModeOptionForReport(String reportName) {
		String x = format("//span[text()='%s']/ancestor::div[1]", reportName);
		String row = getAttribute(x, "aria-rowindex");
		System.out.println("row " + row);
		assertTrue(click("//div[@column=2]/div[@role='gridcell' and @aria-rowindex='" + row + "']"));
		String design = format("//div[@aria-rowindex='" + row + "']//li/a[text()='Design']");
		waitForElementClickable(design);
		assertTrue(click(design));
		doWait(3000);
		navigateToTab(1);
		return this;
	}

	/* Mashood khan */

	public DatasetExplorer expandFieldsButton() {
		assertTrue(click(fieldsButton));
		return this;
	}

	public DatasetExplorer selectColumns() {
		assertTrue(clear("//input[@type='search']"));
		return this;
	}

	public DatasetExplorer selectFields(ArrayList<String> reportColumns) {
		assertTrue(click("//button[@title='None selected']"));
		for (String columnName : reportColumns) {
			String xpathExpression = String.format("//label[contains(text(),'%s')]", columnName);
			WebElement element = findElement(xpathExpression);
			assertTrue(click(element));
			doWait(2000);
		}
		assertTrue(click("(//label[text()='Field Configuration'])[1]"));
		assertTrue(click("button[id='button-field_configuration']"));
		waitForElementInvisible("//div[text()='Field Configuration...']");
		doWait(3000);
		return this;
	}

}
