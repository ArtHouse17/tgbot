package art.com.testtgbot.experimental;

import art.com.testtgbot.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class RegistrationforUser {

    @Autowired
    private UserRepository userRepository;

    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";

    public void showRows(SendMessage message){

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yes_button = new InlineKeyboardButton();
        yes_button.setText("Yes");
        yes_button.setCallbackData(YES_BUTTON);

        var no_button = new InlineKeyboardButton();
        no_button.setText("No");
        no_button.setCallbackData(NO_BUTTON);

        rowInLine.add(yes_button);
        rowInLine.add(no_button);

        rowsLine.add(rowInLine);

        markup.setKeyboard(rowsLine);
        message.setReplyMarkup(markup);

    }


}
