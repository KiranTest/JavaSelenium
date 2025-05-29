package com.hrsoft.test.tccsecurity;

import com.hrsoft.test.setuphelpers.WebBaseTest;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */
public class BaseQueries extends WebBaseTest{
    
    
    String channel_1_SqlToGiveUniqueUsersToTestAgainst =  "SELECT *\r\n" + 
            "FROM\r\n" + 
            "(\r\n" + 
            "    SELECT *, \r\n" + 
            "           DENSE_RANK() OVER(PARTITION BY a.CUST_GUID, \r\n" + 
            "                                          a.HIERARCHY_GUID, \r\n" + 
            "                                          a.Sensitivity_CSV\r\n" + 
            "           ORDER BY a.ACTING_PARTICIPANT_GUID) AS Rank\r\n" + 
            "    FROM\r\n" + 
            "    (\r\n" + 
            "        SELECT tud.USER_NAME,  cax.cust_guid, cust.cust_id, \r\n" +
            "                cax.APP_ID, \r\n" + 
            "               cax.HIERARCHY_GUID, \r\n" + 
            "               tpp.ACTING_PARTICIPANT_GUID, \r\n" + 
            "               STRING_AGG(tpp.sensitivity, ',') WITHIN GROUP(ORDER BY tpp.sensitivity) Sensitivity_CSV\r\n" + 
            "        FROM TCC_CUST_APP_HIER_SECURITY_MODEL_XREF cax\r\n" + 
            "             INNER JOIN TCC_USER_DEMOGRAPHIC tud ON tud.CUST_GUID = cax.cust_guid AND tud.EMP_STATUS_CODE = 'A'\r\n" + 
            "             INNER JOIN tcc_participant_permissions tpp ON tpp.ACTING_PARTICIPANT_GUID = tud.USER_GUID\r\n" + 
            "                                                           AND tpp.CUST_GUID = tud.CUST_GUID\r\n" + 
            "                                                           AND tpp.APP_ID = cax.APP_ID\r\n" + 
            "                                                           AND tpp.HIERARCHY_GUID = cax.HIERARCHY_GUID\r\n" + 
            "                                                           AND tpp.permission IN(20, 60, 100, 320, 360, 400)\r\n" +
            "              INNER JOIN tcc_cust_cfg cust ON cax.CUST_GUID = cust.CUST_GUID"+
            "        WHERE cax.APP_ID = 'HRSOFTCLOUD'\r\n" + 
            "              AND cax.IS_DEFAULT = 'Y'\r\n" + 
            "              AND EXISTS\r\n" + 
            "        (\r\n" + 
            "            SELECT 1\r\n" + 
            "            FROM TCC_PARTICIPANT_PERMISSIONS tpp\r\n" + 
            "            WHERE tpp.CUST_GUID = cax.CUST_GUID\r\n" + 
            "                  AND tpp.HIERARCHY_GUID = cax.HIERARCHY_GUID\r\n" + 
            "        )\r\n" + 
            "        GROUP BY cax.CUST_GUID, \r\n" +
            "                 cax.app_id, \r\n" +
            "                 cax.hierarchy_guid, \r\n" + 
            "                 tpp.ACTING_PARTICIPANT_GUID,\r\n" + 
            "                 tud.user_name, \r\n" +
            "                 cust.cust_id \r\n" +
            "    ) a\r\n" + 
            ") b\r\n" + 
            "WHERE b.Rank in(1,2)\r\n" + 
            "\r\n" + 
            "ORDER BY b.CUST_GUID, b.HIERARCHY_GUID";
    
            String channel_1_SqlToGiveUniqueUsersExplodedPersmissions= "SELECT b.*, tpp2.SENSITIVITY, tpp2.PERMISSION, CASE WHEN tpp2.permission IN(20, 60, 100, 320, 360, 400) THEN 'Y' ELSE 'N' END AS CAN_READ\r\n" + 
                    "FROM\r\n" + 
                    "(\r\n" + 
                    "    SELECT *, \r\n" + 
                    "           DENSE_RANK() OVER(PARTITION BY a.CUST_GUID, \r\n" + 
                    "                                          a.HIERARCHY_GUID, \r\n" + 
                    "                                          a.Sensitivity_CSV\r\n" + 
                    "           ORDER BY a.ACTING_PARTICIPANT_GUID) AS Rank\r\n" + 
                    "    FROM\r\n" + 
                    "    (\r\n" + 
                    "        SELECT cax.cust_guid, \r\n" + 
                    "               cax.HIERARCHY_GUID, \r\n" + 
                    "               cax.APP_ID, \r\n" + 
                    "               tpp.ACTING_PARTICIPANT_GUID, \r\n" + 
                    "               STRING_AGG(tpp.sensitivity, ',') WITHIN GROUP(ORDER BY tpp.sensitivity) Sensitivity_CSV\r\n" + 
                    "        FROM TCC_CUST_APP_HIER_SECURITY_MODEL_XREF cax\r\n" + 
                    "             INNER JOIN TCC_USER_DEMOGRAPHIC tud ON tud.CUST_GUID = cax.cust_guid\r\n" + 
                    "                                                    AND tud.EMP_STATUS_CODE = 'A'\r\n" + 
                    "             INNER JOIN tcc_participant_permissions tpp ON tpp.ACTING_PARTICIPANT_GUID = tud.USER_GUID\r\n" + 
                    "                                                           AND tpp.CUST_GUID = tud.CUST_GUID\r\n" + 
                    "                                                           AND tpp.APP_ID = cax.APP_ID\r\n" + 
                    "                                                           AND tpp.HIERARCHY_GUID = cax.HIERARCHY_GUID\r\n" + 
                    "                                                           AND tpp.permission IN(20, 60, 100, 320, 360, 400)\r\n" + 
                    "        WHERE cax.APP_ID = 'HRSOFTCLOUD'\r\n" + 
                    "              AND cax.IS_DEFAULT = 'Y'\r\n" + 
                    "              AND EXISTS\r\n" + 
                    "        (\r\n" + 
                    "            SELECT 1\r\n" + 
                    "            FROM TCC_PARTICIPANT_PERMISSIONS tpp\r\n" + 
                    "            WHERE tpp.CUST_GUID = cax.CUST_GUID\r\n" + 
                    "                  AND tpp.HIERARCHY_GUID = cax.HIERARCHY_GUID\r\n" + 
                    "        )\r\n" + 
                    "        GROUP BY cax.CUST_GUID, \r\n" + 
                    "                 cax.app_id, \r\n" + 
                    "                 cax.hierarchy_guid, \r\n" + 
                    "                 tpp.ACTING_PARTICIPANT_GUID\r\n" + 
                    "    ) a\r\n" + 
                    ") b\r\n" + 
                    "INNER JOIN TCC_PARTICIPANT_PERMISSIONS tpp2 ON b.CUST_GUID = tpp2.CUST_GUID\r\n" + 
                    "                                               AND b.HIERARCHY_GUID = tpp2.HIERARCHY_GUID\r\n" + 
                    "                                               AND b.ACTING_PARTICIPANT_GUID = tpp2.ACTING_PARTICIPANT_GUID\r\n" + 
                    "                                               AND b.app_id = tpp2.app_id\r\n" + 
                    "WHERE b.Rank IN(1, 2)\r\n" + 
                    "ORDER BY b.CUST_GUID, \r\n" + 
                    "         b.HIERARCHY_GUID, \r\n" + 
                    "         b.ACTING_PARTICIPANT_GUID,\r\n" + 
                    "         tpp2.SENSITIVITY;";
            
