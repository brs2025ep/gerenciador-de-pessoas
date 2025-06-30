package com.example.api.infrastructure.dto;

import com.example.api.domain.model.Endereco;
import com.example.api.domain.model.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class PessoaGetResponse {
    private String nome;
    private String dataNascimento;
    private String cpf;
    private String email;
    private Endereco endereco;
    private LocalDateTime dataHoraInclusaoRegistro;
    private LocalDateTime dataHoraUltimaAlteracaoRegistro;

    /**
     * Converte a model Pessoa em DTO PessoaGetResponse.
     * @param domain A model Pessoa.
     * @return PessoaGetResponse Retorno esperado.
     */
    public static PessoaGetResponse fromDomain(Pessoa domain) {
        return PessoaGetResponse.builder()
                .nome(domain.getNome())
                .dataNascimento(domain.getNascimento().toString())
                .cpf(domain.getCpf())
                .email(domain.getEmail())
                .dataHoraInclusaoRegistro(domain.getCriacaoRegistro())
                .dataHoraUltimaAlteracaoRegistro(domain.getAlteracaoRegistro())
                .endereco(domain.getEndereco())
                .build();
    }
}