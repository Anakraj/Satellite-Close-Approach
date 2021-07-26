package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;

public class OrbitResults {

    //PVCoordinates that are to be generated
    private final ArrayList<PVCoordinates> coords = new ArrayList<>();



    //Properties from named TLE
    private final NamedTLE namedTLE;
    private final String name;
    private final TLE tle;

    //the propagator
    private final TLEPropagator tProp;

    //Orbit properties
    private double perigee;
    private double apogee;
    private double trueAnomaly;
    private double a;
    private double avgAngularSpeed;

    //Data for propagation
    private double intervalInSeconds;
    private double durationInSeconds;
    private AbsoluteDate startDate;



    public OrbitResults(NamedTLE namedTLE, double intervalInSeconds, double durationInSeconds, AbsoluteDate startDate) {
        this.namedTLE = namedTLE;
        this.name = namedTLE.name();
        this.tle = namedTLE.TLE();
        this.tProp = TLEPropagator.selectExtrapolator(this.tle);

        this.intervalInSeconds = intervalInSeconds;
        this.durationInSeconds = durationInSeconds;
        this.startDate = startDate;

        calculateSemiMajorAxis();
        calculateApsis();
        calculateTrueAnomaly();
        propagate();
    }

    public double avgAngularSpeed() {
        return avgAngularSpeed;
    }

    public ArrayList<PVCoordinates> coordinates() {
        return coords;
    }

    public double perigee() {
        return perigee;
    }

    public double apogee() {
        return apogee;
    }

    public double trueAnomaly() {
        return trueAnomaly;
    }

    public double semimajorAxis() {
        return a;
    }

    public String name() {
        return name;
    }

    public TLE TLE() {
        return tle;
    }

    private void calculateTrueAnomaly() {
        this.trueAnomaly = Anomaly.trueAnomaly(tle.getMeanAnomaly(), tle.getE());
    }

    private void calculateSemiMajorAxis() {
        /* finding equation for semimajor axis
        T^2 = ((4PI^2)/(GM))a^3
        a^3 = ((GM)/(4PI^2))T^2
        a^3 = ((mu)/(4PI^2))(1/mean_motion * 24 * 3600)^2
        a = (mu^.3333)/(2nPI*86400)^.6666666

         */

        //double mu = 3.986e14;
        double mu = 3.986 * Math.pow(10, 14);
        double n = tle.getMeanMotion();

//        System.out.println(mu);
//        System.out.println(n);

        this.a = Math.pow(mu, 1.0/3.0) / Math.pow(2*n*Math.PI/86400, 2.0/3.0);
    }

    private void calculateApsis() {
        this.perigee = this.a * (1 - tle.getE());
        this.apogee = this.a * (1 + tle.getE());
    }

    private void propagate() {
        //set up the date
        Frame inertialFrame = FramesFactory.getEME2000();
        AbsoluteDate tempDate = startDate;
        AbsoluteDate endDate = tempDate.shiftedBy(this.durationInSeconds);

        //while end date hasn't been reached, propagate orbit up to current point, add PVCoordinates, then increment working date
        while(tempDate.compareTo((endDate)) <= 0.0) {
            PVCoordinates pv = tProp.getPVCoordinates(tempDate, inertialFrame);
            //System.out.println(pv);
            coords.add(pv);
            tempDate = tempDate.shiftedBy(intervalInSeconds);
        }
    }

    private void setAverageAngularSpeed() {
        double toRet = 0.0;
        for(PVCoordinates pv : coords) {
            Vector3D angVec = pv.getAngularVelocity();
            double x = angVec.getX();
            double y = angVec.getY();
            double z = angVec.getZ();

            double mag = Math.sqrt(x + y + z);

            toRet += mag;
        }
        toRet /= coords.size();
        this.avgAngularSpeed = toRet;
    }


}
