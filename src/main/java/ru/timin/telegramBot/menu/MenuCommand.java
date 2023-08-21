package ru.timin.telegramBot.menu;

import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import ru.timin.telegramBot.command.CommandName;

import java.util.ArrayList;
import java.util.List;

public class MenuCommand {
    List<BotCommand> listOfCommands;

    public MenuCommand() {
        this.listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(CommandName.START.getCommandName(), "Начать работу с ботом"));
        listOfCommands.add(new BotCommand(CommandName.DELETE.getCommandName(), "Удалить мои данные из бота"));
        listOfCommands.add(new BotCommand(CommandName.HELP.getCommandName(), "Помощь по работе с ботом"));
    }

    public SetMyCommands getMyCommands() {
        return new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null);
    }
}
