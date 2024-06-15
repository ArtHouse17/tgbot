package art.com.testtgbot.botcommands;

import art.com.testtgbot.service.MessageServiceImp;
import com.google.common.collect.ImmutableMap;

import static art.com.testtgbot.botcommands.CommandName.*;
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

    public CommandInterface retrieveCommand(String commandIdentifier) {
        return commandInterfaceImmutableMap.getOrDefault(commandIdentifier, testCommand);
    }
}
