package com.algolearn;

import com.algolearn.controller.SimulationController;
import com.algolearn.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

public class AlgoLearnApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        SimulationController controller = new SimulationController();
        MainView view = new MainView(controller, primaryStage);
        view.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}