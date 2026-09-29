package br.com.fabioperettig.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.fabioperettig.domain.Produto;
import br.com.fabioperettig.exceptions.DAOException;
import br.com.fabioperettig.exceptions.MaisDeUmRegistroException;
import br.com.fabioperettig.exceptions.TableException;
import br.com.fabioperettig.exceptions.TipoChaveNaoEncontradaException;

public class ProdutoDaoMock implements IProdutoDAO {

	@Override
	public Produto cadastrar(Produto produto) {
		return produto;
	}

	@Override
	public void excluir(Produto produto) {
	}

	@Override
	public Produto alterar(Produto produto) {
		return produto;
	}

	@Override
	public Produto consultar(String codigo) {
		Produto produto = new Produto();
		produto.setCodigo(codigo);
		return produto;
	}

	@Override
	public Collection<Produto> buscarTodos() {
		return new ArrayList<>();
	}
}
