package com.hrsoft.gui.cloudadmin;

import static java.lang.String.format;
import static org.testng.Assert.assertTrue;
import static com.hrsoft.reports.ExtentLogger.info;
import java.util.*;
import java.util.stream.Collectors;

import com.hrsoft.annotations.Author;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import static com.hrsoft.driver.DriverManager.getDriver;
import com.hrsoft.gui.HRSoftPage;

public class ManageUsersPage extends  HRSoftPage{
    private String userNameTextField = "//div[@itemid='TEXT_INPUT_558092497966689']//input";
    private String statusFilter = "//div[@itemid='MENU_558724223115454']//div[@class='webix_inp_static']";
    private String hierarchyDropDown = "//div[@itemid='MENU_1518635719828_copy_28_copy_758eb64b']//input/following-sibling::span";

    //public List<String>, String hierarchyName

    /**
     * @author Hera Aijaz
     * <a href="mailto:hera.aijaz@hrsoft.com">hera.aijaz@hrsoft.com</a>
     */
    public ManageUsersPage setUserNameInInputField (String userName) {
        doWait(500);
        findElement(userNameTextField).sendKeys(userName);
        return this;
    }
    public ManageUsersPage selectAllStatusUncheckedCheckbox () {
        click(statusFilter);
        String[] empStatusesWebixLId = {"A", "L", "T"};
        for (String empStatus: empStatusesWebixLId) {
            String xpath = "//div[@class='webix_view webix_window webix_popup']//div[@webix_l_id='" +empStatus+ "' and @tabindex='-1']";
            try {
                if (isElementPresent(xpath)) {
                    xpath += "//span";
                    assertTrue(click(xpath));
                }
            } catch (NoSuchElementException e) {
                System.out.println(empStatus + "is checked already");
            }

        }
        return this;
    }
    public ManageUsersPage selectHierarchyFromDropDown (String hierarchyName) {
        click(hierarchyDropDown);
        scrollTo(By.xpath("//div[@class='webix_view webix_window webix_popup']//div[ text()='"+hierarchyName+"']"));
        assertTrue(click("//div[@class='webix_view webix_window webix_popup']//div[ text()='"+hierarchyName+"']"));
        return this;
    }
    public void clickOnSearchButton() {
        assertTrue(click("//div[@itemid='BUTTON_560465135540452']//button"));
    }
    /**
     * @author Hera Aijaz
     * <a href="mailto:hera.aijaz@hrsoft.com">hera.aijaz@hrsoft.com</a>
     */
    public ManageUsersPage clickOnEditOfFirstRowInSearchResults () {
        waitForElementVisible("//div[@itemid='SECTION_555047826535873']");
        assertTrue(click("//div[@class='webix_view webix_dtable text-none']//div[@column=0]//div[@aria-rowindex='1']//a[@title='Edit']"));
        return this;
    }
    /**
     * @author Hera Aijaz
     * <a href="mailto:hera.aijaz@hrsoft.com">hera.aijaz@hrsoft.com</a>
     */
    public String getEmpStatusOfUserFromEditPopUp () {
        waitForElementVisible("//div[@itemid='66e598eb-ae53-433d-a5bb-8148fff42944_msgSection']");
        return findElement("//div[@itemid='MENU_1519079699560']//input").getAttribute("value");
    }



    public ManageUsersPage searchNameInManageUsers(String name) {
        assertTrue(setText("//input[contains(@id,'TEXT_INPUT_558092497966689')]", name));
        assertTrue(click("//button[contains(@id,'BUTTON_560465135540452')]"));
        return this;
    }
}