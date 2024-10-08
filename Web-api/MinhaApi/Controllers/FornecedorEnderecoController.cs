using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class FornecedoresEnderecosController : ControllerBase
{
    private readonly ApplicationDbContext _context;

    public FornecedoresEnderecosController(ApplicationDbContext context)
    {
        _context = context;
    }

    // GET: api/Fornecedores
	[HttpGet]
	public async Task<ActionResult<IEnumerable<FornecedorEndereco>>> GetFornecedoresEnderecos()
	{
		return await _context.FornecedoresEnderecos.ToListAsync();
	}

    [HttpGet("{cnpj}")]
    public async Task<ActionResult<FornecedorEndereco>> GetFornecedorEndereco(string cnpj)
    {
        var endereco = await _context.FornecedoresEnderecos.FindAsync(cnpj);

        if (endereco == null)
        {
            return NotFound();
        }

        return endereco;
    }

    [HttpPost]
    public async Task<ActionResult<FornecedorEndereco>> PostEndereco(FornecedorEndereco fornecedorEndereco)
    {
        _context.FornecedoresEnderecos.Add(fornecedorEndereco);
        await _context.SaveChangesAsync();
        return CreatedAtAction(nameof(GetFornecedoresEnderecos), new { cnpj = fornecedorEndereco.Frn_cnpj }, fornecedorEndereco);
    }

    [HttpPut("{cnpj}")]
    public async Task<IActionResult> PutEndereco(string cnpj, FornecedorEndereco fornecedorEndereco)
    {
        if (cnpj != fornecedorEndereco.Frn_cnpj)
        {
            return BadRequest();
        }

        _context.Entry(fornecedorEndereco).State = EntityState.Modified;
        await _context.SaveChangesAsync();

        return NoContent();
    }
}