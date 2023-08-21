package ru.timin.telegramBot.callbackAction;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackAction {
        void execute(Update update);
        String getCommand();
}
