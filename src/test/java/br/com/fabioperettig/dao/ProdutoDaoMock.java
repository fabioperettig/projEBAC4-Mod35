package br.com.fabioperettig.dao;

import java.util.ArrayList;
import java.util.Collection;

import br.com.fabioperettig.domain.Produto;

@Deprecated
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
	public Produto consultar(Long id) {
		Produto produto = new Produto();
//		produto.setCodigo(codigo);
		return produto;
	}

	@Override
	public Collection<Produto> buscarTodos() {
		return new ArrayList<>();
	}

	@Override
	public Produto consultaPorCodigo(String codigo) {
		return null;
	}
}
