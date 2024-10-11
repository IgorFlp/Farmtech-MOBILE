using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class ProdutoController : ControllerBase
{
	private readonly ApplicationDbContext _context;

	public ProdutoController(ApplicationDbContext context)
	{
		_context = context;
	}

	// GET: api/Produtos
	[HttpGet]
	public async Task<ActionResult<IEnumerable<Produto>>> GetProdutos()
	{
		return await _context.Produtos.ToListAsync();
    }

	// GET: api/Produtos/5
	[HttpGet("{id:int}")]
	public async Task<ActionResult<Produto>> GetProduto(int id)
	{
		var produto = await _context.Produtos.FindAsync(id);

		if (produto == null)
		{
			return NotFound();
		}

		return produto;
	}
    
	// POST: api/Produto
	[HttpPost]
	public async Task<ActionResult<Produto>> PostProduto(Produto produto)
	{
		using (var transaction = await _context.Database.BeginTransactionAsync())
		{
			try
			{
				_context.Produtos.Add(produto);
				await _context.SaveChangesAsync();
				Console.WriteLine("Produto id", produto.Id);

				var produtoExistente = await _context.Produtos.FindAsync(produto.Id);
				Console.WriteLine("Produto existente", produtoExistente);
				if (produtoExistente != null)
				{
					var estoque = new Estoque { Pdt_id = produtoExistente.Id, Quant = 100 };
					_context.Estoques.Add(estoque);
					await _context.SaveChangesAsync();
				}

				await transaction.CommitAsync();
				return produto;
			}
			catch (Exception)
			{
				await transaction.RollbackAsync();
				throw;
			}
		}
	}

	// PUT: api/Produto/5
	[HttpPut("{id}")]
	public async Task<IActionResult> PutProduto(int id, Produto produto)                
	{
		if (id != produto.Id)
		{
			return BadRequest();
		}

		_context.Entry(produto).State = EntityState.Modified;

		try
		{
			await _context.SaveChangesAsync();
		}
		catch (DbUpdateConcurrencyException)
		{
			if (!ProdutoExists(id))
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

	// DELETE: api/Produto/5
	[HttpDelete("{id}")]
	public async Task<IActionResult> DeleteProduto(int id)
	{
		var produto = await _context.Produtos.FindAsync(id);
		if (produto == null)
		{
			return NotFound();
		}

		_context.Produtos.Remove(produto);
		await _context.SaveChangesAsync();

		return NoContent();
	}

	private bool ProdutoExists(int id)
	{
		return _context.Produtos.Any(e => e.Id == id);
	}
	}
