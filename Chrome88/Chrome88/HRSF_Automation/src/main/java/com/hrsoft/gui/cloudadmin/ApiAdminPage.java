package com.hrsoft.gui.cloudadmin;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.WebElement;

import com.hrsoft.gui.HRSoftPage;



public class ApiAdminPage extends HRSoftPage
{ 
	                  /*For Api - Partner Registration Test Case */
	
  private String clickAPIAdminCard          = "//ul/li[text()='Add/Modify partners']";
  private String clickPartnerRegistration   = "//div[contains(text(),'Register a new partner or modify an existing one')]";
  private String clickEditIcon              = "//div[@role='gridcell']/div[@rowindex=0]/a[@title='Edit']";
  private String enterPartnerName           = "//input[contains(@id,'fldpartner_name')]";
  private String saveAndClose               = "//button[text()='Save & Close']";
  private String closePopUp                 = "//button[text()='×']";
   
                      /* For Api - End Point Management TestCase */
  
  private String endPointManagement         = "//div[contains(text(),'Manage API Endpoint')]";
  private String rowIcon                    = "//div[contains(@class,'webix_column  webix_first')]/div[@role='gridcell' and @aria-rowindex='1']/div[@class='webix_tree_close']";
  private String EditTextForEndPoint        = "//input[contains(@id,'fldendpoint_name')]";
  
                     /*For API - Transform Attributes */
  
  private String transformAttributes        = "//div[contains(text(),'Manage Custom transformations')]";
  private String clickOnDetails             = "//div[contains(@class,'webix_column left_area webix_last')]/div[@role='gridcell' and @aria-rowindex='1']/div/a[@title='Details']";
  private String clickOnEditForAttributes   = "//div[contains(@class,'webix_column')]/div[@role='gridcell' and @aria-rowindex=1]/div/a[@title='Edit']";
  private String EditTextForAttrValue       = "//input[contains(@id,'fldtarnsform_attribute_value')]";
  
  private String partnerNameColumnXPath = "//div[@itemid='GRID_3272879231117616']//div[@role='treegrid']//div[@role='rowgroup']//div[@role='gridcell'][@aria-colindex='2'][@aria-level='1']";
  private String endpointNameColumnXPath = "//div[@itemid='GRID_3272879231117616']//div[@role='treegrid']//div[@role='rowgroup']//div[@role='gridcell'][@aria-colindex='3'][@aria-level='2']";
  private String endpointEditBtnXPath = "//div[@itemid='GRID_3272879231117616']//div[@role='treegrid']//div[@class='webix_ss_left']//a[@title='Edit']";
  
  public boolean openEndpointManagementPopup() {
	  return click(endPointManagement);
  }
  
  @Deprecated
  public void APIManagement() 
  {   
	  click(clickAPIAdminCard);
	  hasPageLoaded();  
	  String parent=driver.getWindowHandle();
	  Set<String>s=driver.getWindowHandles();

	  /* iterating using Iterator to switch Web driver to new window */
	  Iterator<String> I1= s.iterator();

	  while(I1.hasNext())
	  {
		  String child_window=I1.next();
		  if(!parent.equals(child_window))
		  {
			  driver.switchTo().window(child_window);
			  System.out.println(driver.switchTo().window(child_window).getTitle());
			  click(clickPartnerRegistration);  
			  doWait(5000);
			  click(clickEditIcon);
			  clear(enterPartnerName);
			  //setText(enterPartnerName,"Test Edited By Mashood");
			  click(saveAndClose);  
			  doWait(3000);
			  click(closePopUp);
			  doWait(3000);
			  
			  /* For End Point Management */
			  
			  click(endPointManagement);
			  click(rowIcon);
			  click(clickEditIcon);
			  clear(EditTextForEndPoint);
			  //setText(EditTextForEndPoint,"Test EndPoint By Mashood");
			  click(saveAndClose);
			  click(closePopUp);
			  
			  /* For Transform Attributes */
			  
			  click(transformAttributes);
			  click(clickOnDetails);
			  click(clickOnEditForAttributes);
			  clear(EditTextForAttrValue);
			  //setText(EditTextForAttrValue,"Edit Attribute value Test By Mashood");
			  click(saveAndClose);
			  click(closePopUp);
		  
	        }
	     }
	  
	 
     }

	public List<String> getAvaiablePartnerNames() {
		List<WebElement> partnerNames = findElements(partnerNameColumnXPath);
		return partnerNames.stream().map(x -> x.getText()).collect(Collectors.toList());	
	}

	public List<String> getPartnerEndpointNames(String apiPartnerName) {
		// close all expanded partners if any
		for(WebElement el : findElements(partnerNameColumnXPath+"//div[@class='webix_tree_open']"))
			click(el);
		//expand required partner
		click(partnerNameColumnXPath+"[@text()='"+apiPartnerName+"']//div[@class='webix_tree_close']");
		
		List<WebElement> partnerNames = findElements(endpointNameColumnXPath);
		return partnerNames.stream().map(x -> x.getText()).collect(Collectors.toList());	
		
	}

	public boolean openEditPopupForEndpoint(int order) {
		List<WebElement> btnList = findElements(endpointEditBtnXPath);
		if(btnList.size() < order+1)
			return false;
		return click(btnList.get(order));
	}
  }
 

