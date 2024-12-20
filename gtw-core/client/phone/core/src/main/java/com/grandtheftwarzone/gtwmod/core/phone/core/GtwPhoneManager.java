package com.grandtheftwarzone.gtwmod.core.phone.core;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneManager;
import com.grandtheftwarzone.gtwmod.api.gui.phone.annotations.RegisterPhoneApp;

import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.service.AtumModService;
import me.phoenixra.libs.io.github.classgraph.ClassGraph;
import me.phoenixra.libs.io.github.classgraph.ClassInfo;
import me.phoenixra.libs.io.github.classgraph.ClassInfoList;
import me.phoenixra.libs.io.github.classgraph.ScanResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
            registerApps();
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Override
    public void onRemove() {

    }

    private void registerApps(){
        GtwAPI.getInstance().getGtwMod().getLogger().info("Registering phone apps");
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(
                        "com.grandtheftwarzone.gtwmod" +
                                ".core.phone..*")
                .scan()) {// Begin the scan
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
    public void tryOpenApp(String appId){
        if(!(Minecraft.getMinecraft().currentScreen
                instanceof GtwPhoneScreen)){
            return;
        }
        PhoneApp app= getApp(appId);
        if(app==null) return;
        GtwPhoneScreen phoneScreen = (GtwPhoneScreen) Minecraft.getMinecraft().currentScreen;
        CanvasPhone canvasPhone= (CanvasPhone) phoneScreen.getRenderer().getBaseCanvas();
        canvasPhone.openApp(app);
    }
    @Override
    public void removeApp(@NotNull PhoneApp app) {
        apps.remove(app.getId());
    }

    @Override
    public @Nullable PhoneApp getApp(@NotNull String id) {
        return apps.get(id);
    }

    @SubscribeEvent
    public void onMouseClicked(MouseEvent e) {
        int i = Mouse.getEventButton();

        if(Mouse.getEventButtonState() && i == 2){
            Minecraft mc = Minecraft.getMinecraft();
            if(mc.player == null || mc.currentScreen != null){
                return;
            }
            if(mc.player.isCreative()) return;
            String phoneId = Objects.requireNonNull(GtwAPI.getInstance().getGtwMod().
                            getConfigManager().getConfig("settings"))
                    .getString("phone");
            Minecraft.getMinecraft().displayGuiScreen(
                    new GtwPhoneScreen(atumMod,phoneId)
            );
            e.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onMouseClicked(GuiScreenEvent.MouseInputEvent.Pre e) {
        int i = Mouse.getEventButton();

        if(Mouse.getEventButtonState() && i == 2){
            Minecraft mc = Minecraft.getMinecraft();
            if(mc.currentScreen instanceof GtwPhoneScreen){
                ((GtwPhoneScreen)mc.currentScreen).closeAnimated();

            }
        }
    }
}
