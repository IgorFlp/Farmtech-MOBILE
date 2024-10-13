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
        public DbSet<Fornecedor> Fornecedores { get; set; }
        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<Produto> Produtos { get; set; }
        public DbSet<Estoque> Estoques { get; set; }
        public DbSet<Producao> Producoes { get; set; }
        public DbSet<ProducaoProdutos> ProducaoProdutos { get; set; }
        public DbSet<ClienteEndereco> ClientesEnderecos { get; set; }
        public DbSet<FornecedorEndereco> FornecedoresEnderecos { get; set; }
        public DbSet<Cupom> Cupons { get; set; }
        //public DbSet<ClienteEndereco> ClientesEnderecos { get; set; }
        // Adicione outras DbSets aqui
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Configurar o nome da tabela para a classe Cliente
            modelBuilder.Entity<Cliente>().ToTable("Tb_cliente");
            modelBuilder.Entity<ClienteEndereco>().ToTable("Tb_cl_endereco");
            modelBuilder.Entity<Fornecedor>().ToTable("Tb_fornecedor");
            modelBuilder.Entity<FornecedorEndereco>().ToTable("Tb_frn_Endereco");
            modelBuilder.Entity<Usuario>().ToTable("Tb_usuario");
            modelBuilder.Entity<Produto>().ToTable("Tb_produto");
            modelBuilder.Entity<Estoque>().ToTable("Tb_estoque");
            modelBuilder.Entity<Estoque>()
                .Property(e => e.Pdt_id)
                .ValueGeneratedNever();

            modelBuilder.Entity<Producao>().ToTable("Tb_producao");  
            modelBuilder.Entity<ProducaoProdutos>().ToTable("Tb_producao_produtos");   
            modelBuilder.Entity<Cupom>().ToTable("Tb_cupom");      

        }
    }
}