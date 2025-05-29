package com.hrsoft.test.tccsecurity;

import static com.hrsoft.config.ConfigFactory.getConfig;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static com.hrsoft.driver.DriverManager.getDriver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.hrsoft.db.HrSoftDb;
import com.hrsoft.db.datadriven.User;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.cloudadmin.ManageCloudProductsPage;
import com.hrsoft.reports.ExtentLogger;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */

@Test
public class ChannelRendering extends BaseQueries {

    private List <String>           validSensivitityList_for_ch1_test = asList ("0,20,30,35,37,40,55,60,65,73,75,80,95,100,112,135,200".split (","));
    private List <String>           validSensivitityList_for_ch3_test = asList ("0,20,30,35,37,40,55,60,65,73,75,80,95,100,112,115,120,125,130,135,140,145,155,160,165,170".split (","));

    private LoginPage               login                             = new LoginPage ();
    private HRSoftPage              hrSoftPage                        = new HRSoftPage ();
    private ManageCloudProductsPage manageCloudProducts               = new ManageCloudProductsPage ();
    private HrSoftDb                hrSoftDb                          = new HrSoftDb ();
//    		"localhost", "TCC2200UUEIP01",
//            "sa", "!New2Day!");

    @Test
    public void SM_59_TccPlatformSecurityChannel_1_RenderingSecurity () {
        String sensitivityUrl = getConfig ().webAppicationURL () + "/content/portal/tccserver/loadApp.htm?wiztype=HRSOFTCLOUD_APPD_TEST_AUTOMATION_CHANNEL_1_RENDERING&wizinstanceid=993C9873-A13E-41EA-A4A4-BDC0C2A0E6ED";
        List <String> buttonList = new ArrayList <String> ();
        List <User> users = getUsersChannel1 ();

        login.loadUrl (getConfig ().webAppicationURL ())
        .logIn ();
        for (User user : users) {
            ExtentLogger.info (user.getUserName () + " " + user.getCustId ());
            hrSoftPage.proxyAs (user.getUserName (), user.getCustId ());
            hrSoftPage.openNewEmptyTab ();
            getDriver ().get (sensitivityUrl);
            List <WebElement> buttons = manageCloudProducts.getSensitivityButtons ();
            buttonList = buttons.stream ().map (e -> e.getText ().split ("_")[1]).collect (toList ());
            List <String> queryList = new ArrayList <> (asList (user.getSensitivityList ()));
            queryList.retainAll (validSensivitityList_for_ch1_test);
            queryList.removeAll (buttonList);
            if (!queryList.isEmpty ()) {
                 System.err.println ("Can read N:" + queryList);
            }
            getDriver ().close ();
            hrSoftPage.navigateToTab (0);
            hrSoftPage.cancelProxy ();
        }
    } 

