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
        callbackContainer = new CallbackContainer(clientRepository, sendBotMessageService);

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
}