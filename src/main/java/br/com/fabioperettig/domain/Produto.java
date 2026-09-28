package br.com.fabioperettig.domain;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_PRODUTO")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_seq")
	@SequenceGenerator(name = "produto_seq", sequenceName = "sq_produto", initialValue = 1, allocationSize = 1)
	private Long id;

	@Column(name = "CODIGO", nullable = false, unique = true)
	private String codigo;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "VALOR")
	private BigDecimal valor;

	@Column(name = "CUPOM")
	private Boolean cupom15Off = false;

	///getter_setter
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Long getId() {
		return id;
	}
	public Boolean getCupom15Off() {
		return cupom15Off;
	}
	public void setCupom15Off(Boolean valor) {
		this.cupom15Off = java.util.Objects.requireNonNull(valor);
	}
}
