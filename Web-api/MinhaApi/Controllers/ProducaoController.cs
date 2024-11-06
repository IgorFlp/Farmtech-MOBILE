using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class ProducaoController : ControllerBase
{
	private readonly ApplicationDbContext _context;

	public ProducaoController(ApplicationDbContext context)
	{
		_context = context;
	}

	// GET: api/Producoes
	[HttpGet]
	public async Task<ActionResult<IEnumerable<Producao>>> GetProducoes()
	{
		return await _context.Producoes.ToListAsync();
    }
	// GET: api/Producoes/5
	[HttpGet("{id:int}")]
	public async Task<ActionResult<Producao>> GetProducao(int id)
	{
		var producao = await _context.Producoes.FindAsync(id);

		if (producao == null)
		{
			return NotFound();
		}

		return producao;
	}   

	// POST: api/Producao
	[HttpPost]
	public async Task<ActionResult<Producao>> PostProducao([FromBody] Producao producao)
	{
		try{
		if (!ModelState.IsValid)
		{
			return BadRequest(ModelState); // Retornar erro de validação
		}
		Console.WriteLine("Teste:"+ producao.DataProd);			
		//producao.DataProd = dataProd;

		_context.Producoes.Add(producao);
		await _context.SaveChangesAsync();

		return Ok(producao);
		}catch(Exception e){
			Console.WriteLine("Erro: "+e.Message);
			return BadRequest(ModelState);
		}
		//return CreatedAtAction(nameof(GetProducoes), new { login = producao.Login }, producao);
	}

	// PUT: api/Producao/5
	[HttpPut("{id}")]
	public async Task<IActionResult> PutProducao(int id, Producao producao)                
	{
		if (id != producao.Id)
		{
			return BadRequest();
		}

		_context.Entry(producao).State = EntityState.Modified;

		try
		{
			await _context.SaveChangesAsync();
		}
		catch (DbUpdateConcurrencyException)
		{
			if (!ProducaoExists(id))
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

	// DELETE: api/Producao/5
	[HttpDelete("{id}")]
	public async Task<IActionResult> DeleteProducao(int id)
	{
		var producao = await _context.Producoes.FindAsync(id);
		if (producao == null)
		{
			return NotFound();
		}

		_context.Producoes.Remove(producao);
		await _context.SaveChangesAsync();

		return NoContent();
	}

	private bool ProducaoExists(int id)
	{
		return _context.Producoes.Any(e => e.Id == id);
	}
	}
