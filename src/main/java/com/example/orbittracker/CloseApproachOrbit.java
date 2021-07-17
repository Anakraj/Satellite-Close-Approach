package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;

public class CloseApproachOrbit {
    OrbitResults a;
    OrbitResults b;

    ArrayList<PVCoordinates> coordsA;
    ArrayList<PVCoordinates> coordsB;

    ArrayList<Vector3D> distances;

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
    }

    public CloseApproachOrbit(OrbitResults a, OrbitResults b, double timeOfApproach, double distanceAtApproach) {
        this.a = a;
        this.b = b;
        this.coordsA = a.getCoords();
        this.coordsB = b.getCoords();
        this.timeOfApproach = timeOfApproach;
        this.distanceAtApproach = distanceAtApproach;
    }

    public OrbitResults getA() {
        return a;
    }

    public OrbitResults getB() {
        return b;
    }

    public void generateDistances() {
        for(int i = 0; i < coordsA.size(); i++) {
            Vector3D distance = coordsA.get(i).getPosition().subtract(coordsB.get(i).getPosition());
            distances.add(distance);
        }
    }
}
