package br.com.fabioperettig.dao.generic;

import br.com.fabioperettig.config.EmFactorySingleton;
import br.com.fabioperettig.exceptions.DAOException;
import br.com.fabioperettig.exceptions.MaisDeUmRegistroException;
import br.com.fabioperettig.exceptions.TableException;
import br.com.fabioperettig.exceptions.TipoChaveNaoEncontradaException;
import jakarta.persistence.EntityManager;

import java.util.Collection;
import java.util.List;

public class GenericDao<T,ID> implements IGenericDao<T,ID> {

    private final Class<T> persistenteClass;

    ///injeção de Dependência para que o metodo de busca possa trabalhar de forma genérica
    public GenericDao(Class<T> persistenteClass) {
        this.persistenteClass = persistenteClass;
    }

    @Override
    public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException {

        EntityManager em = EmFactorySingleton.getEntityManager();

        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    @Override
    public void excluir(T entity) throws DAOException {

        EntityManager em = EmFactorySingleton.getEntityManager();

        em.getTransaction().begin();
        entity = em.merge(entity);
        em.remove(entity);
        em.getTransaction().commit();
        em.close();

    }

    @Override
    public T alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException {

        EntityManager em = EmFactorySingleton.getEntityManager();

        em.getTransaction().begin();
        entity = em.merge(entity);
        em.getTransaction().commit();
        em.close();

        return entity;
    }

    @Override
    public T consultar(ID id) throws MaisDeUmRegistroException, TableException, DAOException {

        EntityManager em = EmFactorySingleton.getEntityManager();
        try {
            return em.find(this.persistenteClass, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<T> buscarTodos() throws DAOException {

        EntityManager em = EmFactorySingleton.getEntityManager();

        List<T> list = em.createQuery(getSelectSql(), this.persistenteClass).getResultList();
        em.close();

        return list;
    }

    ///auxiliares
    private String getSelectSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT obj FROM ");
        sb.append(this.persistenteClass.getSimpleName());
        sb.append(" obj");
        return sb.toString();
    }
}
