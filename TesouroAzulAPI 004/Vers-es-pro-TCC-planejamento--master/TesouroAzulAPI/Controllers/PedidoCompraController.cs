using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using TesouroAzulAPI.Data;
using TesouroAzulAPI.Dtos;
using TesouroAzulAPI.Models;

namespace TesouroAzulAPI.Controllers
{
    [Route ("api/PedidoCompra"), ApiController]
    public class PedidoCompraController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        // Instandiando o context da ApplicationDbContext para _context
        public PedidoCompraController(ApplicationDbContext context) { _context = context; }

        // DTOs
        // DTO para switch de campos
        public class CamposDtos
        {
            public string Campo { get; set; } = string.Empty;
            public string NovoValor { get; set; } = string.Empty;
        }


        //POSTs
        //Criar Pedido da Compra
        //Neste POST já registra nas tabealas TB_PEDIDO_COMPRA, TB_ITEM_COMPRA, TB_ESTOQUE_PRODUTO (Já será cadastrado por Trigger)
        [HttpPost]
        public async Task<IActionResult> CriarPedidoCompra([FromBody] PedidoCompraCompleto dto)
        {

            // Por se tratar de um tabela que depende de outra, já salva em ambas automaticamente

            var pedido = new PedidosCompra
            {
                ID_USUARIO_FK = dto.Pedido.ID_USUARIO_FK,
                ID_FORNECEDOR_FK = dto.Pedido.ID_FORNECEDOR,
                VALOR_PEDIDO = dto.Pedido.VALOR_VALOR
            };

            _context.PedidosCompra.Add(pedido);
            await _context.SaveChangesAsync();
            

            // Adicionar busca aqui para descobrir o ID do pedido criado e adicionar na variavel
            int idPedido = pedido.ID_PEDIDO;


            var itemCompra = new ItensCompra
            {
                ID_PRODUTO_FK = dto.Item.ID_PRODUTO_FK,
                ID_PEDIDO_FK = idPedido,
                LOTE_COMPRA = dto.Item.LOTE_COMPRA,
                QUANTIDADE_ITEM_COMPRA = dto.Item.QUANTIDADE_ITEM_COMPRA,
                N_ITEM_COMPRA = dto.Item.N_ITEM_COMPRA
            };

            _context.ItensCompra.Add(itemCompra);
            await _context.SaveChangesAsync();

            return Ok(new { PedidoID = pedido.ID_PEDIDO, ItemID = itemCompra.ID_ITEM_COMPRA});
        }
        // Buscar por campo do pedido
        [HttpPost("Pedido/Buscar-por-campo")]
        public async Task<ActionResult<IEnumerable<PedidosCompra>>> PedidoBuscarPorCampo(int id_usuario_fk, [FromBody] CamposDtos filtro)
        {
            // tratamento de erro
            int id_usuario = id_usuario_fk;
            if (id_usuario == 0) return BadRequest("ID do usuário não pode ser 0");
            // Verifica se o campo existe na tabela
            var campo = _context.PedidosCompra.FirstOrDefault(x => x.GetType().GetProperty(filtro.Campo) != null);
            if (campo == null) return BadRequest("Campo não existe na tabela");

            switch (filtro.Campo) 
            {
                case "fornecedor_pedido":
                    var pedidoFornecedor = await _context.PedidosCompra.Where(p => p.Fornecedor.NOME_FORNECEDOR == filtro.NovoValor).ToListAsync();
                    if(!pedidoFornecedor.Any()) return NotFound("Fornecedor não encontrado");
                    return Ok(pedidoFornecedor);
                    break;
                case "data_pedido":
                    var pedidoData = await _context.PedidosCompra.Where(p => p.DATA_PEDIDO == Convert.ToDateTime(filtro.NovoValor)).ToListAsync();
                    if (!pedidoData.Any()) return NotFound("Data não encontrada");
                    return Ok(pedidoData);
                    break;
                case "data_anterior_pedido":
                    var pedidoDataAnterior = await _context.PedidosCompra.Where(p => p.DATA_PEDIDO < Convert.ToDateTime(filtro.NovoValor)).ToListAsync();
                    if (!pedidoDataAnterior.Any()) return NotFound("Data não encontrada");
                    // Retorna os pedidos com data anterior
                    return Ok(pedidoDataAnterior);
                    break;
                case "data_posterior_pedido":
                    var pedidoDataPosterior = await _context.PedidosCompra.Where(p => p.DATA_PEDIDO > Convert.ToDateTime(filtro.NovoValor)).ToListAsync();
                    if (!pedidoDataPosterior.Any()) return NotFound("Data não encontrada");
                    // Retorna os pedidos com data posterior
                    return Ok(pedidoDataPosterior);
                    break;
                case "cod_lote_pedido":
                    var pedidoLote = await _context.ItensCompra.Where(p => p.LOTE_COMPRA == filtro.NovoValor).ToListAsync();
                    if (!pedidoLote.Any()) return NotFound("Lote não encontrado");
                    return Ok(pedidoLote);
                    break;
                case "valor_pedido":
                    var pedidoValor = await _context.PedidosCompra.Where(p => p.VALOR_PEDIDO == Convert.ToDecimal(filtro.NovoValor)).ToListAsync();
                    if (!pedidoValor.Any()) return NotFound("Valor não encontrado");
                    return Ok(pedidoValor);
                    break;
                case "valor_menor_pedido":
                    var pedidoValorMenor = await _context.PedidosCompra.Where(p => p.VALOR_PEDIDO < Convert.ToDecimal(filtro.NovoValor)).ToListAsync();
                    if (!pedidoValorMenor.Any()) return NotFound("Valor não encontrado");
                    return Ok(pedidoValorMenor);
                    break;
                case "valor_maior_pedido":
                    var pedidoValorMaior = await _context.PedidosCompra.Where(p => p.VALOR_PEDIDO > Convert.ToDecimal(filtro.NovoValor)).ToListAsync();
                    if (!pedidoValorMaior.Any()) return NotFound("Valor não encontrado");
                    return Ok(pedidoValorMaior);
                    break;
                default:
                    return BadRequest("Somente os campos são aceitos : fornecedor_pedido, data_pedido, data_anterior_pedido, data_posterior_pedido, cod_lote_pedido, valor_pedido," +
                        "valor_menor_pedido, valor_maior_pedido");
                    break;
            }
        }
        //GETs
        //Buscar Compras Pedido
        [HttpGet]
        public async Task<ActionResult<IEnumerable<PedidosCompra>>> BuscarComprasPedido()
        {
            var pedido = await _context.PedidosCompra.ToListAsync();
            if (!pedido.Any()) return NotFound("Nenhum pedido encontrado");
            return Ok(pedido);
        }
        //Buscar Compras Pedido por usuario

        //PATCHs
        //Alterar Pedido Compra {campo}

        //DElETEs
        //Deletar pedido compra


    }
}
