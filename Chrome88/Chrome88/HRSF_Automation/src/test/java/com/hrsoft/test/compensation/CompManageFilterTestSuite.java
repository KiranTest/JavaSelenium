package com.hrsoft.test.compensation;

import static org.testng.Assert.assertEquals;
import com.hrsoft.test.setuphelpers.RetryAnalyzer;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import com.hrsoft.annotations.Author;
import com.hrsoft.constants.ComponentGroups;
import com.hrsoft.constants.Constants;
import com.hrsoft.constants.QAResources;
import com.hrsoft.constants.TestGroups;
import com.hrsoft.dataprovider.DataProviders;
import com.hrsoft.gui.HRSoftPage;
import com.hrsoft.gui.LoginPage;
import com.hrsoft.gui.compensation.CompOverviewPage;
import com.hrsoft.test.setuphelpers.WebBaseTest;
import com.hrsoft.utils.zerocell.TestDataExcel;

public class CompManageFilterTestSuite extends WebBaseTest {
	private String randomString = RandomStringUtils.randomAlphabetic(3);
	private String filterForCopy = "Copy Test Filter" + randomString;

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")

	public void SM_88_90_CompensationFilters_copyAndDeleteFilter(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		CompOverviewPage overviewPage = hrSoftPage.clickManageFilter();
		overviewPage.saveCopiedItem(filterForCopy).deleteCopiedItem(filterForCopy);
		hrSoftPage.logOut();
	}

	@Author(name = QAResources.MASHOOD)
	@Test(groups = { TestGroups.SMOKE,
			ComponentGroups.COMPENSATION }, dataProviderClass = DataProviders.class, dataProvider = "SmokeTest")
	public void SM_91_94_CompensationFilters_CreateNewFilterAndDelete(TestDataExcel data) {
		LoginPage login = new LoginPage().loadUrl(data.getApplicationURL());
		HRSoftPage hrSoftPage = login.logIn(data.getUserName(), data.getPassword());
		CompOverviewPage overviewPage = hrSoftPage.clickManageFilter();
		String newFilter = randomString + "Test New Filter";
		overviewPage.addNewFilter(newFilter)
				.clickShareButton(newFilter);
		String expectedType = "Public";
		String actualType = overviewPage.AssertType(newFilter);
		assertEquals(expectedType, actualType);
		overviewPage.clickAddedLink(newFilter);
		overviewPage.deleteAddedFilter(newFilter);
		hrSoftPage.logOut();
	}
}
