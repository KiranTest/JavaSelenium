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

import com.hrsoft.constants.Constants;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.cloudadmin.DataManagementPage;
import com.hrsoft.gui.cloudadmin.DatasetExplorer;

public class DataViewPage extends HRSoftPage {

    private String reportNameGridCell                   = "//div[@role='gridcell']//span[text() = '%s']";
    private String budgetAllocation                     = "//span[text()='Budget Allocation by Leader and Division V2']";
    private String planDropdown                         = "//input[@placeholder='Select a Compensation Plan']";
    private String groupName                            = "//input[@placeholder='Select a Group Name']";
    private String filter                               = "//input[@placeholder='Select a Filter']";
    private String budget                               = "//input[@placeholder='Select a Budget']";
    private String runBtn                               = "div[itemid='BUTTON_7044203639573549'] button";
    private String createReportBtn                      = "//button[@data-content='Create New Report']";
    private String productCategory                      = "//*[text()='Product Category']/following::input[@role='combobox']";
    private String reportsList                          = "//div[@column='1']//div[@role='gridcell']//span[@class='custom_renderer_click']";
    private String firstReportItem                      = "//div[@column='1']//div[@role='gridcell']//span[@class='custom_renderer_click'][1]";
    private String reportLink                           = "//div[@column='1']//div[@role='gridcell']//span[text()='%s']";
    private String reportTitle                          = "//div[@data-step-title='Report Title 1   ']/../div[@rendertype='h5']/h5/div";
    // private String reportGridXPath =
    // "//div[@itemid='GRID_LAYOUT_COLUMN_1634733111851_1']//div[@itemid='%s']";
    private String pleaseWait                           = "//h5[text()='Please wait...']";
    private String dataViewIcon                         = ".question_title  .font-icon.tcc_product_dataview";
    private String reportListTypeDropDown               = "//div[@itemid='GRID_LAYOUT_COLUMN_1638351956364_0']//input[@role='combobox']";
    private String reportColumnHeaders                  = "//div[@itemid='GRID_LAYOUT_COLUMN_1634733111851_1']//div[@class='webix_hcolumn']/div[@row='0']";
    private String reportRows                           = "//div[@itemid='GRID_LAYOUT_COLUMN_1634733111851_1']//div[@aria-colindex='1']";
    private String noDataAvailable                      = "//div[@itemid='GRID_LAYOUT_COLUMN_1634733111851_1']//div[@class='webix_overlay']";

    private String reportOptionsList                    = "//div[@itemid='BUTTON_3e34b370']//button";
    private String reportInput                          = "//div[@itemid='TEXT_INPUT_10132273422594585']//input";
    private String reportInputDesc                      = "//div[@itemid='TEXT_INPUT_10131928374333830']//textarea";
    private String reportInputTag                       = "//div[@itemid='TEXT_INPUT_10132098455396434']//input";
    private String updateRenameReportButton             = "//div[@itemid='BUTTON_3b5ea639']//button";
    private String successButton                        = "div[class='bootbox modal modal_DEFAULT_message bootbox_message_box bootbox_SUCCESS ui-draggable show'] button";
    private String infoButton                           = "div[class='bootbox modal modal_DEFAULT_message bootbox_message_box bootbox_INFO ui-draggable show'] button";

    private String searchButton                         = "//div[@itemid='BUTTON_9941e42b']//button";
    private String searchInputFile                      = "//div[@itemid='TEXT_INPUT_2349421579091713']//input";
    private String searchDialogSearchButton             = "//div[@itemid='BUTTON_2354392278443478']//button";
    private String searchDialogResetButton              = "//div[@itemid='BUTTON_2354384792591984']//button";
    private String searchCloseDialogButton              = "//div[@itemid='BUTTON_2360930583142282']//button";
    private String searchDialogResultFirstRow           = "div[itemid='GRID_2344943517659758'] div.webix_ss_body div.webix_ss_left input.webix_table_radio:first-child";
    private String searchDialogSelectGroupBtn           = "//div[@itemid='BUTTON_2360900231990283']//button";
    private String sharePopUpSearchButton               = "//div[@itemid='BUTTON_4858192164904773']//button";
    private String searchPopUpSearchButtonForSharePopUp = "//div[@itemid='BUTTON_1517540552356_copy_245_copy_60']//button";
    private String selectAndExitButtonOnSearchPopUp     = "//div[@itemid='button_preview_copy_210_copy_84']//button";
    private String filterDropDown                       = "div[itemid='MENU_86466648522318']";
    private String runReportBtn                         = "//div[@itemid='BUTTON_7044203639573549']//button";

