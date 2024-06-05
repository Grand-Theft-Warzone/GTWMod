package com.grandtheftwarzone.gtwmod.core.map.globalmap.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class DataMapSubMenu {

    private String name;
    private AtumColor colorName;
    private int sizeText;
    @Nullable
    private ResourceLocation icon;
    @Nullable
    private Integer sizeIcon;
    private String actionStr;
}
