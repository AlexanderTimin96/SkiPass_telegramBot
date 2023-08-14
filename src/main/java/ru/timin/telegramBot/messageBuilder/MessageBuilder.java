package ru.timin.telegramBot.messageBuilder;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class MessageBuilder {
    public SendMessage getSimpleMessage(long chatId, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);
        return message;
    }

    public SendMessage getMessageWithInlineKeyboards(long chatId, String msg, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = getSimpleMessage(chatId, msg);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage getMessageWithReplyKeyboards(long chatId, String msg, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage message = getSimpleMessage(chatId, msg);
        message.setReplyMarkup(replyKeyboardMarkup);
        return message;
    }

    public EditMessageText getEditMessageText(long chatId, int msgId, String newMsg) {
        EditMessageText message = new EditMessageText();
        message.setText(newMsg);
        message.setChatId(chatId);
        message.setMessageId(msgId);
        return message;
    }

    public EditMessageText getEditMessageTextWithInlineKeyboards(long chatId, int msgId, String newMsg,
                                                                 InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText message = getEditMessageText(chatId, msgId, newMsg);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }
}
