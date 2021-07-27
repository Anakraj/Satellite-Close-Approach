package com.example.orbittracker;

import org.hipparchus.analysis.function.Abs;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import java.io.File;

public class InputParser {
    public static double parseThresholdInMeters(String input) {
        return Double.parseDouble(input);
    }

    public static double parseIntervalInSeconds(String input) {
        return Double.parseDouble(input);
    }

    public static double parseDurationInSeconds(String qty, String unit) {
        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));

        double q = Double.parseDouble(qty);

        if(unit.equals("s") || unit.equals("seconds")) {
            //do nothing
        }
        else if(unit.equals("min") || unit.equals("minutes")) {
            q *= 60;
        }
        else if(unit.equals("h") || unit.equals("hours")) {
            q *= 60 * 60;
        }
        else if(unit.equals("d") || unit.equals("days")) {
            q *= 60 * 60 * 24;
        }
        else {
            System.out.println("Unknown time unit, defaulting to seconds");
        }

        return q;
    }

    public static AbsoluteDate parseStartDate(String input) {

        String[] arr = input.split("-");
        Integer[] d = {0,0,0,0,0,0};

        for(int i = 0; i < arr.length; i++) {
            d[i] = Integer.parseInt(arr[i]);
        }

        AbsoluteDate toRet = new AbsoluteDate(d[0], d[1], d[2], d[3], d[4], d[5], TimeScalesFactory.getUTC());
        return toRet;
    }
}
