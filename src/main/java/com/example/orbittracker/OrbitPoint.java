package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.time.AbsoluteDate;

public class OrbitPoint {

    //data class for holding data relevant to later orbit analysis
    private final Vector3D distanceXYZ;
    private final double distance;
    private final AbsoluteDate time;

    public OrbitPoint(Vector3D distanceXYZ, double distance, AbsoluteDate time) {
        this.distanceXYZ = distanceXYZ;
        this.distance = distance;
        this.time = time;
    }

    public Vector3D distanceVector3() {
        return distanceXYZ;
    }

    public double distance() {
        return distance;
    }

    public AbsoluteDate time() {
        return time;
    }
}
