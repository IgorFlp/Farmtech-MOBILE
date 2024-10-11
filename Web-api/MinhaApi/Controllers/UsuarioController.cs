using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MinhaApi.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class UsuarioController : ControllerBase
{
	private readonly ApplicationDbContext _context;

	public UsuarioController(ApplicationDbContext context)
	{
		_context = context;
	}

	// GET: api/Usuarios
	[HttpGet]
	public async Task<ActionResult<IEnumerable<Usuario>>> GetUsuarios()
	{
		return await _context.Usuarios.ToListAsync();
    }
	// GET: api/Usuarios/5
	[HttpGet("{id:int}")]
	public async Task<ActionResult<Usuario>> GetUsuario(int id)
	{
		var usuario = await _context.Usuarios.FindAsync(id);

		if (usuario == null)
		{
			return NotFound();
		}

		return usuario;
	}

    // GET: api/Usuarios/logar?login=example&senha=example
    [HttpGet("{logar}")]
    public async Task<ActionResult<Usuario>> GetLogin([FromQuery] string login, [FromQuery] string senha)
	{
		var usuario = await _context.Usuarios.FirstOrDefaultAsync(u => u.Login == login);

		if (usuario == null){
			return NotFound("Usuario não encontrado");
		}else if(usuario.Senha != senha){
            return NotFound("Senha incorreta");            
        }else{
            return Ok(usuario);
        }		
	}

	// POST: api/Usuario
	[HttpPost]
	public async Task<ActionResult<Usuario>> PostUsuario(Usuario usuario)
	{
		_context.Usuarios.Add(usuario);
		await _context.SaveChangesAsync();

		return Ok(usuario);
		//return CreatedAtAction(nameof(GetUsuarios), new { login = usuario.Login }, usuario);
	}

	// PUT: api/Usuario/5
	[HttpPut("{id}")]
	public async Task<IActionResult> PutUsuario(int id, Usuario usuario)                
	{
		if (id != usuario.Id)
		{
			return BadRequest();
		}

		_context.Entry(usuario).State = EntityState.Modified;

		try
		{
			await _context.SaveChangesAsync();
		}
		catch (DbUpdateConcurrencyException)
		{
			if (!UsuarioExists(id))
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

	// DELETE: api/Usuario/5
	[HttpDelete("{id}")]
	public async Task<IActionResult> DeleteUsuario(int id)
	{
		var usuario = await _context.Usuarios.FindAsync(id);
		if (usuario == null)
		{
			return NotFound();
		}

		_context.Usuarios.Remove(usuario);
		await _context.SaveChangesAsync();

		return NoContent();
	}

	private bool UsuarioExists(int id)
	{
		return _context.Usuarios.Any(e => e.Id == id);
	}
	}
