using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;
using TesouroAzulAPI.Data;
using TesouroAzulAPI.Dtos;
using TesouroAzulAPI.Models;

namespace TesouroAzulAPI.Controllers
{
    [Route("api/PedidoCompra"), ApiController]
    public class PedidoCompraController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        // Instandiando o context da ApplicationDbContext para _context
        public PedidoCompraController(ApplicationDbContext context) { _context = context; }

        // DTOs
        // DTO para switch de campos
        public class CamposDtos
        {
            [Required]
            public string Campo { get; set; } = string.Empty;
            [Required]
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

            var itensSalvo = new List<ItensCompra>();
            foreach (var item in dto.Item)
            {
                var itemCompra = new ItensCompra
                {
                    ID_PRODUTO_FK = item.ID_PRODUTO_FK,
                    ID_PEDIDO_FK = idPedido, // Usando o ID do pedido criado
                    VAL_ITEM_COMPRA = item.VAL_ITEM_COMPRA ?? null,
                    LOTE_COMPRA = item.LOTE_COMPRA,
                    QUANTIDADE_ITEM_COMPRA = item.QUANTIDADE_ITEM_COMPRA,
                    N_ITEM_COMPRA = item.N_ITEM_COMPRA
                };

                _context.ItensCompra.Add(itemCompra);
                itensSalvo.Add(itemCompra);
            }
            await _context.SaveChangesAsync();

            // Retorno do JSON com o pedido e seus items
            return Ok(new
            {
                PedidoID = pedido.ID_PEDIDO,
                Itens = itensSalvo.Select(i => new
                {
                    i.ID_ITEM_COMPRA,
                    i.ID_PRODUTO_FK,
                    i.QUANTIDADE_ITEM_COMPRA
                })
            });
        }

        // Inserir item em compra
        [HttpPost("Itens")]
        public async Task<IActionResult> InserirItemCompra([FromBody] List<ItensCompraDto> dto)
        {
            if (dto == null || !dto.Any()) return BadRequest("Lista de itens não pode ser vazia");
            var itensSalvos = new List<ItensCompra>();
            foreach (var item in dto)
            {
                var itemCompra = new ItensCompra
                {
                    ID_PRODUTO_FK = item.ID_PRODUTO_FK,
                    ID_PEDIDO_FK = item.ID_PEDIDO_FK,
                    VAL_ITEM_COMPRA = item.VAL_ITEM_COMPRA ?? null,
                    LOTE_COMPRA = item.LOTE_COMPRA,
                    QUANTIDADE_ITEM_COMPRA = item.QUANTIDADE_ITEM_COMPRA,
                    N_ITEM_COMPRA = item.N_ITEM_COMPRA
                };
                _context.ItensCompra.Add(itemCompra);
                itensSalvos.Add(itemCompra);
            }
            await _context.SaveChangesAsync();
            return Ok(itensSalvos);
        }

