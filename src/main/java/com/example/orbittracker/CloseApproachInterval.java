package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.time.AbsoluteDate;

import java.util.ArrayList;

//class that contains the information about a close approach

public class CloseApproachInterval {
    //indexes on the original list of points
    private final double closestDistance;
    private final Vector3D closestSeparation;
    private final AbsoluteDate closestDistanceDate;

    private final AbsoluteDate startDate;
    private final AbsoluteDate endDate;

    public double closestDistance() { return closestDistance; }

    public Vector3D closestSeparation() {
        return closestSeparation;
    }

    public AbsoluteDate closestDistanceDate() {
        return closestDistanceDate;
    }

    public AbsoluteDate startDate() {
        return startDate;
    }

    public AbsoluteDate endDate() {
        return endDate;
    }


    public CloseApproachInterval(double closestDistance, AbsoluteDate closestDistanceDate, Vector3D closestSeparation, AbsoluteDate startDate, AbsoluteDate endDate) {
        this.closestDistance = closestDistance;
        this.closestDistanceDate = closestDistanceDate;
        this.closestSeparation = closestSeparation;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static CloseApproachInterval createCloseApproachInterval(int startIndex, int lastIndex, ArrayList<OrbitPoint> original) {
        double minDist = original.get(startIndex).distance();
        Vector3D tempClosestSeparation = original.get(startIndex).distanceVector3();
        AbsoluteDate tempAbsoluteDate = original.get(startIndex).time();

        for(int i = startIndex; i <= lastIndex; i++) {
            OrbitPoint point = original.get(i);
            if(point.distance() < minDist) {
                minDist = point.distance();
                tempClosestSeparation = point.distanceVector3();
                tempAbsoluteDate = point.time();
            }
        }

        AbsoluteDate startDate = original.get(startIndex).time();
        AbsoluteDate endDate = original.get(lastIndex).time();

        return new CloseApproachInterval(minDist, tempAbsoluteDate, tempClosestSeparation, startDate, endDate);
    }
}
