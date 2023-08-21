package ru.timin.telegramBot.command.commandImpl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.timin.telegramBot.command.Command;
import ru.timin.telegramBot.command.CommandName;
import ru.timin.telegramBot.entity.Client;
import ru.timin.telegramBot.entity.SkiPass;
import ru.timin.telegramBot.callbackAction.CallbackName;
import ru.timin.telegramBot.keyboard.InlineKeyboard;
import ru.timin.telegramBot.messageBuilder.MessageBuilder;
import ru.timin.telegramBot.repository.ClientRepository;
import ru.timin.telegramBot.repository.SkiPassRepository;
import ru.timin.telegramBot.service.SendBotMessageService;


public class AddSkiPassCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final SkiPassRepository skiPassRepository;
    private final ClientRepository clientRepository;

    private final InlineKeyboard inlineKeyboard;
    private final MessageBuilder messageBuilder;

    public AddSkiPassCommand(SendBotMessageService sendBotMessageService, SkiPassRepository skiPassRepository,
                             ClientRepository clientRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.skiPassRepository = skiPassRepository;
        this.clientRepository = clientRepository;
        this.messageBuilder = new MessageBuilder();
        this.inlineKeyboard = new InlineKeyboard();
    }

    @Override
    public void execute(Update update) {
        String msgText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        String skiPassNumber = msgText.substring(msgText.indexOf(" ") + 1);
        SendMessage msg;
        String response;

        if (skiPassRepository.findBySkiPassNumber(skiPassNumber).isPresent()) {
            SkiPass skiPass = skiPassRepository.findBySkiPassNumber(skiPassNumber).get();
            Client client = clientRepository.findById(chatId).get();
            client.setSkiPass(skiPass);
            clientRepository.save(client);

            response = "Привязанный к твоему номеру ски-пасс: " + skiPass.getSkiPassNumber() + "\n"
                    + "У тебя осталось подъемов: " + skiPass.getLifts() + "\n"
                    + "Если вы хотите добавить подъемы нажмите кнопку \"Добавить подъемы >>\"";
            msg = messageBuilder.getMessageWithInlineKeyboards(chatId,
                    response, inlineKeyboard.getInlineKeyBoardWithRedirect("Добавить подъемы >>",
                            CallbackName.ADD_LIFTS_BUTTON.getcallback(),
                            "https://www.bank.ru/&skipassnumber=" + skiPass.getSkiPassNumber()));
        } else {
            response = "Введены некорректные данные. Попробуй еще раз!\n" +
                    "Отправь мне номер своего ски-пасса (написан на лицевой карты).\n"
                    + "Через команду: \"/skipass ********\"\n - где звездочки это твой номер ски-паса или "
                    + "обратись к администратору горнолыжного курорты";
            msg = messageBuilder.getSimpleMessage(chatId, response);
        }
        sendBotMessageService.sendMessage(msg);
    }

    @Override
    public String getCommand() {
        return CommandName.SKIPASS.getCommandName();
    }
}
