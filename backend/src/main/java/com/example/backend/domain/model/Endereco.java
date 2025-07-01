package com.example.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @OneToOne
    @MapsId
    @JoinColumn(name = "idPessoa")
    @JsonBackReference
    private Pessoa pessoa;

    @Column(nullable = true)
    @Size(min = 8, max = 8, message = "O CEP deve ter 8 dígitos.")
    private Integer cep;

    @Column(nullable = true)
    private String rua;

    @Column(nullable = true)
    private Integer numero;

    @Column(nullable = true)
    private String cidade;

    @Column(nullable = true)
    private String estado;
}
