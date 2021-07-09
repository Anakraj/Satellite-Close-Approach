package com.example.orbittracker;

import org.orekit.propagation.analytical.tle.TLE;

import java.util.*;

public class NamedTLE {

    private final TLE tle;
    private final String name;

    public NamedTLE(TLE tle, String name) {
        this.tle = tle;
        this.name = name;
    }

    public TLE getTle() {
        return tle;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "\n" + tle;
    }
}
