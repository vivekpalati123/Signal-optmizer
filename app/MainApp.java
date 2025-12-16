package com.klu.tfs.app;

import com.klu.tfs.simulator.TrafficSimulator;
import com.klu.tfs.factory.OptimizerFactory;
import com.klu.tfs.strategy.SignalOptimizer;
import com.klu.tfs.model.SignalPlan;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        TrafficSimulator simulator = new TrafficSimulator();
        Scanner sc = new Scanner(System.in);
        String configPath = "junction.cfg";
        String lastPlanPath = "signal_plan.txt";
        String lastReportPath = "congestion_report.csv";
        while (true) {
            System.out.println("===== TRAFFIC FLOW SIMULATOR =====");
            System.out.println("1. Load Configuration (default: junction.cfg)");
            System.out.println("2. Simulate Traffic");
            System.out.println("3. Optimize Signals");
            System.out.println("4. Export Plan & Report");
            System.out.println("5. List Junctions");
            System.out.println("6. Exit");
            System.out.print("Choose: ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid input");
                continue;
            }
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter config file path (enter for default): ");
                        String p = sc.nextLine().trim();
                        if (!p.isEmpty()) configPath = p;
                        simulator.loadConfig(configPath);
                        System.out.println("Configuration loaded from " + configPath);
                        break;
                    case 2:
                        System.out.print("Simulation seconds (e.g., 60): ");
                        int sec = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Arrival probability percent per second per lane (0-100, e.g., 20): ");
                        int prob = Integer.parseInt(sc.nextLine().trim());
                        simulator.simulate(sec, prob);
                        System.out.println("Simulation completed for " + sec + " seconds");
                        break;
                    case 3:
                        List<String> jids = simulator.listJunctionIds();
                        if (jids.isEmpty()) {
                            System.out.println("No junctions loaded");
                            break;
                        }
                        System.out.println("Available junctions:");
                        for (String jid : jids) System.out.println(" - " + jid);
                        System.out.print("Enter junction id to optimize: ");
                        String jid = sc.nextLine().trim();
                        System.out.print("Choose optimizer (basic/adaptive): ");
                        String type = sc.nextLine().trim();
                        SignalOptimizer opt = OptimizerFactory.getOptimizer(type);
                        simulator.setOptimizer(opt);
                        SignalPlan plan = simulator.optimizeSignals(jid);
                        if (plan == null) System.out.println("Could not optimize for " + jid);
                        else {
                            System.out.println("Optimized plan for " + jid + " (total cycle " + plan.totalCycle() + "s)");
                            plan.getLaneTimings().forEach((lane, time) -> System.out.println(lane + " -> " + time + "s"));
                        }
                        break;
                    case 4:
                        System.out.print("Enter junction id to export plan for: ");
                        String exJ = sc.nextLine().trim();
                        SignalPlan p2 = simulator.optimizeSignals(exJ);
                        if (p2 == null) {
                            System.out.println("No plan available for " + exJ);
                        } else {
                            System.out.print("Signal plan file path (enter for default " + lastPlanPath + "): ");
                            String sp = sc.nextLine().trim();
                            if (!sp.isEmpty()) lastPlanPath = sp;
                            System.out.print("Congestion report file path (enter for default " + lastReportPath + "): ");
                            String rp = sc.nextLine().trim();
                            if (!rp.isEmpty()) lastReportPath = rp;
                            simulator.exportPlan(p2, lastPlanPath, lastReportPath);
                            System.out.println("Exported plan to " + lastPlanPath + " and report to " + lastReportPath);
                        }
                        break;
                    case 5:
                        List<String> jj = simulator.listJunctionIds();
                        if (jj.isEmpty()) System.out.println("No junctions loaded");
                        else {
                            System.out.println("Junctions:");
                            for (String s : jj) System.out.println(" - " + s);
                        }
                        break;
                    case 6:
                        System.out.println("Exiting");
                        sc.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
