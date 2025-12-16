package com.klu.tfs.model;

import java.util.ArrayList;
import java.util.List;

public class Junction {
    private String id;
    private List<Lane> lanes;

    public Junction(String id, List<Lane> lanes) {
        this.id = id;
        this.lanes = new ArrayList<>(lanes);
    }

    public String getId() {
        return id;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public int totalVehicles() {
        int sum = 0;
        for (Lane l : lanes) sum += l.size();
        return sum;
    }
}
