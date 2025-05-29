package com.hrsoft.gui.compensation;

import static java.lang.String.format;
import static org.testng.Assert.assertTrue;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;
import com.hrsoft.utils.seleniumfy.BasePage;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */
public class PlanOverviewPage extends BasePage {

    protected String pleaseWait = "(//h4[@id='waitWinMessage1'])[1]";

    @Override
    protected ExpectedCondition getPageLoadCondition () {
        return ExpectedConditions.visibilityOfElementLocated (locateBy ("#overview_2"));
    }

    public PlanOverviewPage navigatedPlanToOverviewPage () {
        doWait (3000);
        switchToInnerFrame ();
        assertTrue (waitUntilVisible ("#overview_2"));
        return this;
    }

    public PlanOverviewPage goToCompensation () {
        assertTrue (click ("//div[text()='Compensation']"));
        assertTrue (waitForElementInvisible (pleaseWait));
        hasPageLoaded ();
        return this;
    }

    public PlanOverviewPage clickReset () {
        switchToInnerFrame ();
        assertTrue (click ("#btnResetSelections"));
        assertTrue (waitForElementInvisible (pleaseWait));
        return this;
    }

    public PlanOverviewPage selectGroupInSelections (String select) {
        switchToInnerFrame ();
        clickAndSelectText ("//select[@class='selectGroup wstmSelect d-inline-block float-left']", select);
        return this;
    }

    public PlanOverviewPage clickSearchAndChangeGroup () {
        switchToInnerFrame ();
        assertTrue (click ("//a[contains(text(),'Search and Change Group')]"));
        return this;
    }

    public PlanOverviewPage selectFilterInSelections (String select) {
        switchToInnerFrame ();
        clickAndSelectText ("//select[@class='selectFilters wstmSelect d-inline-block float-left']",
                            select);
        hasPageLoaded ();
        return this;
    }

    public String getEmployeeCount (String group) {
        return getText (format ("(//a[contains(text(),'%s')]/preceding::td[@typecode='WS_Group_Size'])[1]", group));
    }

    public PlanOverviewPage clickManageFilters () {
        switchToInnerFrame ();
        assertTrue (click ("#manageFilters"));
        assertTrue (waitForElementInvisible (pleaseWait));
        return this;
    }

    public PlanningPage clickPlanRecomendations () {
        switchToInnerFrame ();
        assertTrue (click ("//a[@id='Planning_link_A'][text()='PLAN Recommendations'] "));
        assertTrue (waitForElementInvisible (pleaseWait));
        return new PlanningPage ();
    }

    public boolean isPlanningPageWithSelectedFilter (String filter) {
        switchToInnerFrame ();
        return isElementVisible (format ("//span[contains(text(),'%s')]", filter));

    }

    public PlanOverviewPage clickReviewRecomendations () {
        switchToInnerFrame ();
        assertTrue (click ("//a[@id='Reviewing_link_A'][text()='REVIEW Recommendations']"));
        assertTrue (waitForElementInvisible (pleaseWait));
        hasPageLoaded ();
        return this;
    }

    public PlanOverviewPage clickDistibuteFundsLink () {
        switchToInnerFrame ();
        assertTrue (click ("//div[text()='Links']/..//a[@id='Funding_link_A']"));
        assertTrue (waitForElementInvisible (pleaseWait));
        hasPageLoaded ();
        return this;
    }

    public PlanOverviewPage clickDirect () {
        assertTrue (click ("//input[@id='scope1']/parent::label"));
        hasPageLoaded ();
        return this;
    }

    public PlanOverviewPage clickRollup () {
        switchToInnerFrame ();
        assertTrue (click ("//input[@id='scope2']/parent::label"));
        hasPageLoaded ();
        return this;
    }

    public PlanOverviewPage selectGroup (String groupName) {
        switchToOuterFrame ();
        assertTrue (setText ("#txt_WS_Group_Name", groupName));
        assertTrue (click ("(//td[text()='Group Name']/following::input[@class='seachAction pbtn'])[1]"));
        assertTrue (waitForElementInvisible ("//td[text()='Loading...']"));
        assertTrue (click (format ("//span[contains(text(),'%s')]/preceding::input[@class='sp_checkbox']", groupName)));
        assertTrue (click ("//input[@value='Accept']"));
        assertTrue (waitForElementInvisible (pleaseWait));
        return this;
    }

    public String getDropdowntext () {
        switchToInnerFrame ();
        return getText ("//select[@class='selectGroup wstmSelect d-inline-block float-left']");
    }

    public PlanOverviewPage clickFundingTab () {
        doWait (3000);
        switchToInnerFrame ();
        String el ="//a[@href='#WS_Funding']";
        if (!isElementPresent (el))
            throw new SkipException ("Funding tab is not present");
        assertTrue (click (el));
        return this;
    }

    public boolean isCascadingBudgetVisible () {
        switchToInnerFrame ();
        return findElement ("//span[@wstmtitle='Cascading Budget']").isDisplayed ();
    }

    public PlanOverviewPage clickPlanningBudget () {
        switchToInnerFrame ();
        assertTrue (click ("//a[@href='#WS_Planning_Budget']"));
        return this;
    }

    public boolean isRuleBasedBudgets () {
        switchToInnerFrame ();
        String el ="//span[text()='Promotion and Market']";
        if (!isElementPresent (el))
            throw new SkipException ("Promotion and Market is not present");
        return findElement (el).isDisplayed ();
    }

    public void clickReviewingInStatusTab () {
    	doWait(1000);
        switchToInnerFrame ();
        assertTrue (click ("a[href='#WS_Reviewing']"));
    }

    public void clickPlanningStatusInStatusTab () {
        switchToInnerFrame ();
        assertTrue (click ("a[href='#WS_Planning_Status']"));
    }

    public String getEmployeeCountFromReviewingStatusTab () {
        switchToInnerFrame ();
        return getText ("//table[@componentid='reviewingSnapshot']//following::span[@wstypecode='WS_Group_Size']");
    }

    public String getEmployeeCountFromPlanningStatusTab () {
        switchToInnerFrame ();
        return getText ("//table[@componentid='planningStatusSnapshot']//following::span[@wstypecode='WS_Num_Employees']");
    }
}
