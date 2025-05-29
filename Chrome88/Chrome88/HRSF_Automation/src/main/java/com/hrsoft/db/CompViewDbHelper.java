
package com.hrsoft.db;

import static com.hrsoft.config.ConfigFactory.getConfig;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.hrsoft.constants.Constants;
import com.hrsoft.db.datadriven.model.Budget;
import com.hrsoft.db.datadriven.model.CompPlan;
import com.hrsoft.db.datadriven.model.Component;
import com.hrsoft.db.datadriven.model.Employee;
import com.hrsoft.db.datadriven.model.GroupPlanner;
import com.hrsoft.db.datadriven.model.PerfRating;
import com.hrsoft.db.datadriven.model.PerfRatingSet;
import com.hrsoft.db.datadriven.model.Program;
import com.hrsoft.db.datadriven.model.TCCUser;

/**
 * @author Annameni Srinivas
 *         <a href="mailto:sannameni@gmail.com">sannameni@gmail.com</a>
 */
public class CompViewDbHelper extends HrSoftDb {

	public Collection<CompPlan> getActiveCompPlansByCustId(String custId) {
		Collection<CompPlan> res = new ArrayList<>();
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(
					"SELECT p.* from V_RPT_COM_COMP_PLAN p inner join TCC_CUST_CFG c on c.cust_guid = p.cust_guid  WHERE p.PLAN_STAGE_ID = 4 and c.cust_id = ?");

			stmt.setString(1, getConfig().custId());
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				CompPlan c = new CompPlan();
				c.setId(resultSet.getInt("COMP_PLAN_ID"));
				c.setName(resultSet.getString("COMP_PLAN_NAME"));
				c.setRelId(resultSet.getString("REL_ID"));
				c.setHierName(resultSet.getString("HIER_NAME"));
				c.setPlanStageId(resultSet.getInt("PLAN_STAGE_ID"));
				c.setBaseCurrencyCode(resultSet.getString("BASE_CURRENCY_CODE"));
				res.add(c);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Collection<CompPlan> getActiveCompPlanByCompPlanId(int compPlanId) {
		Collection<CompPlan> res = new ArrayList<>();
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(
					"SELECT p.* from V_RPT_COM_COMP_PLAN p inner join TCC_CUST_CFG c on c.cust_guid = p.cust_guid  WHERE p.PLAN_STAGE_ID = 4 and p.COMP_PLAN_ID = ?");

			stmt.setInt(1, compPlanId);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				CompPlan c = new CompPlan();
				c.setId(resultSet.getInt("COMP_PLAN_ID"));
				c.setName(resultSet.getString("COMP_PLAN_NAME"));
				c.setRelId(resultSet.getString("REL_ID"));
				c.setHierName(resultSet.getString("HIER_NAME"));
				c.setPlanStageId(resultSet.getInt("PLAN_STAGE_ID"));
				c.setBaseCurrencyCode(resultSet.getString("BASE_CURRENCY_CODE"));
				res.add(c);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public CompPlan getCompPlanByPlanId(Integer planId) {
		PreparedStatement stmt;
		CompPlan res = new CompPlan();
		try {
			stmt = connection.prepareStatement("SELECT * from V_RPT_COM_COMP_PLAN p WHERE p.COMP_PLAN_ID = ?");

			stmt.setInt(1, planId);

			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				res = new CompPlan();
				res.setId(resultSet.getInt("COMP_PLAN_ID"));
				res.setName(resultSet.getString("COMP_PLAN_NAME"));
				res.setRelId(resultSet.getString("REL_ID"));
				res.setHierName(resultSet.getString("HIER_NAME"));
				res.setPlanStageId(resultSet.getInt("PLAN_STAGE_ID"));
				res.setBaseCurrencyCode(resultSet.getString("BASE_CURRENCY_CODE"));
				resultSet.close();
				stmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	public CompPlan getChildGroupByManager(String mgrUserName, int compPlanId) {
		PreparedStatement stmt;
		CompPlan res = new CompPlan();
		try {
			stmt = connection.prepareStatement(String.format(
					"SELECT Top 1 * from V_RPT_COM_GROUP_ROLLUP vrc inner join TCC_USER_DEMOGRAPHIC tud ON tud.USER_GUID = vrc.GROUP_MGR_GUID where tud.USER_NAME='%s' and COMP_PLAN_ID=%d;\r\n"
							+ "",
					mgrUserName, compPlanId));

			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				res.setManagerToSearchGroup(resultSet.getString("USER_NAME"));
				res.setGroupName(resultSet.getString("CHILD_GROUP_MGR_DISPLAY_NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return res;
	}

	public Collection<Budget> getBudgetsByPlanId(Integer planId) {
		Collection<Budget> res = new HashSet<>();
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("SELECT * from V_RPT_COM_COMP_PLAN_BUDGETS WHERE COMP_PLAN_ID = ?");

			stmt.setInt(1, planId);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Budget p = new Budget();
				p.setPlanId(resultSet.getInt("COMP_PLAN_ID"));
				p.setId(resultSet.getInt("BUDGET_ID"));
				p.setSequence(resultSet.getInt("sequence"));
				p.setName(resultSet.getString("NAME"));
				p.setType(resultSet.getString("TYPE"));
				p.setBasis(resultSet.getString("BASIS"));
				p.setAvailableBudget(resultSet.getDouble("available_budget"));
				p.setAvailableBudgetIsPCT(resultSet.getString("available_budget_is_pct"));
				p.setProtectedCurrency(resultSet.getString("PROTECTED_CURRENCY"));
				p.setEligibility(resultSet.getString("ELIGIBILITY"));
				p.setDefaultPCTForEmp(resultSet.getDouble("default_pct_per_emp"));
				p.setIsVisible(resultSet.getString("is_visible"));
				p.setBaseComponentName(resultSet.getString("base_component"));
				res.add(p);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Collection<Program> getProgramsByPlanId(Integer planId) {
		Collection<Program> res = new HashSet<>();
		try {
			PreparedStatement stmt = connection
					.prepareStatement("SELECT p.* from V_RPT_COM_COMP_PLAN_COMPONENTS p WHERE p.COMP_PLAN_ID = ?");
			stmt.setInt(1, planId);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Program p = new Program();
				p.setId(resultSet.getInt("COMP_PROG_ID"));
				p.setName(resultSet.getString("PROGRAM_NAME"));
				p.setOrder(resultSet.getInt("PROGRAM_ORDER"));
				p.setTypeId(resultSet.getInt("COMP_TYPE_ID"));
				p.setTypeName(resultSet.getString("COMP_TYPE"));
				res.add(p);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	private Collection<Component> getProgramComponentsByProgIdAndComponentType(Integer progId, Integer componentType) {
		Collection<Component> res = new ArrayList<>();
		String query = "SELECT p.* from V_RPT_COM_COMP_PLAN_COMPONENTS p WHERE p.COMP_PROG_ID = ? ";
		if (componentType != null)
			query = query + " AND COMP_TYPE_ID = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, progId);
			if (componentType != null)
				stmt.setInt(2, componentType);

			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Component p = new Component();
				p.setId(resultSet.getInt("COMP_PROG_SUB_ID"));
				p.setName(resultSet.getString("COMPONENT_NAME"));
				p.setOrder(resultSet.getInt("COMPONENT_ORDER"));
				p.setCurrency(resultSet.getString("COMPONENT_CURRENCY"));
				p.setAwardDate(resultSet.getDate("AWARD_DATE"));
				p.setEffectiveDateFrom(resultSet.getDate("EFFECTIVE_DATE_FROM"));
				p.setEffectiveDateTo(resultSet.getDate("EFFECTIVE_DATE_TO"));
				res.add(p);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Collection<Component> getSalaryProgramComponentsByProgId(Integer progId, Integer componentType) {
		return getProgramComponentsByProgIdAndComponentType(progId, Integer.valueOf(1));
	}

	public Collection<Component> getProgramComponentsByProgId(Integer progId) throws SQLException {
		return getProgramComponentsByProgIdAndComponentType(progId, null);
	}

	public Collection<PerfRatingSet> getPerfRatingSetsByPlanId(Integer planId) {
		Collection<PerfRatingSet> res = new HashSet<>();
		try {
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * from V_RPT_COM_COMP_PLAN_PERF_RATING_SETS WHERE COMP_PLAN_ID = ?");
			stmt.setInt(1, planId);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				PerfRatingSet p = new PerfRatingSet();
				p.setPlanId(resultSet.getInt("COMP_PLAN_ID"));
				p.setId(resultSet.getInt("PERF_RATING_SET_ID"));
				p.setName(resultSet.getString("PERF_RATING_SET_NAME"));
				p.setPriority(resultSet.getInt("PRIORITY"));
				p.setUserEditable(resultSet.getString("USER_EDITABLE"));
				res.add(p);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Collection<PerfRating> getPerfRatingsByPerfRatingSetId(Integer perfRatingSetId) {
		Collection<PerfRating> res = new HashSet<>();
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("SELECT * from V_RPT_COM_COMP_PLAN_PERF_RATINGS WHERE PERF_RATING_SET_ID = ?");

			stmt.setInt(1, perfRatingSetId);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				PerfRating p = new PerfRating();
				p.setPlanId(resultSet.getInt("COMP_PLAN_ID"));
				p.setId(resultSet.getInt("PERF_RATING_ID"));
				p.setName(resultSet.getString("PERF_RATING_NAME"));
				p.setOrder(resultSet.getInt("PERF_RATING_ORDER"));
				p.setTgtDistribution(resultSet.getDouble("TGT_DISTRIBUTION"));
				p.setIncludeInDistribution(resultSet.getString("INCL_IN_DISTRIBUTION"));
				p.setIsDefault(resultSet.getString("IS_DEFAULT"));
				res.add(p);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Collection<Employee> getEmployeesByPlanId(Integer planId) {
		Collection<Employee> res = new HashSet<>();
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("SELECT * from V_RPT_COM_EMP_PLAN WHERE COMP_PLAN_ID = ?");
			stmt.setInt(1, planId);
			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				Employee p = new Employee();

				p.setEmpId(resultSet.getInt("EMP_ID"));
				p.setMgrEmpId(resultSet.getInt("MGR_EMP_ID"));
				p.setMgrGuid(resultSet.getString("MGR_GUID"));
				p.setMgrDisplayName(resultSet.getString("MGR_DISPLAY_NAME"));
				p.setMgrNumber(resultSet.getString("MGR_NUMBER"));
				p.setMgrUserName(resultSet.getString("MGR_USER_NAME"));
				p.setFirstName(resultSet.getString("FIRST_NAME"));
				p.setLastName(resultSet.getString("LAST_NAME"));
				p.setMiddleName(resultSet.getString("MIDDLE_NAME"));
				p.setIsDefault(resultSet.getString("IS_DEFAULT"));
				p.setDisplayName(resultSet.getString("DISPLAY_NAME"));
				p.setEmailId(resultSet.getString("EMAIL_ID"));
				p.setJobCode(resultSet.getString("CURR_JOB_CODE"));
				p.setNewJobCode(resultSet.getString("NEW_JOB_CODE"));
				p.setPartTimeFactor(resultSet.getInt("PART_TIME_FACTOR"));
				p.setCurrSalaryActual(resultSet.getInt("CURR_SALARY_ACTUAL"));
				p.setPeriodFactor(resultSet.getInt("PERIOD_FACTOR"));
				p.setPayRateId(resultSet.getInt("PAY_RATE_ID"));
				p.setPayRateSchemeId(resultSet.getInt("PAY_RATE_SCHEME_ID"));
				p.setGeoPayId(resultSet.getString("GEO_PAY_ID"));
				p.setCurrencyCode(resultSet.getString("CURRENCY_CODE"));
				p.setWuid(resultSet.getInt("WUID"));
				p.setEmployeeNumber(resultSet.getString("EMPLOYEENUMBER"));
				p.setEmpUserName(resultSet.getString("EMP_USERNAME"));
				p.setChgBy(resultSet.getString("CHG_BY"));
				p.setJobTitle(resultSet.getString("CURR_JOB_TITLE"));
				p.setJobGrade(resultSet.getInt("JOB_GRADE"));
				p.setJobGradeName(resultSet.getString("JOB_GRADE_NAME"));
				p.setJobGradeRank(resultSet.getInt("JOB_GRADE_RANK"));
				p.setJobFamily(resultSet.getString("JOB_FAMILY"));
				res.add(p);
			}

			resultSet.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Collection<TCCUser> getManagerListByPlanId(Integer planId, String custId) {
		Collection<TCCUser> res = new HashSet<>();
		String sql = String.format("select gpa.*, tud.USER_NAME Co_Planner_UserName,tud.DISPLAY_NAME "
				+ "Co_Planner_Displayname, man.DISPLAY_NAME ManagerName, man.USER_NAME Manager_Username, man.FIRST_NAME, man.LAST_NAME\r\n"
				+ "From V_RPT_COM_GROUP_PLANNER_ACCESS gpa,\r\n" + "TCC_USER_DEMOGRAPHIC tud,\r\n"
				+ "TCC_USER_DEMOGRAPHIC man,\r\n" + "TCC_CUST_CFG tcc\r\n" + "where \r\n"
				+ "tud.CUST_GUID = gpa.CUST_GUID\r\n" + "and tud.USER_GUID = gpa.PLANNER_GUID\r\n"
				+ "and man.CUST_GUID = gpa.CUST_GUID\r\n" + "and man.USER_GUID = gpa.MANAGER_GUID\r\n"
				+ "and gpa.CO_PLANNER = 'Y'\r\n" + "and gpa.CUST_GUID = tcc.CUST_GUID\r\n"
				+ "and tcc.CUST_ID = '%s'\r\n" + "and gpa.COMP_PLAN_ID = '%s'\r\n"
				+ "and tud.USER_NAME not in ('talentcenter_admin', 'admin_user') OPTION (USE HINT ('QUERY_OPTIMIZER_COMPATIBILITY_LEVEL_110')) ", custId, planId);

		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				TCCUser user = new TCCUser();
//				user.setUserGuid(resultSet.getString("USER_GUID"));
				user.setCustGuid(resultSet.getString("CUST_GUID"));
				user.setDisplayName(resultSet.getString("ManagerName"));
//				user.setHierarchyGuid(resultSet.getString("HIERARCHY_GUID"));
//				user.setManagerGuid(resultSet.getString("MGR_GUID"));
				user.setUserName(resultSet.getString("Manager_Username"));
				user.setFirstName(resultSet.getString("FIRST_NAME"));
				user.setLastname(resultSet.getString("LAST_NAME"));
				res.add(user);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Collection<TCCUser> getEmployeeListByPlanId(String custId, Integer planId) {
		Collection<TCCUser> res = new HashSet<>();
		String sql = "SELECT * from V_RPT_COM_EMPLOYEE where COMP_PLAN_ID = ? and CUST_ID = ?";

		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sql);

			stmt.setInt(1, planId);

			stmt.setString(2, getConfig().custId());
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				TCCUser user = new TCCUser();
				user.setUserName(resultSet.getString("EMP_USERNAME"));
				user.setUserGuid(resultSet.getString("USER_GUID"));
				user.setDisplayName(resultSet.getString("DISPLAY_NAME"));
				res.add(user);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Collection<TCCUser> getEmployeesByManagerGuid(String custId, String managerGuid, Integer compPlanId,
			String groupType) {
		Collection<TCCUser> res = new HashSet<>();
		String selectSql = "select * from V_EMP_PLAN_MEMBERS_ROLLUP where group_id = "
				+ "(select top 1 p.group_id from V_RPT_COM_GROUP_PLANNER_ACCESS p, TCC_CUST_CFG cust where p.MANAGER_GUID = ? and p.COMP_PLAN_ID = ?"
				+ " and p.CUST_GUID = cust.CUST_GUID and cust.CUST_ID = ? )"
				+ " and COMP_PLAN_ID = ?  and IS_CO_PLANNER = 'N' and IS_DIRECT_GROUP = ? ";

		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(selectSql);

			String t_groupType = "R".equalsIgnoreCase(groupType) ? "R" : "D";
			stmt.setString(1, managerGuid);
			stmt.setInt(2, Constants.compPlanId);
			stmt.setString(3, Constants.custId);
			stmt.setInt(4, Constants.compPlanId);
			stmt.setString(5, t_groupType);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				TCCUser user = new TCCUser();
				user.setManagerGuid(resultSet.getString("MANAGER_GUID"));
				user.setUserName(resultSet.getString("EMP_USERNAME"));
				user.setUserGuid(resultSet.getString("USER_GUID"));
				user.setDisplayName(resultSet.getString("DISPLAY_NAME"));
				res.add(user);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public CompPlan getCompPlanIdByCustId(String custId) {
		PreparedStatement stmt;
		CompPlan compPlanId = new CompPlan();
		try {
			stmt = connection.prepareStatement(String.format(
					"select  * from  V_RPT_COM_EMPLOYEE p WHERE p.CUST_ID= '%s' ORDER BY p.COMP_PLAN_ID DESC ;",
					custId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				compPlanId = new CompPlan();
				compPlanId.setId(resultSet.getInt("COMP_PLAN_ID"));
				resultSet.close();
				stmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return compPlanId;
	}

	public TCCUser getEmployeeWithSalaryComponentByManager(String custId, String manager, int planId) {
		PreparedStatement stmt;
		TCCUser user = new TCCUser();
		try {
			stmt = connection.prepareStatement(String.format(
					"select TOP 1 cc.CUST_ID, c.COMP_PLAN_ID, c.COMP_PLAN_NAME, c.HIER_NAME, c.REL_ID, c.PLAN_STAGE_ID, e.GROUP_ID, e.EMP_ID, e.EMPLOYEENUMBER,e.DISPLAY_NAME, e.EMP_USERNAME, e.MGR_NUMBER, e.MGR_USERNAME,\r\n"
							+ "							        a.COMP_PROG_ID, a.COMP_PROG_NAME, a.COMP_PROG_SUB_ID, a.COMP_PROG_SUB_NAME\r\n"
							+ "							from V_RPT_COM_EMPLOYEE e\r\n"
							+ "							inner join V_RPT_COM_COMP_PLAN c on c.CUST_GUID = e.CUST_GUID and c.COMP_PLAN_ID = e.COMP_PLAN_ID\r\n"
							+ "							inner join TCC_CUST_CFG cc on cc.CUST_GUID = c.CUST_GUID\r\n"
							+ "							inner join V_RPT_COM_EMP_AWARD a on a.CUST_GUID = e.CUST_GUID and a.COMP_PLAN_ID = e.COMP_PLAN_ID and a.USER_GUID = e.USER_GUID and a.PAY_RATE_ID is not null\r\n"
							+ "							inner join V_COMPONENT co on co.comp_plan_id = a.COMP_PLAN_ID and co.COMP_PROG_SUB_ID = a.COMP_PROG_SUB_ID\r\n"
							+ "							where a.status_id in (10,20,40) and e.MGR_USERNAME='%s'\r\n"
							+ "							and co.ALLOW_DISCRETION in ('A','Y')         and co.IS_READONLY = 'N'\r\n"
							+ "							and cc.CUST_ID = '%s' and c.COMP_PLAN_ID=%d",
					manager, custId, planId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				user = new TCCUser();
				user.setDisplayName(resultSet.getString("DISPLAY_NAME"));
				resultSet.close();
				stmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public TCCUser getManagerWithDistributionFundLink(String custId, int planId) {
		PreparedStatement stmt;
		TCCUser user = new TCCUser();
		try {
			stmt = connection.prepareStatement(String.format(
					"select top 1 * from V_RPT_COM_EMPLOYEE e inner join V_RPT_COM_GROUP_BUDGET_FUNDING f on  e.CUST_GUID = f.CUST_GUID AND e.COMP_PLAN_ID = f.COMP_PLAN_ID inner join TCC_CUST_CFG cc on cc.CUST_GUID = f.CUST_GUID\r\n"
							+ "			where f.CAN_MGR_DISTRIBUTE = 'Y' \r\n"
							+ "			and cc.CUST_ID = '%s' and e.COMP_PLAN_ID=%d",
					custId, planId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				user = new TCCUser();
				user.setUserName(resultSet.getString("MGR_USERNAME"));
				resultSet.close();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public TCCUser getOneReviewingManager(String custId) {
		PreparedStatement stmt;
		TCCUser user = new TCCUser();
		try {
			stmt = connection.prepareStatement(String.format(
					"select distinct  TOP 1 cc.CUST_ID, r.COMP_PLAN_ID, r.HIER_NAME, tud.EMP_ID AS MANAGER_NUMBER, tud.USER_WUID AS MANAGER_ID, tud.USER_NAME AS MANAGER_USER_NAME, tud.DISPLAY_NAME AS MANAGER_DISPLAY_NAME\r\n"
							+ "	from V_RPT_COM_GROUP_ROLLUP r\r\n"
							+ " inner join V_GROUP_STATUS vgs on r.GROUP_ID=vgs.GROUP_ID\r\n"
							+ "	inner join PLAN_STATUS_DESCR psd on psd.STATUS_ID = vgs.STATUS_ID \r\n"
							+ "	inner join TCC_CUST_CFG cc on cc.CUST_GUID = r.CUST_GUID\r\n"
							+ "	inner join TCC_USER_DEMOGRAPHIC tud on tud.CUST_GUID = r.CUST_GUID and tud.USER_GUID = r.GROUP_MGR_GUID\r\n"
							+ "	where cc.CUST_ID = '%s' and r.HIER_LEVEL>1 and psd.STATE='Planning'",
					custId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				user = new TCCUser();
				user.setPlanId(resultSet.getInt("COMP_PLAN_ID"));
				user.setUserName(resultSet.getString("MANAGER_USER_NAME"));
				user.setDisplayName(resultSet.getString("MANAGER_DISPLAY_NAME"));
				resultSet.close();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public TCCUser getOneReviewingManagerWithRollupGroup(String custId) {
		PreparedStatement stmt;
		TCCUser user = new TCCUser();
		try {
			stmt = connection.prepareStatement(String.format(
					"select distinct  TOP 1 cp.COMP_PLAN_NAME, r.HIER_LEVEL,cc.CUST_ID, r.COMP_PLAN_ID, r.HIER_NAME, tud.EMP_ID AS MANAGER_NUMBER, tud.USER_WUID AS MANAGER_ID, tud.USER_NAME AS MANAGER_USER_NAME, tud.DISPLAY_NAME AS MANAGER_DISPLAY_NAME from V_RPT_COM_GROUP_ROLLUP r\r\n"
							+ "												 inner join V_GROUP_STATUS vgs on r.GROUP_ID=vgs.GROUP_ID\r\n"
							+ "													inner join PLAN_STATUS_DESCR psd on psd.STATUS_ID = vgs.STATUS_ID \r\n"
							+ "													inner join V_RPT_COM_COMP_PLAN cp on cp.COMP_PLAN_ID=r.COMP_PLAN_ID\r\n"
							+ "													inner join TCC_CUST_CFG cc on cc.CUST_GUID = r.CUST_GUID\r\n"
							+ "													inner join TCC_USER_DEMOGRAPHIC tud on tud.CUST_GUID = r.CUST_GUID and tud.USER_GUID = r.GROUP_MGR_GUID\r\n"
							+ "													where cc.CUST_ID = '%s' and r.HIER_LEVEL>2 and psd.STATE='Planning'",
					custId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				user = new TCCUser();
				user.setPlanId(resultSet.getInt("COMP_PLAN_ID"));
				user.setPlanName(resultSet.getString("COMP_PLAN_NAME"));
				user.setUserName(resultSet.getString("MANAGER_USER_NAME"));
				user.setDisplayName(resultSet.getString("MANAGER_DISPLAY_NAME"));
				resultSet.close();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public TCCUser getGroupManagerWithRedAlert(String custId, int planId) {
		PreparedStatement stmt;
		TCCUser user = new TCCUser();
		try {
			stmt = connection.prepareStatement(String.format(
					"select distinct top 10 a.COMP_PLAN_ID, a.GROUP_ID, a.GROUP_TYPE, gp.GROUP_NAME, gp.MANAGER_NUMBER, gp.MGR_DISPLAY_NAME \r\n"
							+ "from V_ROLLUP_GROUP_ALERT_COUNT a\r\n"
							+ "inner join CONFIG_ALERT ca on a.alert_id = ca.ALERT_ID and a.comp_plan_id = ca.COMP_PLAN_ID and a.rule_id = ca.RULE_ID\r\n"
							+ "inner join V_RPT_COM_GROUP_PLAN gp on gp.COMP_PLAN_ID = a.COMP_PLAN_ID AND gp.GROUP_ID = a.GROUP_ID AND gp.GROUP_TYPE = a.GROUP_TYPE\r\n"
							+ "inner join TCC_CUST_CFG cc on gp.CUST_GUID = cc.CUST_GUID\r\n"
							+ "where ca.ENABLED = 'Y' \r\n"
							+ "and a.IS_HARDSTOP = 'Y'  -- use 'N' to find non-red alerts\r\n"
							+ "and a.HAS_FIRED = 'Y' \r\n" + "and cc.cust_id = '%s'\r\n" + "and a.COMP_PLAN_ID = %d",
					custId, planId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				user = new TCCUser();
				user.setUserName(resultSet.getString("MANAGER_USER_NAME"));
				user.setDisplayName(resultSet.getString("MANAGER_DISPLAY_NAME"));
				resultSet.close();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

//	public TCCUser getManagerWithBothDirectAndRollupGrp(String custId, int planId) {
//		PreparedStatement stmt;
//		TCCUser user = new TCCUser();
//		try {
//			stmt = connection.prepareStatement(String.format(
//					"select distinct top 1 cc.CUST_ID, r.COMP_PLAN_ID, r.HIER_NAME, tud.EMP_ID AS MANAGER_NUMBER, tud.USER_WUID AS MANAGER_ID, tud.USER_NAME AS MANAGER_USER_NAME, tud.DISPLAY_NAME AS MANAGER_DISPLAY_NAME\r\n"
//					+ "	from V_RPT_COM_GROUP_ROLLUP r\r\n"
//					+ "	inner join TCC_CUST_CFG cc on cc.CUST_GUID = r.CUST_GUID\r\n"
//					+ "	inner join TCC_USER_DEMOGRAPHIC tud on tud.CUST_GUID = r.CUST_GUID and tud.USER_GUID = r.GROUP_MGR_GUID\r\n"
//					+ "	where cc.CUST_ID = '%s'\r\n"
//					+ "	and r.COMP_PLAN_ID = %d",
//					custId, planId));
//			ResultSet resultSet = stmt.executeQuery();
//			if (resultSet.next()) {
//				user = new TCCUser();
//				user.setUserName(resultSet.getString("MANAGER_USER_NAME"));
//				user.setDisplayName(resultSet.getString("MANAGER_DISPLAY_NAME"));
//				resultSet.close();
//				stmt.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return user;
//	}
//	

	public GroupPlanner getCoPlannersListByManager(String custId, int planId, String mgrUser) {
		PreparedStatement stmt;
		GroupPlanner groupPlanner = new GroupPlanner();
		String sqlQuery = String.format(
				"select top 1 gpa.*, tud.USER_NAME Co_Planner_UserName,tud.DISPLAY_NAME Co_Planner_Displayname, man.DISPLAY_NAME ManagerName, man.USER_NAME Manager_Username\r\n"
						+ "From V_RPT_COM_GROUP_PLANNER_ACCESS gpa,\r\n" + "TCC_USER_DEMOGRAPHIC tud,\r\n"
						+ "TCC_USER_DEMOGRAPHIC man,\r\n" + "TCC_CUST_CFG tcc\r\n" + "where \r\n"
						+ "tud.CUST_GUID = gpa.CUST_GUID\r\n" + "and tud.USER_GUID = gpa.PLANNER_GUID\r\n"
						+ "and man.CUST_GUID = gpa.CUST_GUID\r\n" + "and man.USER_GUID = gpa.MANAGER_GUID\r\n"
						+ "and gpa.CO_PLANNER = 'Y'\r\n" + "and gpa.CUST_GUID = tcc.CUST_GUID\r\n"
						+ "and tcc.CUST_ID = '%s'\r\n" + "and gpa.COMP_PLAN_ID = '%s'\r\n"
						+ "and tud.USER_NAME not in ('talentcenter_admin', 'admin_user') and man.USER_NAME='%s';",
				custId, planId, mgrUser);
		try {
			stmt = connection.prepareStatement(sqlQuery);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				groupPlanner.setCoPlanner(resultSet.getString("Co_Planner_Displayname"));
				groupPlanner.setCoPlannerUserName(resultSet.getString("Co_Planner_UserName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupPlanner;
	}

	public Collection<CompPlan> getTopPlannerByCompId(String custId, int compPlanId) {

		Collection<CompPlan> res = new HashSet<>();
		String sqlQuery = String.format("select top 1 gpa.GROUP_HIER_NAME, gpa.GROUP_ID, \r\n"
				+ "tud.USER_NAME, tud.first_name, tud.MIDDLE_NAME, tud.LAST_NAME, tud.DISPLAY_NAME, tud.USER_WUID, tud.USER_GUID\r\n"
				+ "from V_RPT_COM_GROUP_PLANNER_ACCESS gpa\r\n"
				+ "inner join GROUP_HIER_DESCR ghd ON ghd.REL_ID = gpa.REL_ID and ghd.ROOT_GROUP_ID = gpa.GROUP_ID\r\n"
				+ "inner join TCC_CUST_CFG cc on cc.CUST_GUID = gpa.CUST_GUID\r\n"
				+ "inner join TCC_USER_DEMOGRAPHIC tud on cc.CUST_GUID = tud.CUST_GUID and gpa.MANAGER_GUID = tud.USER_GUID\r\n"
				+ "where cc.CUST_ID = '%s'\r\n" + "and gpa.CO_PLANNER = 'N' and gpa.COMP_PLAN_ID = %s ", custId,
				compPlanId);
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sqlQuery);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				CompPlan user = new CompPlan();
				user.setTopPlannerFullName(resultSet.getString("DISPLAY_NAME"));
				user.setTopPlannerUserName(resultSet.getString("USER_NAME"));
				res.add(user);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getFilterByCustIdAndPlanId(String custguid, int compPlanId) {
		PreparedStatement stmt;
		String filter = "";
		String sqlQuery = String.format(
				"Select top 1 * from V_RPT_COM_COMP_PLAN_FILTERS where CUST_GUID ='%s' and COMP_PLAN_ID = %s order by FILTER_NAME desc",
				custguid, compPlanId);
		try {
			stmt = connection.prepareStatement(sqlQuery);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				filter = resultSet.getString("FILTER_NAME");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return filter;
	}

	public String getJobRecommendation(String custId) {
		PreparedStatement stmt;
		String jobrecom = "";
		String sqlQuery = String.format("Select TOP 1 * FROM JOB_CODE WHERE CUST_ID ='%s'", custId);
		try {
			stmt = connection.prepareStatement(sqlQuery);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				jobrecom = resultSet.getString("JOB_TITLE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jobrecom;
	}

	public CompPlan getEmpSpareFieldsByPlan(String custId, int planId) {
		PreparedStatement stmt;
		CompPlan spare = new CompPlan();
		String sql = String.format(
				"SELECT DISTINCT TOP 1 *  from V_RPT_COM_COMP_PLAN_EMP_SPARES  cps JOIN TCC_CUST_CFG tcc on cps.CUST_GUID = tcc.CUST_GUID where cps.DATA_TYPE='N' and tcc.CUST_ID='%s' and cps.COMP_PLAN_ID=%d",
				custId, planId);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				spare.setempSpareId(resultSet.getString("EMP_SPARES_ID"));
				spare.setempSpareName(resultSet.getString("NAME"));
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return spare;
	}

	public TCCUser getOneEmployeeDetailsByCustId(String custId) {
		PreparedStatement stmt;
		TCCUser user = new TCCUser();
		try {
			stmt = connection.prepareStatement(String.format(
					"SELECT TOP 1 * FROM TCC_USER_DEMOGRAPHIC tcc join TCC_CUST_CFG cfg ON tcc.CUST_GUID=cfg.CUST_GUID WHERE cfg.CUST_ID='mercfin'\r\n"
							+ "",
					custId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				user = new TCCUser();
				user.setDisplayName(resultSet.getString("DISPLAY_NAME"));
				user.setUserName(resultSet.getString("USER_NAME"));
				user.setFirstName(resultSet.getString("FIRST_NAME"));
				user.setLastname(resultSet.getString("LAST_NAME"));
				resultSet.close();
				stmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public String getGroupNameByPlanIdForDataView(int custId, String planId) {
		PreparedStatement stmt;
		String grp = "";
		String sqlQuery = String
				.format("select DISTINCT TOP 1 gpa.GROUP_NAME from V_RPT_COM_GROUP_PLANNER_ACCESS_ROLLUP\r\n"
						+ "gpa JOIN TCC_CUST_CFG tcc ON  tcc.CUST_GUID=gpa.CUST_GUID inner join TCC_USER_DEMOGRAPHIC tud ON tud.USER_GUID = gpa.PLANNER_GUID\r\n"
						+ "WHERE tcc.CUST_ID='%s' and comp_plan_id=%d \r\n", planId, custId);

		try {
			stmt = connection.prepareStatement(sqlQuery);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				grp = resultSet.getString("GROUP_NAME");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return grp;
	}

	public Program getSalaryProgramDetailsByPlanId(String custId, int planId) {
		PreparedStatement stmt;
		Program prog = new Program();
		String sql = String.format(
				"select distinct top 1 e.DISPLAY_NAME,a.COMP_PROG_SUB_ID, cc.CUST_ID, c.COMP_PLAN_ID,a.COMP_TYPE_ID, c.COMP_PLAN_NAME, c.HIER_NAME, c.REL_ID, c.PLAN_STAGE_ID, e.GROUP_ID, e.MGR_NUMBER, e.MGR_USERNAME,\r\n"
						+ "        a.COMP_PROG_ID, a.COMP_PROG_NAME, ( CASE WHEN a.COMP_TYPE_ID = 1 THEN 'Salary' ELSE (CASE WHEN a.COMP_TYPE_ID = 3 THEN 'Variable Pay' ELSE 'Stock' END) END) as PROG_TYPE, a.COMP_PROG_SUB_NAME\r\n"
						+ "from V_RPT_COM_EMPLOYEE e\r\n"
						+ "inner join V_RPT_COM_COMP_PLAN c on c.CUST_GUID = e.CUST_GUID and c.COMP_PLAN_ID = e.COMP_PLAN_ID\r\n"
						+ "inner join TCC_CUST_CFG cc on cc.CUST_GUID = c.CUST_GUID\r\n"
						+ "inner join V_RPT_COM_EMP_AWARD a on a.CUST_GUID = e.CUST_GUID and a.COMP_PLAN_ID = e.COMP_PLAN_ID and a.USER_GUID = e.USER_GUID\r\n"
						+ "where cc.CUST_ID = '%s' and c.COMP_PLAN_ID=%d",
				custId, planId);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				prog.setProgramName(resultSet.getString("COMP_PROG_NAME"));
				prog.setProgramId(resultSet.getInt("COMP_PROG_ID"));
				prog.setSalarySubProgramId(resultSet.getInt("COMP_PROG_SUB_ID"));
				prog.setCompProgramSubName(resultSet.getString("COMP_PROG_SUB_NAME"));
				prog.setManagerForProgram(resultSet.getString("MGR_USERNAME"));
				prog.setEmpWithAwardLink(resultSet.getString("DISPLAY_NAME"));
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prog;
	}

	public TCCUser getOneHRAdmin(String custId) {
		PreparedStatement stmt;
		TCCUser user = new TCCUser();
		try {
			stmt = connection.prepareStatement(String.format("\r\n" + "\r\n"
					+ "select distinct  top 1 c.CUST_ID, tud.USER_GUID, tud.USER_NAME,tud.DISPLAY_NAME, tud.USER_WUID\r\n"
					+ "from TCC_USER_DEMOGRAPHIC tud\r\n"
					+ "inner join TCC_USER_ROLE_XREF ur on ur.CUST_GUID = tud.CUST_GUID and ur.ACTING_USER_GUID = tud.USER_GUID\r\n"
					+ "inner join TCC_CUST_CFG c on c.CUST_GUID = tud.CUST_GUID\r\n" + "where c.CUST_ID = 'mercfin'\r\n"
					+ "and ur.ROLE_ID like 'HR_ADMIN'", custId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				user = new TCCUser();
				user.setDisplayName(resultSet.getString("DISPLAY_NAME"));
				user.setUserName(resultSet.getString("USER_NAME"));
				resultSet.close();
				stmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public TCCUser reviewingManagerWithDistFundLink(String custId, int planId) {
		PreparedStatement stmt;
		TCCUser user = new TCCUser();
		try {
			stmt = connection.prepareStatement(String.format(
					"select distinct TOP 1 cc.CUST_ID, f.COMP_PLAN_ID, c.COMP_PLAN_NAME, c.HIER_NAME, ud.USER_GUID AS MGR_USER_GUID, ud.USER_NAME AS MGR_USER_NAME, ud.EMP_ID AS MGR_NUMBER\r\n"
							+ "from V_RPT_COM_GROUP_BUDGET_FUNDING f\r\n"
							+ "inner join V_RPT_COM_COMP_PLAN c ON c.CUST_GUID = f.CUST_GUID AND c.COMP_PLAN_ID = f.COMP_PLAN_ID\r\n"
							+ "inner join TCC_CUST_CFG cc on cc.CUST_GUID = f.CUST_GUID\r\n"
							+ "inner join V_RPT_COM_GROUP_PLANNER_ACCESS a ON a.CUST_GUID = f.CUST_GUID and a.COMP_PLAN_ID = f.COMP_PLAN_ID and a.GROUP_ID = f.REVIEWER_GROUP_ID\r\n"
							+ "inner join TCC_USER_DEMOGRAPHIC ud ON ud.CUST_GUID = a.CUST_GUID and ud.USER_GUID = a.MANAGER_GUID\r\n"
							+ "where f.CAN_MGR_DISTRIBUTE = 'Y' and f.GROUP_BUDGET_STATUS = 'Budgeted'\r\n"
							+ "and cc.CUST_ID = '%s' and f.COMP_PLAN_ID=%d\r\n" + "\r\n" + "",
					custId, planId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				user = new TCCUser();
				user.setUserName(resultSet.getString("MGR_USER_NAME"));
				resultSet.close();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
}
