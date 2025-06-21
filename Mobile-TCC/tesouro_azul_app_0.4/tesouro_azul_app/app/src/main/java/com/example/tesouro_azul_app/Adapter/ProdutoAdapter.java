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

import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private static final String TAG = "ProdutoAdapter";

    private List<SuperClassProd.ProdutoDtoArray> produtos;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SuperClassProd.ProdutoDtoArray produto);
    }

    public ProdutoAdapter(List<SuperClassProd.ProdutoDtoArray> produtos, Context context, OnItemClickListener listener) {
        this.produtos = produtos != null ? produtos : new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduto;
        TextView txtNome, txtCodigo, txtValor, txtTipo;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduto = itemView.findViewById(R.id.imgProduto);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtCodigo = itemView.findViewById(R.id.txtCodigo);
            txtValor = itemView.findViewById(R.id.txtValor);
            txtTipo = itemView.findViewById(R.id.txtTipo);
        }
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        SuperClassProd.ProdutoDtoArray produto = produtos.get(position);

        // Nome
        holder.txtNome.setText(produto.getNomeProduto() != null ? produto.getNomeProduto() : "Nome indisponível");

        // Código
        holder.txtCodigo.setText("Código: " + produto.getCodProduto());

        // Valor
        holder.txtValor.setText(String.format("R$ %.2f", produto.getValorProduto()));

        // Tipo
        holder.txtTipo.setText("Categoria: " + (produto.getTipoProduto() != null ? produto.getTipoProduto() : "Desconhecida"));

        // Imagem Base64
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

        // Clique no item
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

    public void atualizarLista(List<SuperClassProd.ProdutoDtoArray> novaLista) {
        if (novaLista == null) {
            this.produtos = new ArrayList<>();
        } else {
            this.produtos = novaLista;
        }
        notifyDataSetChanged();
    }
}
