package com.example.backend.infrastructure.dto;

import com.example.backend.domain.model.Endereco;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoResponse {
    private Integer cep;
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
        return EnderecoResponse.builder()
                .cep(domain.getCep())
                .rua(domain.getRua())
                .numero(domain.getNumero())
                .cidade(domain.getCidade())
                .estado(domain.getEstado())
                .build();
    }
}