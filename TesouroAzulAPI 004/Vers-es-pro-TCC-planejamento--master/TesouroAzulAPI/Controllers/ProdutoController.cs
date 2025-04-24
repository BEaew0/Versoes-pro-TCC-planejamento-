using Microsoft.AspNetCore.Mvc;

namespace TesouroAzulAPI.Controllers
{
    public class ProdutoController : ControllerBase
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
