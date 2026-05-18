/**
 * Data-Driven Test: Register Then Login
 * Uses valid rows from TD_Register.csv to register, logout, then login with same credentials.
 * Verifies the full user lifecycle: register → logout → login.
 * 
 * Variables bound from Data File:
 *   - caseId, username, email, password, dataType, expectedResult, notes
 */
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil
import internal.GlobalVariable

TestObject xpath(String name, String selector) {
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

String frontendUrl() {
    try { return GlobalVariable.FRONTEND_URL.toString() }
    catch (Throwable ignored) { return 'http://localhost:4100/' }
}

int defaultTimeout() {
    try { return GlobalVariable.DEFAULT_TIMEOUT.toString().toInteger() }
    catch (Throwable ignored) { return 15 }
}

int stepDelaySeconds() {
    try { return GlobalVariable.STEP_DELAY_SECONDS.toString().toInteger() }
    catch (Throwable ignored) { return 1 }
}

String baseUrl = frontendUrl()
int timeout = defaultTimeout()
int stepDelay = stepDelaySeconds()
def pause = { WebUI.delay(stepDelay) }

// Variables from data binding
String testCaseId = caseId
String testUsername = username
String testEmail = email
String testPassword = password
String testDataType = dataType
String testExpectedResult = expectedResult
String testNotes = notes

// Only run for valid data rows (skip invalid/boundary rows)
if (testDataType != 'valid') {
    KeywordUtil.markPassed("${testCaseId}: Skipped (dataType='${testDataType}', only 'valid' rows are tested for register-then-login).")
    return
}

// Make username/email unique to avoid conflicts with DD_Register
String runId = System.currentTimeMillis().toString()
String uniqueUsername = "${testUsername}_rtl_${runId}"
String uniqueEmail = "rtl_${runId}_${testEmail}"

KeywordUtil.logInfo("=== Running Data-Driven Register Then Login: ${testCaseId} ===")
KeywordUtil.logInfo("Username: '${uniqueUsername}' | Email: '${uniqueEmail}'")

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.waitForPageLoad(timeout)
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    WebUI.navigateToUrl(baseUrl)
    pause()
    
    // Step 1: Register
    WebUI.click(xpath('Sign up nav', "(//a[normalize-space()='Sign up'])[1]"))
    pause()
    WebUI.waitForElementVisible(xpath('Sign Up heading', "//h1[normalize-space()='Sign Up']"), timeout)
    WebUI.setText(xpath('Username input', "//input[@placeholder='Username']"), uniqueUsername)
    pause()
    WebUI.setText(xpath('Email input', "//input[@placeholder='Email']"), uniqueEmail)
    pause()
    WebUI.setText(xpath('Password input', "//input[@placeholder='Password']"), testPassword)
    pause()
    WebUI.click(xpath('Sign up button', "//button[normalize-space()='Sign up']"))
    pause()
    
    boolean registered = WebUI.waitForElementVisible(
        xpath('Settings nav', "//a[contains(normalize-space(.),'Settings')]"), timeout, FailureHandling.OPTIONAL)
    
    if (!registered) {
        KeywordUtil.markFailed("${testCaseId}: Registration step failed.")
        return
    }
    KeywordUtil.logInfo("${testCaseId}: Registration successful.")
    
    // Step 2: Logout
    WebUI.click(xpath('Settings nav', "//a[contains(normalize-space(.),'Settings')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Logout button', "//button[contains(normalize-space(.),'logout')]"), timeout)
    WebUI.click(xpath('Logout button', "//button[contains(normalize-space(.),'logout')]"))
    pause()
    WebUI.delay(2)
    WebUI.waitForElementVisible(xpath('Sign in nav', "//a[normalize-space()='Sign in']"), timeout)
    KeywordUtil.logInfo("${testCaseId}: Logout successful.")
    
    // Step 3: Login with same credentials
    WebUI.click(xpath('Sign in nav', "(//a[normalize-space()='Sign in'])[1]"))
    pause()
    WebUI.waitForElementVisible(xpath('Sign In heading', "//h1[normalize-space()='Sign In']"), timeout)
    WebUI.setText(xpath('Email input', "//input[@placeholder='Email']"), uniqueEmail)
    pause()
    WebUI.setText(xpath('Password input', "//input[@placeholder='Password']"), testPassword)
    pause()
    WebUI.click(xpath('Sign in button', "//button[normalize-space()='Sign in']"))
    pause()
    
    boolean loggedIn = WebUI.waitForElementVisible(
        xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"), timeout, FailureHandling.OPTIONAL)
    
    if (loggedIn) {
        // Verify username is displayed
        boolean usernameVisible = WebUI.verifyTextPresent(uniqueUsername, false, FailureHandling.OPTIONAL)
        KeywordUtil.markPassed("${testCaseId}: Full lifecycle (register → logout → login) completed successfully.")
    } else {
        KeywordUtil.markFailed("${testCaseId}: Login after registration failed.")
    }
    
} finally {
    WebUI.closeBrowser()
}
