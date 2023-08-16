package ru.timin.telegramBot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.timin.telegramBot.keyboard.CallbackButton;
import ru.timin.telegramBot.keyboard.InlineKeyboard;
import ru.timin.telegramBot.keyboard.ReplyKeyboard;
import ru.timin.telegramBot.menu.MenuCommand;
import ru.timin.telegramBot.messageBuilder.MessageBuilder;
import ru.timin.telegramBot.model.Client;
import ru.timin.telegramBot.repository.ClientRepository;
import ru.timin.telegramBot.repository.SkiPassRepository;

import java.util.List;

@Service
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    private final InlineKeyboard inlineKeyboard;
    private final ReplyKeyboard replyKeyboard;
    private final MessageBuilder messageBuilder;

    private final ClientRepository clientRepository;
    private final SkiPassRepository skiPassRepository;

    public TelegramBot(@Value("${bot.token}") String botToken,
                       MenuCommand menuCommand,
                       InlineKeyboard inlineKeyboard,
                       ReplyKeyboard replyKeyboard,
                       MessageBuilder messageBuilder,
                       ClientRepository clientRepository,
                       SkiPassRepository skiPassRepository) {

        super(botToken);

        this.inlineKeyboard = inlineKeyboard;
        this.replyKeyboard = replyKeyboard;
        this.messageBuilder = messageBuilder;
        this.clientRepository = clientRepository;
        this.skiPassRepository = skiPassRepository;

        try {
            execute(menuCommand.getMyCommands());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String msgText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (msgText.equals("/start")) {
                if (clientRepository.findById(update.getMessage().getChatId()).isEmpty()) {
                    String response = "Привет, " +
                            update.getMessage().getChat().getFirstName() +
                            "! \n " +
                            "Я - электронный кассир горнолыжного комплекса РОЗА ХУТОР." +
                            " Я всегда готов помочь тебе быстро и просто узнать и пополнить" +
                            " баланс твоего ски-пасса. \n" +
                            "А так же буду присылать тебе интересные новости и скидки! \n" +
                            "Необходимо зарегистрироваться. " +
                            "Жми кнопку \"Зарегистрироваться\"!";

                    SendMessage msg = messageBuilder.getMessageWithReplyKeyboards(chatId, response,
                            replyKeyboard.getReplyKeyboardRegistry());
                    sendMessage(msg);
                } else {
                    String response = "Привет, " +
                            update.getMessage().getChat().getFirstName() +
                            "! \n" +
                            "Я - электронный кассир горнолыжного комплекса РОЗА ХУТОР." +
                            " Я всегда готов помочь тебе быстро и просто узнать и пополнить" +
                            " баланс твоего ски-пасса. \n" +
                            "А так же буду присылать тебе интересные новости и скидки! \n" +
                            "Жми кнопку \"Начать\" и погнали!";

                    SendMessage msg = messageBuilder.getMessageWithInlineKeyboards(chatId, response,
                            inlineKeyboard.getInlineKeyBoardStart());
                    sendMessage(msg);
                }
            }

            if (msgText.equals("/help")) {
                String response = "В данный момент бот не поддерживает никаких команд." +
                        " Управление осуществляется кнопками на экране диалога после запуска бота, через команду /start";
                SendMessage msg = messageBuilder.getSimpleMessage(chatId, response);
                sendMessage(msg);
            }

            if (msgText.contains("/sendforallclients")) {
                String response = msgText.substring(msgText.indexOf(" "));
                List<Client> listAllClients = clientRepository.findAll();

                for (Client client : listAllClients) {
                    SendMessage msg = messageBuilder.getSimpleMessage(client.getChatId(), response);
                    sendMessage(msg);
                }
            }

            if (msgText.equals("/deletemydata")) {
                clientRepository.deleteById(chatId);
                String response = "Удаление ваших данных прошло успешно." +
                        " К сожалению, я больше не смогу посылать тебе интересные новости и скидки! Если решишь вернуться" +
                        " просто запусти меня заново командой: /start";
                SendMessage msg = messageBuilder.getSimpleMessage(chatId, response);
                sendMessage(msg);
            }
        }


        if (update.hasMessage() && update.getMessage().hasContact()) {
            registerClient(update.getMessage());
            String response = "Вы успешно зарегистрировались. Жми кнопку \"Начать\" и погнали!";
            long chatId = update.getMessage().getChatId();

            SendMessage msg = messageBuilder.getMessageWithInlineKeyboards(chatId, response,
                    inlineKeyboard.getInlineKeyBoardStart());

            sendMessage(msg);
        }


//        if (update.hasCallbackQuery()) {
//            String callBackData = update.getCallbackQuery().getData();
//            long chatId = update.getCallbackQuery().getMessage().getChatId();
//            int msgId = update.getCallbackQuery().getMessage().getMessageId();
//
//            if (callBackData.equals(CallbackButton.START_BUTTON.getcallback())) {
//                if (skiPassRepository.findClientByPhone(clientRepository.findById(chatId).get().getPhone()).isPresent()) {
//                    String response = "Привязанный к твоему номеру ски-пасс: "
////                            + СКИПАСС НОМЕР "\n"
////                            + "У вас доступно подъемов: "
////                            + ПОДЪЕМЫ "\n "
//                            + "Если вы хотите добавить подъемы нажмите кнопку \"Добавить подъемы >>\"";
//                    EditMessageText msg = messageBuilder.getEditMessageTextWithInlineKeyboards(chatId,
//                            msgId, response, inlineKeyboard.getInlineKeyBoardWithAddLifts());
//                    sendEditMessage(msg);
//                } else {
//                    String response = "К сожалению у меня нет твоего ски-пасса. Давай добавим его! \n " +
//                            " Жми на кнопку \"Привязать ски-пасс >>\"";
//                    EditMessageText msg = messageBuilder.getEditMessageTextWithInlineKeyboards(chatId,
//                            msgId, response, inlineKeyboard.getInlineKeyBoardWithAddLifts());
//                    sendEditMessage(msg);
//                }
//            }
//
//            if (callBackData.equals(CallbackButton.ADD_LIFTS_BUTTON.getcallback())) {
//                String response = "Тут происходит перенаправление на оплату";
//                EditMessageText msg = messageBuilder.getEditMessageText(chatId,
//                        msgId, response);
//                sendEditMessage(msg);
//            }
//        }
    }


    @Override
    public String getBotUsername() {
        return botName;
    }

    private void sendMessage(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendEditMessage(EditMessageText msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void registerClient(Message message) {
        if (clientRepository.findById(message.getChatId()).isEmpty()) {
            Long chatId = message.getChatId();
            Chat chat = message.getChat();
            Contact contact = message.getContact();

            Client client = new Client();
            client.setChatId(chatId);
            client.setUserName(chat.getUserName());
            client.setFirstName(chat.getFirstName());
            client.setLastName(chat.getLastName());
            client.setPhone(contact.getPhoneNumber());

            System.out.println(contact.getPhoneNumber());

            clientRepository.save(client);
        }
    }
}

