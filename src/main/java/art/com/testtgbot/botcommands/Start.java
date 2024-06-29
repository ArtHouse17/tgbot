package art.com.testtgbot.botcommands;

import art.com.testtgbot.model.UserRepository;
import art.com.testtgbot.service.MessageServiceImp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;



@Slf4j
@Service
public class Start implements CommandInterface {
    private MessageServiceImp messageServiceImp;
    public final static String testMessage = "starttext";

    @Autowired
    private UserRepository userRepository;

    public Start(MessageServiceImp messageServiceImp) {
        this.messageServiceImp = messageServiceImp;
    }

    @Override
    public void execute(Update update) {
        messageServiceImp.sendMessage(update.getMessage().getChatId().toString(),testMessage);
    }
}