    private String designReportBtn                      = "div[itemid='GRID_4fa8d569'] .form-group > div > button i.fa-cogs";
    private String refreshReportBtn                     = "div[itemid='GRID_4fa8d569'] .widget-action-holder i.fa-sync-alt";
    private String printReportOptionsBtn                = "div[itemid='GRID_4fa8d569'] .widget-action-holder i.fa-file-pdf";
    private String exportToPDFBtn                       = "//div[@itemid='BUTTON_53ec3b3f']//button";
    private String exportToExcelBtn                     = "//div[@itemid='BUTTON_7848f052']//button";
    private String exportToCSVBtn                       = "//div[@itemid='BUTTON_51dd2551']//button";

    private String groupableListDropDown                = "div[itemid='GRID_4fa8d569'] .grid-tcc-groupable-list .selectedInputGroupableList";
    private String groupableListDropDownItems           = "div[itemid='GRID_4fa8d569'] .grid-tcc-groupable_list_items li:nth-child(2) input";

    private String addFilterButton                      = "//button[text()= 'Add filter']";
    private String queryBuilderFilterDropdownList       = "div.webix_view.webix_popup[style*='display: block'] .webix_win_body > .webix_el_richselect .webix_el_box";
    private String loadingIcon                          = "span[class='loadingOverlay']";
    private String searchingText                        = "//h5[contains(text(),'Searching...')]";

    private String settingReportSharingTab              = "//a[@data=panel-id='Link11637834308472']";

    @Override
    protected ExpectedCondition getPageLoadCondition () {
        return ExpectedConditions.visibilityOfElementLocated (locateBy ("[itemid='GRID_179315616374987']"));
    }

    public DataViewPage clickAddUserForSharingReport () {
        assertTrue (click ("//div[@itemid='BUTTON_1376865803173853']//button"));
        doWait (3000);
        return this;
    }

    public void clickbudgetAllocation () {
        assertTrue (click (budgetAllocation));
    }

    public DataViewPage clickRun () {
        waitUntilVisible (runBtn);
        doWait (3000);
        assertTrue (click (runBtn));
        assertTrue (isElementPresent ("(//label[@class='translatable question_title pull-left mr-2'])[3]"));
        return this;
    }

    public DataViewPage clickReportOptionsList () {
        assertTrue (click (reportOptionsList));
        return this;
    }

    public DataViewPage clickSearchBtn () {
        assertTrue (click (searchButton));
        return this;
    }

    public DataViewPage clickDialogSearchBtn () {
        assertTrue (click (searchDialogSearchButton));
        return this;
    }

    public DataViewPage clickDialogResetBtn () {
        assertTrue (click (searchDialogResetButton));
        return this;
    }

    public DataViewPage clickDialogCloseBtn () {
        assertTrue (click (searchCloseDialogButton));
        return this;
    }

    public boolean clickDialogSearchResultFirstRow () {
        WebElement el = findElement (By.cssSelector (searchDialogResultFirstRow));
        return click (el);
    }

    public boolean clickDialogSelectGroupBtn () {
        return (click (searchDialogSelectGroupBtn));
    }

    public boolean clickRunReportBtn () {
        return (click (runReportBtn));
    }

    public boolean clickReportDesignerBtn () {
        WebElement el = findElement (By.cssSelector (designReportBtn));
        return click (el);
    }

    public boolean clickRefreshReportBtn () {
        WebElement el = findElement (By.cssSelector (refreshReportBtn));
        return click (el);
    }

    public boolean clickPrintOptionsReportBtn () {
        WebElement el = findElement (By.cssSelector (printReportOptionsBtn));
        return click (el);
    }

    public boolean clickExportToPDFBtn () {
        return (click (exportToPDFBtn));
    }

    public boolean clickExportToExcelBtn () {
        return (click (exportToExcelBtn));
    }

    public boolean clickExportToCSVBtn () {
        return (click (exportToCSVBtn));
    }

    public boolean clickGroupableListDropDown () {
        WebElement el = findElement (By.cssSelector (groupableListDropDown));
        return click (el);
    }

    public boolean clickGroupableListDropDownItems () {
        WebElement el = findElement (By.cssSelector (groupableListDropDownItems));
        return click (el);
    }

