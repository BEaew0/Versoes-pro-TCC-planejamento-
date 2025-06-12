package com.example.tesouro_azul_app.Class;

import java.util.ArrayList;
import java.util.List;

//Serve para emular a lista de produtos
//Testo melhor no pc potente da etec
public class ProdutoMock {
    public static List<SuperClassProd.ProdutoDto> gerarProdutosMockados() {
        List<SuperClassProd.ProdutoDto> produtos = new ArrayList<>();

        produtos.add(new SuperClassProd.ProdutoDto(
                1, "Smartphone X", "SMX-001", 1999.90,
                "Eletrônicos", "https://exemplo.com/smartphone.jpg"
        ));

        produtos.add(new SuperClassProd.ProdutoDto(
                2, "Notebook Pro", "NTB-202", 4599.00,
                "Eletrônicos", "https://exemplo.com/notebook.jpg"
        ));

        produtos.add(new SuperClassProd.ProdutoDto(
                3, "Mesa de Escritório", "MES-015", 599.90,
                "Móveis", "https://exemplo.com/mesa.jpg"
        ));

        produtos.add(new SuperClassProd.ProdutoDto(
                4, "Cadeira Ergonômica", "CAD-033", 899.00,
                "Móveis", "https://exemplo.com/cadeira.jpg"
        ));

        produtos.add(new SuperClassProd.ProdutoDto(
                5, "Smartphone Y", "SMY-002", 1799.00,
                "Eletrônicos", "https://exemplo.com/smartphone2.jpg"
        ));

        return produtos;
    }
}