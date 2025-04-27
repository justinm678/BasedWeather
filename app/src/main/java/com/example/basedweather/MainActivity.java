package com.example.basedweather;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.basedweather.models.Todo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.ChartTouchListener;
import android.view.MotionEvent;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import com.example.basedweather.SyncChartGestureListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private LineChart lineChartTop;
    private LineChart lineChartBot;
    private List<String> xValues;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChartTop = findViewById(R.id.chart);  // Top chart
        lineChartBot = findViewById(R.id.chart2);  // Bottom chart

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Todo> call = apiService.getTodos();
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                Log.d("yay", String.format("%s was received!", response.body()));
                List<String> hourly_time_list = response.body().hourly.time;
                List<Float> hourly_temperature_list = response.body().hourly.temperature_2m;
                List<Float> hourly_precipitation_probability_list = response.body().hourly.precipitation;
                List<Float> hourly_uv_index_list = response.body().hourly.uv_index;
                List<Float> hourly_cloud_cover_list = response.body().hourly.cloud_cover;
                List<Float> hourly_dew_point_list = response.body().hourly.dew_point_2m;
                Description description = new Description();

                // 1. Setup parsedTimes list
                List<LocalDateTime> parsedTimes = new ArrayList<>();
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

                // 2. Create entries
                List<Entry> temperatureArrayList = new ArrayList<>();
                List<Entry> precipitationArrayList = new ArrayList<>();
                List<Entry> dewpointArrayList = new ArrayList<>();
                List<Entry> cloudcoverArrayList = new ArrayList<>();
                List<Entry> uvArrayList = new ArrayList<>();

                for (int i = 0; i < hourly_time_list.size(); i++) {
                    // Parse each time string
                    LocalDateTime time = LocalDateTime.parse(hourly_time_list.get(i), inputFormatter);
                    parsedTimes.add(time);

                    // Create chart points
                    temperatureArrayList.add(new Entry(i, hourly_temperature_list.get(i)));
                    precipitationArrayList.add(new Entry(i, hourly_precipitation_probability_list.get(i)));
                    dewpointArrayList.add(new Entry(i,hourly_dew_point_list.get(i)));
                    cloudcoverArrayList.add(new Entry(i,hourly_cloud_cover_list.get(i)));
                    uvArrayList.add(new Entry(i,hourly_uv_index_list.get(i)));
                }

                // Setup top chart

                // Setup X axis with time formatter
                XAxis xAxisTop = lineChartTop.getXAxis();
                TimeAxisValueFormatter formatter = new TimeAxisValueFormatter(parsedTimes);
                xAxisTop.setValueFormatter(formatter);
                xAxisTop.setGranularity(1f); // Important: 1 point per label
                xAxisTop.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxisTop.setLabelRotationAngle(-45f); // Tilt to prevent overlapping

                // Setup Left Y-Axis (for temperature)
                YAxis yAxisTopLeft = lineChartTop.getAxisLeft();
                yAxisTopLeft.setAxisLineWidth(2f);
                yAxisTopLeft.setAxisLineColor(Color.BLACK);

                // Setup Right Y-Axis (for precipitation)
                YAxis yAxisTopRight = lineChartTop.getAxisRight();
                yAxisTopRight.setTextColor(Color.BLUE);

                // Setup dataset to add to chart
                LineDataSet temperatureDataSet = new LineDataSet(temperatureArrayList, "Temperature");
                temperatureDataSet.setColor(Color.BLACK);
                temperatureDataSet.setDrawCircles(false);
                temperatureDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

                LineDataSet precipitationDataSet = new LineDataSet(precipitationArrayList, "Precipitation Probability");
                precipitationDataSet.setColor(Color.BLUE);
                precipitationDataSet.setDrawCircles(false);
                precipitationDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

                LineDataSet precipitationFillDataset = new LineDataSet(precipitationArrayList, null);

                // Don't draw line or circles
                precipitationFillDataset.setColor(Color.BLUE);
                precipitationFillDataset.setDrawCircles(false);
                precipitationFillDataset.setDrawValues(false);
                precipitationFillDataset.setLineWidth(0f);

                // Hide from legend completely
                precipitationFillDataset.setLabel(null);
                precipitationFillDataset.setForm(Legend.LegendForm.NONE);

                // Fill under the line
                precipitationFillDataset.setDrawFilled(true);
                precipitationFillDataset.setFillColor(Color.BLUE);
                precipitationFillDataset.setFillAlpha(30); // 0-255, lower = more transparent

                // (Optional) Move to background visually
                precipitationFillDataset.setAxisDependency(YAxis.AxisDependency.RIGHT);

                // Set all datasets to the chart
                LineData lineDataTop = new LineData(temperatureDataSet, precipitationDataSet, precipitationFillDataset);
                lineChartTop.setData(lineDataTop);

                // Move the legend to the top
                Legend legend = lineChartTop.getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setDrawInside(false);  // Make sure it draws *outside* the chart area

                // No description label
                lineChartTop.getDescription().setEnabled(false);


                // Lower chart
                //--------------------------------------------------------------------------------------------------------

                lineChartBot = findViewById(R.id.chart2);

                // Get X-Axis
                XAxis xAxisBot = lineChartBot.getXAxis();

                // Setup X axis with time formatter
                xAxisBot.setValueFormatter(formatter);
                xAxisBot.setGranularity(1f); // Important: 1 point per label
                xAxisBot.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxisBot.setLabelRotationAngle(-45f); // Tilt to prevent overlapping

                xAxisBot.setDrawLabels(false);  // Hide the labels
                xAxisBot.setDrawAxisLine(false); // Hide the axis line
