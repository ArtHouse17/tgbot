package art.com.testtgbot.menu;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.*;

/**
 * Отображение меню и команд, позволяет добавить новые команды для отображения.
 *
 */

public class CommandDisplay {

    MenuClass mc = new MenuClass();

    public List<BotCommand> display(){

        var map = mc.readYaml();
        List<BotCommand> botCommandList = new ArrayList<>();
        for (Map.Entry<String,String> entry : map.entrySet()){
            botCommandList.add(new BotCommand("/"+ entry.getKey(), entry.getValue()));
        }
        return botCommandList;
    }

}
