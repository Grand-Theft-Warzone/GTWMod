package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumers;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;


public class ProcessConsumer {

    public ProcessConsumer() {
        request();
    }


    public void request() {
        GtwAPI.getInstance().getMapManagerServer().getMapConsumers().setSRequest(
                (it) ->{
                    System.out.println("МЫ ПОЛУЧИЛИ ОТ КЛИЕНТА ДАННЫЕ:");
                    System.out.println("UUID: " + it.getUuid());
                    System.out.println("Event: " + it.getConfig().getString("event"));
                }
        );
    }


}
