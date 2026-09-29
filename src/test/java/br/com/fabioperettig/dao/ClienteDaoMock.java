package br.com.fabioperettig.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.fabioperettig.domain.Cliente;
import br.com.fabioperettig.exceptions.DAOException;
import br.com.fabioperettig.exceptions.MaisDeUmRegistroException;
import br.com.fabioperettig.exceptions.TableException;
import br.com.fabioperettig.exceptions.TipoChaveNaoEncontradaException;

public class ClienteDaoMock implements IClienteDAO {

	@Override
	public Cliente cadastrar(Cliente entity) {
		return entity;
	}

	@Override
	public void excluir(Cliente entity) {

	}

	@Override
	public Cliente alterar(Cliente entity) {
		return entity;
	}

	@Override
	public Cliente consultar(Long cpf) {
		Cliente cliente = new Cliente();
		cliente.setCpf(cpf);
		return cliente;
	}

	@Override
	public Collection<Cliente> buscarTodos() {
        return new ArrayList<>();
	}
}
