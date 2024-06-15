package art.com.testtgbot.botcommands;

import art.com.testtgbot.model.User;
import art.com.testtgbot.model.UserRepository;
import art.com.testtgbot.service.MessageServiceImp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;


@Slf4j
@Service
public class Start implements CommandInterface {
    private final MessageServiceImp messageServiceImp;
    public final static String testMessage = "Test123";

    @Autowired
    private UserRepository userRepository;

    public Start(MessageServiceImp messageServiceImp) {
        this.messageServiceImp = messageServiceImp;
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

    @Override
    public void execute(Update update) {
        registerUser(update.getMessage());
        messageServiceImp.sendMessage(update.getMessage().getChatId().toString(),testMessage);
    }
}
