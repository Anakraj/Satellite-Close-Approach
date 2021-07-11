package com.example.tests.junit5;

import com.example.orbittracker.ApproachRunnable;
import com.example.orbittracker.NamedTLE;
import com.example.orbittracker.TLEUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ThreadTest {
    @Test
    void testThreads() throws InterruptedException {
        String[] iridium = {"./src/main/resources/space-track_iridium.txt"};
        String[] active = {"./src/main/resources/celestrak_active.txt"};

        ArrayList<NamedTLE> iridiumTLEs;
        ArrayList<NamedTLE> activeTLEs;

        iridiumTLEs = TLEUtil.readTLEs(iridium, 5);
        activeTLEs = TLEUtil.readTLEs(active, 5);


        ApproachRunnable iri = new ApproachRunnable(iridiumTLEs);
        ApproachRunnable act = new ApproachRunnable(activeTLEs);

        Thread iThread = new Thread(iri);
        Thread aThread = new Thread(act);

        iThread.run();
        aThread.run();

        iThread.join();
        aThread.join();

        System.out.println(iri.getCloseApproaches().size());
        System.out.println(act.getCloseApproaches().size());
    }
}
