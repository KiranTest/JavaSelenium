package com.hrsoft.gui;

import static com.hrsoft.reports.ExtentLogger.*;
import static org.testng.Assert.assertTrue;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static com.hrsoft.config.ConfigFactory.*;
import com.hrsoft.driver.DriverManager;
import com.hrsoft.utils.seleniumfy.BasePage;

public class LoginPage extends BasePage {

	private String username = "#login-name";
	private String password = "#login-pass";
	private String loginBtn = "#submit-login-form";
	private String settingsBtn = "//i[@class='fal fa-cog fa-2x   font-icon  text-none']";

	@SuppressWarnings("unchecked")
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOfElementLocated(locateBy(loginBtn));
	}

	public LoginPage navigatedToLoginPage() {
		assertTrue(waitUntilVisible("//h1[text()='Login to TalentCenter']"));
		assertTrue(hasPageLoaded());
		return this;
	}

	public LoginPage enterUsername() {
		assertTrue(setText(username, getConfig().userName()));
		return this;
	}

	public LoginPage enterUsername(String user) {
		assertTrue(setText(username, user));
		return this;
	}

	public LoginPage enterPassword() {
		assertTrue(setText(password, getConfig().password()));
		return this;
	}

	public LoginPage enterPassword(String pass) {
		assertTrue(setText(password, pass));
		return this;
	}

	public HRSoftPage clickLogin() {
		assertTrue(click(loginBtn));
		return new HRSoftPage();
	}

	public LoginPage clickLoginAndReturnLoginPage() {
		assertTrue(click(loginBtn));
		return this;
	}

	public HRSoftPage logIn() {
		enterUsername(getConfig().userName());
		enterPassword(getConfig().password());
		clickLogin();
		assertTrue(hasPageLoaded());
		info("Logged in as: " + getConfig().userName());
		return (HRSoftPage) navigateToPage(HRSoftPage.class);
	}

	public String getLoginError() {
		return getText("div.app-title");
	}

	public void returnToLogin() {
		assertTrue(click("a#return-login"));
	}

	public HRSoftPage logInAndRefresh() {
		enterUsername(getConfig().userName());
		enterPassword(getConfig().password());
		clickLogin();
		refresh();
		info("Logged in as: " + getConfig().userName());
		return (HRSoftPage) navigateToPage(HRSoftPage.class);
	}

	public HRSoftPage logIn(String user, String pass) {
		enterUsername(user);
		enterPassword(pass);
		clickLogin();
		assertTrue(hasPageLoaded());
		info("Logged in as: " + user);
		return (HRSoftPage) navigateToPage(HRSoftPage.class);
	}

	public HRSoftPage logInAndRefresh(String user, String pass) {
		enterUsername(user);
		enterPassword(pass);
		clickLogin();
		assertTrue(hasPageLoaded());
		refresh();
		info("Logged in as: " + user);
		return (HRSoftPage) navigateToPage(HRSoftPage.class);
	}

	public String getAttribute(String text) {
		String attrValue = getAttribute(username, text);
		return attrValue;
	}

	public String getLoginCss(String text) {
		String css = getCssValue(loginBtn, text);
		return css;
	}

	public LoginPage clickReturnToLogin() {
		click("#return-login");
		return this;
	}

	public LoginPage loadUrl(String applicationURL) {
		DriverManager.getDriver().get(applicationURL);
//		DriverManager.getDriver().get(getConfig().webAppicationURL()
//				+ "content/portal/tccserver/loadApp.htm?wizinstanceid=C1ADF74F-08C7-4E40-8321-ED6CC20280CB&paramhash=F90E70DE-34B9-4803-9220-0A455585E645");
		refresh();
		return this;
	}

}
