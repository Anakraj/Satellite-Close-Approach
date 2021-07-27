package com.example.tests.junit5;

import com.example.orbittracker.InputParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import java.io.File;

public class InputParserTest {
    @Test
    public void durationTest() {
        Assertions.assertEquals(3600 * 24 * 7 * 1.0, InputParser.parseDurationInSeconds("7","days"));
        Assertions.assertEquals( 50 * 60,InputParser.parseDurationInSeconds("50", "min"));
        Assertions.assertEquals( 100, InputParser.parseDurationInSeconds("100", "grams"));
    }

    @Test
    public void dateTest() {
        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));

        AbsoluteDate date1a = new AbsoluteDate(2002, 5, 7, 12, 0, 0.0, TimeScalesFactory.getUTC());
        AbsoluteDate date1b = InputParser.parseStartDate("2002-05-07-12");
        Assertions.assertTrue(date1a.equals(date1b));

        AbsoluteDate date2a = new AbsoluteDate(2021, 7, 26, 21, 53, 58.0, TimeScalesFactory.getUTC());
        AbsoluteDate date2b = InputParser.parseStartDate("2021-07-26-21-53-58");
        Assertions.assertTrue(date2a.equals(date2b));
    }
}
