package com.grandtheftwarzone.gtwmod.api.map.data;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CStartData {
    private MapImageData minimapData;
    private MapImageData globalmapData;
    private RestrictionsData restrictionsData;
}
