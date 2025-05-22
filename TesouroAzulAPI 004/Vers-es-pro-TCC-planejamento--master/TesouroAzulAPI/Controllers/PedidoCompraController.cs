using Microsoft.AspNetCore.Mvc;
using TesouroAzulAPI.Data;
using TesouroAzulAPI.Dtos;
using TesouroAzulAPI.Models;

namespace TesouroAzulAPI.Controllers
{
    [Route ("api/PeidoCompra"), ApiController]
    public class PedidoCompraController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        // Instandiando o context da ApplicationDbContext para _context
        public PedidoCompraController(ApplicationDbContext context) { _context = context; }

        //POSTs
        //Criar Pedido da Compra
        //Neste POST já registra nas tabealas TB_PEDIDO_COMPRA, TB_ITEM_COMPRA, TB_ESTOQUE_PRODUTO (Já será cadastrado por Trigger)
        [HttpPost]
        public async Task<IActionResult> CriarPedidoCompra([FromBody] CriarPedidoCompraDto PedidoDto )
        {
            var pedido = new PedidosCompra();
            {
                 

            }
            // temporario
            return default;
        }
        // Buscar por campo

        //GETs
        //Buscar Compras Pedido

        //Buscar Compras Pedido por usuario

        //PATCHs
        //Alterar Pedido Compra {campo}

        //DElETEs
        //Deletar pedido compra


    }
}
