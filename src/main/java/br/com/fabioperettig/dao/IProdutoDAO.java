package br.com.fabioperettig.dao;

import br.com.fabioperettig.exceptions.DAOException;


import br.com.fabioperettig.dao.generic.IGenericDao;
import br.com.fabioperettig.domain.Produto;

public interface IProdutoDAO extends IGenericDao<Produto, String> {

    Boolean cadastrar(Produto produto, Integer quantidadeInicial) throws DAOException;
}
