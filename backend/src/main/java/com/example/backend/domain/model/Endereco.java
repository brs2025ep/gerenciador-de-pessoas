package com.example.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Endereco {

    @Id
    @Column(name = "idPessoa")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "idPessoa")
    private Pessoa pessoa;

    private Integer cep;

    private String rua;

    private Integer numero;

    private String cidade;

    private String estado;

    public static Endereco createDefaultEndereco() {
        Endereco defaultEndereco = new Endereco();
        defaultEndereco.setCep(0);
        defaultEndereco.setRua("");
        defaultEndereco.setNumero(0);
        defaultEndereco.setCidade("");
        defaultEndereco.setEstado("");

        return defaultEndereco;
    }
}
