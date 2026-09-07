package com.algolearn.view;

import com.algolearn.controller.SimulationController;
import com.algolearn.model.AlgorithmStep;
import com.algolearn.model.SimulationResult;
import com.algolearn.patterns.*;
import com.algolearn.util.ArrayGenerator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.List;

public class MainView implements Observer {

    private final SimulationController controller;
    private final Stage primaryStage;

    private VisualizationPane visualizationPane;
    private Label stepDescriptionLabel, statsLabel, stepCounterLabel, complexityLabel;
    private ProgressBar progressBar;
    private Button playPauseBtn, stepFwdBtn, stepBwdBtn, resetBtn;
    private Slider speedSlider;
    private ComboBox<String> algorithmCombo, arraySizeCombo, arrayTypeCombo;
    private TextField customArrayField, searchTargetField;
    private TextArea pseudoCodeArea, historyArea;

    private static final String BG_DARK    = "#171614";
    private static final String BG_SURFACE = "#1c1b19";
    private static final String BG_CARD    = "#201f1d";
    private static final String ACCENT     = "#4f98a3";
    private static final String TEXT_MAIN  = "#cdccca";
    private static final String TEXT_MUTED = "#797876";
    private static final String BORDER     = "#393836";

    public MainView(SimulationController controller, Stage primaryStage) {
        this.controller   = controller;
        this.primaryStage = primaryStage;
        controller.addObserver(this);
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG_DARK + ";");
        root.setTop(buildHeader());
        root.setCenter(buildMainContent());
        root.setBottom(buildStatusBar());
        Scene scene = new Scene(root, 1280, 760);
        primaryStage.setTitle("AlgoLearn — Educational Algorithm Simulator");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
        visualizationPane.renderArray(controller.getCurrentArray());
        updateInfoPanel();
    }

    private HBox buildHeader() {
        HBox h = new HBox(16);
        h.setStyle("-fx-background-color:" + BG_SURFACE + ";-fx-border-color:" + BORDER
                 + ";-fx-border-width:0 0 1 0;");
        h.setPadding(new Insets(14, 24, 14, 24));
        h.setAlignment(Pos.CENTER_LEFT);
        Text logo = new Text("AlgoLearn");
        logo.setFont(Font.font("System", FontWeight.BOLD, 22));
        logo.setFill(Color.web(ACCENT));
        Text sub = new Text("Interactive Algorithm Simulator");
        sub.setFont(Font.font("System", 13));
        sub.setFill(Color.web(TEXT_MUTED));
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label badge = new Label("CS Educational Tool");
        badge.setStyle("-fx-background-color:" + ACCENT + "22;-fx-text-fill:" + ACCENT
                     + ";-fx-padding:4 10;-fx-background-radius:20;-fx-font-size:11;");
        h.getChildren().addAll(logo, new Separator(Orientation.VERTICAL), sub, sp, badge);
        return h;
    }

    private HBox buildMainContent() {
        HBox c = new HBox(0);
        VBox left = buildLeftPanel();
        left.setPrefWidth(280); left.setMinWidth(260);
        VBox center = buildCenterPanel();
        HBox.setHgrow(center, Priority.ALWAYS);
        VBox right = buildRightPanel();
        right.setPrefWidth(280); right.setMinWidth(260);
        c.getChildren().addAll(left, center, right);
        return c;
    }

    private VBox buildLeftPanel() {
        VBox p = new VBox(12);
        p.setStyle("-fx-background-color:" + BG_SURFACE + ";-fx-border-color:" + BORDER
                 + ";-fx-border-width:0 1 0 0;");
        p.setPadding(new Insets(20, 16, 20, 16));

        algorithmCombo = new ComboBox<>();
        algorithmCombo.getItems().addAll("Bubble Sort","Selection Sort","Insertion Sort",
            "Merge Sort","Quick Sort","── Searching ──","Linear Search","Binary Search");
        algorithmCombo.setValue("Bubble Sort");
        styleCombo(algorithmCombo);
        algorithmCombo.setMaxWidth(Double.MAX_VALUE);
        algorithmCombo.setOnAction(e -> onAlgorithmSelected());

        arraySizeCombo = new ComboBox<>();
        arraySizeCombo.getItems().addAll("6","8","10","12","15","18","20");
        arraySizeCombo.setValue("12");
        styleCombo(arraySizeCombo);
        arraySizeCombo.setMaxWidth(Double.MAX_VALUE);

        arrayTypeCombo = new ComboBox<>();
        arrayTypeCombo.getItems().addAll("Random","Sorted","Reverse Sorted");
        arrayTypeCombo.setValue("Random");
        styleCombo(arrayTypeCombo);
        arrayTypeCombo.setMaxWidth(Double.MAX_VALUE);

        Button genBtn = primaryButton("Generate Array");
        genBtn.setOnAction(e -> onGenerateArray());

        customArrayField = new TextField();
        styleTextField(customArrayField);
        customArrayField.setPromptText("e.g. 5,3,8,1,9,2");

        Button applyBtn = secondaryButton("Apply Custom Array");
        applyBtn.setOnAction(e -> onApplyCustomArray());

        searchTargetField = new TextField("42");
        styleTextField(searchTargetField);
        searchTargetField.setPromptText("Value to search");

        speedSlider = new Slider(0.25, 4.0, 1.0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(1.0);
        speedSlider.setStyle("-fx-accent:" + ACCENT + ";");

        Label speedVal = new Label("1.0x");
        speedVal.setStyle("-fx-text-fill:" + ACCENT + ";-fx-font-size:12;");
        speedSlider.valueProperty().addListener((obs, ov, nv) -> {
            double v = Math.round(nv.doubleValue() * 4) / 4.0;
            speedVal.setText(v + "x");
            controller.setSpeed(v);
        });

        p.getChildren().addAll(
            sectionLabel("Algorithm"), algorithmCombo, new Separator(),
            sectionLabel("Array Configuration"),
            mutedLabel("Size"), arraySizeCombo,
            mutedLabel("Type"), arrayTypeCombo, genBtn, new Separator(),
            mutedLabel("Custom Array (comma-separated)"),
            customArrayField, applyBtn, new Separator(),
            sectionLabel("Search Target"), searchTargetField, new Separator(),
            sectionLabel("Animation Speed"), speedSlider, speedVal);
        return p;
    }

    private VBox buildCenterPanel() {
        VBox p = new VBox(12);
        p.setPadding(new Insets(20));
        p.setStyle("-fx-background-color:" + BG_DARK + ";");

        visualizationPane = new VisualizationPane();
        visualizationPane.setPrefHeight(280);
        VBox.setVgrow(visualizationPane, Priority.ALWAYS);

        stepDescriptionLabel = new Label("Select an algorithm and press Play to start.");
        stepDescriptionLabel.setStyle("-fx-text-fill:" + TEXT_MAIN
            + ";-fx-font-size:13;-fx-padding:10 16;-fx-background-color:" + BG_CARD
            + ";-fx-background-radius:8;");
        stepDescriptionLabel.setWrapText(true);
        stepDescriptionLabel.setMaxWidth(Double.MAX_VALUE);

        statsLabel = new Label("Comparisons: 0  |  Swaps: 0");
        statsLabel.setStyle("-fx-text-fill:" + ACCENT
            + ";-fx-font-size:12;-fx-font-weight:bold;");

        stepCounterLabel = new Label("Step: 0 / 0");
        stepCounterLabel.setStyle("-fx-text-fill:" + TEXT_MUTED + ";-fx-font-size:12;");

        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        HBox statsBar = new HBox(16, statsLabel, sp, stepCounterLabel);
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setPadding(new Insets(6, 12, 6, 12));
        statsBar.setStyle("-fx-background-color:" + BG_SURFACE + ";-fx-background-radius:8;");

        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(6);
        progressBar.setStyle("-fx-accent:" + ACCENT + ";");

        HBox controls = buildControls();
        p.getChildren().addAll(visualizationPane, stepDescriptionLabel,
            statsBar, progressBar, controls);
        return p;
    }

    private HBox buildControls() {
        stepBwdBtn   = controlButton("⏮ Back");
        playPauseBtn = primaryButton("▶ Play");
        stepFwdBtn   = controlButton("Step ⏭");
        resetBtn     = secondaryButton("↺ Reset");

        playPauseBtn.setOnAction(e -> onPlayPause());
        stepFwdBtn.setOnAction(e -> { controller.pause(); controller.stepForward(); });
        stepBwdBtn.setOnAction(e -> { controller.pause(); controller.stepBackward();
            updateView(); });
        resetBtn.setOnAction(e -> controller.resetSimulation());
        playPauseBtn.setPrefWidth(120);

        HBox c = new HBox(10, stepBwdBtn, playPauseBtn, stepFwdBtn, resetBtn);
        c.setAlignment(Pos.CENTER);
        return c;
    }

    private VBox buildRightPanel() {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.setStyle("-fx-background-color:" + BG_SURFACE + ";");
        tabs.getTabs().addAll(
            new Tab("Info", buildInfoTab()),
            new Tab("History", buildHistoryTab()));
        VBox p = new VBox(tabs);
        p.setStyle("-fx-background-color:" + BG_SURFACE + ";-fx-border-color:" + BORDER
                 + ";-fx-border-width:0 0 0 1;");
        VBox.setVgrow(tabs, Priority.ALWAYS);
        return p;
    }

    private ScrollPane buildInfoTab() {
        VBox c = new VBox(14);
        c.setPadding(new Insets(16));
        c.setStyle("-fx-background-color:" + BG_SURFACE + ";");

        complexityLabel = new Label();
        complexityLabel.setStyle("-fx-text-fill:" + TEXT_MAIN
            + ";-fx-font-size:12;-fx-background-color:" + BG_CARD
            + ";-fx-padding:10;-fx-background-radius:8;");
        complexityLabel.setWrapText(true);
        complexityLabel.setMaxWidth(Double.MAX_VALUE);

        pseudoCodeArea = new TextArea();
        pseudoCodeArea.setEditable(false);
        pseudoCodeArea.setStyle("-fx-control-inner-background:" + BG_CARD
            + ";-fx-text-fill:" + TEXT_MAIN
            + ";-fx-font-family:'Courier New';-fx-font-size:11;");
        pseudoCodeArea.setPrefHeight(220);
        pseudoCodeArea.setWrapText(true);

        c.getChildren().addAll(complexityLabel, sectionLabel("Pseudocode"), pseudoCodeArea);
        ScrollPane sc = new ScrollPane(c);
        sc.setFitToWidth(true);
        sc.setStyle("-fx-background-color:" + BG_SURFACE
                  + ";-fx-background:" + BG_SURFACE + ";");
        return sc;
    }

    private VBox buildHistoryTab() {
        VBox c = new VBox(10);
        c.setPadding(new Insets(16));
        c.setStyle("-fx-background-color:" + BG_SURFACE + ";");

        historyArea = new TextArea();
        historyArea.setEditable(false);
        historyArea.setStyle("-fx-control-inner-background:" + BG_CARD
            + ";-fx-text-fill:" + TEXT_MAIN
            + ";-fx-font-family:'Courier New';-fx-font-size:10;");
        historyArea.setPrefHeight(360);
        VBox.setVgrow(historyArea, Priority.ALWAYS);

        Button refreshBtn = secondaryButton("↻ Refresh");
        refreshBtn.setOnAction(e -> refreshHistory());
        Button clearBtn = new Button("🗑 Clear");
        clearBtn.setStyle("-fx-background-color:#a13544;-fx-text-fill:white;"
            + "-fx-font-size:12;-fx-background-radius:6;-fx-padding:6 12;");
        clearBtn.setOnAction(e -> { controller.clearHistory(); refreshHistory(); });

        c.getChildren().addAll(sectionLabel("Simulation History"), historyArea,
            new HBox(8, refreshBtn, clearBtn));
        refreshHistory();
        return c;
    }

    private HBox buildStatusBar() {
        HBox b = new HBox();
        b.setPadding(new Insets(8, 20, 8, 20));
        b.setStyle("-fx-background-color:" + BG_SURFACE + ";-fx-border-color:" + BORDER
                 + ";-fx-border-width:1 0 0 0;");
        Label lbl = new Label(
            "Ready  |  Advanced Programming Project — Educational Algorithm Simulator");
        lbl.setStyle("-fx-text-fill:" + TEXT_MUTED + ";-fx-font-size:11;");
        b.getChildren().add(lbl);
        return b;
    }

    // ── Event Handlers ───────────────────────────────────────────────────────

    private void onAlgorithmSelected() {
        String s = algorithmCombo.getValue();
        if (s == null || s.startsWith("──")) return;
        AlgorithmStrategy strategy;
        switch (s) {
            case "Bubble Sort":    strategy = SortingStrategy.bubbleSort();    break;
            case "Selection Sort": strategy = SortingStrategy.selectionSort(); break;
            case "Insertion Sort": strategy = SortingStrategy.insertionSort(); break;
            case "Merge Sort":     strategy = SortingStrategy.mergeSort();     break;
            case "Quick Sort":     strategy = SortingStrategy.quickSort();     break;
            case "Linear Search":  strategy = SearchingStrategy.linearSearch();break;
            case "Binary Search":  strategy = SearchingStrategy.binarySearch();break;
            default:               strategy = SortingStrategy.bubbleSort();
        }
        controller.setStrategy(strategy);
        updateInfoPanel();
    }

    private void onGenerateArray() {
        int size = Integer.parseInt(arraySizeCombo.getValue());
        int[] arr;
        switch (arrayTypeCombo.getValue()) {
            case "Sorted":         arr = ArrayGenerator.sortedArray(size, 1, 99);        break;
            case "Reverse Sorted": arr = ArrayGenerator.reverseSortedArray(size, 1, 99); break;
            default:               arr = ArrayGenerator.randomArray(size, 1, 99);
        }
        controller.setArray(arr);
        visualizationPane.renderArray(arr);
    }

    private void onApplyCustomArray() {
        try {
            int[] arr = ArrayGenerator.parseFromString(customArrayField.getText());
            if (arr.length < 2) { showAlert("Error", "Enter at least 2 numbers."); return; }
            controller.setArray(arr);
            visualizationPane.renderArray(arr);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input. Use numbers separated by commas.");
        }
    }

    private void onPlayPause() {
        if (controller.isPlaying()) {
            controller.pause();
        } else {
            if (controller.getTotalSteps() == 0) {
                int t = 0;
                try { t = Integer.parseInt(searchTargetField.getText().trim()); }
                catch (NumberFormatException ignored) {}
                controller.prepareSimulation(t);
            }
            controller.play();
        }
    }

    // ── Observer Callbacks ───────────────────────────────────────────────────

    @Override
    public void onStepChanged(int s, int t) {
        Platform.runLater(() -> {
            updateView();
            playPauseBtn.setText(controller.isPlaying() ? "⏸ Pause" : "▶ Play");
        });
    }

    @Override
    public void onSimulationComplete() {
        Platform.runLater(() -> {
            playPauseBtn.setText("▶ Play");
            stepDescriptionLabel.setText("Simulation complete! Check History tab.");
            stepDescriptionLabel.setStyle("-fx-text-fill:#6daa45;-fx-font-size:13;"
                + "-fx-padding:10 16;-fx-background-color:#6daa4518;"
                + "-fx-background-radius:8;");
            refreshHistory();
        });
    }

    @Override
    public void onSimulationReset() {
        Platform.runLater(() -> {
            playPauseBtn.setText("▶ Play");
            stepDescriptionLabel.setText("Select an algorithm and press Play to start.");
            stepDescriptionLabel.setStyle("-fx-text-fill:" + TEXT_MAIN
                + ";-fx-font-size:13;-fx-padding:10 16;-fx-background-color:" + BG_CARD
                + ";-fx-background-radius:8;");
            statsLabel.setText("Comparisons: 0  |  Swaps: 0");
            stepCounterLabel.setText("Step: 0 / 0");
            progressBar.setProgress(0);
            visualizationPane.renderArray(controller.getCurrentArray());
        });
    }

    private void updateView() {
        AlgorithmStep step = controller.getCurrentStep();
        if (step == null) return;
        visualizationPane.render(step);
        stepDescriptionLabel.setText(step.getDescription());
        stepDescriptionLabel.setStyle("-fx-text-fill:" + TEXT_MAIN
            + ";-fx-font-size:13;-fx-padding:10 16;-fx-background-color:" + BG_CARD
            + ";-fx-background-radius:8;");
        statsLabel.setText("Comparisons: " + step.getComparisons()
            + "  |  Swaps: " + step.getSwaps());
        int cur = controller.getCurrentStepIndex() + 1;
        int tot = controller.getTotalSteps();
        stepCounterLabel.setText("Step: " + cur + " / " + tot);
        progressBar.setProgress(tot > 0 ? (double) cur / tot : 0);
    }

    private void updateInfoPanel() {
        String name = algorithmCombo.getValue();
        if (name == null || name.startsWith("──")) return;
        String[] info = getAlgorithmInfo(name);
        complexityLabel.setText("Time: " + info[0] + "\nSpace: " + info[1]
            + "\n\n" + info[2]);
        pseudoCodeArea.setText(info[3]);
    }

    private void refreshHistory() {
        List<SimulationResult> history = controller.getHistory();
        if (history.isEmpty()) { historyArea.setText("No simulations run yet."); return; }
        StringBuilder sb = new StringBuilder();
        for (int i = history.size()-1; i >= 0; i--)
            sb.append(history.get(i)).append("\n");
        historyArea.setText(sb.toString());
    }

    private String[] getAlgorithmInfo(String name) {
        switch (name) {
            case "Bubble Sort":    return new String[]{"O(n^2) worst, O(n) best","O(1)",
                "Repeatedly swaps adjacent elements if in wrong order.",
                "for i = 0 to n-2:\n  for j = 0 to n-i-2:\n    if arr[j] > arr[j+1]:\n      swap(arr[j], arr[j+1])"};
            case "Selection Sort": return new String[]{"O(n^2) all cases","O(1)",
                "Finds minimum and places it at the correct position each pass.",
                "for i = 0 to n-2:\n  minIdx = i\n  for j = i+1 to n-1:\n    if arr[j] < arr[minIdx]: minIdx = j\n  swap(arr[i], arr[minIdx])"};
            case "Insertion Sort": return new String[]{"O(n^2) worst, O(n) best","O(1)",
                "Builds sorted array one element at a time by insertion.",
                "for i = 1 to n-1:\n  key = arr[i]; j = i-1\n  while j>=0 and arr[j]>key:\n    arr[j+1]=arr[j]; j--\n  arr[j+1] = key"};
            case "Merge Sort":     return new String[]{"O(n log n) all cases","O(n)",
                "Divide-and-conquer: split, sort recursively, merge.",
                "mergeSort(arr, l, r):\n  if l < r:\n    mid=(l+r)/2\n    mergeSort(arr,l,mid)\n    mergeSort(arr,mid+1,r)\n    merge(arr,l,mid,r)"};
            case "Quick Sort":     return new String[]{"O(n log n) avg, O(n^2) worst","O(log n)",
                "Picks pivot, partitions array around it recursively.",
                "quickSort(arr, low, high):\n  if low < high:\n    pi=partition(arr,low,high)\n    quickSort(arr,low,pi-1)\n    quickSort(arr,pi+1,high)"};
            case "Linear Search":  return new String[]{"O(n) worst, O(1) best","O(1)",
                "Checks each element sequentially until target is found.",
                "for i = 0 to n-1:\n  if arr[i] == target:\n    return i\nreturn -1"};
            case "Binary Search":  return new String[]{"O(log n) worst, O(1) best","O(1)",
                "Halves search range each step. Array MUST be sorted.",
                "left=0; right=n-1\nwhile left<=right:\n  mid=(left+right)/2\n  if arr[mid]==target: return mid\n  elif arr[mid]<target: left=mid+1\n  else: right=mid-1\nreturn -1"};
            default: return new String[]{"N/A","N/A","Select an algorithm.",""};
        }
    }

    // ── Style Helpers ────────────────────────────────────────────────────────

    private Label sectionLabel(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-text-fill:" + TEXT_MAIN + ";-fx-font-weight:bold;-fx-font-size:13;");
        return l;
    }
    private Label mutedLabel(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-text-fill:" + TEXT_MUTED + ";-fx-font-size:11;");
        return l;
    }
    private Button primaryButton(String t) {
        Button b = new Button(t);
        b.setStyle("-fx-background-color:" + ACCENT + ";-fx-text-fill:white;"
            + "-fx-font-size:13;-fx-font-weight:bold;-fx-background-radius:8;"
            + "-fx-padding:8 18;-fx-cursor:hand;");
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }
    private Button secondaryButton(String t) {
        Button b = new Button(t);
        b.setStyle("-fx-background-color:" + BG_CARD + ";-fx-text-fill:" + TEXT_MAIN
            + ";-fx-font-size:12;-fx-background-radius:8;-fx-border-color:" + BORDER
            + ";-fx-border-radius:8;-fx-padding:6 14;-fx-cursor:hand;");
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }
    private Button controlButton(String t) {
        Button b = new Button(t);
        b.setStyle("-fx-background-color:" + BG_CARD + ";-fx-text-fill:" + TEXT_MAIN
            + ";-fx-font-size:12;-fx-background-radius:8;-fx-border-color:" + BORDER
            + ";-fx-border-radius:8;-fx-padding:8 16;-fx-cursor:hand;");
        return b;
    }
    private void styleCombo(ComboBox<?> c) {
        c.setStyle("-fx-background-color:" + BG_CARD + ";-fx-text-fill:" + TEXT_MAIN
            + ";-fx-border-color:" + BORDER + ";-fx-border-radius:6;-fx-background-radius:6;");
    }
    private void styleTextField(TextField f) {
        f.setStyle("-fx-background-color:" + BG_CARD + ";-fx-text-fill:" + TEXT_MAIN
            + ";-fx-border-color:" + BORDER + ";-fx-border-radius:6;-fx-background-radius:6;"
            + "-fx-prompt-text-fill:" + TEXT_MUTED + ";");
    }
    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }
}