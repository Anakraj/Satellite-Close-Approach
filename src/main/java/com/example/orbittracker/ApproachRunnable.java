package com.example.orbittracker;

import org.orekit.propagation.analytical.tle.TLE;

import java.util.ArrayList;

public class ApproachRunnable implements Runnable{

    ArrayList<OrbitResults> orbitResults;
    ArrayList<NamedTLE> listA;
    ArrayList<NamedTLE> listB;
    boolean single;

    @Override
    public void run() {
        if(single) {
            orbitResults = generateCloseApproachesSingle();
        }
        else {
            //don't do anything for now
        }
    }

    public ApproachRunnable(ArrayList<NamedTLE> listA, ArrayList<NamedTLE> listB) {
        this.listA = listA;
        this.listB = listB;

        single = false;
    }

    public ApproachRunnable(ArrayList<NamedTLE> listA) {
        this.listA = listA;

        single = true;
    }

    public ArrayList<OrbitResults> getCloseApproaches() {
        return orbitResults;
    }

    ArrayList<OrbitResults> generateCloseApproachesSingle() {
        ArrayList<OrbitResults> toRet = new ArrayList<>();

        for(int i = 0; i < listA.size(); i++) {
            for(int j = i + 1; j < listA.size(); j++) {
                //do comparison here
            }
        }

        return toRet;
    }
}
