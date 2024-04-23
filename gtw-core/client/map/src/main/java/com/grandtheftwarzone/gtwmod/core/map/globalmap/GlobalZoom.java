package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import jdk.nashorn.internal.objects.annotations.Getter;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;


public class GlobalZoom {
    private List<Integer> zoomInterpolations;

    public GlobalZoom(int defaultZoom) {

        this.zoomInterpolations = new ArrayList<>();
        this.zoomInterpolations.add(defaultZoom);
    }

    public int getLastZoom() {
        return zoomInterpolations.get(zoomInterpolations.size() - 1);
    }

    // Итерполяляцию тут доделат надас

    public int getLastZoomAndClearList() {
        int lastZoom = zoomInterpolations.get(zoomInterpolations.size() - 1);
        // This step is necessary so that one element remains in the list under any conditions.
        ArrayList<Integer> newList = new ArrayList<>();
        newList.add(lastZoom);
        zoomInterpolations = newList;
        return lastZoom;
    }

    public int getZoom() {
        if (zoomInterpolations.size() > 1) {
            return zoomInterpolations.remove(0);
        } else if (zoomInterpolations.size() == 1) {
            return zoomInterpolations.get(0);
        } else {
            System.out.println("Всё плохо. Список zoomInterpolations пуст");
            return -3000;
        }
    }
}
