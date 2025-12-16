package com.klu.tfs.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class SignalPlan {
    private String junctionId;
    private Map<String, Integer> laneTimings;

    public SignalPlan(String junctionId) {
        this.junctionId = junctionId;
        this.laneTimings = new LinkedHashMap<>();
    }

    public void setTiming(String laneId, int seconds) {
        laneTimings.put(laneId, seconds);
    }

    public Map<String, Integer> getLaneTimings() {
        return laneTimings;
    }

    public String getJunctionId() {
        return junctionId;
    }

    public int totalCycle() {
        int sum = 0;
        for (Integer v : laneTimings.values()) sum += v;
        return sum;
    }
}
