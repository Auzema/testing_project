/**
 * Data-Driven Test: Update Profile with AI-Generated Test Data
 * Reads each row from TD_Profile.csv and updates profile settings via UI.
 * Registers a fresh user for each row.
 * 
 * Variables bound from Data File:
 *   - caseId, username, bio, imageUrl, email, dataType, expectedResult, notes
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
String testUsername = username
String testBio = bio
String testImageUrl = imageUrl
String testEmail = email
String testDataType = dataType
String testExpectedResult = expectedResult
String testNotes = notes

// Generate unique user for registration
String runId = System.currentTimeMillis().toString()
String regUsername = "dd_prof_${runId}"
String regEmail = "${regUsername}@example.com"

KeywordUtil.logInfo("=== Running Data-Driven Update Profile: ${testCaseId} ===")
KeywordUtil.logInfo("Username: '${testUsername}' | Bio: '${testBio?.take(30)}...' | Type: ${testDataType} | Expected: ${testExpectedResult}")

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
    WebUI.setText(xpath('Username input', "//input[@placeholder='Username']"), regUsername)
    pause()
    WebUI.setText(xpath('Email input', "//input[@placeholder='Email']"), regEmail)
    pause()
    WebUI.setText(xpath('Password input', "//input[@placeholder='Password']"), password)
    pause()
    WebUI.click(xpath('Sign up button', "//button[normalize-space()='Sign up']"))
    pause()
    WebUI.waitForElementVisible(xpath('Settings nav', "//a[contains(normalize-space(.),'Settings')]"), timeout)
    
    // Navigate to Settings
    WebUI.click(xpath('Settings nav', "//a[contains(normalize-space(.),'Settings')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Settings heading', "//h1[normalize-space()='Your Settings']"), timeout)
    
    // Clear and fill settings form with test data
    // Image URL
    if (testImageUrl != null) {
        WebUI.clearText(xpath('Image URL input', "//input[@placeholder='URL of profile picture']"))
        if (testImageUrl.length() > 0) {
            WebUI.setText(xpath('Image URL input', "//input[@placeholder='URL of profile picture']"), testImageUrl)
        }
    }
    pause()
    
    // Username
    if (testUsername != null) {
        WebUI.clearText(xpath('Username input settings', "//input[@placeholder='Username']"))
        if (testUsername.length() > 0) {
            WebUI.setText(xpath('Username input settings', "//input[@placeholder='Username']"), testUsername)
        }
    }
    pause()
    
    // Bio
    if (testBio != null) {
        WebUI.clearText(xpath('Bio textarea', "//textarea[@placeholder='Short bio about you']"))
        if (testBio.length() > 0) {
            WebUI.setText(xpath('Bio textarea', "//textarea[@placeholder='Short bio about you']"), testBio)
        }
    }
    pause()
    
    // Email
    if (testEmail != null) {
        WebUI.clearText(xpath('Email input settings', "//input[@placeholder='Email']"))
        if (testEmail.length() > 0) {
            WebUI.setText(xpath('Email input settings', "//input[@placeholder='Email']"), testEmail)
        }
    }
    pause()
    
    // Click Update Settings
    WebUI.click(xpath('Update Settings button', "//button[normalize-space()='Update Settings']"))
    pause()
    WebUI.delay(3) // Wait for server response
    
    // Check result
    if (testExpectedResult == 'success') {
        // After successful update, page should reload or show success state
        // Check that we're still on settings or redirected without error
        boolean hasError = WebUI.waitForElementVisible(
            xpath('Error messages', "//ul[contains(@class,'error-messages')]"), 
            5, FailureHandling.OPTIONAL)
        
        if (!hasError) {
            KeywordUtil.markPassed("${testCaseId}: Profile updated successfully as expected.")
        } else {
            String errorText = WebUI.getText(xpath('Error messages', "//ul[contains(@class,'error-messages')]"))
            KeywordUtil.markFailed("${testCaseId}: Expected success but got error: ${errorText}")
        }
    } else if (testExpectedResult == 'error') {
        boolean hasError = WebUI.waitForElementVisible(
            xpath('Error messages', "//ul[contains(@class,'error-messages')]"), 
            timeout, FailureHandling.OPTIONAL)
        
        if (hasError) {
            String errorText = WebUI.getText(xpath('Error messages', "//ul[contains(@class,'error-messages')]"))
            KeywordUtil.markPassed("${testCaseId}: Error shown as expected: ${errorText}")
        } else {
            KeywordUtil.markFailed("${testCaseId}: Expected error but no error shown. Missing validation. Notes: ${testNotes}")
        }
    } else if (testExpectedResult == 'verify') {
        boolean hasError = WebUI.waitForElementVisible(
            xpath('Error messages', "//ul[contains(@class,'error-messages')]"), 
            8, FailureHandling.OPTIONAL)
        
        if (hasError) {
            String errorText = WebUI.getText(xpath('Error messages', "//ul[contains(@class,'error-messages')]"))
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: Error shown - ${errorText}")
        } else {
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: Settings updated without error.")
        }
        KeywordUtil.markPassed("${testCaseId}: Behavior observed and logged. Notes: ${testNotes}")
    }
    
} finally {
    WebUI.closeBrowser()
}
