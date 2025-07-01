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
    private String situacaoIntegracao;


    /**
     * Convert a model Pessoa em DTO PessoaResponse.
     * @param domain A model Pessoa.
     * @return PessoaResponse A resposta a ser retornada.
     */
    public static PessoaResponse fromDomain(Pessoa domain) {

        PessoaResponse.PessoaResponseBuilder pessoaResponseBuilder = PessoaResponse.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .nascimento(domain.getNascimento())
                .cpf(domain.getCpf())
                .email(domain.getEmail())
                .endereco(domain.getEndereco() != null ? EnderecoResponse.fromDomain(domain.getEndereco()): null);

        if (domain.getSituacaoIntegracao() != null) {
            try {
                pessoaResponseBuilder.situacaoIntegracao(domain.getSituacaoIntegracao().toString());
            } catch (IllegalArgumentException e) {
                // Log the issue, or throw a specific exception if an invalid enum value is critical
                System.err.println("Invalid SituacaoIntegracao value received: " + domain.getSituacaoIntegracao());
                // Optionally, handle this by setting a default or re-throwing
            }
        }

        return pessoaResponseBuilder.build();
    }
}