package br.com.fabioperettig.dao;

import br.com.fabioperettig.config.EmFactorySingleton;
import br.com.fabioperettig.dao.generic.GenericDao;
import br.com.fabioperettig.domain.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class ProdutoDAO extends GenericDao<Produto, Long> implements IProdutoDAO {

	public ProdutoDAO() {
		super(Produto.class);
	}

	@Override
	public Produto consultaPorCodigo(String codigo) {

		EntityManager em = EmFactorySingleton.getEntityManager();

		CriteriaBuilder cBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Produto> cQuery = cBuilder.createQuery(Produto.class);

		Root<Produto> clienteRoot = cQuery.from(Produto.class);
		cQuery.select(clienteRoot).where(cBuilder.equal(clienteRoot.get("codigo"), codigo));

		TypedQuery<Produto> typedQuery = em.createQuery(cQuery);

		return typedQuery.getSingleResult();
	}
}
