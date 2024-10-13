using Microsoft.AspNetCore.Http;
using Microsoft.SqlServer.Server;
using System.ComponentModel.DataAnnotations;
using System.Data.SqlClient;

public class ProducaoProdutos{
    [Key]
    public int Id{ get; set; }
    public int Pdc_id { get; set; } 
    public int Pdt_id { get; set; }
    public decimal Quant { get; set; }
}