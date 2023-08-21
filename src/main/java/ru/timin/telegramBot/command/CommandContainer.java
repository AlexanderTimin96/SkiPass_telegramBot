package ru.timin.telegramBot.command;

import ru.timin.telegramBot.command.commandImpl.*;
import ru.timin.telegramBot.repository.ClientRepository;
import ru.timin.telegramBot.repository.SkiPassRepository;
import ru.timin.telegramBot.service.SendBotMessageService;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    AddSkiPassCommand addSkiPassCommand;
    DeleteCommand deleteCommand;
    HelpCommand helpCommand;
    SendAllClientsCommand sendAllClientsCommand;
    StartCommand startCommand;
    UnknownCommand unknownCommand;

    Map<String, Command> commands;

    public CommandContainer(SkiPassRepository skiPassRepository, ClientRepository clientRepository,
                            SendBotMessageService sendBotMessageService) {
        this.addSkiPassCommand = new AddSkiPassCommand(sendBotMessageService, skiPassRepository, clientRepository);
        this.deleteCommand = new DeleteCommand(sendBotMessageService, clientRepository);
        this.helpCommand = new HelpCommand(sendBotMessageService);
        this.sendAllClientsCommand = new SendAllClientsCommand(sendBotMessageService, clientRepository);
        this.startCommand = new StartCommand(sendBotMessageService, clientRepository);

        this.unknownCommand = new UnknownCommand(sendBotMessageService);

        commands = new HashMap<>();
        commands.put(addSkiPassCommand.getCommand(), addSkiPassCommand);
        commands.put(deleteCommand.getCommand(), deleteCommand);
        commands.put(helpCommand.getCommand(), helpCommand);
        commands.put(sendAllClientsCommand.getCommand(), sendAllClientsCommand);
        commands.put(startCommand.getCommand(), startCommand);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commands.getOrDefault(commandIdentifier, unknownCommand);
    }
}
