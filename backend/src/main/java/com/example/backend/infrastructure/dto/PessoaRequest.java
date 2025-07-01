package com.example.backend.infrastructure.dto;

import com.example.backend.domain.model.Pessoa;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaRequest {
    @NotBlank(message = "É obrigatório ter nome da Pessoa")
    private String nome;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate nascimento;

    @Size(min = 11, max = 11, message = "O CPF deve ter 11 caracteres.")
    private String cpf;

    @Email(message = "Email format is invalid")
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