    public DataViewPage clickAddFilterButton () {
        if (!isElementPresent (addFilterButton))
            throw new org.testng.SkipException ("Add Filter button is not available for this report");
        assertTrue (click (addFilterButton));
        return this;
    }

    public DataViewPage clickQueryBuilderFilterDropDown () {
        assertTrue (click (queryBuilderFilterDropdownList));
        return this;
    }

    public DataViewPage clickFilterDropDown () {
        assertTrue (click (filterDropDown));
        return this;
    }

    public void addSearchText () {
        String dataViewSearchText = getConfig ().dataViewSearchText ();
        // System.out.println ("dataViewSearchText=" + dataViewSearchText);
        assertTrue (setText (searchInputFile, dataViewSearchText));
    }

    public DataViewPage clickCreateNewReport () {
        assertTrue (click (createReportBtn));
        return this;
    }

    @Deprecated
    public void clickproductCategory () {
        assertTrue (click (productCategory));
        assertTrue (click ("(//div[text()='Compensation'])[2]"));
    }

    public void clickTemplate (String name) {
        assertTrue (click (format ("//span[text()='%s']", name)));
    }

    public void clickSaveAsReport () {
        assertTrue (click ("//span[text()='Save As Report']"));
        waitForElementInvisible (pleaseWait);
    }

    public void clickConfirmReport () {
        assertTrue (click ("//span[text()='CONFIRM']"));
        waitForElementInvisible (pleaseWait);
        navigateToTab (1);
        isElementVisible ("[itemid='GRID_LAYOUT_6f11cfdf']");
        DriverManager.getDriver ().close ();
        navigateToTab (0);
    }

    public String getReportIndexForReportWithName (String reportName) {
        String xpathForReportWebElement = format ("//span[contains(text(), '%s')]/ancestor::div[@aria-colindex='2']",
                                                  reportName);
        assertTrue (isElementPresent (xpathForReportWebElement));
        return findElement (xpathForReportWebElement).getAttribute ("aria-rowindex");
    }

    public List <String> getReportList (String listName) {
        waitForElement (reportListTypeDropDown);
        assertTrue (click (reportListTypeDropDown));
        selectGivenListItemFromOpenWebixList (listName);

        List <WebElement> reportList = waitForElements (reportsList);
        List <String> resp = new ArrayList <> ();
        for (WebElement webElement : reportList) {
            resp.add (webElement.getText ().split ("\n")[0]);
        }
        return resp;
    }

    public String getFirstReport (String listName) {
        waitForElement (reportListTypeDropDown);
        assertTrue (click (reportListTypeDropDown));
        selectGivenListItemFromOpenWebixList (listName);

        WebElement firstReport = findElement (firstReportItem);
        String resp = new String ();
        resp = firstReport.getText ().split ("\n")[0];
        return resp;
    }

    public DataViewPage openReport (String reportName) {
        WebElement el = findElement (format (reportLink, reportName));
        scrollToView (el);
        assertTrue (click (el));
        return this;
    }

    public String getPresentReportTitle () {
        WebElement title = waitForElement (reportTitle);
        return title.getText ();
    }

    public boolean selectRandomItemFromDropDownIfAny (String name) {
        if (!openListBox (name))
            return false;

        return selectRandomListItemFromOpenWebixList ();
    }

    public boolean selectGivenItemFromDropDownIfAny (String listName, String itemName) {

        if (!openListBox (listName))
            return false;

        return selectGivenListItemFromOpenWebixList (itemName);
    }

    private int getReportColumnCount () {
        return findElements (reportColumnHeaders).size ();
    }

    private int getReportRowsCount () {
        return findElements (reportRows).size ();
    }

    private boolean isNoDataAvailable () {
        return (findElements (noDataAvailable).size () == 1 && findElements (noDataAvailable).get (0).isDisplayed ());
    }

    public boolean isReportLoaded () {
        // String reportDiv = format(reportGridXPath, reportGridItemId);
        return (getReportColumnCount () > 0 && (getReportRowsCount () > 0 || isNoDataAvailable ()));
    }

    public DataViewPage clickOptionsOfReport (String rowIndex) {
        // String clickOptionOfReport = format("//div[@column='2']/div[@aria-rowindex='%s']/div",
        // rowIndex);
        // waitForElementVisible(clickOptionOfReport);
        // assertTrue(click(clickOptionOfReport));
        assertTrue (click ("div[itemid='BUTTON_3e34b370'] button"));
        return this;
    }

