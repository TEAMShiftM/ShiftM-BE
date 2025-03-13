package com.shiftm.shiftm.infra.location;

import org.springframework.stereotype.Component;

import static java.lang.StrictMath.*;
import static org.apache.commons.math3.util.FastMath.atan2;

@Component
public class ProximityCalculator {

    private static final double EARTH_RADIUS = 6371000; // 지구 반지름 (미터)

    public boolean isWithinDistance(final Double originLatitude, final Double originLongitude, final Double destinationLatitude, final Double destinationLongitude, final Double distanceThreshold) {
        final Double latitudeDifference = toRadians(destinationLatitude - originLatitude);
        final Double longitudeDifference = toRadians(destinationLongitude - originLongitude);
        final Double haversinePart = pow(sin(latitudeDifference / 2), 2) + cos(toRadians(originLatitude)) * cos(toRadians(destinationLatitude)) * pow(sin(longitudeDifference / 2), 2);
        final Double centralAngle = 2 * atan2(sqrt(haversinePart), sqrt(1 - haversinePart));
        final Double distance = EARTH_RADIUS * centralAngle;
        return distance <= distanceThreshold;
    }
}
