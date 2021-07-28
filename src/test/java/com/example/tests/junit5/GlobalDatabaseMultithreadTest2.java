package com.example.tests.junit5;

import com.example.orbittracker.*;
import org.junit.jupiter.api.Test;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
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

        System.out.println("Up and running");

        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));

        String[] tlePaths = {"./src/main/resources/celestrak_active.txt"};

        ArrayList<NamedTLE> testTLEs;
        ArrayList<OrbitResults> testResults = new ArrayList<>();
        ArrayList<CloseApproachPair> closeApproachPairs = new ArrayList<>();
        final AbsoluteDate startDate = new AbsoluteDate(2002, 5, 7, 12, 0, 0.0, TimeScalesFactory.getUTC());



        testTLEs = TLEUtil.readTLEs(tlePaths, 30);
/*
        //NONMULTITHREADING
        //this is the part that takes the longest
        for(NamedTLE i : testTLEs) {
            testResults.add(new OrbitResults(i, 60.0, 60.0 * 60.0 * 24 * 7, startDate));
        }
        //this is the part that takes the longest
*/

        //MULTITHREADING
        testTLEs.parallelStream().forEach(i -> {
            //System.out.println(i.name());
            //System.out.println(i.TLE().getE());
            try {
                testResults.add(OrbitResults.createOrbitResults(i, 60, 60 * 60 * 24 * 7, startDate));
            }
            catch (Exception e) {
                System.out.println(e);
                System.out.println(i.TLE().getE());
            }
        });



        System.out.println("Done with OrbitResults");
        final long startTime = System.currentTimeMillis();

/*
        for(int n1 = 0; n1 < testResults.size(); n1++) {
            for(int n2 = n1 + 1; n2 < testResults.size(); n2++) {
//                CloseApproachPair temp = Comparisons.testIfClose(testResults.get(n1), testResults.get(n2), 50000.0);
//                if(temp != null) {
//                    closeApproachPairs.add(temp);
//                }
                Optional<CloseApproachPair> temp = Comparisons.testIfApproach(testResults.get(n1),
                        testResults.get(n2),
                        50000.0,
                        startDate,
                        60.0);

                if(temp.isPresent()) {
                    closeApproachPairs.add(temp.get());
                }
            }
        }

 */

//multithread

        IntStream.range(0, testResults.size()).parallel().forEach(n1 -> {
            IntStream.range(n1+1, testResults.size()).parallel().forEach(n2 -> {
//                CloseApproachPair temp = Comparisons.testIfClose(testResults.get(n1), testResults.get(n2), 50000.0);
//                if(temp != null) {
//                    closeApproachPairs.add(temp);
//                }
                Optional<CloseApproachPair> temp = Comparisons.testIfApproach(testResults.get(n1),
                        testResults.get(n2),
                        50000.0,
                        startDate,
                        60.0);

                if(temp.isPresent()) {
                    closeApproachPairs.add(temp.get());
                }
            });
        });

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));

        System.out.println("Done with pairs");
        Comparisons.generateLogs(closeApproachPairs, 5);
        System.out.println("Done with log");

    }
}
