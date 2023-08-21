package ru.timin.telegramBot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.timin.telegramBot.callbackAction.CallbackContainer;
import ru.timin.telegramBot.command.CommandContainer;
import ru.timin.telegramBot.menu.MenuCommand;
import ru.timin.telegramBot.registrar.Registrar;
import ru.timin.telegramBot.repository.ClientRepository;
import ru.timin.telegramBot.repository.SkiPassRepository;
import ru.timin.telegramBot.service.SendBotMessageService;
import ru.timin.telegramBot.service.SendBotMessageServiceImpl;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    private final CommandContainer commandContainer;
    private final CallbackContainer callbackContainer;
    private final Registrar registrar;


    public TelegramBot(@Value("${bot.token}") String botToken,
                       ClientRepository clientRepository,
                       SkiPassRepository skiPassRepository) {

        super(botToken);

        SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(this);

        commandContainer = new CommandContainer(skiPassRepository, clientRepository, sendBotMessageService);
        registrar = new Registrar(clientRepository, sendBotMessageService);
        callbackContainer = new CallbackContainer(clientRepository,sendBotMessageService);

        MenuCommand menuCommand = new MenuCommand();
        try {
            execute(menuCommand.getMyCommands());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String msgText = update.getMessage().getText();
            String commandIdentifier = msgText.split(" ")[0].toLowerCase();
            commandContainer.retrieveCommand(commandIdentifier).execute(update);
        }

        if (update.hasMessage() && update.getMessage().hasContact()) {
            registrar.registerClient(update);
        }

        if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            callbackContainer.retrieveCommand(callBackData).execute(update);
            }
    }

//    private void sendMessage(SendMessage msg) {
//        try {
//            execute(msg);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void sendEditMessage(EditMessageText msg) {
//        try {
//            execute(msg);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//

//
//    private void responseStartButton(long chatId, int msgId) {
//        if (clientRepository.findById(chatId).get().getSkiPass() != null) {
//            printSkiPass(chatId, msgId);
//        } else {
//            String response = "К сожалению у меня нет твоего ски-пасса. Давай добавим его! \n " +
//                    "Жми на кнопку \"Привязать ски-пасс >>\"";
//            EditMessageText msg = messageBuilder.getEditMessageTextWithInlineKeyboards(chatId,
//                    msgId, response, inlineKeyboard.getInlineKeyBoardWithAddSkiPass());
//            sendEditMessage(msg);
//        }
//    }
//
//    private void responseAddSkiPassButton(long chatId, int msgId) {
//        String response = "Отправь мне номер своего ски-пасса (написан на лицевой карты). \n"
//                + "Через команду: \"skipass ********\"\n - где звездочки это твой номер ски-паса";
//        EditMessageText msg = messageBuilder.getEditMessageText(chatId,
//                msgId, response);
//        sendEditMessage(msg);
//    }

//    private void printSkiPass(long chatId) {
//        SkiPass skiPass = clientRepository.findById(chatId).get().getSkiPass();
//        String response = "Привязанный к твоему номеру ски-пасс: " + skiPass.getSkiPassNumber() + "\n"
//                + "У тебя осталось подъемов: " + skiPass.getLifts() + "\n"
//                + "Если вы хотите добавить подъемы нажмите кнопку \"Добавить подъемы >>\"";
//        SendMessage msg = messageBuilder.getMessageWithInlineKeyboards(chatId,
//                response, inlineKeyboard.getInlineKeyBoardWithRedirect("https://www.bank.ru/&skipassnumber="
//                        + skiPass.getSkiPassNumber()));
//        sendMessage(msg);
//    }
//
//    private void printSkiPass(long chatId, int msgId) {
//        SkiPass skiPass = clientRepository.findById(chatId).get().getSkiPass();
//        String response = "Привязанный к твоему номеру ски-пасс: " + skiPass.getSkiPassNumber() + "\n"
//                + "У тебя осталось подъемов: " + skiPass.getLifts() + "\n"
//                + "Если вы хотите добавить подъемы нажмите кнопку \"Добавить подъемы >>\"";
//        EditMessageText msg = messageBuilder.getEditMessageTextWithInlineKeyboards(chatId,
//                msgId, response, inlineKeyboard.getInlineKeyBoardWithRedirect("https://www.bank.ru/&skipassnumber="
//                        + skiPass.getSkiPassNumber()));
//        sendEditMessage(msg);
//    }
}