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
import ru.timin.telegramBot.model.SkiPass;
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

    public TelegramBot(@Value("${bot.token}") String botToken, ClientRepository clientRepository,
                       SkiPassRepository skiPassRepository) {

        super(botToken);

        this.inlineKeyboard = new InlineKeyboard();
        this.replyKeyboard = new ReplyKeyboard();
        this.messageBuilder = new MessageBuilder();
        MenuCommand menuCommand = new MenuCommand();

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
            int msgId = update.getMessage().getMessageId();


            if (msgText.equals("/start")) {
                String firstName = update.getMessage().getChat().getFirstName();
                responseStart(chatId, firstName);
            } else if (msgText.equals("/help")) {
                responseHelp(chatId);
            } else if (msgText.contains("/sendforallclients")) {
                sendMessageForAllClients(msgText);
            } else if (msgText.contains("skipass")) {
                saveSkiPass(msgText, chatId, msgId);
            } else if (msgText.equals("/deletemydata")) {
                deleteData(chatId);
            } else {
                responseDefault(chatId);
            }
        }

        if (update.hasMessage() && update.getMessage().hasContact()) {
            registerClient(update.getMessage());
        }

        if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int msgId = update.getCallbackQuery().getMessage().getMessageId();

            if (callBackData.equals(CallbackButton.START_BUTTON.getcallback())) {
                responseStartButton(chatId, msgId);
            } else if (callBackData.equals(CallbackButton.ADD_SKI_PASS_BUTTON.getcallback())) {
                responseAddSkiPassButton(chatId, msgId);
            }
        }
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

            clientRepository.save(client);

            String response = "Вы успешно зарегистрировались. Жми кнопку \"Начать\" и погнали!";
            SendMessage msg = messageBuilder.getMessageWithInlineKeyboards(message.getChatId(), response,
                    inlineKeyboard.getInlineKeyBoardStart());

            sendMessage(msg);
        }
    }

    private void responseStart(long chatId, String firstName) {
        String response = "Привет, " +
                firstName +
                "! \n " +
                "Я - электронный кассир горнолыжного комплекса РОЗА ХУТОР." +
                " Я всегда готов помочь тебе быстро и просто узнать и пополнить" +
                " баланс твоего ски-пасса. \n" +
                "А так же буду присылать тебе интересные новости и скидки! \n";

        SendMessage msg;

        if (clientRepository.findById(chatId).isEmpty()) {
            response += "Необходимо зарегистрироваться. Жми кнопку \"Зарегистрироваться\"!";
            msg = messageBuilder.getMessageWithReplyKeyboards(chatId, response,
                    replyKeyboard.getReplyKeyboardRegistry());
        } else {
            response += "Жми кнопку \"Начать\" и погнали!";
            msg = messageBuilder.getMessageWithInlineKeyboards(chatId, response,
                    inlineKeyboard.getInlineKeyBoardStart());
        }
        sendMessage(msg);
    }

    private void responseHelp(long chatId) {
        String response = "Управление осуществляется кнопками на экране диалога после запуска бота, через команду /start";
        SendMessage msg = messageBuilder.getSimpleMessage(chatId, response);
        sendMessage(msg);
    }

    private void sendMessageForAllClients(String msgText) {
        String response = msgText.substring(msgText.indexOf(" "));
        List<Client> listAllClients = clientRepository.findAll();

        for (Client client : listAllClients) {
            SendMessage msg = messageBuilder.getSimpleMessage(client.getChatId(), response);
            sendMessage(msg);
        }
    }

    private void saveSkiPass(String msgText, long chatId, int msgId) {
        String skiPassNumber = msgText.substring(msgText.indexOf(" ") + 1);

        if (skiPassRepository.findBySkiPassNumber(skiPassNumber).isPresent()) {
            SkiPass skiPass = skiPassRepository.findBySkiPassNumber(skiPassNumber).get();
            Client client = clientRepository.findById(chatId).get();
            client.setSkiPass(skiPass);
            clientRepository.save(client);
            printSkiPass(chatId);
        } else {
            String response = "Введены некорректные данные. Попробуй еще раз!\n" +
                    "Отправь мне номер своего ски-пасса (написан на лицевой карты).\n"
                    + "Через команду: \"skipass ********\"\n - где звездочки это твой номер ски-паса или "
                    + "обратись к администратору горнолыжного курорты";
            SendMessage msg = messageBuilder.getSimpleMessage(chatId, response);
            sendMessage(msg);
        }
    }

    private void deleteData(long chatId) {
        String response;
        if (clientRepository.findById(chatId).isPresent()) {
            Client client = clientRepository.findById(chatId).get();
            client.setSkiPass(null);
            clientRepository.save(client);

            clientRepository.deleteById(chatId);

            response = "Удаление ваших данных прошло успешно." +
                    " К сожалению, я больше не смогу посылать тебе интересные новости и скидки! Если решишь вернуться" +
                    " просто запусти меня заново командой: /start";
        } else {
            response = "Мы еще не познакомились, поэтому твоих данных у меня нет." +
                    " Запусти меня командой: /start";
        }
        SendMessage msg = messageBuilder.getSimpleMessage(chatId, response);
        sendMessage(msg);
    }

    private void responseStartButton(long chatId, int msgId) {
        if (clientRepository.findById(chatId).get().getSkiPass() != null) {
            printSkiPass(chatId, msgId);
        } else {
            String response = "К сожалению у меня нет твоего ски-пасса. Давай добавим его! \n " +
                    "Жми на кнопку \"Привязать ски-пасс >>\"";
            EditMessageText msg = messageBuilder.getEditMessageTextWithInlineKeyboards(chatId,
                    msgId, response, inlineKeyboard.getInlineKeyBoardWithAddSkiPass());
            sendEditMessage(msg);
        }
    }

    private void responseAddSkiPassButton(long chatId, int msgId) {
        String response = "Отправь мне номер своего ски-пасса (написан на лицевой карты). \n"
                + "Через команду: \"skipass ********\"\n - где звездочки это твой номер ски-паса";
        EditMessageText msg = messageBuilder.getEditMessageText(chatId,
                msgId, response);
        sendEditMessage(msg);
    }

    private void responseDefault(long chatId) {
        String response = "Такой команды я не знаю, для начала работы используй меню, либо воспользуйся командой /start";
        SendMessage msg = messageBuilder.getSimpleMessage(chatId, response);
        sendMessage(msg);
    }

    private void printSkiPass(long chatId) {
        SkiPass skiPass = clientRepository.findById(chatId).get().getSkiPass();
        String response = "Привязанный к твоему номеру ски-пасс: " + skiPass.getSkiPassNumber() + "\n"
                + "У тебя осталось подъемов: " + skiPass.getLifts() + "\n"
                + "Если вы хотите добавить подъемы нажмите кнопку \"Добавить подъемы >>\"";
        SendMessage msg = messageBuilder.getMessageWithInlineKeyboards(chatId,
                response, inlineKeyboard.getInlineKeyBoardWithAddLifts("https://www.bank.ru/&skipassnumber="
                        + skiPass.getSkiPassNumber()));
        sendMessage(msg);
    }

    private void printSkiPass(long chatId, int msgId) {
        SkiPass skiPass = clientRepository.findById(chatId).get().getSkiPass();
        String response = "Привязанный к твоему номеру ски-пасс: " + skiPass.getSkiPassNumber() + "\n"
                + "У тебя осталось подъемов: " + skiPass.getLifts() + "\n"
                + "Если вы хотите добавить подъемы нажмите кнопку \"Добавить подъемы >>\"";
        EditMessageText msg = messageBuilder.getEditMessageTextWithInlineKeyboards(chatId,
                msgId, response, inlineKeyboard.getInlineKeyBoardWithAddLifts("https://www.bank.ru/&skipassnumber="
                        + skiPass.getSkiPassNumber()));
        sendEditMessage(msg);
    }
}