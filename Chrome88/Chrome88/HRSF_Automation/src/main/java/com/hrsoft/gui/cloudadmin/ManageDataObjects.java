package com.hrsoft.gui.cloudadmin;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.hrsoft.utils.csvprocessing.CSVReader;
import com.hrsoft.utils.seleniumfy.BasePage;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */

public class ManageDataObjects extends BasePage {
	private String dataObjectXpath = "//div[@itemid='select_data_object']//button[@class='btn-sm btn btn-secondary']";
	private String dataFieldXpath = "//i[contains(text(),'Data Field') and @class='fal fa-plus-square']";
	private String modalTitle = "//h5[@class='modal-title']";
	private String tableNameField = "//input[@id='fldtable_name' and @name='fldtable_name']";
	private String columnNameField = "//label[text()='Column Name']/following-sibling::input[@id='fldcolumn_name' and @name='fldcolumn_name']";
	private String sensitivity = "//div[@id='fldsensitivity' and @name='fldsensitivity']//child::input";
	private String saveAndClose = "//*[text()='Save & Close']";
	private String save = "//div[@itemid='BUTTON_379157566385311']//button";
	private String selectYes = "//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@webix_l_id='Y']";
	private String selectNo = "//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@webix_l_id='N']";
	private String selectDataType = "//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@webix_l_id='%s']";
	private String selectUniqueId = "//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@webix_l_id='uniqueidentifier']";
	private String isUnique = "//div[@name='fldis_unique']//div[@class='webix_el_box']//input";
	private String isRequired = "//div[@name='fldrequired_ind']//div[@class='webix_el_box']//input";
	private String isPrimary = "//div[@name='fldis_primary']//div[@class='webix_el_box']//input";
	private String dataType = "//div[@name='flddata_type']//div[@class='webix_el_box']//input";
	private String dataLength = "//div[@name='data_length']//input[@id='flddata_length']";
	private String okPopUp = "button[class='btn btn-secondary btn-md']";
	private String confirm = "//button[contains(@class,'btn btn-primary btn-md')]";
	private String pleaseWait = "//h5[contains(text(),'Please wait')]";
	private String synchronize = "//h5[@class='modal-title'][contains(text(),'Synchronizing')]";
	private String loadingIndicators = "(//img[@src='/resources/skins/default/portal/new/images/wait-icon.gif'])";
	private String relationshipTab = "//li[@title='Relationships']//a";
	private String relationshipPlusButton = "//button//i[contains(text(),'Relationship')]";
	private String foreignKeyDropdown = "//div[@name='fldforeign_key']//div[@class='webix_el_box']//input";
	private String localKeyDropdown = "//div[@name='fldlocal_key']//div[@class='webix_el_box']//input";
	private String tableNameDropdown = "//div[@name='fldtable_name']//div[@class='webix_el_box']//input";
	private String foreignCardinalityDropdown = "//div[@name='fldforeign_cardinality']//div[@class='webix_el_box']//input";
	private String localCardinalityDropdown = "//div[@name='fldlocal_cardinality']//div[@class='webix_el_box']//input";
	private String typeOfRelationship = "//div[@id='fldrelationship_type']//input";
	private String confirmBtn = "//button[text()='Confirm']";
	private String blurOut = "//h5[text()='Add New Record']";
	private String addMappingBtn = "div[itemid='GRID_413115183171617'] div[role='group'] button:first-child";

