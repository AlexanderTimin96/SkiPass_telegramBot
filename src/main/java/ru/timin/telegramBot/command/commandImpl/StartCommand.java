package ru.timin.telegramBot.command.commandImpl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.timin.telegramBot.command.Command;
import ru.timin.telegramBot.command.CommandName;
import ru.timin.telegramBot.callbackAction.CallbackName;
import ru.timin.telegramBot.keyboard.InlineKeyboard;
import ru.timin.telegramBot.keyboard.ReplyKeyboard;
import ru.timin.telegramBot.messageBuilder.MessageBuilder;
import ru.timin.telegramBot.repository.ClientRepository;
import ru.timin.telegramBot.service.SendBotMessageService;

public class StartCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final ClientRepository clientRepository;

    private final ReplyKeyboard replyKeyboard;
    private final InlineKeyboard inlineKeyboard;
    private final MessageBuilder messageBuilder;

    public StartCommand(SendBotMessageService sendBotMessageService, ClientRepository clientRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.clientRepository = clientRepository;
        this.replyKeyboard = new ReplyKeyboard();
        this.messageBuilder = new MessageBuilder();
        this.inlineKeyboard = new InlineKeyboard();
    }

    @Override
    public void execute(Update update) {
        String response = "Привет, " +
                update.getMessage().getChat().getFirstName() +
                "! \n " +
                "Я - электронный кассир горнолыжного комплекса РОЗА ХУТОР." +
                " Я всегда готов помочь тебе быстро и просто узнать и пополнить" +
                " баланс твоего ски-пасса. \n" +
                "А так же буду присылать тебе интересные новости и скидки! \n";

        SendMessage msg;
        if (clientRepository.findById(update.getMessage().getChatId()).isEmpty()) {
            response += "Необходимо зарегистрироваться. Жми кнопку \"Зарегистрироваться\"!";
            msg = messageBuilder.getMessageWithReplyKeyboards(update.getMessage().getChatId(), response,
                    replyKeyboard.getReplyKeyboardWithRequestContact("Зарегистрироваться >>"));
        } else {
            response += "Жми кнопку \"Начать\" и погнали!";
            msg = messageBuilder.getMessageWithInlineKeyboards(update.getMessage().getChatId(), response,
                    inlineKeyboard.getInlineKeyBoardWithOneButton("Начать >>", CallbackName.START_BUTTON.getcallback()));
        }

        sendBotMessageService.sendMessage(msg);
    }

    @Override
    public String getCommand() {
        return CommandName.START.getCommandName();
    }
}
