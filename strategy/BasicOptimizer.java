package com.klu.tfs.strategy;

import com.klu.tfs.model.Junction;
import com.klu.tfs.model.Lane;
import com.klu.tfs.model.SignalPlan;
import java.util.List;

public class BasicOptimizer implements SignalOptimizer {
    private int totalCycle;
    private int minGreen;

    public BasicOptimizer() {
        this.totalCycle = 60;  // total signal cycle in seconds
        this.minGreen = 5;      // minimum green time per lane
    }

    public BasicOptimizer(int totalCycle, int minGreen) {
        this.totalCycle = totalCycle;
        this.minGreen = minGreen;
    }

    @Override
    public SignalPlan optimize(Junction j) {
        List<Lane> lanes = j.getLanes();
        SignalPlan plan = new SignalPlan(j.getId());
        if (lanes.isEmpty()) return plan;

        int totalVehicles = 0;
        for (Lane l : lanes) totalVehicles += l.size();

        for (Lane l : lanes) {
            int green;
            if (totalVehicles == 0) {
                green = minGreen;  // if no vehicles, assign minimum green
            } else {
                // green time proportional to vehicles in lane
                green = (int) Math.round(((double) l.size() / totalVehicles) * totalCycle);
                if (green < minGreen) green = minGreen;
            }
            plan.setTiming(l.getId(), green);
        }

        return plan;
    }
}
