package com.example.tesouro_azul_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.tesouro_azul_app.Class.SuperClassProd;
import com.example.tesouro_azul_app.R;

import java.util.List;


public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private List<SuperClassProd.ProdutoDto> produtos;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SuperClassProd.ProdutoDto produto);
    }

    public ProdutoAdapter(List<SuperClassProd.ProdutoDto> produtos, Context context, OnItemClickListener listener) {
        this.produtos = produtos;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        SuperClassProd.ProdutoDto produto = produtos.get(position);

        // Carregamento de imagem com Glide
        Glide.with(context)
                .load(produto.getImgProduto())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.imgProduto);

        holder.txtNome.setText(produto.getNomeProduto());
        holder.txtCodigo.setText("Código: " + produto.getCodProduto());
        holder.txtValor.setText(String.format("R$ %.2f", produto.getValorProduto()));
        holder.txtTipo.setText("Categoria: " + produto.getTipoProduto());

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

    public void atualizarLista(List<SuperClassProd.ProdutoDto> novaLista) {
        produtos = novaLista;
        notifyDataSetChanged();
    }

}
