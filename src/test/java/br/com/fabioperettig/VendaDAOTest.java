package br.com.fabioperettig;

import br.com.fabioperettig.dao.*;
import br.com.fabioperettig.domain.Cliente;
import br.com.fabioperettig.domain.Produto;
import br.com.fabioperettig.domain.Venda;
import br.com.fabioperettig.exceptions.DAOException;
import br.com.fabioperettig.exceptions.MaisDeUmRegistroException;
import br.com.fabioperettig.exceptions.TableException;
import br.com.fabioperettig.exceptions.TipoChaveNaoEncontradaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class VendaDAOTest {

    private Cliente cliente;
    private Produto produto;
    private IClienteDAO clienteDAO;
    private IProdutoDAO produtoDAO;
    private IVendaDAO vendaDAO;
    private IVendaDAO excluirVenda;

    private Random random;
    private final String sufixo = UUID.randomUUID().toString();
    private final List<Venda> vendasCriadas = new ArrayList<>();
    private final List<Produto> produtosCriados = new ArrayList<>();


    public VendaDAOTest() {
        this.clienteDAO = new ClienteDAO();
        this.produtoDAO = new ProdutoDAO();
        this.vendaDAO = new VendaDAO();
        excluirVenda = new ExcluirVenda();

        random = new Random();
    }

    @BeforeEach
    public void init() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {
        this.cliente = cadastrarCliente();
        this.produto = cadastrarProduto("PROD000", BigDecimal.TEN);
    }

    @AfterEach
    public void safeDelete() throws DAOException {
        excluirVendas();
        excluirProdutos();
        if (cliente != null && cliente.getId() != null) {
            clienteDAO.excluir(cliente);
        }
    }

    @Test
    public void salvar() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);

        Assertions.assertEquals(venda.getValorTotal(), BigDecimal.valueOf(20));
        Assertions.assertEquals(venda.getStatus(), Venda.Status.INICIADA);

        Venda vConsulta = vendaDAO.consultar(venda.getId());
        Assertions.assertNotNull(vConsulta.getId());
        Assertions.assertEquals(venda.getCodigo(), vConsulta.getCodigo());
        Venda completa = vendaDAO.consultarCollectionCriteria(venda.getCodigo());
        Assertions.assertEquals(cliente.getId(), completa.getCliente().getId());
        Assertions.assertEquals(2, completa.getQuantidadeTotalProdutos());
        Assertions.assertEquals(0, BigDecimal.valueOf(20).compareTo(completa.getValorTotal()));
        Assertions.assertEquals(produto.getId(), completa.getProdutos().iterator().next().getProduto().getId());
        Assertions.assertEquals(completa.getId(), completa.getProdutos().iterator().next().getVenda().getId());
    }

    @Test
    public void pesquisar() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);

        Venda vResult = vendaDAO.consultar(venda.getId());
        Assertions.assertNotNull(vResult);
        Assertions.assertEquals(venda.getCodigo(), vResult.getCodigo());
    }



    @Test
    public void cancelarVenda() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);

        venda.setStatus(Venda.Status.CANCELADA);
        vendaDAO.cancelarVenda(venda);

        Venda vResult = vendaDAO.consultar(venda.getId());
        Assertions.assertEquals("VND000" + sufixo, vResult.getCodigo());
        Assertions.assertEquals(Venda.Status.CANCELADA, vResult.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDoMesmo() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);

        Venda vConsulta = vendaDAO.consultarCollectionCriteria(venda.getCodigo());
        vConsulta.adicionarProduto(produto, 1);
        Assertions.assertEquals(3, (int) vConsulta.getQuantidadeTotalProdutos());

        BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        Assertions.assertEquals(vConsulta.getValorTotal(), valorTotal);
        Assertions.assertEquals(Venda.Status.INICIADA, vConsulta.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDiferentes() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);

        Produto produto = cadastrarProduto("PROD001", BigDecimal.valueOf(50));
        Assertions.assertNotNull(produto);
        Assertions.assertEquals("PROD001" + sufixo, produto.getCodigo());

        Venda vConsulta = vendaDAO.consultarCollectionCriteria(venda.getCodigo());
        vConsulta.adicionarProduto(produto, 1);
        Assertions.assertEquals(3, (int) vConsulta.getQuantidadeTotalProdutos());

        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assertions.assertEquals(vConsulta.getValorTotal(), valorTotal);
        Assertions.assertEquals(Venda.Status.INICIADA, vConsulta.getStatus());
    }

    @Test()
    public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontradaException, DAOException {

        Venda venda1 = criarVenda("VND000");
        cadastrarVenda(venda1);
        Assertions.assertNotNull(venda1);

        Venda venda2 = criarVenda("VND000");
        Assertions.assertThrows(DAOException.class, () -> vendaDAO.cadastrar(venda2));

        Assertions.assertEquals(Venda.Status.INICIADA, venda1.getStatus());
    }

    @Test
    public void removerProduto() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);
        Assertions.assertEquals("VND000" + sufixo, venda.getCodigo());

        Produto produto = cadastrarProduto("PROD001", BigDecimal.valueOf(50));
        Assertions.assertNotNull(produto);
        Assertions.assertEquals("PROD001" + sufixo, produto.getCodigo());

        Venda vConsulta = vendaDAO.consultarCollectionCriteria(venda.getCodigo());
        vConsulta.adicionarProduto(produto, 1);
        Assertions.assertEquals(3, (int) vConsulta.getQuantidadeTotalProdutos());

        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assertions.assertEquals(vConsulta.getValorTotal(), valorTotal);

        vConsulta.removerProduto(produto, 1);
        Assertions.assertEquals(2, (int) vConsulta.getQuantidadeTotalProdutos());

        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        Assertions.assertEquals(vConsulta.getValorTotal(), valorTotal);
        Assertions.assertEquals(Venda.Status.INICIADA, vConsulta.getStatus());
    }

    @Test
    public void removerApenasUmProduto() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);
        Assertions.assertEquals("VND000" + sufixo, venda.getCodigo());

        Produto produto = cadastrarProduto("PROD001", BigDecimal.valueOf(50));
        Assertions.assertNotNull(produto);
        Assertions.assertEquals("PROD001" + sufixo, produto.getCodigo());

        Venda vConsulta = vendaDAO.consultarCollectionCriteria(venda.getCodigo());
        vConsulta.adicionarProduto(produto, 2);
        Assertions.assertEquals(4, (int) vConsulta.getQuantidadeTotalProdutos());

        BigDecimal valorTotal = BigDecimal.valueOf(120).setScale(2, RoundingMode.HALF_DOWN);
        Assertions.assertEquals(vConsulta.getValorTotal(), valorTotal);


        vConsulta.removerProduto(produto, 1);
        Assertions.assertEquals(3, (int) vConsulta.getQuantidadeTotalProdutos());

        valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assertions.assertEquals(vConsulta.getValorTotal(), valorTotal);
        Assertions.assertEquals(Venda.Status.INICIADA, vConsulta.getStatus());
    }

    @Test
    public void removerTodosProdutos() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);
        Assertions.assertEquals("VND000" + sufixo, venda.getCodigo());

        Produto produto = cadastrarProduto("PROD001", BigDecimal.valueOf(50));
        Assertions.assertNotNull(produto);
        Assertions.assertEquals("PROD001" + sufixo, produto.getCodigo());

        Venda vConsulta = vendaDAO.consultarCollectionCriteria(venda.getCodigo());
        vConsulta.adicionarProduto(produto, 1);
        Assertions.assertEquals(3, (int) vConsulta.getQuantidadeTotalProdutos());

        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assertions.assertEquals(vConsulta.getValorTotal(), valorTotal);

        vConsulta.removerTodosProdutos();
        Assertions.assertEquals(0, (int) vConsulta.getQuantidadeTotalProdutos());
        Assertions.assertEquals(vConsulta.getValorTotal(), BigDecimal.valueOf(0));
        Assertions.assertEquals(Venda.Status.INICIADA, vConsulta.getStatus());
    }

    @Test
    public void finalizarVenda() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);
        Assertions.assertEquals("VND000" + sufixo, venda.getCodigo());

        venda.setStatus(Venda.Status.CONCLUIDA);
        vendaDAO.finalizarVenda(venda);

        Venda vConsulta = vendaDAO.consultarCollectionCriteria(venda.getCodigo());
        Assertions.assertEquals(venda.getCodigo(), vConsulta.getCodigo());
        Assertions.assertEquals(Venda.Status.CONCLUIDA, vConsulta.getStatus());
    }

    @Test
    public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {

        Venda venda = criarVenda("VND000");
        cadastrarVenda(venda);
        Assertions.assertNotNull(venda);
        Assertions.assertEquals("VND000" + sufixo, venda.getCodigo());

        venda.setStatus(Venda.Status.CONCLUIDA);
        vendaDAO.finalizarVenda(venda);

        Venda vendaConsultada = vendaDAO.consultarCollectionCriteria(venda.getCodigo());
        Assertions.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        Assertions.assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> vendaConsultada.adicionarProduto(this.produto, 1));

    }


    @Test
    public void excluirVendaPreservaProdutoDeOutraVenda() throws Exception {
        Venda primeira = criarVenda("VND000");
        Venda segunda = criarVenda("VND001");
        cadastrarVenda(primeira);
        cadastrarVenda(segunda);

        excluirVenda.excluir(primeira);
        vendasCriadas.remove(primeira);

        Assertions.assertNull(vendaDAO.consultar(primeira.getId()));
        Assertions.assertNotNull(produtoDAO.consultar(produto.getId()));
        Assertions.assertNotNull(clienteDAO.consultar(cliente.getId()));
        Venda restante = vendaDAO.consultarCollectionCriteria(segunda.getCodigo());
        Assertions.assertEquals(2, restante.getQuantidadeTotalProdutos());
    }

    private void excluirProdutos() throws DAOException {
        for (Produto produto : produtosCriados) {
            produtoDAO.excluir(produto);
        }
    }

    private void excluirVendas() throws DAOException {
        for (Venda venda : vendasCriadas) {
            excluirVenda.excluir(venda);
        }
    }

    private void cadastrarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
        vendaDAO.cadastrar(venda);
        vendasCriadas.add(venda);
        Assertions.assertNotNull(venda.getId());
    }

    private Produto cadastrarProduto(String codigo, BigDecimal valor) throws TipoChaveNaoEncontradaException,
            MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = new Produto();
        produto.setCodigo(codigo + sufixo);
        produto.setDescricao("Desc PRODUTO 001");
        produto.setNome("PRODUTO 001");
        produto.setValor(valor);
        produtoDAO.cadastrar(produto);
        produtosCriados.add(produto);

        return produto;
    }

    private Cliente cadastrarCliente() throws TipoChaveNaoEncontradaException, DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(random.nextLong());
        cliente.setNome("Fabio");
        cliente.setCidade("São Paulo");
        cliente.setEnd("End");
        cliente.setEstado("SP");
        cliente.setNumero(123);
        cliente.setTel(1199999999L);
        clienteDAO.cadastrar(cliente);

        return cliente;
    }

    private Venda criarVenda(String codigo) {
        Venda venda = new Venda();
        venda.setCodigo(codigo + sufixo);
        venda.setDataVenda(Instant.now());
        venda.setCliente(this.cliente);
        venda.setStatus(Venda.Status.INICIADA);
        venda.adicionarProduto(this.produto, 2);
        return venda;
    }

}
