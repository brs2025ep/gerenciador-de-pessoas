package com.example.backend.infrastructure.dto.integracao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SavePessoaIntegracaoResponse {
    private Integer idPessoa;
    private String mensagem;
}
