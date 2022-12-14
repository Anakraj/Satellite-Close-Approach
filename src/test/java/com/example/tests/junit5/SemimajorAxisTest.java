package com.example.tests.junit5;

import com.example.orbittracker.*;
import org.junit.jupiter.api.Test;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import java.io.File;
import java.util.ArrayList;

public class SemimajorAxisTest {
    @Test
    public void a1Test () {
        //This is the actual Iridium 7 data: https://www.n2yo.com/satellite/?s=24793
        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));


        ArrayList<NamedTLE> testTLEs;
        ArrayList<OrbitResults> testResults = new ArrayList<>();
        ArrayList<CloseApproachPair> closeApproachPairs = new ArrayList<>();
        final AbsoluteDate startDate = new AbsoluteDate(2020, 5, 7, 12, 0, 0.0, TimeScalesFactory.getUTC());

        String[] tlePaths = {"./src/main/resources/space-track_iridium.txt"};

        testTLEs = TLEUtil.readTLEs(tlePaths);
        for(NamedTLE i : testTLEs) {
            testResults.add(OrbitResults.createOrbitResults(i, 60.0, 60.0 * 60.0 * 24 * 7, startDate));
        }

        for(OrbitResults i : testResults) {
            System.out.println(i.name());
            System.out.println(i.semiMajorAxis());
            System.out.println(i.apogee());
            System.out.println(i.perigee());
            System.out.println(i.TLE().getE());
        }

    }

    @Test
    public void a2Test () {
        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));


        ArrayList<NamedTLE> testTLEs;
        ArrayList<OrbitResults> testResults = new ArrayList<>();
        ArrayList<CloseApproachPair> closeApproachPairs = new ArrayList<>();
        final AbsoluteDate startDate = new AbsoluteDate(2020, 5, 7, 12, 0, 0.0, TimeScalesFactory.getUTC());

        String[] tlePaths = {"./src/main/resources/celestrak_active.txt"};

        testTLEs = TLEUtil.readTLEs(tlePaths, 20);
        for(NamedTLE i : testTLEs) {
            testResults.add(OrbitResults.createOrbitResults(i, 60.0, 60.0 * 60.0 * 24 * 7, startDate));
        }

        for(OrbitResults i : testResults) {
            System.out.println(i.name());
            System.out.println(i.semiMajorAxis());
            System.out.println(i.apogee());
            System.out.println(i.perigee());
            System.out.println(i.TLE().getE());
        }
    }
}
