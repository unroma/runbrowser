package fxapp.runbrowser;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import fxapp.runbrowser.model.TabValue;
import fxapp.runbrowser.pages.ChatGptPage;
import fxapp.runbrowser.pages.FacebookPage;
import fxapp.runbrowser.pages.LinkedinPage;
import fxapp.runbrowser.pages.VkPage;
import fxapp.runbrowser.utils.EncryptUtils;
import fxapp.runbrowser.utils.ProcessUtils;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Selenide.open;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@UtilityClass
public class ChromeDriverManager {

    public void initDriver(String path) {
        setConfiguration(path);
        openTabs();
        Storage.getTabs().clear();
    }

    private void setConfiguration(String path) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        if (!path.isEmpty()) {
            options.addArguments("user-data-dir=".concat(path));
        }
        options.addArguments("--no-sandbox");
        Configuration.browser = CHROME;
        Configuration.baseUrl = "about:blank";
        Configuration.holdBrowserOpen = true;
        Configuration.screenshots = false;
        Configuration.savePageSource = false;
        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    public void closeDriver() {
        Selenide.closeWebDriver();
        System.out.println("Web driver closed");
        ProcessUtils.killChromeProcess();
    }

    private void openTabs() {
        IntStream.range(0, Storage.getTabs().size()).forEach(index -> {
            if (index == 0) {
                openUrl(Storage.getTabs().get(index).getUrl());
            } else {
                String script = String.format("window.open('%s', '_blank');", Storage.getTabs().get(index).getUrl());
                Selenide.executeJavaScript(script);
                Selenide.switchTo().window(WebDriverRunner.getWebDriver().getWindowHandles().size() -1);
            }
            loginPage(Storage.getTabs().get(index));
        });
    }

    private void openUrl(String url) {
        if (!isEmpty(url)) {
            open(url);
        } else {
            open();
        }
    }

    private void loginPage(TabValue tabValue) {
        String username = EncryptUtils.decryptText(tabValue.getUsername());
        String password = EncryptUtils.decryptText(tabValue.getPassword());
        if (isEmpty(username) || isEmpty(password)) return;
        switch (tabValue.getSavedDefault()) {
            case FACEBOOK -> withElementExceptionHandle(FacebookPage::login, username, password);
            case CHATGPT -> withElementExceptionHandle(ChatGptPage::login, username, password);
            case LINKEDIN -> withElementExceptionHandle(LinkedinPage::login, username, password);
            case VK -> withElementExceptionHandle(VkPage::login, username, password);
        }
    }

    private void withElementExceptionHandle(BiConsumer<String,String> loginMethod, String username, String password) {
        try {
            loginMethod.accept(username,password);
        } catch (ElementNotFound | NoSuchElementException | ElementShould e) {
            System.out.printf("Got exception during login:\n%s", e.getMessage());
        }
    }
}
