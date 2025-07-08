package com.example.backend.application.port.out;

import com.example.backend.infrastructure.dto.integracao.DeletePessoaIntegracaoResponse;
import com.example.backend.infrastructure.dto.integracao.GetPessoaIntegracaoResponse;

import java.util.Optional;

/**
 * Interface para obter ou remover uma Pessoa Integrada do sistema externo.
 */
public interface PessoaIntegracaoPort {

    /**
     * Obtenha uma pessoa Integrada com a API de integração usando CPF.
     *
     * @param cpf The CPF of the person to retrieve.
     * @return An Optional containing PessoaIntegrada if found, or empty if not.
     */
    Optional<GetPessoaIntegracaoResponse> getPessoaIntegrada(String cpf);

    /**
     * Remove Pessoa Integrada com a API de integração usando CPF.
     *
     * @param cpf The CPF of the person to delete.
     * @return DeletePessoaIntegradaResponse indicating the outcome of the delete operation.
     */
    DeletePessoaIntegracaoResponse deletePessoaIntegrada(String cpf);
}