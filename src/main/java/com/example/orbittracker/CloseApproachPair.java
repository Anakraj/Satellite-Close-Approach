package com.example.orbittracker;

import org.hipparchus.analysis.function.Abs;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;
import java.util.Collections;

public class CloseApproachPair {

    //2 orbits that are to be compared and analyzed
    OrbitResults a;
    OrbitResults b;

    //PVCoordinates of both orbits
    ArrayList<PVCoordinates> coordsA;
    ArrayList<PVCoordinates> coordsB;

    //distance in meters between both satellites at given intervals
    ArrayList<Double> distances = new ArrayList<>();
    double distanceAtApproach;
    //above is deprecated

    double bufferInMeters;
    ArrayList<OrbitPoint> points;
    ArrayList<CloseApproachInterval> intervals = new ArrayList<>();

    @Deprecated
    public CloseApproachPair(OrbitResults a, OrbitResults b) {
        this.a = a;
        this.b = b;
        this.coordsA = a.getCoords();
        this.coordsB = b.getCoords();
        generateDistances();

    }

    @Deprecated
    public CloseApproachPair(OrbitResults a, OrbitResults b, double distanceAtApproach) {
        this.a = a;
        this.b = b;
        this.coordsA = a.getCoords();
        this.coordsB = b.getCoords();
        this.distanceAtApproach = distanceAtApproach;
        generateDistances();
    }


    public CloseApproachPair(OrbitResults a, OrbitResults b, ArrayList<OrbitPoint> orbitPoints, double bufferInMeters) {
        this.a = a;
        this.b = b;
        this.coordsA = a.getCoords();
        this.coordsB = b.getCoords();
        this.points = orbitPoints;
        this.bufferInMeters = bufferInMeters;
        generateIntervals();
    }

    public OrbitResults getA() {
        return a;
    }

    public OrbitResults getB() {
        return b;
    }


    public ArrayList<CloseApproachInterval> getIntervals() {
        return intervals;
    }

    void generateIntervals() {

        //initializing variables for generating intervals of approach
        boolean isClose = false;
        int startIndex = 0;
        int endIndex = 0;


        for(int i = 0; i < points.size(); i++) {
            //get the current point
            OrbitPoint curPoint = points.get(i);

            //are we in the middle of a close approach?
            if(isClose) {
                //is the close approach still going?
                if(curPoint.getDistance() <= bufferInMeters) {
                    endIndex = i;
                }
                else {
                    //we are at the end of a close approach
                    CloseApproachInterval temp = new CloseApproachInterval(startIndex, endIndex, this.points);
                    intervals.add(temp);
                }
            }
            else {
                //are we at the beginning of a close approach?
                if(curPoint.getDistance() <= bufferInMeters) {
                    isClose = true;
                    startIndex = i;
                    endIndex = i;
                }
            }



        }
    }


    public void generateDistances() {
        //fills distance arraylist with distances of 2 satellites at different time intervals
        for(int i = 0; i < Math.min(coordsA.size(), coordsB.size()); i++) {
            distances.add(coordsA.get(i).getPosition().distance(coordsB.get(i).getPosition()));
        }
    }

    //finds the closest distance the 2 satellites will be near each other
    public double getClosestDistance() {
        double toRet = Integer.MAX_VALUE * 1.0;
        for(Double v : distances) {
            toRet = Math.min(toRet, v);
        }
        return toRet;
    }


}
