package com.grandtheftwarzone.gtwmod.api.map.misc;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GlobalCentrCoord {

    private List<MapLocation> centrCoordsInterpol;

    @Setter
    private double animSpeed;

    private GlobalZoom zoom;

    public GlobalCentrCoord(MapLocation centreCoords, GlobalZoom zoom, double animSpeed) {

        this.animSpeed = animSpeed;
        this.zoom = zoom;

        this.centrCoordsInterpol = new ArrayList<>();
        this.centrCoordsInterpol.add(centreCoords);

    }

    public GlobalCentrCoord(MapLocation centreCoords, GlobalZoom zoom) {
        this(centreCoords, zoom, 1);
    }


    // @TODO Удалить!
    public void setCentrCoords(MapLocation newCenter, double animSpeed) {

        List<MapLocation> addCentrCoordInterpolations = new ArrayList<>();

        MapLocation lastCenter = getLastCentreCoord();
        double deltaX = newCenter.getX() - lastCenter.getX();
        double deltaY = newCenter.getY() - lastCenter.getY();
        int numSteps = (int) (Math.max(Math.abs(deltaX), Math.abs(deltaY)) / animSpeed);

        if (numSteps == 0) {
            addCentrCoordInterpolations.add(newCenter);
            this.centrCoordsInterpol.addAll(addCentrCoordInterpolations);
            return;
        }

        double stepSizeX = deltaX / numSteps;
        double stepSizeY = deltaY / numSteps;

        for (int i = 0; i < numSteps; i++) {
            double interpolatedX = lastCenter.getX() + i * stepSizeX;
            double interpolatedY = lastCenter.getY() + i * stepSizeY;
            MapLocation interpolatedLocation = new MapLocation(interpolatedX, interpolatedY);
            addCentrCoordInterpolations.add(interpolatedLocation);
        }

        addCentrCoordInterpolations.add(newCenter);
        this.centrCoordsInterpol.addAll(addCentrCoordInterpolations);
    }

    public void setCentrCoordDurations(MapLocation newCenter, double animSpeed, boolean checkCorrectCord) {

        List<MapLocation> addCentrCoordInterpolations = new ArrayList<>();

        MapLocation lastCenter = getLastCentreCoord();
        double deltaX = newCenter.getX() - lastCenter.getX();
        double deltaY = newCenter.getY() - lastCenter.getY();
        double totalSteps = animSpeed * 60; // Assuming 60 frames per second for smooth animation
        double stepSizeX = deltaX / totalSteps;
        double stepSizeY = deltaY / totalSteps;

        for (int i = 0; i <= totalSteps; i++) {
            double interpolatedX = lastCenter.getX() + i * stepSizeX;
            double interpolatedY = lastCenter.getY() + i * stepSizeY;
            MapLocation interpolatedLocation = new MapLocation(interpolatedX, interpolatedY);

            if (checkCorrectCord && !isAccessZone(interpolatedLocation, GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalmapImage())) {
                MapLocation newLocation = getVerifiedCoordinates(interpolatedLocation, GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalmapImage());
                addCentrCoordInterpolations.add(newLocation);
                continue;
            }

            addCentrCoordInterpolations.add(interpolatedLocation);
        }

        this.centrCoordsInterpol.addAll(addCentrCoordInterpolations);
    }

    public void setCentrCoordDurations(MapLocation newCenter, double animSpeed) {
        setCentrCoordDurations(newCenter, animSpeed, true);
    }


    public void setStraightCenter(MapLocation newCenter, boolean checkCorrectCord) {
        ArrayList<MapLocation> newList = new ArrayList<>();
        if (checkCorrectCord) {
            MapLocation newLocation = getVerifiedCoordinatesAndCheckZone(newCenter, GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalmapImage());
            newList.add(newLocation);
        } else {
            newList.add(newCenter);
        }

        centrCoordsInterpol = newList;
    }

    public void setStraightCenter(MapLocation newCenter) {
        setStraightCenter(newCenter, true);
    }

    // --------------------------------------------------------------------------------------


    public MapLocation getVerifiedCoordinates(MapLocation location, MapLocation distanceAccess, MapLocation deltaCoords, int imageWidth, int imageHeight) {

        double centerX = (double) imageWidth / 2;
        double centerY = (double) imageHeight / 2;

        double DistanceFromTheCenterX = Math.abs(location.getX() - centerX);
        double DistanceFromTheCenterY = Math.abs(location.getY() - centerY);

        double cordX, cordY;

        if (location.getX() > centerX) {
            if (DistanceFromTheCenterX > distanceAccess.getX()) {
                cordX = location.getX() + deltaCoords.getX();
            } else {
                cordX = location.getX();
            }
        } else {
            if (DistanceFromTheCenterX > distanceAccess.getX()) {
                cordX = location.getX() - deltaCoords.getX();
            } else {
                cordX = location.getX();
            }
        }

        if (location.getY() > centerY) {
            if (DistanceFromTheCenterY > distanceAccess.getY()) {
                cordY = location.getY() + deltaCoords.getY();
            } else {
                cordY = location.getY();
            }
        } else {
            if (DistanceFromTheCenterY > distanceAccess.getY()) {
                cordY = location.getY() - deltaCoords.getY();
            } else {
                cordY = location.getY();
            }
        }

        return new MapLocation(cordX, cordY, 7);
    }


    // Для LCM
    public MapLocation getVerifiedCoordinatesLCM(MapLocation location, MapImage mapImage, MapLocation deltaCoords) {

        MapLocation distanceAccess = getDistanceAccess(mapImage);

        return getVerifiedCoordinates(location, distanceAccess, deltaCoords, mapImage.getImageWidth(), mapImage.getImageHeight());
    }

    public MapLocation getVerifiedCoordinates(MapLocation location, MapImage mapImage) {
        MapLocation distanceAccess = getDistanceAccess(mapImage);
        MapLocation deltaCoords = getDeltaCoords(location, mapImage);
        return getVerifiedCoordinates(location, distanceAccess, deltaCoords, mapImage.getImageWidth(), mapImage.getImageHeight());
    }

    public MapLocation getVerifiedCoordinatesAndCheckZone(MapLocation location, MapImage mapImage) {
        MapLocation deltaCoords = getDeltaCoords(location, mapImage);
        if (isAccessZone(deltaCoords)) {
            return location;
        }
        MapLocation distanceAccess = getDistanceAccess(mapImage);
        return getVerifiedCoordinates(location, distanceAccess, deltaCoords, mapImage.getImageWidth(), mapImage.getImageHeight());
    }



        public MapLocation getDistanceAccess(int imageWidth, int imageHeight, int zoom) {
            double distanceAccessX = (imageWidth - zoom*this.zoom.getCoefZoomX()) / 2;
            double distanceAccessY = (imageHeight - zoom*this.zoom.getCoefZoomY()) / 2;
            return new MapLocation(distanceAccessX, distanceAccessY);
    }

    public MapLocation getDistanceAccess(MapImage mapImage, int zoom) {
        return this.getDistanceAccess(mapImage.getImageWidth(), mapImage.getImageHeight(), zoom);
    }

    public MapLocation getDistanceAccess(MapImage mapImage) {
        return this.getDistanceAccess(mapImage.getImageWidth(), mapImage.getImageHeight(), zoom.getFirstZoom());
    }


    /*
    Числа (X : Y), насколько координата вышла из доступной зоны.
     */
    public MapLocation getDeltaCoords(MapLocation location, int imageWidth, int imageHeight, MapLocation distanceAccess) {

        double centerX = (double) imageWidth / 2;
        double centerY = (double) imageHeight / 2;

        double DistanceFromTheCenterX = Math.abs(location.getX() - centerX);
        double DistanceFromTheCenterY = Math.abs(location.getY() - centerY);

        return new MapLocation(distanceAccess.getX() - DistanceFromTheCenterX, distanceAccess.getY() - DistanceFromTheCenterY);
    }


    public MapLocation getDeltaCoords(MapLocation location, int imageWidth, int imageHeight, int zoom) {

        MapLocation distanceAccess = getDistanceAccess(imageWidth, imageHeight, zoom);

        return this.getDeltaCoords(location, imageWidth, imageHeight, distanceAccess);
    }

    public MapLocation getDeltaCoords(MapLocation location, MapImage mapImage, MapLocation distanceAccess) {
        return getDeltaCoords(location, mapImage.getImageWidth(), mapImage.getImageHeight(), distanceAccess);
    }

    public MapLocation getDeltaCoords(MapLocation location, MapImage mapImage, int zoom) {
        return getDeltaCoords(location, mapImage.getImageWidth(), mapImage.getImageHeight(), zoom);
    }

    public MapLocation getDeltaCoords(MapLocation location, MapImage mapImage) {
        return getDeltaCoords(location, mapImage, this.zoom.getFirstZoom());
    }

    public boolean isAccessZone(MapLocation location, MapImage mapImage, int zoom) {
        MapLocation deltaCoords = getDeltaCoords(location, mapImage, zoom);
        return this.isAccessZone(deltaCoords);
    }

    public boolean isAccessZone(MapLocation location, MapImage mapImage) {
        return isAccessZone(location, mapImage, this.zoom.getFirstZoom());
    }

    public boolean isAccessZone(MapLocation deltaCoords) {
        if (deltaCoords.getX() < 0 || deltaCoords.getY() < 0) {
            return false;
        } else {
            return true;
        }
    }


    public double getSpringResistance(double distance, double maxDistance) {
        if (maxDistance == 0) {
            System.out.println("ЗАМЕЧЕНО ДЕЛЕНИЕ НА 0!!!!!!!!");
            maxDistance = 0.5;
        }
        return (MathUtils.fastCos((Math.PI * distance) / maxDistance) + 1) / 2;
    }

//    public double getSpringResistance(MapLocation location, int imageWidth, int imageHeight, int windowWidth, int windowHeight) {
//        double distanceAccessX = (double) (imageWidth - windowWidth) / 2;
//        double distanceAccessY = (double) (imageHeight - windowHeight) / 2;
//
//        double centerX = (double) imageWidth / 2;
//        double centerY = (double) imageHeight / 2;
//
//        double DistanceFromTheCenterX = Math.abs(location.getX() - centerX);
//        double DistanceFromTheCenterY = Math.abs(location.getY() - centerY);
//
//        double deltaX = distanceAccessX - DistanceFromTheCenterX;
//        double deltaY = distanceAccessY - DistanceFromTheCenterY;
//
//        double resistenceX = 1;
//        double resistenceY = 1;
//
//        // @TODO Продумать NotNull. Деление на   maxDepartureX
//
//        if (deltaX < 0) {
//            double maxDepartureX = distanceAccessX / 4;
//            resistenceX = (MathUtils.fastCos((Math.PI * -deltaX) / maxDepartureX) + 1) / 2;
//        }
//        if (deltaY < 0) {
//            double maxDepartureY = distanceAccessY / 4;
//            resistenceY = (MathUtils.fastCos((Math.PI * -deltaY) / maxDepartureY) + 1) / 2;
//        }
//
//        System.out.println("=============\ndeltaX " + deltaX + "\ndeltaY " + deltaY + "\nresistenceX " + resistenceX + "\nresistenceY " + resistenceY);
//        return Math.min(resistenceX, resistenceY);
//    }

    public void setCentrCoordFirstIterpAndClearInterp() {
        ArrayList<MapLocation> newList = new ArrayList<>();
        newList.add(getFirstCoordInter());
        centrCoordsInterpol = newList;
    }

    private void addCentrCoordInterpolation(MapLocation location) {
        centrCoordsInterpol.add(location);
    }

    public MapLocation getFirstCoordInter() {
        return centrCoordsInterpol.get(0);
    }

    public MapLocation getCentreCoordInter() {
        if (centrCoordsInterpol.size() > 1) {
            return centrCoordsInterpol.remove(0);
        } else if (centrCoordsInterpol.size() == 1) {
            return centrCoordsInterpol.get(0);
        } else {
            System.out.println("Всё плохо. Список zoomInterpolations пуст");
            return new MapLocation(0, 0);
        }
    }

    public MapLocation getLastCentreCoord() {
        return centrCoordsInterpol.get(centrCoordsInterpol.size() - 1);
    }


}