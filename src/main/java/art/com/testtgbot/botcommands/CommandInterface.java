package art.com.testtgbot.botcommands;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandInterface {
    void execute (Update update);
}
