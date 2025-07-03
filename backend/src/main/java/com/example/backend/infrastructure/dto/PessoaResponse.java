package com.example.backend.infrastructure.dto;

import com.example.backend.domain.model.Pessoa;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaResponse {
    private Integer id;
    private String nome;
    private LocalDate nascimento;
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
                pessoaResponseBuilder.situacaoIntegracao(domain.getSituacaoIntegracao().getValue());
            } catch (Exception e) { // Catch more general exception if toString() or getValue() could fail
                log.error("Erro ao converter SituacaoIntegracao do domínio para resposta. Valor inválido: {}", domain.getSituacaoIntegracao(), e);
                pessoaResponseBuilder.situacaoIntegracao("PENDENTE");
            }
        }

        return pessoaResponseBuilder.build();
    }
}