package art.com.testtgbot.service;


import art.com.testtgbot.botcore.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class MessageServiceImp implements MessageSenderinterface{

    private final TelegramBot telegramBot;


    public MessageServiceImp(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);

        try{
            telegramBot.execute(sendMessage);
        }catch (TelegramApiException e){
            log.error("Error: " + e.getMessage());
        }
    }
}
