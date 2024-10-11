using Microsoft.AspNetCore.Http;
using Microsoft.SqlServer.Server;
using System.ComponentModel.DataAnnotations;
using System.Data.SqlClient;

public class Produto
{
    // Construir classe com base no banco
    [Key]
    public int Id { get; set; }    
    public string Nome { get; set; }  
    public decimal PrecoUn { get; set; }
    public string unMedida { get; set; }     
       
}