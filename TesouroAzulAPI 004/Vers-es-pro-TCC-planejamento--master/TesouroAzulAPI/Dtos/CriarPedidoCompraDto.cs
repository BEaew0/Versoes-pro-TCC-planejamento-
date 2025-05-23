using System.ComponentModel.DataAnnotations;

namespace TesouroAzulAPI.Dtos
{
    public class CriarPedidoCompraDto
    {
        public int ID_USUARIO_FK { get; set; }
        public int ID_FORNECEDOR { get; set; }
        public string NOME_PRODUTO_PEDIDO { get; set; }
        public decimal VALOR_VALOR { get; set; }

    }

    public class ItensCompraDto
    {
        public int ID_PRODUTO_FK { get; set; }
        public int ID_PEDIDO_FK { get; set; }
        public string LOTE_COMPRA { get; set; }
        public decimal QUANTIDADE_ITEM_COMPRA { get; set; }
        public int N_ITEM_COMPRA { get; set; }

    }
    public class PedidoCompraCompleto
    {
        public CriarPedidoCompraDto Pedido { get; set; }
        public ItensCompraDto Item { get; set; }
    }
}
