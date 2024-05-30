package art.com.testtgbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@Data
@PropertySource("application.yaml")
@EnableScheduling
public class BotConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.key}")
    String botToken;
    @Value("${bot.owner}")
    Long botOwnerID;
}
