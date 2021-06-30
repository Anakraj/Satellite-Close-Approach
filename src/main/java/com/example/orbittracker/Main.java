package com.example.orbittracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.hipparchus.util.FastMath;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.time.UTCScale;

public class Main {
    public static void main(String[] args) {
        //Hello Universe
        System.out.println("Hello universe");

        //Define the inertial frame as reference frame
        Frame inertialFrame = FramesFactory.getEME2000();

        //Initial state
        UTCScale utc = TimeScalesFactory.getUTC();
        AbsoluteDate initialDate = new AbsoluteDate(2004, 01, 01, 23, 30, 00.000, utc);

        //big number
        double mu =  3.986004415e+14;

        //6 parameters to define the orbit
        double a = 24396159;                     // semi major axis in meters
        double e = 0.72831215;                   // eccentricity
        double i = FastMath.toRadians(7);        // inclination
        double omega = FastMath.toRadians(180);  // perigee argument
        double raan = FastMath.toRadians(261);   // right ascension of ascending node
        double lM = 0;                           // mean anomaly

        //defining the keplerian orbit with parameters
        Orbit initialOrbit = new KeplerianOrbit(a, e, i, omega, raan, lM, PositionAngle.MEAN,
                inertialFrame, initialDate, mu);

        //simple propagator apparently
        KeplerianPropagator kepler = new KeplerianPropagator(initialOrbit);

        //set to slave mode, but is default, so only for demonstration purposes
        //what is slave mode anyway?
        kepler.setSlaveMode();

        //duration of orbit
        double duration = 600.;
        //final date after orbit tracking is done
        AbsoluteDate finalDate = initialDate.shiftedBy(duration);
        //step for each propagated data point
        double stepT = 60.;
        //declares an integer cpt with value 1 (I don't know)
        int cpt = 1;
        //alien code
        //increments the date by the duration
        //for every iteration, sets the current state of the spacecraft to the propagated state
        for (AbsoluteDate extrapDate = initialDate;
             extrapDate.compareTo(finalDate) <= 0;
             extrapDate = extrapDate.shiftedBy(stepT))  {
            SpacecraftState currentState = kepler.propagate(extrapDate);
            System.out.format(Locale.US, "step %2d %s %s%n",
                    cpt++, currentState.getDate(), currentState.getOrbit());
        }


    }
}
