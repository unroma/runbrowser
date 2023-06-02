package fxapp.runbrowser.enums;

import lombok.Getter;

import java.util.Arrays;

public enum SavedDefaults {

    EMPTY("", null),
    VK("VK", "https://vk.com/"),
    GMAIL("GMAIL", "https://mail.google.com/"),
    FACEBOOK("FACEBOOK", "https://www.facebook.com/"),
    CHAT_GPT("CHAT GPT", "https://chat.openai.com/");

    @Getter
    private final String name;
    @Getter
    private final String url;
    SavedDefaults(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static SavedDefaults getDefaultByName(String name) {
        return Arrays.stream(SavedDefaults.values())
                .filter(value ->value.getName().equals(name))
                .findFirst().orElseThrow();
    }
}
