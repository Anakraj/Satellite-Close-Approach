package com.example.orbittracker;

public class CloseApproachOrbit {
    OrbitResults a;
    OrbitResults b;

    public CloseApproachOrbit(OrbitResults a, OrbitResults b) {
        this.a = a;
        this.b = b;
    }

    public OrbitResults getA() {
        return a;
    }

    public OrbitResults getB() {
        return b;
    }
}
