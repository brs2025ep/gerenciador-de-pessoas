package com.example.backend.infrastructure.dto;

import com.example.backend.domain.model.Pessoa;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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