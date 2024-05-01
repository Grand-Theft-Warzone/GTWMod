package com.grandtheftwarzone.gtwmod.api.map.misc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GlobalZoom {
    private List<Integer> zoomInterpolations;

    @Setter
    private double animSpeed;

    @Getter
    private double coefZoomX, coefZoomY;

    public GlobalZoom(int defaultZoom, double animSpeed, double coefZoomX, double coefZoomY) {

        this.animSpeed = animSpeed;
        this.coefZoomX = coefZoomX;
        this.coefZoomY = coefZoomY;

        this.zoomInterpolations = new ArrayList<>();
        this.zoomInterpolations.add(defaultZoom);
    }
    public GlobalZoom(int defaultZoom) {
        this(defaultZoom, 1, 16, 9);
    }
    public GlobalZoom(int defaultZoom, double coefZoomX, double coefZoomY) {
        this(defaultZoom, 1, coefZoomX, coefZoomY);
    }

    public int getLastZoom() {
        return zoomInterpolations.get(zoomInterpolations.size() - 1);
    }



    public void setZoom(int newZoom, double animSpeed) {

        List<Integer> addZoomInterpolations = new ArrayList<>();
        int lastZoom = getLastZoom();
        int difference = newZoom - lastZoom;
        int numSteps = (int) (Math.abs(difference) / animSpeed);

        if (numSteps == 0) {
            addZoomInterpolations.add(newZoom);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            this.zoomInterpolations.addAll(addZoomInterpolations);
            return;
        }

        double stepSize = (double) difference / numSteps;

        for (int i = 0; i < numSteps; i++) {
            int interpolatedZoom = (int) (lastZoom + i * stepSize);
            addZoomInterpolations.add(interpolatedZoom);
        }

        addZoomInterpolations.add(newZoom);
        this.zoomInterpolations.addAll(addZoomInterpolations);

    }

    public void setZoomWithDuration(int newZoom, double duration) {
        List<Integer> addZoomInterpolations = new ArrayList<>();
        int lastZoom = getLastZoom();
        int difference = newZoom - lastZoom;
        double totalSteps = duration * 60; // Assuming 60 frames per second for smooth animation
        double stepSize = difference / totalSteps;

        for (int i = 0; i <= totalSteps; i++) {
            int interpolatedZoom = (int) (lastZoom + i * stepSize);
            addZoomInterpolations.add(interpolatedZoom);
        }

        this.zoomInterpolations.addAll(addZoomInterpolations);
    }

    public void setZoom(int newZoom) {
        this.setZoom(newZoom, this.animSpeed);
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

    public void addZoom(int addZoom, double animSpeed) {
        setZoom(zoomInterpolations.get(zoomInterpolations.size() - 1) + addZoom, animSpeed);
    }


    public void removeZoom(int removeZoom) {
        setZoom(zoomInterpolations.get(zoomInterpolations.size() - 1) - removeZoom);
    }

    public void removeZoom(int removeZoom, double animSpeed) {
        setZoom(zoomInterpolations.get(zoomInterpolations.size() - 1) - removeZoom, animSpeed);
    }


    public int getLastZoomAndClearList() {
        int lastZoom = zoomInterpolations.get(zoomInterpolations.size() - 1);
        // This step is necessary so that one element remains in the list under any conditions.
        ArrayList<Integer> newList = new ArrayList<>();
        newList.add(lastZoom);
        zoomInterpolations = newList;
        return lastZoom;
    }

    public int getZoomInterpolation() {
        if (zoomInterpolations.size() > 1) {
            return zoomInterpolations.remove(0);
        } else if (zoomInterpolations.size() == 1) {
            return zoomInterpolations.get(0);
        } else {
            System.out.println("Всё плохо. Список zoomInterpolations пуст");
            return -3000;
        }
    }

    public int getFirstZoom() {
        return zoomInterpolations.get(0);
    }
}
