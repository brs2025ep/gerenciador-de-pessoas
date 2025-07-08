package com.example.api.application.usecase;

import com.example.api.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletePessoaByCpfUseCase {

    private final PessoaRepository pessoaRepository;

    /**
     * Remove uma nova Pessoa.
     * @param cpf A string cpf da pessoa a ser removida do armazenamento.
     * @return String com Sucesso.
     * @throws ResponseStatusException Estoura a exceção para Pessoa não encontrada.
     */
    public boolean execute(String cpf) {
        if (!pessoaRepository.existsByCpf(cpf)) {
            log.warn("Pessoa com CPF {} não foi encontrada para realizar atualização", cpf);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com CPF " + cpf + " não encontrado para remoção.");
        }

        log.info("Removendo pessoa de CPF: {}", cpf);
        pessoaRepository.deleteByCpf(cpf);

        return true;
    }
}