package fxapp.runbrowser.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@UtilityClass
public class ChatGptPage {

    private final By loginLink =  By.xpath("//span[text()='Log in']");
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By continueButton = By.name("action");

    public void login(String username, String password) {
        $(loginLink).click();
        Selenide.switchTo().window(WebDriverRunner.getWebDriver().getWindowHandles().size() -1);
        $(usernameInput).setValue(username);
        $(continueButton).click();
        $(passwordInput).setValue(password);
        $(continueButton).click();
        System.out.println("Chat GPT login done.");
    }
}
