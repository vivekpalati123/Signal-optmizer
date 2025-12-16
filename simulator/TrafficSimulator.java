package com.klu.tfs.simulator;

import com.klu.tfs.model.Junction;
import com.klu.tfs.model.Lane;
import com.klu.tfs.model.Vehicle;
import com.klu.tfs.model.SignalPlan;
import com.klu.tfs.strategy.SignalOptimizer;
import com.klu.tfs.io.ConfigReader;
import com.klu.tfs.io.PlanWriter;
import com.klu.tfs.exception.OverflowLaneException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashSet;
import java.util.*;
public class TrafficSimulator {
    private Map<String, Junction> junctions = new HashMap<>();
    private SignalOptimizer optimizer;
    private Random rng = new Random(42);
    private long vehicleCounter = 1000L;

    public void loadConfig(String filePath) throws Exception {
        List<Junction> list = ConfigReader.readConfig(filePath);
        junctions.clear();
        for (Junction j : list) junctions.put(j.getId(), j);
    }

    public void setOptimizer(SignalOptimizer opt) {
        this.optimizer = opt;
    }

    public void simulate(int seconds, int arrivalProbabilityPercent) throws OverflowLaneException {
        if (junctions.isEmpty()) return;

        int[][] unevenVehicles = {
            {2, 3, 4, 1},   // J1 → total 10
            {5, 7, 6, 2},   // J2 → total 20
            {8, 10, 7, 5}   // J3 → total 30
        };

        int jIndex = 0;

        for (Junction j : junctions.values()) {
            List<Lane> lanes = j.getLanes();
            int[] laneVehicles = unevenVehicles[jIndex % unevenVehicles.length];

            for (int i = 0; i < lanes.size(); i++) {
                Lane l = lanes.get(i);
                int count = laneVehicles[i];

                for (int v = 0; v < count; v++) {
                    l.enqueue(new Vehicle("V" + (++vehicleCounter)));
                }
            }
            jIndex++;
        }
    }

    

    public SignalPlan optimizeSignals(String junctionId) {
        Junction j = junctions.get(junctionId);
        if (j == null) return null;
        if (optimizer == null) optimizer = new com.klu.tfs.strategy.BasicOptimizer();
        return optimizer.optimize(j);
    }

    public void exportPlan(SignalPlan p, String signalPath, String congestionPath) throws IOException {
        if (p == null) return;
        PlanWriter.writeSignalPlan(p, signalPath);
        Junction j = junctions.get(p.getJunctionId());
        if (j == null) return;
        List<Map<String, String>> rows = new ArrayList<>();
        HashSet<String> seenLanes = new HashSet<>();
        for (Lane l : j.getLanes()) {
            Map<String, String> r = new HashMap<>();
            r.put("lane", l.getId());
            r.put("vehicles", Integer.toString(l.size()));
            r.put("capacity", Integer.toString(l.getMaxCapacity()));
            double ratio = l.size() / (double) l.getMaxCapacity();
            r.put("ratio", String.format("%.3f", ratio));
            rows.add(r);
            seenLanes.add(l.getId());
        }
        PlanWriter.writeCongestionReport(j.getId(), congestionPath, rows);
    }

    public List<String> listJunctionIds() {
        return new ArrayList<>(junctions.keySet());
    }
}
