package com.algolearn.patterns;

import com.algolearn.model.AlgorithmEngine;
import com.algolearn.model.AlgorithmStep;
import java.util.Arrays;
import java.util.List;

public class SearchingStrategy implements AlgorithmStrategy {

    private final String  name;
    private final boolean sorted;
    private int searchTarget = 0;

    public SearchingStrategy(String name, boolean sorted) {
        this.name   = name;
        this.sorted = sorted;
    }

    public void setSearchTarget(int target) {
        this.searchTarget = target;
    }

    @Override
    public List<AlgorithmStep> execute(int[] inputArray) {
        int[] arr = sorted
            ? Arrays.stream(inputArray).sorted().toArray()
            : inputArray.clone();
        if ("Binary Search".equals(name))
            return AlgorithmEngine.binarySearch(arr, searchTarget);
        return AlgorithmEngine.linearSearch(arr, searchTarget);
    }

    @Override public String  getName()             { return name; }
    @Override public String  getCategory()         { return "Searching"; }
    @Override public boolean requiresSortedInput() { return sorted; }

    public static SearchingStrategy linearSearch() { return new SearchingStrategy("Linear Search", false); }
    public static SearchingStrategy binarySearch() { return new SearchingStrategy("Binary Search", true); }
}