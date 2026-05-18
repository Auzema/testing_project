import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

TestObject xpath(String name, String selector) {
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

String frontendUrl() {
    try { return GlobalVariable.FRONTEND_URL.toString() } catch (Throwable ignored) { return 'http://localhost:4100/' }
}

String testPassword() {
    try { return GlobalVariable.TEST_PASSWORD.toString() } catch (Throwable ignored) { return 'Password123!' }
}

int defaultTimeout() {
    try { return GlobalVariable.DEFAULT_TIMEOUT.toString().toInteger() } catch (Throwable ignored) { return 15 }
}

int stepDelaySeconds() {
    try { return GlobalVariable.STEP_DELAY_SECONDS.toString().toInteger() } catch (Throwable ignored) { return 1 }
}

void registerUser(String baseUrl, String username, String email, String password, int timeout, Closure pause) {
    WebUI.navigateToUrl(baseUrl + 'register')
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
    WebUI.waitForElementVisible(xpath('Authenticated nav', "//a[contains(normalize-space(.),'Settings')]"), timeout)
}

String baseUrl = frontendUrl()
String password = testPassword()
int timeout = defaultTimeout()
int stepDelay = stepDelaySeconds()
def pause = { WebUI.delay(stepDelay) }
String runId = System.currentTimeMillis().toString()
String authorUsername = "author_${runId}"
String followerUsername = "follower_${runId}"

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.executeJavaScript('window.localStorage.clear();', null)

    registerUser(baseUrl, authorUsername, "${authorUsername}@example.com", password, timeout, pause)
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    WebUI.navigateToUrl(baseUrl)
    pause()

    registerUser(baseUrl, followerUsername, "${followerUsername}@example.com", password, timeout, pause)
    WebUI.navigateToUrl(baseUrl + "@${authorUsername}")
    pause()

    WebUI.waitForElementVisible(xpath('Author profile heading', "//h4[normalize-space()='${authorUsername}']"), timeout)
    WebUI.waitForElementVisible(xpath('Follow button', "//button[contains(normalize-space(.),'Follow ${authorUsername}')]"), timeout)
    WebUI.click(xpath('Follow button', "//button[contains(normalize-space(.),'Follow ${authorUsername}')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Unfollow button', "//button[contains(normalize-space(.),'Unfollow ${authorUsername}')]"), timeout)
    WebUI.click(xpath('Unfollow button', "//button[contains(normalize-space(.),'Unfollow ${authorUsername}')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Follow button again', "//button[contains(normalize-space(.),'Follow ${authorUsername}')]"), timeout)
} finally {
    WebUI.closeBrowser()
}

