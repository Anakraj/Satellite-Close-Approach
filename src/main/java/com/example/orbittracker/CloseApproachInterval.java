package com.example.orbittracker;

import org.hipparchus.analysis.function.Abs;
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

    public double getClosestDistance() {
        return closestDistance;
    }

    public Vector3D getClosestSeparation() {
        return closestSeparation;
    }

    public AbsoluteDate getClosestDistanceDate() {
        return closestDistanceDate;
    }

    public AbsoluteDate getStartDate() {
        return startDate;
    }

    public AbsoluteDate getEndDate() {
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
        double minDist = original.get(startIndex).getDistance();
        Vector3D tempClosestSeparation = original.get(startIndex).getDistanceXYZ();
        AbsoluteDate tempAbsoluteDate = original.get(startIndex).getTime();

        for(int i = startIndex; i <= lastIndex; i++) {
            OrbitPoint point = original.get(i);
            if(point.getDistance() < minDist) {
                minDist = point.getDistance();
                tempClosestSeparation = point.getDistanceXYZ();
                tempAbsoluteDate = point.getTime();
            }
        }

        AbsoluteDate startDate = original.get(startIndex).getTime();
        AbsoluteDate endDate = original.get(lastIndex).getTime();

        return new CloseApproachInterval(minDist, tempAbsoluteDate, tempClosestSeparation, startDate, endDate);
    }
}
