package art.com.testtgbot.config;

import art.com.testtgbot.experimental.SenADS;
import art.com.testtgbot.model.User;
import art.com.testtgbot.model.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@Data
@PropertySource("application.yaml")
@EnableScheduling
@EnableJpaRepositories(basePackages = "art.com.testtgbot.model")
@ComponentScan(basePackages = "art.com.testtgbot.model")
public class BotConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.key}")
    String botToken;
    @Value("${bot.owner}")
    Long botOwnerID;

}
