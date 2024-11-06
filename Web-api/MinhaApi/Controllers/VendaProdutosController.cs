using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class VendaProdutosController : ControllerBase
{
	private readonly ApplicationDbContext _context;
	public VendaProdutosController(ApplicationDbContext context)
	{
		_context = context;
	} 
	// GET: api/Vendas
	[HttpGet]
	public async Task<ActionResult<IEnumerable<VendaProdutos>>> GetVendaProdutos()
	{
		return await _context.VendaProdutos.ToListAsync();
    }
	// POST: api/VendaProdutos
	[HttpPost]
	public async Task<ActionResult<VendaProdutos>> PostVendaProdutos([FromBody] VendaProdutos vendaProdutos)
	{
		try{           
		if (!ModelState.IsValid)
		{
			return BadRequest(ModelState); // Retornar erro de validação
		}
        _context.VendaProdutos.Add(vendaProdutos);
        await _context.SaveChangesAsync();     	

		return Ok(vendaProdutos);
		}catch(Exception e){
			Console.WriteLine("Erro: "+e.Message);
			return BadRequest(ModelState);
		}
		
	}
    
}