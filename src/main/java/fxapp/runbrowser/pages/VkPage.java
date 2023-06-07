package fxapp.runbrowser.pages;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@UtilityClass
public class VkPage {
    private final By emailInput = By.id("index_email");
    private final By passwordInput = By.name("password");
    private final By loginButton = By.cssSelector("button[type ='submit']");

    public void login(String username, String password) {
        $(emailInput).setValue(username);
        $(loginButton).click();
        $(passwordInput).setValue(password);
        $(loginButton).click();
        System.out.println("VK login done.");
    }

}
