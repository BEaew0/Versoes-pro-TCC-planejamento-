package com.example.tesouro_azul_app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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

        String imgBase64 = produto.getImgProduto();
        if (imgBase64 != null && !imgBase64.isEmpty()) {
            try {
                imgBase64 = imgBase64.replace("\n", "").replace("\r", "");

                byte[] imageBytes = android.util.Base64.decode(imgBase64, android.util.Base64.DEFAULT);
                Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                if (bitmap != null) {
                    holder.imgProduto.setImageBitmap(bitmap);
                } else {
                    holder.imgProduto.setImageResource(R.drawable.error_image);
                }
            } catch (Exception e) {
                Log.e("AdapterError", "Erro ao processar imagem: " + e.getMessage());
                holder.imgProduto.setImageResource(R.drawable.error_image);
            }
        } else {
            holder.imgProduto.setImageResource(R.drawable.placeholder);
        }

        holder.txtNome.setText(produto.getNomeProduto() != null ? produto.getNomeProduto() : "Nome não disponível");
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