    @Test
    public void SM_60_TccPlatformSecurityChannel_3_RenderingSecurity () throws InterruptedException {
        String sensitivityUrl = getConfig ().webAppicationURL () + "/content/portal/tccserver/loadApp.htm?mode=RUN&wiztype=DATAVIEW_APPD_TEST_AUTOMATION_CHANNEL_3_DATAAPI_READ&wizinstanceid=41C9EFC2-870F-4439-980E-4BA484654292&paramhier_guid=%s&paramuser_guid_csv=%s";
        String extractJson = "jQuery( \":root\" ).append($(\"<p id='dataobj12'></p>\").text(JSON.stringify($(\"div.widgetGRID:first\").data(\"jsonObj\").data)))";
        String channel3SecondQuery = "SELECT * FROM TCC_PARTICIPANT_RELATIONSHIP_PERMISSIONS WHERE CUST_GUID =  '%s' AND HIERARCHY_GUID = '%s'\r\n" + "AND APP_ID = 'DATAVIEW'\r\n" + "AND ACTING_PARTICIPANT_GUID = '%s' AND TARGET_PARTICIPANT_GUID     IN ('%s')\r\n" + "";
        List <User> users = getUsersChannel3 ();
        Set <String> processedUsers = new HashSet <> ();

        login.loadUrl (getConfig ().webAppicationURL ())
             .logIn ();
        for (User user : users) {
            if (!processedUsers.add (user.getUserName () + user.getCustId ()))
                continue;
            Thread.sleep (5000l);
            hrSoftPage.proxyAs (user.getUserName (), user.getCustId ());
            Thread.sleep (5000l);
            hrSoftPage.openNewEmptyTab ();
            String csv = users.stream ()
                              .filter (x -> x.getActingParticipantGuid ()
                                             .equalsIgnoreCase (user.getActingParticipantGuid ()))
                              .map (u -> u.gettargetParticipantGuid ()).distinct ().collect (Collectors.joining (","));
            getDriver ().get (format (sensitivityUrl, user.getHierarchyGuid (), csv));
            Thread.sleep (5000l);
            hrSoftPage.executeJScript (extractJson);
            String json1 = getDriver ().findElement (By.id ("dataobj12")).getText ();
            Object obj = JSONValue.parse (json1);
            JSONArray json = (JSONArray) obj;
            String genSQL2 = format (channel3SecondQuery,
                                     user.getCustGuid (),
                                     user.getHierarchyGuid (),
                                     user.getActingParticipantGuid (),
                                     csv.replaceAll (",", "','"));
            List <Map <String, Object>> db3 = hrSoftDb.executeSelect (genSQL2);
            ExtentLogger.info (user.getUserName () + " Proxied User: " + user.getActingParticipantGuid () + "(" + user.getUserName () + ") with cust: " + user.getCustGuid () + "(" + user.getCustId () + ")");
            ExtentLogger.info (user.getUserName () + " Url: " + format (sensitivityUrl, user.getHierarchyGuid (), csv));
            ExtentLogger.info (user.getUserName () + " TPRP SQL: " + genSQL2);

            HashMap <String, String> dbSensitivity = new HashMap <> ();
            for (Map <String, Object> dbRec : db3) {
                String targetParticipantGuid = dbRec.get ("TARGET_PARTICIPANT_GUID").toString ();
                String sensitivityCsv = dbRec.get ("SENSITIVITY_CSV").toString ();
                if (!dbSensitivity.containsKey (targetParticipantGuid))
                    dbSensitivity.put (targetParticipantGuid, sensitivityCsv);
                else {
                    dbSensitivity.put (targetParticipantGuid,
                                       dbSensitivity.get (targetParticipantGuid)
                                                    .toString () + "," + dbRec.get ("SENSITIVITY_CSV").toString ());
                }
            }

            if (dbSensitivity.size () != json.size ()) {
                 System.err.println ("Found " + json.size () + " rows on UI and " + db3.size () + "rows returned from SQL");
            } else {

                for (int i = 0; i < json.size (); i++) {
                    JSONObject jsonObj = (JSONObject) json.get (i);
                    if (dbSensitivity.get (jsonObj.get ("user_guid").toString ()) == null) {
                         System.err.println ("Sensitivity_CSV not found user_guid :" + jsonObj.get ("user_guid")
                                                                                   .toString () + " on DB, but value exists on UI");
                        continue;
                    }

                    List <String> dbSensitivityList = new ArrayList <> (
                            asList (dbSensitivity.get (jsonObj.get ("user_guid").toString ()).split (",")));
                    dbSensitivityList.retainAll (validSensivitityList_for_ch3_test);

                    List <String> jsonSensitivtyList = new ArrayList <> ();
                    for (String x : validSensivitityList_for_ch3_test) {
                        if (jsonObj.get ("column_" + x) != null && jsonObj.get ("column_" + x).toString ().equals ("1"))
                            jsonSensitivtyList.add (x);
                    }

                    ArrayList <String> jsonSensitivtyList2 = new ArrayList <> (jsonSensitivtyList);
                    jsonSensitivtyList2.removeAll (dbSensitivityList);
                    if (!jsonSensitivtyList2.isEmpty ()) {
                         System.err.println ("UI shows additional sensitivity values for user_guid :" + jsonObj.get ("user_guid")
                                                                                                    .toString () + ": Additional Values found on DB : " + jsonSensitivtyList2);
                    }

                    dbSensitivityList.removeAll (jsonSensitivtyList);
                    if (!dbSensitivityList.isEmpty ()) {
                         System.err.println ("DB shows additional sensitivity values for user_guid :" + jsonObj.get ("user_guid")
                                                                                                    .toString () + ": Additional Values found on DB : " + dbSensitivityList);
                    }
                }
            }
            getDriver ().close ();
            Thread.sleep (5000l);
            hrSoftPage.navigateToTab (0);
            Thread.sleep (5000l);
            hrSoftPage.cancelProxy ();
            Thread.sleep (5000l);
        }
    }

    private List <User> getUsersChannel1 () {
        List <User> users = new ArrayList <User> ();
        List <Map <String, Object>> result = hrSoftDb.executeSelect (channel_1_SqlToGiveUniqueUsersToTestAgainst);
        for (Map <String, Object> r : result) {
            User user = new User ();
            user.setUserNmae ((String) r.get ("USER_NAME"));
            user.setCustGuid ((String) r.get ("cust_guid"));
            user.setCustId ((String) r.get ("cust_id"));
            user.setAppId ((String) r.get ("APP_ID"));
            user.setActingParticipantGuid ((String) r.get ("ACTING_PARTICIPANT_GUID"));
            user.setHierarchyGuid ((String) r.get ("HIERARCHY_GUID"));
            user.setSensitivityList ((String) r.get ("Sensitivity_CSV"));
            users.add (user);
        }
        return users;
    }

    private List <User> getUsersChannel3 () {
        List <User> users = new ArrayList <User> ();
        List <Map <String, Object>> result = hrSoftDb.executeSelect (channel_3_SqlToGiveUniqueUsersToTestAgainst);
        for (Map <String, Object> r : result) {
            User user = new User ();
            user.setCustGuid ((String) r.get ("CUST_GUID"));
            user.setUserNmae ((String) r.get ("user_name"));
            user.setCustId ((String) r.get ("cust_id"));
            user.setActingParticipantGuid ((String) r.get ("ACTING_PARTICIPANT_GUID"));
            user.setTargetParticipantGuid ((String) r.get ("TARGET_PARTICIPANT_GUID"));
            user.setHierarchyGuid ((String) r.get ("HIERARCHY_GUID"));
            users.add (user);
        }
        return users;
    }

}
