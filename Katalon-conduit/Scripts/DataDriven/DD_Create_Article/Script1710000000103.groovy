/**
 * Data-Driven Test: Create Article with AI-Generated Test Data
 * Reads each row from TD_Article.csv and creates article via UI.
 * Registers a fresh user for each row to ensure clean state.
 * 
 * Variables bound from Data File:
 *   - caseId, title, description, body, tags, dataType, expectedResult, notes
 */
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil
import internal.GlobalVariable
import org.openqa.selenium.Keys

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
String testTitle = title
String testDescription = description
String testBody = body
String testTags = tags
String testDataType = dataType
String testExpectedResult = expectedResult
String testNotes = notes

// Generate unique user for this test iteration
String runId = System.currentTimeMillis().toString()
String uniqueUsername = "dd_art_${runId}"
String uniqueEmail = "${uniqueUsername}@example.com"

KeywordUtil.logInfo("=== Running Data-Driven Create Article: ${testCaseId} ===")
KeywordUtil.logInfo("Title: '${testTitle}' | Type: ${testDataType} | Expected: ${testExpectedResult}")

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.waitForPageLoad(timeout)
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    WebUI.navigateToUrl(baseUrl)
    pause()
    
    // Register a fresh user first
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
    WebUI.waitForElementVisible(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"), timeout)
    
    // Navigate to New Post
    WebUI.click(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Article title input', "//input[@placeholder='Article Title']"), timeout)
    
    // Fill article form with test data
    if (testTitle != null && testTitle.length() > 0) {
        WebUI.setText(xpath('Article title input', "//input[@placeholder='Article Title']"), testTitle)
    }
    pause()
    
    if (testDescription != null && testDescription.length() > 0) {
        WebUI.setText(xpath('Article description input', "//input[@placeholder=\"What's this article about?\"]"), testDescription)
    }
    pause()
    
    if (testBody != null && testBody.length() > 0) {
        WebUI.setText(xpath('Article body textarea', "//textarea[@placeholder='Write your article (in markdown)']"), testBody)
    }
    pause()
    
    if (testTags != null && testTags.length() > 0) {
        WebUI.setText(xpath('Tag input', "//input[@placeholder='Enter tags']"), testTags)
        pause()
        WebUI.sendKeys(xpath('Tag input', "//input[@placeholder='Enter tags']"), Keys.chord(Keys.ENTER))
    }
    pause()
    
    // Click Publish Article
    WebUI.click(xpath('Publish Article button', "//button[normalize-space()='Publish Article']"))
    pause()
    WebUI.delay(3) // Wait for server response
    
    // Check result
    if (testExpectedResult == 'success') {
        boolean articleCreated = WebUI.waitForElementVisible(
            xpath('Article page', "//div[contains(@class,'article-page')]"), 
            timeout, FailureHandling.OPTIONAL)
        
        if (articleCreated) {
            KeywordUtil.markPassed("${testCaseId}: Article created successfully as expected.")
        } else {
            KeywordUtil.markFailed("${testCaseId}: Expected article creation success but failed.")
        }
    } else if (testExpectedResult == 'error') {
        boolean hasError = WebUI.waitForElementVisible(
            xpath('Error messages', "//ul[contains(@class,'error-messages')]"), 
            timeout, FailureHandling.OPTIONAL)
        
        if (hasError) {
            String errorText = WebUI.getText(xpath('Error messages', "//ul[contains(@class,'error-messages')]"))
            KeywordUtil.markPassed("${testCaseId}: Error shown as expected: ${errorText}")
        } else {
            boolean articleCreated = WebUI.waitForElementVisible(
                xpath('Article page', "//div[contains(@class,'article-page')]"), 
                5, FailureHandling.OPTIONAL)
            if (articleCreated) {
                KeywordUtil.markFailed("${testCaseId}: Expected error but article was created! Missing validation. Notes: ${testNotes}")
            } else {
                // Still on editor page - might be client-side validation
                KeywordUtil.markPassed("${testCaseId}: Article not created (stayed on editor). Notes: ${testNotes}")
            }
        }
    } else if (testExpectedResult == 'verify') {
        boolean articleCreated = WebUI.waitForElementVisible(
            xpath('Article page', "//div[contains(@class,'article-page')]"), 
            10, FailureHandling.OPTIONAL)
        boolean hasError = WebUI.waitForElementVisible(
            xpath('Error messages', "//ul[contains(@class,'error-messages')]"), 
            5, FailureHandling.OPTIONAL)
        
        if (articleCreated) {
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: Article created successfully.")
        } else if (hasError) {
            String errorText = WebUI.getText(xpath('Error messages', "//ul[contains(@class,'error-messages')]"))
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: Error shown - ${errorText}")
        } else {
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: Stayed on editor page.")
        }
        KeywordUtil.markPassed("${testCaseId}: Behavior observed and logged. Notes: ${testNotes}")
    }
    
} finally {
    WebUI.closeBrowser()
}
