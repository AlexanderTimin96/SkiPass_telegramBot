package ru.timin.telegramBot.callbackAction.callbackActionImpl;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.timin.telegramBot.callbackAction.CallbackAction;
import ru.timin.telegramBot.callbackAction.CallbackName;
import ru.timin.telegramBot.messageBuilder.MessageBuilder;
import ru.timin.telegramBot.service.SendBotMessageService;

public class AddSkiPassCallback implements CallbackAction {
    private final SendBotMessageService sendBotMessageService;

    private final MessageBuilder messageBuilder;

    public AddSkiPassCallback(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
        this.messageBuilder = new MessageBuilder();

    }

    @Override
    public void execute(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int msgId = update.getCallbackQuery().getMessage().getMessageId();

        String response = "Отправь мне номер своего ски-пасса (написан на лицевой карты). \n"
                + "Через команду: \"/skipass ********\"\n - где звездочки это твой номер ски-паса";
        EditMessageText msg = messageBuilder.getEditMessageText(chatId,
                msgId, response);
        sendBotMessageService.sendEditMessage(msg);
    }

    @Override
    public String getCommand() {
        return CallbackName.ADD_SKI_PASS.getcallback();
    }
}
