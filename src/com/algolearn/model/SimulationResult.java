package com.algolearn.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimulationResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String algorithmName;
    private final int arraySize;
    private final int totalSteps;
    private final int totalComparisons;
    private final int totalSwaps;
    private final long durationMs;
    private final String timestamp;

    public SimulationResult(String algorithmName, int arraySize, int totalSteps,
                            int totalComparisons, int totalSwaps, long durationMs) {
        this.algorithmName    = algorithmName;
        this.arraySize        = arraySize;
        this.totalSteps       = totalSteps;
        this.totalComparisons = totalComparisons;
        this.totalSwaps       = totalSwaps;
        this.durationMs       = durationMs;
        this.timestamp        = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getAlgorithmName()  { return algorithmName; }
    public int    getArraySize()      { return arraySize; }
    public int    getTotalSteps()     { return totalSteps; }
    public int    getTotalComparisons(){ return totalComparisons; }
    public int    getTotalSwaps()     { return totalSwaps; }
    public long   getDurationMs()     { return durationMs; }
    public String getTimestamp()      { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Size:%d | Steps:%d | Cmp:%d | Swaps:%d | %dms",
            timestamp, algorithmName, arraySize,
            totalSteps, totalComparisons, totalSwaps, durationMs);
    }
}