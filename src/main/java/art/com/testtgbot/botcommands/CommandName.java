package art.com.testtgbot.botcommands;

public enum CommandName {
    START("/start"),
    HELP("/help"),
    TEST("/start");
    private final String commandName;


    CommandName(String commandName) {
        this.commandName = commandName;
    }


    public String getCommandName() {
        return commandName;
    }
}
