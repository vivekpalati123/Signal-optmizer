package com.klu.tfs.io;

import com.klu.tfs.model.SignalPlan;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.List;

public class PlanWriter {
    public static void writeSignalPlan(SignalPlan plan, String path) throws IOException {
        try (FileWriter fw = new FileWriter(path, false)) {
            fw.write("JUNCTION_ID," + plan.getJunctionId() + System.lineSeparator());
            fw.write("LANE_ID,GREEN_TIME_SECONDS" + System.lineSeparator());
            for (Map.Entry<String, Integer> e : plan.getLaneTimings().entrySet()) {
                fw.write(e.getKey() + "," + e.getValue() + System.lineSeparator());
            }
            fw.flush();
        }
    }

    public static void writeCongestionReport(String junctionId, String path, List<java.util.Map<String, String>> rows) throws IOException {
        try (FileWriter fw = new FileWriter(path, false)) {
            fw.write("JUNCTION_ID,LANE_ID,VEHICLES_IN_LANE,MAX_CAPACITY,OCCUPANCY_RATIO" + System.lineSeparator());
            for (java.util.Map<String, String> r : rows) {
                fw.write(junctionId + "," + r.get("lane") + "," + r.get("vehicles") + "," + r.get("capacity") + "," + r.get("ratio") + System.lineSeparator());
            }
            fw.flush();
        }
    }
}
