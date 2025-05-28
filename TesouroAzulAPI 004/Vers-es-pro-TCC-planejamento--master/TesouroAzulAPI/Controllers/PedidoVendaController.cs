using Microsoft.AspNetCore.Mvc;
using TesouroAzulAPI.Data;

namespace TesouroAzulAPI.Controllers
{
    public class PedidoVendaController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        // Instanciando com o banco
        public PedidoVendaController(ApplicationDbContext context) { _context = context; }

        // POSTs
        // Criar Pedido Venda
        // Neste Post já adiciona os ItensVenda associados ao PedidoVenda

        // Criar Item Venda
        // Adiciona Item associado ao Pedido venda 

        // Buscar Pedido Venda por campo

        // Buscar Itens Venda por campo

        // GETs
        // Buscar todos os pedidos de venda

        // Buscar pedidos venda por id_usuario

        // Buscar todos os itens de venda

        // Buscar itens venda por id_usuario

        // PATCHs
        // Alterar Pedido Venda por id_pedido_venda

        // Alterar Item Venda por id_item_venda

        // DELETEs
        // Deletar Pedido Venda por id_usuario

        // Deletar Item Venda por id_item_venda
    }
}