            String channel_3_SqlToGiveUniqueUsersToTestAgainst= "SELECT *\r\n" + 
                    "FROM\r\n" + 
                    "(\r\n" + 
                    "    SELECT TPRX.CUST_GUID, \r\n" + 
                    "        tud.user_name,\r\n" + 
                    "       cust.cust_id,\r\n" + 
                    "           TPRX.HIERARCHY_GUID, \r\n" + 
                    "           tprx.RELATIONSHIP_RULE_GUID,\r\n" + 
                    "           trro.RELATIONSHIP_RULE_NAME,\r\n" + 
                    "           tprx.ACTING_PARTICIPANT_GUID, \r\n" + 
                    "           tprx.TARGET_PARTICIPANT_GUID, \r\n" + 
                    "           DENSE_RANK() OVER(PARTITION BY tprx.CUST_GUID, \r\n" + 
                    "                                          TPRX.HIERARCHY_GUID, \r\n" + 
                    "                                          tprx.RELATIONSHIP_RULE_GUID\r\n" + 
                    "           ORDER BY tprx.TARGET_PARTICIPANT_GUID, tprx.ACTING_PARTICIPANT_GUID) AS Rank\r\n" +
                    "    FROM TCC_CUST_APP_HIER_SECURITY_MODEL_XREF cax\r\n" + 
                    "         INNER JOIN TCC_USER_DEMOGRAPHIC tud ON tud.CUST_GUID = cax.cust_guid\r\n" + 
                    "                                                AND tud.EMP_STATUS_CODE = 'A'\r\n" + 
                    "         INNER JOIN TCC_PARTICIPANT_RELATIONSHIPs_XREF TPRX ON TPRX.ACTING_PARTICIPANT_GUID = tud.USER_GUID\r\n" + 
                    "                                                               AND TPRX.CUST_GUID = tud.CUST_GUID\r\n" + 
                    "                                                               AND TPRX.HIERARCHY_GUID = cax.HIERARCHY_GUID\r\n" + 
                    "                                                               AND TPRX.ACTING_PARTICIPANT_GUID IS NOT NULL\r\n" + 
                    "                                                               AND TPRX.TARGET_PARTICIPANT_GUID IS NOT NULL\r\n" + 
                    "        inner join TCC_GROUP_RELATIONSHIP_RULEs_OLM trro ON trro.relationship_rule_guid = tprx.RELATIONSHIP_RULE_GUID\r\n" + 
                    "        INNER JOIN tcc_cust_cfg cust ON cax.CUST_GUID = cust.CUST_GUID\r\n" + 
                    "    WHERE cax.APP_ID = 'DATAVIEW'\r\n" + 
                    "          AND cax.IS_DEFAULT = 'Y'\r\n" + 
                    "          AND EXISTS\r\n" + 
                    "    (\r\n" + 
                    "        SELECT 1\r\n" + 
                    "        FROM TCC_PARTICIPANT_PERMISSIONS TPRX\r\n" + 
                    "        WHERE TPRX.CUST_GUID = cax.CUST_GUID\r\n" + 
                    "              AND TPRX.HIERARCHY_GUID = cax.HIERARCHY_GUID\r\n" + 
                    "    )\r\n" + 
                    ") b\r\n" + 
                    "WHERE b.Rank IN(1,2,3)\r\n" +
                    "ORDER BY b.CUST_GUID, \r\n" + 
                    "         b.HIERARCHY_GUID, \r\n" + 
                    "          b.USER_NAME, \r\n" + 
                    "           b.CUST_ID,\r\n" + 
                    "         b.RELATIONSHIP_RULE_GUID;\r\n" + 
                    "";
            
            
           String channel_3_SqlToGiveUniqueUsersExplodedPersmissions="";
}
