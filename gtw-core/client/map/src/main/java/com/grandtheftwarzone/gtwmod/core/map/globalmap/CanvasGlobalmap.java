package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.manager.client.MapManagerClient;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.misc.GlobalCentrCoord;
import com.grandtheftwarzone.gtwmod.api.map.misc.GlobalZoom;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.events.input.InputPressEvent;
import me.phoenixra.atumodcore.api.events.input.InputReleaseEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;

import javax.vecmath.Vector2d;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.grandtheftwarzone.gtwmod.api.utils.GLUtils.drawPartialImage;

@RegisterDisplayElement(templateId = "canvas_globalmap")
public class CanvasGlobalmap extends BaseCanvas {

    boolean isActive = false;

    private HashMap<String, ElementMarker> markerMap = new HashMap<>();

    private MapImage globalmap;
    private ResourceLocation globalmapTexture;

    private GlobalZoom zoom;

    private GlobalCentrCoord centrCoord;

    private boolean pressIncreaseZoom;
    private boolean pressDecreaseZoom;
    private boolean pressLCM;

    private boolean pressWheel;

    private int directionWheel;

    private int lastZoom;
    private MapLocation lastDeltaCoords = new MapLocation(0, 0);
    private boolean blockReturnLCM = false;
    private MapLocation firstMouseLocation;

