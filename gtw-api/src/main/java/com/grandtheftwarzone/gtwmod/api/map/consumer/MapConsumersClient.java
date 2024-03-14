package com.grandtheftwarzone.gtwmod.api.map.consumer;

import com.grandtheftwarzone.gtwmod.api.map.data.CStartData;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
public class MapConsumersClient {

    @Setter
    private Consumer<CStartData> cStartData;
}
