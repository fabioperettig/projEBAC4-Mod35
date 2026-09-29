package br.com.fabioperettig.dao;

import br.com.fabioperettig.dao.generic.GenericDao;
import br.com.fabioperettig.domain.Cliente;

public class ClienteDAO extends GenericDao<Cliente, Long> implements IClienteDAO {

	public ClienteDAO() {
		super(Cliente.class);
	}
}
