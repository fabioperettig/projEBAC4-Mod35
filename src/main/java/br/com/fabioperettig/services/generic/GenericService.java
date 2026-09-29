package br.com.fabioperettig.services.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.fabioperettig.dao.Persistente;
import br.com.fabioperettig.dao.generic.IGenericDao;
import br.com.fabioperettig.exceptions.DAOException;
import br.com.fabioperettig.exceptions.MaisDeUmRegistroException;
import br.com.fabioperettig.exceptions.TableException;
import br.com.fabioperettig.exceptions.TipoChaveNaoEncontradaException;

public abstract class GenericService<T, ID> implements IGenericService<T, ID> {
	
	protected IGenericDao<T,ID> dao;
	
	public GenericService(IGenericDao<T,ID> dao) {
		this.dao = dao;
	}

	@Override
	public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
		return this.dao.cadastrar(entity);
	}

	@Override
	public void excluir(T entity) throws DAOException {
		this.dao.excluir(entity);
	}

	@Override
	public T alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
		return this.dao.alterar(entity);
	}

	@Override
	public T consultar(ID valor) throws MaisDeUmRegistroException, TableException, DAOException {
			return this.dao.consultar(valor);
	}

	@Override
	public Collection<T> buscarTodos() throws DAOException {
		return this.dao.buscarTodos();
	}

}
