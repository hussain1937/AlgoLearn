package com.algolearn.model;

import java.io.Serializable;
import java.util.Arrays;

public class AlgorithmStep implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int[] arraySnapshot;
    private final int[] highlightIndices;
    private final String description;
    private final int comparisons;
    private final int swaps;
    private final int searchTarget;
    private final boolean found;
    private final int foundIndex;

    // Sorting constructor
    public AlgorithmStep(int[] arraySnapshot, int[] highlightIndices,
                         String description, int comparisons, int swaps) {
        this.arraySnapshot    = Arrays.copyOf(arraySnapshot, arraySnapshot.length);
        this.highlightIndices = Arrays.copyOf(highlightIndices, highlightIndices.length);
        this.description      = description;
        this.comparisons      = comparisons;
        this.swaps            = swaps;
        this.searchTarget     = -1;
        this.found            = false;
        this.foundIndex       = -1;
    }

    // Searching constructor
    public AlgorithmStep(int[] arraySnapshot, int[] highlightIndices,
                         String description, int comparisons,
                         int searchTarget, boolean found, int foundIndex) {
        this.arraySnapshot    = Arrays.copyOf(arraySnapshot, arraySnapshot.length);
        this.highlightIndices = Arrays.copyOf(highlightIndices, highlightIndices.length);
        this.description      = description;
        this.comparisons      = comparisons;
        this.swaps            = 0;
        this.searchTarget     = searchTarget;
        this.found            = found;
        this.foundIndex       = foundIndex;
    }

    public int[]  getArraySnapshot()    { return arraySnapshot; }
    public int[]  getHighlightIndices() { return highlightIndices; }
    public String getDescription()      { return description; }
    public int    getComparisons()      { return comparisons; }
    public int    getSwaps()            { return swaps; }
    public int    getSearchTarget()     { return searchTarget; }
    public boolean isFound()            { return found; }
    public int    getFoundIndex()       { return foundIndex; }
}