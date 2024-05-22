package art.com.testtgbot.service;

import art.com.testtgbot.config.BotConfig;
import art.com.testtgbot.model.User;
import art.com.testtgbot.model.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;

    final BotConfig config;
    static final String HELP_TEXT = " egea";
    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";
    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "Начало пользования ботом"));
        botCommandList.add(new BotCommand("/mydata", "Получить информацию о пользователе"));
        botCommandList.add(new BotCommand("/mydatadelete", "Удаление персональных данных"));
        botCommandList.add(new BotCommand("/help", "Описание команд для бота"));
        botCommandList.add(new BotCommand("/settings", "Изменение настроек бота"));
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Err sett");
        }
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if(messageText.contains("/send") && config.getBotOwnerID() == chatId){
                var textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
                var users = userRepository.findAll();
                for (User user : users){
                    prepareAndSendMessage(user.getChatID(), textToSend);
                }
            }else{
                switch (update.getMessage().getText()) {
                    case "/start":
                        registerUser(update.getMessage());
                        startCommandRecievd(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/help":
                        prepareAndSendMessage(chatId, HELP_TEXT);
                        break;
                    case "/register":

                        register(chatId);
                        break;
                    case "/send":
                        break;
                    default:
                        prepareAndSendMessage(chatId, "Sry cmd not recognized");
                        break;
                }
            }
        } else  if (update.hasCallbackQuery()){
            String callbackData = update.getCallbackQuery().getData();
            long messageID = update.getCallbackQuery().getMessage().getMessageId();
            long chatID = update.getCallbackQuery().getMessage().getChatId();

            if(callbackData.equals(YES_BUTTON)){
                String text = "You pressed yes";
                executeEditMessageText(text, chatID, messageID);
            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "You pressed No";
                executeEditMessageText(text, chatID, messageID);
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

    private void startCommandRecievd(long chatId, String firstName){
        // String answ = "Hello there " + firstName + " you look great";
        String answ = EmojiParser.parseToUnicode("Hello there " + firstName + " you look great" + ":blush:");
        log.info("reply: " + answ);

        openMenu(chatId, answ);
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
}
