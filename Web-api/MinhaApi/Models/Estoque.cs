using Microsoft.AspNetCore.Http;
using Microsoft.SqlServer.Server;
using System.ComponentModel.DataAnnotations;
using System.Data.SqlClient;

public class Estoque
{
    // Construir classe com base no banco
    [Key]
    public int Pdt_id { get; set; }  
      
    public decimal Quant { get; set; }        
       
}