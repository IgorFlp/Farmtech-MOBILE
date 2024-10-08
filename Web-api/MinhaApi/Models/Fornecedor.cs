using Microsoft.AspNetCore.Http;
using Microsoft.SqlServer.Server;
using System.ComponentModel.DataAnnotations;
using System.Data.SqlClient;

public class Fornecedor
{
    // Construir classe com base no banco
    [Key]
    public string Cnpj { get; set; }
    
    public string NomeFantasia { get; set; }
    public string RazaoSocial { get; set; }
    public string Telefone { get; set; }
    public string Email { get; set; }
       
}
public class FornecedorEndereco
{
    // Construir classe com base no banco
    [Key]
    public string Frn_cnpj { get; set; }

    public string Rua { get; set; }
    public string Bairro { get; set; }
    public string Cidade { get; set; }
    public string Estado { get; set; }
    public string Cep { get; set; }

}