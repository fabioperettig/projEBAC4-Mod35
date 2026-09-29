package br.com.fabioperettig.dao;


import br.com.fabioperettig.dao.generic.GenericDao;
import br.com.fabioperettig.domain.Produto;

public class ProdutoDAO extends GenericDao<Produto, String> implements IProdutoDAO {

	public ProdutoDAO(Class<Produto> persistenteClass) {
		super(persistenteClass);
	}
}
