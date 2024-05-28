package art.com.testtgbot.botcommands;

import art.com.testtgbot.service.MessageServiceImp;
import org.telegram.telegrambots.meta.api.objects.Update;

public class help implements CommandInterface {

    private final MessageServiceImp messageServiceImp;
    public final static String HELP_TEXT = "123";

    public help(MessageServiceImp messageServiceImp) {
        this.messageServiceImp = messageServiceImp;
    }


    @Override
    public void execute(Update update) {
        messageServiceImp.sendMessage(update.getMessage().getChatId().toString(), HELP_TEXT);
    }
}
