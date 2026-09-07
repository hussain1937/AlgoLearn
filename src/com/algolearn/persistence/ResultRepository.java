package com.algolearn.persistence;

import com.algolearn.model.SimulationResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResultRepository {

    private static final String FILE_PATH = "algolearn_results.dat";
    private final List<SimulationResult> cache;

    public ResultRepository() {
        this.cache = new ArrayList<>();
        loadFromFile();
    }

    public void save(SimulationResult result) {
        cache.add(result);
        persistToFile();
    }

    public List<SimulationResult> findAll() {
        return new ArrayList<>(cache);
    }

    public void clearAll() {
        cache.clear();
        persistToFile();
    }

    public int count() {
        return cache.size();
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(file))) {
            cache.addAll((List<SimulationResult>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Could not load results: " + e.getMessage());
        }
    }

    private void persistToFile() {
        try (ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(new ArrayList<>(cache));
        } catch (IOException e) {
            System.err.println("Could not save results: " + e.getMessage());
        }
    }
}