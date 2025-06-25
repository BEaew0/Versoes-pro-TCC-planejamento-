package com.example.tesouro_azul_app.Pages;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tesouro_azul_app.Class.SuperClassProd;
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
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraficosActivity extends AppCompatActivity {

    private ApiService apiService;
    String token;

    private static final String TAG = "GraficosActivity";

    // Constantes
    private static final String TYPE_PIE = "pizza";
    private static final String TYPE_BAR = "barra";
    private static final String TYPE_LINE = "linha";

    private static final String TYPE_VENDAS = "vendas";
    private static final String TYPE_COMPRAS = "compras";

    private String selectedDataType = TYPE_VENDAS; // ou TYPE_COMPRAS

    private String compraString,vendaString;

    private List<SuperClassProd.ItemVendaDtoQuant> listaDeVendas = new ArrayList<>();
    private List<SuperClassProd.ItemCompraDtoQuant> listaDeCompras = new ArrayList<>();


    private TextView comprasInfo,vendasInfo;

    // Componentes de UI
    private BarChart barChart;
    private LineChart lineChart;
    private PieChart pieChart;
    private CardView pizzaCard, barraCard, linhaCard, comprasCard, graficVendasCard ;

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

        selectedGraphType = TYPE_BAR; // valor padrão
        selectedDataType = TYPE_VENDAS; // ou TYPE_COMPRAS
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

        vendasInfo = findViewById(R.id.VendasTotalInfo);
        comprasInfo = findViewById(R.id.ComprasTotalInfo);

        comprasCard = findViewById(R.id.CompraCard);
        graficVendasCard = findViewById(R.id.GraficVendasCard);

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
        pizzaCard.setOnClickListener(v -> {
            selectedGraphType = TYPE_PIE;
            displaySelectedGraph();
        });

        barraCard.setOnClickListener(v -> {
            selectedGraphType = TYPE_BAR;
            displaySelectedGraph();
        });

        linhaCard.setOnClickListener(v -> {
            selectedGraphType = TYPE_LINE;
            displaySelectedGraph();
        });

        comprasCard.setOnClickListener(v -> {
            selectedDataType = TYPE_COMPRAS;
            showToast("Exibindo dados de compras");
            displaySelectedGraph(); // Atualiza gráfico com novo tipo de dado
        });

        graficVendasCard.setOnClickListener(v -> {
            selectedDataType = TYPE_VENDAS;
            showToast("Exibindo dados de vendas");
            displaySelectedGraph();
        });
    }


    /**
     * Exibe o gráfico selecionado
     */
    private void displaySelectedGraph() {
        hideAllCharts(); // Garante que tudo fique oculto antes de qualquer ação

        if (selectedDataType.equals(TYPE_COMPRAS)) {
            carregarItensCompraDoUsuario(); // Vai exibir o gráfico correto depois
        } else if (selectedDataType.equals(TYPE_VENDAS)) {
            carregarItensVendaDoUsuario();
        } else {
            showToast("Tipo de dado inválido");
        }
    }


    private void gerarGraficoComprasPorData(List<SuperClassProd.ItemCompraDtoQuant> itens) {
        // Agrupar por data
        Map<String, Integer> totalPorData = new TreeMap<>();
        for (SuperClassProd.ItemCompraDtoQuant item : itens) {
            if (item.pedidoCompra != null && item.pedidoCompra.dataPedido != null) {
                String data = item.pedidoCompra.dataPedido.split("T")[0];
                int atual = totalPorData.containsKey(data) ? totalPorData.get(data) : 0;
                totalPorData.put(data, atual + item.quantidade);
            }
        }

        // Preparar entradas do gráfico
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Integer> entry : totalPorData.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));
            labels.add(entry.getKey());
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Itens comprados por dia");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Configurar eixo X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        // Eixo Y
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);

        barChart.setVisibility(View.VISIBLE);

        barChart.invalidate(); // Redesenhar gráfico
    }
    private void exibirLineChartItensCompra(List<SuperClassProd.ItemCompraDtoQuant> itens) {
        Map<String, Integer> totalPorData = new TreeMap<>();

        for (SuperClassProd.ItemCompraDtoQuant item : itens) {
            if (item.pedidoCompra != null && item.pedidoCompra.dataPedido != null) {
                String data = item.pedidoCompra.dataPedido.split("T")[0];
                int atual = totalPorData.getOrDefault(data, 0);
                totalPorData.put(data, atual + item.quantidade);
            }
        }

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Integer> entry : totalPorData.entrySet()) {
            entries.add(new Entry(index, entry.getValue()));
            labels.add(entry.getKey());
            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Itens Comprados por Dia");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(ColorTemplate.COLORFUL_COLORS[0]);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        lineChart.setVisibility(View.VISIBLE);

        lineChart.invalidate();
    }
    private void exibirPieChartItensCompra(List<SuperClassProd.ItemCompraDtoQuant> itens) {
        Map<String, Integer> totalPorData = new TreeMap<>();

        for (SuperClassProd.ItemCompraDtoQuant item : itens) {
            if (item.pedidoCompra != null && item.pedidoCompra.dataPedido != null) {
                String data = item.pedidoCompra.dataPedido.split("T")[0];
                int atual = totalPorData.getOrDefault(data, 0);
                totalPorData.put(data, atual + item.quantidade);
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : totalPorData.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Distribuição por Data");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.getDescription().setEnabled(false);

        pieChart.setVisibility(View.VISIBLE);

        pieChart.invalidate();
    }

    private void exibirGraficoVendasPorDataLinha(List< SuperClassProd.ItemVendaDtoQuant > itens) {
        Map<String, Integer> vendasPorData = new TreeMap<>(); // TreeMap ordena as datas

        // Agrupar quantidades por data
        for (SuperClassProd.ItemVendaDtoQuant item : itens) {
            try {
                String data = item.pedidoVenda.dataPedidoVenda.split("T")[0]; // Ex: 2025-06-17
                int quantidadeAtual = vendasPorData.getOrDefault(data, 0);
                vendasPorData.put(data, quantidadeAtual + item.quantidade);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao processar item: " + item, e);
            }
        }

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Integer> entry : vendasPorData.entrySet()) {
            entries.add(new Entry(index, entry.getValue()));
            labels.add(entry.getKey());
            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Vendas por Dia");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        lineChart.setVisibility(View.VISIBLE);

        lineChart.invalidate(); // Redesenha o gráfico
    }
    private void exibirGraficoBarrasVendasPorData(List<SuperClassProd.ItemVendaDtoQuant> itens) {
        Map<String, Integer> vendasPorData = new TreeMap<>();

        // Agrupando a quantidade vendida por data
        for (SuperClassProd.ItemVendaDtoQuant item : itens) {
            String data = item.pedidoVenda.dataPedidoVenda.split("T")[0]; // Ex: "2025-06-17"
            int quantidadeAtual = vendasPorData.getOrDefault(data, 0);
            vendasPorData.put(data, quantidadeAtual + item.quantidade);
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Integer> entry : vendasPorData.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));
            labels.add(entry.getKey());
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Itens Vendidos por Data");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        barChart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChart.setVisibility(View.VISIBLE);

        barChart.invalidate();
    }
    private void exibirGraficoPizzaVendasPorData(List<SuperClassProd.ItemVendaDtoQuant> itens) {
        Map<String, Integer> vendasPorData = new TreeMap<>();

        // Agrupando a quantidade vendida por data
        for (SuperClassProd.ItemVendaDtoQuant item : itens) {
            String data = item.pedidoVenda.dataPedidoVenda.split("T")[0];
            int quantidadeAtual = vendasPorData.getOrDefault(data, 0);
            vendasPorData.put(data, quantidadeAtual + item.quantidade);
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : vendasPorData.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Distribuição de Vendas");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);

        pieChart.setVisibility(View.VISIBLE); // para gráficos de pizza

        pieChart.invalidate();
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
     * Exibe uma mensagem Toast
     */
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void carregarItensVendaDoUsuario() {
        apiService.buscarItensVendaPorUsuario(token).enqueue(new Callback<List<SuperClassProd.ItemVendaDtoQuant>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.ItemVendaDtoQuant>> call, Response<List<SuperClassProd.ItemVendaDtoQuant>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaDeVendas = response.body();

                    // Exibe o gráfico apropriado
                    switch (selectedGraphType) {
                        case TYPE_PIE:
                            exibirGraficoPizzaVendasPorData(listaDeVendas);
                            break;
                        case TYPE_BAR:
                            exibirGraficoBarrasVendasPorData(listaDeVendas);
                            break;
                        case TYPE_LINE:
                            exibirGraficoVendasPorDataLinha(listaDeVendas);
                            break;
                    }

                    // Atualiza totais
                    calcularTotaisEArmazenar(listaDeVendas, listaDeCompras);
                } else {
                    Log.e(TAG, "Erro ao buscar itens de venda: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<SuperClassProd.ItemVendaDtoQuant>> call, Throwable t) {
                Log.e(TAG, "Falha ao buscar itens de venda", t);
            }
        });
    }

    private void carregarItensCompraDoUsuario() {
        apiService.buscarItensCompraPorUsuario(token).enqueue(new Callback<List<SuperClassProd.ItemCompraDtoQuant>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.ItemCompraDtoQuant>> call, Response<List<SuperClassProd.ItemCompraDtoQuant>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaDeCompras = response.body();

                    // Exibe o gráfico apropriado
                    switch (selectedGraphType) {
                        case TYPE_PIE:
                            exibirPieChartItensCompra(listaDeCompras);
                            break;
                        case TYPE_BAR:
                            gerarGraficoComprasPorData(listaDeCompras);
                            break;
                        case TYPE_LINE:
                            exibirLineChartItensCompra(listaDeCompras);
                            break;
                    }

                    // Atualiza totais
                    calcularTotaisEArmazenar(listaDeVendas, listaDeCompras);
                } else {
                    Log.e(TAG, "Erro ao buscar itens de compra: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<SuperClassProd.ItemCompraDtoQuant>> call, Throwable t) {
                Log.e(TAG, "Falha ao buscar itens de compra", t);
            }
        });
    }


    private void calcularTotaisEArmazenar(List<SuperClassProd.ItemVendaDtoQuant> vendas, List<SuperClassProd.ItemCompraDtoQuant> compras) {
        int totalVendidos = 0;
        int totalComprados = 0;

        // Soma os itens vendidos
        for (SuperClassProd.ItemVendaDtoQuant item : vendas) {
            totalVendidos += item.quantidade;
        }

        // Soma os itens comprados
        for (SuperClassProd.ItemCompraDtoQuant item : compras) {
            totalComprados += item.quantidade;
        }

        // Armazena os totais em String
        String totalVendidosStr = String.valueOf(totalVendidos);
        String totalCompradosStr = String.valueOf(totalComprados);

        // Exemplo de uso:
        Log.d("Totais", totalVendidosStr + " | " + totalCompradosStr);
        Toast.makeText(this, totalVendidosStr + "\n" + totalCompradosStr, Toast.LENGTH_LONG).show();

        // Se quiser exibir em TextViews:
        vendasInfo.setText(totalVendidosStr);
        comprasInfo.setText(totalCompradosStr);
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