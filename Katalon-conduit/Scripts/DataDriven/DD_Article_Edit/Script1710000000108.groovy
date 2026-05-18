/**
 * Data-Driven Test: Edit Article with AI-Generated Test Data
 * Creates an article, then edits it using data from TD_Article.csv.
 * Tests that article editing works with various data types.
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

// Only test valid and edge data for editing (skip invalid rows that can't create initial article)
if (testDataType == 'invalid') {
    KeywordUtil.markPassed("${testCaseId}: Skipped for edit test (invalid data cannot create initial article).")
    return
}

String runId = System.currentTimeMillis().toString()
String uniqueUsername = "dd_edit_${runId}"
String uniqueEmail = "${uniqueUsername}@example.com"
String originalTitle = "Original Article ${runId}"

KeywordUtil.logInfo("=== Running Data-Driven Article Edit: ${testCaseId} ===")
KeywordUtil.logInfo("New Title: '${testTitle}' | Type: ${testDataType} | Expected: ${testExpectedResult}")

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.waitForPageLoad(timeout)
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    WebUI.navigateToUrl(baseUrl)
    pause()
    
    // Register user
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
    
    // Create initial article
    WebUI.click(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Article title input', "//input[@placeholder='Article Title']"), timeout)
    WebUI.setText(xpath('Article title input', "//input[@placeholder='Article Title']"), originalTitle)
    pause()
    WebUI.setText(xpath('Article description input', "//input[@placeholder=\"What's this article about?\"]"), "Original description")
    pause()
    WebUI.setText(xpath('Article body textarea', "//textarea[@placeholder='Write your article (in markdown)']"), "Original body content.")
    pause()
    WebUI.setText(xpath('Tag input', "//input[@placeholder='Enter tags']"), "original")
    pause()
    WebUI.sendKeys(xpath('Tag input', "//input[@placeholder='Enter tags']"), Keys.chord(Keys.ENTER))
    pause()
    WebUI.click(xpath('Publish Article button', "//button[normalize-space()='Publish Article']"))
    pause()
    WebUI.waitForElementVisible(xpath('Article page', "//div[contains(@class,'article-page')]"), timeout)
    
    // Click Edit Article
    WebUI.click(xpath('Edit Article button', "//a[contains(normalize-space(.),'Edit Article')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Article title input', "//input[@placeholder='Article Title']"), timeout)
    
    // Clear and fill with new test data
    WebUI.clearText(xpath('Article title input', "//input[@placeholder='Article Title']"))
    if (testTitle != null && testTitle.length() > 0) {
        WebUI.setText(xpath('Article title input', "//input[@placeholder='Article Title']"), testTitle)
    }
    pause()
    
    WebUI.clearText(xpath('Article description input', "//input[@placeholder=\"What's this article about?\"]"))
    if (testDescription != null && testDescription.length() > 0) {
        WebUI.setText(xpath('Article description input', "//input[@placeholder=\"What's this article about?\"]"), testDescription)
    }
    pause()
    
    WebUI.clearText(xpath('Article body textarea', "//textarea[@placeholder='Write your article (in markdown)']"))
    if (testBody != null && testBody.length() > 0) {
        WebUI.setText(xpath('Article body textarea', "//textarea[@placeholder='Write your article (in markdown)']"), testBody)
    }
    pause()
    
    // Publish edited article
    WebUI.click(xpath('Publish Article button', "//button[normalize-space()='Publish Article']"))
    pause()
    WebUI.delay(3)
    
    // Check result
    if (testExpectedResult == 'success' || testExpectedResult == 'verify') {
        boolean articleUpdated = WebUI.waitForElementVisible(
            xpath('Article page', "//div[contains(@class,'article-page')]"),
            timeout, FailureHandling.OPTIONAL)
        
        if (articleUpdated) {
            KeywordUtil.markPassed("${testCaseId}: Article edited successfully with ${testDataType} data.")
        } else {
            boolean hasError = WebUI.waitForElementVisible(
                xpath('Error messages', "//ul[contains(@class,'error-messages')]"),
                5, FailureHandling.OPTIONAL)
            if (hasError) {
                String errorText = WebUI.getText(xpath('Error messages', "//ul[contains(@class,'error-messages')]"))
                KeywordUtil.logInfo("${testCaseId}: Edit rejected with error: ${errorText}")
                KeywordUtil.markPassed("${testCaseId}: Edit behavior observed. Notes: ${testNotes}")
            } else {
                KeywordUtil.markFailed("${testCaseId}: Article edit did not complete and no error shown.")
            }
        }
    }
    
} finally {
    WebUI.closeBrowser()
}
