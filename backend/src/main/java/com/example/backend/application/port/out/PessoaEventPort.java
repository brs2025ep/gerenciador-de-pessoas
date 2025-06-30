package com.example.backend.application.port.out;

import com.example.backend.domain.model.Pessoa;

public interface PessoaEventPort {
    void sendPessoaForValidation(Pessoa pessoa);
}
