package com.example.orbittracker;

import org.hipparchus.analysis.function.Abs;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.time.AbsoluteDate;

import java.util.ArrayList;

//class that contains the information about a close approach

public class CloseApproachInterval {
    //indexes on the original list of points
    private int startIndex;
    private int lastIndex;

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

    private double closestDistance;
    private Vector3D closestSeparation;
    private AbsoluteDate closestDistanceDate;

    private AbsoluteDate startDate;
    private AbsoluteDate endDate;

    ArrayList<OrbitPoint> original;

    public CloseApproachInterval(int startIndex, int lastIndex, ArrayList<OrbitPoint> original) {
        this.startIndex = startIndex;
        this.lastIndex = lastIndex;
        this.original = original;
        this.startDate = original.get(0).getTime();
        this.endDate = original.get(original.size() - 1).getTime();
        generateClosestDistances();
    }

    void generateClosestDistances() {

        //boils down to simple linear search for minimum distance

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

        this.closestDistance = minDist;
        this.closestDistanceDate = tempAbsoluteDate;
        this.closestSeparation = tempClosestSeparation;
    }
}
