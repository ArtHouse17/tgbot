package art.com.testtgbot.menu;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Getter
@Setter
public class MenuClass {

    public Map<String, String> readYaml() {
        Yaml yaml = new Yaml();
        try (InputStream in = getClass().getResourceAsStream("/menu.yaml")) {
            return yaml.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить YAML", e);
        }
    }
}
