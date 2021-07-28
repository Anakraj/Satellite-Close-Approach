package com.example.orbittracker;


import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {

        /*
        * arg 0: threshold in meters
        * arg 1: interval in seconds
        * arg 2: duration
        * arg 3: duration units
        * arg 4: startDate in yy-mm-dd or yy-mm-dd-hh-mm-ss
        * */

        //command line expects either no parameters or all 5 parameters, if no parameters, previously used parameters will be used
        double thresholdInMeters;
        double intervalInSeconds;
        double durationInSeconds;
        AbsoluteDate startDate;

        FileReader reader = new FileReader("./src/main/resources/db.properties");
        Properties p = new Properties();
        p.load(reader);

        if(args.length == 5) {
            thresholdInMeters = InputParser.parseThresholdInMeters(args[0]);
            intervalInSeconds = InputParser.parseIntervalInSeconds(args[1]);
            durationInSeconds = InputParser.parseDurationInSeconds(args[2], args[3]);
            startDate = InputParser.parseStartDate(args[4]);

            p.setProperty("threshold", args[0]);
            p.setProperty("interval", args[1]);
            p.setProperty("duration", args[2]);
            p.setProperty("durationUnit", args[3]);
            p.setProperty("startDate", args[4]);

            p.store(new FileWriter("./src/main/resources/db.properties"), "Configurable inputs for orbit propagation");

        }
        else if(args.length == 0) {
            thresholdInMeters = InputParser.parseThresholdInMeters(p.getProperty("threshold"));
            intervalInSeconds = InputParser.parseIntervalInSeconds(p.getProperty("interval"));
            durationInSeconds = InputParser.parseDurationInSeconds(p.getProperty("duration"), p.getProperty("durationUnit"));
            startDate = InputParser.parseStartDate(p.getProperty("startDate"));
        }
        else {
            throw new RuntimeException("Improper number of parameters");
        }


        System.out.println("Up and running");

        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));



        String[] tlePaths = {"./src/main/resources/celestrak_active.txt"};


        ArrayList<NamedTLE> testTLEs;
        ArrayList<OrbitResults> testResults = new ArrayList<>();
        ArrayList<CloseApproachPair> closeApproachPairs = new ArrayList<>();


        testTLEs = TLEUtil.readTLEs(tlePaths, 20);

        //this is the part that takes the longest
        for(NamedTLE i : testTLEs) {
            try {
                testResults.add(OrbitResults.createOrbitResults(i, intervalInSeconds, durationInSeconds, startDate));
            }
            catch (Exception e) {
                System.out.println(e);
                System.out.println(i.TLE().getE());
            }
        }
        //this is the part that takes the longest

        System.out.println("Done with OrbitResults");

        for(int n1 = 0; n1 < testResults.size(); n1++) {
            for(int n2 = n1 + 1; n2 < testResults.size(); n2++) {
//                CloseApproachPair temp = Comparisons.testIfClose(testResults.get(n1), testResults.get(n2), 50000.0);
//                if(temp != null) {
//                    closeApproachPairs.add(temp);
//                }
                Optional<CloseApproachPair> temp = Comparisons.testIfApproach(testResults.get(n1),
                        testResults.get(n2),
                        thresholdInMeters,
                        startDate,
                        intervalInSeconds);

                if(temp.isPresent()) {
                    closeApproachPairs.add(temp.get());
                }
            }
        }

        System.out.println("Done with pairs");
        try {
            Comparisons.generateLogs(closeApproachPairs, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done with log");
    }

}
