package br.com.cotiinformatica.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import br.com.cotiinformatica.entities.Cliente;
import br.com.cotiinformatica.repositories.interfaces.IClienteRepository;

public class ClienteRepository implements IClienteRepository {

	// atributo
	private JdbcTemplate jdbcTemplate;

	// construtor (dependencia)
	public ClienteRepository(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void create(Cliente obj) throws Exception {

		String query = "insert into cliente values(null, ?, ?)";
		Object[] params = { obj.getNome(), obj.getEmail() };

		jdbcTemplate.update(query, params);
	}

	@Override
	public void update(Cliente obj) throws Exception {

		String query = "update cliente set nome = ?, email = ? where idcliente = ?";
		Object[] params = { obj.getNome(), obj.getEmail(), obj.getIdCliente() };

		jdbcTemplate.update(query, params);
	}

	@Override
	public void delete(Integer id) throws Exception {

		String query = "delete from cliente where idcliente = ?";
		Object[] params = { id };

		jdbcTemplate.update(query, params);
	}

	@Override
	public List<Cliente> findAll() throws Exception {

		String query = "select * from cliente order by nome";

		List<Cliente> lista = jdbcTemplate.query(query, new RowMapper<Cliente>() {

			@Override
			public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
				return getCliente(rs);
			}
		});

		return lista;
	}

	@Override
	public Cliente findById(Integer id) throws Exception {

		String query = "select * from cliente where idcliente = ?";
		Object[] params = { id };

		List<Cliente> result = jdbcTemplate.query(query, params, new RowMapper<Cliente>() {

			@Override
			public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
				return getCliente(rs);
			}
		});
		
		if(result != null && result.size() > 0)
			return result.get(0);
		
		return null;
	}
	
	@Override
	public Cliente findByEmail(String email) throws Exception {

		String query = "select * from cliente where email = ?";
		Object[] params = { email };

		List<Cliente> result = jdbcTemplate.query(query, params, new RowMapper<Cliente>() {

			@Override
			public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
				return getCliente(rs);
			}
		});
		
		if(result != null && result.size() > 0)
			return result.get(0);
		
		return null;
	}

	private Cliente getCliente(ResultSet rs) throws SQLException {

		Cliente cliente = new Cliente();
		cliente.setIdCliente(rs.getInt("idcliente"));
		cliente.setNome(rs.getString("nome"));
		cliente.setEmail(rs.getString("email"));

		return cliente;
	}	
}