package com.example.backend.application.port.out;

import com.example.backend.domain.model.PessoaCadastro;

public interface IntegracaoApiPort {
    void createPessoa(PessoaCadastro pessoa);
    void updatePessoa(PessoaCadastro pessoa);
}