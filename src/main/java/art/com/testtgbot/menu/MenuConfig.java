package art.com.testtgbot.menu;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Setter
@Getter
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "menu")
public class MenuConfig {
    private HashMap<String, String> commands;

}
