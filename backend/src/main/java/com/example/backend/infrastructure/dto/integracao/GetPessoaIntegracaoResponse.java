package com.example.backend.infrastructure.dto.integracao;

import com.example.backend.infrastructure.dto.EnderecoResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPessoaIntegracaoResponse {
    private String nome;
    private LocalDate dataNascimento;
    private String cpf;
    private String email;
    private EnderecoResponse endereco;
    private LocalDateTime dataHoraInclusaoRegistro;
    private LocalDateTime dataHoraUltimaAlteracaoRegistro;
}