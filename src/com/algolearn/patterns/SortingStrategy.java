package com.algolearn.patterns;

import com.algolearn.model.AlgorithmEngine;
import com.algolearn.model.AlgorithmStep;
import java.util.List;
import java.util.function.Function;

public class SortingStrategy implements AlgorithmStrategy {

    private final String name;
    private final Function<int[], List<AlgorithmStep>> executor;

    public SortingStrategy(String name, Function<int[], List<AlgorithmStep>> executor) {
        this.name     = name;
        this.executor = executor;
    }

    @Override
    public List<AlgorithmStep> execute(int[] input) {
        return executor.apply(input);
    }

    @Override public String  getName()              { return name; }
    @Override public String  getCategory()          { return "Sorting"; }
    @Override public boolean requiresSortedInput()  { return false; }

    public static SortingStrategy bubbleSort()    { return new SortingStrategy("Bubble Sort",    AlgorithmEngine::bubbleSort); }
    public static SortingStrategy selectionSort() { return new SortingStrategy("Selection Sort", AlgorithmEngine::selectionSort); }
    public static SortingStrategy insertionSort() { return new SortingStrategy("Insertion Sort", AlgorithmEngine::insertionSort); }
    public static SortingStrategy mergeSort()     { return new SortingStrategy("Merge Sort",     AlgorithmEngine::mergeSort); }
    public static SortingStrategy quickSort()     { return new SortingStrategy("Quick Sort",     AlgorithmEngine::quickSort); }
}