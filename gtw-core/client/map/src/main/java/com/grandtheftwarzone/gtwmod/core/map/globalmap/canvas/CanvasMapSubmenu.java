package com.grandtheftwarzone.gtwmod.core.map.globalmap.canvas;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.api.utils.GLUtils;
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
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.events.input.InputPressEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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

    @Getter
    private int indentText = 7; // Отступ между двумя строчками текста

    private int sizeIcon;

    private int indentIcon = 3;

    private int edgeMargin = 4;
    private EntityLocation mapCoord;

    private CanvasGlobalmap ownerCanvas;

    private int outsize = 1;

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

        AtumColor colorBackground = new AtumColor(160, 160, 160, 1);
        AtumColor colorStroke = AtumColor.ORANGE;

        int width = textWidthMax + indentIcon + sizeIcon + edgeMargin*2;
        int height = (textHeightMax+indentText)*elementNumber-indentText + edgeMargin*2;
        int coordX = coordTopLeftX - edgeMargin - outsize;
        int coordY = coordTopLeftY - edgeMargin - outsize;
        RenderUtils.drawRect(coordX, coordY, width, height, colorBackground);
        RenderUtils.drawOutline(coordX, coordY, width, height, outsize, colorStroke);

        for (int e = 0; e<=elementNumber-2; e++) {
            int posX = coordX;

            int posY = coordY + edgeMargin + textHeightMax + indentText/2 + (textHeightMax+indentText)*e;
//            RenderUtils.drawOutline(posX, posY, width, 1, 1, AtumColor.BLACK);
            GLUtils.drawDashedLine(posX, posY, posX+width, posY, colorStroke.toInt(), 1, 2, 1);
        }
    }

    public void init() {

        System.out.println("Вызывается init в CanvasMapSubmenu");

        ownerCanvas = (CanvasGlobalmap) getElementOwner();
        mapCoord = ownerCanvas.getGlobalmap().calculateWorldCoord(posX, posY);

        // Определение списка действий
        // !+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-!

        int sizeText = 12;
        float proporziaSizeText = (float) sizeText / 12;
        int textHeight = (int) (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * proporziaSizeText);
        textHeightMax = textHeight;
        int textWidth;
        this.sizeIcon = textHeight;
        AtumColor colorLocal = AtumColor.BLACK;
        AtumColor colorServer = new AtumColor(220, 0, 85);
        // Клик на пустое место
        System.out.println("Выбран элемент: " + elementClick);
        if (elementClick instanceof ElementMarker) {
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
                    DataMapSubMenu dataMapSubMenu = new DataMapSubMenu(name, AtumColor.BLUE, edgeMargin, sizeText, "action", sizeIcon, indentIcon, action.replace("№coord№", mapCoord.toString()));
                    dataMapSubMenus.add(dataMapSubMenu);
                }
            }
            // Если локальный маркер
            if (local) {
                DataMapSubMenu dataMapEdit = new DataMapSubMenu("Edit makrer", colorLocal, edgeMargin, sizeText, "edit_marker", sizeIcon, indentIcon, "map_edit_marker@" + marker.getIdentificator());
                dataMapSubMenus.add(dataMapEdit);

                DataMapSubMenu dataMapDelete = new DataMapSubMenu("Delete marker", colorLocal, edgeMargin, sizeText, "delete_marker", sizeIcon, indentIcon, "map_delete_marker@" + marker.getIdentificator());
                dataMapSubMenus.add(dataMapDelete);

            // Если маркер сервера и чел - админ
            } else if (admin) {

                DataMapSubMenu dataMapEdit = new DataMapSubMenu("Edit makrer", colorServer, edgeMargin, sizeText, "admin_edit_marker", sizeIcon, indentIcon, "map_edit_marker_server@" + marker.getIdentificator());
                dataMapSubMenus.add(dataMapEdit);


                DataMapSubMenu dataMapDelete = new DataMapSubMenu("Delete marker", colorServer, edgeMargin, sizeText, "delete_marker", sizeIcon,indentIcon, "map_delete_marker_server@" + marker.getIdentificator());
                dataMapSubMenus.add(dataMapDelete);

            }

            if (admin) {
                DataMapSubMenu dataMapTeleport = new DataMapSubMenu("Teleport", colorServer, edgeMargin, sizeText, "admin_teleport", sizeIcon, indentIcon, "map_teleport@" + mapCoord.toString());
                dataMapSubMenus.add(dataMapTeleport);
            }

            DataMapSubMenu dataCopyCoord = new DataMapSubMenu("Copy coordinates", colorLocal, edgeMargin, sizeText, "coord", sizeIcon,indentIcon, "map_copy_coord@" + mapCoord.toString() + marker.getIdentificator());
            dataMapSubMenus.add(dataCopyCoord);
        } else {
            if (admin) {
                DataMapSubMenu dataMapLocalCreate = new DataMapSubMenu("Create marker", colorLocal, edgeMargin, sizeText, "create_marker", sizeIcon, indentIcon, "map_create_marker@" + mapCoord.toString());
                dataMapSubMenus.add(dataMapLocalCreate);

                DataMapSubMenu dataMapAdminCreate = new DataMapSubMenu("Create marker", colorServer, edgeMargin, sizeText, "admin_create_marker", sizeIcon, indentIcon, "map_create_marker_server@" + mapCoord.toString());
                dataMapSubMenus.add(dataMapAdminCreate);

            } else {
                DataMapSubMenu dataMapLocalCreate = new DataMapSubMenu("Create marker", colorLocal, edgeMargin, sizeText, "create_marker", sizeIcon, indentIcon, "map_create_marker@" + mapCoord.toString());
                dataMapSubMenus.add(dataMapLocalCreate);
            }

            DataMapSubMenu dataMapTeleport = new DataMapSubMenu("Teleport", colorServer, edgeMargin, sizeText, "admin_teleport", sizeIcon, indentIcon, "map_teleport@" + mapCoord.toString());
            dataMapSubMenus.add(dataMapTeleport);
            DataMapSubMenu dataCopyCoord = new DataMapSubMenu("Copy coordinates", colorLocal, edgeMargin, sizeText, "coord", sizeIcon,indentIcon, "map_copy_coord@" + mapCoord.toString());
            dataMapSubMenus.add(dataCopyCoord);
        }


        // !+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-!


        // Рассчёт, где должен отобразиться правый верхний угол изображения.
        // !_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!
        int coordClickX = posX+edgeMargin+outsize*2;
        int coordClickY = posY+edgeMargin+outsize;

        for (DataMapSubMenu subMenu : dataMapSubMenus) {
            String text = !StringUtils.removeColorCodes(subMenu.getName()).isEmpty() ? StringUtils.removeColorCodes(subMenu.getName()) : "?";
            textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(text) * proporziaSizeText);

            if (textWidth > textWidthMax) {
                textWidthMax = textWidth;
            }
        }

        int width = textWidthMax + indentIcon + sizeIcon + edgeMargin*2;
        int height = (textHeightMax+indentText)*dataMapSubMenus.size()-indentText + edgeMargin*2;

        if (checkInZona(coordClickX, coordClickY, width, -height)) {
            System.out.println("1 true");
            coordTopLeftX = coordClickX;
            coordTopLeftY = coordClickY - height;
        } else if (checkInZona(coordClickX, coordClickY, -width, -height)) {
            System.out.println("2 true");
            coordTopLeftX = coordClickX - width;
            coordTopLeftY = coordClickY - height;
        } else if (checkInZona(coordClickX, coordClickY, width, height)) {
            System.out.println("3 true");
            coordTopLeftX = coordClickX;
            coordTopLeftY = coordClickY;
        } else if (checkInZona(coordClickX, coordClickY, -width, height)) {
            System.out.println("4 true");
            coordTopLeftX = coordClickX - width;
            coordTopLeftY = coordClickY;
        } else {
            coordTopLeftX = coordClickX - width;
            coordTopLeftY = coordClickY - height;
        }

