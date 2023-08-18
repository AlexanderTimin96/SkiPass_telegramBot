package ru.timin.telegramBot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {

    public InlineKeyboardMarkup getInlineKeyBoardStart() {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInLineButtons = new ArrayList<>();

        InlineKeyboardButton startButton = new InlineKeyboardButton("Начать >>");
        startButton.setCallbackData(CallbackButton.START_BUTTON.getcallback());

        firstRowInLineButtons.add(startButton);

        rowsInLine.add(firstRowInLineButtons);

        return new InlineKeyboardMarkup(rowsInLine);
    }

    public InlineKeyboardMarkup getInlineKeyBoardWithAddLifts() {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInLineButtons = new ArrayList<>();

        InlineKeyboardButton addLiftsButton = new InlineKeyboardButton("Добавить подъемы >>");
        addLiftsButton.setCallbackData(CallbackButton.ADD_LIFTS_BUTTON.getcallback());

        firstRowInLineButtons.add(addLiftsButton);

        rowsInLine.add(firstRowInLineButtons);

        return new InlineKeyboardMarkup(rowsInLine);
    }

    public InlineKeyboardMarkup getInlineKeyBoardWithAddSkiPass() {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInLineButtons = new ArrayList<>();

        InlineKeyboardButton skiPassButton = new InlineKeyboardButton("Привязать ски-пасс >>");
        skiPassButton.setCallbackData(CallbackButton.ADD_SKI_PASS_BUTTON.getcallback());

        firstRowInLineButtons.add(skiPassButton);

        rowsInLine.add(firstRowInLineButtons);

        return new InlineKeyboardMarkup(rowsInLine);
    }
}
