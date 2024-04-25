package com.grandtheftwarzone.gtwmod.api.map.misc;

import akka.japi.pf.Match;
import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GlobalZoom {
    private List<Integer> zoomInterpolations;

    private int step = 1;

    @Setter
    private double animSpeed;

    public GlobalZoom(int defaultZoom, double animSpeed) {

        this.animSpeed = animSpeed;

        this.zoomInterpolations = new ArrayList<>();
        this.zoomInterpolations.add(defaultZoom);
    }
    public GlobalZoom(int defaultZoom) {
        this(defaultZoom, 1);
    }

    public int getLastZoom() {
        return zoomInterpolations.get(zoomInterpolations.size() - 1);
    }

    // Итерполяляцию тут доделат надас
//    public void setZoom(int newZoom) {
//
//        System.out.println("Запускается setZoom, newZoom = " + newZoom + ", lastZoom = " + this.zoomInterpolations.get(this.zoomInterpolations.size() - 1) + ", step = " + step);
//
//        List<Integer> addZoomInterpolations = new ArrayList<>();
//        int lastZoom = this.zoomInterpolations.get(this.zoomInterpolations.size() - 1);
//        int difference = newZoom - lastZoom;
//        int znak = difference / Math.abs(difference);
//
//        int prozessZoom = lastZoom;
//        for (int i = 0; i < difference; i++) {
//            if (znak > 0) {
//                if (prozessZoom + znak*step < newZoom) {
//                    prozessZoom += znak*step;
//                    addZoomInterpolations.add(prozessZoom);
//                } else if (prozessZoom + znak*step >= newZoom) {
//                    addZoomInterpolations.add(newZoom);
//                    break;
//                }
//            } else if (znak < 0) {
//                if (prozessZoom + znak*step > newZoom) {
//                    prozessZoom += znak*step;
//                    addZoomInterpolations.add(prozessZoom);
//                } else if (prozessZoom + znak*step <= newZoom) {
//                    addZoomInterpolations.add(newZoom);
//                    break;
//                }
//            }
//
//        }
//        this.zoomInterpolations.addAll(addZoomInterpolations);
//        System.out.println("Cписок zoomInterpolations:" );
//        for (Integer ia : zoomInterpolations) {
//            System.out.print(ia + " ");
//        }
//    }

//    public void setZoom(int newZoom) {
//        int lastZoom = getLastZoom();
//        int difference = newZoom - lastZoom;
//        int numSteps = Math.abs(difference) / step;
//
//        if (numSteps == 0) {
//            addZoomInterpolation(newZoom);
//            return;
//        }
//
//        double stepSize = (double) difference / numSteps * animSpeed;
//
//        for (int i = 0; i < numSteps; i++) {
//            int interpolatedZoom = (int) (lastZoom + i * stepSize);
//            addZoomInterpolation(interpolatedZoom);
//        }
//
//        addZoomInterpolation(newZoom);
//    }

    public void setZoom(int newZoom) {

        List<Integer> addZoomInterpolations = new ArrayList<>();
        int lastZoom = getLastZoom();
        int difference = newZoom - lastZoom;
        int numSteps = Math.abs(difference) / step;

        if (numSteps == 0) {
            addZoomInterpolations.add(newZoom);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            this.zoomInterpolations.addAll(addZoomInterpolations);
            return;
        }

        double stepSize = (double) difference / numSteps * animSpeed;

        for (int i = 0; i < numSteps; i++) {
            int interpolatedZoom = (int) (lastZoom + i * stepSize);
            addZoomInterpolations.add(interpolatedZoom);
        }

        addZoomInterpolations.add(newZoom);
        this.zoomInterpolations.addAll(addZoomInterpolations);

    }


    private void addZoomInterpolation(int zoom) {
        zoomInterpolations.add(zoom);
    }

    public void setStraightZoom(int newZoom) {
        ArrayList<Integer> newList = new ArrayList<>();
        newList.add(newZoom);
        zoomInterpolations = newList;
    }

    public void addZoom(int addZoom) {
        setZoom(zoomInterpolations.get(zoomInterpolations.size() - 1) + addZoom);
    }

    public void removeZoom(int removeZoom) {
        setZoom(zoomInterpolations.get(zoomInterpolations.size() - 1) - removeZoom);
    }

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
