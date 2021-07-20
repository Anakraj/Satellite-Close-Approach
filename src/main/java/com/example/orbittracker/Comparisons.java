package com.example.orbittracker;

import org.hipparchus.analysis.function.Abs;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.orbits.Orbit;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Comparisons {

    //sees if we need to compare rockets based on apogee and perigee
    private static final int TIME_PERIOD = 7;

    private static boolean apogeeTest(double apogee1, double perigee1, double apogee2, double perigee2, double buffer){
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

        ArrayList<OrbitPoint> points = new ArrayList<>();

        ArrayList<PVCoordinates> aCoords = a.getCoords();
        ArrayList<PVCoordinates> bCoords = b.getCoords();

        AbsoluteDate tempDate = startDate;

        for(int i = 0; i < a.getCoords().size(); i++) {
            double distance = aCoords.get(i).getPosition().distance(bCoords.get(i).getPosition());
            Vector3D separation = aCoords.get(i).getPosition().subtract(bCoords.get(i).getPosition());
            tempDate = tempDate.shiftedBy(intervalInSeconds);

            points.add(new OrbitPoint(separation, distance, tempDate));
        }

        return points;
    }

    public static Optional<CloseApproachPair> testIfApproach(OrbitResults a, OrbitResults b, double bufferMeters, AbsoluteDate startDate, double internvalInSeconds) {
        if(apogeeTest(a.getApogee(), a.getPerigee(), b.getApogee(), b.getPerigee(), bufferMeters)) {
            return Optional.empty();
        }
        //consider getting rid of since very situational and doesn't consider buffer
        if(angularSpeedTest(a.getTrueAnomaly(), a.getAvgAngularSpeed(), b.getTrueAnomaly(), b.getAvgAngularSpeed())) {
            return Optional.empty();
        }

        ArrayList<OrbitPoint> results = calculateDistanceData(a, b, startDate, internvalInSeconds);

        for(OrbitPoint point : results) {
            //System.out.println(point.getDistance());
            if(point.getDistance() <= bufferMeters) {
                return Optional.of(new CloseApproachPair(a, b, results, bufferMeters));
            }
        }

        return Optional.empty();
    }

//    public static CloseApproachPair testIfClose(OrbitResults a, OrbitResults b, double bufferMeters) {
//        //comment
//        if(apogeeTest(a.getApogee(), a.getPerigee(), b.getApogee(), b.getPerigee(), bufferMeters)) {
//            return null;
//        }
//
//
//        //consider getting rid of since very situational and doesn't consider buffer
//        if(angularSpeedTest(a.getTrueAnomaly(), a.getAvgAngularSpeed(), b.getTrueAnomaly(), b.getAvgAngularSpeed())) {
//            return null;
//        }
//
//        //PV get the PVCoordinates of both satellites to be compared
//        ArrayList<PVCoordinates> aCoords = a.getCoords();
//        ArrayList<PVCoordinates> bCoords = b.getCoords();
//
//        for(int i = 0; i < aCoords.size(); i++) {
//            //get positions of both satellites as a 3d vector
//            Vector3D aPos = aCoords.get(i).getPosition();
//            Vector3D bPos = bCoords.get(i).getPosition();
//
//            //calculate the distance and see if buffer is overcome
//            double dist = aPos.distance(bPos);
//            if(dist <= bufferMeters) {
//                return new CloseApproachPair(a, b, dist);
//            }
//        }
//
//        return null;
//
//    }

    //write out a demo log file, currently only has names of satellites and closest approach
    public static void generateLogs(ArrayList<CloseApproachPair> approaches) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("orbit_out.txt"));


        for(CloseApproachPair i : approaches) {
            writer.write("Close approach(es) between " + i.getA().getName().strip() + " and " + i.getB().getName().strip() + ".");
            writer.write("\n");


            writer.write("Closest distance: " + i.getClosestDistance() + " m");
            writer.write("\n");
        }
        writer.close();
    }
}

