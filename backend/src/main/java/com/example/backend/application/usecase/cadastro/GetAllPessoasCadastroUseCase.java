package com.example.backend.application.usecase.cadastro;

import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllPessoasCadastroUseCase {

    private final PessoaRepository pessoaRepository;

    /**
     * Obtem uma página de Pessoa.
     * @param pageable Um objeto Pageable.
     * @return Página de Pessoa.
     */
    public Page<PessoaCadastro> execute(Pageable pageable) {
        log.info("Obtendo todas as Pessoas: {}", pageable);
        return pessoaRepository.findAllPessoasWithEndereco(pageable);
    }
}
