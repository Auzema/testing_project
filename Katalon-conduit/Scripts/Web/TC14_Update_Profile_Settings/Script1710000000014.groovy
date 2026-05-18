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
    WebUI.waitForElementVisible(xpath('Settings nav', "//a[contains(normalize-space(.),'Settings')]"), timeout)
}

String baseUrl = frontendUrl()
String password = testPassword()
int timeout = defaultTimeout()
int stepDelay = stepDelaySeconds()
def pause = { WebUI.delay(stepDelay) }
String runId = System.currentTimeMillis().toString()
String newUsername = "updated_user_${runId}"
String newEmail = "updated_user_${runId}@example.com"
String newBio = "Updated bio from Katalon ${runId}"

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    registerUser(baseUrl, "settings_user_${runId}", "settings_user_${runId}@example.com", password, timeout, pause)

    WebUI.click(xpath('Settings nav', "//a[contains(normalize-space(.),'Settings')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Settings heading', "//h1[normalize-space()='Your Settings']"), timeout)

    WebUI.clearText(xpath('Username input', "//input[@placeholder='Username']"))
    WebUI.setText(xpath('Username input', "//input[@placeholder='Username']"), newUsername)
    pause()
    WebUI.clearText(xpath('Bio textarea', "//textarea[@placeholder='Short bio about you']"))
    WebUI.setText(xpath('Bio textarea', "//textarea[@placeholder='Short bio about you']"), newBio)
    pause()
    WebUI.clearText(xpath('Email input', "//input[@placeholder='Email']"))
    WebUI.setText(xpath('Email input', "//input[@placeholder='Email']"), newEmail)
    pause()
    WebUI.click(xpath('Update Settings button', "//button[normalize-space()='Update Settings']"))
    pause()

    WebUI.waitForElementVisible(xpath('Updated username in nav', "//a[contains(normalize-space(.),'${newUsername}')]"), timeout)
    WebUI.verifyTextPresent(newUsername, false)
} finally {
    WebUI.closeBrowser()
}

