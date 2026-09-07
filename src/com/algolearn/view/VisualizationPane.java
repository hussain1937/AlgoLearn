package com.algolearn.view;

import com.algolearn.model.AlgorithmStep;
import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VisualizationPane extends Pane {

    private static final Color BAR_DEFAULT   = Color.web("#4f98a3");
    private static final Color BAR_HIGHLIGHT = Color.web("#e8af34");
    private static final Color BAR_FOUND     = Color.web("#6daa45");

    private int[] currentArray    = {};
    private int[] highlightIndices = {};
    private boolean showFound     = false;
    private int foundIndex        = -1;

    public VisualizationPane() {
        setStyle("-fx-background-color:#1c1b19;-fx-background-radius:12;");
        setPrefHeight(300);
    }

    public void render(AlgorithmStep step) {
        if (step == null) return;
        this.currentArray     = step.getArraySnapshot();
        this.highlightIndices = step.getHighlightIndices();
        this.showFound        = step.isFound();
        this.foundIndex       = step.getFoundIndex();
        drawBars();
    }

    public void renderArray(int[] arr) {
        this.currentArray     = arr.clone();
        this.highlightIndices = new int[]{};
        this.showFound        = false;
        this.foundIndex       = -1;
        drawBars();
    }

    private void drawBars() {
        getChildren().clear();
        if (currentArray.length == 0) return;

        double paneWidth  = Math.max(getWidth(), 400);
        double paneHeight = Math.max(getHeight(), 300);
        double padding    = 20, gap = 6;
        double barWidth   = (paneWidth - 2*padding - gap*(currentArray.length-1))
                             / currentArray.length;
        int maxVal = Arrays.stream(currentArray).max().orElse(1);

        Set<Integer> highlighted = new HashSet<>();
        for (int idx : highlightIndices) highlighted.add(idx);

        for (int i = 0; i < currentArray.length; i++) {
            double barHeight = ((double) currentArray[i] / maxVal)
                                * (paneHeight - padding*2 - 30);
            double x = padding + i * (barWidth + gap);
            double y = paneHeight - padding - barHeight - 25;

            Color barColor = (showFound && foundIndex == i) ? BAR_FOUND
                           : highlighted.contains(i)        ? BAR_HIGHLIGHT
                           : BAR_DEFAULT;

            Rectangle bar = new Rectangle(x, y, barWidth, barHeight);
            bar.setFill(barColor);
            bar.setArcWidth(4);
            bar.setArcHeight(4);

            Text valueText = new Text(String.valueOf(currentArray[i]));
            valueText.setFont(Font.font("System", FontWeight.BOLD,
                Math.min(12, barWidth - 2)));
            valueText.setFill(Color.web("#cdccca"));
            valueText.setLayoutX(x + (barWidth
                - valueText.getBoundsInLocal().getWidth()) / 2);
            valueText.setLayoutY(y - 4);

            Text indexText = new Text(String.valueOf(i));
            indexText.setFont(Font.font("System", 10));
            indexText.setFill(Color.web("#797876"));
            indexText.setLayoutX(x + (barWidth
                - indexText.getBoundsInLocal().getWidth()) / 2);
            indexText.setLayoutY(paneHeight - 8);

            FadeTransition ft = new FadeTransition(Duration.millis(200), bar);
            ft.setFromValue(0.6);
            ft.setToValue(1.0);
            ft.play();

            getChildren().addAll(bar, valueText, indexText);
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        drawBars();
    }
}