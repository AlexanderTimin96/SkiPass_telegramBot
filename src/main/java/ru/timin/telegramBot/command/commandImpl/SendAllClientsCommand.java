package ru.timin.telegramBot.command.commandImpl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.timin.telegramBot.command.Command;
import ru.timin.telegramBot.command.CommandName;
import ru.timin.telegramBot.entity.Client;
import ru.timin.telegramBot.messageBuilder.MessageBuilder;
import ru.timin.telegramBot.repository.ClientRepository;
import ru.timin.telegramBot.service.SendBotMessageService;

import java.util.List;

public class SendAllClientsCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final ClientRepository clientRepository;

    private final MessageBuilder messageBuilder;

    public SendAllClientsCommand(SendBotMessageService sendBotMessageService, ClientRepository clientRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.clientRepository = clientRepository;
        this.messageBuilder = new MessageBuilder();
    }

    @Override
    public void execute(Update update) {
        String msgText = update.getMessage().getText();
        String response = msgText.substring(msgText.indexOf(" "));
        List<Client> listAllClients = clientRepository.findAll();

        for (Client client : listAllClients) {
            SendMessage msg = messageBuilder.getSimpleMessage(client.getChatId(), response);
            sendBotMessageService.sendMessage(msg);
        }
    }

    @Override
    public String getCommand() {
        return CommandName.SEND_ALL_USER.getCommandName();
    }
}
