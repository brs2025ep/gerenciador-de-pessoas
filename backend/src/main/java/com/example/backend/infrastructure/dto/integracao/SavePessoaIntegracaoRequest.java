package com.example.backend.infrastructure.dto.integracao;

import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.infrastructure.dto.EnderecoResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavePessoaIntegracaoRequest {
    private String nome;
    private LocalDate dataNascimento;
    private String cpf;
    private String email;
    private EnderecoResponse endereco;
    private String situacaoIntegracao;

    /**
     * Convert a model PessoaCadastro em DTO SavePessoaIntegradaRequest.
     * @param domain A model PessoaCadastro.
     * @return SavePessoaIntegradaRequest A resposta a ser retornada.
     */
    public static SavePessoaIntegracaoRequest fromDomain(PessoaCadastro domain) {

        SavePessoaIntegracaoRequest.SavePessoaIntegracaoRequestBuilder savePessoaIntegracaoResponseBuilder = SavePessoaIntegracaoRequest.builder()
                .nome(domain.getNome())
                .dataNascimento(domain.getNascimento())
                .cpf(domain.getCpf())
                .email(domain.getEmail())
                .endereco(domain.getEndereco() != null ? EnderecoResponse.fromDomain(domain.getEndereco()): null);

        if (domain.getSituacaoIntegracao() != null) {
            try {
                savePessoaIntegracaoResponseBuilder.situacaoIntegracao(domain.getSituacaoIntegracao().getValue());
            } catch (Exception e) { // Catch more general exception if toString() or getValue() could fail
                log.error("Erro ao converter SituacaoIntegracao do domínio para request. Valor inválido: {}", domain.getSituacaoIntegracao(), e);
                savePessoaIntegracaoResponseBuilder.situacaoIntegracao("PENDENTE");
            }
        }

        return savePessoaIntegracaoResponseBuilder.build();
    }
}