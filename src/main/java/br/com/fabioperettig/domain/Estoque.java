package br.com.fabioperettig.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "TB_ESTOQUE")
public class Estoque {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="estoque_seq")
    @SequenceGenerator(name="estoque_seq", sequenceName="sq_estoque", initialValue = 1, allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_estoque_fk",
            foreignKey = @ForeignKey(name = "fk_prod_qtd_estoque"),
            referencedColumnName = "id", nullable = false
    )
    private Produto produto;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    ///getter_setter
    public Long getId() { return id; }
    public Produto getProduto() { return produto; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