//        coordTopLeftX = posX+edgeMargin+outsize;
//        coordTopLeftY = posY+edgeMargin+outsize;
        // !_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!


        // Регистрация элементов
        // |=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=|

        for (DataMapSubMenu subMenu : dataMapSubMenus) {
            String text = !StringUtils.removeColorCodes(subMenu.getName()).isEmpty() ? StringUtils.removeColorCodes(subMenu.getName()) : "?";
            int elementWidth = textWidthMax + edgeMargin*2 + subMenu.getSizeIcon() + subMenu.getIndentIcons();
            int elementHeight = textHeightMax + indentText;

            int posX = coordTopLeftX - edgeMargin*2;
            int posY = coordTopLeftY + ((textHeight+indentText)*elementNumber ) - indentText/2;

            elementNumber += 1;

            System.out.println(elementNumber + " posX PosY " + posX + " <> " + posY);
            this.addElement(new ElementSubMenu(getAtumMod(), 70, posX * RenderUtils.getScaleFactor(), posY * RenderUtils.getScaleFactor(), (elementWidth) * RenderUtils.getScaleFactor(), elementHeight * RenderUtils.getScaleFactor(), this, subMenu));
        }
        // |=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=|

        init = true;

    }

    public boolean checkInZona(int clickX, int clickY, int width, int height) {
        System.out.println("Check " + clickX + " " + clickY + " " + width + " " + height);
        return checkInterval(clickX, clickY) && checkInterval(clickX, clickY + height) && checkInterval(clickX + width, clickY + height) && checkInterval(clickX + width, clickY);
    }

    public boolean checkInterval(int x, int y) {
        return (x >= getX() && x <= getWidth()) && (y >= getY() && y <= getHeight());
    }


    @SubscribeEvent
    protected void onPress(InputPressEvent event) {
        if (this.isActive()) {
            DisplayElement element = this.getElementFromCoordinates(event.getMouseX(), event.getMouseY());
            if (event.getType() == InputType.MOUSE_LEFT) {
                if (element != null) {
                    if (element instanceof ElementSubMenu) {
                        ((ElementSubMenu)element).onClick();
                        return;
                    }
                } else {
                    onRemove();
                }
            }

        }
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
