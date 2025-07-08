package com.example.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name="Endereco")
public class Endereco {

    @Id
    @Column(name = "idPessoa")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idPessoa")
    @JsonBackReference
    private PessoaCadastro pessoa;

    @Column(nullable = true)
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
