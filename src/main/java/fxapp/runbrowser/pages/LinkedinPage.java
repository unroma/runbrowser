package fxapp.runbrowser.pages;

import com.codeborne.selenide.Selenide;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@UtilityClass
public class LinkedinPage {

    private final By emailInput = By.name("session_key");
    private final By passwordInput = By.name("session_password");
    private final By loginButton = By.cssSelector("button[data-id='sign-in-form__submit-btn']");
    private final By denyCookieButton = By.cssSelector("button[action-type='DENY'");
    private final By loginLink = By.cssSelector("[data-tracking-control-name='auth_wall_desktop-login-toggle']");

    public void login(String username, String password) {
        if (!$(denyCookieButton).isDisplayed()) {
            Selenide.refresh();
        }
        $(denyCookieButton).click();
        if ($(loginLink).isDisplayed()) {
            $(loginLink).click();
        }
        $(emailInput).setValue(username);
        $(passwordInput).setValue(password);
        $(loginButton).click();
        System.out.println("Linkedin login done.");
    }
}
