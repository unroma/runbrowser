package fxapp.runbrowser.enums;

import lombok.Getter;

import java.util.Arrays;

public enum SavedDefaults {

    EMPTY(null),
    VK("https://vk.com/"),
    FACEBOOK("https://www.facebook.com/"),
    CHATGPT("https://openai.com/blog/chatgpt"),
    LINKEDIN("https://www.linkedin.com");

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
