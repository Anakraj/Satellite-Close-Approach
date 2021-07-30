package com.example.orbittracker;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;

import javax.naming.Name;
import java.util.ArrayList;

public class OrbitResults {

    //PVCoordinates that are to be generated
    private ArrayList<PVCoordinates> coords;



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


    public static double calcSemiMajorAxis(TLE tle) {
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

        return Math.pow(mu, 1.0/3.0) / Math.pow(n, 2.0/3.0);
    }

    public static double calcPerigee(double a, TLE tle) {
        double earthRadiusMeters = 6371000;
         return a * (1 - tle.getE()) - earthRadiusMeters;
    }

    public static double calcApogee(double a, TLE tle) {
        double earthRadiusMeters = 6371000;
        return a * (1 + tle.getE()) - earthRadiusMeters;
    }

    public static ArrayList<PVCoordinates> propagatedList(AbsoluteDate startDate, double durationInSeconds, double intervalInSeconds, TLEPropagator tProp) {
        Frame inertialFrame = FramesFactory.getEME2000();
        AbsoluteDate tempDate = startDate;
        AbsoluteDate endDate = tempDate.shiftedBy(durationInSeconds);

        ArrayList<PVCoordinates> coords = new ArrayList<>();

        //while end date hasn't been reached, propagate orbit up to current point, add PVCoordinates, then increment working date
        while(tempDate.compareTo((endDate)) <= 0.0) {
            PVCoordinates pv = tProp.getPVCoordinates(tempDate, inertialFrame);
            //System.out.println(pv);
            coords.add(pv);
            tempDate = tempDate.shiftedBy(intervalInSeconds);
        }

        return coords;
    }


    public static OrbitResults createOrbitResults(NamedTLE namedTLE, double intervalInSeconds, double durationInSeconds, AbsoluteDate startDate) {
        NamedTLE _namedTLE = namedTLE;
        String _name = namedTLE.name();
        TLE _TLE = namedTLE.TLE();
        TLEPropagator _tProp = TLEPropagator.selectExtrapolator(_TLE);

        double _intervalInSeconds = intervalInSeconds;
        double _durationInSeconds = durationInSeconds;
        AbsoluteDate _startDate = startDate;

        double _trueAnomaly = Anomaly.trueAnomaly(_TLE.getMeanAnomaly(), _TLE.getE());

        double _semiMajorAxis = calcSemiMajorAxis(_TLE);
        double _perigee = calcPerigee(_semiMajorAxis, _TLE);
        double _apogee = calcApogee(_semiMajorAxis, _TLE);

        ArrayList<PVCoordinates> _coords = propagatedList(startDate, durationInSeconds, intervalInSeconds, _tProp);

        System.out.println(_name + " done");

        return new OrbitResults(_namedTLE, _name, _TLE, _tProp,
                _intervalInSeconds, _durationInSeconds, _startDate,
                _trueAnomaly, _semiMajorAxis, _perigee, _apogee,
                _coords);

    }

    public OrbitResults(NamedTLE namedTLE, String name, TLE tle,
                        TLEPropagator tProp,
                        double intervalInSeconds, double durationInSeconds, AbsoluteDate startDate,
                        double trueAnomaly, double semiMajorAxis, double perigee, double apogee,
                        ArrayList<PVCoordinates> coords) {
        this.namedTLE = namedTLE;
        this.name = name;
        this.tle = tle;
        this.tProp = tProp;
        this.intervalInSeconds = intervalInSeconds;
        this.durationInSeconds = durationInSeconds;
        this.startDate = startDate;
        this.trueAnomaly = trueAnomaly;
        this.a = semiMajorAxis;
        this.perigee = perigee;
        this.apogee = apogee;
        this.coords = coords;
    }

    public double averageAngularSpeed() {
        return avgAngularSpeed;
    }

    public ArrayList<PVCoordinates> coords() {
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

    public double semiMajorAxis() {
        return a;
    }

    public String name() {
        return name;
    }

    public TLE TLE() {
        return tle;
    }

    public NamedTLE namedTLE() {
        return namedTLE;
    }

//    private void setAverageAngularSpeed() {
//        double toRet = 0.0;
//        for(PVCoordinates pv : coords) {
//            Vector3D angVec = pv.getAngularVelocity();
//            double x = angVec.getX();
//            double y = angVec.getY();
//            double z = angVec.getZ();
//
//            double mag = Math.sqrt(x + y + z);
//
//            toRet += mag;
//        }
//        toRet /= coords.size();
//        this.avgAngularSpeed = toRet;
//    }


}
