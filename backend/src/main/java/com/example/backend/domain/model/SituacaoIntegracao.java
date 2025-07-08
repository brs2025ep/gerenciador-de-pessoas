package com.example.backend.domain.model;

public enum SituacaoIntegracao {
    PENDENTE("PENDENTE"),
    SUCESSO("SUCESSO"),
    ERRO("ERRO");

    private final String value;

    SituacaoIntegracao(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
