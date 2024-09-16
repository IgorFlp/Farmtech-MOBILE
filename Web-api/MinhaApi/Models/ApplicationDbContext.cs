using Microsoft.EntityFrameworkCore;

namespace MinhaApi.Models
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }

        public DbSet<Cliente> Clientes { get; set; }
        public DbSet<ClienteEndereco> ClientesEnderecos { get; set; }
        //public DbSet<ClienteEndereco> ClientesEnderecos { get; set; }
        // Adicione outras DbSets aqui
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Configurar o nome da tabela para a classe Cliente
            modelBuilder.Entity<Cliente>()
                .ToTable("Tb_cliente"); // Nome da tabela no banco de dados
                modelBuilder.Entity<ClienteEndereco>().ToTable("Tb_cl_endereco");
        }
    }
}