	private DataManagementPage dm = new DataManagementPage();

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy(""));
	}

	public ManageDataObjects selectProduct(String product) {
		assertTrue(click("//div[@itemid='GRID_LAYOUT_COLUMN_1517340688639_0']//input[@role='combobox']"));
		assertTrue(click(String.format("//div[contains(@style,'display: block;')]//strong[text()='%s']", product)));
		return this;
	}

	public ManageDataObjects selectProductInDataManagement(String product) {
		assertTrue(click("//div[@itemid='select_a_aeedd78e']//input[@role='combobox']"));
		assertTrue(click(String.format("//div[contains(@style,'display: block;')]//strong[text()='%s']", product)));
		doWait(2000);
		return this;
	}

	public ManageDataObjects createNewDataObject(String dataObjectName) {
		doWait(3000);
		assertTrue(click(dataObjectXpath));
		assertTrue(waitUntilVisible(modalTitle));
		assertTrue(clear(tableNameField));
		assertTrue(setText(tableNameField, dataObjectName));
		assertTrue(click(sensitivity));
		assertTrue(click(
				"//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[contains(text(),'PUBLIC WITHIN ORGANIZATION')]"));
		saveAndClose();
		refreshDataObjects();
		return this;
	}

	public ManageDataObjects selectDataObject(String dataObjectName) {
		refreshDataObjects();
		assertTrue(click(String.format("//div[@role='gridcell'][contains(text(),'%s')]", dataObjectName)));
		if (isElementVisible(okPopUp))
			assertTrue(click(okPopUp));
		return this;
	}

	public ManageDataObjects addNewPrimaryDataField(String colmnName, String dataTypeOption) {
		assertTrue(click(dataFieldXpath));
		assertTrue(clear(columnNameField));
		waitForElementClickable("div[id='fldsensitivity'] input");
		setText("div[id='fldsensitivity'] input", "PUBLIC WITHIN ORGANIZATION-hrsofti");
		assertTrue(setText(columnNameField, colmnName));
		assertTrue(click(dataType));
		assertTrue(click(String.format(selectDataType, dataTypeOption)));
		assertTrue(setText(dataLength, "100"));
		assertTrue(click(isUnique));
		assertTrue(click(selectYes));
		assertTrue(click(isRequired));
		assertTrue(click(selectYes));
		assertTrue(click(isPrimary));
		assertTrue(click(selectYes));
		saveAndClose();
		return this;
	}

	public ManageDataObjects addNewUniqueDataField(String colmnName, String dataTypeOption) {
		assertTrue(click(dataFieldXpath));
		assertTrue(clear(columnNameField));
		waitForElementClickable("div[id='fldsensitivity'] input");
		setText("div[id='fldsensitivity'] input", "PUBLIC WITHIN ORGANIZATION-hrsofti");
		assertTrue(setText(columnNameField, colmnName));
		assertTrue(click(dataType));
		assertTrue(click(String.format(selectDataType, dataTypeOption)));
		assertTrue(clear(dataLength));
		assertTrue(setText(dataLength, "100"));
		assertTrue(click(isUnique));
		assertTrue(click(selectYes));
		assertTrue(click(isRequired));
		assertTrue(click(selectYes));
		saveAndClose();
		return this;
	}

	public ManageDataObjects addDataField(String colmnName, String dataTypeOption) {
		assertTrue(click(dataFieldXpath));
		assertTrue(clear(columnNameField));
		waitForElementClickable("div[id='fldsensitivity'] input");
		setText("div[id='fldsensitivity'] input", "PUBLIC WITHIN ORGANIZATION-hrsofti");
		assertTrue(setText(columnNameField, colmnName));
		assertTrue(click(dataType));
		assertTrue(click(String.format(selectDataType, dataTypeOption)));
		doWait(2000);
		WebElement x = findElement(dataLength);
//		waitForElementClickable(x);
//		x.click();
//		x.sendKeys("100");
		doWait(1000);
		assertTrue(setText(dataLength, "100"));
		saveAndClose();
		return this;
	}

	public ManageDataObjects synchronizeDataObject(String dataObjectName) {
		clickSynchronize(dataObjectName);
		waitUntilVisible(confirm);
		assertTrue(click(confirm));
		waitForElementInvisible(synchronize);
		// if (isElementVisible ("//div[@class='h4 message-box-title text-danger' and
		// text()='Error']"))
		// Assert.fail ("Error occured while synchronizing " + dataObjectName);
		if (isElementVisible(okPopUp))
			assertTrue(click(okPopUp));
		return this;
	}

	public ManageDataObjects deleteDataObject(String dataObjectName) {
		refreshDataObjects();
		clickDelete(dataObjectName);
		waitUntilVisible(confirm);
		assertTrue(click(confirm));
		waitForElementInvisible(synchronize);
		if (isElementVisible("//div[@class='h4 message-box-title text-danger' and text()='Error']"))
			Assert.fail("Error occured while synchronizing " + dataObjectName);
		if (isElementVisible(okPopUp))
			assertTrue(click(okPopUp));
		return this;
	}

	private void clickSynchronize(String tableName) {
		String row = getAttribute("//div[text()='" + tableName + "']", "aria-rowindex");
		assertTrue(click(String.format(
				"//div[text()='%s']//preceding::div[@aria-rowindex='%s']//a[@title='Synchronize']", tableName, row)));
	}

	private void clickDelete(String tableName) {
		String delete = "//div[@role='gridcell' and text()='" + tableName + "']";
		if (isElementPresent(delete)) {
			String row = getAttribute(delete, "aria-rowindex");
			assertTrue(click(String.format(
					"//div[text()='%s']//preceding::div[@aria-rowindex='%s']//a[contains(@title,'Delete')]", tableName,
					row)));
		}
	}

	private void saveAndClose() {
		assertTrue(click(saveAndClose));
		if (isElementVisible(okPopUp))
			assertTrue(click(okPopUp));
		if (isElementVisible(okPopUp))
			assertTrue(click(okPopUp));
	}

	/* Create Relationships : Mashood */

	public ManageDataObjects createRelationship(String key, String table1, String table2, String foreignCardinality,
			String localCardinality) {
		refreshDataObjects();
		selectDataObject(table2);
		if (!isElementVisible("div[rendertype='richtext']"))
			clickRelationshipTab();
		clickRelationshipPlusButton();
		selectForeignKeyCol(key);
		selectDataObjectToMap(table1);
		selectLocalKeyCol(key);
		selectForeignCardinality(foreignCardinality);
		selectLocalCardinality(localCardinality);
		assertTrue(click(saveAndClose));
		refreshDataObjects();
		return this;
	}

	public ManageDataObjects createCustGuidRelationship(String key, String table1, String table2,
			String foreignCardinality, String localCardinality) {
		refreshDataObjects();
		selectDataObject(table2);
		if (!isElementVisible("div[rendertype='richtext']"))
			clickRelationshipTab();
		clickRelationshipPlusButton();
		selectForeignKeyCol(key);
		selectDataObjectToMap(table1);
		selectLocalKeyCol(key);
		selectForeignCardinality(foreignCardinality);
		selectLocalCardinality(localCardinality);
		assertTrue(click(saveAndClose));
		return this;
	}

	public ManageDataObjects refreshDataObjects() {
		assertTrue(click("//button[@class='btn-sm btn btn-white']//i"));
		return this;

	}

	public ManageDataObjects clickRelationshipTab() {
		assertTrue(click(relationshipTab));
		return this;
	}

	public ManageDataObjects clickRelationshipPlusButton() {
		assertTrue(click(relationshipPlusButton));
		assertTrue(waitUntilVisible("//*[text()='Add New Relationship']"));
		return this;
	}

	public ManageDataObjects selectForeignKeyCol(String foreignkeyCol) {
		assertTrue(click(foreignKeyDropdown));
		doWait(2000);
		String key = String.format(
				"//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@webix_l_id='%s']",
				foreignkeyCol);
		assertTrue(click(key));
		return this;
	}

	public ManageDataObjects selectLocalKeyCol(String localkeyCol) {
		doWait(2000);
		assertTrue(click(localKeyDropdown));
		String key = String.format(
				"//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@webix_l_id='%s']",
				localkeyCol);
		assertTrue(click(key));
//		findElement(localKeyDropdown).sendKeys(localkeyCol);
//		click("//h5[@class='modal-title']");
		doWait(2000);
		return this;
	}

	public ManageDataObjects selectDataObjectToMap(String tblName) {
		findElement(tableNameDropdown).sendKeys(tblName);
		click("//h5[@class='modal-title']");
		doWait(2000);
		return this;
	}

	public ManageDataObjects selectForeignCardinality(String value) {
		findElement(foreignCardinalityDropdown).sendKeys(value);
		click("//h5[@class='modal-title']");
		doWait(2000);
		return this;
	}

	public ManageDataObjects selectLocalCardinality(String value) {
		assertTrue(click(localCardinalityDropdown));
		String s = String.format(
				"//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@webix_l_id='%s']",
				value);
		assertTrue(click(s));
		return this;
	}

	public ManageDataObjects selectType(String value) {
		assertTrue(click(typeOfRelationship));
		String s = String.format(
				"//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[@webix_l_id='%s']",
				value);
		assertTrue(click(s));
		return this;
	}

	public ManageDataObjects setLocalOrdinalPosition() {
		WebElement el = findElement("//input[@id='fldlocal_ordinal_position']");
		String stringValue = Integer.toString(1);
		el.sendKeys(stringValue);
		return this;
	}

	public ManageDataObjects defineNewFileFeed(String fileFeed) {
		defineFileFeedTillSave(fileFeed);
//		if (isElementVisible("//div[@class='h4 message-box-title text-danger' and text()='Duplicate Name']")) {
//			assertTrue(click("//span[text()='Feed Management']"));
//			new DataManagementPage().deleteRowForGivenFeedName(fileFeed);
//			defineFileFeedTillSave(fileFeed);
//		}
		assertTrue(click(okPopUp));
		return this;
	}

	public ManageDataObjects selectExtensionForFeed(String extension) {
		dm.selectOptionFromDropdown("MENU_307744904533454", extension);
		return this;
	}

	private void defineFileFeedTillSave(String fileFeed) {
		if (!isElementVisible("div[itemid='GRID_LAYOUT_COLUMN_1588358709266_0']"))
			assertTrue(click("//span[text()='Configure Feed']"));
		assertTrue(click("//span[text()='Create New Feed']"));
		assertTrue(setText("//div[@itemid='TEXT_INPUT_378662877440517']//child::input", fileFeed));
		assertTrue(click(save));
	}

	public ManageDataObjects clickFieldMapping() {
		assertTrue(click("//span[text()='Field Mapping']"));
		return this;
	}

	public ManageDataObjects clickMapUsingCsv() {
		assertTrue(click("//div[@itemid='GRID_413115183171617']//button//i[contains(text(),'Map Using CSV')]/.."));
		return this;
	}

	public void uploadFilesForMapping(String filePath, String dataObject) {
		doWait(1000);
		findElement("//div[@itemid='ASSET_UPLOADER_9996baa5']//input[@name='uploadFile[]']").sendKeys(filePath);
		assertTrue(click("//div[@itemid='ASSET_UPLOADER_9996baa5']//a[@title='Upload selected files']"));
		WebElement dataObjectInput = findElement("div[itemid='target_tables_for_auto_c9992a51'] input");
		doWait(2000);
		clear(dataObjectInput);
		assertTrue(setText(dataObjectInput, dataObject));
		dataObjectInput.sendKeys(Keys.ENTER);
		doWait(2000);
		assertTrue(click("//button//span[contains(text(),'Generate Automatic Mappings')]/.."));
		refreshFeedGrid();

	}

	public void refreshFeedGrid() {
		assertTrue(click("//i[contains(text(),'Refresh')]/../."));
		waitForElementInvisible("span.loadingOverlay");
	}

	public ManageDataObjects addFieldMapping(String columnName, String tableName, String dataType) {
		assertTrue(click(addMappingBtn));
		doWait(2000);
		// findElement("//div[@itemid='MENU_181244686527847']//child::input").sendKeys(tableName);
		assertTrue(setText("//div[@itemid='MENU_181244686527847']//child::input", tableName));
		findElement("//div[@itemid='MENU_181244686527847']//child::input").sendKeys(Keys.ENTER);
		doWait(2000);
		// findElement("//div[@itemid='TEXT_INPUT_181070341312163']//child::input").sendKeys(columnName);
		assertTrue(setText("//div[@itemid='TEXT_INPUT_181070341312163']//child::input", columnName));
		findElement("//div[@itemid='TEXT_INPUT_181070341312163']//child::input").sendKeys(Keys.ENTER);
		doWait(2000);
		// findElement("//div[@itemid='MENU_181253905524841']//child::input").sendKeys(columnName);
		assertTrue(setText("//div[@itemid='MENU_181253905524841']//child::input", columnName));
		findElement("//div[@itemid='MENU_181253905524841']//child::input").sendKeys(Keys.ENTER);
		click("//h5[@class='modal-title']");
		doWait(2000);
		// findElement("//div[@itemid='MENU_181268070362412']//child::input").sendKeys(dataType);
		assertTrue(setText("//div[@itemid='MENU_181268070362412']//child::input", dataType));
		findElement("//div[@itemid='MENU_181268070362412']//child::input").sendKeys(Keys.ENTER);
		// click("//h5[@class='modal-title']");
		doWait(2000);
		if (dataType.equalsIgnoreCase("DATE")) {// Mandatory to select format option , if datatype is date{
			assertTrue(setText("//div[@itemid='MENU_1556762177044176']//child::input", "MM/dd/yyyy"));
			findElement("//div[@itemid='MENU_1556762177044176']//child::input").sendKeys(Keys.ENTER);
		}
		doWait(2000);
		assertTrue(setText("//div[@itemid='TEXT_INPUT_181228004661947']//child::input", "100"));
		findElement("//div[@itemid='TEXT_INPUT_181228004661947']//child::input").sendKeys(Keys.ENTER);
		doWait(2000);
		saveAndClose();
		return this;
	}

	public ManageDataObjects clickvalidate() {
		assertTrue(click("//button//i[contains(text(),'Validate')]"));
		assertTrue(click(okPopUp));
		return this;
	}

	public ManageDataObjects importFileFeed(String fileFeed) {
		if (!isElementVisible("div[itemid='GRID_LAYOUT_COLUMN_1588372256381_0']"))
			assertTrue(click("//span[text()='Import']"));
		assertTrue(click("//div[@itemid='MENU_335629169244049']//child::input"));
		assertTrue(click(String.format(
				"//div[@class='webix_view webix_window webix_popup'][contains(@style,'display: block;')]//div[text()='%s']",
				fileFeed)));
		return this;
	}

	public ManageDataObjects selectCreatedByMe() {
		assertTrue(click("//div[@itemid='MENU_86466648522318']//input"));
		assertTrue(click("//div[@webix_l_id='owned_by_me']"));
		assertTrue(waitForElementInvisible(loadingIndicators));
		return this;
	}

	public ManageDataObjects searchAndDeleteReport(String reportName) {
		assertTrue(setText("//div[@itemid='TEXT_INPUT_a23d103f']//input", reportName));
		assertTrue(waitForElementInvisible(loadingIndicators));
		assertTrue(click("//div[@itemid='GRID_179315616374987']//div[@column='2']"));
		assertTrue(click("//div[@itemid='GRID_179315616374987']//div[@column='2']//a[text()='Remove']"));
		assertTrue(click(confirm));
		return this;
	}

	public ManageDataObjects searchAndDeleteDataset(String datasetName) {
		assertTrue(click(
				"//div[@itemid='GRID_6522871416908']//div[@column='1']//span[@class='webix_excel_filter webix_icon wxi-filter']"));
		assertTrue(setText(
				"//div[@class='webix_view webix_window webix_popup' and contains(@style,'display: block;')]//input",
				datasetName));
		String row = getAttribute("//div[@role='gridcell']//span[contains(text(),'" + datasetName + "')]/..",
				"aria-rowindex");
		assertTrue(click(
				String.format("//div[text()='%s']//preceding::div[@aria-rowindex='%s']//a[contains(@title,'Delete')]",
						datasetName, row)));
		assertTrue(click(confirm));
		return this;
	}

	public ManageDataObjects clickFeedManagement() {
		assertTrue(click("//div[@itemid='NAVIGATION_296057045140243']//*[text()='Feed Management']"));
		return this;
	}

	public ManageDataObjects deleteFileFeed(String fileFeed) {
		clickDelete(fileFeed);
		assertTrue(click(confirm));
		assertTrue(click(okPopUp));
		return this;
	}

	/* With POJO */

	public ManageDataObjects createCustGuidRelationship(DatobjectRelationships relationship) {
		refreshDataObjects();
		selectDataObject(relationship.getTable1());
		if (!isElementVisible("div[rendertype='richtext']"))
			clickRelationshipTab();
		clickRelationshipPlusButton();
		selectForeignKeyCol(relationship.getKey());
		selectForeignCardinality(relationship.getForeignCardinality());
		selectDataObjectToMap(relationship.getTable2());
		selectLocalKeyCol(relationship.getKey());
		selectLocalCardinality(relationship.getLocalCardinality());
		selectType(relationship.getType());
		assertTrue(click(saveAndClose));
		return this;
	}

	public ManageDataObjects createRelationship(DatobjectRelationships relationship) {
		refreshDataObjects();
		selectDataObject(relationship.getTable1());
		if (!isElementVisible("div[rendertype='richtext']"))
			clickRelationshipTab();
		clickRelationshipPlusButton();
		System.out.println("Key : " + relationship.getKey());
		selectForeignKeyCol(relationship.getKey());
		selectForeignCardinality(relationship.getForeignCardinality());
		selectDataObjectToMap(relationship.getTable2());
		selectLocalKeyCol(relationship.getKey());
		selectLocalCardinality(relationship.getLocalCardinality());
		selectType(relationship.getType());
		assertTrue(click(saveAndClose));
		refreshDataObjects();
		return this;
	}

}
