package com.grandtheftwarzone.gtwmod.core.map.globalmap.data;


import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.jetbrains.annotations.Nullable;

@Data
public class DataMapSubMenu {

    private String name;
    private AtumColor colorName;
    private int sizeText;
    private int edgeMargin;
    private ResourceLocation icon;
    private Integer sizeIcon;
    private Integer IndentIcons = 0;
    private String actionStr;

    public DataMapSubMenu(String name, AtumColor colorName, int sizeText, int edgeMargin, ResourceLocation icon, Integer sizeIcon, Integer IndentIcons, String actionStr) {
        this.name = name;
        this.colorName = colorName;
        this.sizeText = sizeText;
        this.edgeMargin = edgeMargin;
        this.icon = icon;
        this.sizeIcon = sizeIcon;
        this.IndentIcons = IndentIcons;
        this.actionStr = actionStr;
    }

    public DataMapSubMenu(String name, AtumColor colorName, int edgeMargin, int sizeText, String icon, Integer sizeIcon, Integer IndentIcons, String actionStr) {
        this.name = name;
        this.colorName = colorName;
        this.sizeText = sizeText;
        this.edgeMargin = edgeMargin;
        this.icon = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getImage("system/" + icon);
        this.sizeIcon = sizeIcon;
        this.IndentIcons = IndentIcons;
        this.actionStr = actionStr;
    }

}
