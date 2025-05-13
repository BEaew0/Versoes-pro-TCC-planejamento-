using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks.Dataflow;
using Microsoft.EntityFrameworkCore;
using TesouroAzulAPI.Data;
using TesouroAzulAPI.Dtos;
using TesouroAzulAPI.Models;

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


        //POSTs
        //Cadastrar Produto
        /*
        [HttpPost]
        public async Task<IActionResult> CadastrarProduto([FromBody] CadastrarProdutoDto produtoDto)
        {
            var produto = new Produto
            {
                ID_USUARIO_FK = produtoDto.ID_USUARIO,
                COD_PRODUTO = produtoDto.COD_PRODUTO,
                NOME_PRODUTO = produtoDto.NOME_PRODUTO,
                TIPO_PRODUTO = produtoDto.TIPO_PRODUTO,
                DATA_VAL_PRODUTO = produtoDto.DATA_VAL_PRODUTO, // Problema por se tratar de um datetime, para caso o dado seja NULL
                IMG_PRODUTO = produtoDto.IMG_PRODUTO
            };

            _context.Produtodos.Add(produto);
            await _context.SaveChangesAsync();
            return Ok(produto); // Colocar um retorno de mensagem aqui
            
        }
        */

        //GETs
        //Buscar Produtos
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Produto>>> BuscarProdutos()
        {
            return await _context.Produtodos.ToListAsync();
        }

        // Bucar Produtos {id_usuario}
        [HttpGet("{id_usuario}")]
        public async Task<ActionResult<IEnumerable<Produto>>> BuscarProdutoIdUsuario(int id_usuario)
        {
            var produto = await _context.Produtodos.FindAsync(id_usuario);
            if (produto == null) return NotFound("Produto não encontrado");
            return Ok(produto);
        }

        //Buscar Produto {id}
        [HttpGet("{id}")]
        public async Task<ActionResult<IEnumerable<Produto>>> BuscarProdutoId(int id)
        {
            var produto = await _context.Produtodos.FindAsync(id);
            if (produto == null) return NotFound("Produto não encontrado");
            return Ok(produto);
        }

        
        //Buscar Produto {campo}
        [HttpGet("Buscar-por-campo")]
        public async Task<ActionResult<IEnumerable<Produto>>> BurcarPorCampo(string campo, string novoValor)
        {
            

            // temporario para não dar erro
            return default;
        }

        //PATCHs
        //Alterar Produto {campo}

        //Alterar Imagem

        //DELETs
        //Deletar Produto {id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletarProduto(int id)
        {
            var id_produto = await _context.Produtodos.FindAsync(id);
            if (id_produto == null) return NotFound("Produto não encontrado");

            _context.Produtodos.Remove(id_produto);
            await _context.SaveChangesAsync();
            return Ok( new { mensagem = "Produto Removido com sucesso"});
        }
    }
}
