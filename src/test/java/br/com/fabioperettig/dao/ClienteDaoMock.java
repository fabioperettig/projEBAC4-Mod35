package br.com.fabioperettig.dao;

import java.util.ArrayList;
import java.util.Collection;

import br.com.fabioperettig.domain.Cliente;

@Deprecated
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
