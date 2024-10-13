using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class EstoqueController : ControllerBase
{
	private readonly ApplicationDbContext _context;

	public EstoqueController(ApplicationDbContext context)
	{
		_context = context;
	}

	// GET: api/Estoques
	[HttpGet]
	public async Task<ActionResult<IEnumerable<Estoque>>> GetEstoques()
	{
		return await _context.Estoques.ToListAsync();
    }

	// GET: api/Estoques/5
	[HttpGet("{pdt_id:int}")]
	public async Task<ActionResult<Estoque>> GetEstoque(int pdt_id)
	{
		var estoque = await _context.Estoques.FindAsync(pdt_id);

		if (estoque == null)
		{
			return NotFound();
		}

		return estoque;
	}
    
	// POST: api/Estoque
	[HttpPost]
	public async Task<ActionResult<Estoque>> PostEstoque(Estoque estoque)
	{
		_context.Estoques.Add(estoque);
		await _context.SaveChangesAsync();
		return estoque;
		//return CreatedAtAction(nameof(GetEstoques), new { pdt_id = estoque.Pdt_id}, estoque);
	}

	// PUT: api/Estoque/5
	[HttpPut("{pdt_id}")]
	public async Task<IActionResult> PutEstoque(int pdt_id, Estoque estoque)                
	{
		if (pdt_id != estoque.Pdt_id)
		{
			return BadRequest();
		}

		_context.Entry(estoque).State = EntityState.Modified;

		try
		{
			await _context.SaveChangesAsync();
		}
		catch (DbUpdateConcurrencyException)
		{
			if (!EstoqueExists(pdt_id))
			{
				return NotFound();
			}
			else
			{
				throw;
			}
		}

		return NoContent();
	}
	// PUT: api/Estoque/Adicionar/5
	[HttpPut("Adicionar/{pdt_id}")]
	public async Task<IActionResult> AdicionarEstoque(int pdt_id, decimal quantidade)                
	{	
		
		Estoque estoqueAtual = await _context.Estoques.FindAsync(pdt_id);
		Estoque estoqueNovo = estoqueAtual;
		Console.WriteLine("Estoque Atual: "+estoqueAtual.Quant);
		
		estoqueNovo.Quant = estoqueAtual.Quant + quantidade;
		Console.WriteLine("Estoque input: "+quantidade);
				
		Console.WriteLine("Estoque Novo: "+estoqueNovo.Quant);
		
		_context.Entry(estoqueNovo).State = EntityState.Modified;
		try
		{
			await _context.SaveChangesAsync();
		}
		catch (DbUpdateConcurrencyException)
		{
			if (!EstoqueExists(pdt_id))
			{
				return NotFound();
			}
			else
			{
				throw;
			}
		}

		return NoContent();
	}
	// PUT: api/Estoque/Adicionar/5
	[HttpPut("Subtrair/{pdt_id}")]
	public async Task<IActionResult> SubtrairEstoque(int pdt_id, Estoque estoque)                
	{
		if (pdt_id != estoque.Pdt_id)
		{
			return BadRequest();
		}

		Estoque estoqueAtual = await _context.Estoques.FindAsync(pdt_id);

		Estoque estoqueNovo = estoqueAtual;
		estoqueNovo.Quant = estoqueAtual.Quant - estoque.Quant;
		
		_context.Entry(estoqueNovo).State = EntityState.Modified;
		try
		{
			await _context.SaveChangesAsync();
		}
		catch (DbUpdateConcurrencyException)
		{
			if (!EstoqueExists(pdt_id))
			{
				return NotFound();
			}
			else
			{
				throw;
			}
		}

		return NoContent();
	}


	// DELETE: api/Estoque/5
	[HttpDelete("{pdt_id}")]
	public async Task<IActionResult> DeleteEstoque(int pdt_id)
	{
		var estoque = await _context.Estoques.FindAsync(pdt_id);
		if (estoque == null)
		{
			return NotFound();
		}

		_context.Estoques.Remove(estoque);
		await _context.SaveChangesAsync();

		return NoContent();
	}

	private bool EstoqueExists(int pdt_id)
	{
		return _context.Estoques.Any(e => e.Pdt_id == pdt_id);
	}
	}
