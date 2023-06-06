package fxapp.runbrowser.utils;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.Cookie;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

@UtilityClass
public class CookieUtils {

    private final String FACEBOOK_CONSENT_COOKIE_NAME = "datr";
    private final String FACEBOOK_CONSENT_COOKIE_VALUE = "pEt-ZL9BeN_VHOAOF4j-PPeu";

    public void addFacebookConsentCookie() {
        Cookie cookie = new Cookie.Builder(FACEBOOK_CONSENT_COOKIE_NAME, FACEBOOK_CONSENT_COOKIE_VALUE)
                .domain(".facebook.com").path("/").isSecure(true).expiresOn(getDateForCookiePlusYear()).build();
        getWebDriver().manage().addCookie(cookie);
    }

    private Date getDateForCookiePlusYear() {
        return Date.from(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
