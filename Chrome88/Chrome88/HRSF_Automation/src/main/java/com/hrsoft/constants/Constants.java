package com.hrsoft.constants;

import static com.hrsoft.config.ConfigFactory.getConfig;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.hrsoft.config.ConfigFactory;
import com.hrsoft.db.CompViewDbHelper;
import com.hrsoft.db.datadriven.model.CompPlan;
import com.hrsoft.db.datadriven.model.GroupPlanner;
import com.hrsoft.db.datadriven.model.Program;
import com.hrsoft.db.datadriven.model.TCCUser;

public class Constants {

	@SuppressWarnings("unused")
	private static String getCustId() {
		String url = getConfig().webAppicationURL().split("//")[1];
		custId = url.split("-")[0];
		return custId;
	}

	public static String dataviewReportName = "Employee Listing Report";
	public static String dataviewRenameReportName = dataviewReportName + " RENAMED";
	public static String modelViewTemplate = "Annual Increase Calibration Template (PUBLISHED)";
	public static int compPlanId;
	public static int compPlanId2;
	public static String compPlanName;
	public static String HrSoftUsername = getConfig().userName();
	public static String HrSoftPassword = getConfig().password();
	public static String HrSoftTestUrl = getConfig().webAppicationURL();
	public static String custId = "mercfin";
	public static String CompManagerFullName = "";
	public static String CompManagerFirstName = "";
	public static String CompManagerLastName = "";
	// getConfig
	public static String groupNameForGroupFilter = "";
	public static String custGuid = "";
	public static String managerGuid = "";
	public static String hierarchyGuid = "";
	public static String CompManagerUserName = "";
	public static String PlanForManagerView = "";
	public static String CoPlannerName = "";
	public static String GroupName = "";
	public static String managerToSearchGroup;
	public static String CoPlannerUserName = "";
	public static String TopPlannerGrp = "";
	public static String TopPlannerFullName = "";
	public static String ReviewingManagerUserName = "";
	public static int planIdForReviewingManagereWithRollup;
	public static String planNameForReviewingManagereWithRollup;
	public static int planIdForReviewingManager;
	public static String ReviewingManagerFullName = "";
	public static String ReviewingManagerUserNameWithRollup = "";
	public static String ReviewingManagerFullNameWithRollup = "";
	public static String JobRecommendation = "";
	public static String employeeWithAwardLink = "";
	public static String dataViewSearchText = "";
	public static String SMOKE_TEST_MASTER = System.getProperty("user.dir")
			+ "/src/test/resources/test-input/SmokeTestAutomation.xlsx";
	public static String CSV_SUMMARY_FILE = "[PLANNING-SUMMARY]" + PlanForManagerView;
	public static String PlanningCanEditWithDrawableGroup = "PLANNING_CAN_EDIT_WITHDRAWABLE_GROUP";
	public static String COMPENSATION_PLANS = "Compensation Plans";
	public static String CurrentSalary = "Current Salary";
	public static String CalibrationFile = "calibration grid";
	public static String PLAN_STATUS_DISTRIBUTION = "Plan Status Distribution.xlsx";
	public static String FILTER = "";
	public static String empSpareName = "";
	public static String empSpareId = "";
	public static String EmployeeUserName = "";
	public static String restrictedCoplannerFullName = "";
	public static String restrictedCoplannerUserName = "";
	public static String restrictedManagerUserName = "";
	public static String restrictedManagerFullName = "";
	public static String salaryProgramName = "";
	public static int salaryProgramId;
	public static int componentId;
	public static String componentName;
	public static String TopPlannerUserName = "";
	public static String managerGuidForManagerView = "";
	public static String EmployeeFirstName = "";
	public static String EmployeeLastName = "";
	public static String HRAdminUserName = "";
	public static String HRAdminDisplayName = "";
	public static String reviewManagerUserNameWithDistLink = "";
	public static String managerwithAwardLink = "";
	public static final String CSV_DOBJECT_FILES_PATH = System.getProperty("user.dir") + "/src/test/resources/"
			+ ConfigFactory.getConfig().csvDataObjectsPath();
	public static final String CSV_INIT_DOBJECT_DEF_FILE = System.getProperty("user.dir") + "/src/test/resources/"
			+ ConfigFactory.getConfig().csvBaseDobjectsDefPath();

