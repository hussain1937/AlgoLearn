package com.algolearn.model;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmEngine {

    // ─── Bubble Sort ────────────────────────────────────────────────────────
    public static List<AlgorithmStep> bubbleSort(int[] input) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int[] arr = input.clone();
        int n = arr.length, comparisons = 0, swaps = 0;
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Starting Bubble Sort on " + n + " elements", comparisons, swaps));
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                steps.add(new AlgorithmStep(arr, new int[]{j, j+1},
                    "Comparing arr["+j+"]="+arr[j]+" with arr["+(j+1)+"]="+arr[j+1],
                    comparisons, swaps));
                if (arr[j] > arr[j+1]) {
                    int tmp = arr[j]; arr[j] = arr[j+1]; arr[j+1] = tmp;
                    swaps++; swapped = true;
                    steps.add(new AlgorithmStep(arr, new int[]{j, j+1},
                        "Swapped arr["+j+"] and arr["+(j+1)+"]", comparisons, swaps));
                }
            }
            if (!swapped) {
                steps.add(new AlgorithmStep(arr, new int[]{},
                    "No swaps in pass "+(i+1)+" — already sorted!", comparisons, swaps));
                break;
            }
        }
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Bubble Sort complete!", comparisons, swaps));
        return steps;
    }

    // ─── Selection Sort ─────────────────────────────────────────────────────
    public static List<AlgorithmStep> selectionSort(int[] input) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int[] arr = input.clone();
        int n = arr.length, comparisons = 0, swaps = 0;
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Starting Selection Sort on " + n + " elements", comparisons, swaps));
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            steps.add(new AlgorithmStep(arr, new int[]{i},
                "Pass "+(i+1)+": finding minimum from index "+i, comparisons, swaps));
            for (int j = i + 1; j < n; j++) {
                comparisons++;
                steps.add(new AlgorithmStep(arr, new int[]{minIdx, j},
                    "Comparing arr["+j+"]="+arr[j]+" with min arr["+minIdx+"]="+arr[minIdx],
                    comparisons, swaps));
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                    steps.add(new AlgorithmStep(arr, new int[]{minIdx},
                        "New minimum: arr["+minIdx+"]="+arr[minIdx], comparisons, swaps));
                }
            }
            if (minIdx != i) {
                int tmp = arr[i]; arr[i] = arr[minIdx]; arr[minIdx] = tmp;
                swaps++;
                steps.add(new AlgorithmStep(arr, new int[]{i, minIdx},
                    "Placed minimum "+arr[i]+" at position "+i, comparisons, swaps));
            }
        }
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Selection Sort complete!", comparisons, swaps));
        return steps;
    }

    // ─── Insertion Sort ─────────────────────────────────────────────────────
    public static List<AlgorithmStep> insertionSort(int[] input) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int[] arr = input.clone();
        int n = arr.length, comparisons = 0, swaps = 0;
        steps.add(new AlgorithmStep(arr, new int[]{0},
            "Insertion Sort. arr[0]="+arr[0]+" is trivially sorted.", comparisons, swaps));
        for (int i = 1; i < n; i++) {
            int key = arr[i], j = i - 1;
            steps.add(new AlgorithmStep(arr, new int[]{i},
                "Inserting key="+key+" (index "+i+") into sorted portion",
                comparisons, swaps));
            while (j >= 0 && arr[j] > key) {
                comparisons++;
                arr[j+1] = arr[j];
                swaps++;
                steps.add(new AlgorithmStep(arr, new int[]{j, j+1},
                    "Shifted arr["+j+"] right to make room for key="+key,
                    comparisons, swaps));
                j--;
            }
            if (j >= 0) comparisons++;
            arr[j+1] = key;
            steps.add(new AlgorithmStep(arr, new int[]{j+1},
                "Placed key="+key+" at index "+(j+1), comparisons, swaps));
        }
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Insertion Sort complete!", comparisons, swaps));
        return steps;
    }

    // ─── Merge Sort ─────────────────────────────────────────────────────────
    public static List<AlgorithmStep> mergeSort(int[] input) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int[] arr = input.clone();
        int[] cmp = {0}, sw = {0};
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Starting Merge Sort (divide & conquer)", cmp[0], sw[0]));
        mergeSortHelper(arr, 0, arr.length - 1, steps, cmp, sw);
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Merge Sort complete!", cmp[0], sw[0]));
        return steps;
    }

    private static void mergeSortHelper(int[] arr, int l, int r,
            List<AlgorithmStep> steps, int[] cmp, int[] sw) {
        if (l < r) {
            int mid = (l + r) / 2;
            steps.add(new AlgorithmStep(arr, new int[]{l, mid, r},
                "Dividing: ["+l+".."+mid+"] and ["+(mid+1)+".."+r+"]",
                cmp[0], sw[0]));
            mergeSortHelper(arr, l, mid, steps, cmp, sw);
            mergeSortHelper(arr, mid + 1, r, steps, cmp, sw);
            merge(arr, l, mid, r, steps, cmp, sw);
        }
    }

    private static void merge(int[] arr, int l, int mid, int r,
            List<AlgorithmStep> steps, int[] cmp, int[] sw) {
        int n1 = mid - l + 1, n2 = r - mid;
        int[] L = new int[n1], R = new int[n2];
        System.arraycopy(arr, l, L, 0, n1);
        System.arraycopy(arr, mid + 1, R, 0, n2);
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            cmp[0]++;
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else { arr[k++] = R[j++]; sw[0]++; }
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
        int[] hi = new int[r - l + 1];
        for (int x = 0; x < hi.length; x++) hi[x] = l + x;
        steps.add(new AlgorithmStep(arr, hi,
            "Merged subarray ["+l+".."+r+"]", cmp[0], sw[0]));
    }

    // ─── Quick Sort ─────────────────────────────────────────────────────────
    public static List<AlgorithmStep> quickSort(int[] input) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int[] arr = input.clone();
        int[] cmp = {0}, sw = {0};
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Starting Quick Sort", cmp[0], sw[0]));
        quickSortHelper(arr, 0, arr.length - 1, steps, cmp, sw);
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Quick Sort complete!", cmp[0], sw[0]));
        return steps;
    }

    private static void quickSortHelper(int[] arr, int low, int high,
            List<AlgorithmStep> steps, int[] cmp, int[] sw) {
        if (low < high) {
            int pi = partition(arr, low, high, steps, cmp, sw);
            quickSortHelper(arr, low, pi - 1, steps, cmp, sw);
            quickSortHelper(arr, pi + 1, high, steps, cmp, sw);
        }
    }

    private static int partition(int[] arr, int low, int high,
            List<AlgorithmStep> steps, int[] cmp, int[] sw) {
        int pivot = arr[high];
        steps.add(new AlgorithmStep(arr, new int[]{high},
            "Pivot = "+pivot+" at index "+high, cmp[0], sw[0]));
        int i = low - 1;
        for (int j = low; j < high; j++) {
            cmp[0]++;
            steps.add(new AlgorithmStep(arr, new int[]{j, high},
                "Comparing arr["+j+"]="+arr[j]+" with pivot="+pivot,
                cmp[0], sw[0]));
            if (arr[j] <= pivot) {
                i++;
                int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
                sw[0]++;
                steps.add(new AlgorithmStep(arr, new int[]{i, j},
                    "Swapped arr["+i+"]="+arr[i]+" and arr["+j+"]="+arr[j],
                    cmp[0], sw[0]));
            }
        }
        int tmp = arr[i+1]; arr[i+1] = arr[high]; arr[high] = tmp;
        sw[0]++;
        steps.add(new AlgorithmStep(arr, new int[]{i+1},
            "Pivot "+pivot+" placed at final position "+(i+1), cmp[0], sw[0]));
        return i + 1;
    }

    // ─── Linear Search ──────────────────────────────────────────────────────
    public static List<AlgorithmStep> linearSearch(int[] input, int target) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int[] arr = input.clone();
        int comparisons = 0;
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Starting Linear Search for target=" + target,
            comparisons, target, false, -1));
        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            steps.add(new AlgorithmStep(arr, new int[]{i},
                "Checking arr["+i+"]="+arr[i]+" == "+target+"?",
                comparisons, target, false, -1));
            if (arr[i] == target) {
                steps.add(new AlgorithmStep(arr, new int[]{i},
                    "Found "+target+" at index "+i+"!",
                    comparisons, target, true, i));
                return steps;
            }
        }
        steps.add(new AlgorithmStep(arr, new int[]{},
            target+" not found after "+comparisons+" comparisons.",
            comparisons, target, false, -1));
        return steps;
    }

    // ─── Binary Search ──────────────────────────────────────────────────────
    public static List<AlgorithmStep> binarySearch(int[] input, int target) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int[] arr = input.clone();
        int comparisons = 0;
        steps.add(new AlgorithmStep(arr, new int[]{},
            "Starting Binary Search for target="+target+" (sorted array)",
            comparisons, target, false, -1));
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisons++;
            steps.add(new AlgorithmStep(arr, new int[]{left, mid, right},
                "Range ["+left+".."+right+"], mid="+mid+", arr[mid]="+arr[mid],
                comparisons, target, false, -1));
            if (arr[mid] == target) {
                steps.add(new AlgorithmStep(arr, new int[]{mid},
                    "Found "+target+" at index "+mid+"!",
                    comparisons, target, true, mid));
                return steps;
            } else if (arr[mid] < target) {
                steps.add(new AlgorithmStep(arr, new int[]{mid},
                    arr[mid]+" < "+target+" -> search right half",
                    comparisons, target, false, -1));
                left = mid + 1;
            } else {
                steps.add(new AlgorithmStep(arr, new int[]{mid},
                    arr[mid]+" > "+target+" -> search left half",
                    comparisons, target, false, -1));
                right = mid - 1;
            }
        }
        steps.add(new AlgorithmStep(arr, new int[]{},
            target+" not found in array.",
            comparisons, target, false, -1));
        return steps;
    }
}