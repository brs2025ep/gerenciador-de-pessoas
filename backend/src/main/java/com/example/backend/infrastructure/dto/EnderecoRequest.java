package com.example.backend.infrastructure.dto;

import com.example.backend.domain.model.Endereco;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoRequest {
    @Min(value = 1001000, message = "O CEP deve ter 8 dígitos (mínimo 10000000).")
    @Max(value = 99999999, message = "O CEP deve ter 8 dígitos (máximo 99999999).")
    private Integer cep;
    private String rua;
    private Integer numero;
    private String cidade;
    private String estado;

    /**
     * Converte o EnderecoRequest DTO para Endereco domain model.
     * @return Endereco retorna a Pessoa domain model.
     */
    public Endereco toDomain() {
        return Endereco.builder()
                .cep(this.cep)
                .rua(this.rua)
                .numero(this.numero)
                .cidade(this.cidade)
                .estado(this.estado)
                .build();
    }
}