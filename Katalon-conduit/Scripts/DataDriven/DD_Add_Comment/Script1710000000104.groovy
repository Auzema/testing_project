/**
 * Data-Driven Test: Add Comment with AI-Generated Test Data
 * Reads each row from TD_Comment.csv and adds comment via UI.
 * Registers a fresh user and creates an article for each row.
 * 
 * Variables bound from Data File:
 *   - caseId, commentBody, dataType, expectedResult, notes
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
String testCommentBody = commentBody
String testDataType = dataType
String testExpectedResult = expectedResult
String testNotes = notes

// Generate unique user and article for this test
String runId = System.currentTimeMillis().toString()
String uniqueUsername = "dd_com_${runId}"
String uniqueEmail = "${uniqueUsername}@example.com"
String articleTitle = "Comment Test Article ${runId}"

KeywordUtil.logInfo("=== Running Data-Driven Add Comment: ${testCaseId} ===")
KeywordUtil.logInfo("Comment: '${testCommentBody}' | Type: ${testDataType} | Expected: ${testExpectedResult}")

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
    WebUI.waitForElementVisible(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"), timeout)
    
    // Create an article to comment on
    WebUI.click(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Article title input', "//input[@placeholder='Article Title']"), timeout)
    WebUI.setText(xpath('Article title input', "//input[@placeholder='Article Title']"), articleTitle)
    pause()
    WebUI.setText(xpath('Article description input', "//input[@placeholder=\"What's this article about?\"]"), "Test article for comment")
    pause()
    WebUI.setText(xpath('Article body textarea', "//textarea[@placeholder='Write your article (in markdown)']"), "Article body for comment testing.")
    pause()
    WebUI.setText(xpath('Tag input', "//input[@placeholder='Enter tags']"), "commenttest")
    pause()
    WebUI.sendKeys(xpath('Tag input', "//input[@placeholder='Enter tags']"), Keys.chord(Keys.ENTER))
    pause()
    WebUI.click(xpath('Publish Article button', "//button[normalize-space()='Publish Article']"))
    pause()
    WebUI.waitForElementVisible(xpath('Article page', "//div[contains(@class,'article-page')]"), timeout)
    
    // Now add comment with test data
    WebUI.waitForElementVisible(xpath('Comment textarea', "//textarea[@placeholder='Write a comment...']"), timeout)
    
    if (testCommentBody != null && testCommentBody.length() > 0) {
        WebUI.setText(xpath('Comment textarea', "//textarea[@placeholder='Write a comment...']"), testCommentBody)
    }
    pause()
    
    // Click Post Comment
    WebUI.click(xpath('Post Comment button', "//button[normalize-space()='Post Comment']"))
    pause()
    WebUI.delay(3) // Wait for server response
    
    // Check result
    if (testExpectedResult == 'success') {
        boolean commentPosted = WebUI.waitForElementVisible(
            xpath('Comment card', "//div[contains(@class,'card')]//p[contains(text(),'${testCommentBody.take(20)}')]"),
            timeout, FailureHandling.OPTIONAL)
        
        if (!commentPosted) {
            // Alternative check: any comment card appeared
            commentPosted = WebUI.waitForElementVisible(
                xpath('Any comment card', "//div[contains(@class,'card') and not(contains(@class,'comment-form'))]//p"),
                5, FailureHandling.OPTIONAL)
        }
        
        if (commentPosted) {
            KeywordUtil.markPassed("${testCaseId}: Comment posted successfully as expected.")
        } else {
            KeywordUtil.markFailed("${testCaseId}: Expected comment success but comment not visible.")
        }
    } else if (testExpectedResult == 'error') {
        // For empty comment, check if comment was NOT posted
        WebUI.delay(2)
        boolean commentPosted = WebUI.waitForElementVisible(
            xpath('Any new comment', "//div[contains(@class,'card') and not(contains(@class,'comment-form'))]//p"),
            5, FailureHandling.OPTIONAL)
        
        if (!commentPosted) {
            KeywordUtil.markPassed("${testCaseId}: Empty/invalid comment rejected as expected.")
        } else {
            // Comment was posted even though it shouldn't have been
            KeywordUtil.markFailed("${testCaseId}: Expected error but comment was posted! Missing validation. Notes: ${testNotes}")
        }
    } else if (testExpectedResult == 'verify') {
        WebUI.delay(2)
        boolean commentPosted = WebUI.waitForElementVisible(
            xpath('Any new comment', "//div[contains(@class,'card') and not(contains(@class,'comment-form'))]//p"),
            8, FailureHandling.OPTIONAL)
        
        if (commentPosted) {
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: Comment was posted.")
        } else {
            KeywordUtil.logInfo("${testCaseId} [VERIFY]: Comment was not posted.")
        }
        KeywordUtil.markPassed("${testCaseId}: Behavior observed and logged. Notes: ${testNotes}")
    }
    
} finally {
    WebUI.closeBrowser()
}