    public DataViewPage clickTopEllipsisAndSave () {
        assertTrue (click ("//div[@itemid='BUTTON_3e34b370']//button/i"));
        assertTrue (click (
                           "(//ul[@class='dropdown-menu dropdown-menu-left show']//child::span[contains(text(),'Save as New')])"));
        return this;
    }

    public DataViewPage selectProductForReport (String product) {
        assertTrue (click ("//div[@itemid='MENU_8f3d4a22']"));
        String el = "//div[@class='webix_view webix_window webix_popup' and contains(@style,'display: block;')]//following::div[contains(text(),'DATAview')]//span";
        if (!findElement (el).isSelected ())
            assertTrue (click (el));
        assertTrue (click ("//div[@itemid='MENU_8f3d4a22']"));
        return this;
    }

    public String clickOptionsOfReportAndReturnSelectedReportName (String rowIndex) {
        String clickOptionOfReport = format ("//div[@column='2']/div[@aria-rowindex='%s']/div", rowIndex);
        assertTrue (isElementPresent (clickOptionOfReport));
        String selectedReport = getText (clickOptionOfReport);
        assertTrue (click (clickOptionOfReport));
        waitForElementInvisible ("(//span[@class='loadingOverlay'])[1]");
        return selectedReport;
    }

    public DataViewPage clickSaveAsNew (String rowIndex) {
        doWait (3000);
        String saveAsNew = format ("//div[@aria-rowindex='%s']/div/div/ul/li/a[text()='Save as New']", rowIndex);
        assertTrue (click (saveAsNew));
        return this;
    }

    public DataViewPage copyReport (String reportNameForCopy) {
        String reportNameInput = "div[itemid='TEXT_INPUT_10132273422594585'] input";
        String reportName = getText (reportNameInput);
        clear (reportNameInput);
        setText (reportNameInput, reportName + reportNameForCopy);
        assertTrue (click (findElement ("//span[text()='CONFIRM']")));
        waitForElementInvisible (pleaseWait);
        doWait (5000);
        navigateToTab (1);
        // waitForElementVisible(format("(//p[contains(text(),'%s')])[1]",
        // reportNameForCopy));
        navigateToTab (0);
        // ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        // System.out.println("tabs size "+tabs);
        // if (tabs.size() > 0) {
        // driver.switchTo().window(tabs.get(1));
        // waitForElementVisible(format("//p[@title='%s']", reportNameForCopy));
        // driver.switchTo().window(tabs.get(0));
        // }
        return this;
    }

    public String selectCreatedByMeOption (String reportNameForCopy) {
        selectOptionFromDropdownMenu ("MENU_86466648522318", "      -- Created by Me");
        waitForElementInvisible (loadingIcon);
        String addedReport = format ("//span[text()='%s']/parent::div", reportNameForCopy);
        String addedReportIndex = findElement (addedReport).getAttribute ("aria-rowindex");
        return addedReportIndex;
    }

    public DataViewPage selectCreatedByMeOption () {
        selectOptionFromDropdownMenu ("MENU_86466648522318", "      -- Created by Me");
        waitForElementInvisible (loadingIcon);
        return this;

    }

    public DataViewPage selectValueFromDropdown (String value) {
        assertTrue (click ("[itemid='MENU_86466648522318']"));
        assertTrue (click (format ("//div[@role='option'][contains(text(),'%s')]", value)));
        waitForElementInvisible (loadingIcon);
        return this;
    }

    public DataViewPage removeCopiedReport () {
        assertTrue (click ("//li//span[contains(text(),'Remove')]"));
        assertTrue (click ("//button[text()='Confirm']"));
        System.out.println ("Removed copied report");
        return this;
    }

    public DataViewPage removeCopiedReport (String rowIndex) { // overload
        String remove = format ("//div[@role='gridcell' and @aria-rowindex='%s']/div/div/ul/li/a[text()='Remove']",
                                rowIndex);
        assertTrue (click (remove));
        assertTrue (click ("//button[text()='Confirm']"));
        System.out.println ("Removed copied report");
        return this;
    }

    public DataViewPage clickFirstBuiltInTemplate () {
        assertTrue (click ("[itemid^='GRID_LAYOUT_COLUMN_1636381881476_0__TCCREPEATERROW0_']"));
        return this;
    }

