package com.example.orbittracker;

public class Anomaly {

    private static final double WARNING_THRESHOLD = 0.01;

    public double trueAnomaly(double meanAnomaly, double e){

        //this expression for the true anomaly from mean anomaly emits terms that have e^4 and greater terms
        //if e^4 > .01, send a warning
        double trueAnom = meanAnomaly+
                (2*e-0.25*Math.pow(e, 3))*Math.sin(meanAnomaly)+
                5.0/4*Math.pow(e, 2)*Math.sin(2*meanAnomaly)+
                13.0/12*Math.pow(e, 3)*Math.sin(3*meanAnomaly);

        if(Math.pow(e, 4) > WARNING_THRESHOLD){
            System.out.println("Warning: True anomaly calculations may have errors");
        }
        return trueAnom;

    }
}

