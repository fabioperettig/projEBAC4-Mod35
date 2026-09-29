package br.com.fabioperettig.dao;

import br.com.fabioperettig.config.EmFactorySingleton;
import br.com.fabioperettig.dao.generic.GenericDao;
import br.com.fabioperettig.domain.Cliente;
import br.com.fabioperettig.domain.Produto;
import br.com.fabioperettig.domain.Venda;
import br.com.fabioperettig.exceptions.DAOException;
import br.com.fabioperettig.exceptions.TipoChaveNaoEncontradaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class VendaDAO extends GenericDao<Venda, String> implements IVendaDAO {

	public VendaDAO(Class<Venda> persistenteClass) {
		super(persistenteClass);
	}

	@Override
	public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
		super.alterar(venda);
	}

	@Override
	public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
		super.alterar(venda);
	}


	@Override
	public Venda cadastrar(Venda entity) throws TipoChaveNaoEncontradaException, DAOException {

		try {

			EntityManager em = EmFactorySingleton.getEntityManager();
			entity.getProdutos().forEach(prd -> {
				Produto produto = em.merge(prd.getProduto());
				prd.setProduto(produto);
			});

			Cliente cliente = em.merge(entity.getCliente());
			entity.setCliente(cliente);
			em.persist(entity);
			em.getTransaction().commit();
			return entity;

		} catch (Exception e) {
			throw new DAOException("NÃO FOI POSSÍVEL SALVAR VENDA ", e);
		}

	}

	@Override
	public Venda consultarCollectionCriteria(String codigo) {

		EntityManager em = EmFactorySingleton.getEntityManager();

		CriteriaBuilder cBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Venda> query = cBuilder.createQuery(Venda.class);

		Root<Venda> root = query.from(Venda.class);
		root.fetch("cliente");
		root.fetch("produtos");
		query.select(root).where(cBuilder.equal(root.get("codigo"), codigo));

		TypedQuery<Venda> tpQuery = em.createQuery(query);

		return tpQuery.getSingleResult();
	}
}
