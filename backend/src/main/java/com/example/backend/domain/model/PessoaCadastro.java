package com.example.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name = "Pessoa")
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Pessoa")
public class PessoaCadastro extends PessoaBase{
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SituacaoIntegracao situacaoIntegracao = SituacaoIntegracao.PENDENTE;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private Endereco endereco;

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
        if (endereco != null) {
            endereco.setPessoa(this);
        }
    }
}