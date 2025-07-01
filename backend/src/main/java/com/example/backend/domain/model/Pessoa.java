package com.example.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonManagedReference
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SituacaoIntegracao situacaoIntegracao = SituacaoIntegracao.PENDENTE;

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
        if (endereco != null) {
            endereco.setPessoa(this);
        }
    }
}
