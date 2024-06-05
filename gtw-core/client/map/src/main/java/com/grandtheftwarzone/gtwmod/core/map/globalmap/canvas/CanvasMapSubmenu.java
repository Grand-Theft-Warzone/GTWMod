package com.grandtheftwarzone.gtwmod.core.map.globalmap.canvas;

import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.data.DataMapSubMenu;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.element.ElementMarker;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.element.ElementSubMenu;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.variables.OptimizedVarInt;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

public class CanvasMapSubmenu extends BaseCanvas {

    @Nullable @Getter @Setter
    private DisplayElement elementClick;

    @Getter @Setter
    private boolean admin;
    private boolean init;
    private int posX;
    private int posY;

    private int textWidthMax = 0;
    private int textHeightMax = 0;

    private int indentText = 7; // Отступ между двумя строчками текста

    private EntityLocation mapCoord;

    private CanvasGlobalmap ownerCanvas;

    private int elementNumber = 0;

    int coordTopLeftX;
    int coordTopLeftY;

    List<DataMapSubMenu> dataMapSubMenus = new ArrayList<>();

    public CanvasMapSubmenu(@NotNull AtumMod atumMod, @NotNull int layer, int posX, int posY, int width, int height, @Nullable DisplayCanvas elementOwner, @Nullable DisplayElement elementClick) {
        super(atumMod, layer, 0, 0, width, height, elementOwner);
        this.elementClick = elementClick;
        this.posX = posX;
        this.posY = posY;
        this.admin =  Minecraft.getMinecraft().player.capabilities.isCreativeMode;
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {

        if (!(getElementOwner() instanceof CanvasGlobalmap)) {
            System.out.println("[ERROR] CanvasMapSubmenu only works from elementOwner = CanvasGlobalmap!");
            onRemove();
            return;
        }

        if (!init) {
            init();
            return;
        }

        System.out.println("Пытаюсь отобразить");
        System.out.println(getX() + " " + getX());
        RenderUtils.drawRect(coordTopLeftX, coordTopLeftY, textWidthMax, (textHeightMax+indentText)*elementNumber, AtumColor.WHITE);

    }

    public void init() {

        System.out.println("Вызывается init в CanvasMapSubmenu");

        ownerCanvas = (CanvasGlobalmap) getElementOwner();
        mapCoord = ownerCanvas.getGlobalmap().calculateWorldCoord(posX, posY);

        // Определение списка действий
        // !+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-!
        int sizeText = 12;
        AtumColor colorLocal = AtumColor.BLUE;
        AtumColor colorServer = AtumColor.RED;
        // Клик на пустое место
        if (elementClick == null) {
            if (admin) {
                DataMapSubMenu dataMapLocalCreate = new DataMapSubMenu("Create marker", colorLocal, sizeText, null, null, "map_create_marker@" + mapCoord.toString());
                dataMapSubMenus.add(dataMapLocalCreate);
                DataMapSubMenu dataMapAdminCreate = new DataMapSubMenu("Create marker", colorServer, sizeText, null, null, "map_create_marker_server@" + mapCoord.toString());
                dataMapSubMenus.add(dataMapAdminCreate);
                DataMapSubMenu dataMapTeleport = new DataMapSubMenu("Teleport", colorServer, sizeText, null, null, "map_teleport@" + mapCoord.toString());
                dataMapSubMenus.add(dataMapTeleport);
            } else {
                DataMapSubMenu dataMapLocalCreate = new DataMapSubMenu("Create marker", colorLocal, sizeText, null, null, "map_create_marker@" + mapCoord.toString());
                dataMapSubMenus.add(dataMapLocalCreate);
            }

        // Клик на маркер
        } else if (elementClick instanceof ElementMarker) {
            ElementMarker elementMarker = ((ElementMarker) elementClick);
            MapMarker marker = elementMarker.getMarker();
            boolean local = marker.isLocalMarker();
            List<String> actionList = marker.getActionList();

            // Первыми идут действия маркера
            if (actionList != null) {
                for (String action : actionList) {
                    String name = ownerCanvas.getSettingsConfig().getSubsection("settings").getSubsection("actions").getSubsectionOrNull(action.split("@")[0]).getStringOrNull("text");
                    if (name == null) {
                        name = "| NO ACTION |";
                    }
                    // !! В action если указать №coord№ - заменится на координаты.
                    DataMapSubMenu dataMapSubMenu = new DataMapSubMenu(name, AtumColor.BLUE, sizeText, null, null, action.replace("№coord№", mapCoord.toString()));
                    dataMapSubMenus.add(dataMapSubMenu);
                }
            }
            // Если локальный маркер
            if (local) {
                DataMapSubMenu dataMapEdit = new DataMapSubMenu("Edit makrer", colorLocal, sizeText, null, null, "map_edit_marker@" + marker.getIdentificator());
                dataMapSubMenus.add(dataMapEdit);

                DataMapSubMenu dataMapDelete = new DataMapSubMenu("Delete marker", colorLocal, sizeText, null, null, "map_delete_marker@" + marker.getIdentificator());
                dataMapSubMenus.add(dataMapDelete);

            // Если маркер сервера и чел - админ
            } else if (admin) {

                DataMapSubMenu dataMapEdit = new DataMapSubMenu("Edit makrer", colorServer, sizeText, null, null, "map_edit_marker_server@" + marker.getIdentificator());
                dataMapSubMenus.add(dataMapEdit);


                DataMapSubMenu dataMapDelete = new DataMapSubMenu("Delete marker", colorServer, sizeText, null, null, "map_delete_marker_server@" + marker.getIdentificator());
                dataMapSubMenus.add(dataMapDelete);

            }
        }

        DataMapSubMenu dataCopyCoord = new DataMapSubMenu("Copy coordinates", colorLocal, sizeText, null, null, "map_copy_coord@" + mapCoord.toString());
        dataMapSubMenus.add(dataCopyCoord);
        // !+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-!


        // Рассчёт, где должен отобразиться правый верхний угол изображения.
        // !_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!
        coordTopLeftX = posX;
        coordTopLeftY = posY;
        // !_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!


        // Регистрация элементов
        // |=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=|
        float proporziaSizeText = (float) sizeText / 12;
        int textHeight = (int) (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * proporziaSizeText);
        textHeightMax = textHeight;
        int textWidth;

        for (DataMapSubMenu subMenu : dataMapSubMenus) {
            elementNumber += 1;
            String text = !StringUtils.removeColorCodes(subMenu.getName()).isEmpty() ? StringUtils.removeColorCodes(subMenu.getName()) : "?";
            textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(text) * proporziaSizeText);

            if (textWidth > textWidthMax) {
                textWidthMax = textWidth;
            }

            int posX = coordTopLeftX;
            int posY = ((coordTopLeftY + indentText)*elementNumber);
            this.addElement(new ElementSubMenu(getAtumMod(), 70, posX, posY, textWidth, textHeight, this, subMenu));
        }

        // |=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=|

        init = true;

    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

    @Override
    public void onRemove() {
        super.onRemove();
        System.out.println("Вызываю onRemove в CanvasMapSubmenu");
        if (getElementOwner() instanceof CanvasGlobalmap) {
            ((CanvasGlobalmap) getElementOwner()).setSubCanvas(null);
        }
    }

}
