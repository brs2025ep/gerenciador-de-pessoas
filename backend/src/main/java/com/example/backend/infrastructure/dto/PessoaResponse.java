package com.example.backend.infrastructure.dto;

import com.example.backend.domain.model.Pessoa;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaResponse {
    private Integer id;
    private String nome;
    private LocalDateTime nascimento;
    private String cpf;
    private String email;
    private EnderecoResponse endereco;

    /**
     * Convert a model Pessoa em DTO PessoaResponse.
     * @param domain A model Pessoa.
     * @return PessoaResponse A resposta a ser retornada.
     */
    public static PessoaResponse fromDomain(Pessoa domain) {
        return PessoaResponse.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .cpf(domain.getCpf())
                .email(domain.getEmail())
                .endereco(domain.getEndereco() != null ? EnderecoResponse.fromDomain(domain.getEndereco()) : null)
                .nascimento(domain.getNascimento())
                .build();
    }
}