    public DataViewPage enterReportName (String name) {
        clear ("//div[@itemid='TEXT_INPUT_10132273422594585']//descendant::input");
        assertTrue (setText ("//div[@itemid='TEXT_INPUT_10132273422594585']//descendant::input", name));
        return this;
    }

    public boolean isReportPublished () {
        return (isElementPresent (
                                  "(//ul[@class='dropdown-menu grid-navbuttoninlinecaret']//child::a[text()='Unpublish'])[1]"));

    }

    public boolean isReportPublishedAndFeatured () {
        if (isElementPresent (
                              "(//ul[@class='dropdown-menu grid-navbuttoninlinecaret']//child::a[text()='Unpublish'])[1]") && isElementPresent (
                                                                                                                                                "(//ul[@class='dropdown-menu grid-navbuttoninlinecaret']//child::a[text()='Unfeature'])[1]"))
            return true;
        else
            return false;
    }

    public DataViewPage clickDesignInTheReportOptionsAndVerify () {
        assertTrue (click ("//div[@itemid='BUTTON_3e34b370']//button/i"));
        assertTrue (click (
                           "(//ul[@class='dropdown-menu dropdown-menu-left show']//child::span[contains(text(),'Design')])"));
        navigateToTab (1);
        waitForElementVisible ("button#handle-show-widget-borders");
        assertTrue (isElementPresent ("button#handle-show-widget-borders"));
        DriverManager.getDriver ().close ();
        navigateToTab (0);
        return this;
    }

    public DataViewPage clickRenameTagInTheReportOptions () {
        assertTrue (click ("//div[@itemid='BUTTON_3e34b370']//button/i"));
        assertTrue (click (
                           "(//ul[@class='dropdown-menu dropdown-menu-left show']//child::span[contains(text(),'Rename/Tag')])"));
        return this;
    }

    public DataViewPage clickReportDetailsInTheReportOptions () {
        assertTrue (click ("//div[@itemid='BUTTON_3e34b370']//button/i"));
        assertTrue (click (
                           "(//ul[@class='dropdown-menu dropdown-menu-left show']//child::span[contains(text(),'Report Details')])"));

        assertTrue (click (infoButton));

        return this;
    }

    public DataViewPage clickScheduleEmailDeliveryInTheReportOptions () {
        assertTrue (click ("//div[@itemid='BUTTON_3e34b370']//button/i"));
        assertTrue (click (
                           "(//ul[@class='dropdown-menu dropdown-menu-left show']//child::span[contains(text(),'Schedule E-mail Delivery')])"));

        return this;
    }

    public DataViewPage clickSaveAsNewInTheReportOptions () {
        assertTrue (
                    click ("(//ul[@class='dropdown-menu grid-navbuttoninlinecaret']//child::a[text()='Save as New'])[1]"));
        waitForElementVisible ("[itemid=\"GRID_LAYOUT_10131700182734207\"]");
        return this;
    }

    public DataViewPage clickShareInTheReportOptions (String rowIndex) {
        assertTrue (click (format (
                                   "//div[@aria-rowindex='%s']//ul[@class='dropdown-menu grid-navbuttoninlinecaret']//child::a[text()='Share']",
                                   rowIndex)));
        waitForElement ("//div[@class='modal-content']//h5[contains(text(), 'Share Report')]");
        return this;
    }

    public DataViewPage clickPublishInTheReportOptionsAndConfirm (String rowIndex) {
        assertTrue (click (format (
                                   "//div[@aria-rowindex='%s']//ul[@class='dropdown-menu grid-navbuttoninlinecaret']//child::a[text()='Publish']",
                                   rowIndex)));
        assertTrue (click ("//div[@class='modal-footer ui-draggable-handle']//button[contains(text(),'Confirm')]"));
        waitForElement ("//div[contains(text(), 'Report Published')]");
        assertTrue (click ("//div[@class='modal-footer ui-draggable-handle']//button"));
        return this;
    }

    public DataViewPage clickFeatureInTheReportOptionsAndConfirm (String rowIndex) {
        assertTrue (click (format (
                                   "//div[@aria-rowindex='%s']//ul[@class='dropdown-menu grid-navbuttoninlinecaret']//child::a[text()='Feature']",
                                   rowIndex)));
        assertTrue (click ("//div[@class='modal-footer ui-draggable-handle']//button[contains(text(),'Confirm')]"));
        waitForElement ("//div[contains(text(), 'Report featured')]");
        assertTrue (click ("//div[@class='modal-footer ui-draggable-handle']//button"));
        return this;
    }

