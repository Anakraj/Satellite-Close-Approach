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
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.time.UTCScale;
import com.example.orbittracker.KeplerianOrbitTester;

import java.io.File;


public class Main {
    public static void main(String[] args) {

        //load in the paths of the files containing tles to load in
        String[] tlePaths = {"./src/main/resources/celestrak_active.txt", "./src/main/resources/space-track_iridium.txt"};
        TLEReader tleReader = new TLEReader(tlePaths);

        //get the first 10 entries in each files, and put them into an array
        tleReader.readNumTLEs(10);

        System.out.println(tleReader.getTles()[0]);
        System.out.println(tleReader.getTles().length);

        for(TLE t : tleReader.getTles()) {
            System.out.println(t);
        }
    }
}
