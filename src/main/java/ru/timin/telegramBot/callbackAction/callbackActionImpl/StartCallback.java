package ru.timin.telegramBot.callbackAction.callbackActionImpl;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.timin.telegramBot.callbackAction.CallbackAction;
import ru.timin.telegramBot.callbackAction.CallbackName;
import ru.timin.telegramBot.entity.SkiPass;
import ru.timin.telegramBot.keyboard.InlineKeyboard;
import ru.timin.telegramBot.messageBuilder.MessageBuilder;
import ru.timin.telegramBot.repository.ClientRepository;
import ru.timin.telegramBot.service.SendBotMessageService;

public class StartCallback implements CallbackAction {
    private final SendBotMessageService sendBotMessageService;
    private final ClientRepository clientRepository;

    private final InlineKeyboard inlineKeyboard;
    private final MessageBuilder messageBuilder;

    public StartCallback(SendBotMessageService sendBotMessageService,
                         ClientRepository clientRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.clientRepository = clientRepository;
        this.inlineKeyboard = new InlineKeyboard();
        this.messageBuilder = new MessageBuilder();
    }


    @Override
    public void execute(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int msgId = update.getCallbackQuery().getMessage().getMessageId();
        EditMessageText msg;

        if (clientRepository.findById(chatId).get().getSkiPass() != null) {
            SkiPass skiPass = clientRepository.findById(chatId).get().getSkiPass();
            String response = "Привязанный к твоему номеру ски-пасс: " + skiPass.getSkiPassNumber() + "\n"
                    + "У тебя осталось подъемов: " + skiPass.getLifts() + "\n"
                    + "Если вы хотите добавить подъемы нажмите кнопку \"Добавить подъемы >>\"";
            msg = messageBuilder.getEditMessageTextWithInlineKeyboards(chatId,
                    msgId, response, inlineKeyboard.getInlineKeyBoardWithRedirect("Добавить подъемы >>",
                            CallbackName.ADD_LIFTS_BUTTON.getcallback(),
                            "https://www.bank.ru/&skipassnumber=" + skiPass.getSkiPassNumber()));
        } else {
            String response = "К сожалению у меня нет твоего ски-пасса. Давай добавим его! \n " +
                    "Жми на кнопку \"Привязать ски-пасс >>\"";
            msg = messageBuilder.getEditMessageTextWithInlineKeyboards(chatId,
                    msgId, response, inlineKeyboard.getInlineKeyBoardWithOneButton("Привязать ски-пасс >>",
                            CallbackName.ADD_SKI_PASS.getcallback()));
        }
        sendBotMessageService.sendEditMessage(msg);
    }

    @Override
    public String getCommand() {
        return CallbackName.START_BUTTON.getcallback();
    }
}
