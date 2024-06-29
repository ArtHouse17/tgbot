package art.com.testtgbot.botcommands;

import art.com.testtgbot.model.User;
import art.com.testtgbot.model.UserRepository;
import art.com.testtgbot.service.MessageServiceImp;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;
import java.util.Objects;

import static art.com.testtgbot.botcommands.CommandName.*;
@Component
public class CommandContainer {

    public final ImmutableMap<String, CommandInterface> commandInterfaceImmutableMap;
    public final CommandInterface testCommand;
    public CommandContainer(MessageServiceImp messageServiceImp){
        commandInterfaceImmutableMap = ImmutableMap.<String, CommandInterface>builder()
                .put(HELP.getCommandName(),new help(messageServiceImp))
                .put(START.getCommandName(), new Start(messageServiceImp))
                .build();
        testCommand = new Test(messageServiceImp);
    }


    public CommandInterface retrieveCommand(String commandIdentifier, Message message) {

        return commandInterfaceImmutableMap.getOrDefault(commandIdentifier, testCommand);
    }
}
