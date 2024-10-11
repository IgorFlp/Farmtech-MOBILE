using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class FornecedoresController : ControllerBase
{
	private readonly ApplicationDbContext _context;

	public FornecedoresController(ApplicationDbContext context)
	{
		_context = context;
	}

	// GET: api/Fornecedores
	[HttpGet]
	public async Task<ActionResult<IEnumerable<Fornecedor>>> GetFornecedores()
	{
		return await _context.Fornecedores.ToListAsync();
	}

	// GET: api/Fornecedores/5
	[HttpGet("{cnpj}")]
	public async Task<ActionResult<Fornecedor>> GetFornecedor(String cnpj)
	{
		var fornecedor = await _context.Fornecedores.FindAsync(cnpj);

		if (fornecedor == null)
		{
			return NotFound();
		}

		return fornecedor;
	}

	// POST: api/Fornecedores
	[HttpPost]
	public async Task<ActionResult<Fornecedor>> PostFornecedor(Fornecedor fornecedor)
	{
		_context.Fornecedores.Add(fornecedor);
		await _context.SaveChangesAsync();

		return CreatedAtAction(nameof(GetFornecedor), new { cnpj = fornecedor.Cnpj }, fornecedor);
	}

	// PUT: api/Fornecedores/5
	[HttpPut("{cnpj}")]
	public async Task<IActionResult> PutFornecedor(string cnpj, Fornecedor fornecedor)
	{
		if (cnpj != fornecedor.Cnpj)
		{
			return BadRequest();
		}

		_context.Entry(fornecedor).State = EntityState.Modified;

		try
		{
			await _context.SaveChangesAsync();
		}
		catch (DbUpdateConcurrencyException)
		{
			if (!FornecedorExists(cnpj))
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

	// DELETE: api/Fornecedores/5
	[HttpDelete("{cnpj}")]
	public async Task<IActionResult> DeleteFornecedor(string cnpj)
	{
		var fornecedor = await _context.Fornecedores.FindAsync(cnpj);
		if (fornecedor == null)
		{
			return NotFound();
		}

		_context.Fornecedores.Remove(fornecedor);
		await _context.SaveChangesAsync();

		return NoContent();
	}

	private bool FornecedorExists(string cnpj)
	{
		return _context.Fornecedores.Any(e => e.Cnpj == cnpj);
	}
	}

