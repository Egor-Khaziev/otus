package ru.khaziev.TelegramBotModule.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.khaziev.TelegramBotModule.DTO.ClientDTO;
import ru.khaziev.TelegramBotModule.service.ClientService;


import java.io.IOException;
import java.lang.ref.SoftReference;

@Component
public class Cache extends AbstractCache<Long, ClientDTO> {

    @Autowired
    ClientService clientService;

    @Override
    public ClientDTO load(Long key)  {

        ClientDTO clientDTO;

        try {
            clientDTO = clientService.getClientByTelegramChat(key.toString());
            if (clientDTO != null) {
                put(key, clientDTO);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return clientDTO;
    }

    public boolean containsKey(Long key){
        return cache.containsKey(key);
    }

    public ClientDTO replace(Long key, ClientDTO value){
        return cache.replace(key,new SoftReference<>(value)).get();
    }

    public int size(){
        return cache.size();
    }


}
