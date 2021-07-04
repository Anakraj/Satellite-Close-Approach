package com.example.orbittracker;

import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.propagation.analytical.tle.TLE;

import java.io.*;
import java.util.*;

public class TLEReader {
    //ArrayList<File> files;
    TLE[] tles;
    String[] filesToRead;

    public TLEReader(String[] filesToRead) {
        this.filesToRead = filesToRead;
    }
    public void readTLEs() {
        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));


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

        numTLEs = lines / 3;
        tles = new TLE[numTLEs];

        int n = 0;
        for(int i = 0; i < filesToRead.length; i++) {
            //get number of lines in each file
            try (BufferedReader reader = new BufferedReader(new FileReader(filesToRead[i]))) {

                while(true) {
                    String name = reader.readLine();
                    if(name == null) {
                        break;
                    }
                    String line1 = reader.readLine().stripTrailing();
                    String line2 = reader.readLine().stripTrailing();


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
            lines += Math.min(k * 3, curFileLength);
        }

        numTLEs = lines / 3;
        tles = new TLE[numTLEs];

        int n = 0;
        int n_c = 0;
        for(int i = 0; i < filesToRead.length; i++) {
            //get number of lines in each file
            try (BufferedReader reader = new BufferedReader(new FileReader(filesToRead[i]))) {

                while(true) {
                    String name = reader.readLine();
                    if(name == null || n_c >= k) {
                        n_c = 0;
                        break;
                    }
                    String line1 = reader.readLine().stripTrailing();
                    String line2 = reader.readLine().stripTrailing();


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
