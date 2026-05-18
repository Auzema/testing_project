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

String baseUrl = frontendUrl()
String password = testPassword()
int timeout = defaultTimeout()
int stepDelay = stepDelaySeconds()
def pause = { WebUI.delay(stepDelay) }

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.executeJavaScript('window.localStorage.clear();', null)

    WebUI.navigateToUrl(baseUrl + 'login')
    pause()
    WebUI.waitForElementVisible(xpath('Sign In heading', "//h1[normalize-space()='Sign In']"), timeout)
    WebUI.setText(xpath('Password input', "//input[@placeholder='Password']"), password)
    pause()
    WebUI.click(xpath('Sign in button', "//button[normalize-space()='Sign in']"))
    pause()
    WebUI.waitForElementVisible(
        xpath('Email blank error', "//*[contains(@class,'error-messages')]//li[contains(.,'email') and contains(.,'blank')]"),
        timeout
    )

    WebUI.navigateToUrl(baseUrl + 'login')
    pause()
    WebUI.waitForElementVisible(xpath('Sign In heading again', "//h1[normalize-space()='Sign In']"), timeout)
    WebUI.setText(xpath('Email input', "//input[@placeholder='Email']"), 'blank_password@example.com')
    pause()
    WebUI.click(xpath('Sign in button again', "//button[normalize-space()='Sign in']"))
    pause()
    WebUI.waitForElementVisible(
        xpath('Password blank error', "//*[contains(@class,'error-messages')]//li[contains(.,'password') and contains(.,'blank')]"),
        timeout
    )
} finally {
    WebUI.closeBrowser()
}

