package fxapp.runbrowser;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import fxapp.runbrowser.model.TabValue;
import lombok.Getter;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Selenide.open;

public class ChromeDriverManager {

    @Getter
    private static final List<TabValue> tabs = new ArrayList<>();

    public static void initDriver(String path) {
        setConfiguration(path);
        Selenide.open();
        openTabs();
        tabs.clear();
    }

    private static void setConfiguration(String path) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        if (!path.isEmpty()) {
            options.addArguments("user-data-dir=".concat(path));
        }
        Configuration.browser = CHROME;
        Configuration.holdBrowserOpen = true;
        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        Configuration.baseUrl ="about:blank";
    }

    public static void closeDriver() {
        Selenide.closeWebDriver();
        System.out.println("Web driver closed");
        Utils.killChromeProcess();
    }

    private static void openTabs() {
        IntStream.range(0, tabs.size()).forEach(index -> {
            if (index == 0) {
                openUrl(tabs.get(index).getUrl());
            } else {
                String script = String.format("window.open('%s', '_blank');",tabs.get(index).getUrl());
                Selenide.executeJavaScript(script);
            }
        });
    }

    private static void openUrl(String url) {
        if (url != null && !url.isEmpty()) {
            open(url);
        } else {
            open();
        }
    }
}