    public DataViewPage clickOnreport () {
        assertTrue (click (
                           "(//div[@class='webix_column webix_header_valign-top ']//child::span[@class='custom_renderer_click'])[1]"));
        waitUntilVisible ("[itemid='GRID_LAYOUT_COLUMN_1580134370037_d5acb51d']");
        return this;
    }

    public DataViewPage clickOnreport (String reportName) {
        doWait (2000);
        assertTrue (click (format (reportNameGridCell, reportName)));
        if (!waitUntilVisible ("[itemid ='GRID_LAYOUT_7042492463850095']"))
            clickUsingJs (findElement (locateBy ("[itemid ='GRID_LAYOUT_7042492463850095']")));
        return this;
    }

    public DataViewPage chooseFilterAndEnterValue (String value, String text) {
        assertTrue (click ("(//div[@class='webix_inp_static'])[4]"));
        assertTrue (click (format ("div[webix_l_id='%s'][class^='webix_list_item']", value)));
        assertTrue (setText ("//div[@class='webix_view webix_control webix_el_text']//input", text));
        return this;
    }

    public DataViewPage clickApply () {
        assertTrue (click ("//button[@class='webix_button' and text()='Apply']"));
        return this;
    }

    public DataViewPage clickSearchButtonOnSharePopUp () {
        doWait (3000);
        assertTrue (click (sharePopUpSearchButton));
        waitForElement ("//h5[contains(text(), 'Search')]");
        return this;
    }

    public DataViewPage clickOnSearchAndSelectUserByUserNameInSearchPopUp (String userName) {
        findElement ("//input[@inputid='TEXT_INPUT_1517540313596_copy_245_copy_60']").sendKeys (userName);
        assertTrue (click (searchPopUpSearchButtonForSharePopUp));
        findElement (
                     "//div[@class='webix_column center left_area webix_last webix_first webix_select_mark']//div[@aria-rowindex='1']//input[@type='checkbox']")
                                                                                                                                                                .click ();
        waitForElementClickable (selectAndExitButtonOnSearchPopUp);
        assertTrue (click (selectAndExitButtonOnSearchPopUp));
        waitForElementInvisible (loadingIcon);
        return this;
    }

    public DataViewPage clickRandomReport (int rand) {
        String report = format (
                                "(//div[@class='webix_column webix_header_valign-top ']//child::span[@class='custom_renderer_click'])[%d]",
                                rand);
        assertTrue (click (report));
        waitUntilVisible ("[itemid='GRID_LAYOUT_COLUMN_1580134370037_d5acb51d']");
        return this;
    }

    public int getRandomValue () {
        return generateRandomInt (10, 1);
    }

    public DataViewPage selectReportByReportName (String reportName) {
        String xpathForReport = format (
                                        "//div[@class='webix_column webix_header_valign-top ' and @column='1']//span[contains(text(), '%s')]",
                                        reportName);
        assertTrue (isElementPresent (xpathForReport));
        assertTrue (click (xpathForReport));
        return this;
    }

    public DataViewPage selectGroupNameFilter () {
        selectRandomOptionFromDropdownMenu ("MENU_5c576f1e");
        return this;
    }

    public DataViewPage selectGroupType (String type) {
        selectOptionFromDropdownMenu ("MENU_87025967", type);
        return this;
    }

    public DataViewPage changeGroupFilter () {
        waitForElementInvisible ("(//span[@class='loadingOverlay'])[2]");
        assertTrue (click ("div[class='arrow']"));
        doWait (1000);
        assertTrue (click ("(//div[@class='grid-tcc-groupable_list_items']//input)[1]"));
        return this;
    }

    public String clickOnRandomOfReport (String rowIndex) {
        String clickOptionOfReport = format ("//div[@column='1']/div[@aria-rowindex='%s']/span", rowIndex);
        assertTrue (isElementPresent (clickOptionOfReport));
        String selectedReport = getText (clickOptionOfReport);
        assertTrue (click (clickOptionOfReport));
        waitForElementInvisible ("(//span[@class='loadingOverlay'])[1]");
        return selectedReport;
    }

    public DataViewPage clickChoosePrintOptions () {
        assertTrue (click ("div[class='buttonHolder'] button:nth-child(2)"));
        return this;
    }

