package com.example.backend.application.usecase;

import com.example.backend.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletePessoaByCpfUseCase {

    private final PessoaRepository pessoaRepository;

    /**
     * Remove uma nova Pessoa.
     * @param cpf A string cpf da pessoa a ser removida do armazenamento.
     * @return O domain model da pessoa salva.
     */
    public void execute(String cpf) {
        log.info("Removendo pessoa de CPF: {}", cpf);
        pessoaRepository.deleteByCpf(cpf);
    }
}