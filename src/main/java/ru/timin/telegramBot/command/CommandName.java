package ru.timin.telegramBot.command;

public enum CommandName {

    START("/start"),
    HELP("/help"),
    DELETE("/deletemydata"),
    SKIPASS("/skipass"),
    SEND_ALL_USER("/sendallclients"),
    UNKOWN("unkownCommand");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}
