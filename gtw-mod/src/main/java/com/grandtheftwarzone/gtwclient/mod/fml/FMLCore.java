package com.grandtheftwarzone.gtwclient.mod.fml;

import com.grandtheftwarzone.gtwclient.api.GtwAPI;
import com.grandtheftwarzone.gtwclient.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwclient.core.display.GtwScreensManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;


@SideOnly(Side.CLIENT)
@IFMLLoadingPlugin.SortingIndex(2)
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class FMLCore implements IFMLLoadingPlugin {


    @Override
    public String[] getASMTransformerClass() {
        return new String[]{ScreensManager.getLoadingScreenClassName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
