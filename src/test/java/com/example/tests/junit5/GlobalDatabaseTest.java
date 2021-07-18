package com.example.tests.junit5;

import com.example.orbittracker.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class GlobalDatabaseTest {
    @Test
    void useDatabase () throws IOException {
        String[] tlePaths = {"./src/main/resources/celestrak_active.txt"};

        ArrayList<NamedTLE> testTLEs;
        ArrayList<OrbitResults> testResults = new ArrayList<>();
        ArrayList<CloseApproachOrbit> closeApproachOrbits = new ArrayList<>();

        testTLEs = TLEUtil.readTLEs(tlePaths, 20);
        for(NamedTLE i : testTLEs) {
            testResults.add(new OrbitResults(i, 60.0, 60.0 * 60.0 * 24 * 7));
        }


        for(int n1 = 0; n1 < testResults.size(); n1++) {
            for(int n2 = n1 + 1; n2 < testResults.size(); n2++) {
                CloseApproachOrbit temp = Comparisons.testIfClose(testResults.get(n1), testResults.get(n2), 50000.0);
                if(temp != null) {
                    closeApproachOrbits.add(temp);
                }
            }
        }

        Comparisons.generateLogs(closeApproachOrbits);




    }
}
