package com.example.tesouro_azul_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    // Lista de produtos e contexto
    private List<SuperClassProd.ProdutoGet> produtos;
    private Context context;
    private OnItemClickListener listener;

    // Interface para clicks
    public interface OnItemClickListener {
        void onItemClick(SuperClassProd.ProdutoGet produto);
    }

    // Construtor
    public ProdutoAdapter(List<SuperClassProd.ProdutoGet> produtos, Context context, OnItemClickListener listener) {
        this.produtos = produtos;
        this.context = context;
        this.listener = listener;
    }

    // ViewHolder
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
        SuperClassProd.ProdutoGet produto = produtos.get(position);

        // Carregar imagem com Picasso
        Picasso.get()
                .load(produto.getImG_PRODUTO())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(holder.imgProduto);

        // Configurar textos
        holder.txtNome.setText(produto.getNomE_PRODUTO());
        holder.txtCodigo.setText("Código: " + produto.getCoD_PRODUTO());
        holder.txtValor.setText(String.format("R$ %.2f", produto.getValoR_PRODUTO()));
        holder.txtTipo.setText("Categoria: " + produto.getTipO_PRODUTO());

        // Configurar clique
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

    // Método para atualizar a lista
    public void atualizarLista(List<SuperClassProd.ProdutoGet> novaLista) {
        produtos = novaLista;
        notifyDataSetChanged();
    }
}
