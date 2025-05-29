package com.hrsoft.test.modelview;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.modelview.ModelViewPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

public class ModelViewTestSuite extends WebBaseTest {

	private String randomTitle = " Test Automation " + RandomStringUtils.randomAlphabetic(3);
	private String currencyTitle = "currency title" + RandomStringUtils.randomAlphabetic(3);
	private ModelViewPage modelView = new ModelViewPage();

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_177CreateModelviewSession(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.clickNewSessionTemplate(Constants.modelViewTemplate);
		modelView.selectSessionType("Create from baseline template").enterSessionTitle(randomTitle).saveSession();
		modelView.isSessionAdded(randomTitle);
		modelView.clickShowUnderManager().selectManagerDropdown();
		modelView.discardSession();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_178ModelviewEditAnySession(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.editModelViewTemplate(Constants.modelViewTemplate);
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_177AModelviewEditAnySessionByCloning(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.clickNewSessionTemplate(Constants.modelViewTemplate);
		modelView.selectSessionType("Clone from existing session").selectSession().enterSessionTitle(randomTitle)
				.saveSession();
		modelView.isSessionAdded(randomTitle);
		modelView.clickShowUnderManager().selectManagerDropdown();
		modelView.discardSession();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_179AModelViewclickNewTemplatWhichPopupsTemplate(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.clickNewTemplateButton().isTemplatePopupOpened();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest", priority = 5)
	public void SM_184ModelViewSelectDiscardStatusFromDropdownAndVerifySessionStatus(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.selectStatus("Discarded");
		String selectedStatus = modelView.getStatusValue();
		doWait(4000);
		String statusInGrid = modelView.statusList(selectedStatus);
		System.out.println("Actual : " + statusInGrid);
		assertEquals(selectedStatus, statusInGrid);

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_185ModelViewOpenSettingsPage(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.openSettingsPage();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_27_28_ModelViewCalibrationFilters(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();

		modelView.isNavigatedToModelView().clickNewSessionTemplate(Constants.modelViewTemplate)
				.enterSessionTitle(randomTitle).saveSession();
		assertTrue(modelView.isSessionAdded(randomTitle));
		modelView.clickAndSearchEmployeeFilter(Constants.CompManagerFullName);
		assertTrue(modelView.isFilteredEmployee(Constants.CompManagerFullName));
		modelView.clickBulkLock();
		modelView.isBulkUnlock();

	}

	@Author(name = QAResources.SRINIVAS) // In-progress
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_26_ModelViewCalibrationFilters(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.isNavigatedToModelView();
		modelView.clickNewSessionTemplate(Constants.modelViewTemplate).enterSessionTitle(randomTitle).saveSession();
		assertTrue(modelView.isSessionAdded(randomTitle));
		modelView.clickBulkUpdate().selectComponent("Calibrated Merit Percent").enterNumericValue("1")
				.clickApplyChanges().clickPublish();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_200_ModelView_CanSaveMapping(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.isNavigatedToModelView().clickEditTemplateButton(Constants.modelViewTemplate).clickFieldMapping()
				.clickInlineEditForFieldMapping().clickSaveAndClose();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_201_ModelView_CanSaveMapping(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.isNavigatedToModelView().clickEditTemplateButton(Constants.modelViewTemplate).clickSessions()
				.clickInlineEditForSessions();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_202_ModelView_CanDeleteSession(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.isNavigatedToModelView().clickEditTemplateButton(Constants.modelViewTemplate).clickSessions()
				.clickDeleteEditForSessions();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_203_ModelView_ChangeCurrency(TestDataExcel data) {
		String currency = "CAD";
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.isNavigatedToModelView().clickOpenCalibrationSession(Constants.modelViewTemplate);
		String beforeCurrency = modelView.getCurrencyFromPage();
		System.out.println(beforeCurrency);
		modelView.SelectCurrencyFromDropDown(currency);
		String AfterCurrency = modelView.getCurrencyFromPage();
		assertEquals(AfterCurrency, currency);
		assertNotEquals(AfterCurrency, beforeCurrency);

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_186_ModelViewCanEditTemplateWithEditIconInSettingPage(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.clickSettingsButton().clickEditIconInSettingsPageAndCheckPopupOpens();

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_187_ModelViewCanCloneTemplateWithEditIconInSettingPage(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.clickSettingsButton().cloneTemplate(randomTitle).discardClonedTemplate(randomTitle);

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_188_ModelViewEditManageCurrencyAndVerifyTheChanges(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.clickSettingsButton().openSettingsPageTabs("Manage Currency");
		modelView.isManageCurrencyOpened();
		String enteredRateToBase = modelView.clickEditIconInCurrecnyPopupAndEnterRateToBase();
		String rateToBaseAfterEdit = modelView.getRateToBase();
		assertEquals(enteredRateToBase, rateToBaseAfterEdit);

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest", enabled = false)
	public void SM_190_ModelViewManageUnitPage(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.clickSettingsButton().openSettingsPageTabs("Manage Currency");
		modelView.isManageCurrencyOpened();
		modelView.clickManageUnits();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_204_ModelView_SessionProcess_CanEnableOrDisable(TestDataExcel data) {
		createNewSessionInModelView(data);
		assertEquals(modelView.getEmployeeCurrency(), "INR 1.00");
		modelView.clickShowEmployeeInOwnCurrencyCheckbox();
		assertEquals(modelView.getEmployeeCurrency(), "USD 0.01");
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_205_ModelView_SessionProcess_CanHideChart(TestDataExcel data) {
		createNewSessionInModelView(data);
		assertTrue(modelView.isChartsAndCards());
		modelView.clickHideChartButton();
		assertFalse(modelView.isChartsAndCards());
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_206_207_208_209_ModelView_SessionProcess_CanSelectManagerFromManagerList(TestDataExcel data) {
		createNewSessionInModelView(data);
		modelView.clickShowAllRadioButton();
		assertTrue(modelView.isEmployeesUnderManagerGrid());

		modelView.clickShowUnderManager();
		assertTrue(modelView.isNoSessionData());
		assertFalse(modelView.isEmployeesUnderManagerGrid());
		modelView.selectValueFromManagerDropdown(Constants.CompManagerFullName)

				.clickDirectRadioButton();
		assertTrue(modelView.isEmployeesUnderManagerGrid());

		modelView.clickRollupRadioButton();
		assertTrue(modelView.isEmployeesUnderManagerGrid());
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_210_211_ModelView_SessionProcess_CanSelectManagerFromManagerList(TestDataExcel data) {
		createNewSessionInModelView(data);
		modelView.clickShowAllRadioButton();
		assertTrue(modelView.isEmployeesUnderManagerGrid());
		modelView.clickFunnelFilterIconFor(Constants.CurrentSalary);
		modelView.clickClearFilter();

	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_212_ModelView_SessionProcess_CanDoBulkUpdate(TestDataExcel data) {
		createNewSessionInModelView(data);
		modelView.clickFunnelFilterIconFor(Constants.CurrentSalary).chooseFilterAndEnterValue("greater", "100");

		modelView.clickBulkUpdate().selectComponent("Calibrated Merit Percent").selectType("Increase by %")
				.enterNumericValue("5").clickApplyChanges();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_213_ModelView_SessionProcess_CanDoBulkUpdate(TestDataExcel data) {
		createNewSessionInModelView(data);
		modelView.clickFunnelFilterIconFor(Constants.CurrentSalary).chooseFilterAndEnterValue("greater", "100");
		modelView.clickBulkUpdate().selectComponent("Calibrated Merit Amount").selectType("Increase by %")
				.selectRecommendedGuideline("Max").clickApplyChanges();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_214_215_216_217_ModelView_SessionProcess_UserActions(TestDataExcel data) {
		createNewSessionInModelView(data);
		modelView.clickInlineAction();
		assertTrue(modelView.isInlineAction());
		modelView.clickClose().clickInlineToLock().clickInlineToUnLock().clickDownload();
		modelView.isFileDownloaded(Constants.CalibrationFile);
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_218_ModelView_SessionProcess_BulkLock(TestDataExcel data) {
		createNewSessionInModelView(data);
		modelView.clickFunnelFilterIconFor(Constants.CurrentSalary).chooseFilterAndEnterValue("greater", "10")
				.clickBulkLock().clickBulkUnLock();
	}

	@Author(name = QAResources.SRINIVAS)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_219_220_221_ModelView_SessionProcess_UserActions(TestDataExcel data) {
		createNewSessionInModelView(data);
		String b = modelView.getBackGroundColorOfCalibrationGrid();
		assertEquals(b, "#000000");
		modelView.clickMarkAsDone();
		String s = modelView.getBackGroundColorOfCalibrationGrid();
		assertEquals(s, "#00c851");
		modelView.selectPaginationNumber("25");
		assertEquals(modelView.getNumberOfRowsInPagination(), 25);
		modelView.clickBack();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_182_ModelView_EditSessionAndVerifyItgotEdited(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.clickEditSession().editUserPoolDropDown();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_183_ModelView_EditSessionAndDiscardItAndVerifySessionGotRemoved(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.clickEditSession().editTitleStatusAndSave(randomTitle);
		assertFalse(modelView.isSessionAdded(randomTitle));
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_199_ModelView_EditAnyRowValueInFieldMapping(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.editModelViewTemplate(Constants.modelViewTemplate).clickFieldMapping().editRow(currencyTitle);
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_198_ModelView_clickBrowsebuttonTopopupSearchAPP(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.editModelViewTemplate(Constants.modelViewTemplate).clickBrowse();
		String selectApp = modelView.selectApp("Main Comp Calibration Page");
		String isAppSelected = modelView.isAppGotAdded();
		System.out.println("selectApp" + selectApp);
		System.out.println("isAppSelected" + isAppSelected);
		assertTrue(isAppSelected.contains(selectApp));
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_189_191ModelView_AddAndSaveNewUnitAndCurrencyAndDelete(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.openSettingsPage().openSettingsPageTabs("Manage Currency");
		modelView.isManageCurrencyOpened();
		modelView.clickManageUnits().addNewUnit(randomTitle + " IND", randomTitle + " UNIT")
				.deleteAddedUnit(randomTitle + " IND");

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_194_196ModelView_UserPoolsDetails(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.openSettingsPage().openSettingsPageTabs("Manage Pools").selectProductToFilterUserPools("HRSOFTCLOUD")
				.selectProductToFilterUserPools("HRSOFTCLOUD").clickMemberIcon().getPoolName();
		modelView.poolMembers();
		modelView.getPoolGuid();

	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_192_193ModelView_CreateNewCurrencySetAndDeleteIt(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.openSettingsPage().openSettingsPageTabs("Manage Currency");
		modelView.isManageCurrencyOpened();
		modelView.addNewRate();
		modelView.createNewCurrencySet(randomTitle + " Currency").deleteAddedCurrencySet(randomTitle + " Currency");
	}

	@Author(name = QAResources.TANVI)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.ModelView }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_197_ModelView_clickAddPoolButtonForAddingNewPool(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.openSettingsPage().openSettingsPageTabs("Manage Pools").clickAddPool();
	}

	private void createNewSessionInModelView(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		ModelViewPage modelView = hrSoftPage.clickModelView();
		modelView.isNavigatedToModelView();
		modelView.clickNewSessionTemplate(Constants.modelViewTemplate).enterSessionTitle(randomTitle).saveSession();
		assertTrue(modelView.isSessionAdded(randomTitle));
	}
}
