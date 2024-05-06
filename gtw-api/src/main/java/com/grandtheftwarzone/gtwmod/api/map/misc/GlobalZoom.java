package com.grandtheftwarzone.gtwmod.api.map.misc;

import com.grandtheftwarzone.gtwmod.api.map.MapImage;
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

    private int minZoom, maxZoom;

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
            addZoomInterpolations.add(correctZoomInDiapazon(newZoom));
            this.zoomInterpolations.addAll(addZoomInterpolations);
            return;
        }

        double stepSize = (double) difference / numSteps;

        for (int i = 0; i < numSteps; i++) {
            int interpolatedZoom = correctZoomInDiapazon((int) (lastZoom + i * stepSize));
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
            int interpolatedZoom =  correctZoomInDiapazon((int) (lastZoom + i * stepSize));
            addZoomInterpolations.add(interpolatedZoom);
        }

        this.zoomInterpolations.addAll(addZoomInterpolations);
    }

    public List<Integer> setZoomWithDurationAndReplace(int newZoom, double duration) {
        List<Integer> addZoomInterpolations = new ArrayList<>();
        int lastZoom = getLastZoom();
        int difference = newZoom - lastZoom;
        double totalSteps = duration * 60; // Assuming 60 frames per second for smooth animation
        double stepSize = difference / totalSteps;

        for (int i = 0; i <= totalSteps; i++) {
            int interpolatedZoom =  correctZoomInDiapazon((int) (lastZoom + i * stepSize));
            addZoomInterpolations.add(interpolatedZoom);
        }

        this.zoomInterpolations = addZoomInterpolations;
        return addZoomInterpolations;
    }


    public void setZoom(int newZoom) {
        this.setZoom(newZoom, this.animSpeed);
    }


    public void setStraightZoom(int newZoom) {
        ArrayList<Integer> newList = new ArrayList<>();
        newList.add(correctZoomInDiapazon(newZoom));
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

    public int correctZoomInDiapazon(int zoom) {
        if (zoom < minZoom) {
            return minZoom;
        } else if (zoom > maxZoom) {
            return maxZoom;
        }
        return zoom;
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

    public void updateDiapazon(MapImage mapImage) {
        int maxZoomX = (int) ((int) (mapImage.getImageWidthReal() / coefZoomX) - ((int) (mapImage.getImageWidthReal() / coefZoomX) * 0.10));
        int maxZoomY = (int) ((int) (mapImage.getImageHeightReal() / coefZoomY) - ((int) (mapImage.getImageHeightReal() / coefZoomY) * 0.10));
        this.maxZoom = Math.min(maxZoomX, maxZoomY);
        this.minZoom = 10;
    }

    public int getFirstZoom() {
        return zoomInterpolations.get(0);
    }
}
