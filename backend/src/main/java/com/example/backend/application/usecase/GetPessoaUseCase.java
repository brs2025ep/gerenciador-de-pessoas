package com.example.backend.application.usecase;

import com.example.backend.domain.model.Pessoa;
import com.example.backend.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPessoaUseCase {

    private final PessoaRepository pessoaRepository;

    /**
     * Busca uma Pessoa.
     * @param id ID da pessoa a ser buscada no armazenamento.
     * @return Optional de Pessoa.
     */
    public Optional<Pessoa> execute(Integer id) {
        log.info("Obtendo pessoa de CPF: {}", id);
        return pessoaRepository.findById(id);
    }
}
