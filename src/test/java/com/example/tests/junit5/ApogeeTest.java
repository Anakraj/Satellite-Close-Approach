package com.example.tests.junit5;

import com.example.orbittracker.Comparisons;
import com.example.orbittracker.NamedTLE;
import com.example.orbittracker.TLEUtil;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.*;


public class ApogeeTest {

    //should get false when satellite 2 is at a much larger orbit than satellite 1
    @Test
    public void testapogeeTest1() {
        Assertions.assertEquals(0, Comparisons.apogeeTest(100,30,200,101, 0) ? 1 : 0);
    }

    //should get false when satellite 2 is at a much smaller orbit than satellite 1
    @Test
    public void testapogeeTest2() {
        Assertions.assertEquals(0, Comparisons.apogeeTest(100,30,29,2, 0) ? 1 : 0);
    }

    //should get true when satellite 2 is at a similar orbit than satellite 1
    @Test
    public void testapogeeTest3() {
        Assertions.assertEquals(1, Comparisons.apogeeTest(100,30,50,12, 0) ? 1 : 0);
    }
}