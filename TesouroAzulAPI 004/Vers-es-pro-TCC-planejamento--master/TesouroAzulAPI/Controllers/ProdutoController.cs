using Microsoft.AspNetCore.Mvc;

namespace TesouroAzulAPI.Controllers
{
    public class ProdutoController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
