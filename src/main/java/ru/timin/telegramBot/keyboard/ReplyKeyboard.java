package ru.timin.telegramBot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboard {
    public ReplyKeyboardMarkup getReplyKeyboardRegistry() {
        List<KeyboardRow> rowsButtons = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        KeyboardButton registryButton = new KeyboardButton("Зарегистрироваться >>");
        registryButton.setRequestContact(true);

        keyboardRow.add(registryButton);
        rowsButtons.add(keyboardRow);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(rowsButtons);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
}
