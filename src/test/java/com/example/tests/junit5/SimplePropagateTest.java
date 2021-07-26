package com.example.tests.junit5;

import com.example.orbittracker.Comparisons;
import com.example.orbittracker.NamedTLE;
import com.example.orbittracker.OrbitResults;
import com.example.orbittracker.TLEUtil;
import org.junit.jupiter.api.Test;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;

import java.io.File;
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

        System.out.println(a.apogee());
        System.out.println(a.perigee());
        System.out.println(a.semimajorAxis());

        for(PVCoordinates coords : a.coords()) {
            System.out.println(coords.getAngularVelocity());
        }
    }

    @Test
    void compareTwo() {

        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));

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

        System.out.println(Comparisons.testIfApproach(a, b, 10.0 * 1000, startDate, 60.0));
        System.out.println(Comparisons.testIfApproach(a, b, 100.0 * 1000, startDate, 60.0));
        System.out.println(Comparisons.testIfApproach(a, b, 1000.0 * 1000, startDate, 60.0));
        System.out.println(Comparisons.testIfApproach(a, b, 10000.0 * 1000, startDate, 60.0));

        System.out.println("Done with tests");
    }
}
