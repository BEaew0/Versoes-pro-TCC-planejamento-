using Microsoft.EntityFrameworkCore;
using TesouroAzulAPI.Models;

namespace TesouroAzulAPI.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options) { }


        public DbSet<Produto> Produtods { get; set; }

        public DbSet<Usuario> Usuarios { get; set; }

        public DbSet<Fornecedor> Fornecedores { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            // Aqui pode realizar configurações adicionais, como definir chaves primárias compostas, etc.
            // Somente em casos de criar o DB atravez da propia API
        }
    }
}
