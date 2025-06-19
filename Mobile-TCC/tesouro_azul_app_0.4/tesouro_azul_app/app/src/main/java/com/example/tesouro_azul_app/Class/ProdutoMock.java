package com.example.tesouro_azul_app.Class;

import java.util.ArrayList;
import java.util.List;

//Serve para emular a lista de produtos
//Testo melhor no pc potente da etec

public class ProdutoMock {
    public static List<SuperClassProd.ProdutoDto> gerarProdutosMockados() {
        List<SuperClassProd.ProdutoDto> produtos = new ArrayList<>();

        produtos.add(new SuperClassProd.ProdutoDto(
                 "SMX-001", "Smartphone X", 1999.90,
                "Eletrônicos", ""
        ));

        produtos.add(new SuperClassProd.ProdutoDto(
                 "NTB-202", "Notebook Pro", 4599.00,
                "Eletrônicos", ""
        ));

        produtos.add(new SuperClassProd.ProdutoDto(
                 "MES-015", "Mesa de Escritório", 599.90,
                "Móveis", ""
        ));

        produtos.add(new SuperClassProd.ProdutoDto(
                 "CAD-033", "Cadeira Ergonômica", 899.00,
                "Móveis", ""
        ));

        produtos.add(new SuperClassProd.ProdutoDto(
                 "SMY-002", "Smartphone Y", 1799.00,
                "Eletrônicos", ""
        ));

        return produtos;
    }
}