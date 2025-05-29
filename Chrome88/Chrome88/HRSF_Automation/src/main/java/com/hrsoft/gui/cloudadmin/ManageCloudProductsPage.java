package com.hrsoft.gui.cloudadmin;

import static org.testng.Assert.assertTrue;
import java.util.List;
import org.openqa.selenium.WebElement;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */
public class ManageCloudProductsPage extends CloudAdminPage {

    private String searchbox = "(//div[@class='webix_el_box']//input[@role='combobox'])";
    /* Manage Comp Hierarchy */
    private String cloudAdmin           = "//span[text()='Cloud Admin']";
    private String ManageCompHierCard   = "//div[@itemid='GRID_LAYOUT_1517094953183_copy_62ec09f2']";
    private String clickDefHierOk       = "//button[text()='OK']";
    private String clickHierarchyIcon   = "//div[@itemid='relationships_list_copy_3c55660d']/div/div/div/div//input";
    private String selectSmokeHierarchy = "//div[@webix_l_id='smoketest Hierarchy- David Kennedy Grp']";

    public void selectHrSoftCloud () {
        assertTrue (click (searchbox + "[2]"));
        assertTrue (clear (searchbox + "[2]"));
        assertTrue (click ("//strong[text()='HRsoft Cloud']"));
        doWait (2000);

    }

    public void searchChannel_1_TestAutomation () {
        assertTrue (setText ("//input[@placeholder='Search Application here...']",
                             "TEST_AUTOMATION_CHANNEL_1_RENDERING"));
        doWait (2000);
    }

    public void clickRun () {
        assertTrue (click ("//i[@class='fal fa-play']"));
        navigateToTab (2);
    }

    public List <WebElement> getSensitivityButtons () {
        return findElements ("//span[contains(text(),'BUTTON_')]");
    }

             /* For Comp Hierarchy */
    
    public void AssigningCoPlannerInManageCompHierarchy() {
    	assertTrue(click(cloudAdmin));
    	assertTrue(click(ManageCompHierCard));
    	assertTrue(click(clickDefHierOk));
    	assertTrue(click(clickHierarchyIcon));
    	assertTrue(click(selectSmokeHierarchy));
    	
    }


    public List <WebElement> getColumnNames () {
        return findElements ("//div[contains(text(),' Column')]");
    }

    public List <WebElement> getColumnValues () {
        return findElements ("//div[contains(text(),' Column')]/following::div[text()='1']");
    }

}
