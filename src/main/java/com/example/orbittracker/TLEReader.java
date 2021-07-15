package com.example.orbittracker;

import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.propagation.analytical.tle.TLE;

import java.io.*;
import java.util.*;

@Deprecated
public class TLEReader {

    //this will store all the tles from all the files in filesToRead
    TLE[] tles;
    //contains the path to all the files containing tle data
    String[] filesToRead;


    //constructor takes in array of file paths
    public TLEReader(String[] filesToRead) {
        this.filesToRead = filesToRead;
    }

    //reads all of the tles from every file, and puts them all into the tles array
    public void readTLEs() {
        //load in required orekit data
        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));


        //first calculate the total number of lines in all files
        int lines = 0;
        int numTLEs;
        for(int i = 0; i < filesToRead.length; i++) {
            //get number of lines in each file
            try (BufferedReader reader = new BufferedReader(new FileReader(filesToRead[i]))) {
                while (reader.readLine() != null) lines++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //this gives the number of tle elements in all files, an array can then be created
        numTLEs = lines / 3;
        tles = new TLE[numTLEs];

        //keeps track of which element in the array we are on
        int n = 0;
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
                        tles[n] = tle;
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
    }

    //reads the first k tles from each files, and puts them in the tles array, mostly meant for testing
    public void readNumTLEs(int k) {

        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));


        int lines = 0;
        int numTLEs;
        for(int i = 0; i < filesToRead.length; i++) {
            int curFileLength = 0;
            //get number of lines in each file
            try (BufferedReader reader = new BufferedReader(new FileReader(filesToRead[i]))) {
                while (reader.readLine() != null) curFileLength++;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //if there are less than k elements in any file, then that amount of tles will be read instead, and storage will be allocated accordingly
            lines += Math.min(k * 3, curFileLength);
        }

        numTLEs = lines / 3;
        tles = new TLE[numTLEs];

        //n tracks index for tles, n_c, or n sub c, tracks how many elements have been read on the current file
        int n = 0;
        int n_c = 0;


        for(int i = 0; i < filesToRead.length; i++) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filesToRead[i]))) {

                while(true) {
                    String name = reader.readLine();
                    //stop reading for this file if k has been exceeded, or if there are no more files to read
                    if(name == null || n_c >= k) {
                        //reset n_c after working with a file
                        n_c = 0;
                        break;
                    }

                    //the rest is similar to readTLEs
                    String line1 = reader.readLine().stripTrailing();
                    String line2 = reader.readLine().stripTrailing();

                    System.out.println(line1);

                    if(TLE.isFormatOK(line1, line2)) {
                        TLE tle = new TLE(line1, line2);
                        tles[n] = tle;
                        n_c++;
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
    }

    public void emptyTLEs() {
        this.tles = null;
    }

    public TLE[] getTles() {
        return tles;
    }

}
