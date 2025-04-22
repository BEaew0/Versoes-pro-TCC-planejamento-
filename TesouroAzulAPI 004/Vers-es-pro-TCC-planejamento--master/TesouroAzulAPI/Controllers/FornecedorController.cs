using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using TesouroAzulAPI.Data;
using TesouroAzulAPI.Models;

namespace TesouroAzulAPI.Controllers
{
    [Route("api/Fornecedores"), ApiController]
    public class FornecedorController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        // Instanciando o context da ApplicationDbContext para _context
        public FornecedorController(ApplicationDbContext context)
        {
            _context = context;
        }

        // DTO para Http AlterarCamposFornecedor
        public class AtualizarCampoFornecedorDto
        {
            public string Campo { get; set; } = string.Empty;
            public string NovoValor { get; set; } = string.Empty;
        }


        //POSTs
        //Criar Fornecedor
        [HttpPost]
        public async Task<IActionResult> CriarFornecedor([FromBody] Fornecedor fornecedor)
        {
            // Tratamento de erro
            if (await _context.Fornecedores.AnyAsync(f => f.CNPJ_FORNECEDOR == fornecedor.CNPJ_FORNECEDOR))
            {
                return BadRequest(new { mensagem = "CNPJ já cadastrado." });
            }

            _context.Fornecedores.Add(fornecedor);
            await _context.SaveChangesAsync();
            return CreatedAtAction(nameof(BuscarFornecedorPorId), new { id = fornecedor.ID_FORNECEDOR }, fornecedor);
        }

        //GETs
        //Buscar Fornecedores
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Fornecedor>>> BuscarFornecedores()
        {
            return await _context.Fornecedores.ToListAsync();
        }

        //Burcar Forncecedor por ID
        [HttpGet("{id}")]
        public async Task<ActionResult<Fornecedor>> BuscarFornecedorPorId(int id)
        {
            var fornecedor = await _context.Fornecedores.FindAsync(id);
            if (fornecedor == null) return NotFound(new { mensagem = "Fornecedor não encontrado." });
            return fornecedor;
        }

        //PACHTs
        //Alterar Fornecedor
        [HttpPatch("{id}")]
        public async Task<IActionResult> AlterarFornecedor(int id, [FromBody] AtualizarCampoFornecedorDto dto)
        {
            var fornecedor = await _context.Fornecedores.FindAsync(id);
            if (fornecedor == null) return NotFound(new { mensagem = "Fornecedor não encontrado." });

            if (string.IsNullOrEmpty(dto.Campo) || string.IsNullOrEmpty(dto.NovoValor)) return BadRequest("Campo ou Novo Valor não podem ser nulos ou vazios.É permitido os seguintes campos : 'nome', 'cnpj', 'email', 'telefone', 'celular', 'endereco'");

            // Atribuindo novo valor ao campo escolhido
            switch (dto.Campo.ToLower())
            {
                case "nome":
                    fornecedor.NOME_FORNECEDOR = dto.NovoValor;
                    break;
                case "cnpj":
                    if (await _context.Fornecedores.AnyAsync(f => f.CNPJ_FORNECEDOR == dto.NovoValor)) return BadRequest(new { mensagem = "CNPJ já cadastrado." });
                    fornecedor.CNPJ_FORNECEDOR = dto.NovoValor;
                    break;
                case "email":
                    if (await _context.Fornecedores.AnyAsync(f => f.EMAIL_FORNECEDOR == dto.NovoValor)) return BadRequest(new { mensagem = "Email já cadastrado." });
                    fornecedor.EMAIL_FORNECEDOR = dto.NovoValor;
                    break;
                case "telefone":
                    if (await _context.Fornecedores.AnyAsync(f => f.TEL_FORNECEDOR == dto.NovoValor)) return BadRequest(new { mensagem = "Telefone já cadastrado." });
                    fornecedor.TEL_FORNECEDOR = dto.NovoValor;
                    break;
                case "celular":
                    if (await _context.Fornecedores.AnyAsync(f => f.CEL_FORNECEDOR == dto.NovoValor)) return BadRequest(new { mensagem = "Celular já cadastrado." });
                    fornecedor.CEL_FORNECEDOR = dto.NovoValor;
                    break;
                case "endereco":
                    fornecedor.ENDERECO_FORNECEDOR = dto.NovoValor;
                    break;
                default:
                    return BadRequest("Campo inválido. Os campos válidos são: 'nome', 'cnpj', 'email', 'telefone', 'celular', 'endereco'");
            }

            _context.Fornecedores.Update(fornecedor);
            await _context.SaveChangesAsync();
            return Ok(new { mensagem = "Fornecedor atualizado com sucesso." });

        }


        //DELETEs
        //Deletar Fornecedor
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletarFornecedor(int id)
        {
            var fornecedor = await _context.Fornecedores.FindAsync(id);
            if (fornecedor == null) return NotFound(new { mensagem = "Fornecedor não encontrado." });

            _context.Fornecedores.Remove(fornecedor);
            await _context.SaveChangesAsync();
            return Ok(new { mensagem = "Fornecedor deletado com sucesso." });
        }
    }
}
