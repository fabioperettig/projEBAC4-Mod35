package br.com.fabioperettig.dao;

import br.com.fabioperettig.dao.generic.IGenericDao;
import br.com.fabioperettig.domain.Estoque;
import br.com.fabioperettig.exceptions.DAOException;

public interface IEstoqueDAO extends IGenericDao<Estoque, Long> {
    void adicionar(String codigoProduto, Integer quantidade) throws DAOException;
}
