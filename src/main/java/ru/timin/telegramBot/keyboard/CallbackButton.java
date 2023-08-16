package ru.timin.telegramBot.keyboard;

public enum CallbackButton {
    START_BUTTON("START_BUTTON"),
    ADD_LIFTS_BUTTON("ADD_LIFTS_BUTTON");

    private final String callback;

    CallbackButton(String callback) {
        this.callback = callback;
    }

    public String getcallback() {
        return callback;
    }
}