    public CanvasGlobalmap(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {


        // Проверка, нужна ли инициализация
        boolean init = GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().isInitCanvasDraw();
        isActive = init;
        if (!init) {
            init();
            return;
        }

        // Блок отслеживания нажатия на клавиши зума.
        // ------------------------------------------------
        if (pressDecreaseZoom) {
            System.out.println("Отдаление...");
            int step = getSettingsConfig().getSubsection("settings").getSubsection("zoom").getInt("step");
            zoom.addZoom(step);
            centrCoord.setStraightCenter(centrCoord.getVerifiedCoordinates(centrCoord.getFirstCoordInter(), globalmap));
        } else if (pressIncreaseZoom) {
            System.out.println("Приближение...");
            int step = getSettingsConfig().getSubsection("settings").getSubsection("zoom").getInt("step");
            zoom.removeZoom(step);
            centrCoord.setStraightCenter(centrCoord.getVerifiedCoordinates(centrCoord.getFirstCoordInter(), globalmap));
        }
        // ------------------------------------------------


        // Блок перемещение через курсор
        // ------------------------------------------
        if (pressWheel) {
            System.out.println("Вижу нажатие колесика...");
            int centerScreenX = getWidth() / 2;
            int centerScreenY = getHeight() / 2;

            int mouseX = getLastMouseX();
            int mouseY = getLastMouseY();

            double step = getSettingsConfig().getSubsection("settings").getSubsection("scroll").getDouble("step");

            Vector2d vectorCenterCursor = new Vector2d(mouseX-centerScreenX, mouseY-centerScreenY);
            vectorCenterCursor.scale((double) 1 / step * directionWheel);

            double coef_mul = getSettingsConfig().getSubsection("settings").getSubsection("scroll").getDouble("coef_mul_zoom");
            double duration = getSettingsConfig().getSubsection("settings").getSubsection("scroll").getDouble("duration");

            zoom.setStraightZoom(lastZoom); // !
            List<Integer> interpolationZoom = zoom.setZoomWithDurationAndReplace((int) (lastZoom+(-(step*coef_mul)*directionWheel)), duration);

            double deltaX = vectorCenterCursor.getX() * (double) (int) (zoom.getFirstZoom()*zoom.getCoefZoomX()) / getWidth();
            double deltaY = vectorCenterCursor.getY() * (double) (int) (zoom.getFirstZoom()*zoom.getCoefZoomY()) / getHeight();
            MapLocation newCenterLocation = new MapLocation((centrCoord.getFirstCoordInter().getX() + deltaX), (centrCoord.getFirstCoordInter().getY() + deltaY));

            centrCoord.setStraightCenter(centrCoord.getFirstCoordInter());
            centrCoord.setCentrCoordDurationsDependencyZoom(newCenterLocation, duration, interpolationZoom);

            pressWheel = false;
        }
        // ------------------------------------------

        // Определение переменных 1
        // ------------------------------------------
        lastZoom = zoom.getZoomInterpolation();
        MapLocation cetnerLocation = centrCoord.getCentreCoordInter();
        // ------------------------------------------
        // Определение переменных 2
        // ------------------------------------------------
        int zoomX = (int) (lastZoom*zoom.getCoefZoomX());
        int zoomY = (int) (lastZoom*zoom.getCoefZoomY());

        double proporziaX = (double) zoomX / getWidth();
        double proporziaY = (double) zoomY / getHeight();
        // ------------------------------------------------

        // Блок перемещения через LCM
        // ------------------------------------------------
        if (pressLCM) {

            MapLocation distanceAccess = centrCoord.getDistanceAccess(globalmap);
            MapLocation deltaCoords = centrCoord.getDeltaCoords(centrCoord.getFirstCoordInter(), globalmap, distanceAccess);
            double springResistance = 1;
            if (!centrCoord.isAccessZone(deltaCoords)) {
                System.out.println("Вышли за пределы доступной зоны");

                MapLocation maxDistance = new MapLocation(distanceAccess.getX() / 5, distanceAccess.getY() / 6);

                System.out.println("MaxDelta: " + maxDistance.toString());


                double springResistanceX = centrCoord.getSpringResistance(deltaCoords.getX(), maxDistance.getX());
                double springResistanceY = centrCoord.getSpringResistance(deltaCoords.getY(), maxDistance.getY());

                if (lastDeltaCoords != null && deltaCoords.sumAbsCord() < lastDeltaCoords.sumAbsCord()) {
                    if (Math.abs(deltaCoords.getX()) < Math.abs(lastDeltaCoords.getX())) {
                        springResistanceX = springResistanceX*2;
                    }
                    if (Math.abs(deltaCoords.getY()) < Math.abs(lastDeltaCoords.getY())) {
                        springResistanceY = springResistanceY*2;
                    }
                }

                springResistance = Math.min(springResistanceX, springResistanceY);
            }

            lastDeltaCoords = deltaCoords;
            System.out.println("Сoef сопротивления: " + springResistance);

            double deltaX = (getLastMouseX() - firstMouseLocation.getX()) * proporziaX * springResistance;
            double deltaY = (getLastMouseY() - firstMouseLocation.getY()) * proporziaY * springResistance;

            System.out.println("deltaX " + deltaX);
            System.out.println("deltaY " + deltaY);

            MapLocation newCenterLocation = new MapLocation((centrCoord.getLastCentreCoord().getX() - deltaX), (centrCoord.getLastCentreCoord().getY() - deltaY));
            centrCoord.setStraightCenter(newCenterLocation, false);
            firstMouseLocation = new MapLocation(getLastMouseX(), getLastMouseY());
        } else {
            if (blockReturnLCM && !centrCoord.isAccessZone(lastDeltaCoords)) {
                MapLocation newCenterLocation = centrCoord.getVerifiedCoordinatesLCM(centrCoord.getFirstCoordInter(), globalmap, lastDeltaCoords);
                System.out.println("Kvakva");
                centrCoord.setCentrCoordDurations(newCenterLocation, 0.4, false);
                blockReturnLCM = false;
            }
        }
        // ------------------------------------------------



        // Рендер
        // ==========================================
        RenderUtils.bindTexture(globalmapTexture);
        float renderCenterX = (float) (cetnerLocation.getX()  - (zoomX / 2));
        float rebderCenterY = (float) (cetnerLocation.getY()  - (zoomY / 2));
        drawPartialImage(0, 0, getWidth(), getHeight(), renderCenterX, rebderCenterY, zoomX, zoomY);


        // Обработка маркеров.
        // ----------------------------------------

        // _ Регистрация маркеров _
        List<MapMarker> markerList = GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getAllMarkerFilter(globalmap.getImageId());
        registerElementMarkers(markerList);


        // Определение переменных
        // =======================================
        int sizeMarker = 18;
        // =======================================

        // _ Отрисовка маркеров _
        for (String markerId : markerMap.keySet()) {

            ElementMarker elementMarker = markerMap.get(markerId);

            MapMarker marker = elementMarker.marker;

            if (marker.isDraw()) {

                MapLocation markerCoord = marker.getMapLocation("globalmap");

                double deltaX = markerCoord.getX() - cetnerLocation.getX();
                double deltaY = markerCoord.getY() - cetnerLocation.getY();

                int drawX = (int) ((deltaX / ((double) zoomX / getOriginWidth().getValue(displayResolution))) + getOriginX().getValue(displayResolution) + getOriginWidth().getValue(displayResolution) /2) - sizeMarker/2;
                int drawY = (int) ((deltaY / ((double) zoomY / getOriginHeight().getValue(displayResolution))) + getOriginY().getValue(displayResolution) + getOriginHeight().getValue(displayResolution) /2) - sizeMarker/2;

                System.out.println("SAS: " + drawX + " <> " + drawY);

                    elementMarker.setSize(sizeMarker);
                    elementMarker.setXY(drawX, drawY);
                    elementMarker.draw(displayResolution, v, i, i1);

    //                RenderUtils.drawRect(drawX, drawY, 16, 16, AtumColor.LIME);

            }
        }
        // -----------------------------------------



        // ==========================================




    }




    private void init() {
        // БлаБлаБла
        // @TODO remove
        if (!GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay()) {
            return;
        }

        zoom = GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalZoom();
        centrCoord = GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getCentrCoord();
        globalmap = GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalmapImage();
        globalmapTexture = globalmap.getImage();
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalZoom().updateDiapazon(globalmap);


        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setInitCanvasDraw(true);

    }


    // @TODO It's better to redo it, because... greater difficulty with long markers
    private void registerElementMarkers(List<MapMarker> markers) {

        for (String markerId : markerMap.keySet()) {
            boolean ostavl = false;
            for (MapMarker marker : markers) {
                if (marker.getIdentificator().equals(markerId)) {
                    ostavl = true;
                    markers.remove(marker);
                    break ;
                }
            }
            if (!ostavl) {
                removeElementMarker(markerMap.get(markerId));
            }

        }
        for (MapMarker marker : markers) {
            addElementMarker(marker);
        }
    }

    private void addElementMarker(MapMarker marker) {
        ElementMarker elementMarker = new ElementMarker(getAtumMod(), this, marker);
        this.addElement(elementMarker);
        markerMap.put(marker.getIdentificator(), elementMarker);
        System.out.println("[Canvas GlobalMap] Маркер " + marker.getName() + " добавлен.");
    }
    private void removeElementMarker(ElementMarker elementMarker) {
        this.removeElement(elementMarker);
        markerMap.remove(elementMarker.marker.getIdentificator());
        System.out.println("[Canvas GlobalMap] Маркер " + elementMarker.marker.getName() + " удалён.");
    }



    @Override
    public void updateElementVariables(@NotNull Config config) {
        System.out.println("Вызываю updateElementVariables в CanvasGlobalmap");
//        String debugCordStr = config.getString("debug_cord");
//        imageLocation = new MapLocation(debugCordStr);

        // zoom
        int configZoom = config.getSubsection("zoom").getInt("zoom");
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalZoom().setStraightZoom(configZoom);
        double configZoomSpeed = config.getSubsection("zoom").getDouble("speed");
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalZoom().setAnimSpeed(configZoomSpeed);

        // centr coord
        System.out.println("DEBUG " + config.getSubsection("center").getDouble("speed"));
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getCentrCoord().setAnimSpeed(config.getSubsection("center").getDouble("speed"));
        if (getSettingsConfig().getSubsection("settings").getBool("save_coord")) {
            GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getCentrCoord().setStraightCenter(new MapLocation(config.getString("center_coord")));
        }
        isActive = false;
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setInitCanvasDraw(false);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        System.out.println("Вызываю onRemove в CanvasGlobalmap");
        isActive = false;
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setInitCanvasDraw(false);

        saveZoomInConfig();

        if (getSettingsConfig().getSubsection("settings").getBool("save_coord")) {
            saveCenterCoordInConfig();
        }
    }

    public void saveZoomInConfig() {
        LoadableConfig config = (LoadableConfig) getSettingsConfig();
        config.getSubsection("settings").getSubsection("zoom").set("zoom", GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalZoom().getLastZoomAndClearList());
        try {
            config.save();
            System.out.println("Zoom успешно сохранено в config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCenterCoordInConfig() {
        LoadableConfig config = (LoadableConfig) getSettingsConfig();
        config.getSubsection("settings").set("center_coord", GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getCentrCoord().getFirstCoordInter().toString());
        try {
            config.save();
            System.out.println("Center_coord успешно сохранено в config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }


    public void onScrollEvent(int deltaWheel) {

        // @TODO Нужна проверка, что н е открыты другие GUI в этом GUI.

        if (pressLCM) {
            return;
        }

        System.out.println("Вижу нажатие колесика...");
        this.directionWheel = deltaWheel / 120;
        pressWheel = true;
    }


    @SubscribeEvent
    protected void onPressLCM(InputPressEvent event) {

        if (!isActive()) {
            return;
        }

        System.out.println(event.getMouseScrollDelta());

        if (event.getType().equals(InputType.MOUSE_LEFT)) {
            System.out.println("Нажата левая кнопка мыши");
            firstMouseLocation = new MapLocation(getLastMouseX(), getLastMouseY());
            blockReturnLCM = true;
            pressLCM = true;

        }
    }


    @SubscribeEvent
    protected void onReleaseLCM(InputReleaseEvent event) {
        if(!isActive()){
            return;
        }
        MapManagerClient mapManagerClient = GtwAPI.getInstance().getMapManagerClient();

        if (event.getType().equals(InputType.MOUSE_LEFT)) {
            System.out.println("Отпущена левая кнопка мыши");
            pressLCM = false;
//            GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getCentrCoord().setStraightCenter(GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getCentrCoord().getFirstCoordInter());
            if (getSettingsConfig().getSubsection("settings").getBool("save_coord")) {
                saveCenterCoordInConfig();
            }
        }

    }






    @SubscribeEvent
    protected void onPressZoom(InputPressEvent event) {

        MapManagerClient mapManagerClient = GtwAPI.getInstance().getMapManagerClient();

        if (!isActive()) {
            return;
        }

        // ZOOM
        if (event.getKeyboardKey() == mapManagerClient.getKeyIncreaseZoom().getKeyCode() && !pressDecreaseZoom) {
            System.out.println("Включаю pressIncreaseZoom");
            pressIncreaseZoom = true;
        } else if (event.getKeyboardKey() == mapManagerClient.getKeyDecreaseZoom().getKeyCode() && !pressIncreaseZoom) {
            System.out.println("Включаю pressDecreaseZoom");
            pressDecreaseZoom = true;
        }

    }

    @SubscribeEvent
    protected void onReleaseZoom(InputReleaseEvent event) {
        if(!isActive()){
            return;
        }
        MapManagerClient mapManagerClient = GtwAPI.getInstance().getMapManagerClient();

        // ZOOM
        if (event.getKeyboardKey() == GtwAPI.getInstance().getMapManagerClient().getKeyIncreaseZoom().getKeyCode()) {
            System.out.println("Выключаю pressIncreaseZoom");
            pressIncreaseZoom = false;
            mapManagerClient.getGlobalmapManager().getGlobalZoom().setStraightZoom(lastZoom);
            saveZoomInConfig();
        } else if (event.getKeyboardKey() == GtwAPI.getInstance().getMapManagerClient().getKeyDecreaseZoom().getKeyCode()) {
            System.out.println("Выключаю pressDecreaseZoom");
            pressDecreaseZoom = false;
            mapManagerClient.getGlobalmapManager().getGlobalZoom().setStraightZoom(lastZoom);
            saveZoomInConfig();
        }

    }


}
