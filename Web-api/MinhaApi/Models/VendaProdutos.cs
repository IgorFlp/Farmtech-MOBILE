using Microsoft.AspNetCore.Http;
using Microsoft.SqlServer.Server;
using System.ComponentModel.DataAnnotations;
using System.Data.SqlClient;

public class VendaProdutos
{
    // Construir classe com base no banco
    [Key]
    public int Id { get; set; }    
    public int Pdt_id{ get; set; }
    public int Ven_id{ get; set; }
    public decimal Quant { get; set; }
    
        
}