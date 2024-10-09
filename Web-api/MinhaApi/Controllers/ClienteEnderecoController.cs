using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class ClientesEnderecosController : ControllerBase
{
    private readonly ApplicationDbContext _context;

    public ClientesEnderecosController(ApplicationDbContext context)
    {
        _context = context;
    }

    // GET: api/Clientes
	[HttpGet]
	public async Task<ActionResult<IEnumerable<ClienteEndereco>>> GetClientesEnderecos()
	{
		return await _context.ClientesEnderecos.ToListAsync();
	}

    [HttpGet("{cpf}")]
    public async Task<ActionResult<ClienteEndereco>> GetClienteEndereco(string cpf)
    {
        var endereco = await _context.ClientesEnderecos.FindAsync(cpf);

        if (endereco == null)
        {
            return NotFound();
        }

        return endereco;
    }

    [HttpPost]
    public async Task<ActionResult<ClienteEndereco>> PostEndereco(ClienteEndereco clienteEndereco)
    {
        _context.ClientesEnderecos.Add(clienteEndereco);
        await _context.SaveChangesAsync();
        return CreatedAtAction(nameof(GetClientesEnderecos), new { cpf = clienteEndereco.Cl_cpf }, clienteEndereco);
    }

    [HttpPut("{cl_cpf}")]
    public async Task<IActionResult> PutEndereco(string cl_cpf, ClienteEndereco clienteEndereco)
    {
        if (cl_cpf != clienteEndereco.Cl_cpf)
        {
            return BadRequest();
        }

        _context.Entry(clienteEndereco).State = EntityState.Modified;
        await _context.SaveChangesAsync();

        return NoContent();
    }
    [HttpDelete("{cl_cpf}")]
	public async Task<IActionResult> DeleteEndereco(string cl_cpf)
	{
		var endereco = await _context.ClientesEnderecos.FindAsync(cl_cpf);
		if (endereco == null)
		{
			return NotFound();
		}

		_context.ClientesEnderecos.Remove(endereco);
		await _context.SaveChangesAsync();

		return NoContent();
	}
}