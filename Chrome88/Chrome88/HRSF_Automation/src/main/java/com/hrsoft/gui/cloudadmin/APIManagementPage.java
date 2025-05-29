package com.hrsoft.gui.cloudadmin;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.hrsoft.utils.seleniumfy.BasePage;

public class APIManagementPage extends BasePage {
    
    private String partnerRegistrationLink = "//div[@itemid='TEXT_5110490700696010']";
    private String apiPartnerGrid = "//div[@itemid='GRID_3016046435652668']//div[@role='grid']";
    private String inlineEditButtons = "//div[@class='webix_ss_left']//div[@role='gridcell']//div[@class='grid-inline-btn-holder tcc-grid-valign NAVBUTTONINLINE_']//a[@title='Edit']";
    private String mainApiUrlCell = "//div[@class='webix_ss_center']//div[@column='2'][@class='webix_column ']//div[@role='gridcell']";
    private String mainApiUrlInputBox = "//input[@name='fldmain_api_url_1b4f2855']";
    private String saveAndCloseButtonOnEditRecordPopup = "//button[text()='Save & Close']";
    private String buttonToReloadPartnerGrid = "//div[@class='widget-action-holder pull-right']//div[@class='buttonHolder']//button[2]";
    private String apiTransformAttributes = "//div[text()='Manage Custom transformations for API related data for customer']";
    private String apiTransformAttributesGrid = "//div[@itemid='GRID_LAYOUT_3510631191576953']";
    @Override
    protected ExpectedCondition getPageLoadCondition () {
        return ExpectedConditions.visibilityOfElementLocated (locateBy ("[itemid='GRID_LAYOUT_COLUMN_1575924714930_0']"));
    }

    public void openPartnerRegistrationPopup() {
        click (partnerRegistrationLink);
        waitForElementsVisible (apiPartnerGrid);
    }
    
    public int getPartnerCount() {
        return Integer.parseInt (findElement(apiPartnerGrid).getAttribute ("aria-rowcount"));
    }
    
    public String getMainApiUrlFromRow(int index) {
        return findElements(apiPartnerGrid+mainApiUrlCell).get (index).getText();
    }
    
    public boolean openEditPopupForApiPartner(int index) {
        click(findElements(apiPartnerGrid+inlineEditButtons).get (index));
        return waitUntilVisible("//h5[@class='modal-title'][text()='Edit Record']");
    }
    
    public void updateMainApiUrl(String newUrl) {
        clear(mainApiUrlInputBox);
        setText(mainApiUrlInputBox,(newUrl));
    }
    
    public boolean saveAndCloseEditRecordPopup() {
        click(saveAndCloseButtonOnEditRecordPopup);
        return waitForElementInvisible ("//h5[@class='modal-title'][text()='Edit Record']");
    }
    
    public boolean refreshPartnerGrid() {
        click(buttonToReloadPartnerGrid);
        return waitUntilVisible (apiPartnerGrid);
    }

}
