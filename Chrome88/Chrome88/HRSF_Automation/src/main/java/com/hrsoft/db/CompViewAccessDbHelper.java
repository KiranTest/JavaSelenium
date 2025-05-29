package com.hrsoft.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.hrsoft.constants.Constants;
import com.hrsoft.db.datadriven.model.GroupPlanner;

public class CompViewAccessDbHelper extends HrSoftDb {

    /**
     * 
     * @param custId
     * @param userName
     *            - leave NULL when any user name is ok
     * @param compPlanId
     *            - leave NULL when any plan is ok
     * @param isManager
     *            - looking for managers (true) or co-planners
     *            (false)?
     * @param isReviewer
     *            - looking for review groups only?
     * @param isMultiGroupPlanner
     *            - looking for planners of multiple groups?
     * @param isLimitedCoPlanner
     *            - looking for planner of groups with mandatory
     *            filter / limited set of employees (true)?
     * @return up to 10 matching results
     */
    private Collection <GroupPlanner> getAccessibleGroups (String custId, String userName, Integer compPlanId,
                                                           boolean isManager, boolean isReviewer,
                                                           boolean isMultiGroupPlanner, boolean isLimitedCoPlanner) {
        Set <GroupPlanner> groups = new HashSet <> ();
        String selectSql = "select top 10 c.CUST_ID, gp.CUST_GUID, gp.PLANNER_GUID, gp.PLANNER_NUMBER, ud.USER_NAME AS PLANNER_USER_NAME,  FIRST_NAME,LAST_NAME, gp.MANAGER_GUID, gp.MANAGER_NUMBER, " + " gp.GROUP_ID, gp.CO_PLANNER, gp.COMP_PLAN_ID, gp.REL_ID, gp.GROUP_HIER_NAME, gp.MANDATORY_FILTER_ID, gp.LIMIT_TO_EMP_IDS" + " from V_RPT_COM_GROUP_PLANNER_ACCESS gp" + " inner join TCC_USER_DEMOGRAPHIC ud ON ud.CUST_GUID = gp.CUST_GUID AND ud.USER_GUID = gp.PLANNER_GUID" + " inner join COMP_PLAN c ON c.COMP_PLAN_ID = gp.COMP_PLAN_ID " + " WHERE c.PLAN_STAGE_ID = 4" + " and c.CUST_ID = ? " + " and (? != gp.CO_PLANNER)" + " and (? = 'N' OR gp.GROUP_ID IN (SELECT gs.GROUP_ID FROM GROUP_STATUS gs WHERE gs.group_type='R' and gs.STATUS_ID!=8))" + " and (? = 'N' OR (SELECT count(1) FROM V_RPT_COM_GROUP_PLANNER_ACCESS gp1 WHERE gp1.CUST_GUID = gp.CUST_GUID AND gp1.COMP_PLAN_ID = gp.COMP_PLAN_ID AND gp1.PLANNER_GUID = gp.PLANNER_GUID) > 1)" + " and (? = 'N' OR gp.MANDATORY_FILTER_ID != null OR gp.LIMIT_TO_EMP_IDS != null)";

        if (userName != null)
            selectSql += " and ud.USER_NAME = ?";
        if (compPlanId != null)
            selectSql += " and c.COMP_PLAN_ID = ?";

        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement (selectSql);

            int n = 0;
            stmt.setString (++n, Constants.custId);
            stmt.setString (++n, isManager ? "Y" : "N");
            stmt.setString (++n, isReviewer ? "Y" : "N");
            stmt.setString (++n, isMultiGroupPlanner ? "Y" : "N");
            stmt.setString (++n, isLimitedCoPlanner ? "Y" : "N");
            if (userName != null)
                stmt.setString (++n, userName);
            if (compPlanId != null)
                stmt.setInt (++n, Constants.compPlanId);

            ResultSet resultSet = stmt.executeQuery ();

            while (resultSet.next ()) {
                GroupPlanner gp = new GroupPlanner ();
                gp.setPlannerFirstName (resultSet.getString ("FIRST_NAME"));
                gp.setPlannerLastName (resultSet.getString ("LAST_NAME"));
                gp.setCustId (resultSet.getString ("CUST_ID"));
                gp.setCustGuid (resultSet.getString ("CUST_GUID"));
                gp.setPlannerGuid (resultSet.getString ("PLANNER_GUID"));
                gp.setPlannerNumber (resultSet.getString ("PLANNER_NUMBER"));
                gp.setPlannerUserName (resultSet.getString ("PLANNER_USER_NAME"));
                gp.setMgrGuid (resultSet.getString ("MANAGER_GUID"));
                gp.setMgrNumber (resultSet.getString ("MANAGER_NUMBER"));
                gp.setGroupId (resultSet.getString ("GROUP_ID"));
                gp.setCoPlanner ("Y".equalsIgnoreCase (resultSet.getString ("CO_PLANNER")));
                gp.setCompPlanId (resultSet.getInt ("COMP_PLAN_ID"));
                gp.setRelId (resultSet.getString ("REL_ID"));
                gp.setHierName (resultSet.getString ("GROUP_HIER_NAME"));
                gp.setMandatoryFilterId (resultSet.getInt ("MANDATORY_FILTER_ID"));
                gp.setLimitToEmpIds (resultSet.getString ("LIMIT_TO_EMP_IDS"));
                groups.add (gp);
            }
            resultSet.close ();
            stmt.close ();
        } catch (SQLException e) {
            e.printStackTrace ();
        }
        return groups;
    }

    /**
     * @param custId
     * @param userName
     * @return list of own (= managed, not co-planned) groups in any plan,
     *         accessible by the given user
     * @throws SQLException
     */
    public Collection <GroupPlanner> getAccessibleGroupsByUserName (String custId,
                                                                    String userName) throws SQLException {
        return getAccessibleGroups (Constants.custId, userName, null, true, false, false, false);
    }

    /**
     * @param custId
     * @param userName
     * @return list of own (= managed, not co-planned) groups in the given plan,
     *         accessible by the given user
     * @throws SQLException
     */
    public Collection <GroupPlanner> getAccessibleGroupsByUserNameAndPlan (String custId, String userName,
                                                                           int compPlanId) {
        return getAccessibleGroups (Constants.custId,
                                    userName,
                                    Constants.compPlanId,
                                    true,
                                    false,
                                    false,
                                    false);
    }

    /**
     * @param custId
     * @param compPlanId
     * @return one mgr for a review group of the given or any plan
     */
    public GroupPlanner getOneReviewingManager (String custId, Integer compPlanId) {
        Collection <GroupPlanner> g = getAccessibleGroups (Constants.custId,
                                                           null,
                                                           Constants.compPlanId,
                                                           true,
                                                           true,
                                                           false,
                                                           false);
        return g.stream ().findFirst ().orElse (null);
    }

    /**
     * @param custId
     * @param compPlanId
     * @return one co-planner in the given or any plan
     */
    public GroupPlanner getOneCoPlannerWithMultipleGroups (String custId, Integer compPlanId) {
        Collection <GroupPlanner> g = getAccessibleGroups (Constants.custId,
                                                           null,
                                                           Constants.compPlanId,
                                                           false,
                                                           false,
                                                           true,
                                                           false);
        return g.stream ().findFirst ().orElse (null);
    }

    /**
     * @param custId
     * @param compPlanId
     *            (NULL if any plan is ok)
     * @return a couple of users for the given (or any) plan
     */
    public Collection <GroupPlanner> getMixofCompPlanners (String custId, Integer compPlanId) {
        GroupPlanner rm = getOneReviewingManager (Constants.custId, Constants.compPlanId);
        GroupPlanner cp = getOneCoPlannerWithMultipleGroups (Constants.custId, Constants.compPlanId);
        Set <GroupPlanner> u = new HashSet <> ();
        u.add (rm);
        u.add (cp);
        return u;
    }
}
