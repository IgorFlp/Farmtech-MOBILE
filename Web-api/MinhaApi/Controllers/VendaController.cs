using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class VendaController : ControllerBase
{
	private readonly ApplicationDbContext _context;

	public VendaController(ApplicationDbContext context)
	{
		_context = context;
	}

	// GET: api/Vendas
	[HttpGet]
	public async Task<ActionResult<IEnumerable<Venda>>> GetVendas()
	{
		return await _context.Vendas.ToListAsync();
    }
	// GET: api/Vendas/5
	[HttpGet("{id:int}")]
	public async Task<ActionResult<Venda>> GetVenda(int id)
	{
		var venda = await _context.Vendas.FindAsync(id);

		if (venda == null)
		{
			return NotFound();
		}

		return venda;
	}   

	// POST: api/Venda
	[HttpPost]
	public async Task<ActionResult<Venda>> PostVenda([FromBody] Venda venda)
	{
		try{
		if (!ModelState.IsValid)
		{
			return BadRequest(ModelState); // Retornar erro de validação
		}
		Console.WriteLine("Teste:"+ venda.DtVenda);			
		//venda.DataProd = dataProd;

		_context.Vendas.Add(venda);
		await _context.SaveChangesAsync();

		return Ok(venda);
		}catch(Exception e){
			Console.WriteLine("Erro: "+e.Message);
			return BadRequest(ModelState);
		}
		//return CreatedAtAction(nameof(GetVendas), new { login = venda.Login }, venda);
	}

	// PUT: api/Venda/5
	[HttpPut("{id}")]
	public async Task<IActionResult> PutVenda(int id, Venda venda)                
	{
		if (id != venda.Id)
		{
			return BadRequest();
		}

		_context.Entry(venda).State = EntityState.Modified;

		try
		{
			await _context.SaveChangesAsync();
		}
		catch (DbUpdateConcurrencyException)
		{
			if (!VendaExists(id))
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

	// DELETE: api/Venda/5
	[HttpDelete("{id}")]
	public async Task<IActionResult> DeleteVenda(int id)
	{
		var venda = await _context.Vendas.FindAsync(id);
		if (venda == null)
		{
			return NotFound();
		}

		_context.Vendas.Remove(venda);
		await _context.SaveChangesAsync();

		return NoContent();
	}

	private bool VendaExists(int id)
	{
		return _context.Vendas.Any(e => e.Id == id);
	}
	}
