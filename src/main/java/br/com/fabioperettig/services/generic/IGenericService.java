package br.com.fabioperettig.services.generic;

import br.com.fabioperettig.exceptions.DAOException;
import br.com.fabioperettig.exceptions.MaisDeUmRegistroException;
import br.com.fabioperettig.exceptions.TableException;
import br.com.fabioperettig.exceptions.TipoChaveNaoEncontradaException;

import java.util.Collection;

public interface IGenericService <T, ID> {
    public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException;
    public void excluir(T entity) throws DAOException;
    public T alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException;
    public T consultar(ID id) throws DAOException, MaisDeUmRegistroException, TableException;
    public Collection<T> buscarTodos() throws DAOException;
}
