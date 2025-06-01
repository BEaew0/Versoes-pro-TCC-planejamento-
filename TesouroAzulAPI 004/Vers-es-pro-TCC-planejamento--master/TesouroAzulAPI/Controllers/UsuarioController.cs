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
                FOTO_USUARIO = Convert.FromBase64String(usuarioDto.FOTO_USUARIO),
                SENHA_USUARIO = usuarioDto.SENHA_USUARIO,
                STATUS_USUARIO = usuarioDto.STATUS_USUARIO

            };

            _context.Usuarios.Add(usuario);
            await _context.SaveChangesAsync();
            return CreatedAtAction(nameof(BuscarUsuarioPorId), new { id = usuario.ID_USUARIO }, usuario);
        }

        // Realizar login 
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginDto dto)
        {
            // tratamentos de erros

            if (string.IsNullOrEmpty(dto.EMAIL_USUARIO) || string.IsNullOrEmpty(dto.SENHA_USUARIO)) { return BadRequest("Email e Senha são obrigatórios."); }
            var usuario = await _context.Usuarios
                .FirstOrDefaultAsync(u => u.EMAIL_USUARIO == dto.EMAIL_USUARIO && u.SENHA_USUARIO == dto.SENHA_USUARIO);

            if (usuario == null) { return Unauthorized("Email ou senhas invalidos"); }

            // Verifica se o usuario está ativo

            if (usuario.STATUS_USUARIO != "a") { return BadRequest("Usuario inativo. Entre em contato com o suporte."); }

            // Retorna Usuario

            return Ok(new
            {
                mensagem = "Login realizado com sucesso.",
                usuario = new
                {
                    usuario.ID_USUARIO,
                    usuario.NOME_USUARIO,
                    usuario.EMAIL_USUARIO,
                    usuario.DATA_NASC_USUARIO,
                    usuario.CPF_USUARIO,
                    usuario.CNPJ_USUARIO,
                    usuario.ID_ASSINATURA_FK,
                    usuario.STATUS_USUARIO
                }
            });
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

        // Buscar Imagem por ID
        [HttpGet("Buscar-Imagem")]
        public async Task<ActionResult<Usuario>> BuscarUsuarioFoto(int id)
        {
            return default;
        }

        // PATCHs
        // Atualizar Usuario
        [HttpPatch("{id}/alterar-campo")]
        public async Task<IActionResult> AlterarCamposUsuario(int id, [FromBody] AtualizarCampoUsuarioDto dto)
        {
            // Controller Dinâmico, ou seja, utiliza a classe AtualizarCampoUsuarioDto para qualquer campo e informação que deseja alterar

            // Tratamentos de erro de {id}
            // Criar uma função privada de mensagem de erro mais tarde aqui para ecomonizar codigo
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
        [HttpPatch("{id}/imagem")]
        public async Task<IActionResult> AtualizarImagem(int id, [FromBody] ImagemDto dto)
        {
            // Busca pelo id
            var usuario = await _context.Usuarios.FindAsync(id);
            if (usuario == null) return NotFound("Usuario não encontrado.");

            try
            {
                byte[] imagemBytes = Convert.FromBase64String(dto.ImagemBase64);

                // Tratamento de erro quando receber o JSON
                dto.ImagemBase64 = dto.ImagemBase64.Replace("data:image/png;base64,", string.Empty);
                dto.ImagemBase64 = dto.ImagemBase64.Replace("\r", "").Replace("\n", "");
                // Tratamento para tamnhos de imagem
                if (imagemBytes.Length > 16777215) return BadRequest("Imagem muito grande. O tamanho máximo é de 16MB.");

                usuario.FOTO_USUARIO = imagemBytes;

                _context.Usuarios.Update(usuario);
                await _context.SaveChangesAsync();
                return Ok(new { mensagem = "Imagem Atualizada com sucesso.", usuario });
        }
            catch (FormatException) 
            {
                return BadRequest("Formatação de imagem64 incorreta");
            }


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
