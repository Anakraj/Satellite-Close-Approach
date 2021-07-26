package com.example.tests.junit5;

import com.example.orbittracker.NamedTLE;
import com.example.orbittracker.TLEUtil;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

public class ExampleTest {
    @Test
    void eccentricityTest () {
        String[] tlePaths = {"./src/main/resources/celestrak_active.txt", "./src/main/resources/space-track_iridium.txt"};
        ArrayList<NamedTLE> testTLEs;
        testTLEs = TLEUtil.readTLEs(tlePaths);

        for(NamedTLE i : testTLEs) {
            System.out.println(i.TLE().getE());
            Assertions.assertTrue(i.TLE().getE() <= 1.0);
        }
    }

    @Test
    void allOrbits () {
        String[] tlePaths = {"./src/main/resources/celestrak_active.txt"};
        ArrayList<NamedTLE> testTLEs;
        testTLEs = TLEUtil.readTLEs(tlePaths);

        Assertions.assertEquals(testTLEs.size(), 4500);
    }

    @Test
    void success () {
        Assertions.assertEquals(1, 1);
    }

}
