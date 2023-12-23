package com.grandtheftwarzone.gtwmod.core.phone.core;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneGui;
import com.grandtheftwarzone.gtwmod.api.gui.phone.annotations.RegisterPhoneApp;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class GtwPhoneGui implements PhoneGui {
    private final HashMap<String, PhoneApp> apps;

    public GtwPhoneGui() {
        apps = new HashMap<>();
        registerApps();
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

    @Override
    public void addApp(@NotNull PhoneApp app) {
        apps.put(app.getId(), app);
    }

    @Override
    public void removeApp(@NotNull PhoneApp app) {
        apps.remove(app.getId());
    }

    @Override
    public @Nullable PhoneApp getApp(@NotNull String id) {
        return apps.get(id);
    }

    @Override
    public @NotNull BaseScreen getGuiScreen() {
        return null;
    }


}
