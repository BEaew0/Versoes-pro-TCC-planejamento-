using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks.Dataflow;
using Microsoft.EntityFrameworkCore;
using TesouroAzulAPI.Data;
using TesouroAzulAPI.Dtos;
using TesouroAzulAPI.Models;
using Microsoft.AspNetCore.Mvc.Infrastructure;
using System.Numerics;
using Microsoft.OpenApi.Any;

namespace TesouroAzulAPI.Controllers
{
    [Route("api/Produtos"), ApiController]
    public class ProdutoController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        // instanciando o contexto do banco
        public ProdutoController(ApplicationDbContext context)
        {
            _context = context;
        }

        // DTOs
        // DTO para switch de campos
        public class camposDtos
        {
            public string Campo { get; set; } = string.Empty;
            public string NovoValor { get; set; } = string.Empty;
        }

        //POSTs
        //Cadastrar Produto

        [HttpPost]
        public async Task<IActionResult> CadastrarProduto([FromBody] CadastrarProdutoDto produtoDto)
        {
            var produto = new Produto
            {
                ID_USUARIO_FK = produtoDto.ID_USUARIO,
                COD_PRODUTO = produtoDto.COD_PRODUTO,
                NOME_PRODUTO = produtoDto.NOME_PRODUTO,
                TIPO_PRODUTO = produtoDto.TIPO_PRODUTO,
                DATA_VAL_PRODUTO = produtoDto.DATA_VAL_PRODUTO,
                IMG_PRODUTO = produtoDto.IMG_PRODUTO
            };

            _context.Produtos.Add(produto);
            await _context.SaveChangesAsync();
            return Ok();
            
        }

        //Buscar Produto {campo}
        [HttpPost("Buscar-por-campo")]
        public async Task<ActionResult<IEnumerable<Produto>>> BurcarPorCampo(int id_usuario_fk, [FromBody] camposDtos filtro)
        {
            // tratamento de erro
            int id_usuario = id_usuario_fk;
            var usuario = await _context.Usuarios.FindAsync(id_usuario);
            if (usuario == null) return NotFound("Usuario não encontrado");
            if (string.IsNullOrEmpty(filtro.Campo) || string.IsNullOrEmpty(filtro.NovoValor)) return BadRequest("Campos permitidos : cod_produto, nome_produto, tipo_produto, data_val_produto, não_vencidos, vencidos");

            switch (filtro.Campo)
            {
                case "cod_produto":
                    var produtoCod = await _context.Produtos.Where(p => p.COD_PRODUTO == filtro.NovoValor).ToListAsync();
                    if (!produtoCod.Any()) return NotFound("Código não encontrado");
                    return Ok(produtoCod);
                    break;
                case "nome_produto":
                    var produtoNome = await _context.Produtos.Where(p => p.NOME_PRODUTO == filtro.NovoValor).ToListAsync();
                    if (!produtoNome.Any()) return NotFound("Nome não encontrado");
                    return Ok(produtoNome);
                    break;
                case "tipo_produto":
                    var produtoTipo = await _context.Produtos.Where(p => p.TIPO_PRODUTO == filtro.NovoValor).ToListAsync();
                    if (!produtoTipo.Any()) return NotFound("Tipo não encontrado");
                    return Ok(produtoTipo);
                    break;
                case "data_val_produto":
                    var produtoData = await _context.Produtos.Where(p => p.DATA_VAL_PRODUTO == DateTime.Parse(filtro.NovoValor)).ToListAsync();
                    if (!produtoData.Any()) return NotFound("Data não encontrada");
                    return Ok(produtoData);
                    break;
                case "não_vencidos":
                    var produtoNaoVencidos = await _context.Produtos.Where(p => p.DATA_VAL_PRODUTO > DateTime.Now).ToListAsync();
                    if (!produtoNaoVencidos.Any()) return NotFound("Produto não encontrado");
                    return Ok(produtoNaoVencidos);
                    break;
                case "vencidos":
                    var produtoVencidos = await _context.Produtos.Where(p => p.DATA_VAL_PRODUTO < DateTime.Now).ToListAsync();
                    if (!produtoVencidos.Any()) return NotFound("Produto não encontrado");
                    return Ok(produtoVencidos);
                    break;
                default:
                    return BadRequest("Campos permitidos : cod_produto, nome_produto, tipo_produto, data_val_produto, não_vencidos, vencidos");
            }


        }

        // criar um post especifico somente para validade
        //GETs
        //Buscar Produtos
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Produto>>> BuscarProdutos()
        {
            return await _context.Produtos.ToListAsync();
        }

        // Bucar Produtos {id_usuario}
        [HttpGet("usuario/{id_usuario}")]
        public async Task<ActionResult<IEnumerable<Produto>>> BuscarProdutoIdUsuario(int id_usuario)
        {
            var produto = await _context.Produtos.Where(p => p.ID_USUARIO_FK == id_usuario).ToListAsync();
            if (!produto.Any()) return NotFound("Produto não encontrado para este usuario");
            return Ok(produto);
        }

        //Buscar Produto {id}
        [HttpGet("produto/{id}")]
        public async Task<ActionResult<IEnumerable<Produto>>> BuscarProdutoId(int id)
        {
            var produto = await _context.Produtos.FindAsync(id);
            if (produto == null) return NotFound("Produto não encontrado");
            return Ok(produto);
        }

         
        //PATCHs
        //Alterar Produto {campo}
        [HttpPatch("{id}")]
        public async Task<IActionResult> AlterarProduto(int id, [FromBody] camposDtos campo)
        {
            // tratamento de erro
            var produto = await _context.Produtos.FindAsync(id);
            if (produto == null) return NotFound("Produto não encontrado");

            switch(campo.Campo)
            {
                case "cod_produto":
                    produto.COD_PRODUTO = campo.NovoValor;
                    break;
                case "nome_produto":
                    produto.NOME_PRODUTO = campo.NovoValor;
                    break;
                case "tipo_produto":
                    produto.TIPO_PRODUTO = campo.NovoValor;
                    break;
                case "data_val_produto":
                    if (DateTime.TryParse(campo.NovoValor, out DateTime dataVal))
                    {
                        produto.DATA_VAL_PRODUTO = dataVal;
                    }
                    else
                    {
                        return BadRequest("Novo Valor para Data deve ser uma data válida.");
                    }
                    break;
                default:
                    return BadRequest("Campos permitidos : cod_produto, nome_produto, tipo_produto, data_val_produto");
                    break;
            }

            _context.Produtos.Update(produto);
            await _context.SaveChangesAsync();
            return Ok(produto);

        }
        //Alterar Imagem

        //DELETs
        //Deletar Produto {id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletarProduto(int id)
        {
            var id_produto = await _context.Produtos.FindAsync(id);
            if (id_produto == null) return NotFound("Produto não encontrado");

            _context.Produtos.Remove(id_produto);
            await _context.SaveChangesAsync();
            return Ok( new { mensagem = "Produto Removido com sucesso"});
        }
    }
}
