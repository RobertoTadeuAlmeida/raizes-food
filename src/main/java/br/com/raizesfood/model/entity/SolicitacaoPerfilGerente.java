package br.com.raizesfood.model.entity;

import br.com.raizesfood.model.enums.StatusSolicitacaoGerente;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes_perfil_gerente")
public class SolicitacaoPerfilGerente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSolicitacaoGerente status = StatusSolicitacaoGerente.PENDENTE;

    @Column(nullable = false)
    private LocalDateTime criadaEm = LocalDateTime.now();

    private LocalDateTime concluidaEm;

    public SolicitacaoPerfilGerente() {
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public StatusSolicitacaoGerente getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacaoGerente status) {
        this.status = status;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public LocalDateTime getConcluidaEm() {
        return concluidaEm;
    }

    public void setConcluidaEm(LocalDateTime concluidaEm) {
        this.concluidaEm = concluidaEm;
    }
}