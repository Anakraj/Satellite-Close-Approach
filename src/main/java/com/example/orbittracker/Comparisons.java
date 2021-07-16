package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;

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

    public static boolean angularSpeedTest(double anomly1, double angularSpeed1, double anomly2, double angularSpeed2){
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

    public static boolean propagationTest(OrbitResults a, OrbitResults b, double bufferInKm) {
        //comment
        if(apogeeTest(a.getApogee(), a.getPerigee(), b.getApogee(), b.getPerigee(), bufferInKm)) {
            return false;
        }

        ArrayList<PVCoordinates> aCoords = a.getCoords();
        ArrayList<PVCoordinates> bCoords = b.getCoords();

        for(int i = 0; i < aCoords.size(); i++) {
            Vector3D aPos = aCoords.get(i).getPosition();
            Vector3D bPos = bCoords.get(i).getPosition();

            double dist = aPos.distance(bPos);
            if(dist <= bufferInKm * 1000) {
                return true;
            }
        }

        return false;

    }
}

