package com.example.api.infrastructure.dto;

import com.example.api.domain.model.Endereco;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoRequest {

    @NotBlank(message = "É obrigatório ter CEP")
    private Integer cep;

    @NotBlank(message = "É obrigatório ter rua(logradouro)")
    private String rua;


    @NotBlank(message = "É obrigatório ter número")
    private Integer numero;

    @NotBlank(message = "É obrigatório ter Cidade")
    private String cidade;

    @NotBlank(message = "É obrigatório ter Estado")
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