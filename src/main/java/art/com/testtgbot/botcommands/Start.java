package art.com.testtgbot.botcommands;

import art.com.testtgbot.service.MessageServiceImp;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Start implements CommandInterface {
    private final MessageServiceImp messageServiceImp;
    public final static String testMessage = "Test123";

    public Start(MessageServiceImp messageServiceImp) {
        this.messageServiceImp = messageServiceImp;
    }

    @Override
    public void execute(Update update) {
        messageServiceImp.sendMessage(update.getMessage().getChatId().toString(),testMessage);
    }
}
