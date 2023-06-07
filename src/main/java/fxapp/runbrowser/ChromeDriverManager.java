package fxapp.runbrowser;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import fxapp.runbrowser.model.TabValue;
import fxapp.runbrowser.pages.ChatGptPage;
import fxapp.runbrowser.pages.FacebookPage;
import fxapp.runbrowser.pages.LinkedinPage;
import fxapp.runbrowser.pages.VkPage;
import fxapp.runbrowser.utils.EncryptUtils;
import fxapp.runbrowser.utils.ProcessUtils;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.stream.IntStream;

import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Selenide.open;

public class ChromeDriverManager {

    private static int tabIncrement = 0;

    public static void initDriver(String path) {
        setConfiguration(path);
        openTabs();
        Storage.getTabs().clear();
    }

    private static void setConfiguration(String path) {
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

    public static void closeDriver() {
        tabIncrement = 0;
        Selenide.closeWebDriver();
        System.out.println("Web driver closed");
        ProcessUtils.killChromeProcess();
    }

    private static void openTabs() {
        IntStream.range(0, Storage.getTabs().size()).forEach(index -> {
            if (index == 0) {
                openUrl(Storage.getTabs().get(index).getUrl());
            } else {
                String script = String.format("window.open('%s', '_blank');", Storage.getTabs().get(index).getUrl());
                Selenide.executeJavaScript(script);
                Selenide.switchTo().window(index + tabIncrement);
            }
            loginPage(Storage.getTabs().get(index), index);
        });
    }

    private static void openUrl(String url) {
        if (url != null && !url.isEmpty()) {
            open(url);
        } else {
            open();
        }
    }

    private static void loginPage(TabValue tabValue, int index) {
        String username = EncryptUtils.decryptText(tabValue.getUsername());
        String password = EncryptUtils.decryptText(tabValue.getPassword());
        switch (tabValue.getSavedDefault()) {
            case FACEBOOK -> FacebookPage.login(username, password);
            case CHATGPT -> {
                ChatGptPage.login(username, password, index +1);
                tabIncrement++;
            }
            case LINKEDIN -> LinkedinPage.login(username, password);
            case VK -> VkPage.login(username, password);
        }
    }
}
