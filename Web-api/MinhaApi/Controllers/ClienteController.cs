using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class ClientesController : ControllerBase
{
	private readonly ApplicationDbContext _context;

	public ClientesController(ApplicationDbContext context)
	{
		_context = context;
	}

	// GET: api/Clientes
	[HttpGet]
	public async Task<ActionResult<IEnumerable<Cliente>>> GetClientes()
	{
		return await _context.Clientes.ToListAsync();
	}

	// GET: api/Clientes/5
	[HttpGet("{cpf}")]
	public async Task<ActionResult<Cliente>> GetCliente(String cpf)
	{
		var cliente = await _context.Clientes.FindAsync(cpf);

		if (cliente == null)
		{
			return NotFound();
		}

		return cliente;
	}

	// POST: api/Clientes
	[HttpPost]
	public async Task<ActionResult<Cliente>> PostCliente(Cliente cliente)
	{
		_context.Clientes.Add(cliente);
		await _context.SaveChangesAsync();

		return CreatedAtAction(nameof(GetCliente), new { cpf = cliente.Cpf }, cliente);
	}

	// PUT: api/Clientes/5
	[HttpPut("{cpf}")]
	public async Task<IActionResult> PutCliente(string cpf, Cliente cliente)
	{
		if (cpf != cliente.Cpf)
		{
			return BadRequest();
		}

		_context.Entry(cliente).State = EntityState.Modified;

		try
		{
			await _context.SaveChangesAsync();
		}
		catch (DbUpdateConcurrencyException)
		{
			if (!ClienteExists(cpf))
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

	// DELETE: api/Clientes/5
	[HttpDelete("{cpf}")]
	public async Task<IActionResult> DeleteCliente(string cpf)
	{
		var cliente = await _context.Clientes.FindAsync(cpf);
		if (cliente == null)
		{
			return NotFound();
		}

		_context.Clientes.Remove(cliente);
		await _context.SaveChangesAsync();

		return NoContent();
	}

	private bool ClienteExists(string cpf)
	{
		return _context.Clientes.Any(e => e.Cpf == cpf);
	}
	}

