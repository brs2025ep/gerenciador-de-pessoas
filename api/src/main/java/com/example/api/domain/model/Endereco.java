package com.example.api.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Endereco {

    @OneToOne
    @MapsId
    @JoinColumn(name = "idPessoa")
    private Pessoa pessoa;

    @NotBlank(message = "O CEP não pode ser nulo ou vazio")
    @Column(nullable = false)
    private Integer cep;

    @NotBlank(message = "O nome da rua(logradouro) não pode ser nulo ou vazio")
    @Column(nullable = false)
    private String rua;

    // TODO: Numero da casa deve permitir N/A ou 0
    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;
}
