package com.example.orbittracker;

import org.orekit.propagation.analytical.tle.TLE;

@Deprecated
public class CloseApproach {

    NamedTLE a;
    NamedTLE b;

    public CloseApproach(NamedTLE a, NamedTLE b) {
        this.a = a;
        this.b = b;
    }

    public NamedTLE getA() {
        return a;
    }

    public NamedTLE getB() {
        return b;
    }
}
