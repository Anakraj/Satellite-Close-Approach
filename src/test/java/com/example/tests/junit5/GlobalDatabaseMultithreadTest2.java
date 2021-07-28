package com.example.tests.junit5;

import com.example.orbittracker.*;
import org.junit.jupiter.api.Test;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

//execution time for streams is 111380
//execution time for regular is 111128
//execution time for parallel is 62249

//day 2
//execution time is 42058 for stream.parallel
//execution time for parallelStreams is 34307

//nested for loop
//execution time for multithread is 74
//execution time for no multithread is 111
public class GlobalDatabaseMultithreadTest2 {
    @Test
    void useDatabase () throws IOException {


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

        thresholdInMeters = InputParser.parseThresholdInMeters(p.getProperty("threshold"));
        intervalInSeconds = InputParser.parseIntervalInSeconds(p.getProperty("interval"));
        durationInSeconds = InputParser.parseDurationInSeconds(p.getProperty("duration"), p.getProperty("durationUnit"));
        startDate = InputParser.parseStartDate(p.getProperty("startDate"));


        System.out.println("Up and running");

        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));



        String[] tlePaths = {"./src/main/resources/celestrak_active.txt"};


        ArrayList<NamedTLE> testTLEs;
        ArrayList<OrbitResults> testResults = new ArrayList<>();
        ArrayList<CloseApproachPair> closeApproachPairs = new ArrayList<>();

        //determines number of testTLEs
        testTLEs = TLEUtil.readTLEs(tlePaths, 20);

        //this is the part that takes the longest
        testTLEs.parallelStream().forEach(i -> {
            //System.out.println(i.name());
            //System.out.println(i.TLE().getE());
            try {
                testResults.add(OrbitResults.createOrbitResults(i,intervalInSeconds,durationInSeconds,startDate));
            }
            catch (Exception e) {
                System.out.println(e);
                System.out.println(i.TLE().getE());
            }
        });
        //this is the part that takes the longest

        System.out.println("Done with OrbitResults");

        IntStream.range(0, testResults.size()).parallel().forEach(n1 -> {
            IntStream.range(n1+1, testResults.size()).parallel().forEach(n2 -> {
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
            });
        });

        System.out.println("Done with pairs");
        try {
            Comparisons.generateLogs(closeApproachPairs, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done with log");

        CloseApproachInterval test = closeApproachPairs.get(0).getIntervals().get(0);
        ArrayList<OrbitPoint> testPoints = Comparisons.generateDetailedApproach(closeApproachPairs.get(0), test, 1, intervalInSeconds);

        for(OrbitPoint point : testPoints) {
            System.out.println(point);
        }

//        System.out.println(testPoints.closestDistance());
//        System.out.println(testPoints.closestDistanceDate());


    }
}
