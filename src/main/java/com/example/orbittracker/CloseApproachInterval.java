package com.example.orbittracker;

import org.hipparchus.analysis.function.Abs;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.time.AbsoluteDate;

import java.util.ArrayList;

public class CloseApproachInterval {
    int startIndex;
    int lastIndex;

    double closestDistance;
    Vector3D closestSeparation;
    AbsoluteDate closestDistanceDate;

    AbsoluteDate startDate;
    AbsoluteDate endDate;

    ArrayList<OrbitPoint> original;

    public CloseApproachInterval(int startIndex, int lastIndex, ArrayList<OrbitPoint> original) {
        this.startIndex = startIndex;
        this.lastIndex = lastIndex;
        this.original = original;
        generateClosestDistances();
    }

    void generateClosestDistances() {
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
