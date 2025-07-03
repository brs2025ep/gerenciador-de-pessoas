package com.example.backend.infrastructure.dto;

import com.example.backend.domain.model.Endereco;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoResponse {
    private String cep;
    private String rua;
    private Integer numero;
    private String cidade;
    private String estado;

    /**
     * Convert a model Endereco em DTO EnderecoResponse.
     * @param domain A model Endereco.
     * @return EnderecoResponse Retorno esperado.
     */
    public static EnderecoResponse fromDomain(Endereco domain) {
        // Formata o cep para ter 8 dígitos, incluindo zero na frente para preencher.
        String formattedCep = String.format("%08d", domain.getCep());

        return EnderecoResponse.builder()
                .cep(formattedCep)
                .rua(domain.getRua())
                .numero(domain.getNumero())
                .cidade(domain.getCidade())
                .estado(domain.getEstado())
                .build();
    }
}