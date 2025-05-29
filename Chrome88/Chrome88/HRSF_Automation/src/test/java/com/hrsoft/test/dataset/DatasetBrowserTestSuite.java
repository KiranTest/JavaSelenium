
package com.hrsoft.test.dataset;

import static com.hrsoft.reports.ExtentLogger.info;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.db.HrSoftDb;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.cloudadmin.DatasetExplorer;
import com.hrsoft.gui.dataset.DatasetEditor;
import com.hrsoft.gui.dataset.ValidateAll;
import com.hrsoft.test.setuphelpers.RetryAnalyzer;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

@Test (groups = "smoke")
public class DatasetBrowserTestSuite extends WebBaseTest {

    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")
    public void SM_226_DatasetBrowserCustomerDropdown(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DatasetExplorer datasetExplorer = hrSoftPage.clickCloudAdmin().clickDatasetExplorer();
		datasetExplorer.verifySelectedClient(Constants.custId);
		hrSoftPage.logOut();
	}
    
    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")
    public void SM_227_DatasetBrowserProductDropdown(TestDataExcel data) {
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DatasetExplorer datasetExplorer = hrSoftPage.clickCloudAdmin().clickDatasetExplorer();
		
		HrSoftDb myDb = new HrSoftDb();
		
    	List<String> presentProducts = new LinkedList<>();
        presentProducts = datasetExplorer.getProductOptions();
        
        List <Map <String, Object>> activeProducts;
        activeProducts = myDb.executeSelect("select ac.app_name as app_name "
							        		+ "from tcc_cust_app_cfg ca "
							        		+ "inner join tcc_app_cfg ac on ca.app_id=ac.app_id "
							        		+ "inner join tcc_cust_cfg cc on ca.cust_guid=cc.cust_guid "
							        		+ "where cc.cust_id='" + Constants.custId + "' "
							        		+ "order by app_name asc");
        myDb.closeConnection();
        
    	assertEquals(activeProducts.size(), presentProducts.size());
        
    	for(int i=0; i<presentProducts.size(); i++) {
    		assertEquals(activeProducts.get(i).get("app_name"), presentProducts.get(i));
    	}
        
        hrSoftPage.logOut();
	}

    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")    
    public void SM_228_DatasetBrowserProductFiltering(TestDataExcel data) {
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DatasetExplorer datasetExplorer = hrSoftPage.clickCloudAdmin().clickDatasetExplorer();
		
		List<String> presentProducts = new LinkedList<>();
        presentProducts = datasetExplorer.getProductOptions();
        
        HrSoftDb myDb = new HrSoftDb();
        for(String p : presentProducts) {
        	datasetExplorer.verifyProductFilter(myDb, p);
        }
        
        myDb.closeConnection();
        
        hrSoftPage.logOut();
	}
	
    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")    
	public void SM_229_DatasetBrowserMultiProductFiltering(TestDataExcel data) {
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DatasetExplorer datasetExplorer = hrSoftPage.clickCloudAdmin().clickDatasetExplorer();
		
		List<String> presentProducts = new LinkedList<>();
        presentProducts = datasetExplorer.getProductOptions();
        
        HrSoftDb myDb = new HrSoftDb();
        
        Collections.shuffle(presentProducts);
        List<String> products = new ArrayList<String>();
        while(presentProducts.size() > 1) {
        	products.clear();
        	products.addAll(presentProducts.subList(0,2));
        	presentProducts.removeAll(products);
        	datasetExplorer.verifyProductFilter(myDb, products);
        }
        
        myDb.closeConnection();
        
        hrSoftPage.logOut();
	}
	
    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")    
	public void SM_230_DatasetBrowserValidateAllLoads(TestDataExcel data) {
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DatasetExplorer datasetExplorer = hrSoftPage.clickCloudAdmin().clickDatasetExplorer();
		ValidateAll validateAll = datasetExplorer.clickValidateAll();
		info(Integer.toString(validateAll.getErrorCount()) + " errors found!");		
        hrSoftPage.logOut();
	}
	
    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")    
	public void SM_231_DatasetBrowserValidateAllRescans(TestDataExcel data) {
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DatasetExplorer datasetExplorer = hrSoftPage.clickCloudAdmin().clickDatasetExplorer();
		int errorCount = datasetExplorer.clickValidateAll().getErrorCount();
		
		datasetExplorer.closeCurrentTab();
		
		String testDsName = "AutomatedValidationError_Delme";
		
		DatasetEditor datasetEditor = datasetExplorer.createDataset(testDsName);
		datasetEditor.causeValidationError().closeCurrentTab();		
		
		datasetExplorer.clickValidateAll().validateErrorCountIncreased(errorCount);		
		datasetExplorer.closeCurrentTab();
		datasetExplorer.deleteDataset(testDsName);
        hrSoftPage.logOut();
	} 

    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")    
	public void SM_232_DatasetBrowserValidateEditDs(TestDataExcel data) {
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DatasetExplorer datasetExplorer = hrSoftPage.clickCloudAdmin().clickDatasetExplorer();
		
		String testDsName = "AutomatedValidationError_Delme";
		DatasetEditor datasetEditor = datasetExplorer.createDataset(testDsName);
		datasetEditor.causeValidationError().closeCurrentTab();
		datasetExplorer.clickValidateAll().editDataset(testDsName);
		
		datasetExplorer.navigateToTab(0);
		datasetExplorer.deleteDataset(testDsName);
        
        hrSoftPage.logOut();
	}
    
    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")    
	public void SM_233_DatasetBrowserCreateDataset(TestDataExcel data) {
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.clickCloudAdmin().clickDatasetExplorer().validateCreateDsAndCleanup();
        hrSoftPage.logOut();
	}

    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")    
	public void SM_234_DatasetBrowserCreateDatasetMultipleProducts(TestDataExcel data) {
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		DatasetExplorer datasetExplorer = hrSoftPage.clickCloudAdmin().clickDatasetExplorer();
		
		List<String> presentProducts = new LinkedList<>();
        presentProducts = datasetExplorer.getProductOptions();        
        Collections.shuffle(presentProducts);
        datasetExplorer.vaidateMultiProductCreate(presentProducts.subList(0,2));
        hrSoftPage.logOut();
	}
    
    @Author (name = QAResources.LINDSEY)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.DATASET }, dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class,
            dataProvider = "SmokeTest")    
	public void SM_243_DatasetBrowserEditDataset(TestDataExcel data) {
    	LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		hrSoftPage.clickCloudAdmin().clickDatasetExplorer().verifyEditDataset();
        hrSoftPage.logOut();
	}
}
