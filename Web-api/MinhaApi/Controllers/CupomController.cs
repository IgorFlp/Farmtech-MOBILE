using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class CupomController : ControllerBase
{
	private readonly ApplicationDbContext _context;

	public CupomController(ApplicationDbContext context)
	{
		_context = context;
	}

	// GET: api/Cupons
	[HttpGet]
	public async Task<ActionResult<IEnumerable<Cupom>>> GetCupons()
	{
		return await _context.Cupons.ToListAsync();
	}

	// GET: api/Cupons/5
	[HttpGet("{nome}")]
	public async Task<ActionResult<Cupom>> GetCupom(String nome)
	{
		var cupom = await _context.Cupons.FindAsync(nome);

		if (cupom == null)
		{
			return NotFound();
		}
		return cupom;
	}
}