/**
 * Data-Driven Test: Register with AI-Generated Test Data
 * Reads each row from TD_Register.csv and performs registration via UI.
 * Compares actual result with expectedResult column.
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

KeywordUtil.logInfo("=== Running Data-Driven Register: ${testCaseId} ===")
KeywordUtil.logInfo("Username: '${testUsername}' | Email: '${testEmail}' | Type: ${testDataType} | Expected: ${testExpectedResult}")

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.waitForPageLoad(timeout)
    
    // Clear any existing session
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    WebUI.navigateToUrl(baseUrl)
    pause()
    WebUI.waitForPageLoad(timeout)
    
    // Navigate to Sign Up page
    WebUI.click(xpath('Sign up nav', "(//a[normalize-space()='Sign up'])[1]"))
    pause()
    WebUI.waitForElementVisible(xpath('Sign Up heading', "//h1[normalize-space()='Sign Up']"), timeout)
    
    // Fill registration form
    if (testUsername != null && testUsername.length() > 0) {
        WebUI.setText(xpath('Username input', "//input[@placeholder='Username']"), testUsername)
    }
    pause()
    
    if (testEmail != null && testEmail.length() > 0) {
        WebUI.setText(xpath('Email input', "//input[@placeholder='Email']"), testEmail)
    }
    pause()
    
    if (testPassword != null && testPassword.length() > 0) {
        WebUI.setText(xpath('Password input', "//input[@placeholder='Password']"), testPassword)
    }
    pause()
    
    // Click Sign up button
    WebUI.click(xpath('Sign up button', "//button[normalize-space()='Sign up']"))
    pause()
    WebUI.delay(3) // Wait for server response
    
    // Check result based on expected outcome
    if (testExpectedResult == 'success') {
        // Expect successful registration - should see New Post nav
        boolean isLoggedIn = WebUI.waitForElementVisible(
            xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"), 
            timeout, FailureHandling.OPTIONAL)
        
        if (isLoggedIn) {
            KeywordUtil.markPassed("${testCaseId}: Registration successful as expected.")
        } else {
            KeywordUtil.markFailed("${testCaseId}: Expected success but registration failed.")
        }
    } else if (testExpectedResult == 'error') {
        // Expect error message
        boolean hasError = WebUI.waitForElementVisible(
            xpath('Error messages', "//ul[contains(@class,'error-messages')]"), 
            timeout, FailureHandling.OPTIONAL)
        
        if (hasError) {
            String errorText = WebUI.getText(xpath('Error messages', "//ul[contains(@class,'error-messages')]"))
            KeywordUtil.markPassed("${testCaseId}: Error shown as expected: ${errorText}")
        } else {
            // Check if it accidentally succeeded (no validation)
            boolean isLoggedIn = WebUI.waitForElementVisible(
                xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"), 
                5, FailureHandling.OPTIONAL)
            if (isLoggedIn) {
                KeywordUtil.markFailed("${testCaseId}: Expected error but registration succeeded! Missing validation. Notes: ${testNotes}")
            } else {
                KeywordUtil.markFailed("${testCaseId}: Expected error but no error message displayed.")
            }
        }
    } else if (testExpectedResult == 'verify') {
        // Observe and log behavior without strict pass/fail
        boolean hasError = WebUI.waitForElementVisible(
            xpath('Error messages', "//ul[contains(@class,'error-messages')]"), 
            8, FailureHandling.OPTIONAL)
        boolean isLoggedIn = WebUI.waitForElementVisible(
            xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"), 
            5, FailureHandling.OPTIONAL)
        
        if (hasError) {
            String errorText = WebUI.getText(xpath('Error messages', "//ul[contains(@class,'error-messages')]"))
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: Error shown - ${errorText}")
        } else if (isLoggedIn) {
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: Registration succeeded.")
        } else {
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: No clear success or error state detected.")
        }
        KeywordUtil.markPassed("${testCaseId}: Behavior observed and logged. Notes: ${testNotes}")
    }
    
} finally {
    WebUI.closeBrowser()
}
