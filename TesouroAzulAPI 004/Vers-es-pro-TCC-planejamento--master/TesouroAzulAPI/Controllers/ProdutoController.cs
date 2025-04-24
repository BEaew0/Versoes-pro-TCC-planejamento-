using Microsoft.AspNetCore.Mvc;
using TesouroAzulAPI.Data;

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



    }
}
