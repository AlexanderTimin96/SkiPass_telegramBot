package ru.timin.telegramBot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
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

}
