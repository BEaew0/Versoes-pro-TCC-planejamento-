using System.ComponentModel.DataAnnotations;

namespace TesouroAzulAPI.Dtos
{
    public class CriarPedidoCompraDto
    {
        public int ID_USUARIO_FK {  get; set; }
        public int ID_FORNECEDOR {  get; set; }
        public string NOME_PRODUTO_PEDIDO { get; set; }
        public decimal VALOR_VALOR {  get; set; }

    }
}
