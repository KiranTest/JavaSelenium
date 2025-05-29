package com.hrsoft.gui.compensation;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static java.lang.String.format;
import static org.testng.Assert.assertTrue;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.utils.seleniumfy.BasePage;

public class DistributeFundPage extends BasePage {

    private String distFundpageTitle     = "//div[@id='pageTitle']/following::span[contains(text(),'Distribute Funds')]";
    private String confirmApply          = "//input[@value='Apply']";
    private String distAmountFromTop     = "//span[@wstypecode='WS_Budget_Distributed_Amount']";
    private String budgetAmountFromTop   = "//span[@wstypecode='WS_Budget_GrpStats_Total_Amount']";
    private String holdBackAmountFromTop = "//span[@wstypecode='WS_Budget_Holdback_Amount']";

    /* Table Rows */
    private String tblRows               = "//table[@id='gridBody']/tbody/tr";
    private String budgetAmtFromTbl      = "//table/tbody/tr/td[@wsmeta='Amount']/input";
    private String tblForApplyBtn        = "//table/tbody/tr/td/button[@title='Apply' or @value='Apply']";
    private String confirmFundingInp     = "//body[@id='tinymce']";

    public DistributeFundPage (HRSoftPage hrSoftPage) {
        this.hrSoftPage = hrSoftPage;
    }

    @Override
    protected ExpectedCondition getPageLoadCondition () {
        return ExpectedConditions.visibilityOfElementLocated (locateBy (distFundpageTitle));
    }

    public String getDistributedAmountFromTopTable () {
        switchToDefaultContent ();
        switchToInnerFrame ();
        String distAmtFromTop = format (getText (distAmountFromTop));
        return distAmtFromTop;
    }

    public String getBudgetAmountFromTopTable () {
        switchToDefaultContent ();
        switchToInnerFrame ();
        String budgetAmtFromTop = format (getText (budgetAmountFromTop));
        return budgetAmtFromTop;
    }

    public String getHoldBackAmtFromTopTable () {
        switchToDefaultContent ();
        switchToInnerFrame ();
        String holdBackAmtFromTop = format (getText (holdBackAmountFromTop));
        return holdBackAmtFromTop;
    }

    public String getOldBudgetVal (int rowNum) {
        switchToOuterFrame ();
        switchToSupportingFrame ();
        String distFundTblRow = format ("//table/tbody/tr[@originalrowpos='%s']/td/input[@wsmeta='Amount']",
                                        rowNum);
        String oldBudgetVal = getAttribute (distFundTblRow, "value");
        return oldBudgetVal;
    }

    public String enterNewBudgetVal (int rowNum, String setRandomValue) {
        switchToOuterFrame ();
        switchToSupportingFrame ();
        String distFundTblRow = format ("//table/tbody/tr[@originalrowpos='%s']/td/input[@wsmeta='Amount']",
                                        rowNum);
        clear (distFundTblRow);
        setText (distFundTblRow, setRandomValue);
        clickOnBlankSpace ();
        String newBudgetVal = getAttribute (distFundTblRow, "value");
        doWait (3000);
        return newBudgetVal;
    }

    public String getGroupNames (WebElement row) {
        return row.findElement (By.xpath ("td[@typecode='WS_Group_Title']/span")).getText ();
    }

    public DistributeFundPage clickIsVisible (WebElement row) {
        doWait (500);
        WebElement isVisible = row.findElement (By.xpath ("td[@typecode='WS_Budget_Is_Visible']/input"));
        if (isVisible.isSelected ()) {} else {

            isVisible.click ();
        }
        return this;

    }

    public DistributeFundPage clickIsDist (WebElement row) {
        doWait (500);
        WebElement isDistributeFurther = row.findElement (By.xpath ("td[@typecode='WS_Can_Distribute_Budget']/input"));
        if (isDistributeFurther.isSelected ()) {} else {
            isDistributeFurther.click ();
        }
        return this;
    }

    public DistributeFundPage clearBudgetInput (WebElement row) {
        doWait (1000);
        row.findElement (By.xpath (budgetInput ())).clear ();
        return this;
    }

    public void setBudgetInput (WebElement row, String random) {
        doWait (1000);
        WebElement setBudget = row.findElement (By.xpath (budgetInput ()));
        setText (setBudget, random);
        // clickOnBlankSpace();

    }

    public Integer getBudgetInput (WebElement row) {
        String enteredBudgetValue = row.findElement (By.xpath (budgetInput ())).getAttribute ("value");
        int budgetConvert = Integer.parseInt (enteredBudgetValue) / 1;
        return budgetConvert;
    }

    public String getCurrencyCode (WebElement row) {
        String currencyCode = row.findElement (By.xpath (budgetInput ())).getAttribute ("currencycode");
        return currencyCode;

    }

