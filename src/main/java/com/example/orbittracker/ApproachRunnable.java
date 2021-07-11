package com.example.orbittracker;

import org.orekit.propagation.analytical.tle.TLE;

import java.util.ArrayList;

public class ApproachRunnable implements Runnable{

    ArrayList<CloseApproach> closeApproaches;
    ArrayList<NamedTLE> listA;
    ArrayList<NamedTLE> listB;
    boolean single;

    @Override
    public void run() {
        if(single) {
            closeApproaches = generateCloseApproachesSingle();
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

    public ArrayList<CloseApproach> getCloseApproaches() {
        return closeApproaches;
    }

    ArrayList<CloseApproach> generateCloseApproachesSingle() {
        ArrayList<CloseApproach> toRet = new ArrayList<>();

        for(int i = 0; i < listA.size(); i++) {
            for(int j = i + 1; j < listA.size(); j++) {
                CloseApproach closeApproach = new CloseApproach(listA.get(i), listA.get(j));
                System.out.println(closeApproach);
                toRet.add(closeApproach);
            }
        }

        return toRet;
    }
}
