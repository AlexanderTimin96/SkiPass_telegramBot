package ru.timin.telegramBot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface SendBotMessageService {
    void sendMessage(SendMessage sendMessage);

    void sendEditMessage(EditMessageText editMessageText);
}
