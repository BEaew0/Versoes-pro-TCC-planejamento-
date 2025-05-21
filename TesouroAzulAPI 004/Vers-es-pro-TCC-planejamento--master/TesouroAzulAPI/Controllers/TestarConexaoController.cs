using Microsoft.AspNetCore.Mvc;

namespace TesouroAzulAPI.Controllers
{
    public class TestarConexaoController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
