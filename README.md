![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Curso EBAC](https://img.shields.io/badge/Curso--EBAC-235789?style=for-the-badge)
![Java Persistence](https://img.shields.io/badge/Java_Persistence_API-FDFFFC?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

# ☕ Projeto número 4 — EBAC, módulo 35

Projeto DAO com sistema CRUD, baseado no repositório [projEBAC3-Mod30](https://github.com/fabioperettig/projEBAC3-Mod30).
O objetivo principal deste projeto foi revisitar todo o projeto DAO implementado no módulo 30 e refatorar o seu sitema
de métodos CRUD, de estrutura `JDBC` para a estrutura `JPA + Hibernate`.


## 📖 Entidades
O projeto preservou todas as entidades implementadas no módulo 30, mas deixou de usar anotações JDBC e passou a usar
anotações JPA.

```java
@Entity
@Table(name= "TB_CLIENTE")
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
	@SequenceGenerator(name = "cliente_seq", sequenceName = "sq_cliente", initialValue = 1, allocationSize = 1)
	private Long id;
	
	@Column(name = "NOME", nullable = false, length = 50)
	private String nome;

	@Column(name = "CPF", nullable = false, unique = true)
    private Long cpf;

	@Column(name = "TELEFONE", nullable = false)
    private Long tel;
    
	@Column(name = "ENDERECO", nullable = false, length = 50)
    private String end;

	@Column(name = "NUMERO", nullable = false)
    private Integer numero;

	@Column(name = "CIDADE", nullable = false)
    private String cidade;

	@Column(name = "ESTADO", nullable = false)
    private String estado;
}
```
## ⚙️ Sistema DAO

O projeto é composto com um sisteam DAO Genérico, em Classe Abstrata, sendo utilizável em todas as entidades
através de Injeção de dependência. Além do CRUD tradicional, o método de consulta foi construído com JPQL
`(Java Persistence Query Language)` em StringBuilder, alocada em no método auxiliar `private String getSelectSql()`.

```java
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
```

## ✨Critéria API

Para fins de reforço dos estudos, decidi implementar dois métodos extras de consulta para as entidades `Cliente` e
`Produto`, com parâmetros personalizados, através da consulta com Critéria API.

`Cliente`
```java
@Override
public Cliente consultaPorCPF(Long cpf) {

    EntityManager em = EmFactorySingleton.getEntityManager();

    CriteriaBuilder cBuilder = em.getCriteriaBuilder();
    CriteriaQuery<Cliente> cQuery = cBuilder.createQuery(Cliente.class);

    Root<Cliente> clienteRoot = cQuery.from(Cliente.class);
    cQuery.select(clienteRoot).where(cBuilder.equal(clienteRoot.get("cpf"),cpf));

    TypedQuery<Cliente> typedQuery = em.createQuery(cQuery);

    return typedQuery.getSingleResult();
}
```
`Produto`
```java
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
```


## 📋 Principais mudanças do projeto

Durante a refatoração, tomei algumas decisões como registro da mudança estrutural e como escolhas de aberturas de
conexões que pareceram fazer mais sentido para este projeto/estudo.

| Decisões                                               | Motivos                                                                                                                                                                            |
|--------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ConnectionFactory para EntityManagerFactory            | Mudança de estrutura`ConnectionFactory` JDBC para `EntityManagerFactory` JPA construída em pattern Singleton com `Double-Checked Locking`;                                         |
| Classes sinalizadas como @Deprecated | Classes JDBC mantidas e devidamentes sinalizadas como `@Deprecated` para que se mantenha o registro da evolução do projeto, evite exclusão de Classes e proteja de usos indevidos; |
| JUnit Suite 6                                          | Após todas as Classes de Testes implementadas, todas foram concentradas à Classe Suite e novamente testadas em sequência.                                                          |


## ✅ Conclusão
Com a mudança estrutural deste projeto, fica claro entender tanto a importância do JPA, quanto a implementação
deste projeto em JDBC em módulos passados. Assim, é possível entender quais partes do projeto permanecem sob o controle
do Dev e quais partes fica a cargo do framework.

----

### Fabio Peretti Guimarães | Ebac mod 35 | SET 2026
