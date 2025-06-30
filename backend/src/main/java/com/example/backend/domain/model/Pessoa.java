package com.example.backend.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPessoa")
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = true)
    private LocalDateTime nascimento;

    @Column(unique = true, nullable = true)
    private String cpf;

    @Column(nullable = true)
    private String email;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private SituacaoIntegracao situacaoIntegracao = SituacaoIntegracao.PENDENTE;

    public void setEndereco(Endereco endereco) {
        if (endereco == null) {
            this.endereco = Endereco.createDefaultEndereco();
        } else {
            this.endereco = endereco;
        }

        if (this.endereco.getPessoa() != this) {
            this.endereco.setPessoa(this);
        }
    }
}
