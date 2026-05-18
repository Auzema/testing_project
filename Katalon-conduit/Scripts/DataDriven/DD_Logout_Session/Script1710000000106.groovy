/**
 * Data-Driven Test: Logout and Session Protection
 * Registers user, logs out, then verifies protected routes are blocked.
 * 
 * Variables bound from Data File:
 *   - caseId, protectedRoute, dataType, expectedResult, notes
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

String testPassword() {
    try { return GlobalVariable.TEST_PASSWORD.toString() }
    catch (Throwable ignored) { return 'Password123!' }
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
String password = testPassword()
int timeout = defaultTimeout()
int stepDelay = stepDelaySeconds()
def pause = { WebUI.delay(stepDelay) }

// Variables from data binding
String testCaseId = caseId
String testProtectedRoute = protectedRoute
String testDataType = dataType
String testExpectedResult = expectedResult
String testNotes = notes

String runId = System.currentTimeMillis().toString()
String uniqueUsername = "dd_logout_${runId}"
String uniqueEmail = "${uniqueUsername}@example.com"

KeywordUtil.logInfo("=== Running Data-Driven Logout Session: ${testCaseId} ===")
KeywordUtil.logInfo("Protected Route: '${testProtectedRoute}' | Expected: ${testExpectedResult}")

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.waitForPageLoad(timeout)
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    WebUI.navigateToUrl(baseUrl)
    pause()
    
    // Register a fresh user
    WebUI.click(xpath('Sign up nav', "(//a[normalize-space()='Sign up'])[1]"))
    pause()
    WebUI.waitForElementVisible(xpath('Sign Up heading', "//h1[normalize-space()='Sign Up']"), timeout)
    WebUI.setText(xpath('Username input', "//input[@placeholder='Username']"), uniqueUsername)
    pause()
    WebUI.setText(xpath('Email input', "//input[@placeholder='Email']"), uniqueEmail)
    pause()
    WebUI.setText(xpath('Password input', "//input[@placeholder='Password']"), password)
    pause()
    WebUI.click(xpath('Sign up button', "//button[normalize-space()='Sign up']"))
    pause()
    WebUI.waitForElementVisible(xpath('Settings nav', "//a[contains(normalize-space(.),'Settings')]"), timeout)
    
    // Logout via Settings page
    WebUI.click(xpath('Settings nav', "//a[contains(normalize-space(.),'Settings')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Logout button', "//button[contains(normalize-space(.),'logout')]"), timeout)
    WebUI.click(xpath('Logout button', "//button[contains(normalize-space(.),'logout')]"))
    pause()
    WebUI.delay(2)
    
    // Verify logged out state
    WebUI.waitForElementVisible(xpath('Sign in nav', "//a[normalize-space()='Sign in']"), timeout)
    
    // Now navigate to protected route
    String targetUrl = baseUrl.replaceAll('/+$', '') + testProtectedRoute
    WebUI.navigateToUrl(targetUrl)
    pause()
    WebUI.delay(3)
    
    if (testExpectedResult == 'blocked') {
        // Should NOT see authenticated content
        boolean hasNewPost = WebUI.waitForElementVisible(
            xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"),
            5, FailureHandling.OPTIONAL)
        boolean hasEditorForm = WebUI.waitForElementVisible(
            xpath('Editor form', "//input[@placeholder='Article Title']"),
            3, FailureHandling.OPTIONAL)
        boolean hasSettingsForm = WebUI.waitForElementVisible(
            xpath('Settings form', "//h1[normalize-space()='Your Settings']"),
            3, FailureHandling.OPTIONAL)
        
        if (!hasNewPost && !hasEditorForm && !hasSettingsForm) {
            KeywordUtil.markPassed("${testCaseId}: Protected route '${testProtectedRoute}' is blocked after logout.")
        } else {
            KeywordUtil.markFailed("${testCaseId}: Protected route '${testProtectedRoute}' is accessible after logout! Session not cleared.")
        }
    } else if (testExpectedResult == 'allowed') {
        // Public route should be accessible
        boolean hasHomepage = WebUI.waitForElementVisible(
            xpath('Global Feed', "//a[normalize-space()='Global Feed']"),
            timeout, FailureHandling.OPTIONAL)
        
        if (hasHomepage) {
            KeywordUtil.markPassed("${testCaseId}: Public route '${testProtectedRoute}' is accessible after logout.")
        } else {
            KeywordUtil.markFailed("${testCaseId}: Public route '${testProtectedRoute}' is not accessible.")
        }
    }
    
} finally {
    WebUI.closeBrowser()
}
