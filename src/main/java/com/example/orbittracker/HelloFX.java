package com.example.orbittracker;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        /*
        *URL url = new File("src/main/java/ua/adeptius/goit/sample.fxml").toURI().toURL();
Parent root = FXMLLoader.load(url);
        * */
        propagateAndGenerateLogs();

        URL url = new File("src/main/resources/scene.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);

        Scene scene = new Scene(root);

        URL url2 = new File("src/main/resources/styles.css").toURI().toURL();
        scene.getStylesheets().add(url2.toExternalForm());

        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }

    public void propagateAndGenerateLogs() throws IOException {
        System.out.println("Up and running");

        File orekitData = new File("./src/main/resources/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));

        String[] tlePaths = {"./src/main/resources/celestrak_active.txt"};

        ArrayList<NamedTLE> testTLEs;
        ArrayList<OrbitResults> testResults = new ArrayList<>();
        ArrayList<CloseApproachPair> closeApproachPairs = new ArrayList<>();
        final AbsoluteDate startDate = new AbsoluteDate(2002, 5, 7, 12, 0, 0.0, TimeScalesFactory.getUTC());



        testTLEs = TLEUtil.readTLEs(tlePaths, 20);
        final long startTime = System.currentTimeMillis();
/*
        //NONMULTITHREADING
        //this is the part that takes the longest
        for(NamedTLE i : testTLEs) {
            testResults.add(new OrbitResults(i, 60.0, 60.0 * 60.0 * 24 * 7, startDate));
        }
        //this is the part that takes the longest
*/

        //MULTITHREADING
        testTLEs.stream().parallel().forEach(i -> {
            testResults.add(new OrbitResults(i, 60.0, 60.0 * 60.0 * 24 * 7, startDate));
        });

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));

        System.out.println("Done with OrbitResults");

        for(int n1 = 0; n1 < testResults.size(); n1++) {
            for(int n2 = n1 + 1; n2 < testResults.size(); n2++) {
//                CloseApproachPair temp = Comparisons.testIfClose(testResults.get(n1), testResults.get(n2), 50000.0);
//                if(temp != null) {
//                    closeApproachPairs.add(temp);
//                }
                Optional<CloseApproachPair> temp = Comparisons.testIfApproach(testResults.get(n1),
                        testResults.get(n2),
                        50000.0,
                        startDate,
                        60.0);

                if(temp.isPresent()) {
                    closeApproachPairs.add(temp.get());
                }
            }
        }

        System.out.println("Done with pairs");
        Comparisons.generateLogs(closeApproachPairs, 5);
        System.out.println("Done with log");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
