package com.example.orbittracker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.hipparchus.util.FastMath;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.time.UTCScale;
import com.example.orbittracker.KeplerianOrbitTester;

public class Main {
    public static void main(String[] args) {
        KeplerianOrbitTester orbitTester = new KeplerianOrbitTester();
        orbitTester.propagateKeplerOrbit();
    }
}
