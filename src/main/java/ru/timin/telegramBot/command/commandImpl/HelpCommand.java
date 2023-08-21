package ru.timin.telegramBot.command.commandImpl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.timin.telegramBot.command.Command;
import ru.timin.telegramBot.command.CommandName;
import ru.timin.telegramBot.messageBuilder.MessageBuilder;
import ru.timin.telegramBot.service.SendBotMessageService;

public class HelpCommand implements Command {
    private final MessageBuilder messageBuilder;
    private final SendBotMessageService sendBotMessageService;

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
        this.messageBuilder = new MessageBuilder();
    }

    @Override
    public void execute(Update update) {
        String response = "Управление осуществляется кнопками на экране диалога после запуска бота, через команду /start";
        SendMessage msg = messageBuilder.getSimpleMessage(update.getMessage().getChatId(), response);
        sendBotMessageService.sendMessage(msg);
    }

    @Override
    public String getCommand() {
        return CommandName.HELP.getCommandName();
    }
}
