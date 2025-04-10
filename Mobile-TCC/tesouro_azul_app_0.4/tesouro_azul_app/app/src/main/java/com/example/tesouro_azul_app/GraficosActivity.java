package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraficosActivity extends AppCompatActivity {

    private String typeGraph;
    //Apenas testando, irei modificar futuramente
    private List<String> xValues = Arrays.asList("Miguel", "Carlos", "Victor", "Bea");

    CardView PizzaCard, BarraCard, LinhaCard, LucroCard,VendasCard,FaturamentoCard;

    //Maior parte deste conteudo é teste
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        BarChart barChart = findViewById(R.id.barChart);
        LineChart lineChart = findViewById(R.id.lineChart);
        PieChart pieChart = findViewById(R.id.pieChart);

        CardView PizzaCard = findViewById(R.id.PizzaCard);
        CardView BarraCard = findViewById(R.id.BarraCard);
        CardView LinhaCard = findViewById(R.id.LinhaCard);
        CardView LucroCard = findViewById(R.id.LucroCard);
        CardView VendasCard = findViewById(R.id.VendasCard);
        CardView FaturamentoCard = findViewById(R.id.FaturamentoCard);

        // Oculta todos
        barChart.setVisibility(View.GONE);
        lineChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);

        PizzaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação ao clicar no card de Pizza
                Toast.makeText(getApplicationContext(), "Gráfico de Pizza selecionado", Toast.LENGTH_SHORT).show();
                typeGraph = "pizza";
                exibirGraficoSelecionado(typeGraph,barChart,lineChart,pieChart);
            }
        });

        BarraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação ao clicar no card de Barra
                Toast.makeText(getApplicationContext(), "Gráfico de Barras selecionado", Toast.LENGTH_SHORT).show();
                typeGraph = "barra";
                exibirGraficoSelecionado(typeGraph,barChart,lineChart,pieChart);
            }
        });

        LinhaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação ao clicar no card de Linha
                Toast.makeText(getApplicationContext(), "Gráfico de Linhas selecionado", Toast.LENGTH_SHORT).show();
                typeGraph = "linha";
                exibirGraficoSelecionado(typeGraph,barChart,lineChart,pieChart);
            }
        });

        LucroCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação ao clicar no card de Lucro
                Toast.makeText(getApplicationContext(), "Lucro selecionado", Toast.LENGTH_SHORT).show();

            }
        });

        VendasCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação ao clicar no card de Vendas
                Toast.makeText(getApplicationContext(), "Vendas selecionadas", Toast.LENGTH_SHORT).show();
            }
        });

        FaturamentoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação ao clicar no card de Faturamento
                Toast.makeText(getApplicationContext(), "Faturamento selecionado", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void exibirBarra(BarChart barChart)
    {
        // Oculta os rótulos do eixo da direita
        barChart.getAxisRight().setDrawLabels(false);

        // Entradas de exemplo
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 45f));
        entries.add(new BarEntry(1, 80f));
        entries.add(new BarEntry(2, 65f));
        entries.add(new BarEntry(3, 38f));

        // Configuração do eixo Y (esquerdo)
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f); // Corrigido: é Minimum, não Maximum aqui
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK); // Corrigido: usa Color, não resource ID
        yAxis.setLabelCount(10);

        // Dataset
        BarDataSet dataSet = new BarDataSet(entries, "Subjects");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // Dados e atribuição ao gráfico
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Desabilita a descrição padrão
        barChart.getDescription().setEnabled(false);

        // Eixo X
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter());
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);

        // Atualiza o gráfico
        barChart.invalidate();
    }

    private void exibirPizza(PieChart pieChart)
    {

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(45f, "Miguel"));
        entries.add(new PieEntry(80f, "Carlos"));
        entries.add(new PieEntry(65f, "Victor"));
        entries.add(new PieEntry(38f, "Bea"));

        PieDataSet dataSet = new PieDataSet(entries, "Subjects");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.invalidate(); // Atualiza o gráfico
    }

    private void exibirLinha(LineChart lineChart)
    {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 45f));
        entries.add(new Entry(1, 80f));
        entries.add(new Entry(2, 65f));
        entries.add(new Entry(3, 38f));

        LineDataSet dataSet = new LineDataSet(entries, "Subjects");
        dataSet.setColor(ColorTemplate.getHoloBlue());
        dataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[0]);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);

        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(Arrays.asList("Miguel", "Carlos", "Victor", "Bea")));
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setGranularity(1f);
        lineChart.getXAxis().setGranularityEnabled(true);

        lineChart.invalidate(); // Atualiza o gráfico
    }

    private void exibirGraficoSelecionado(String typeGraph, BarChart barChart, LineChart lineChart, PieChart pieChart)
    {
        // Oculta todos
        barChart.setVisibility(View.GONE);
        lineChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);

        switch (typeGraph.toLowerCase()) {
            case "barra":
                barChart.setVisibility(View.VISIBLE);
               // configurarBarChart(barChart);
                exibirBarra(barChart);
                break;

            case "linha":
                lineChart.setVisibility(View.VISIBLE);
               // configurarLineChart(lineChart);
                exibirLinha(lineChart);
                break;

            case "pizza":
                pieChart.setVisibility(View.VISIBLE);
               // configurarPieChart(pieChart);
                exibirPizza(pieChart);
                break;

            default:
                Toast.makeText(getApplicationContext(), "Tipo de gráfico inválido", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

