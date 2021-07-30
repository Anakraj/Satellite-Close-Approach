package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Comparisons {

    //sees if we need to compare rockets based on apogee and perigee
    private static final int TIME_PERIOD = 7;

    public static boolean apogeeTest(double apogee1, double perigee1, double apogee2, double perigee2, double buffer){
        //test1 sees if satellite 1 is always farther out than satellite 2
        boolean test1 = false;
        if (perigee1 > apogee2 + buffer){
            test1 = true;
        }

        //test2 sees if satellite 1 is always closer in than satellite 2
        boolean test2 = false;
        if (apogee1 < perigee2 - buffer){
            test2 = true;
        }

        //if either satellite 1 is always farther out or closer in than satellite 2, no need to compare
        // so return false
        if(test1 || test2){
            return false;
        }
        //if both tests fail, the satellites should be compared
        return true;
    }

    private static boolean angularSpeedTest(double anomly1, double angularSpeed1, double anomly2, double angularSpeed2){
        //the relative position between the two satellites is the difference between the anomalies
        double relativePosition = anomly1 - anomly2;
        //the relative speed between the two satellites is the difference in speed
        double relativeSpeed = angularSpeed1 - angularSpeed2;

        //case 1: relativePosition is decreasing
        //this is when both relative position and relative speed are the same sign
        if(relativePosition*relativeSpeed>0){
            //time until intersection
            double timeInSec = relativePosition/relativeSpeed;
            double timeInDays = timeInSec/3600/24;

            //if the time is greater than 7 days no need to check orbits
            if(timeInDays > TIME_PERIOD) {
                return false;
            }else{
                return true;
            }
        }else {
            //case 2: relativePosition is increasing
            //this is when the relative position and relative speed are different signs

            //case A: the relativePosition is negative and relativeVelocity is positive
            if(relativePosition < 0) {

                relativePosition += 2*Math.PI;
                //time until intersection
                double timeInSec = relativePosition / relativeSpeed;
                double timeInDays = timeInSec/3600/24;

                //if the time is greater than 7 days no need to check orbits
                if(timeInDays > TIME_PERIOD) {
                    return false;
                }else{
                    return true;
                }
            }else {
                //case B: the relativePosition is positive and relativeVelocity is negative
                relativePosition -= 2*Math.PI;
                //time until intersection
                double timeInSec = relativePosition / relativeSpeed;
                double timeInDays = timeInSec/3600/24;

                //if the time is greater than 7 days no need to check orbits
                if(timeInDays > TIME_PERIOD) {
                    return false;
                }else{
                    return true;
                }
            }
        }
    }

    public static ArrayList<OrbitPoint> calculateDistanceData(OrbitResults a, OrbitResults b, AbsoluteDate startDate, double intervalInSeconds) {
        //array list of data points, one for each "frame"
        ArrayList<OrbitPoint> points = new ArrayList<>();

        //data for both satellites
        ArrayList<PVCoordinates> aCoords = a.coords();
        ArrayList<PVCoordinates> bCoords = b.coords();

        //begin generating the data
        AbsoluteDate tempDate = startDate;

        for(int i = 0; i < a.coords().size(); i++) {

            //3 parameters
            double distance = aCoords.get(i).getPosition().distance(bCoords.get(i).getPosition());
            Vector3D separation = aCoords.get(i).getPosition().subtract(bCoords.get(i).getPosition());

            //add to list of points
            points.add(new OrbitPoint(separation, distance, tempDate));
            tempDate = tempDate.shiftedBy(intervalInSeconds);
        }

        return points;
    }

    public static Optional<CloseApproachPair> testIfApproach(OrbitResults a, OrbitResults b, double bufferMeters, AbsoluteDate startDate, double internvalInSeconds) {
        //test perigee and apogee, if orbit is sufficiently far enough, there will be no close approach
        if(!apogeeTest(a.apogee(), a.perigee(), b.apogee(), b.perigee(), bufferMeters)) {
            return Optional.empty();
        }
//        //consider getting rid of since very situational and doesn't consider buffer
//        if(angularSpeedTest(a.getTrueAnomaly(), a.getAvgAngularSpeed(), b.getTrueAnomaly(), b.getAvgAngularSpeed())) {
//            return Optional.empty();
//        }

        //get the results
        ArrayList<OrbitPoint> results = calculateDistanceData(a, b, startDate, internvalInSeconds);

        //check if at any points the distance between 2 satellites crosses the threshold
        for(OrbitPoint point : results) {
            if(point.distance() <= bufferMeters) {
                return Optional.of(CloseApproachPair.createCloseApproachPair(a, b, results, bufferMeters));
            }
        }

        return Optional.empty();
    }

    //write out a demo log file, currently only has names of satellites and closest approach

    public static void generateLogs(ArrayList<CloseApproachPair> approaches) throws IOException {
        PrintWriter writer = new PrintWriter("orbit_out.txt");

        writer.format("Log generated at time %s \n\n", LocalDateTime.now());

        for(CloseApproachPair i : approaches) {
            writer.write("Close approach(es) between " + i.resultsA().name().strip() + " and " + i.resultsB().name().strip() + ".");
            writer.write("\n");

            for(int jj = 0; jj < i.getIntervals().size(); jj++) {
                CloseApproachInterval interval = i.getIntervals().get(jj);
                writer.format("Close approach from %s to %s", interval.startDate().toString(), interval.endDate().toString());
                writer.write("\n");
                writer.format("Closest distance is %f, occurs at time %s", interval.closestDistance(), interval.closestDistanceDate().toString());
                writer.write("\n");
                writer.format("At this time, position of A compared to B is:" + interval.closestSeparation().toString());
                writer.write("\n");
                writer.write("---\n");
            }
            writer.write("\n");
            writer.write("\n");
        }
        writer.close();
    }

    public static String generateEntry(CloseApproachPair i, int maxCloseApproaches) {
        String toRet = "";
        ArrayList<CloseApproachInterval> intervals = i.getIntervals();
        int len = Math.min(intervals.size(), maxCloseApproaches);

        toRet += "First " + len + " Close approach(es) between " + i.resultsA().name().strip() + " and " + i.resultsB().name().strip() + ".";
        toRet += "\n";


        toRet += "---\n";
        for(int jj = 0; jj < len; jj++) {
            CloseApproachInterval interval = intervals.get(jj);
            toRet += String.format("Close approach from %s to %s \n", interval.startDate().toString(), interval.endDate().toString());
            toRet += String.format("Closest distance is %f, occurs at time %s \n", interval.closestDistance(), interval.closestDistanceDate().toString());
            toRet += String.format("At this time, position of A compared to B is: \n" + interval.closestSeparation().toString());
            toRet += "\n";
        }
        toRet += "---\n\n";

        return toRet;
    }

    public static void generateLogs(ArrayList<CloseApproachPair> pairs, int maxCloseApproaches) throws IOException {
        PrintWriter writer = new PrintWriter("orbit_out.txt");


        for(int j = 0; j < pairs.size(); j++) {

            CloseApproachPair i = pairs.get(j);

            ArrayList<CloseApproachInterval> intervals = i.getIntervals();
            int len = Math.min(intervals.size(), maxCloseApproaches);

            writer.write("First " + len + " Close approach(es) between " + i.resultsA().name().strip() + " and " + i.resultsB().name().strip() + ".");
            writer.write("\n");



            for(int jj = 0; jj < len; jj++) {
                CloseApproachInterval interval = intervals.get(jj);
                writer.format("Close approach from %s to %s", interval.startDate().toString(), interval.endDate().toString());
                writer.write("\n");
                writer.format("Closest distance is %f, occurs at time %s", interval.closestDistance(), interval.closestDistanceDate().toString());
                writer.write("\n");
                writer.format("At this time, position of A compared to B is:" + interval.closestSeparation().toString());
                writer.write("\n");
                writer.write("---\n");
            }
            writer.write("\n");
            writer.write("\n");
        }
        writer.close();
    }

    public static ArrayList<OrbitPoint> generateDetailedApproach(CloseApproachPair pair, CloseApproachInterval interval, double gap, double cushion) {
        NamedTLE namedA = pair.resultsA().namedTLE();
        NamedTLE namedB = pair.resultsB().namedTLE();

        double duration = interval.endDate().shiftedBy(cushion).offsetFrom(interval.startDate().shiftedBy(-cushion), TimeScalesFactory.getUTC());

        System.out.println(duration);

        OrbitResults resultsA = OrbitResults.createOrbitResults(namedA, gap, duration, interval.startDate().shiftedBy(-cushion));
        OrbitResults resultsB = OrbitResults.createOrbitResults(namedB, gap, duration, interval.startDate().shiftedBy(-cushion));

        System.out.println(namedA.name() + " " + namedB.name());
        ArrayList<OrbitPoint> results = calculateDistanceData(resultsA, resultsB, interval.startDate().shiftedBy(-cushion), gap);
        //CloseApproachPair detailedPair = new CloseApproachPair(resultsA, resultsB, results, duration);
        //CloseApproachInterval detailedInterval = CloseApproachInterval.createCloseApproachInterval(0, results.size() - 1, results);


        return results;

    }


}