        // Buscar pedido por campo do pedido
        [HttpPost("Pedido/Buscar-por-campo")]
        public async Task<ActionResult<IEnumerable<PedidosCompra>>> PedidoBuscarPorCampo(int id_usuario_fk, [FromBody] CamposDtos filtro)
        {
            // tratamento de erro
            if (id_usuario_fk == 0) return BadRequest("ID do usuário não pode ser 0");
            List<PedidosCompra> pedidoCampo = new();

            switch (filtro.Campo.ToLower())
            {
                case "fornecedor_pedido":
                    pedidoCampo = await _context.PedidosCompra.Where(p => p.ID_FORNECEDOR_FK == Convert.ToInt16(filtro.NovoValor)).ToListAsync();

                    break;
                case "data_pedido":
                    if (!DateTime.TryParse(filtro.NovoValor, out DateTime dataPedido)) return BadRequest("Novo Valor para Data deve ser uma data válida.");
                    pedidoCampo = await _context.PedidosCompra.Where(p => p.DATA_PEDIDO == Convert.ToDateTime(filtro.NovoValor)).ToListAsync();

                    break;
                // Retorna os pedidos com data anterior
                case "data_anterior_pedido":
                    if(!DateTime.TryParse(filtro.NovoValor, out DateTime dataAnterior)) return BadRequest("Novo Valor para Data deve ser uma data válida.");
                    pedidoCampo = await _context.PedidosCompra.Where(p => p.DATA_PEDIDO < Convert.ToDateTime(filtro.NovoValor)).ToListAsync();

                    break;
                // Retorna os pedidos com data posterior
                case "data_posterior_pedido":
                    if (!DateTime.TryParse(filtro.NovoValor, out DateTime dataPosterior)) return BadRequest("Novo Valor para Data deve ser uma data válida.");
                    pedidoCampo = await _context.PedidosCompra.Where(p => p.DATA_PEDIDO > Convert.ToDateTime(filtro.NovoValor)).ToListAsync();
                    break;
                case "valor_pedido":
                    pedidoCampo = await _context.PedidosCompra.Where(p => p.VALOR_PEDIDO == Convert.ToDecimal(filtro.NovoValor)).ToListAsync();

                    break;
                case "valor_menor_pedido":
                    pedidoCampo = await _context.PedidosCompra.Where(p => p.VALOR_PEDIDO < Convert.ToDecimal(filtro.NovoValor)).ToListAsync();

                    break;
                case "valor_maior_pedido":
                    pedidoCampo = await _context.PedidosCompra.Where(p => p.VALOR_PEDIDO > Convert.ToDecimal(filtro.NovoValor)).ToListAsync();
                    break;
                default:
                    return BadRequest("Somente os campos são aceitos : fornecedor_pedido, data_pedido, data_anterior_pedido, data_posterior_pedido, valor_pedido," +
                        "valor_menor_pedido, valor_maior_pedido");
                    break;
            }

            if (!pedidoCampo.Any()) return NotFound("Valor não encontrado");
            return Ok(pedidoCampo);
        }
        // Buscar por campo do item
        [HttpPost("item-compra/Buscar-por-campo")]
        public async Task<ActionResult<IEnumerable<ItensCompra>>> ItemBuscarPorCampo(int id_pedido, [FromBody] CamposDtos filtro)
        {
            // tratamento de erro
            if (id_pedido == 0) return BadRequest("ID do pedido não pode ser 0");
            List<ItensCompra> itemCampo = new();
            switch (filtro.Campo.ToLower())
            {
                case "produto_item":
                    itemCampo = await _context.ItensCompra.Where(i => i.ID_PRODUTO_FK == Convert.ToInt16(filtro.NovoValor) && i.ID_PEDIDO_FK == id_pedido).ToListAsync();
                    break;
                case "lote_item":
                    itemCampo = await _context.ItensCompra.Where(i => i.LOTE_COMPRA == filtro.NovoValor && i.ID_PEDIDO_FK == id_pedido).ToListAsync();
                    break;
                case "quantidade_item":
                    itemCampo = await _context.ItensCompra.Where(i => i.QUANTIDADE_ITEM_COMPRA == Convert.ToDecimal(filtro.NovoValor) && i.ID_PEDIDO_FK == id_pedido).ToListAsync();
                    break;
                case "n_item_compra":
                    itemCampo = await _context.ItensCompra.Where(i => i.N_ITEM_COMPRA == Convert.ToInt16(filtro.NovoValor) && i.ID_PEDIDO_FK == id_pedido).ToListAsync();
                    break;
                case "val_item":
                    if (!DateTime.TryParse(filtro.NovoValor, out DateTime valItem)) return BadRequest("Novo Valor para Data deve ser uma data válida.");
                    itemCampo = await _context.ItensCompra.Where(i => i.VAL_ITEM_COMPRA == Convert.ToDateTime(filtro.NovoValor) && i.ID_PEDIDO_FK == id_pedido).ToListAsync();
                    break;
                // Item vencido
                case "item_vencido":
                    filtro.NovoValor = DateTime.Now.ToString("yyyy-MM-dd");
                    itemCampo = await _context.ItensCompra.Where(i => i.VAL_ITEM_COMPRA < Convert.ToDateTime(filtro.NovoValor) && i.ID_PEDIDO_FK == id_pedido).ToListAsync();
                    break;
                // Item não vencido
                case "item_nao_vencido":
                    filtro.NovoValor = DateTime.Now.ToString("yyyy-MM-dd");
                    itemCampo = await _context.ItensCompra.Where(i => i.VAL_ITEM_COMPRA > Convert.ToDateTime(filtro.NovoValor) && i.ID_PEDIDO_FK == id_pedido).ToListAsync();
                    break;
                default:
                    return BadRequest("Somente os campos são aceitos : produto_item, lote_item, quantidade_item, n_item_compra, val_item, item_vencido, item_nao_vencido");
            }
            if (!itemCampo.Any()) return NotFound("Item não encontrado");
            return Ok(itemCampo);
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
        //Bucsar Itens das compras por pedido
        [HttpGet("Itens/{id_pedido}")]
        public async Task<ActionResult<IEnumerable<ItensCompra>>> BuscarItensCompraPorPedido(int id_pedido)
        {
            var itens = await _context.ItensCompra.Where(i => i.ID_PEDIDO_FK == id_pedido).ToListAsync();
            if (!itens.Any()) return NotFound("Nenhum item encontrado para o pedido");
            return Ok(itens);
        }

        //Buscar Compras Pedido por usuario
        [HttpGet("Usuario/{id_usuario}")]
        public async Task<ActionResult<IEnumerable<PedidosCompra>>> BuscarComprasPedidoPorUsuario(int id_usuario)
        {
            // tratamento de erro
            if (id_usuario == 0) return BadRequest("ID do usuário não pode ser 0");
            var pedido = await _context.PedidosCompra.Where(p => p.ID_USUARIO_FK == id_usuario).ToListAsync();
            if (!pedido.Any()) return NotFound("Nenhum pedido encontrado para este usuário");
            return Ok(pedido);
        }

        //PATCHs
        //Alterar Pedido Compra {campo}
        [HttpPatch("{id_pedido}")]
        public async Task<IActionResult> AlterarPedidoCompra(int id_pedido, [FromBody] CamposDtos dto)
        {
            // tratamento de erro
            var pedido = await _context.PedidosCompra.FindAsync(id_pedido);
            if (pedido == null) return NotFound("Pedido não encontrado");
            // Inicia busca dinamica
            switch (dto.Campo.ToLower())
            {
                case "fornecedor_pedido":
                    pedido.ID_FORNECEDOR_FK = Convert.ToInt16(dto.NovoValor);
                    break;
                case "data_pedido":
                    if (DateTime.TryParse(dto.NovoValor, out DateTime dataVal)) { pedido.DATA_PEDIDO = dataVal; }
                    else { return BadRequest("Novo Valor para Data deve ser uma data válida."); }
                    break;
                case "valor_pedido":
                    pedido.VALOR_PEDIDO = Convert.ToDecimal(dto.NovoValor);
                    break;
                default:
                    return BadRequest("Campos permitidos : fornecedor_pedido, data_pedido, valor_pedido");
            }
            _context.PedidosCompra.Update(pedido);
            await _context.SaveChangesAsync();
            return Ok(pedido);
        }
        // Alterar Itens Compra {campo}
        [HttpPatch("Itens/{id_item}")]
        public async Task<IActionResult> AlterarItemCompra(int id_item, [FromBody] CamposDtos dto)
        {
            // tratamento de erro
            var item = await _context.ItensCompra.FindAsync(id_item);
            if (item == null) return NotFound("Item não encontrado");
            switch (dto.Campo.ToLower())
            {
                case "produto_item":
                    item.ID_PRODUTO_FK = Convert.ToInt16(dto.NovoValor);
                    break;
                case "lote_item":
                    item.LOTE_COMPRA = dto.NovoValor;
                    break;
                case "quantidade_item":
                    item.QUANTIDADE_ITEM_COMPRA = Convert.ToDecimal(dto.NovoValor);
                    break;
                case "n_item_compra":
                    item.N_ITEM_COMPRA = Convert.ToInt16(dto.NovoValor);
                    break;
                default:
                    return BadRequest("Campos permitidos : produto_item, lote_item, quantidade_item, n_item_compra");
            }
            _context.ItensCompra.Update(item);
            await _context.SaveChangesAsync();
            return Ok(item);
        }

        //DElETEs
        //Deletar pedido compra
        [HttpDelete("{id_pedido}")]
        public async Task<IActionResult> DeletarPedidoCompra(int id_pedido)
        {
            // tratamento de erro
            var pedido = await _context.PedidosCompra.FindAsync(id_pedido);
            if (pedido == null) return NotFound("Pedido não encontrado");
            _context.PedidosCompra.Remove(pedido);
            await _context.SaveChangesAsync();
            return Ok("Pedido deletado com sucesso");

        }
        // Deletar Itens Compra
        [HttpDelete("Itens/{id_item}")]
        public async Task<IActionResult> DeletarITemCompra(int id_pedido, int id_item)
        {

            return default;
        }
    }
}
