package com.example.orbittracker;

import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;

public class CloseApproachPair {

    //2 orbits that are to be compared and analyzed
    private final OrbitResults a;
    private final OrbitResults b;

    //PVCoordinates of both orbits
    private final ArrayList<PVCoordinates> coordsA;
    private final ArrayList<PVCoordinates> coordsB;

    //distance in meters between both satellites at given intervals
    private final ArrayList<Double> distances = new ArrayList<>();
    //above is deprecated

    private final double bufferInMeters;
    private final ArrayList<OrbitPoint> points;
    private final ArrayList<CloseApproachInterval> intervals = new ArrayList<>();


    public CloseApproachPair(OrbitResults a, OrbitResults b, ArrayList<OrbitPoint> orbitPoints, double bufferInMeters) {
        this.a = a;
        this.b = b;
        this.coordsA = a.coordinates();
        this.coordsB = b.coordinates();
        this.points = orbitPoints;
        this.bufferInMeters = bufferInMeters;
        generateIntervals();
    }

    public OrbitResults resultsA() {
        return a;
    }

    public OrbitResults resultsB() {
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
                //System.out.println(startIndex + " " + endIndex + " " + i);
                //is the close approach still going?
                if(curPoint.distance() <= bufferInMeters) {
                    endIndex = i;
                }
                else {
                    //we are at the end of a close approach
                    CloseApproachInterval temp = CloseApproachInterval.createCloseApproachInterval(startIndex, endIndex, points);
                    intervals.add(temp);
                    isClose = false;
                }
            }
            else {
                //are we at the beginning of a close approach?
                if(curPoint.distance() <= bufferInMeters) {
                    isClose = true;
                    startIndex = i;
                    endIndex = i;
                }
            }
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