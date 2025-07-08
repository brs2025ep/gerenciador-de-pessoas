package com.example.backend.application.port.out;

import com.example.backend.domain.model.PessoaCadastro;

public interface PessoaEventPort {
    void sendPessoaCadastroForIntegracao(PessoaCadastro pessoaCadastro);
}
