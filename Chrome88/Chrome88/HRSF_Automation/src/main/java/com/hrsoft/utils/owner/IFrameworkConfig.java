package com.hrsoft.utils.owner;

import java.net.URL;
import org.aeonbits.owner.Config;
import com.hrsoft.enums.BrowserType;
import com.hrsoft.enums.RunModeType;
import com.hrsoft.enums.WebRemoteModeType;
import com.hrsoft.utils.owner.converters.StringToBrowserTypeConverter;
import com.hrsoft.utils.owner.converters.StringToIntCoverter;
import com.hrsoft.utils.owner.converters.StringToRunModeBrowserTypeConverter;
import com.hrsoft.utils.owner.converters.StringToURLConverter;

@Config.Sources(value = "file:${user.dir}/src/test/resources/properties/FrameworkConfig.properties")

public interface IFrameworkConfig extends Config {

	@ConverterClass(StringToBrowserTypeConverter.class)
	@Key(value = "browser")
	BrowserType browser();

	@Key("webAppicationURL")
	String webAppicationURL();

	@Key(value = "waitduration")
	long waitDuration();

	@Key(value = "runModeType")
	RunModeType runModeType();

	WebRemoteModeType browserRemoteMode();

	@ConverterClass(StringToRunModeBrowserTypeConverter.class)
	RunModeType runModeBrowser();

	@Key(value = "seleniumGridURL")
	@ConverterClass(StringToURLConverter.class)
	URL seleniumGridURL();

	@DefaultValue("HRSofti")
	@Key(value = "client")
	String client();

	@Key(value = "username")
	String userName();

	@Key(value = "password")
	String password();

	@Key(value = "adminUser")
	String proxyUser();

	@Key(value = "compPlanName")
	String compPlanName();

	@Key(value = "compPlanId")
	@ConverterClass(StringToIntCoverter.class)
	int compPlanId();

	@Key(value = "custId")
	String custId();

	@Key(value = "custGuid")
	String custGuid();

	@Key(value = "topPlannerName")
	String topPlannerName();

	@Key(value = "clientName")
	String clientName();

	@Key(value = "dataViewSearchText")
	String dataViewSearchText();

	@Key(value = "employeeWithSalaryComponent")
	String employeeWithSalaryComponent();

	String compPlanNameMngr();

	@Key(value = "groupNameForCasecadeBudget")
	String groupNameForCasecadeBudget();

	@Key(value = "jobRecommendation")
	String jobRecommendation();

	@Key(value = "client_name")
	String client_name();

	@Key(value = "empSpareName")
	String empSpareName();

	/**
	 * @return
	 */
	@Key(value = "compPlanId2")
	@ConverterClass(StringToIntCoverter.class)
	int compPlanId2();

	@Key(value = "csvDataObjectsPath")
	String csvDataObjectsPath();

	@Key(value = "csvBaseDobjectsDefPath")
	String csvBaseDobjectsDefPath();

	@Key(value = "API_URL")
	String API_URL();

	@Key(value = "API_KEY")
	String API_KEY();

	@Key(value = "userPrompt1")
	String userPrompt1();

	@Key(value = "userPrompt2")
	String userPrompt2();

	@Key(value = "userPrompt3")
	String userPrompt3();

	@Key(value = "userPrompt4")
	String userPrompt4();

	@Key(value = "userPrompt5")
	String userPrompt5();

	@Key(value = "productName")
	String productName();

	@Key(value = "dataSetName")
	String dataSetName();
}
