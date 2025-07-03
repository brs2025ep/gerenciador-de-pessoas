package com.example.backend.application.usecase;

import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletePessoaIntegradaByCpfUseCase {

    private final PessoaRepository pessoaRepository;

    /**
     * Remove uma nova Pessoa.
     * @param cpf A string cpf da pessoa a ser removida do armazenamento.
     * @return O domain model da pessoa salva.
     */
    public void execute(String cpf) {
        log.info("Removendo pessoa de CPF: {}", cpf);

        if (!pessoaRepository.existsByCpf(cpf)) {
            log.warn("A Pessoa de CPF {} não foi encontrada para realizar a remoção", cpf);
            throw new ResourceNotFoundException("O CPF deve conter exatamente 11 dígitos!" + cpf);
        }

        pessoaRepository.deleteByCpf(cpf);
    }
}