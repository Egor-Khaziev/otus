package ru.khaziev.TelegramBotModule.logic;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class Command {

    protected SendMessage handleCommand(Message message) {
        String text = message.getText();
        String[] parts = text.split(" ");
        String command = parts[0];
        SendMessage sendMessage;

        switch (command) {
            case "/start":
                sendMessage = startCommand(message);
                break;
//            case "/sharecontact":
//                sendMessage = authentication(message);
//                break;
            default:
                sendMessage = sendUnknownCommandMessage(message.getChatId().toString());
        }

        return sendMessage;
    }

//    private SendMessage authentication(Message message) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(String.valueOf(message.getChatId()));
//        sendMessage.setText("Please share your contact information:");
//
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        List<KeyboardRow> keyboard = new ArrayList<>();
//        KeyboardRow row = new KeyboardRow();
//        KeyboardButton shareContactButton = new KeyboardButton();
//        shareContactButton.setText("Share Contact");
//        shareContactButton.setRequestContact(true);
//        row.add(shareContactButton);
//        keyboard.add(row);
//        keyboardMarkup.setKeyboard(keyboard);
//        sendMessage.setReplyMarkup(keyboardMarkup);
//
//        return sendMessage;
//
//    }

    private SendMessage startCommand(Message message) {
        SendMessage messagerequest = SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text("Здравствуйте, " + message.getFrom().getFirstName() + "! Добро пожаловать в Фитнес бот. \nЗдесь вы сможете посмотреть свое расписание занятий. \nИ любую другую интересующую вас информацию.")
                .build();

        return messagerequest;
    }

    private SendMessage sendUnknownCommandMessage(String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Извините, неизвестная команда.")
                .build();

        return message;
    }
}