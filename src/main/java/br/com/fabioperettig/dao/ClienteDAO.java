package br.com.fabioperettig.dao;

import br.com.fabioperettig.config.EmFactorySingleton;
import br.com.fabioperettig.dao.generic.GenericDao;
import br.com.fabioperettig.domain.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class ClienteDAO extends GenericDao<Cliente, Long> implements IClienteDAO {

	public ClienteDAO() {
		super(Cliente.class);
	}

	///metodo CRITÉRIA API
	@Override
	public Cliente consultaPorCPF(Long cpf) {

		EntityManager em = EmFactorySingleton.getEntityManager();

		CriteriaBuilder cBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Cliente> cQuery = cBuilder.createQuery(Cliente.class);

		Root<Cliente> clienteRoot = cQuery.from(Cliente.class);
		cQuery.select(clienteRoot).where(cBuilder.equal(clienteRoot.get("cpf"), cpf));

		TypedQuery<Cliente> typedQuery = em.createQuery(cQuery);

		return typedQuery.getSingleResult();
	}
}
