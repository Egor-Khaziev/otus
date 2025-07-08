package ru.khaziev.TelegramBotModule.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.khaziev.TelegramBotModule.logic.Bot;

import java.io.IOException;

@Component
public class DailyTaskScheduler {

    @Autowired
    private Bot bot;

    // Рассылка напоминание в боте о завтрашних занятиях
    // каждый день в 20:00
    @Scheduled(cron = "0 0 20 * * *")
    public void dailySessionsReminder() throws IOException {
        bot.reminderTomorrow();
    }
}
