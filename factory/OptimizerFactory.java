package com.klu.tfs.factory;

import com.klu.tfs.strategy.SignalOptimizer;
import com.klu.tfs.strategy.BasicOptimizer;
import com.klu.tfs.strategy.AdaptiveOptimizer;

public class OptimizerFactory {
    public static SignalOptimizer getOptimizer(String type) {
        if (type == null) return new BasicOptimizer();
        switch (type.toLowerCase()) {
            case "adaptive":
                return new AdaptiveOptimizer();
            case "basic":
            default:
                return new BasicOptimizer();
        }
    }
}
