package com.hrsoft.gui.dataset;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.hrsoft.gui.HRSoftPage;

public class ValidateAll extends HRSoftPage {

	private String noErrorsText = "//div[@itemid='TEXT_1503259774169013']";
	private String errorGridId = "GRID_542692042814599";
	
	@Override
	@SuppressWarnings({ "rawtypes" })	
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy(""));
	}
	
	public int getErrorCount() {
		waitForElementVisible(noErrorsText + " | //div[@itemid='" + errorGridId + "']");
		if(isElementVisible(noErrorsText)) {
			return 0;
		} else {
			return getColumnValuesOfGrid(3, errorGridId).size();
		}
	}
	
	public void validateErrorCountIncreased(int previousCount) {
		assertTrue(getErrorCount() > previousCount);
	}
	
	public DatasetEditor editDataset(String dsName) {
		int dsIndex = Integer.parseInt(getAttribute("//div[@itemid='" + errorGridId + "']//div[@column='1']//div[text()='" + dsName + "']","aria-rowindex"));
		clickInlineAction(errorGridId,dsIndex+1,"Edit Dataset");
		navigateToTab(2);
		
		DatasetEditor datasetEditor = new DatasetEditor();
		assertTrue(datasetEditor.closeErrorDialog().confirmName(dsName));
		return datasetEditor;
	}
}
