package com.algolearn.patterns;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T extends Observer> {

    private final List<T> observers = new ArrayList<>();

    public void addObserver(T observer) {
        if (!observers.contains(observer))
            observers.add(observer);
    }

    public void removeObserver(T observer) {
        observers.remove(observer);
    }

    protected void notifyStepChanged(int currentStep, int totalSteps) {
        observers.forEach(o -> o.onStepChanged(currentStep, totalSteps));
    }

    protected void notifyComplete() {
        observers.forEach(Observer::onSimulationComplete);
    }

    protected void notifyReset() {
        observers.forEach(Observer::onSimulationReset);
    }
}