package com.example.backend.infrastructure.dto;

import com.example.backend.domain.model.Endereco;
import com.example.backend.domain.model.Pessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaRequest {
    private Integer id;

    @NotBlank(message = "É obrigatório ter nome da Pessoa")
    private String nome;

    private LocalDateTime nascimento;

    private String cpf;

    private String email;

    private EnderecoRequest endereco;

    private String situacaoIntegracao;


    /**
     * Converter a PessoaRequest DTO em model Pessoa.
     * @return Pessoa retorno do domain model.
     */
    public Pessoa toDomain() {
        return Pessoa.builder()
                .id(this.id)
                .nome(this.nome)
                .endereco(this.endereco != null ? this.endereco.toDomain() : null)
                .build();
    }

}
