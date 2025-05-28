using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;
using TesouroAzulAPI.Data;
using TesouroAzulAPI.Models;
using TesouroAzulAPI.Dtos;

namespace TesouroAzulAPI.Controllers
{
    public class PedidoVendaController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        // Instanciando com o banco
        public PedidoVendaController(ApplicationDbContext context) { _context = context; }

        // DTOs
        // Dto de campo dinamico
        public class CamposDtos
        {
            public string Campo { get; set; } = string.Empty;
            public string Valor { get; set; } = string.Empty;

        }

        // POSTs
        // Criar Pedido Venda
        // Neste Post já adiciona os ItensVenda associados ao PedidoVenda
        [HttpPost("CriarPedidoVenda")]
        public async Task<IActionResult> CriarPedidoVenda([FromBody] Dtos.PedidoVendaCompleto dto)
        {
            var pedidoVenda = new PedidosVenda
            {
                ID_USUARIO_FK = dto.Pedido.ID_USUARIO_FK,
                VALOR_PEDIDO_VENDA = dto.Pedido.VALOR_PEDIDO_VENDA ?? 0.00m // Valor padrão se não for informado
            };

            _context.PedidosVenda.Add(pedidoVenda);
            await _context.SaveChangesAsync();

            // Adicionando os ItensVenda associados ao PedidoVenda

            int idPedidoVenda = pedidoVenda.ID_PEDIDO_VENDA;

            var itensSalvo = new List<ItensVenda>();
            foreach (var item in dto.Item)
            {
                var itemVenda = new ItensVenda
                {
                    ID_PRODUTO_FK = item.ID_PRODUTO_FK,
                    ID_PEDIDO_VENDA_FK = idPedidoVenda,
                    LOTE_VENDA = item.LOTE_VENDA,
                    QTS_ITEM_VENDA = item.QTS_ITEM_VENDA,
                    N_ITEM_VENDA = item.N_ITEM_VENDA,
                    DESCONTO_ITEM_VENDA = item.DESCONTO_ITEM_VENDA ?? 0.00m, // Valor padrão se não for informado
                    VALOR_TOTAL_ITEM_VENDA = item.VALOR_TOTAL_ITEM_VENDA
                };

                _context.ItensVenda.Add(itemVenda);
                itensSalvo.Add(itemVenda);
            }
            await _context.SaveChangesAsync();

            return Ok(new
            {
                PedidoVenda = pedidoVenda,
                ItensVenda = itensSalvo
            });

        }

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
