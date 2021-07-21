package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.time.AbsoluteDate;

public class OrbitPoint {

    //data class for holding data relevant to later orbit analysis
    Vector3D distanceXYZ;
    double distance;
    AbsoluteDate time;

    public OrbitPoint(Vector3D distanceXYZ, double distance, AbsoluteDate time) {
        this.distanceXYZ = distanceXYZ;
        this.distance = distance;
        this.time = time;
    }

    public Vector3D getDistanceXYZ() {
        return distanceXYZ;
    }

    public double getDistance() {
        return distance;
    }

    public AbsoluteDate getTime() {
        return time;
    }
}
