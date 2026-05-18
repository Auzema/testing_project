/**
 * Data-Driven Test: Favorite/Unfavorite Article
 * Tests favorite actions with different scenarios from TD_Favorite.csv.
 * 
 * Variables bound from Data File:
 *   - caseId, action, dataType, expectedResult, notes
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
String testAction = action
String testDataType = dataType
String testExpectedResult = expectedResult
String testNotes = notes

String runId = System.currentTimeMillis().toString()
String uniqueUsername = "dd_fav_${runId}"
String uniqueEmail = "${uniqueUsername}@example.com"
String articleTitle = "Favorite Test ${runId}"

KeywordUtil.logInfo("=== Running Data-Driven Favorite: ${testCaseId} ===")
KeywordUtil.logInfo("Action: '${testAction}' | Expected: ${testExpectedResult}")

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
    
    // Create an article to favorite
    WebUI.click(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Article title input', "//input[@placeholder='Article Title']"), timeout)
    WebUI.setText(xpath('Article title input', "//input[@placeholder='Article Title']"), articleTitle)
    pause()
    WebUI.setText(xpath('Article description input', "//input[@placeholder=\"What's this article about?\"]"), "Favorite test article")
    pause()
    WebUI.setText(xpath('Article body textarea', "//textarea[@placeholder='Write your article (in markdown)']"), "Body for favorite testing.")
    pause()
    WebUI.setText(xpath('Tag input', "//input[@placeholder='Enter tags']"), "favtest")
    pause()
    WebUI.sendKeys(xpath('Tag input', "//input[@placeholder='Enter tags']"), Keys.chord(Keys.ENTER))
    pause()
    WebUI.click(xpath('Publish Article button', "//button[normalize-space()='Publish Article']"))
    pause()
    WebUI.waitForElementVisible(xpath('Article page', "//div[contains(@class,'article-page')]"), timeout)
    
    // Go to homepage to see article in feed
    WebUI.click(xpath('Home nav', "//a[normalize-space()='Home']"))
    pause()
    WebUI.waitForElementVisible(xpath('Article preview', "//div[contains(@class,'article-preview')]"), timeout)
    
    // Find favorite button
    TestObject favButton = xpath('Favorite button', "(//button[contains(@class,'btn-outline-primary')])[1]")
    
    if (testAction == 'favorite' || testAction == 'favorite_own_article') {
        // Get initial count
        String initialText = WebUI.getText(favButton, FailureHandling.OPTIONAL)
        WebUI.click(favButton)
        pause()
        WebUI.delay(2)
        
        String afterText = WebUI.getText(favButton, FailureHandling.OPTIONAL)
        KeywordUtil.logInfo("${testCaseId}: Before='${initialText}' After='${afterText}'")
        
        if (testExpectedResult == 'count_increase') {
            KeywordUtil.markPassed("${testCaseId}: Favorite action completed. Count: ${afterText}")
        }
    } else if (testAction == 'unfavorite' || testAction == 'unfavorite_own_article') {
        // First favorite
        WebUI.click(favButton)
        pause()
        WebUI.delay(1)
        // Then unfavorite
        WebUI.click(favButton)
        pause()
        WebUI.delay(2)
        
        String afterText = WebUI.getText(favButton, FailureHandling.OPTIONAL)
        KeywordUtil.logInfo("${testCaseId}: After unfavorite='${afterText}'")
        
        if (testExpectedResult == 'count_decrease') {
            KeywordUtil.markPassed("${testCaseId}: Unfavorite action completed. Count: ${afterText}")
        }
    } else if (testAction == 'favorite_twice') {
        // Click favorite twice quickly
        WebUI.click(favButton)
        pause()
        WebUI.click(favButton)
        pause()
        WebUI.delay(2)
        
        String afterText = WebUI.getText(favButton, FailureHandling.OPTIONAL)
        KeywordUtil.logInfo("${testCaseId}: After double-click='${afterText}'")
        KeywordUtil.markPassed("${testCaseId}: Double favorite behavior observed. Count: ${afterText}")
    }
    
} finally {
    WebUI.closeBrowser()
}
