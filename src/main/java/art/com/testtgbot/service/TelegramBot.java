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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.xml.stream.events.Comment;
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
        };
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
            long chatId = update.getMessage().getChatId();
            switch (update.getMessage().getText()) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommandRecievd(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Sry cmd not recognized");
                    break;
            }
        }
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

        sendMessage(chatId, answ);
    }

    private void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);

        try{
            execute(sendMessage);
        }catch (TelegramApiException e){
            log.error("Error: " + e.getMessage());
        }
    }
}
