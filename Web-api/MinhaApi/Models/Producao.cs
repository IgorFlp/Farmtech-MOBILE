using Microsoft.AspNetCore.Http;
using Microsoft.SqlServer.Server;
using System.ComponentModel.DataAnnotations;
using System.Data.SqlClient;

public class Producao
{
    // Construir classe com base no banco
    [Key]
    public int Id { get; set; }    
    public DateOnly dataProd { get; set; }  
       
}