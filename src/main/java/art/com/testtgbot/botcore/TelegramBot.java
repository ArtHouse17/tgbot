package art.com.testtgbot.botcore;

import art.com.testtgbot.botcommands.CommandContainer;
import art.com.testtgbot.botcommands.CommandDisplay;
import art.com.testtgbot.botcommands.CommandInterface;
import art.com.testtgbot.model.Ads;
import art.com.testtgbot.model.AdsRepisitory;
import art.com.testtgbot.model.User;
import art.com.testtgbot.model.UserRepository;
import art.com.testtgbot.service.MessageServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX ="/";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdsRepisitory adsRepisitory;

    private CommandInterface commandInterface;
    @Value("${bot.name}")
    private String username;

    @Value("${bot.key}")
    private String token;

    static final String HELP_TEXT = " egea";
    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";
    private static CommandContainer commandContainer;
    private static CommandDisplay commandDisplay;
    public TelegramBot(){
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
        commandDisplay = new CommandDisplay();
        List<BotCommand> botCommands = commandDisplay.display();
        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Err sett");
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.startsWith(COMMAND_PREFIX)){
                String commandID = messageText.split(" ")[0].toLowerCase(Locale.ROOT);

                commandContainer.retrieveCommand(commandID).execute(update);
            }
        }
    }

    private void register(Long chatId){
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
    private void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()){
            var chatId = message.getChatId();
            var chat = message.getChat();

            User user = new User();

            user.setChatID(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved" + user);
        }
    }

    private void executeEditMessageText(String text, long chatID, long messageID){
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(chatID));
        messageText.setText(text);
        messageText.setMessageId((int) (messageID));

        executeMessage(messageText);
    }
    private void openMenu(long chatId, String message) {
        SendMessage messages = new SendMessage();
        messages.setChatId(String.valueOf(chatId));
        messages.setText(message);
        prepareAndSendMessage(chatId, message);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("weather");
        row.add("get random joke");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("register");
        row.add("check my data");
        row.add("delete my data");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);


        messages.setReplyMarkup(keyboardMarkup);

    }
    private void executeMessage(SendMessage messageText){
        try{
            execute(messageText);
        }catch (TelegramApiException e){
            log.error("Error: " + e.getMessage());
        }
    }
    private void executeMessage(EditMessageText messageText){
        try{
            execute(messageText);
        }catch (TelegramApiException e){
            log.error("Error: " + e.getMessage());
        }
    }
    private void prepareAndSendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);

    }

    @Scheduled(cron = "${cron.scheduler}")
    private void SendAds(){
        var ads = adsRepisitory.findAll();
        var users = userRepository.findAll();

        for(Ads ad: ads){
            for (User user:users)
                prepareAndSendMessage(user.getChatID(), ad.getAd());
        }
    }
}
