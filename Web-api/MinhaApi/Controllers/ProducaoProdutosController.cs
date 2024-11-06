using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class ProducaoProdutosController : ControllerBase
{
	private readonly ApplicationDbContext _context;
	public ProducaoProdutosController(ApplicationDbContext context)
	{
		_context = context;
	} 
	[HttpGet]
	public async Task<ActionResult<IEnumerable<ProducaoProdutos>>> GetProducoesProdutos()
	{
		return await _context.ProducaoProdutos.ToListAsync();
    }
	// POST: api/ProducaoProdutos
	[HttpPost]
	public async Task<ActionResult<ProducaoProdutos>> PostProducaoProdutos([FromBody] ProducaoProdutos producaoProdutos)
	{
		try{           
		if (!ModelState.IsValid)
		{
			return BadRequest(ModelState); // Retornar erro de validação
		}
        _context.ProducaoProdutos.Add(producaoProdutos);
        await _context.SaveChangesAsync();     	

		return Ok(producaoProdutos);
		}catch(Exception e){
			Console.WriteLine("Erro: "+e.Message);
			return BadRequest(ModelState);
		}
		
	}
    
}
