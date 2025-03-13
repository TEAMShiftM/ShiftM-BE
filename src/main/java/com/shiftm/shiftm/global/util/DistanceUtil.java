package com.shiftm.shiftm.global.util;

public class DistanceUtil {

    private DistanceUtil() { }

    private static final double EARTH_RADIUS_KM = 6371000;

    public static double calculateDistance(final double originLatitude, final double originLongitude, final double destinationLatitude, final double detinationLongitude) {
        final double latitudeDifference = Math.toRadians(destinationLatitude - originLatitude);
        final double longitudeDifference = Math.toRadians(detinationLongitude - originLongitude);
        final double haversinePart = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2) +
                Math.cos(Math.toRadians(originLatitude)) * Math.cos(Math.toRadians(destinationLatitude)) *
                        Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);
        final double centralAngle = 2 * Math.atan2(Math.sqrt(haversinePart), Math.sqrt(1 - haversinePart));

        return EARTH_RADIUS_KM * centralAngle;
    }
}
