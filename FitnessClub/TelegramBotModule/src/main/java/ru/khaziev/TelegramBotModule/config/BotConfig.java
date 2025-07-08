package ru.khaziev.TelegramBotModule.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.yaml.snakeyaml.Yaml;
import ru.khaziev.TelegramBotModule.logic.Bot;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

@Configuration
@EnableAspectJAutoProxy
public class BotConfig {

    private static final String CONFIG_FILE = "application.yml";

    private String botToken;
    private String botName;


    @Bean
    public TelegramBotsApi telegramBotsApi(Bot bot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        return botsApi;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configOverride(LocalDate.class)
                .setFormat(com.fasterxml.jackson.annotation.JsonFormat.Value.forPattern("yyyy-MM-dd"));
        return objectMapper;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }

    public BotConfig() {
        loadConfig();
    }

    private void loadConfig() {
        Yaml yaml = new Yaml();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                return;
            }
            Map<String, Object> config = yaml.load(input);
            if (config != null && config.containsKey("bot")) {
                Map<String, Object> botConfig = (Map<String, Object>) config.get("bot");

                if (botConfig.containsKey("token")) {
                    this.botToken = (String) botConfig.get("token");
                }
                if (botConfig.containsKey("name")) {
                    this.botName = (String) botConfig.get("name");
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}