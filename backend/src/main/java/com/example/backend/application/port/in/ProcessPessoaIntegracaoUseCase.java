package com.example.backend.application.port.in;

import com.example.backend.domain.model.PessoaCadastro;

public interface ProcessPessoaIntegracaoUseCase {
    void processPessoaForIntegration(PessoaCadastro pessoa);
}