	static {

		System.out.println("=".repeat(60));
		System.out.println("\u001B[1m" + "Test Data constants:" + "\u001B[0m");
		CompViewDbHelper compViewDbHelper = new CompViewDbHelper();

		CompPlan compPlanIdByCustId = compViewDbHelper.getCompPlanIdByCustId(Constants.custId);
		Constants.compPlanId = 3;
		Constants.compPlanId2 = 2;
		System.out.println("Plan ID being used: " + Constants.compPlanId);

		CompPlan compPlanNameByPlanId = compViewDbHelper.getCompPlanByPlanId(Constants.compPlanId);
		Constants.compPlanName = compPlanNameByPlanId.getName();
		System.out.println("Plan Name being used: " + Constants.compPlanName);
		System.out.println("Cust Name being used: " + Constants.custId);
		CompPlan compPlanByPlanId = compViewDbHelper.getCompPlanByPlanId(Constants.compPlanId);
		Constants.PlanForManagerView = compPlanByPlanId.getName();

		Collection<TCCUser> managerListByPlanId = compViewDbHelper.getManagerListByPlanId(Constants.compPlanId,
				Constants.custId);
		managerListByPlanId.forEach(s -> Constants.CompManagerFullName = s.getDisplayName());
		managerListByPlanId.forEach(s -> Constants.CompManagerFirstName = s.getFirstName());
		managerListByPlanId.forEach(s -> Constants.CompManagerLastName = s.getLastname());
		Set<String> setOfMgrNames = new LinkedHashSet<>();
		managerListByPlanId.forEach(w -> setOfMgrNames.add(w.getDisplayName()));
		Set<String> setOfMgrs = new LinkedHashSet<>();
		managerListByPlanId.forEach(e -> setOfMgrs.add(e.getUserName()));
		System.out.println("Managers usernames list is: " + setOfMgrs);
		System.out.println("Manager full name is: " + Constants.CompManagerFullName);
		System.out.println("Manager first name is: " + Constants.CompManagerFirstName);
		System.out.println("Manager last name is: " + Constants.CompManagerLastName);
		managerListByPlanId.forEach(s -> Constants.CompManagerUserName = s.getUserName());
		System.out.println("Comp manager user name is: " + Constants.CompManagerUserName);

		managerListByPlanId.forEach(s -> custGuid = s.getCustGuid());
		managerListByPlanId.forEach(s -> managerGuid = s.getManagerGuid());
		managerListByPlanId.forEach(s -> hierarchyGuid = s.getHierarchyGuid());

		GroupPlanner coPlannersListByManager = compViewDbHelper.getCoPlannersListByManager(custId, compPlanId,
				CompManagerUserName);
		Constants.CoPlannerName = coPlannersListByManager.getCoPlanner();
		System.out.println("Co-planner full name is: " + CoPlannerName);
		Constants.CoPlannerUserName = coPlannersListByManager.getCoPlannerUserName();
		System.out.println("Co-planner username is: " + CoPlannerUserName);
		Collection<CompPlan> topPlannerByCompId = compViewDbHelper.getTopPlannerByCompId(custId, compPlanId);
		topPlannerByCompId.forEach(a -> Constants.TopPlannerFullName = a.getTopPlannerFullName());
		topPlannerByCompId.forEach(a -> Constants.TopPlannerUserName = a.getTopPlannerUserName());

		Constants.TopPlannerGrp = Constants.TopPlannerFullName + " grp";
		System.out.println("Top planner full name is: " + TopPlannerFullName);
		System.out.println("Top planner user name is: " + TopPlannerUserName);
		CompPlan childGroupByManager = compViewDbHelper.getChildGroupByManager(Constants.TopPlannerUserName,
				Constants.compPlanId);
		Constants.GroupName = "LName00010, FName00010";// childGroupByManager.getGroupName();
		System.out.println("Group Name being used: " + Constants.GroupName);

		Constants.managerToSearchGroup = childGroupByManager.getManagerToSearchGroup();
		System.out.println("Manager proxied for search group Name being used: " + Constants.managerToSearchGroup);

		managerListByPlanId.forEach(s -> custGuid = s.getCustGuid());
		managerListByPlanId.forEach(s -> managerGuid = s.getManagerGuid());
		managerListByPlanId.forEach(s -> hierarchyGuid = s.getHierarchyGuid());
		System.out.println("Cust guid is: " + Constants.custGuid);
		Constants.groupNameForGroupFilter = compViewDbHelper.getGroupNameByPlanIdForDataView(Constants.compPlanId,
				Constants.custId);

		TCCUser oneReviewingManager = compViewDbHelper.getOneReviewingManager(Constants.custId);
		Constants.ReviewingManagerUserName = oneReviewingManager.getUserName();
		Constants.planIdForReviewingManager = oneReviewingManager.getPlanId();
		System.out.println("Reviewing Manager Username " + Constants.ReviewingManagerUserName + " and Plan Id : "
				+ Constants.planIdForReviewingManager);
		System.out.println("=".repeat(60));

		TCCUser reviewingManagerDetailsRollup = compViewDbHelper
				.getOneReviewingManagerWithRollupGroup(Constants.custId);
		Constants.ReviewingManagerUserNameWithRollup = reviewingManagerDetailsRollup.getUserName();
		Constants.planIdForReviewingManagereWithRollup = reviewingManagerDetailsRollup.getPlanId();
		Constants.planNameForReviewingManagereWithRollup = reviewingManagerDetailsRollup.getPlanName();
		System.out.println(
				"Plan Id used for Reviewing Managers Test case : " + Constants.planIdForReviewingManagereWithRollup
						+ " Plan Name :" + Constants.planNameForReviewingManagereWithRollup);
		Constants.ReviewingManagerFullNameWithRollup = reviewingManagerDetailsRollup.getDisplayName();
		System.out.println("Reviewing Manager UserName With Rollup:" + ReviewingManagerUserNameWithRollup);
		System.out.println("Reviewing Manager Display Name With Rollup:" + ReviewingManagerFullNameWithRollup);
		System.out.println("=".repeat(60));

		Constants.FILTER = compViewDbHelper.getFilterByCustIdAndPlanId(Constants.custGuid, Constants.compPlanId);
		System.out.println("Filter used in the advanced dropdown is: " + FILTER);
		Constants.ReviewingManagerFullName = oneReviewingManager.getDisplayName();
		System.out.println("Reviewing Manager Name :" + Constants.ReviewingManagerFullName);
		Constants.JobRecommendation = compViewDbHelper.getJobRecommendation(custId);
		System.out.println("Job Recommendation constant is: " + JobRecommendation);
		System.out.println("=".repeat(60));

		TCCUser user = compViewDbHelper.getEmployeeWithSalaryComponentByManager(Constants.custId,
				Constants.TopPlannerUserName, Constants.compPlanId);

		CompPlan spareName = compViewDbHelper.getEmpSpareFieldsByPlan(Constants.custId, Constants.compPlanId);
		Constants.empSpareName = spareName.getempSpareName();

		CompPlan spareId = compViewDbHelper.getEmpSpareFieldsByPlan(Constants.custId, Constants.compPlanId);
		Constants.empSpareId = spareId.getempSpareId();

		System.out.println("Emp Spare Field : " + empSpareName + empSpareId);

		TCCUser emp = compViewDbHelper.getOneEmployeeDetailsByCustId(Constants.custGuid);
		Constants.EmployeeUserName = emp.getUserName();
		Constants.EmployeeFirstName = emp.getFirstName();
		Constants.EmployeeLastName = emp.getLastname();
		System.out.println("Employee User Name : " + EmployeeUserName);
		System.out.println("Employee First Name : " + EmployeeFirstName);
		System.out.println("Employee Last Name : " + EmployeeLastName);
		setOfMgrs.forEach(u -> {
			if (!u.equals(CompManagerUserName))
				restrictedManagerUserName = u;
		});
		for (String s : setOfMgrNames) {
			if (!s.equals(CompManagerFullName))
				restrictedManagerFullName = s;
		}
		System.out.println("New manager username: " + restrictedManagerUserName);
		System.out.println("New manager full name: " + restrictedManagerFullName);
		GroupPlanner differentCoPlannerListByManager = compViewDbHelper.getCoPlannersListByManager(custId, compPlanId,
				restrictedManagerUserName);
		restrictedCoplannerFullName = differentCoPlannerListByManager.getCoPlanner();
		System.out.println("New co-planner full name: " + restrictedCoplannerFullName);
		restrictedCoplannerUserName = differentCoPlannerListByManager.getCoPlannerUserName();
		System.out.println("New co-planner username: " + restrictedCoplannerUserName);

		Program pg = compViewDbHelper.getSalaryProgramDetailsByPlanId(Constants.custId, Constants.compPlanId);
		Constants.salaryProgramName = pg.getProgramName();
		System.out.println("salary Tab Name : " + salaryProgramName);

		Constants.managerwithAwardLink = pg.getManagerForProgram();
		System.out.println("Manager for program : " + Constants.managerwithAwardLink);

		Constants.employeeWithAwardLink = pg.getEmpWithAwardLink();
		System.out.println("Employee with Award Link : " + employeeWithAwardLink);

		Constants.salaryProgramId = pg.getProgramId();
		System.out.println("Salary Program Id : " + salaryProgramId);

		Constants.componentId = pg.getSalarySubProgramId();
		System.out.println("Component Id : " + componentId);

		Constants.componentName = pg.getCompProgramSubName();
		System.out.println("Component Name : " + componentName);

		System.out.println("=".repeat(60));

		Constants.managerGuidForManagerView = user.getManagerGuid();

		System.out.println("=".repeat(60));

		TCCUser hradmin = compViewDbHelper.getOneHRAdmin(Constants.custId);
		Constants.HRAdminUserName = hradmin.getUserName();

		TCCUser hradminFullName = compViewDbHelper.getOneHRAdmin(Constants.custId);
		Constants.HRAdminDisplayName = hradminFullName.getDisplayName();

		TCCUser rmdUsername = compViewDbHelper.reviewingManagerWithDistFundLink(Constants.custId, Constants.compPlanId);
		Constants.reviewManagerUserNameWithDistLink = rmdUsername.getUserName();
		System.out.println("ReviewManager UserName With Dist Link : " + reviewManagerUserNameWithDistLink);
	}
	public static String RollupGroup = CompManagerFullName + " Grp (R)";
	public static String RestrictedPlanningPage = "PLAN " + FILTER;

}
