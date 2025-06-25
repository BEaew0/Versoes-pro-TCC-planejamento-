package com.example.tesouro_azul_app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.tesouro_azul_app.Class.SuperClassProd;
import com.example.tesouro_azul_app.R;
import com.example.tesouro_azul_app.Service.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private static final String TAG = "ProdutoAdapter";

    private List<SuperClassProd.ProdutoDtoArray> produtos;  // Lista de produtos para o RecyclerView
    private Context context;                                // Contexto da Activity ou Fragment
    private OnItemClickListener listener;                   // Listener para clique em cada item
    private String token;                                   // Token JWT para autenticação nas requisições da API

    // Cache para armazenar as quantidades de estoque por produto (evita múltiplas requisições por produto)
    private Map<Integer, Double> cacheEstoque = new HashMap<Integer, Double>();

    // Interface para clique no item do RecyclerView
    public interface OnItemClickListener {
        void onItemClick(SuperClassProd.ProdutoDtoArray produto);
    }

    // Construtor do Adapter
    public ProdutoAdapter(List<SuperClassProd.ProdutoDtoArray> produtos, Context context, OnItemClickListener listener, String token) {
        this.produtos = produtos != null ? produtos : new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.token = token;
    }

    // ViewHolder: Mapeia os componentes visuais de cada item da lista
    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduto;
        TextView txtNome, txtCodigo, txtValor, txtTipo, txtQuant;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduto = itemView.findViewById(R.id.imgProduto);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtCodigo = itemView.findViewById(R.id.txtCodigo);
            txtValor = itemView.findViewById(R.id.txtValor);
            txtTipo = itemView.findViewById(R.id.txtTipo);
            txtQuant = itemView.findViewById(R.id.txtQuant);
        }
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout XML para cada item (item_produto.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        SuperClassProd.ProdutoDtoArray produto = produtos.get(position);

        // ------ Dados de texto ------
        holder.txtNome.setText(produto.getNomeProduto() != null ? produto.getNomeProduto() : "Nome indisponível");
        holder.txtCodigo.setText("Código: " + produto.getCodProduto());
        holder.txtValor.setText(String.format("R$ %.2f", produto.getValorProduto()));
        holder.txtTipo.setText("Categoria: " + (produto.getTipoProduto() != null ? produto.getTipoProduto() : "Desconhecida"));

        // ------ Estoque ------
        Double estoque = cacheEstoque.get(produto.getIdProduto());

        if (estoque != null)
        {
            holder.txtQuant.setText("Quantidade: " + estoque);
        }
         else if (produto.getQtdTotalEstoque() > 0)
            {
            holder.txtQuant.setText("Quantidade: " + produto.getQtdTotalEstoque());
            }
         else
         {
            // Se ainda não buscamos, mostra "Carregando..." e faz a requisição para a API
            holder.txtQuant.setText("Carregando estoque...");

            // Chamada assíncrona ao endpoint buscarEstoquePorProduto da API
            RetrofitClient.getApiService(context).buscarEstoquePorProduto("Bearer " + token, produto.getIdProduto())
                    .enqueue(new retrofit2.Callback<SuperClassProd.EstoqueProdutoDto>() {
                        @Override
                        public void onResponse(retrofit2.Call<SuperClassProd.EstoqueProdutoDto> call, retrofit2.Response<SuperClassProd.EstoqueProdutoDto> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                double qtd = response.body().qtdTotalEstoque;

                                cacheEstoque.put(produto.getIdProduto(), qtd);

                                // Armazenar dentro do próprio produto
                                produto.setQtdTotalEstoque(qtd);

                                // Atualizar a TextView
                                holder.txtQuant.setText("Quantidade: " + qtd);
                            } else {
                                holder.txtQuant.setText("Sem estoque");
                                Log.e(TAG, "Erro ao buscar estoque: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<SuperClassProd.EstoqueProdutoDto> call, Throwable t) {
                            holder.txtQuant.setText("Erro ao carregar estoque");
                            Log.e(TAG, "Falha ao buscar estoque", t);
                        }
                    });
        }

        String imgBase64 = produto.getImgProduto();
        if (imgBase64 != null && !imgBase64.isEmpty()) {
            try {
                imgBase64 = imgBase64.replace("\n", "").replace("\r", "");
                byte[] imageBytes = Base64.decode(imgBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                if (bitmap != null) {
                    Glide.with(context)
                            .load(bitmap)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error_image)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(holder.imgProduto);
                } else {
                    Glide.with(context).load(R.drawable.error_image).into(holder.imgProduto);
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro ao decodificar imagem Base64 na posição " + position, e);
                Glide.with(context).load(R.drawable.error_image).into(holder.imgProduto);
            }
        } else {
            Glide.with(context).load(R.drawable.placeholder).into(holder.imgProduto);
        }

        // ------ Clique no item ------
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(produto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return produtos != null ? produtos.size() : 0;
    }

    // Método para atualizar a lista de produtos (ex: após um filtro ou reload)
    public void atualizarLista(List<SuperClassProd.ProdutoDtoArray> novaLista) {
        if (novaLista == null) {
            this.produtos = new ArrayList<>();
        } else {
            this.produtos = novaLista;
        }
        notifyDataSetChanged();
    }
}
