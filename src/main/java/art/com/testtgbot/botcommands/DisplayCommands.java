package art.com.testtgbot.botcommands;


import art.com.testtgbot.botcore.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;


import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DisplayCommands{
    private final TelegramBot telegramBot;

    public DisplayCommands(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void execute(Update update) {
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("/help","Вывести информацию о боте"));
        botCommands.add(new BotCommand("/start","Вывести информацию о боте"));
        try {
            telegramBot.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        }catch (Exception e){
            log.error("Error on command display");
        }
    }
}
