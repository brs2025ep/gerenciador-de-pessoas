package com.example.backend.application.usecase.cadastro;

import com.example.backend.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletePessoaCadastroUseCase {

    private final PessoaRepository pessoaRepository;

    /**
     * Remove uma nova Pessoa.
     * @param id ID da pessoa a ser removida do armazenamento.
     * @return O domain model da pessoa salva.
     */
    public void execute(Integer id) {
        log.info("Removendo pessoa de ID: {}", id);
        pessoaRepository.deleteById(id);
    }
}