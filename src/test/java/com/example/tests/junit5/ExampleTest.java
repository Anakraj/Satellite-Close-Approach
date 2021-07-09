package com.example.tests;

import com.example.orbittracker.NamedTLE;
import com.example.orbittracker.TLEUtil;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

public class ExampleTest {
    @Test
    void firstTenOrbits () {
        String[] tlePaths = {"./src/main/resources/celestrak_active.txt", "./src/main/resources/space-track_iridium.txt"};
        ArrayList<NamedTLE> testTLEs;

        testTLEs = TLEUtil.readTLEs(tlePaths, 10);

        for(NamedTLE i : testTLEs) {
            System.out.println(i);
        }
    }

    @Test
    void fail () {
        Assertions.assertEquals(1, 1);
    }
}
