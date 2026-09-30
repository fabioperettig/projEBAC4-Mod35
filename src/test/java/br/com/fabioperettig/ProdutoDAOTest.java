package br.com.fabioperettig;

import br.com.fabioperettig.dao.IProdutoDAO;
import br.com.fabioperettig.dao.ProdutoDAO;
import br.com.fabioperettig.domain.Produto;
import br.com.fabioperettig.exceptions.DAOException;
import br.com.fabioperettig.exceptions.MaisDeUmRegistroException;
import br.com.fabioperettig.exceptions.TableException;
import br.com.fabioperettig.exceptions.TipoChaveNaoEncontradaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;

public class ProdutoDAOTest {

    private IProdutoDAO produtoDAO;

    public ProdutoDAOTest() {
        this.produtoDAO = new ProdutoDAO();
    }

    @AfterEach
    public void safeDelete() throws DAOException {
        Collection<Produto> list = produtoDAO.buscarTodos();
        list.forEach(p -> {
            try {
                produtoDAO.excluir(p);
            } catch (DAOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void pesquisarProdutoTeste() throws TipoChaveNaoEncontradaException,
            DAOException, MaisDeUmRegistroException, TableException {
        Produto produto = criarProduto("PROD000");
        produtoDAO.cadastrar(produto);

        Produto pResult = produtoDAO.consultar(produto.getId());
        Assertions.assertNotNull(pResult);
    }

    @Test
    public void cadastrarProdutoTeste() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = criarProduto("PROD000");
        produtoDAO.cadastrar(produto);
        Assertions.assertNotNull(produto);
        Assertions.assertSame("PROD000", produto.getCodigo());
    }

    @Test
    public void excluirProdutoTeste() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = criarProduto("PROD000");
        produtoDAO.cadastrar(produto);
        Assertions.assertNotNull(produto);

        Produto pResult = produtoDAO.consultar(produto.getId());
        Assertions.assertNotNull(pResult);

        produtoDAO.excluir(produto);

        Produto pDelete = produtoDAO.consultar(produto.getId());
        Assertions.assertNull(pDelete);
    }

    @Test
    public void alterarProduto() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = criarProduto("PROD000");
        produtoDAO.cadastrar(produto);
        Assertions.assertNotNull(produto);

        Produto pResult = produtoDAO.consultar(produto.getId());
        Assertions.assertNotNull(pResult);

        BigDecimal esperado = new BigDecimal("200.0");

        pResult.setValor(esperado);
        produtoDAO.alterar(pResult);

        Produto atualizado = produtoDAO.consultar(pResult.getId());
        Assertions.assertNotNull(atualizado);
        Assertions.assertEquals(0, esperado.compareTo(atualizado.getValor()));
    }

    @Test
    public void buscarTodos() throws DAOException, TipoChaveNaoEncontradaException {
        criarProduto("PROD0001");
        criarProduto("PROD0002");
        produtoDAO.cadastrar(criarProduto("PROD0001"));
        produtoDAO.cadastrar(criarProduto("PROD0002"));
        Collection<Produto> list = produtoDAO.buscarTodos();
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());

        for (Produto produto : list) {
            this.produtoDAO.excluir(produto);
        }

        list = produtoDAO.buscarTodos();
        Assertions.assertNotNull(list);
        Assertions.assertEquals(0, list.size());
    }

    @Test
    public void buscarProdutoCriteria() throws DAOException, TipoChaveNaoEncontradaException {
        Produto produto = criarProduto("PROD999");
        produtoDAO.cadastrar(produto);
        Assertions.assertNotNull(produto);

        Produto cResult = produtoDAO.consultaPorCodigo(produto.getCodigo());
        Assertions.assertNotNull(cResult);
        Assertions.assertEquals(cResult.getCodigo(), produto.getCodigo());
    }

    private Produto criarProduto(String codigo) throws TipoChaveNaoEncontradaException, DAOException {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setDescricao("Desc PRODUTO 001");
        produto.setNome("PRODUTO 001");
        produto.setValor(BigDecimal.TEN);

        return produto;
    }
}
