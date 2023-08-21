package ru.timin.telegramBot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {

    public InlineKeyboardMarkup getInlineKeyBoardWithOneButton(String buttonName, String callback) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInLineButtons = new ArrayList<>();

        InlineKeyboardButton startButton = new InlineKeyboardButton(buttonName);
        startButton.setCallbackData(callback);

        firstRowInLineButtons.add(startButton);

        rowsInLine.add(firstRowInLineButtons);

        return new InlineKeyboardMarkup(rowsInLine);
    }

    public InlineKeyboardMarkup getInlineKeyBoardWithRedirect(String buttonName, String callback, String url) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInLineButtons = new ArrayList<>();

        InlineKeyboardButton addLiftsButton = new InlineKeyboardButton(buttonName);
        addLiftsButton.setCallbackData(callback);
        addLiftsButton.setUrl(url);

        firstRowInLineButtons.add(addLiftsButton);

        rowsInLine.add(firstRowInLineButtons);

        return new InlineKeyboardMarkup(rowsInLine);
    }
}
