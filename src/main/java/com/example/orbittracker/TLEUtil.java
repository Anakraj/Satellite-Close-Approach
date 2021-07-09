package com.example.orbittracker;

import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.propagation.analytical.tle.TLE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class TLEUtil {

    //reads all of the tles from every file, and puts them all into the tles array
    public static ArrayList<NamedTLE> readTLEs(String[] filesToRead) {
        //load in required orekit data
        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));

        ArrayList<NamedTLE> toRet = new ArrayList<>();

        //keeps track of which element in the array we are on
        for(int i = 0; i < filesToRead.length; i++) {

            //go through each file, and add each tle to the tles array
            try (BufferedReader reader = new BufferedReader(new FileReader(filesToRead[i]))) {

                while(true) {
                    //check if there are no more lines
                    String name = reader.readLine();
                    if(name == null) {
                        break;
                    }

                    //get the two lines
                    String line1 = reader.readLine().stripTrailing();
                    String line2 = reader.readLine().stripTrailing();

                    //check if format is valid, if it is, add it to the array, if it isn't throw an exception
                    if(TLE.isFormatOK(line1, line2)) {
                        TLE tle = new TLE(line1, line2);
                        NamedTLE namedTLE = new NamedTLE(tle, name);
                        toRet.add(namedTLE);
                    }
                    else {
                        throw new IOException();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return toRet;
    }

    public static ArrayList<NamedTLE> readTLEs(String[] filesToRead, int k) {
        //load in required orekit data
        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));

        ArrayList<NamedTLE> toRet = new ArrayList<>();

        //keeps track of which element in the array we are on
        for(int i = 0; i < filesToRead.length; i++) {
            int n = 0;
            //go through each file, and add each tle to the tles array
            try (BufferedReader reader = new BufferedReader(new FileReader(filesToRead[i]))) {

                while(true) {
                    //check if there are no more lines
                    String name = reader.readLine();
                    if(name == null || n >= k) {
                        break;
                    }

                    //get the two lines
                    String line1 = reader.readLine().stripTrailing();
                    String line2 = reader.readLine().stripTrailing();

                    //check if format is valid, if it is, add it to the array, if it isn't throw an exception
                    if(TLE.isFormatOK(line1, line2)) {
                        TLE tle = new TLE(line1, line2);
                        NamedTLE namedTLE = new NamedTLE(tle, name);
                        toRet.add(namedTLE);
                        n++;
                    }
                    else {
                        throw new IOException();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return toRet;
    }

}
