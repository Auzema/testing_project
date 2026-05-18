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
    try {
        return GlobalVariable.FRONTEND_URL.toString()
    } catch (Throwable ignored) {
        return 'http://localhost:4100/'
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
int timeout = defaultTimeout()
int stepDelay = stepDelaySeconds()
def pause = { WebUI.delay(stepDelay) }
String runId = System.currentTimeMillis().toString()

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.waitForPageLoad(timeout)
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    WebUI.navigateToUrl(baseUrl)
    pause()

    WebUI.click(xpath('Sign in nav', "(//a[normalize-space()='Sign in'])[1]"))
    pause()
    WebUI.waitForElementVisible(xpath('Sign In heading', "//h1[normalize-space()='Sign In']"), timeout)
    WebUI.setText(xpath('Email input', "//input[@placeholder='Email']"), "notfound_${runId}@example.com")
    pause()
    WebUI.setText(xpath('Password input', "//input[@placeholder='Password']"), "wrong-password")
    pause()
    WebUI.click(xpath('Sign in button', "//button[normalize-space()='Sign in']"))
    pause()

    WebUI.waitForElementVisible(
        xpath('Invalid login error', "//*[contains(@class,'error-messages')]//li[contains(.,'email or password') and contains(.,'is invalid')]"),
        timeout
    )
} finally {
    WebUI.closeBrowser()
}
