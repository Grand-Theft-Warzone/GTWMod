package com.grandtheftwarzone.gtwmod.core.phone.core;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneManager;
import com.grandtheftwarzone.gtwmod.api.gui.phone.annotations.RegisterPhoneApp;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GtwPhoneManager implements PhoneManager, AtumModService {
    @Getter
    private final String id = "phone";
    private AtumMod atumMod;

    private final HashMap<String, PhoneApp> apps = new HashMap<>();
    @Getter
    private List<PhoneApp> appDrawingOrder = new ArrayList<>();

    public GtwPhoneManager(AtumMod atumMod){
        this.atumMod = atumMod;
        atumMod.provideModService(this);
    }

    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
        if(fmlEvent instanceof FMLInitializationEvent){
            //open phone on middle mouse click
            atumMod.getInputHandler().addListenerOnPress("phone",
                    this::onMouseWheelPressed
            );
            registerApps();
        }
    }

    @Override
    public void onRemove() {

    }

    private void registerApps(){
        // EXAMPLE OF A CHANGE
        GtwAPI.getInstance().getGtwMod().getLogger().info("Registering phone apps");
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(
                        "com.grandtheftwarzone.gtwclient" +
                                ".core.phone.apps..*")
                .scan()) { // Begin the scan

            ClassInfoList classInfos = scanResult.getClassesWithAnnotation(RegisterPhoneApp.class.getName());
            //log
            GtwAPI.getInstance().getGtwMod().getLogger().info("Found " + classInfos.size() + " phone apps");
            for (ClassInfo classInfo : classInfos) {
                Class<?> clazz = classInfo.loadClass();
                if (PhoneApp.class.isAssignableFrom(clazz)) {
                    try {
                        GtwAPI.getInstance().getGtwMod()
                                .getLogger().info("Loading "+clazz.getName()+" phone app...");
                        addApp((PhoneApp) clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addApp(@NotNull PhoneApp app) {
        apps.put(app.getId(), app);

        appDrawingOrder.add(app);
        appDrawingOrder.sort((o1, o2) -> {
            if(o1.getAppPriority() > o2.getAppPriority()){
                return 1;
            }else if(o1.getAppPriority() < o2.getAppPriority()){
                return -1;
            }
            return 0;
        });

    }

    @Override
    public void removeApp(@NotNull PhoneApp app) {
        apps.remove(app.getId());
    }

    @Override
    public @Nullable PhoneApp getApp(@NotNull String id) {
        return apps.get(id);
    }


    private void onMouseWheelPressed(InputPressEvent event){
        if(event.getType() != InputType.MOUSE_MIDDLE){
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.player == null){
            return;
        }
        if(!mc.inGameHasFocus){

            if(mc.currentScreen instanceof GtwPhoneScreen){
                ((GtwPhoneScreen) mc.currentScreen).closeAnimated();
            }
            return;
        }
        Minecraft.getMinecraft().displayGuiScreen(
            new GtwPhoneScreen(atumMod)
        );
    }
}
