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
    WebUI.waitForElementVisible(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"), timeout)
}

void createArticle(String title, String runId, int timeout, Closure pause) {
    WebUI.click(xpath('New Post nav', "//a[contains(normalize-space(.),'New Post')]"))
    pause()
    WebUI.waitForElementVisible(xpath('Article title input', "//input[@placeholder='Article Title']"), timeout)
    WebUI.setText(xpath('Article title input', "//input[@placeholder='Article Title']"), title)
    pause()
    WebUI.setText(xpath('Article description input', "//input[@placeholder=\"What's this article about?\"]"), 'Article for favorite test')
    pause()
    WebUI.setText(xpath('Article body textarea', "//textarea[@placeholder='Write your article (in markdown)']"), 'Body for favorite test')
    pause()
    WebUI.setText(xpath('Tag input', "//input[@placeholder='Enter tags']"), "favoritetag${runId}")
    pause()
    WebUI.sendKeys(xpath('Tag input', "//input[@placeholder='Enter tags']"), Keys.chord(Keys.ENTER))
    pause()
    WebUI.click(xpath('Publish Article button', "//button[normalize-space()='Publish Article']"))
    pause()
    WebUI.waitForElementVisible(xpath('Article detail title', "//div[contains(@class,'article-page')]//h1[normalize-space()='${title}']"), timeout)
}

String baseUrl = frontendUrl()
String password = testPassword()
int timeout = defaultTimeout()
int stepDelay = stepDelaySeconds()
def pause = { WebUI.delay(stepDelay) }
String runId = System.currentTimeMillis().toString()
String title = "Favorite Article ${runId}"
String preview = "//div[contains(@class,'article-preview')][.//h1[normalize-space()='${title}']]"

try {
    WebUI.openBrowser(baseUrl)
    pause()
    WebUI.maximizeWindow(FailureHandling.OPTIONAL)
    WebUI.executeJavaScript('window.localStorage.clear();', null)
    registerUser(baseUrl, "favorite_user_${runId}", "favorite_user_${runId}@example.com", password, timeout, pause)
    createArticle(title, runId, timeout, pause)

    WebUI.navigateToUrl(baseUrl)
    pause()
    WebUI.click(xpath('Global Feed tab', "//a[normalize-space()='Global Feed']"))
    pause()
    WebUI.waitForElementVisible(xpath('Created article preview', "${preview}//h1"), timeout)

    WebUI.click(xpath('Favorite button', "${preview}//button"))
    pause()
    WebUI.waitForElementVisible(xpath('Favorited count', "${preview}//button[contains(@class,'btn-primary') and contains(normalize-space(.),'1')]"), timeout)

    WebUI.click(xpath('Unfavorite button', "${preview}//button"))
    pause()
    WebUI.waitForElementVisible(xpath('Unfavorited count', "${preview}//button[contains(@class,'btn-outline-primary') and contains(normalize-space(.),'0')]"), timeout)
} finally {
    WebUI.closeBrowser()
}

