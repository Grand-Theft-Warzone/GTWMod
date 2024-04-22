package com.grandtheftwarzone.gtwmod.api.map.data.server;

import com.grandtheftwarzone.gtwmod.api.map.data.MapImageData;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;

@AllArgsConstructor
@Data
public class UpdateGlobalmapData {
    MapImageData mapImageData;
    @Nullable
    Boolean allowDisplay;
}
