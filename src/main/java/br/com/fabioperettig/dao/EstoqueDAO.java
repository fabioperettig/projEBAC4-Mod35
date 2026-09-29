package br.com.fabioperettig.dao;

import br.com.fabioperettig.dao.generic.GenericDao;
import br.com.fabioperettig.domain.Estoque;
import br.com.fabioperettig.exceptions.DAOException;

public class EstoqueDAO extends GenericDao<Estoque, Long> implements IEstoqueDAO {

    public EstoqueDAO(Class<Estoque> persistenteClass) {
        super(persistenteClass);
    }

    @Override
    public void adicionar(String codigoProduto, Integer quantidade) throws DAOException {

    }
}
