package fxapp.runbrowser.pages;

import com.codeborne.selenide.Selenide;
import fxapp.runbrowser.utils.CookieUtils;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@UtilityClass
public class FacebookPage {

    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("pass");
    private final By loginButton = By.cssSelector("button[name ='login']");

    public void login(String username, String password) {
        CookieUtils.addFacebookConsentCookie();
        Selenide.refresh();
        $(emailInput).setValue(username);
        $(passwordInput).setValue(password);
        $(loginButton).click();
    }
}
