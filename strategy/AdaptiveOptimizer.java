package com.klu.tfs.strategy;

import com.klu.tfs.model.Junction;
import com.klu.tfs.model.Lane;
import com.klu.tfs.model.SignalPlan;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

public class AdaptiveOptimizer implements SignalOptimizer {
    private int totalCycle;
    private int minGreen;

    public AdaptiveOptimizer() {
        this.totalCycle = 60;
        this.minGreen = 5;
    }

    public AdaptiveOptimizer(int totalCycle, int minGreen) {
        this.totalCycle = totalCycle;
        this.minGreen = minGreen;
    }

    @Override
    public SignalPlan optimize(Junction j) {
        List<Lane> lanes = j.getLanes();
        SignalPlan plan = new SignalPlan(j.getId());
        if (lanes.isEmpty()) return plan;

        PriorityQueue<Lane> pq = new PriorityQueue<>(Comparator.comparingInt(Lane::size).reversed());
        pq.addAll(lanes);
        int totalVehicles = 0;
        for (Lane l : lanes) totalVehicles += l.size();
        if (totalVehicles == 0) {
            int equal = Math.max(minGreen, totalCycle / lanes.size());
            int allocated = 0;
            for (int i = 0; i < lanes.size(); i++) {
                String lid = lanes.get(i).getId();
                if (i == lanes.size() - 1) plan.setTiming(lid, Math.max(minGreen, totalCycle - allocated));
                else {
                    plan.setTiming(lid, equal);
                    allocated += equal;
                }
            }
            return plan;
        }

        int remainingCycle = totalCycle;
        while (!pq.isEmpty()) {
            Lane l = pq.poll();
            int alloc = Math.max(minGreen, (int) Math.round(((double) l.size() / totalVehicles) * totalCycle));
            alloc = Math.min(alloc, Math.max(minGreen, remainingCycle - (pq.size() * minGreen)));
            if (alloc < minGreen) alloc = minGreen;
            plan.setTiming(l.getId(), alloc);
            remainingCycle -= alloc;
            if (remainingCycle <= 0) {
                while (!pq.isEmpty()) {
                    Lane l2 = pq.poll();
                    plan.setTiming(l2.getId(), minGreen);
                }
                break;
            }
        }
        return plan;
    }
}
