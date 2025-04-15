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
                // Oculta todos
                barChart.setVisibility(View.GONE);
                lineChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Gráfico de Pizza selecionado", Toast.LENGTH_SHORT).show();
                typeGraph = "pizza";
                exibirGraficoSelecionado(typeGraph,barChart,lineChart,pieChart);

            }
        });

        BarraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Oculta todos
                barChart.setVisibility(View.GONE);
                lineChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Gráfico de Barras selecionado", Toast.LENGTH_SHORT).show();
                typeGraph = "barra";
                exibirGraficoSelecionado(typeGraph,barChart,lineChart,pieChart);
            }
        });

        LinhaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Oculta todos
                barChart.setVisibility(View.GONE);
                lineChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.GONE);
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
        // Oculta os rótulos do eixo da direita (visualmente limpa o gráfico)
        barChart.getAxisRight().setDrawLabels(false);

        // Cria uma lista de valores de barra (posição X, valor Y)
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 45f));  // Primeiro item com valor 45
        entries.add(new BarEntry(1, 80f));  // Segundo com 80
        entries.add(new BarEntry(2, 65f));  // ...
        entries.add(new BarEntry(3, 38f));

        // Configura o eixo Y (esquerdo)
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f); // Valor mínimo do Y é 0
        yAxis.setAxisMaximum(100f); // Valor máximo do Y é 100
        yAxis.setAxisLineWidth(2f);// Espessura da linha do eixo Y
        yAxis.setAxisLineColor(Color.BLACK);// Cor da linha do eixo Y
        yAxis.setLabelCount(10);// Quantidade de marcadores no eixo Y

        // Cria um conjunto de dados para o gráfico de barras
        BarDataSet dataSet = new BarDataSet(entries, "Subjects");// Título da legenda
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);// Define cores variadas

        // Cria os dados finais com o dataset e associa ao gráfico
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Remove a descrição de texto no canto inferior direito
        barChart.getDescription().setEnabled(false);

        // Configura o eixo X
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter());// Formatação personalizada (opcional)
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Posição na parte de baixo
        barChart.getXAxis().setGranularity(1f); // Intervalo mínimo entre valores
        barChart.getXAxis().setGranularityEnabled(true); // Ativa granularidade

        // Redesenha o gráfico
        barChart.invalidate();

    }

    private void exibirPizza(PieChart pieChart)
    {

        // Cria uma lista com os dados da pizza (valor, rótulo)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(45f, "Miguel"));
        entries.add(new PieEntry(80f, "Carlos"));
        entries.add(new PieEntry(65f, "Victor"));
        entries.add(new PieEntry(38f, "Bea"));

        // Cria o conjunto de fatias da pizza
        PieDataSet dataSet = new PieDataSet(entries, "Subjects");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);// Aplica cores automáticas
        dataSet.setSliceSpace(3f);// Espaço entre as fatias
        dataSet.setValueTextSize(12f);// Tamanho do texto dos valores

        // Cria os dados finais e associa ao gráfico
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);// Converte os valores para porcentagem
        pieChart.getDescription().setEnabled(false);// Remove a descrição padrão
        pieChart.setEntryLabelColor(Color.BLACK);// Cor dos rótulos das fatias
        pieChart.setHoleRadius(30f);// Raio do buraco central
        pieChart.setTransparentCircleRadius(35f);// Raio da borda transparente
        pieChart.invalidate(); // Redesenha o gráfico

    }

    private void exibirLinha(LineChart lineChart)
    {
        // Cria os pontos (x, y) para o gráfico de linha
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 45f));
        entries.add(new Entry(1, 80f));
        entries.add(new Entry(2, 65f));
        entries.add(new Entry(3, 38f));

        // Cria o dataset para o gráfico de linha
        LineDataSet dataSet = new LineDataSet(entries, "Subjects");
        dataSet.setColor(ColorTemplate.getHoloBlue()); // Cor da linha
        dataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[0]);// Cor dos círculos nos pontos
        dataSet.setLineWidth(2f); // Espessura da linha
        dataSet.setCircleRadius(5f); // Tamanho dos círculos
        dataSet.setValueTextSize(10f); // Tamanho dos valores nos pontos

        // Cria os dados finais e associa ao gráfico
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false); // Remove descrição padrão

        // Configura o eixo X com nomes personalizados
        lineChart.getXAxis().setValueFormatter(
                new IndexAxisValueFormatter(Arrays.asList("Miguel", "Carlos", "Victor", "Bea"))
        );
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);// Posição inferior
        lineChart.getXAxis().setGranularity(1f);// Intervalo mínimo
        lineChart.getXAxis().setGranularityEnabled(true); // Ativa granularidade

        lineChart.invalidate(); // Redesenha o gráfico

    }

    private void exibirGraficoSelecionado(String typeGraph, BarChart barChart, LineChart lineChart, PieChart pieChart)
    {

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

