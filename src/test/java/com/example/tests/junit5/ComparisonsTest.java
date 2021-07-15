package com.example.tests.junit5;

import com.example.orbittracker.NamedTLE;
import com.example.orbittracker.TLEUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ComparisonsTest {

    @Test
    void TLEComparisons() {
        String[] tlePaths = {"./src/main/resources/space-track_iridium.txt"};
        ArrayList<NamedTLE> testTLEs;

        testTLEs = TLEUtil.readTLEs(tlePaths, 10);


    }
}
