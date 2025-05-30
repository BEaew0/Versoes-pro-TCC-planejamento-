﻿using Microsoft.AspNetCore.Mvc;
using TesouroAzulAPI.Data;

namespace TesouroAzulAPI.Controllers
{
    // Classe apenas para testar a conexão com o banco de dados e api
    [Route("api/TestarConexao"), ApiController]
    public class TestarConexaoController : ControllerBase
    {
        // Instandiando o context da ApplicationDbContext para _context
        private readonly ApplicationDbContext _context;

        public TestarConexaoController(ApplicationDbContext context) { _context = context; }

        // GETS
        // Testar Conexão
        [HttpGet]
        public async Task<IActionResult> TestarConexao()
        {
            // Verifica se a conexão com o banco de dados está funcionando
            try
            {
                await _context.Database.CanConnectAsync();
                return Ok(new { mensagem = "Conexão com o banco de dados estabelecida com sucesso." });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { mensagem = "Erro ao conectar ao banco de dados.", erro = ex.Message });
            }
        }
    }
}
