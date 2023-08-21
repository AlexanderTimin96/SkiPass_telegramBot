package ru.timin.telegramBot.callbackAction;

public enum CallbackName {
    START_BUTTON("START_BUTTON"),
    ADD_LIFTS_BUTTON("ADD_LIFTS_BUTTON"),
    ADD_SKI_PASS("ADD_SKI_PASS");
    private final String callback;

    CallbackName(String callback) {
        this.callback = callback;
    }

    public String getcallback() {
        return callback;
    }
}
