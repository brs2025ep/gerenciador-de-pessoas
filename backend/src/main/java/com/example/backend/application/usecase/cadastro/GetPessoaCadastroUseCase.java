package com.example.backend.application.usecase.cadastro;

import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPessoaCadastroUseCase {

    private final PessoaRepository pessoaRepository;

    /**
     * Busca uma Pessoa.
     * @param id ID da pessoa a ser buscada no armazenamento.
     * @return Optional de Pessoa.
     */
    public Optional<PessoaCadastro> execute(Integer id) {
        log.info("Obtendo pessoa de ID: {}", id);
        return pessoaRepository.findById(id);
    }
}
