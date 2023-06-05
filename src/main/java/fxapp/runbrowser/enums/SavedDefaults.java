package fxapp.runbrowser.enums;

import lombok.Getter;

import java.util.Arrays;

public enum SavedDefaults {

    EMPTY(null),
    VK("https://vk.com/"),
    GMAIL("https://mail.google.com/"),
    FACEBOOK("https://www.facebook.com/"),
    CHATGPT("https://openai.com/blog/chatgpt");

    @Getter
    private final String url;

    SavedDefaults(String url) {
        this.url = url;
    }

    public static SavedDefaults getDefaultByName(String name) {
        return Arrays.stream(SavedDefaults.values())
                .filter(value -> value.name().equals(name))
                .findFirst().orElse(EMPTY);
    }
}
