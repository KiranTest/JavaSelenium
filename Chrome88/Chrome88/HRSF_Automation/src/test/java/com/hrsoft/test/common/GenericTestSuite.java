package com.hrsoft.test.common;

import static com.hrsoft.constants.Constants.COMPENSATION_PLANS;
import static com.hrsoft.reports.ExtentLogger.info;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import java.util.Collection;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.db.CompViewAccessDbHelper;
import com.hrsoft.db.datadriven.model.GroupPlanner;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.compensation.PlanningPage;
import com.hrsoft.gui.compensation.ReviewingPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

/**
 * @author Annameni Srinivas <a href=
 *         "mailto:annameni.srinivas@hrsoft.com">annameni.srinivas@hrsoft.com</a>
 */

public class GenericTestSuite extends WebBaseTest {
    private CompViewAccessDbHelper compViewAccessDBHelper = new CompViewAccessDbHelper ();
    private PlanningPage           planningPage           = new PlanningPage ();

    @Author (name = QAResources.SRINIVAS)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_6A_CompView_PlanningCan_AccessCoPlannedGroup (TestDataExcel data) {

        Collection <GroupPlanner> users = compViewAccessDBHelper.getMixofCompPlanners (Constants.custId,
                                                                                       Constants.compPlanId);
        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        users.forEach (a -> System.out.println (a));

        users.forEach (u -> {
            if (u != null) {
                hrSoftPage.proxyAs (u.getPlannerUserName (), null);
                info ("Employee:" + u.getPlannerUserName ());
                hrSoftPage.doWait (10000);

                info ("Co-planner for:" + u.getGroupId ());
                hrSoftPage.doWait (5000);
                System.out.println (u.getGroupId ());
                if (hrSoftPage.openPlanningScreenForGeneric (Constants.compPlanId, u.getGroupId ())) {
                    info ("planning screen opened for: " + planningPage.getPlanningGroupName ());
                } else {
                    info ("planning screen is absent for: " + u.getGroupId ());
                }
                hrSoftPage.doWait (5000);

                hrSoftPage.cancelProxy ();
            }
        });
    }

    @Author (name = QAResources.SRINIVAS)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_6B_CompView_Reviewing_CanOpenReviewedGroups (TestDataExcel data) {

        Collection <GroupPlanner> users = compViewAccessDBHelper.getMixofCompPlanners (Constants.custId,
                                                                                       Constants.compPlanId);

        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        users.forEach (u -> {
            if (u != null) {
                hrSoftPage.proxyAs (u.getPlannerUserName (), null);
                info ("Employee:" + u.getPlannerUserName ());
                hrSoftPage.doWait (10000);

                info ("Co-planner for:" + u.getGroupId ());
                hrSoftPage.doWait (5000);
                if (hrSoftPage.openReviewScreenForGeneric (Constants.compPlanId, u.getGroupId ())) {
                    info ("planning screen opened for: " + (new ReviewingPage ()).getReviewingGroupName ());
                } else {
                    info ("planning screen is absent for: " + u.getGroupId ());
                }
                hrSoftPage.doWait (5000);

                hrSoftPage.cancelProxy ();
            }
        });
    }

//    @Author (name = QAResources.SRINIVAS)
//    @Test (groups = {
//            TestGroups.SMOKE,
//            ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
//    public void SM_07_CompView_Reviewing_EmpAwardChangesAccordingly (TestDataExcel data) {
//
//        Collection <GroupPlanner> users = compViewAccessDBHelper.getMixofCompPlanners (Constants.custId,
//                                                                                       Constants.compPlanId);
//
//        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
//        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
//        users.forEach (u ->System.out.println (u));
//        users.forEach (u -> {
//            if (u != null) {
//                hrSoftPage.proxyAs (Constants.CompManagerUserName);
//                info ("Employee:" + u.getPlannerUserName ());
//                hrSoftPage.doWait (10000);
//                hrSoftPage.openPlanRecommendations (Constants.compPlanId) ;
//                planningPage.clickSalaryTab ();
//                planningPage.clickResetAwards ();
//                planningPage.getcolumnToNumber ("5", "WS_Component_Actual_Amount@2");
//                String totalMeritValueBefore = planningPage.getTotalAwardActualValue ();
//                planningPage.enterAwardActualAmountValue ("1000");
//                String totalMeritValueAfter = planningPage.getTotalAwardActualValue ();
//                assertNotEquals (totalMeritValueBefore, totalMeritValueAfter);
//                hrSoftPage.cancelProxy ();
//            }
//        });
//
//    }

    @Author (name = QAResources.KALYAN)
    @Test (groups = {
            TestGroups.SMOKE,
            ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
    public void SM_09_CompView_Planning_ManagerCanOpenOwnLinkFromLeftNav (TestDataExcel data) {
        Collection <GroupPlanner> users = compViewAccessDBHelper.getMixofCompPlanners (Constants.custId,
                                                                                       Constants.compPlanId);
        LoginPage login = new LoginPage ().loadUrl (data.getApplicationURL ());
        HRSoftPage hrSoftPage = login.logIn (data.getUserName (), data.getPassword ());
        assertTrue (Constants.compPlanId > 0, "Test Plan Config Error: Invalid compensation plan id");

        users.forEach (q -> {
            if (q != null) {
                hrSoftPage.proxyAs (q.getPlannerUserName (), null);
                hrSoftPage.openPlanRecommendations (Constants.compPlanId);
                assertEquals (hrSoftPage.getTitle (), COMPENSATION_PLANS);
                hrSoftPage.cancelProxy ();
            }
        });
        hrSoftPage.logOut ();
    }
}
