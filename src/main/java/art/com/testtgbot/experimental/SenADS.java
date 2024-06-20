package art.com.testtgbot.experimental;


import art.com.testtgbot.model.Ads;
import art.com.testtgbot.model.AdsRepisitory;
import art.com.testtgbot.model.User;
import art.com.testtgbot.model.UserRepository;
import art.com.testtgbot.service.MessageServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SenADS{
    @Autowired
    private AdsRepisitory adsRepisitory;
    @Autowired
    private UserRepository userRepository;

    private final MessageServiceImp messageServiceImp;

    public SenADS(MessageServiceImp messageServiceImp) {
        this.messageServiceImp = messageServiceImp;
    }

    @Scheduled(cron = "${cron.scheduler}")
    public void execAdv() {
        var ads = adsRepisitory.findAll();
        var users = userRepository.findAll();

        for(Ads ad: ads){
            for (User user:users)
                messageServiceImp.sendMessage(String.valueOf(user.getChatID()), ad.getAd());
        }
    }
}
