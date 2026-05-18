import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import org.openqa.selenium.Keys

TestObject xpath(String name, String selector) {
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

String frontendUrl() {
    try {
        return GlobalVariable.FRONTEND_URL.toString()
    } catch (Throwable ignored) {
        return 'http://localhost:4100/'
    }
}

String testPassword() {
    try {
        return GlobalVariable.TEST_PASSWORD.toString()
    } catch (Throwable ignored) {
        return 'Password123!'
    }
}

int defaultTimeout() {
    try {
        return GlobalVariable.DEFAULT_TIMEOUT.toString().toInteger()
    } catch (Throwable ignored) {
        return 15
    }
}

int stepDelaySeconds() {
    try {
        return GlobalVariable.STEP_DELAY_SECONDS.toString().toInteger()
    } catch (Throwable ignored) {
        return 1
    }
}

String baseUrl = frontendUrl()
String password = testPassword()
int timeout = defaultTimeout()
int stepDelay = stepDelaySeconds()
def pause = { WebUI.delay(stepDelay) }
String runId = System.currentTimeMillis().toString()
String username = "qa_user_${runId}"
String email = "${username}@example.com"
String title = "Katalon Comment Article ${runId}"
String description = "Article for comment automation"
String body = "This article is used for validating comment creation."
String tag = "commenttag${runId}"
String comment = "Katalon comment ${runId}"

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.waitForPageLoad(timeout)
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    WebUI.navigateToUrl(baseUrl)
    pause()

    WebUI.click(xpath('Sign up nav', "(//a[normalize-space()='Sign up'])[1]"))
    pause()
    WebUI.waitForElementVisible(xpath('Sign Up heading', "//h1[normalize-space()='Sign Up']"), timeout)
    WebUI.setText(xpath('Username input', "//input[@placeholder='Username']"), username)
    pause()
    WebUI.setText(xpath('Email input', "//input[@placeholder='Email']"), email)
    pause()
    WebUI.setText(xpath('Password input', "//input[@placeholder='Password']"), password)
    pause()
    WebUI.click(xpath('Sign up button', "//button[normalize-space()='Sign up']"))
    pause()
    WebUI.waitForElementVisible(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"), timeout)

    WebUI.click(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Article title input', "//input[@placeholder='Article Title']"), timeout)
    WebUI.setText(xpath('Article title input', "//input[@placeholder='Article Title']"), title)
    pause()
    WebUI.setText(xpath('Article description input', "//input[@placeholder=\"What's this article about?\"]"), description)
    pause()
    WebUI.setText(xpath('Article body textarea', "//textarea[@placeholder='Write your article (in markdown)']"), body)
    pause()
    WebUI.setText(xpath('Tag input', "//input[@placeholder='Enter tags']"), tag)
    pause()
    WebUI.sendKeys(xpath('Tag input', "//input[@placeholder='Enter tags']"), Keys.chord(Keys.ENTER))
    pause()
    WebUI.click(xpath('Publish Article button', "//button[normalize-space()='Publish Article']"))
    pause()
    WebUI.waitForElementVisible(xpath('Article detail title', "//div[contains(@class,'article-page')]//h1[normalize-space()='${title}']"), timeout)

    WebUI.waitForElementVisible(xpath('Comment textarea', "//textarea[@placeholder='Write a comment...']"), timeout)
    WebUI.setText(xpath('Comment textarea', "//textarea[@placeholder='Write a comment...']"), comment)
    pause()
    WebUI.click(xpath('Post Comment button', "//button[normalize-space()='Post Comment']"))
    pause()
    WebUI.waitForElementVisible(xpath('Created comment', "//*[contains(@class,'card-text') and normalize-space()='${comment}']"), timeout)
} finally {
    WebUI.closeBrowser()
}
