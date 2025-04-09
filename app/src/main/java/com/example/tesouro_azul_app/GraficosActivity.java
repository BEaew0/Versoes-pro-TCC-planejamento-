package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraficosActivity extends AppCompatActivity {

    //Apenas testando, irei modificar futuramente
    private List<String> xValues = Arrays.asList("Miguel", "Carlos", "Victor", "Bea");


    //Maior parte deste conteudo é teste
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        BarChart barChart = findViewById(R.id.chart);
        barChart.getAxisRight().setDrawLabels(false);


        ArrayList <BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0,45f));
        entries.add(new BarEntry(1,80f));
        entries.add(new BarEntry(2,65f));
        entries.add(new BarEntry(3,38f));

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(android.R.color.black);
        yAxis.setLabelCount(10);

        BarDataSet dataSet = new BarDataSet(entries, "Subjects");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter());
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
    }
}