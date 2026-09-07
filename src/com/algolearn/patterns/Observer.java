package com.algolearn.patterns;

public interface Observer {
    void onStepChanged(int currentStep, int totalSteps);
    void onSimulationComplete();
    void onSimulationReset();
}