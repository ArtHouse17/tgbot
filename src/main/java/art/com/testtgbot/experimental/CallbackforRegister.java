package art.com.testtgbot.experimental;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class CallbackforRegister {
    private void executeEditMessageText(String text, long chatID, long messageID){
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(chatID));
        messageText.setText(text);
        messageText.setMessageId((int) (messageID));
    }
}
