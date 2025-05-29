package com.hrsoft.test.homepage;

import static com.hrsoft.constants.Constants.*;
import static com.hrsoft.reports.ExtentLogger.pass;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.compensation.PlanningPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */
@Test (groups = "smoke")
public class HomePageTestSuite extends WebBaseTest {

    @Author (name = QAResources.SRINIVAS)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.HOMEPAGE }, dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SM_61_HomePage_MyTasksSectionClickTaskLink (TestDataExcel data) {
        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        hrSoftPage.clickMyTasksSubmitButton ();
        assertEquals (new PlanningPage ().getTitle (), COMPENSATION_PLANS);
        hrSoftPage.logOut ();
    }

    @Author (name = QAResources.SRINIVAS)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.HOMEPAGE }, dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SM_62_HomePage_MyTasksExpandOrCollapse (TestDataExcel data) {
        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        for (int i = 0; i < 4; i++) {
            hrSoftPage.clickMyTasksDropDown ();
            assertFalse (hrSoftPage.myTaskSubmitButton ());
            hrSoftPage.clickMyTasksDropDown ();
            assertTrue (hrSoftPage.myTaskSubmitButton ());
        }
        hrSoftPage.logOut ();
        pass ("my tasks dropdown is expanding and collapsing");
    }

    @Author (name = QAResources.SRINIVAS)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.HOMEPAGE }, dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SM_63_HomePage_LeftNavigationBarExpandAndCollapse (TestDataExcel data) {
        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ())
                .closeBurgermenu ();
        for (int i = 0; i < 4; i++) {
            hrSoftPage.clickNavigationBar ();
            hrSoftPage.doWait (2000);
            assertTrue (hrSoftPage.navigationBarExpansion ());
            hrSoftPage.clickNavigationBar ();
            hrSoftPage.doWait (2000);
            assertFalse (hrSoftPage.navigationBarExpansion ());
        }
        hrSoftPage.logOut ();
        pass ("left navigation bar is expanding and collapsing");
    }

    @Author (name = QAResources.SRINIVAS)
    @Test (groups = { TestGroups.SMOKE, ComponentGroups.HOMEPAGE }, dataProviderClass = DataProviders.class,
            dataProvider = "SmokeTest")
    public void SM_64_To_SM_77_HomePage_CompensationReport_VerifyClickFunctions (TestDataExcel data) {

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ())
                .closeBurgermenu ();
        hrSoftPage.selectCompensationPlan (Constants.PlanForManagerView);
        hrSoftPage.selectCompensationGroup (Constants.CompManagerFullName + " Grp");
        hrSoftPage.clickPlanStatusDistribution ();
        assertTrue (hrSoftPage.planStatusReportPopUp ());
        pass ("Plan status report popped up");
        assertTrue (hrSoftPage.isSshowChart ());
        hrSoftPage.clickShowChart ();
        hrSoftPage.clickClearFilters ();
        hrSoftPage.clickPdf ();
        hrSoftPage.clickExcel ();
        assertTrue (hrSoftPage.isFileDownloaded (PLAN_STATUS_DISTRIBUTION));
        hrSoftPage.logOut ();
        pass (PLAN_STATUS_DISTRIBUTION + " has downloaded successfully");
    }

}
