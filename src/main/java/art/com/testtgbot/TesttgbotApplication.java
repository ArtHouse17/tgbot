package art.com.testtgbot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TesttgbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesttgbotApplication.class, args);
    }

}
