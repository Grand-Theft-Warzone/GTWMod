package com.grandtheftwarzone.gtwmod.api.map.data.client;

import com.grandtheftwarzone.gtwmod.api.map.data.MapImageData;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;

@AllArgsConstructor
@Data
public class UpdateMinimapData {
    MapImageData mapImageData;
    String radarImageId;
    @Nullable Boolean allowDisplay;
}
