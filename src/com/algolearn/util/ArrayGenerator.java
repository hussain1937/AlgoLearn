package com.algolearn.util;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public final class ArrayGenerator {

    private static final Random RANDOM = new Random();

    private ArrayGenerator() {}

    public static int[] randomArray(int size, int min, int max) {
        return IntStream.generate(() -> RANDOM.nextInt(max - min + 1) + min)
                        .limit(size)
                        .toArray();
    }

    public static int[] sortedArray(int size, int min, int max) {
        return Arrays.stream(randomArray(size, min, max))
                     .sorted()
                     .toArray();
    }

    public static int[] reverseSortedArray(int size, int min, int max) {
        int[] arr = sortedArray(size, min, max);
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
        return arr;
    }

    public static int[] parseFromString(String input) {
        return Arrays.stream(input.trim().split("[,\\s]+"))
                     .mapToInt(Integer::parseInt)
                     .toArray();
    }
}