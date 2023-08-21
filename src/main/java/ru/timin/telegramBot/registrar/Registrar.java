package ru.timin.telegramBot.registrar;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.timin.telegramBot.callbackAction.CallbackName;
import ru.timin.telegramBot.entity.Client;
import ru.timin.telegramBot.keyboard.InlineKeyboard;
import ru.timin.telegramBot.messageBuilder.MessageBuilder;
import ru.timin.telegramBot.repository.ClientRepository;
import ru.timin.telegramBot.service.SendBotMessageService;

public class Registrar {
    private final ClientRepository clientRepository;
    private final SendBotMessageService sendBotMessageService;

    private final MessageBuilder messageBuilder;
    private final InlineKeyboard inlineKeyboard;

    public Registrar(ClientRepository clientRepository, SendBotMessageService sendBotMessageService1) {
        this.clientRepository = clientRepository;
        this.sendBotMessageService = sendBotMessageService1;
        this.inlineKeyboard = new InlineKeyboard();
        this.messageBuilder = new MessageBuilder();
    }

    public void registerClient(Update update) {
        Message message = update.getMessage();

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
                    inlineKeyboard.getInlineKeyBoardWithOneButton("Начать >>", CallbackName.START_BUTTON.getcallback()));

            sendBotMessageService.sendMessage(msg);
        }
    }
}
