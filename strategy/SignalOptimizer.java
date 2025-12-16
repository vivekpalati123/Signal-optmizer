package com.klu.tfs.strategy;

import com.klu.tfs.model.Junction;
import com.klu.tfs.model.SignalPlan;

public interface SignalOptimizer {
    SignalPlan optimize(Junction j);
}
