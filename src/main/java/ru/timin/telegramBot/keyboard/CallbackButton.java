package ru.timin.telegramBot.keyboard;

public enum CallbackButton {
    START_BUTTON("START_BUTTON"),
    REGISTRY_BUTTON("REGISTRY_BUTTON");

    private final String callback;

    CallbackButton(String callback) {
        this.callback = callback;
    }

    public String getcallback() {
        return callback;
    }
}