    public DataViewPage clickRefreshButton () {
        assertTrue (click ("div[class='buttonHolder'] button:nth-child(1)"));
        assertTrue (isElementPresent ("(//label[@class='translatable question_title pull-left mr-2'])[3]"));
        return this;
    }

    public DataViewPage choosePrintOptions (String option) {
        String print = format ("//button//span[contains(text(),'%s')]", option);
        assertTrue (click (print));
        waitForElementInvisible (pleaseWait);
        return this;
    }

    public DataViewPage clickSettingsButton () {
        assertTrue (click ("//i[@class='fas fa-cogs']"));
        return this;
    }

    public Boolean isDesignerPageOpened () {
        navigateToTab (1);
        waitForElementInvisible (pleaseWait);
        assertTrue (isElementPresent ("(//div[@class='webix_el_box'])[2]"));
        return true;
    }

    public DataViewPage clickSearchGroupButton () {
        assertTrue (click (searchButton));
        waitForElementInvisible (pleaseWait);
        return this;
    }

    public String searchForGroup (String grp) {
        setText (searchInputFile, grp);
        assertTrue (click (searchDialogSearchButton));
        return grp;
    }

    public String checkGroupGotAddedInGrid () {
        String searchedGrp = getText (
                                      "//div[@itemid='GRID_2344943517659758']//div[@column='1']//div[@aria-rowindex='1']");
        return searchedGrp;
    }

    public DataViewPage selectAndAddSearchedGroup () {
        doWait (2000);
        assertTrue (click ("//div[@itemid='GRID_2344943517659758']//div[@column='0']/div/input"));
        assertTrue (click ("div[itemid='BUTTON_2360900231990283'] button")); // select button
        return this;
    }

    public String groupDropdownValue () {
        System.out.println ("Selected grp : " + getText ("//div[@itemid='MENU_5c576f1e']//input"));
        return getAttribute ("//div[@itemid='MENU_5c576f1e']//input", "value");
    }

    public DataViewPage selectOptionFromDropdown (String itemid, String text) {
        String xpath = "//div[@itemid='" + itemid + "']//div[@class='webix_el_box']//input[@role='combobox']";
        doWait (2000);
        assertTrue (click (xpath));
        assertTrue (selectGivenListItemFromOpenWebixList (text));
        doWait (500);
        return this;
    }

    public DataViewPage clickReset () {
        assertTrue (click ("//button//span[text()='RESET']"));
        assertTrue (isElementPresent ("//div[text()='No Records Found!']"));
        return this;
    }

    public DataViewPage clickClose () {
        assertTrue (click ("//button//span[text()='close']"));
        return this;
    }

    public DataViewPage selectPlan (String planName) {
        selectOptionFromDropdown ("MENU_a14b334c", planName);
        return this;
    }

    public DataViewPage renameReport (String newReportName) {
        System.out.println ("newReportName=" + newReportName);
        waitForElementInvisible (pleaseWait);
        assertTrue (clear (reportInput));
        assertTrue (setText (reportInput, newReportName));
        assertTrue (click (updateRenameReportButton));
        assertTrue (click (successButton));
        return this;
    }

    public DataViewPage clickReportSharingTab () {
        assertTrue (click (settingReportSharingTab));
        return this;
    }

    public DataViewPage selectCompensationProduct () {
        assertTrue (click ("div[@itemid='MENU_becd6679']//div[@class='webix_el_box']"));
        return this;
    }

    public DataViewPage selectProduct (String product) {
        assertTrue (click ("div[itemid='MENU_8f3d4a22']"));
        doWait (2000);
        WebElement el = findElement ("(//div[@webix_l_id='%s']/span[@role='checkbox'])[3]");
        String isSelected = el.getAttribute ("aria-checked");
        if (isSelected.equals ("false"))
            assertTrue (click (String.format ("(//div[@webix_l_id='%s']/span[@role='checkbox'])[3]", product)));
        if (isElementPresent ("span.loadingOverlay"))
            waitForElementInvisible ("span.loadingOverlay");
        return this;

    }

    public DataViewPage hoverOnReport (String report) {
        hover (dataViewIcon);
        assertTrue (click (String.format ("//li[contains(text(),'%s')]", report)));
        waitForElementInvisible ("span.loadingOverlay");
        assertTrue (click ("div[itemid='BUTTON_7044203639573549'] button"));
        return this;

    }

}
