package com.algolearn.patterns;

import com.algolearn.model.AlgorithmStep;
import java.util.List;

public interface AlgorithmStrategy {
    List<AlgorithmStep> execute(int[] inputArray);
    String getName();
    String getCategory();
    boolean requiresSortedInput();
}