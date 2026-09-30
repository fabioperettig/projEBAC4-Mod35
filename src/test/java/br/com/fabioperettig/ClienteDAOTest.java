package br.com.fabioperettig;

import java.util.Collection;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.fabioperettig.dao.ClienteDAO;
import br.com.fabioperettig.dao.IClienteDAO;
import br.com.fabioperettig.domain.Cliente;
import br.com.fabioperettig.exceptions.DAOException;
import br.com.fabioperettig.exceptions.MaisDeUmRegistroException;
import br.com.fabioperettig.exceptions.TableException;
import br.com.fabioperettig.exceptions.TipoChaveNaoEncontradaException;

import static org.junit.jupiter.api.Assertions.*;


public class ClienteDAOTest {

	private IClienteDAO clienteDAO;

	private Random random;

	public ClienteDAOTest() {
		this.clienteDAO = new ClienteDAO();
		random = new Random();
	}

	@AfterEach
	public void safeDelete() throws  DAOException {
		Collection<Cliente> list = clienteDAO.buscarTodos();
		list.forEach(c -> {
			try {
                clienteDAO.excluir(c);
            } catch (DAOException e) {
                e.printStackTrace();
            }
        });
	}

	@Test
	public void pesquisarClienteTeste() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
		Cliente cliente = criarCliente();
		clienteDAO.cadastrar(cliente);

		Cliente cResult = clienteDAO.consultar(cliente.getId());
		Assertions.assertNotNull(cResult);
	}

	@Test
	public void cadastrarClienteTeste() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		Cliente cliente = criarCliente();
		clienteDAO.cadastrar(cliente);
		Assertions.assertNotNull(cliente);
		Assertions.assertSame("Fabio", cliente.getNome());
	}

	@Test
	public void excluirClienteTeste() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		Cliente cliente = criarCliente();
		clienteDAO.cadastrar(cliente);
		Assertions.assertNotNull(cliente);

		Cliente cResult = clienteDAO.consultar(cliente.getId());
		Assertions.assertNotNull(cResult);

		clienteDAO.excluir(cliente);

		Cliente cDelete = clienteDAO.consultar(cResult.getId());
		Assertions.assertNull(cDelete);
	}

	@Test
	public void alterarCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		Cliente cliente = criarCliente();
		clienteDAO.cadastrar(cliente);
		Assertions.assertNotNull(cliente);

		Cliente cResult = clienteDAO.consultar(cliente.getId());
		Assertions.assertNotNull(cResult);

		cResult.setNome("Don Lotário");
		clienteDAO.alterar(cResult);

		Assertions.assertSame("Don Lotário", cResult.getNome());
	}

	@Test
	public void buscarTodos() throws TipoChaveNaoEncontradaException, DAOException {
		Cliente cliente1 = criarCliente();
		clienteDAO.cadastrar(cliente1);
		Assertions.assertNotNull(cliente1);

		Cliente cliente2 = criarCliente();
		clienteDAO.cadastrar(cliente2);
		Assertions.assertNotNull(cliente2);

		Collection<Cliente> list = clienteDAO.buscarTodos();
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());

		list.forEach(c -> {
			try {
				clienteDAO.excluir(c);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});

		Collection<Cliente> list1 = clienteDAO.buscarTodos();
        assertNotNull(list1);
        assertEquals(0, list1.size());
	}

	@Test
	public void buscarClienteCriteria() throws DAOException, TipoChaveNaoEncontradaException {
		Cliente cliente = criarCliente();
		cliente.setCpf(12345678901L);
		clienteDAO.cadastrar(cliente);
		Assertions.assertNotNull(cliente);

		Cliente cResult = clienteDAO.consultaPorCPF(cliente.getCpf());
		Assertions.assertNotNull(cResult);
		Assertions.assertEquals(cResult.getCpf(), cliente.getCpf());
	}

	private Cliente criarCliente() {
		Cliente cliente = new Cliente();
		cliente.setCpf(random.nextLong());
		cliente.setNome("Fabio");
		cliente.setCidade("São Paulo");
		cliente.setEnd("End");
		cliente.setEstado("SP");
		cliente.setNumero(123);
		cliente.setTel(1199999999L);
		return cliente;
	}
}
