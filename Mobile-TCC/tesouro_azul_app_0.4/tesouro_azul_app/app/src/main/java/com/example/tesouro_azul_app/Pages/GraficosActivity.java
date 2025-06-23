package com.example.tesouro_azul_app.Pages;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tesouro_azul_app.R;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.example.tesouro_azul_app.Util.AuthUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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

    private ApiService apiService;
    String token;

    private static final String TAG = "GraficosActivity";

    // Constantes
    private static final String TYPE_PIE = "pizza";
    private static final String TYPE_BAR = "barra";
    private static final String TYPE_LINE = "linha";

    // Componentes de UI
    private BarChart barChart;
    private LineChart lineChart;
    private PieChart pieChart;
    private CardView pizzaCard, barraCard, linhaCard, lucroCard, vendasCard, faturamentoCard;

    // Dados mockados (substituir por dados reais posteriormente)
    private final List<String> labels = Arrays.asList("Miguel", "Carlos", "Victor", "Bea");
    private final float[] values = {45f, 80f, 65f, 38f};
    private String selectedGraphType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        initializeViews();

        apiService = RetrofitClient.getApiService(getApplicationContext());
        token = obterTokenUsuario();

        setupChartDefaults();
        setupClickListeners();
    }

    /**
     * Inicializa todos os componentes da UI
     */
    private void initializeViews() {
        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);
        pieChart = findViewById(R.id.pieChart);

        pizzaCard = findViewById(R.id.PizzaCard);
        barraCard = findViewById(R.id.BarraCard);
        linhaCard = findViewById(R.id.LinhaCard);
        lucroCard = findViewById(R.id.LucroCard);
        vendasCard = findViewById(R.id.VendasCard);
        faturamentoCard = findViewById(R.id.FaturamentoCard);
    }

    /**
     * Configura os valores padrão para os gráficos
     */
    private void setupChartDefaults() {
        // Oculta todos os gráficos inicialmente
        hideAllCharts();

        // Configurações comuns para os gráficos
        barChart.getDescription().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
    }

    /**
     * Configura os listeners de clique para os cards
     */
    private void setupClickListeners() {
        pizzaCard.setOnClickListener(v -> selectGraph(TYPE_PIE, "Gráfico de Pizza selecionado"));
        barraCard.setOnClickListener(v -> selectGraph(TYPE_BAR, "Gráfico de Barras selecionado"));
        linhaCard.setOnClickListener(v -> selectGraph(TYPE_LINE, "Gráfico de Linhas selecionado"));

        lucroCard.setOnClickListener(v -> showToast("Lucro selecionado"));
        vendasCard.setOnClickListener(v -> showToast("Vendas selecionadas"));
        faturamentoCard.setOnClickListener(v -> showToast("Faturamento selecionado"));
    }

    /**
     * Processa a seleção de um tipo de gráfico
     */
    private void selectGraph(String graphType, String message) {
        clearAllCharts();
        showToast(message);
        selectedGraphType = graphType;
        displaySelectedGraph();
    }

    /**
     * Exibe o gráfico selecionado
     */
    private void displaySelectedGraph() {
        hideAllCharts();

        switch (selectedGraphType) {
            case TYPE_BAR:
                barChart.setVisibility(View.VISIBLE);
                displayBarChart();
                break;

            case TYPE_LINE:
                lineChart.setVisibility(View.VISIBLE);
                displayLineChart();
                break;

            case TYPE_PIE:
                pieChart.setVisibility(View.VISIBLE);
                displayPieChart();
                break;

            default:
                showToast("Tipo de gráfico inválido");
                break;
        }
    }

    /**
     * Configura e exibe o gráfico de barras
     */
    private void displayBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            entries.add(new BarEntry(i, values[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Dados");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Configuração do eixo Y
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);
        barChart.getAxisRight().setDrawLabels(false);

        // Configuração do eixo X
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);

        barChart.invalidate();
    }

    /**
     * Configura e exibe o gráfico de pizza
     */
    private void displayPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            entries.add(new PieEntry(values[i], labels.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Dados");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.invalidate();
    }

    /**
     * Configura e exibe o gráfico de linhas
     */
    private void displayLineChart() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            entries.add(new Entry(i, values[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Dados");
        dataSet.setColor(ColorTemplate.getHoloBlue());
        dataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[0]);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setGranularity(1f);
        lineChart.getXAxis().setGranularityEnabled(true);

        lineChart.invalidate();
    }

    /**
     * Oculta todos os gráficos
     */
    private void hideAllCharts() {
        barChart.setVisibility(View.GONE);
        lineChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);
    }

    /**
     * Limpa todos os gráficos
     */
    private void clearAllCharts() {
        barChart.clear();
        lineChart.clear();
        pieChart.clear();
    }

    /**
     * Exibe uma mensagem Toast
     */
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Métodos para buscar dados reais (implementar posteriormente)
    private void buscarLucro()
    {

    }
    private void buscarItensVendidos()
    {

    }

    /** Obtém o token do usuário com prefixo Bearer */
    private String obterTokenUsuario() {
        try {
            String token = AuthUtils.getToken(this);
            if (token != null && !token.isEmpty()) {
                Log.d(TAG, "Token do usuário obtido com sucesso");
                return "Bearer " + token;
            } else {
                Log.w(TAG, "Token do usuário não disponível");
                Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
                finish();
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao obter token do usuário", e);
            Toast.makeText(this, "Erro de autenticação", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }
    }
}