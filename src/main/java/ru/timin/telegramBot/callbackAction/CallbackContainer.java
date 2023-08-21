package ru.timin.telegramBot.callbackAction;

import ru.timin.telegramBot.callbackAction.callbackActionImpl.AddSkiPassCallback;
import ru.timin.telegramBot.callbackAction.callbackActionImpl.StartCallback;
import ru.timin.telegramBot.command.Command;
import ru.timin.telegramBot.repository.ClientRepository;
import ru.timin.telegramBot.repository.SkiPassRepository;
import ru.timin.telegramBot.service.SendBotMessageService;

import java.util.HashMap;
import java.util.Map;

public class CallbackContainer {
    private final AddSkiPassCallback addSkiPassCallback;
    private final StartCallback startCallback;

    Map<String, CallbackAction> callbackActions;

    public CallbackContainer(ClientRepository clientRepository,
                             SendBotMessageService sendBotMessageService) {

        addSkiPassCallback = new AddSkiPassCallback(sendBotMessageService);
        startCallback = new StartCallback(sendBotMessageService, clientRepository);


        callbackActions = new HashMap<>();
        callbackActions.put(addSkiPassCallback.getCommand(), addSkiPassCallback);
        callbackActions.put(startCallback.getCommand(), startCallback);

    }

    public CallbackAction retrieveCommand(String commandIdentifier) {
        return callbackActions.get(commandIdentifier);
    }
}
