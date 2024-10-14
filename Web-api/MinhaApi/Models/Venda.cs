using Microsoft.AspNetCore.Http;
using Microsoft.SqlServer.Server;
using System.ComponentModel.DataAnnotations;
using System.Data.SqlClient;

public class Venda
{
    // Construir classe com base no banco
    [Key]
    public int Id { get; set; }    
    public int Usr_id{ get; set; }

    public decimal Subtotal { get; set; }
    public decimal Frete { get; set; }
    public decimal Desconto { get; set; }
    public decimal Total { get; set; }

    public string Cupom { get; set; }
    public string MtdPagto{ get; set; }
    public string Entrega{ get; set; }
    public string Cl_cpf{ get; set; }

    public DateOnly DtVenda{ get; set; }

        
}