using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using TesouroAzulAPI.Data;
using TesouroAzulAPI.Dtos;
using TesouroAzulAPI.Models;

namespace TesouroAzulAPI.Controllers
{
        [Route("api/Usuarios"), ApiController]
        public class UsuarioController : ControllerBase
        {
            private readonly ApplicationDbContext _context;

            // Instanciando o context da ApplicationDbContext para _context
            public UsuarioController(ApplicationDbContext context)
            {
                _context = context;
            }

            // DTO (Data Transfer Object) para o Http AlterarCamposUsuario 
            public class AtualizarCampoUsuarioDto 
            {
                public string Campo { get; set; } = string.Empty;
                public string NovoValor { get; set; } = string.Empty;

            }


            // POSTs
            // Criar Usuario
            [HttpPost]
            public async Task<IActionResult> CriarUsuario([FromBody] CriarUsuarioDto usuarioDto) 
            {
                if (await _context.Usuarios.AnyAsync(u => u.EMAIL_USUARIO == usuarioDto.EMAIL_USUARIO|| u.CPF_USUARIO == usuarioDto.CPF_USUARIO))
                {
                    return BadRequest(new {mensagem = "Email ou CPF já cadastrado." });
                }

                var usuario = new Usuario
                {
                    NOME_USUARIO = usuarioDto.NOME_USUARIO,
                    EMAIL_USUARIO = usuarioDto.EMAIL_USUARIO,
                    DATA_NASC_USUARIO = usuarioDto.DATA_NASC_USUARIO,
                    CPF_USUARIO = usuarioDto.CPF_USUARIO,
                    CNPJ_USUARIO = usuarioDto.CNPJ_USUARIO,
                    ID_ASSINATURA_FK = usuarioDto.ID_ASSINATURA_FK,
                    FOTO_USUARIO = usuarioDto.FOTO_USUARIO,
                    SENHA_USUARIO = usuarioDto.SENHA_USUARIO,
                    STATUS_USUARIO = usuarioDto.STATUS_USUARIO

                };

                _context.Usuarios.Add(usuario);
                await _context.SaveChangesAsync();
                return CreatedAtAction(nameof(BuscarUsuarioPorId), new { id = usuario.ID_USUARIO }, usuario);
            }

            // GETs
            // Buscar Usuarios
            [HttpGet]
            public async Task<ActionResult<IEnumerable<Usuario>>> BuscarUsuarios() 
            {
                return await _context.Usuarios.ToListAsync();
            }

            // Buscar Usuario por ID
            [HttpGet("{id}")]
            public async Task<ActionResult<Usuario>> BuscarUsuarioPorId(int id) 
            {
                var usuario = await _context.Usuarios.FindAsync(id);
                if (usuario == null) return NotFound("Usuario não encontrado.");
                return usuario;
            }

            // PATCHs
            // Atualizar Usuario
            [HttpPatch("{id}/alterar-campo")]
            public async Task<IActionResult> AlterarCamposUsuario(int id, [FromBody] AtualizarCampoUsuarioDto dto)
            {
                // Controller Dinâmico, ou seja, utiliza a classe AtualizarCampoUsuarioDto para qualquer campo e informação que deseja alterar

                // Tratamentos de erro de {id}
                var usuario = await _context.Usuarios.FindAsync(id);
                if (usuario == null) return NotFound("Usuario não encontrado.");

                if (string.IsNullOrEmpty(dto.Campo) || string.IsNullOrEmpty(dto.NovoValor))
                {
                    return BadRequest("Campo ou Novo Valor não podem ser nulos ou vazios.É permitido os seguintes campos : 'nome', 'email', 'cpf', 'cnpj', 'senha', 'data', 'status', 'statusAssinatura'");
                }

                switch (dto.Campo)
                {
                    case "nome":
                        usuario.NOME_USUARIO = dto.NovoValor;
                        break;
                    case "email":
                        usuario.EMAIL_USUARIO = dto.NovoValor;
                        break;
                    case "cpf":
                        usuario.CPF_USUARIO = dto.NovoValor;
                        break;
                    case "cnpj":
                        usuario.CNPJ_USUARIO = dto.NovoValor;
                        break;
                    case "senha":
                        usuario.SENHA_USUARIO = dto.NovoValor;
                        break;
                    case "data":
                        if (DateTime.TryParse(dto.NovoValor, out DateTime dataNasc))
                        {
                            usuario.DATA_NASC_USUARIO = dataNasc;
                        }
                        else
                        {
                            return BadRequest("Novo Valor para Data deve ser uma data válida.");
                        }
                        break;
                    case "imagem":
                        // preciso adiciar a logica de uma nova imagem
                        return Ok();
                        break;
                    case "status":
                        if (dto.NovoValor == "a")
                        {
                            usuario.STATUS_USUARIO = "a";
                        }
                        else if (dto.NovoValor == "d")
                        {
                            usuario.STATUS_USUARIO = "d";
                        }
                        break;
                    case "statusAssinatura":
                        if (dto.NovoValor == "2") usuario.ID_ASSINATURA_FK = 2;

                        else if (dto.NovoValor == "1") usuario.ID_ASSINATURA_FK = 1;

                        else
                        {
                            return BadRequest("Novo Valor para Status Assinatura deve ser : '1' para sem assinatura; '2' para assinante;");
                        }
                        break;

                    default:
                        return BadRequest("Campo inválido. É permitido os seguintes campos : 'nome', 'email', 'cpf', 'cnpj', 'senha', 'data', 'status', 'statusAssinatura'");
                }

                _context.Usuarios.Update(usuario);
                await _context.SaveChangesAsync();
                return Ok(new { mensagem = "Campo Atualizado com sucesso.", usuario });
            }

            // Atualizar Imagem
            // Arrumar aqui mais tarde
            [HttpPatch("{id}/imagem")]
            public async Task<IActionResult> AtualizarImagem(int id, [FromBody] IFormFile imagem)
            {
                var usuario = await _context.Usuarios.FindAsync(id);
                if (usuario == null) return NotFound("Usuario não encontrado.");

                using (var ms = new MemoryStream())
                {
                    await imagem.CopyToAsync(ms);
                    usuario.FOTO_USUARIO = ms.ToArray();
                }

                _context.Usuarios.Update(usuario);
                await _context.SaveChangesAsync();
                return Ok(new { mensagem = "Imagem Atualizada com sucesso.", usuario });
            }

            // DELETEs
            // Deletar Usuario
            [HttpDelete("{id}")]
            public async Task<IActionResult> DeletarUsuario(int id) 
            {
                var usuario = await _context.Usuarios.FindAsync(id);
                if (usuario == null) return NotFound("Usuario não encontrado."); // Tratamento de erro

                _context.Usuarios.Remove(usuario);
                await _context.SaveChangesAsync();
                return NoContent();
            }
        }
}
