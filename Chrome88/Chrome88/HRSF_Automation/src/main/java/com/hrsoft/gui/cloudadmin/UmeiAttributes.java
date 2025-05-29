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

public class UmeiAttributes extends HRSoftPage {

	public UmeiAttributes clickOnInlineEditOfUmeiAttributeGrid(String attributeName) {
		String xpath = "//div[@itemid='attributes_98725793']//div[@class='webix_ss_body']//div[@column=1]//div[text()='"
				+ attributeName + "']";
		String index = findElement(xpath).getAttribute("aria-rowindex");
		assertTrue(click("//div[@itemid='attributes_98725793']//div[@aria-rowindex=" + index + "]//a[@title=' Edit']"));
		click("//button[text()='Confirm']");
		doWait(500);
		return this;
	}

	public UmeiAttributes verifyAttributeValuePresentInUmeiAttribute(String attributeName, String rowvalue) {
		String xpath = "//div[@itemid='attributes_98725793']//div[@class='webix_ss_body']//div[@column=1]//div[text()='"
				+ attributeName + "']";
		String index = findElement(xpath).getAttribute("aria-rowindex");
		String xpath_row = "//div[@itemid='attributes_98725793']//div[@column=2]//div[@aria-rowindex=" + index
				+ " and text()='" + rowvalue + "']";
		scrollTo(xpath_row);
		assertTrue(isElementPresent(xpath_row));
		return this;
	}

	/**
	 * @author Hera Aijaz
	 *         <a href="mailto:hera.aijaz@hrsoft.com">hera.aijaz@hrsoft.com</a>
	 */
	public boolean isAttributeValuePresentInUmeiAttribute(String attributeName, String attributeValue) {
		String xpathName = "//div[@itemid='attributes_98725793']//div[@class='webix_ss_body']//div[@column=1]//div[text()='"
				+ attributeName + "']";
		String index = findElement(xpathName).getAttribute("aria-rowindex");
		String xpathValue = "//div[@itemid='attributes_98725793']//div[@column=2]//div[@aria-rowindex=" + index
				+ " and text()='" + attributeValue + "']";
		scrollTo(xpathValue);
		return isElementPresent(xpathValue);
	}

	/**
	 * @author Hera Aijaz
	 *         <a href="mailto:hera.aijaz@hrsoft.com">hera.aijaz@hrsoft.com</a>
	 */
	public String attributeValueOf(String attributeName) {
		String xpathName = "//div[@itemid='attributes_98725793']//div[@class='webix_ss_body']//div[@column=1]//div[text()='"
				+ attributeName + "']";
		String index = findElement(xpathName).getAttribute("aria-rowindex");
		String xpathValue = "//div[@itemid='attributes_98725793']//div[@column=2]//div[@aria-rowindex=" + index + "]";
		return findElement(xpathValue).getText();
	}

	/**
	 * @author Hera Aijaz
	 *         <a href="mailto:hera.aijaz@hrsoft.com">hera.aijaz@hrsoft.com</a>
	 */
	public UmeiAttributes editAttributeValueInUmeiAttributeTo(String attributeValue) {
		String xpath = "//div[@name='attribute_value']//div[@class='control-div md-form form-md']//input";
		waitForElement(xpath);
		WebElement attributeValueInputField = findElement(xpath);
		attributeValueInputField.clear();
		attributeValueInputField.sendKeys(attributeValue);
		click("//div[@class='modal-footer ui-draggable-handle']//button[text()='Save & Close']");
		return this;
	}
}
