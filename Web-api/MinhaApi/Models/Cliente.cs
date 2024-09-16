using Microsoft.AspNetCore.Http;
using Microsoft.SqlServer.Server;
using System.ComponentModel.DataAnnotations;
using System.Data.SqlClient;

public class Cliente
{
    // Construir classe com base no banco
    [Key]
    public string Cpf { get; set; }
    
    public string Nome { get; set; }
    public string Telefone { get; set; }
    public string Email { get; set; }
    public DateOnly DataNasc { get; set; }
    public char Genero { get; set; }    
}
public class ClienteEndereco
{
    // Construir classe com base no banco
    [Key]
    public string Cl_cpf { get; set; }

    public string Rua { get; set; }
    public string Bairro { get; set; }
    public string Cidade { get; set; }
    public string Estado { get; set; }
    public string Cep { get; set; }

}
