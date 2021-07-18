package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;
import java.util.Collections;

public class CloseApproachOrbit {
    OrbitResults a;
    OrbitResults b;

    ArrayList<PVCoordinates> coordsA;
    ArrayList<PVCoordinates> coordsB;

    ArrayList<Double> distances = new ArrayList<Double>();

    double timeOfApproach;
    double distanceAtApproach;

    public CloseApproachOrbit(OrbitResults a, OrbitResults b) {
        this.a = a;
        this.b = b;
        this.coordsA = a.getCoords();
        this.coordsB = b.getCoords();
    }

    public CloseApproachOrbit(OrbitResults a, OrbitResults b, double distanceAtApproach) {
        this.a = a;
        this.b = b;
        this.coordsA = a.getCoords();
        this.coordsB = b.getCoords();
        this.distanceAtApproach = distanceAtApproach;
        generateDistances();
    }

    public CloseApproachOrbit(OrbitResults a, OrbitResults b, double timeOfApproach, double distanceAtApproach) {
        this.a = a;
        this.b = b;
        this.coordsA = a.getCoords();
        this.coordsB = b.getCoords();
        this.timeOfApproach = timeOfApproach;
        this.distanceAtApproach = distanceAtApproach;
        generateDistances();
    }

    public OrbitResults getA() {
        return a;
    }

    public OrbitResults getB() {
        return b;
    }

    public void generateDistances() {
        for(int i = 0; i < Math.min(coordsA.size(), coordsB.size()); i++) {
            distances.add(coordsA.get(i).getPosition().distance(coordsB.get(i).getPosition()));
        }
    }

    public double getClosestDistance() {
        double toRet = Integer.MAX_VALUE * 1.0;
        for(Double v : distances) {
            toRet = Math.min(toRet, v);
        }
        return toRet;
    }


}
