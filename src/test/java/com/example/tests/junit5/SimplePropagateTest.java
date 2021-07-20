package com.example.tests.junit5;

import com.example.orbittracker.Comparisons;
import com.example.orbittracker.NamedTLE;
import com.example.orbittracker.OrbitResults;
import com.example.orbittracker.TLEUtil;
import org.hipparchus.analysis.function.Abs;
import org.junit.jupiter.api.Test;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
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
        OrbitResults a = new OrbitResults(testTLEs.get(0), interval, duration, new AbsoluteDate(2002, 5, 7, 12, 0, 0.0, TimeScalesFactory.getUTC()));

        System.out.println(a.getApogee());
        System.out.println(a.getPerigee());
        System.out.println(a.getA());

        for(PVCoordinates coords : a.getCoords()) {
            System.out.println(coords.getAngularVelocity());
        }
    }

    @Test
    void compareTwo() {

        double interval = 60.0;
        double duration = 60 * 60 * 24 * 7 * 1.0;
        final AbsoluteDate startDate = new AbsoluteDate(2002, 5, 7, 12, 0, 0.0, TimeScalesFactory.getUTC());

        String[] tlePaths = {"./src/main/resources/space-track_iridium.txt"};
        ArrayList<NamedTLE> testTLEs;

        testTLEs = TLEUtil.readTLEs(tlePaths, 2);

        System.out.println("Done with test TLEs");

        OrbitResults a = new OrbitResults(testTLEs.get(0), interval, duration, startDate);
        OrbitResults b = new OrbitResults(testTLEs.get(1), interval, duration, startDate);

        System.out.println("Done with OrbitResults");

//        System.out.println(Comparisons.testIfClose(a, b, 5.0 * 1000));
//        System.out.println(Comparisons.testIfClose(a, b, 10.0 * 1000));
//        System.out.println(Comparisons.testIfClose(a, b, 100.0 * 1000));
//        System.out.println(Comparisons.testIfClose(a, b, 1000.0 * 1000));
//        System.out.println(Comparisons.testIfClose(a, b, 10000.0 * 1000));

        System.out.println("Done with tests");
    }
}
