package art.com.testtgbot.service;

import art.com.testtgbot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
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
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (update.getMessage().getText()) {
                case "/start":
                    startCommandRecievd(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(chatId, "Sry cmd not recognized");
                    break;
            }
        }
    }
    private void startCommandRecievd(long chatId, String firstName){
        String answ = "Hello there " + firstName + " you look great";
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
