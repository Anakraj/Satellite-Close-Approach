package com.example.tests.junit5;

import com.example.orbittracker.Comparisons;
import com.example.orbittracker.NamedTLE;
import com.example.orbittracker.OrbitResults;
import com.example.orbittracker.TLEUtil;
import org.junit.jupiter.api.Test;
import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;

public class SimplePropagateTest {


    @Test
    void omegaTest() {
        String[] tlePaths = {"./src/main/resources/space-track_iridium.txt"};
        ArrayList<NamedTLE> testTLEs;

        double interval = 60.0;
        double duration = 60 * 60;



        testTLEs = TLEUtil.readTLEs(tlePaths, 1);
        OrbitResults a = new OrbitResults(testTLEs.get(0), interval, duration);

        System.out.println(a.getApogee());
        System.out.println(a.getPerigee());
        System.out.println(a.getA());

        for(PVCoordinates coords : a.getCoords()) {
            System.out.println(coords.getAngularVelocity());
        }
    }

    @Test
    void compareTwo() {

        double testBuffer = 5.0;
        double interval = 60.0;
        double duration = 60 * 60 * 24 * 7 * 1.0;

        String[] tlePaths = {"./src/main/resources/space-track_iridium.txt"};
        ArrayList<NamedTLE> testTLEs;

        testTLEs = TLEUtil.readTLEs(tlePaths, 2);

        System.out.println("Done with test TLEs");

        OrbitResults a = new OrbitResults(testTLEs.get(0), interval, duration);
        OrbitResults b = new OrbitResults(testTLEs.get(1), interval, duration);

        System.out.println("Done with OrbitResults");

        System.out.println(Comparisons.propagationTest(a, b, 5.0));
        System.out.println(Comparisons.propagationTest(a, b, 10.0));
        System.out.println(Comparisons.propagationTest(a, b, 100.0));
        System.out.println(Comparisons.propagationTest(a, b, 1000.0));
        System.out.println(Comparisons.propagationTest(a, b, 10000.0));

        System.out.println("Done with tests");
    }
}
