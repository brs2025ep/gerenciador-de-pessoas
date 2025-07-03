package com.example.backend.infrastructure.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PessoaIntegradaResponse {
    private Integer id;
    private String nome;
    private LocalDate nascimento;
    private String cpf;
    private String email;
    private EnderecoResponse endereco;
    private LocalDateTime dataHoraInclusaoRegistro;
}