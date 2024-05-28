package art.com.testtgbot.service;

import org.jvnet.hk2.annotations.Service;

@Service
public interface MessageSenderinterface {
    void sendMessage(String chatId, String message);
}
