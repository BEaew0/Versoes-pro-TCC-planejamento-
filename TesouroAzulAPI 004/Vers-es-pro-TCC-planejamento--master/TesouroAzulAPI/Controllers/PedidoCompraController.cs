using Microsoft.AspNetCore.Mvc;
using TesouroAzulAPI.Data;

namespace TesouroAzulAPI.Controllers
{
    [Route ("api/PeidoCompra"), ApiController]
    public class PedidoCompraController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        // Instandiando o context da ApplicationDbContext para _context
        public PedidoCompraController(ApplicationDbContext context) { _context = context; }

        // Para este controller é necessário criar o models de EstoqueProduto, seguir com lógica aqui mais tarde
        //POSTs
        //Criar Pedido da Compra
        // Neste POST já registra nas tabealas TB_PEDIDO_COMPRA, TB_ITEM_COMPRA, TB_ESTOQUE_PRODUTO (Já será cadastrado por Trigger, porém é necessario)
        
    }
}
