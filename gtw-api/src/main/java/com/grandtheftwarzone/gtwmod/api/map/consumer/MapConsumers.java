package com.grandtheftwarzone.gtwmod.api.map.consumer;

import com.grandtheftwarzone.gtwmod.api.map.data.SRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;


@Getter
public class MapConsumers {

    @Setter
    private Consumer<SRequest> sRequest;



}
