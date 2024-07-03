package art.com.testtgbot.botcore;

import art.com.testtgbot.botcommands.CommandContainer;
import art.com.testtgbot.experimental.RegistrationforUser;
import art.com.testtgbot.menu.CommandDisplay;
import art.com.testtgbot.model.AdsRepisitory;
import art.com.testtgbot.model.User;
import art.com.testtgbot.model.UserRepository;
import art.com.testtgbot.service.MessageServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdsRepisitory adsRepisitory;

    @Value("${bot.name}")
    private String username;

    @Value("${bot.key}")
    private String token;

    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";
    private static CommandContainer commandContainer;
    private static RegistrationforUser registrationforUser;
    public TelegramBot() {
        commandContainer = new CommandContainer(new MessageServiceImp(this));
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        CommandDisplay commandDisplay = new CommandDisplay();
        List<BotCommand> botCommands = commandDisplay.display();
        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error by" + e);
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            if (messageText.startsWith(COMMAND_PREFIX)) {
                String commandID = messageText.split(" ")[0].toLowerCase(Locale.ROOT);
                commandContainer.retrieveCommand(commandID, update.getMessage()).execute(update);
            }
        }
        if (update.hasCallbackQuery())
        {
            String callbackData = update.getCallbackQuery().getData();
            long messageID = update.getCallbackQuery().getMessage().getMessageId();
            long chatID = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(YES_BUTTON)) {
                String text = "Вы начали регистрацию!";
                executeEditMessageText(text, chatID, messageID);
                registerUser(update.getMessage(), chatID);
            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "Вы отказались от регистрации";
                executeEditMessageText(text, chatID, messageID);
            }
        }
    }


    private void executeEditMessageText(String text, long chatID, long messageID){
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(chatID));
        messageText.setText(text);
        messageText.setMessageId((int) (messageID));
        try{
            execute(messageText);
        }catch (TelegramApiException e){
            log.error("Error: " + e.getMessage());
        }
    }
    private void registerUser(Message message, long chatID) {
        if(userRepository.findById(chatID).isEmpty()){
            //var chatId = message.getChatId();
            var chat = message.getChat();

            User user = new User();

            user.setChatID(chatID);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
        }
    }
}
/*
    private void showRows(Long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Do you whant to reg");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yes_button = new InlineKeyboardButton();
        yes_button.setText("Yes");
        yes_button.setCallbackData(YES_BUTTON);

        var no_button = new InlineKeyboardButton();
        no_button.setText("No");
        no_button.setCallbackData(NO_BUTTON);

        rowInLine.add(yes_button);
        rowInLine.add(no_button);

        rowsLine.add(rowInLine);

        markup.setKeyboard(rowsLine);
        message.setReplyMarkup(markup);

        executeMessage(message);
    }

}
*/