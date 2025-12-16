package com.klu.tfs.io;

import com.klu.tfs.model.Junction;
import com.klu.tfs.model.Lane;
import com.klu.tfs.exception.InvalidSignalConfigException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
    public static List<Junction> readConfig(String filePath) throws Exception {
        List<Junction> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 3) throw new InvalidSignalConfigException("Invalid config at line " + lineNo);
                String jId = parts[0].trim();
                int laneCount;
                int maxCapacity;
                try {
                    laneCount = Integer.parseInt(parts[1].trim());
                    maxCapacity = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    throw new InvalidSignalConfigException("Number format error at line " + lineNo);
                }
                if (laneCount <= 0 || maxCapacity <= 0) throw new InvalidSignalConfigException("Nonpositive values at line " + lineNo);
                List<Lane> lanes = new ArrayList<>();
                for (int i = 1; i <= laneCount; i++) {
                    String lid = jId + "_L" + i;
                    lanes.add(new Lane(lid, maxCapacity));
                }
                result.add(new Junction(jId, lanes));
            }
        }
        return result;
    }
}
