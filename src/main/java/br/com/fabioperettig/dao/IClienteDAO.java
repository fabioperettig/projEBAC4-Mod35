package br.com.fabioperettig.dao;

import br.com.fabioperettig.dao.generic.IGenericDao;
import br.com.fabioperettig.domain.Cliente;

public interface IClienteDAO extends IGenericDao<Cliente, Long> {

    public Cliente consultaPorCPF(Long cpf);

}
