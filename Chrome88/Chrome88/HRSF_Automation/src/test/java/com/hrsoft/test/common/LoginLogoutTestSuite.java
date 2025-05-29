package com.hrsoft.test.common;

import static com.hrsoft.reports.ExtentLogger.info;
import java.util.Optional;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v118.network.Network;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;
import java.util.Map;
import java.util.HashMap;
import org.openqa.selenium.devtools.v118.network.model.Headers;

public class LoginLogoutTestSuite extends WebBaseTest {

    @Author (name = QAResources.SRINIVAS)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.LOGIN }, dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SM_00_loginAndLogout (TestDataExcel data) {
    	
        //LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
    	LoginPage login = new LoginPage ();
//        login.enterUsername ("invalid")
//             .enterPassword ("invalid")
//             .clickLoginAndReturnLoginPage ();
//        assertEquals (login.getLoginError (), "Authentication Failed");
//        info (login.getLoginError () + " message was displayed");
//        login.clickReturnToLogin ()
//        login.logIn (data.getUserName (), data.getPassword ());
        
        DevTools devTools = ((ChromeDriver) DriverManager.getDriver()).getDevTools();
        devTools.createSession();

        // Enable Network
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        
     // Define custom headers
        
       // HrSoftDb hrSoftDb = new HrSoftDb();
        String LANG_ID = "en";
        String SKIN_ID = "default";
        String CUST_ID = "hrsofti";
        String USER_ID = "admin_user" ;
        String WUID = "301143";
//		try {
//			WUID = hrSoftDb.getWUIDByUserIdAndCustId(USER_ID, CUST_ID);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        
        
		String headerName = "User_info";
		String headerValue = "<?xml version='1.0'>" +
				"<authenticated_user>" +
				"<user_id>" + USER_ID + "</user_id>" +
				"<customer_id>" + CUST_ID + "</customer_id>" +
				"<language_id>" + LANG_ID + "</language_id>" +
				"<skin_id>" + SKIN_ID + "</skin_id>" +
				"<wuid>" + WUID + "</wuid>" +
				"</authenticated_user>";

        // Prepare the custom header
        Map<String, Object> headersMap = new HashMap<>();
        headersMap.put(headerName, headerValue);
        Headers headers = new Headers(headersMap);

        // Set the custom header
        devTools.send(Network.setExtraHTTPHeaders(headers));

        // Navigate to the page that requires authentication
        DriverManager.getDriver().get("https://hrsofti-tcc2300-sqa.cloud.hrsoft.com/");
        
        
        info ("logged in successfully");
        new HRSoftPage ().logOut ();
        login.navigatedToLoginPage ();
        info ("logged out successfully and log-in page displayed");
    }
}
