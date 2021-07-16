package com.example.orbittracker;

import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;

public class CloseApproachOrbit {
    OrbitResults a;
    OrbitResults b;

    ArrayList<PVCoordinates> coordsA;
    ArrayList<PVCoordinates> coordsB;

    public CloseApproachOrbit(OrbitResults a, OrbitResults b) {
        this.a = a;
        this.b = b;
        this.coordsA = a.getCoords();
        this.coordsB = b.getCoords();
    }

    public OrbitResults getA() {
        return a;
    }

    public OrbitResults getB() {
        return b;
    }


}
