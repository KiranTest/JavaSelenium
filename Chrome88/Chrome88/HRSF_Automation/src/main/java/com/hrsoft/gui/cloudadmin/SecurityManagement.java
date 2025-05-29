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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
public class SecurityManagement extends HRSoftPage{
    public SecurityManagement clickOnEmployeeHierarchies() {
        assertTrue(click("//div[@itemid='application_role_icon_copy_7_copy_12_copy_198540316314848']"));
        return this;
    }
    public SecurityManagement detachAndRecalculateHierarchy(String hierarchyName) {
        //select in dropdown
        String xpathForDropdown = "//div[@itemid='GRID_4815958281648780']//div[text()=' Hierarchy']/ancestor::div[@column='3']/following-sibling::div//select";
        assertTrue(click(xpathForDropdown));
        String xpathForHierarchyOption = xpathForDropdown + "//option[text()='" + hierarchyName + "']";
        scrollTo(By.xpath(xpathForHierarchyOption));
        assertTrue(click(By.xpath(xpathForHierarchyOption)));
        String xpathForDetach = "//div[@itemid='GRID_4815958281648780']//div[@class='webix_ss_left']//div[@aria-rowindex='2']//i[contains(@class,'far fa-trash-alt')]";
        assertTrue(click(By.xpath(xpathForDetach)));
        //confirming delete
        assertTrue(click("//button[text()='Confirm']"));
        //confirming recalculation of hierarchies
        assertTrue(click("//button[text()='Confirm']"));
        assertTrue(click("//button[text()='OK']"));
        doWait(3000);
        return this;
    }
    public SecurityManagement deleteHierarchy(String hierarchyName) {
        String xpathForHierarchyRow = "//div[@itemid='GRID_4042829998209810']//div[@class='webix_ss_center']//div[@column='1']//div[text()='"+hierarchyName+"']";
        String ariaRowIndex = findElement(xpathForHierarchyRow).getAttribute("aria-rowindex");

        String xpathForHierarchyDeleteOption = "//div[@itemid='GRID_4042829998209810']//div[@class='webix_ss_left']//div[@column='0']//div[@aria-rowindex='"+ariaRowIndex+"']//i[@class='fal fa-trash']";
        assertTrue(click(By.xpath(xpathForHierarchyDeleteOption)));
        assertTrue(click("//button[text()='Confirm']"));
        waitForElementVisible("//div[@class='modal-dialog']//div[text()='Server returning following message- Hierarchy deleted successfully!']");
        assertTrue(click("//button[text()='OK']"));
        doWait(2000);
        return this;
    }
}
