package com.hrsoft.gui.dataset;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.cloudadmin.DatasetExplorer;

public class DatasetEditor extends HRSoftPage {

	private String dsNameInput = "//input[@inputid='TEXT_INPUT_1537991514065']";
	private String applyBtn = "//div[@itemid='BUTTON_1538082861292_copy_131859863300233']//button";
	private String testAndDebug = "//div[@itemid='BUTTON_1115864453176370']//button";
	private String getRecords = "//button[text()='Get Records']";
	private String plusFieldButton = "(//div[@itemid='GRID_47608159762534']//button[contains(@class,'btn-sm btn btn-secondary waves-effect waves-light')])[2]";
	private String allFieldsCheckbox = "//div[@itemid='GRID_85737af6']//div[@row=0]//input";
	private String addFieldsInBulkBtn = "div[itemid='GRID_85737af6'] button[class='btn-sm btn btn-secondary']";
	private String firstDataObjectSearchInput = "//div[@itemid='GRID_c0dd8280']//div[@class='webix_hs_center']//div[@row=1 and @column=1]//input";
	private String nextDataObjectSearchInput = "//div[@itemid='GRID_1539202021042']//div[@class='webix_hs_center']//div[@row=1 and @column=1]//input";
	private String dataObjectSourceIcon = "//div[@role='gridcell' and @aria-rowindex=1]//a[@title='Add a Root Source']";
	private String sourcebutton = "//div[@itemid='GRID_19589639038077']//button[@class='btn-sm btn btn-secondary waves-effect waves-light']";
	private String pleaseWait = "//h5[@class='modal-title' and text()='Please Wait...']";
	private String computedFieldBtn = "//div[@itemid='GRID_85737af6']//button[contains(@class,'btn-primary')]";
	private String fieldGridId = "GRID_47608159762534";
	private String customSqlInput = "//div[@itemid='TEXT_INPUT_1539196239419']//textarea[contains(@class,'form-control')]";
	private String errorDialog = "//div[@itemid='GRID_LAYOUT_543086832210375']";

	// private String appliedFiltersGridId =
	// "GRID_3759306338603_copy_1437693742681856";
	// private String advancedButton =
	// "//div[@itemid='BUTTON_1538082861292']//button";
	// private String inheritanceCheckbox =
	// "//div[@itemid='LIST_1538669305619_copy_40170360187042']//input";
	// private String inheritanceDefaultInput =
	// "//div[@itemid='TEXT_INPUT_1538669483067_copy_40853038995476']//input";

	@Override
	@SuppressWarnings({ "rawtypes" })
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy(""));
	}

	public boolean confirmName(String name) {
		return name.equals(getAttribute(dsNameInput, "value"));
	}

	public DatasetEditor causeValidationError() {
		addComputedField("TEST");
		doWait(1000);
		clickInlineAction(fieldGridId, 1, " Edit");
		waitForElementVisible(customSqlInput);
		setText(customSqlInput, "{{tcc_rad_set.name}}");
		clickOnBlankSpace();
		closePopup();

		// Element keeps going missing as if we're on a different tab?
		navigateToTab(1);

		assertTrue(click(waitForElementClickable(applyBtn)));
		return this;
	}

	public DatasetEditor closeErrorDialog() {
		waitForElementsVisible(errorDialog);
		closePopup();
		return this;
	}

	public DatasetEditor clickSource() {
		assertTrue(click(sourcebutton));
		doWait(500);
		return this;
	}

	public DatasetEditor selectRootDataObjectSource(String tableName) {
		doWait(500);
		clickSource();
		doWait(2000);
		waitForElementInvisible("(//span[@class='loadingOverlay'])[2]");
		clear(firstDataObjectSearchInput);
		doWait(2000);
		setText(firstDataObjectSearchInput, tableName);
		doWait(2000);
		waitForElementVisible(String.format("(//span[text()='%s'])[1]", tableName));
		doWait(1000);
		assertTrue(click(dataObjectSourceIcon));
//		if(findElement("//div[contains(@class,'text-info')]") != null) 
//			clickPopupBtn("Confirm");
		waitForElementInvisible("//div[@itemid='GRID_1539202021042']//span[@class='loadingOverlay']");
		closePopup();

		// test
//		String join = String.format(
//				"//span[text()='%s']/../../../../..//../../../.././/div[@column=4]/div[@role='gridcell']",
//				tableName.toLowerCase());
//		assertTrue(click(join));
//		doWait(1000);
//		assertTrue(click("//div[@webix_l_id='outer']"));
		doWait(2000);
		return this;
	}

	public DatasetEditor selectNextDataObjects(String tableName) {
		doWait(500);
		clickSource();
		doWait(2000);
		waitForElementInvisible("(//span[@class='loadingOverlay'])[2]");
		assertTrue(clear(nextDataObjectSearchInput));
		doWait(2000);
		setText(nextDataObjectSearchInput, tableName);
		doWait(2000);
		waitForElementVisible(String.format("(//span[text()='%s'])[1]", tableName));
		waitForElementInvisible("//div[@itemid='GRID_1539202021042']//span[@class='loadingOverlay']");
		assertTrue(click("//div[@role='gridcell' and @aria-rowindex=1]//a[@title=' Add a Source']"));
		closePopup();
		// test
//		String join = String.format(
//				"//span[text()='%s']/../../../../..//../../../.././/div[@column=4]/div[@role='gridcell']",
//				tableName.toLowerCase());
//		assertTrue(click(join));
//		doWait(1000);
//		assertTrue(click("//div[@webix_l_id='outer']"));
//		doWait(2000);
		return this;
	}

	public DatasetEditor addAllFields() {
		clickFields();
		waitForElementVisible("//h5[@class='modal-title' and text()='Add Fields']");
		selectAllFields();
		clickAddFieldsInBulk();
		return this;
	}

	public DatasetEditor addComputedField(String fieldName) {
		clickFields();
		assertTrue(click(computedFieldBtn));
		setText("//input[@id='promptBox']", fieldName);
		clickPopupBtn("Submit");
		return this;
	}

	public DatasetEditor clickFields() {
		assertTrue(click(plusFieldButton));
		return this;
	}

	public DatasetEditor selectAllFields() {
		assertTrue(click(allFieldsCheckbox));
		return this;
	}

	public DatasetEditor clickAddFieldsInBulk() {
		assertTrue(click(addFieldsInBulkBtn));
		waitForElementInvisible(pleaseWait);
		closePopup();
		return this;
	}

	public DatasetEditor previewData() {
		clickApply();
		clickTestAndDebug();
		clickGetRecords();
		return this;
	}

	public DatasetEditor clickApply() {
		assertTrue(click(applyBtn));
		return this;
	}

	public DatasetEditor clickTestAndDebug() {
		assertTrue(click(testAndDebug));
		waitForElementInvisible(pleaseWait);
		return this;
	}

	public DatasetEditor clickGetRecords() {
		assertTrue(click(getRecords));
		waitForElementInvisible(pleaseWait);
		return this;
	}

	public DatasetEditor addJoinsInDataset(String join) {
		List<WebElement> tbls = findElements("//div[@column=4 and @class='webix_column text-center webix_last']/div");
		for (WebElement tbl : tbls) {
			assertTrue(click(tbl));
			// assertTrue(clear(tbl));
			doWait(1000);
			tbl.sendKeys(join);
			doWait(1000);
			tbl.sendKeys(Keys.ENTER);
			doWait(1000);
		}
		return this;
	}
}