//                xAxisBot.setDrawGridLines(false); // (Optional) Hide gridlines too

                // Sync the X-Axis between the charts
                xAxisBot.setAxisMinimum(xAxisTop.getAxisMinimum());
                xAxisBot.setAxisMaximum(xAxisTop.getAxisMaximum());
                xAxisBot.setGranularity(xAxisTop.getGranularity());

                // Setup Y axes for bottom chart
                YAxis yAxis2 = lineChartBot.getAxisLeft();
                yAxis2.setAxisLineWidth(2f);
                yAxis2.setAxisLineColor(Color.BLACK);

                LineDataSet dew_point_dataset = new LineDataSet(dewpointArrayList,"Dew Point");
                dew_point_dataset.setColor(Color.BLACK);
                dew_point_dataset.setDrawCircles(false);

                // Cloud cover with color background
                LineDataSet cloud_cover_dataset = new LineDataSet(cloudcoverArrayList,"Cloud Cover");
                cloud_cover_dataset.setColor(Color.GRAY);
                cloud_cover_dataset.setDrawCircles(false);

                // Cloud cover Fill
                LineDataSet cloudCoverFillDataset = new LineDataSet(cloudcoverArrayList, null);

                // Don't draw line or circles
                cloudCoverFillDataset.setDrawCircles(false);
                cloudCoverFillDataset.setDrawValues(false);
                cloudCoverFillDataset.setLineWidth(0f);

                // Hide from legend completely
                cloudCoverFillDataset.setLabel(null);
                cloudCoverFillDataset.setForm(Legend.LegendForm.NONE);

                // Fill under the line
                cloudCoverFillDataset.setDrawFilled(true);
                cloudCoverFillDataset.setFillColor(Color.DKGRAY);
                cloudCoverFillDataset.setFillAlpha(80); // 0-255, lower = more transparent

                // Move to background visually
                cloudCoverFillDataset.setAxisDependency(YAxis.AxisDependency.LEFT);

                // UV
                LineDataSet uv_dataset = new LineDataSet(uvArrayList,"UV index");
                uv_dataset.setColor(Color.RED);
                uv_dataset.setDrawCircles(false);
                uv_dataset.setAxisDependency(YAxis.AxisDependency.RIGHT);
                lineChartBot.getAxisRight().setTextColor(Color.RED);

                // UV Fill
                LineDataSet uvFillDataset = new LineDataSet(uvArrayList, null);

                // Don't draw line or circles
                uvFillDataset.setDrawCircles(false);
                uvFillDataset.setDrawValues(false);
                uvFillDataset.setLineWidth(0f);

                // Hide from legend completely
                uvFillDataset.setLabel(null);
                uvFillDataset.setForm(Legend.LegendForm.NONE);

                // Fill under the line
                uvFillDataset.setDrawFilled(true);
                uvFillDataset.setFillColor(Color.YELLOW);
                uvFillDataset.setFillAlpha(50); // 0-255, lower = more transparent

                // Move to background visually
                uvFillDataset.setAxisDependency(YAxis.AxisDependency.RIGHT);


                LineData lineData2 = new LineData(
                        cloudCoverFillDataset,  // Background first
                        dew_point_dataset,
                        cloud_cover_dataset,
                        uv_dataset,
                        uvFillDataset
                );

                lineChartBot.setData(lineData2);


                // No description label
                lineChartBot.getDescription().setEnabled(false);


                // Set chart alignment
                // Disable automatic offset calculation
                lineChartTop.setAutoScaleMinMaxEnabled(false);
                lineChartBot.setAutoScaleMinMaxEnabled(false);

                // Manually align the viewport
                lineChartTop.setViewPortOffsets(80f, 20f, 80f, 120f);  // (left, top, right, bottom)
                lineChartBot.setViewPortOffsets(80f, 0f, 80f, 20f);
                YAxis yAxisBottom = lineChartBot.getAxisLeft();
                yAxisBottom.setAxisMinimum(0f);  // Set minimum Y value to 0
                YAxis yAxisBottomR = lineChartBot.getAxisRight();
                yAxisBottomR.setAxisMinimum(0f);  // Set minimum Y value to 0

                YAxis yAxisTop = lineChartTop.getAxisLeft();
                yAxisTop.setAxisMinimum(0f);  // Set minimum Y value to 0
                YAxis yAxisTopR = lineChartTop.getAxisRight();
                yAxisTopR.setAxisMinimum(0f);  // Set minimum Y value to 0


                // Add current time markers
                LocalDateTime now = LocalDateTime.now();
                int currentTimeIndex = -1;

                // Find the closest time index
                for (int i = 0; i < parsedTimes.size(); i++) {
                    if (!parsedTimes.get(i).isBefore(now)) {
                        currentTimeIndex = i;
                        break;
                    }
                }
                LimitLine currentTimeLine = new LimitLine(currentTimeIndex, "");
                currentTimeLine.setLineColor(Color.GREEN);
                currentTimeLine.setLineWidth(2f);
                currentTimeLine.setTextColor(Color.GREEN);       // Label color
                currentTimeLine.setTextSize(12f);                // Label text size
                LimitLine currentTimeLineBot = new LimitLine(currentTimeIndex, "");
                currentTimeLineBot.setLineColor(Color.GREEN);
                currentTimeLineBot.setLineWidth(2f);
                currentTimeLineBot.setTextColor(Color.GREEN);
                currentTimeLineBot.setTextSize(12f);

                xAxisTop.addLimitLine(currentTimeLine);
                xAxisBot.addLimitLine(currentTimeLineBot);
                // Refresh the chart
                lineChartTop.invalidate();
                lineChartBot.invalidate();
                // Sync Gestures between charts
                lineChartTop.setOnChartGestureListener(new SyncChartGestureListener(lineChartTop, lineChartBot));
                lineChartBot.setOnChartGestureListener(new SyncChartGestureListener(lineChartBot, lineChartTop));

            }



            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.d("oof","Nothin");
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
    });
    }

}
