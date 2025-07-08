package ru.khaziev.TelegramBotModule.logic;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.khaziev.TelegramBotModule.DTO.ClientDTO;
import ru.khaziev.TelegramBotModule.DTO.EmployeeDTO;
import ru.khaziev.TelegramBotModule.DTO.ScheduleDTO;
import ru.khaziev.TelegramBotModule.DTO.TrainingTypeDTO;
import ru.khaziev.TelegramBotModule.config.BotConfig;
import ru.khaziev.TelegramBotModule.service.*;
import ru.khaziev.TelegramBotModule.util.Cache;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private TrainingTypeService trainingTypeService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private EmployeeService employeeService;

    //Ограничение частоты запросов — не более 10 запросов в секунду на контакт.
    //При количестве запросов более 10 rps, сервер ответит статусом — код 429 Too Many Requests
    private final Semaphore semaphore = new Semaphore(10);

    private final Cache chatIds;
    private final BotConfig config;
    private final Command botCommand;
    DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("dd.MMMM HH:mm");
    DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

    public Bot() {
        this.config = new BotConfig();
        this.botCommand = new Command();
        this.chatIds = new Cache();
    }

    @PostConstruct
    public void init() {
        try {
            //get 3 sec for client modules initialization
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ClientDTO[] clientDTOList = clientService.getAllClients();
            if (clientDTOList != null && clientDTOList.length > 0) {
                Arrays.stream(clientDTOList).toList().forEach(c ->
                        {
                            chatIds.put(c.getId(), c);
                        }
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("chatIds size: " + chatIds.size());
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        //if button callback
        if (update.hasCallbackQuery() && chatIds.containsKey(update.getCallbackQuery().getMessage().getChatId())){
            if (update.getCallbackQuery().getData().equals("schedule_command")){
                try {
                    long chatid = update.getCallbackQuery().getMessage().getChatId();
                    ClientDTO clientDTO = chatIds.get(chatid);
                    long clid =  clientDTO.getId();


                    List<ScheduleDTO> scheduleDTOList = scheduleService.getScheduleByUserId(clid);

                    String messageText = scheduleListToText(scheduleDTOList);

                    SendMessage response = createMainMenu(update.getCallbackQuery().getMessage().getChatId());
                    response.setText(messageText);
                    try {
                        sendTextMessage(response);
                    } catch (InterruptedException e) {
                        log.error("sendTextMessage error", e);
                        throw new RuntimeException(e);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (update.getCallbackQuery().getData().equals("wallet_command")){
                try {
                    long chatid = update.getCallbackQuery().getMessage().getChatId();
                    ClientDTO clientDTO = chatIds.get(chatid);
                    long clid =  clientDTO.getId();

                    clientDTO = clientService.getClient(clid);
                    chatIds.replace(chatid, clientDTO);

                    String messageText = "Баланс: " + clientDTO.getWallet().toString();

                    SendMessage response = createMainMenu(update.getCallbackQuery().getMessage().getChatId());
                    response.setText(messageText);
                    try {
                        sendTextMessage(response);
                    } catch (InterruptedException e) {
                        log.error("sendTextMessage error", e);
                        throw new RuntimeException(e);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                SendMessage response;

                //authentication if message has contact
                if (message.hasContact()) {
                    try {
                        response = saveContact(message);
                        try {
                            sendTextMessage(response);
                        } catch (InterruptedException e) {
                            log.error("sendTextMessage error", e);
                            throw new RuntimeException(e);
                        }

                        if(chatIds.containsKey(message.getChatId())) {
                            //menu
                            response = createMainMenu(message.getChatId());
                        } else {
                            String responseText =  "К сожалению вашего номера нет в нашей базе :( ";;
                            response.setText(responseText);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //sending
                    try {
                        sendTextMessage(response);
                    } catch (InterruptedException e) {
                        log.error("sendTextMessage error", e);
                        throw new RuntimeException(e);
                    }
                }


                //unauthenticated user
                if (!chatIds.containsKey(update.getMessage().getChatId())) {
                    ClientDTO client;
                    // check user in Client-model
                    try {
                        System.out.println("New user checking, chat id: " + update.getMessage().getChatId());
                        client = clientService.getClientByTelegramUser(message.getFrom().getUserName());
                        if (client != null) {
                            //if user is found
                            //add to Map
                            chatIds.put(message.getChatId(), client);
                            // standard response
                            response = createMainMenu(update.getMessage().getChatId());
                            try {
                                sendTextMessage(response);
                            } catch (InterruptedException e) {
                                log.error("sendTextMessage error", e);
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // unknown user
                    if (!chatIds.containsKey(update.getMessage().getChatId())) {
                        System.out.println("Unauthenticated user detected chat id: " + update.getMessage().getChatId() + ", telegram id: " + message.getFrom().getUserName());
                        response = requestAuthentication(message);
                        //sending
                        try {
                            sendTextMessage(response);
                        } catch (InterruptedException e) {
                            log.error("sendTextMessage error", e);
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    //message has text
                    if (message.hasText()) {
                        String messageText = message.getText();

                        // text message start with "/"
                        if (messageText.startsWith("/")) {
                            response = botCommand.handleCommand(message);

                            // Handler for regular messages
                        } else {
                            response = createMainMenu(update.getMessage().getChatId());
                        }
                        //sending
                        System.out.println(message.getText());
                        try {
                            sendTextMessage(response);
                        } catch (InterruptedException e) {
                            log.error("sendTextMessage",e);
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    private String scheduleListToText(List<ScheduleDTO> scheduleDTOList) {
        if (scheduleDTOList == null || scheduleDTOList.isEmpty()) {
            return "Нет запланированных занятий.";
        }



        return scheduleDTOList.stream()
                .map(scheduleDTO -> {
                    String startDateTime = scheduleDTO.getDateTimeStart().format(formatterDateTime);
                    String endDateTime = scheduleDTO.getDateTimeEnd().format(formatterTime);
                    String trainingType;
                    String room;
                    String trainerName;
                    try {
                        trainingType = trainingTypeService.getTrainingTypeById(scheduleDTO.getTrainingTypeId().longValue()).getTrainingTypeName();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        room = roomService.getRoomById(scheduleDTO.getRoomId().longValue()).getRoomName();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        EmployeeDTO trainer = employeeService.getEmployee(scheduleDTO.getNumberTr());
                        trainerName = trainer.NamePlusPatronymic();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return String.format("%s - %s \n%s, Зал: %s\nТренер: %s\n",
                            startDateTime, endDateTime, trainingType, room, trainerName);
                })
                .collect(Collectors.joining("\n"));
    }

    private SendMessage saveContact(Message message) throws IOException {
        SendMessage response = new SendMessage();
        String responseText = "Спасибо за доступ к вашей контактной информации";
        response.setChatId(String.valueOf(message.getChatId()));

        ClientDTO client = clientService.getClientByPhone(message.getContact().getPhoneNumber());
        if (client != null) {
            //add client to Map
            responseText = responseText + "\nДобро пожаловать в личный кабинет " + client.getFirstName();
            client.setTelegramUser(message.getFrom().getUserName());
            client.setTelegramChat(message.getChatId().toString());
            clientService.updateClient(client.getId(),client);
            System.out.println("User " + client.getFirstName() + " " + client.getLastName() + " updated. Set telegram id to " + client.getTelegramUser());

            //add client to map
            chatIds.put(message.getChatId(), client);
            System.out.println("authenticating complete chat id: " +  message.getChatId() + " пользователь " + client.getFirstName() + " " + client.getLastName());

            ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
            keyboardMarkup.setSelective(true);
            keyboardMarkup.setRemoveKeyboard(true);
            response.setReplyMarkup(keyboardMarkup);

        } else {
            System.err.println("authenticating ERROR chat id: " +  message.getChatId() + " пользователь " + message.getChat().getFirstName() + " " + message.getChat().getLastName());
        }

        response.setText(responseText);

        return response;
    }

    private SendMessage requestAuthentication(Message message) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText("Я не узнаю вас в гриме. Пожалуйста представьтесь.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton shareContactButton = new KeyboardButton();
        shareContactButton.setText("Поделиться своим контактом");
        shareContactButton.setRequestContact(true);
        row.add(shareContactButton);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }

    public static SendMessage createMainMenu(long chatId) {
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));
        message.setText("Доступные опции:");  // Menu text

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // Schedule and Wallets
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton scheduleButton = new InlineKeyboardButton();
        scheduleButton.setText("Расписание");
        scheduleButton.setCallbackData("schedule_command");  // Unique callback data

        InlineKeyboardButton walletsButton = new InlineKeyboardButton();
        walletsButton.setText("Баланс");
        walletsButton.setCallbackData("wallet_command");    // Unique callback data

        rowInline1.add(scheduleButton);
        rowInline1.add(walletsButton);
        rowsInline.add(rowInline1);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);


        return message;
    }

    public void reminderTomorrow() throws IOException{

        List<ScheduleDTO> scheduleDTOList =  scheduleService.getTomorrowSchedules();
        for (ScheduleDTO scheduleDTO : scheduleDTOList) {
            for (Long id : scheduleDTO.getClListIds()) {
                ClientDTO clientDTO = clientService.getClient(id);
                if (!clientDTO.getTelegramChat().isEmpty()) {
                    TrainingTypeDTO trainingTypeDTO = trainingTypeService.getTrainingTypeById(scheduleDTO.getTrainingTypeId());

                    String startDateTime = scheduleDTO.getDateTimeStart().format(formatterDateTime);
                    String endDateTime = scheduleDTO.getDateTimeEnd().format(formatterTime);
                    String trainingType = trainingTypeDTO.getTrainingTypeName();

                    String text = String.format("Напоминаю, у Вас завтра тренировка %s - %s \n%s.",
                            startDateTime, endDateTime, trainingType);


                    SendMessage message = new SendMessage();
                    message.setChatId(Long.valueOf(clientDTO.getTelegramChat()));
                    message.setText(text);
                    try {
                        sendTextMessage(message);
                    } catch (InterruptedException e) {
                        log.error("sendTextMessage error", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    //Ограничение частоты запросов — не более 10 запросов в секунду на контакт.
    //При количестве запросов более 10 rps, сервер ответит статусом — код 429 Too Many Requests
    private void sendTextMessage(SendMessage message) throws InterruptedException {
        semaphore.acquire();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}