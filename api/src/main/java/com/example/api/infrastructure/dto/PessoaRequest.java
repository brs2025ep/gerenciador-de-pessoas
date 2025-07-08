package com.example.api.infrastructure.dto;

import com.example.api.domain.model.Pessoa;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaRequest {
    @NotBlank(message = "É obrigatório ter nome da Pessoa")
    private String nome;

    private LocalDateTime nascimento;

    @NotBlank(message = "É obrigatório ter CPF")
    private String cpf;

    @NotBlank(message = "É obrigatório ter email")
    private String email;

    @Valid
    private EnderecoRequest endereco;

    private String situacaoIntegracao;


    /**
     * Convert a PessoaRequest DTO em a Pessoa domain model.
     * @return Pessoa retorno do domain model.
     */
    public Pessoa toDomain() {
        return Pessoa.builder()
                .nome(this.nome)
                .nascimento(this.nascimento)
                .cpf(this.cpf)
                .endereco(this.endereco != null ? this.endereco.toDomain() : null)
                .build();
    }

}
