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

public class DeleteCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final ClientRepository clientRepository;
    private final MessageBuilder messageBuilder;

    public DeleteCommand(SendBotMessageService sendBotMessageService, ClientRepository clientRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.clientRepository = clientRepository;
        this.messageBuilder = new MessageBuilder();
    }


    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
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
        sendBotMessageService.sendMessage(msg);
    }

    @Override
    public String getCommand() {
        return CommandName.DELETE.getCommandName();
    }
}
