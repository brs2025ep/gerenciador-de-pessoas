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
    @NotBlank(message = "É obrigatório ter nome da Pessoa")
    private String nome;

    private LocalDateTime nascimento;

    private String cpf;

    private String email;

    private EnderecoRequest endereco;

    /**
     * Converter a PessoaRequest DTO em model Pessoa.
     * @return Pessoa retorno do domain model.
     */
    public Pessoa toDomain() {
        Pessoa.PessoaBuilder pessoaBuilder = Pessoa.builder()
                .nome(this.nome)
                .nascimento(this.nascimento)
                .cpf(this.cpf)
                .email(this.email);

        if (this.endereco != null) {
            pessoaBuilder.endereco(this.endereco.toDomain());
        }

        return pessoaBuilder.build();
    }

}
