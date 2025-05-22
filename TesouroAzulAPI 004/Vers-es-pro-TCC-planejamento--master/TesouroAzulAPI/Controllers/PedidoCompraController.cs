using Microsoft.AspNetCore.Mvc;
using TesouroAzulAPI.Data;

namespace TesouroAzulAPI.Controllers
{
    [Route ("api/PeidoCompra"), ApiController]
    public class PedidoCompraController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        // Instandiando o context da ApplicationDbContext para _context
        public PedidoCompraController(ApplicationDbContext context) { _context = context; }

        // Para este controller é necessário criar o models de EstoqueProduto, seguir com lógica aqui mais tarde
    }
}
