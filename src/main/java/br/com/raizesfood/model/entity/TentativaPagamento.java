package br.com.raizesfood.model.entity;

import br.com.raizesfood.model.enums.StatusPagamento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tentativas_pagamento")
public class TentativaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPagamento resultado;

    @Column(nullable = false)
    private LocalDateTime criadaEm = LocalDateTime.now();

    public TentativaPagamento() {
    }

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public StatusPagamento getResultado() {
        return resultado;
    }


    public void setResultado(StatusPagamento resultado) {
        this.resultado = resultado;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }


}