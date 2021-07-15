package com.example.orbittracker;

import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;

public class OrbitResults {

    ArrayList<PVCoordinates> coords = new ArrayList<>();

    NamedTLE namedTLE;
    TLEPropagator tProp;
    String name;
    TLE tle;



    double perigee;
    double apogee;
    double trueAnomaly;
    double a;

    double intervalInSeconds;
    double durationInSeconds;


    public OrbitResults(NamedTLE namedTLE, double intervalInSeconds, double durationInSeconds) {
        this.namedTLE = namedTLE;
        this.name = namedTLE.getName();
        this.tle = namedTLE.getTle();
        this.tProp = TLEPropagator.selectExtrapolator(this.tle);

        this.intervalInSeconds = intervalInSeconds;
        this.durationInSeconds = durationInSeconds;

        calculateSemiMajorAxis();
        calculateApsis();
        calculateTrueAnomaly();



        propagate();
    }

    private void calculateTrueAnomaly() {
        this.trueAnomaly = Anomaly.trueAnomaly(tle.getMeanAnomaly(), tle.getE());
    }

    private void calculateSemiMajorAxis() {
        //double mu = 3.986e14;
        double mu = 3.986 * Math.pow(10, 14);
        double n = tle.getMeanMotion();

        System.out.println(mu);
        System.out.println(n);

        this.a = Math.pow(mu, 1.0/3.0) / Math.pow(2*n*Math.PI/86400, 2.0/3.0);
    }

    private void calculateApsis() {
        this.perigee = this.a * (1 - tle.getE());
        this.apogee = this.a * (1 + tle.getE());
    }

    private void propagate() {
        Frame inertialFrame = FramesFactory.getEME2000();
        AbsoluteDate startDate = new AbsoluteDate(2002, 5, 7, 12, 0, 0.0, TimeScalesFactory.getUTC());
        AbsoluteDate endDate = startDate.shiftedBy(this.durationInSeconds);

        while(startDate.compareTo((endDate)) <= 0.0) {
            PVCoordinates pv = tProp.getPVCoordinates(startDate, inertialFrame);
            //System.out.println(pv);
            coords.add(pv);
            startDate = startDate.shiftedBy(intervalInSeconds);
        }



        /*
        * while (extrapDate.compareTo(finalDate) <= 0.0):
    pv = propagator.getPVCoordinates(extrapDate, inertialFrame)
    pos_tmp = pv.getPosition()
    pos.append((pos_tmp.getX(),pos_tmp.getY(),pos_tmp.getZ()))

    el_tmp = station_frame.getElevation(pv.getPosition(),
                    inertialFrame,
                    extrapDate)*180.0/pi
    el.append(el_tmp)
    #print extrapDate, pos_tmp, vel_tmp
    extrapDate = extrapDate.shiftedBy(10.0)
        * */
    }

    public ArrayList<PVCoordinates> getCoords() {
        return coords;
    }

    public TLE getTle() {
        return tle;
    }

    public double getPerigee() {
        return perigee;
    }

    public double getApogee() {
        return apogee;
    }

    public double getTrueAnomaly() {
        return trueAnomaly;
    }

    public double getA() {
        return a;
    }
}
