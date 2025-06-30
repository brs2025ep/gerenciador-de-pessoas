package com.example.backend.domain.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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

    private Integer cep;

    private String rua;

    private Integer numero;

    private String cidade;

    private String estado;

    public static Endereco createDefaultEndereco() {
        return new Endereco(null, 0, "", 0, "", "");
    }
}
