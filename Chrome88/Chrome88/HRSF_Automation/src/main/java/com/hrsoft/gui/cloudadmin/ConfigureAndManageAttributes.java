/**
 * 
 */
package com.hrsoft.gui.cloudadmin;

/**
 * @author Vivek Pandey
 *
 * <a href="mailto:vivek.pandey@hrsoft.com">vivek.pandey@hrsoft.com</a>
 */
import static org.testng.Assert.assertTrue;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.hrsoft.gui.HRSoftPage;


public class ConfigureAndManageAttributes extends HRSoftPage {
    
    
    public ConfigureAndManageAttributes searchAndClickSetIDInAttributeLookupGrid (String rowvalue) {
        String xpath = "//div[@itemid='lookup_attribute_set_list']//div[@column=0]//div[text()='" + rowvalue + "']";
        System.out.println (xpath);
        waitForElement("//div[@itemid='lookup_attribute_set_list']");
        doWait (5000);
        findElement ("//div[@itemid='lookup_attribute_set_list']//input[@type='text']").clear ();
        findElement ("//div[@itemid='lookup_attribute_set_list']//input[@type='text']").sendKeys (rowvalue);
        doWait (500);
        scrollTo (xpath);
        assertTrue (isElementPresent (xpath));
        doWait (500);
        assertTrue (click (xpath));
        waitForElement ("//div[@itemid='attributes_grid']//div[@column=0]//div[@role='gridcell' and @aria-rowindex=1]");
        return this;
    }
    
    public ConfigureAndManageAttributes verifyAttributeNamePresentInAttributeLookupGrid (int colnumber, String rowvalue) {
        doWait(2000);
        String xpath = "//div[@itemid='attributes_grid']//div[@column=" + colnumber + "]//div[text()='" + rowvalue + "']";
        scrollTo (xpath);
        assertTrue (isElementPresent (xpath));
        return this;
    }
    
    public ConfigureAndManageAttributes clickOnInlineEditOfAttributeLookupGrid (String text) {
    	String path="//div[@itemid='attributes_grid']//div[@column=1]//div[text()='"+text+"']";
    	String index = findElement(path).getAttribute("aria-rowindex");
    	String xpath = "//div[@itemid='attributes_grid']//div[@column=4]//div[@aria-rowindex=" + index + "]//i[contains(@class,'fal fa-edit')]";
        waitForElementVisible(xpath);
        scrollTo(xpath);
        assertTrue (click (xpath));
        doWait(500);
        return this;
    }
    
    public String getOriginalValueOfInputboxGridInlineEdit (String itemid) {
        String xpath = "//input[contains(@id,'" + itemid + "')]";
        String originalvalue = findElement (xpath).getAttribute ("value");
        return originalvalue;
    }
    
    public ConfigureAndManageAttributes passNewValueInputBoxGridInlineEdit (String itemid, String text) {
        String xpath = "//input[contains(@id,'" + itemid + "')]";
        findElement (xpath).clear ();
        findElement (xpath).sendKeys (text);
        doWait (500);
        return this;
    }
    
    public ConfigureAndManageAttributes clickSaveAndCloseEditRecordPopupOfGrid () {
        doWait (1000);
        System.out.println (findElement ("//div[@class='modal-footer ui-draggable-handle']//button[text()='Save & Close']").getSize ());
        assertTrue (click ("//div[@class='modal-footer ui-draggable-handle']//button[text()='Save & Close']"));
        doWait (1000);
        return this;
    }
    
    public String getOriginalValueOfAttributesEditPopup (String itemid) {
        String xpath = "//div[@itemid='" + itemid + "']//input";
        System.out.println (xpath);
        String originalvalue = findElement (xpath).getAttribute ("value");
        return originalvalue;
    }
    
    public ConfigureAndManageAttributes passNewValueInputBoxAttributesEditPopup (String itemid, String text) {
        String xpath = "//div[@itemid='" + itemid + "']//input";
        findElement (xpath).clear ();
        findElement (xpath).sendKeys (text);
        doWait (500);
        return this;
    }
    
    public ConfigureAndManageAttributes clickSaveAndCloseEditRecordPAttributesEditPopup() {
        doWait (1000);
        System.out.println (findElement ("//div[@itemid='BUTTON_1151805988370572']//button").getSize ());
        assertTrue (click ("//div[@itemid='BUTTON_1151805988370572']//button"));
        doWait (1000);
        return this;
    }
    
    public ConfigureAndManageAttributes verifyAttributeValuePresentInAttributeLookupGrid (int colnumber, String rowvalue) {
        doWait(2000);
        String xpath = "//div[@itemid='attributes_grid']//div[@column=" + colnumber + "]//span[text()='" + rowvalue + "']";
        scrollTo (xpath);
        assertTrue (isElementPresent (xpath));
        return this;
    }
}
