package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;
import java.util.Collections;

public class CloseApproachOrbit {

    //2 orbits that are to be compared and analyzed
    OrbitResults a;
    OrbitResults b;

    //PVCoordinates of both orbits
    ArrayList<PVCoordinates> coordsA;
    ArrayList<PVCoordinates> coordsB;

    //distance in meters between both satellites at given intervals
    ArrayList<Double> distances = new ArrayList<Double>();

    double timeOfApproach;
    double distanceAtApproach;

    public CloseApproachOrbit(OrbitResults a, OrbitResults b) {
        this.a = a;
        this.b = b;
        this.coordsA = a.getCoords();
        this.coordsB = b.getCoords();
        generateDistances();

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
        //fills distance arraylist with distances of 2 satellites at different time intervals
        for(int i = 0; i < Math.min(coordsA.size(), coordsB.size()); i++) {
            distances.add(coordsA.get(i).getPosition().distance(coordsB.get(i).getPosition()));
        }
    }

    //finds the closest distance the 2 satellites will be near each other
    public double getClosestDistance() {
        double toRet = Integer.MAX_VALUE * 1.0;
        for(Double v : distances) {
            toRet = Math.min(toRet, v);
        }
        return toRet;
    }


}
