package art.com.testtgbot.experimental;

import art.com.testtgbot.botcommands.CommandInterface;
import art.com.testtgbot.model.Ads;
import art.com.testtgbot.model.AdsRepisitory;
import art.com.testtgbot.model.User;
import art.com.testtgbot.model.UserRepository;
import art.com.testtgbot.service.MessageServiceImp;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SenADS implements CommandInterface {
    private AdsRepisitory adsRepisitory;
    private UserRepository userRepository;
    private final MessageServiceImp messageServiceImp;

    public SenADS(MessageServiceImp messageServiceImp) {
        this.messageServiceImp = messageServiceImp;
    }

    @Scheduled(cron = "${cron.scheduler}")
    @Override
    public void execute(Update update) {
        var ads = adsRepisitory.findAll();
        var users = userRepository.findAll();

        for(Ads ad: ads){
            for (User user:users)
                messageServiceImp.sendMessage(String.valueOf(user.getChatID()), ad.getAd());
        }
    }
}
