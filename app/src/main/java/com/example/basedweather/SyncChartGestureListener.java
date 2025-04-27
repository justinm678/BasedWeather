package com.example.basedweather;

import android.graphics.Matrix;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

public class SyncChartGestureListener implements OnChartGestureListener {
    private final LineChart sourceChart;
    private final LineChart targetChart;

    public SyncChartGestureListener(LineChart sourceChart, LineChart targetChart) {
        this.sourceChart = sourceChart;
        this.targetChart = targetChart;
    }

    private void syncCharts() {
        Matrix srcMatrix = sourceChart.getViewPortHandler().getMatrixTouch();
        float[] srcVals = new float[9];
        srcMatrix.getValues(srcVals);

        Matrix dstMatrix = targetChart.getViewPortHandler().getMatrixTouch();
        float[] dstVals = new float[9];
        dstMatrix.getValues(dstVals);

        // Only copy X axis scaling and translation
        dstVals[Matrix.MSCALE_X] = srcVals[Matrix.MSCALE_X];
        dstVals[Matrix.MTRANS_X] = srcVals[Matrix.MTRANS_X];

        dstMatrix.setValues(dstVals);
        targetChart.getViewPortHandler().refresh(dstMatrix, targetChart, true);
    }
    private void highlightOtherChart(MotionEvent me) {
        Highlight sourceHighlight = sourceChart.getHighlightByTouchPoint(me.getX(), me.getY());
        if (sourceHighlight != null) {
            float xValue = sourceHighlight.getX();

            // Create a new Highlight for the target chart
            Highlight targetHighlight = new Highlight(
                    xValue,             // x position
                    0f,                 // dummy y (won't matter)
                    0                   // dataset index (assume 0, or whatever you need)
            );

            targetChart.highlightValue(targetHighlight, false);
        } else {
            targetChart.highlightValue(null);
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {}

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {}

    @Override
    public void onChartLongPressed(MotionEvent me) {}

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        syncCharts();  // <-- Add this!
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        highlightOtherChart(me);
    }
    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {}

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        syncCharts();  // <-- Important! Sync when scaling
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        syncCharts();  // <-- Also sync when dragging
    }
}
