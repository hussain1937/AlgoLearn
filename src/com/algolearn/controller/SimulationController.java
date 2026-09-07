package com.algolearn.controller;

import com.algolearn.model.AlgorithmStep;
import com.algolearn.model.SimulationResult;
import com.algolearn.patterns.*;
import com.algolearn.persistence.ResultRepository;
import com.algolearn.util.ArrayGenerator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.List;

public class SimulationController extends Observable<Observer> {

    private AlgorithmStrategy currentStrategy;
    private int[]              currentArray;
    private List<AlgorithmStep> steps;
    private int                currentStepIndex;
    private Timeline           animationTimeline;
    private final ResultRepository repository;
    private long               simulationStartTime;
    private boolean            isPlaying;
    private double             speedMultiplier = 1.0;

    public SimulationController() {
        this.repository    = new ResultRepository();
        this.currentArray  = ArrayGenerator.randomArray(12, 1, 99);
        this.currentStrategy = SortingStrategy.bubbleSort();
    }

    public void setStrategy(AlgorithmStrategy strategy) {
        this.currentStrategy = strategy;
        resetSimulation();
    }

    public void setArray(int[] array) {
        this.currentArray = array.clone();
        resetSimulation();
    }

    public int[] getCurrentArray() {
        return currentArray.clone();
    }

    public void prepareSimulation(int searchTarget) {
        if (currentStrategy instanceof SearchingStrategy) {
            ((SearchingStrategy) currentStrategy).setSearchTarget(searchTarget);
        }
        simulationStartTime = System.currentTimeMillis();
        steps = currentStrategy.execute(currentArray);
        currentStepIndex = 0;
        notifyStepChanged(0, steps.size());
    }

    public void prepareSimulation() {
        prepareSimulation(0);
    }

    public void play() {
        if (steps == null) prepareSimulation();
        if (isPlaying) return;
        isPlaying = true;
        int delayMs = (int)(1000.0 / speedMultiplier);
        animationTimeline = new Timeline(
            new KeyFrame(Duration.millis(delayMs), e -> stepForward()));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
    }

    public void pause() {
        isPlaying = false;
        if (animationTimeline != null) animationTimeline.stop();
    }

    public void stepForward() {
        if (steps == null) prepareSimulation();
        if (currentStepIndex < steps.size() - 1) {
            currentStepIndex++;
            notifyStepChanged(currentStepIndex, steps.size());
        } else {
            pause();
            saveResult();
            notifyComplete();
        }
    }

    public void stepBackward() {
        if (steps == null) return;
        if (currentStepIndex > 0) {
            currentStepIndex--;
            notifyStepChanged(currentStepIndex, steps.size());
        }
    }

    public void resetSimulation() {
        pause();
        steps = null;
        currentStepIndex = 0;
        notifyReset();
    }

    public void setSpeed(double multiplier) {
        this.speedMultiplier = multiplier;
        if (isPlaying) { pause(); play(); }
    }

    public AlgorithmStep getCurrentStep() {
        if (steps == null || steps.isEmpty()) return null;
        return steps.get(currentStepIndex);
    }

    public int     getCurrentStepIndex()   { return currentStepIndex; }
    public int     getTotalSteps()         { return steps != null ? steps.size() : 0; }
    public boolean isPlaying()             { return isPlaying; }
    public AlgorithmStrategy getCurrentStrategy() { return currentStrategy; }

    private void saveResult() {
        if (steps == null || steps.isEmpty()) return;
        AlgorithmStep last = steps.get(steps.size() - 1);
        long duration = System.currentTimeMillis() - simulationStartTime;
        repository.save(new SimulationResult(
            currentStrategy.getName(),
            currentArray.length,
            steps.size(),
            last.getComparisons(),
            last.getSwaps(),
            duration));
    }

    public List<SimulationResult> getHistory() { return repository.findAll(); }
    public void clearHistory()                 { repository.clearAll(); }
}