    public DistributeFundPage clickApplyBtn (WebElement row) {
        assertTrue (click (row.findElement (By.xpath ("td/button[@title='Apply']"))));
        return this;
    }

    public DistributeFundPage applyChanges (int rowNum) {
        switchToOuterFrame ();
        switchToSupportingFrame ();
        String clickApply = format ("//table/tbody/tr[@originalrowpos='0']/td/button[@title='Apply']",
                                    rowNum);
        assertTrue (click (clickApply));
        doWait (2000);
        switchToOuterFrame ();
        waitForElementVisible (confirmApply);
        assertTrue (click (confirmApply));
        doWait (2000);
        acceptAlertWithoutException ();
        switchToInnerFrame ();
        return this;
    }

    public String getNewDistributedAmount () {
        switchToDefaultContent ();
        switchToInnerFrame ();
        String newDistAmount = format (getText (distAmountFromTop));
        return newDistAmount;
    }

    public String getNewHoldBackAmt () {
        switchToDefaultContent ();
        switchToInnerFrame ();
        String newHoldBackAmt = format (getText (holdBackAmountFromTop));
        return newHoldBackAmt;
    }

    public List <WebElement> getTblElements () {
        switchToSupportingFrame ();
        return findElements (tblRows);
    }

    public String groupName () {
        String name = "td[@typecode='WS_Group_Title']/span";
        return name;
    }

    public String isVisible () {
        String visible = "td[@typecode='WS_Budget_Is_Visible']/input";
        return visible;
    }

    public String canDistribute () {
        String canDist = "td[@typecode='WS_Can_Distribute_Budget']/input";
        return canDist;
    }

    public String budgetInput () {
        String budget = "td[@typecode='WS_Budget_GrpStats_Total_Amount']/input";
        return budget;
    }

    public List <WebElement> getbudgetAmtFromTbl () {
        switchToOuterFrame ();
        switchToFrame ("//iframe[@id='supporting_data_frame']");
        return findElements (budgetAmtFromTbl);
    }

    public List <WebElement> tblForApplyBtn () {
        switchToOuterFrame ();
        switchToFrame ("//iframe[@id='supporting_data_frame']");
        return findElements (tblForApplyBtn);
    }

    public DistributeFundPage applyBudgetAmt () {
        switchToOuterFrame ();
        doWait (5000);
        assertTrue (click (confirmApply));
        doWait (1000);
        acceptAlertWithoutException ();
        assertTrue (hasPageLoaded ());
        doWait (2000);
        return this;
    }

    public void cancelProxyUser () {
        hrSoftPage.cancelProxyViaIcon ();
        String cancellingProxy = format ("//div[text()='Admin User']')]");
        waitForElementInvisible (cancellingProxy);

    }

    public void proxyAsGrpName (String proxyAsGrpName, String grpFullName) {
        switchToDefaultContent ();
        hrSoftPage.clickProxyAsAndEnterUserId (proxyAsGrpName);
        hrSoftPage.clickProxySubmitButton ();
        assertTrue (hasPageLoaded ());
        String proxiedGrpName = format ("//div[text()='%s']", grpFullName);
        waitForElementVisible (proxiedGrpName);
    }

    public String budgetAmtAfterProxy () {

        String budgetAmtForVerification = format ("//span[@wstypecode='WS_Budget_GrpStats_Total_Amount']");
        String assertBudgetAmt = getText (budgetAmtForVerification);
        System.out.println ("budgetAmtForVerification" + assertBudgetAmt);
        return assertBudgetAmt;
    }

    public Double convertStringToDouble (String str) {
        StringBuffer number = new StringBuffer ();

        for (int i = 0; i < str.length (); i++) {
            if (Character.isDigit (str.charAt (i))) {
                number.append (str.charAt (i));
            } else if (Character.isAlphabetic (str.charAt (i))) {
                continue;
            } else {
                number.append (str.charAt (i));
            }
        }
        Double num = Double.parseDouble (number.toString ().trim ());

        return num;
    }

    public DistributeFundPage uncheckDistributeFurther (WebElement row) {
        doWait (500);
        WebElement isDistributeFurther = row.findElement (By.xpath ("td[@typecode='WS_Can_Distribute_Budget']/input"));
        if (isDistributeFurther.isSelected ()) {
            isDistributeFurther.click ();
        } else {

        }
        return this;
    }

    public int getBudgetValueForRollup (WebElement row) {
        String enteredBudgetValue = row.findElement (By.xpath (budgetInput ())).getAttribute ("value");
        int budgetConvert = Integer.parseInt (enteredBudgetValue);
        return budgetConvert;
    }

}
