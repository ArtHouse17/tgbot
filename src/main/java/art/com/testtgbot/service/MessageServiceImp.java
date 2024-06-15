package art.com.testtgbot.service;


import art.com.testtgbot.botcore.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Реализация интерфейса по отправке команд, представляет из себя код использующийся для отправки того или
 * иного сообщения команды
 */
@Slf4j
@Service
public class MessageServiceImp implements MessageSenderinterface{

    private final TelegramBot telegramBot;

    /**
     * Конструктор, принимает параметры телеграм бота для метода sendMessage
     * @param telegramBot - принимает объект телеграм бота для использования метода execute
     */
    public MessageServiceImp(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Отправка сообщений пользователю, полученным сообщением от внешних классов
     * @param chatId - ID чата пользователя
     * @param message - сообщение присвоенное внешним классом
     */
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
