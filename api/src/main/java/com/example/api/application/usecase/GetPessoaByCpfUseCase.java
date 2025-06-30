package com.example.api.application.usecase;

import com.example.api.domain.model.Pessoa;
import com.example.api.domain.repository.PessoaRepository;
import com.example.api.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import com.example.api.infrastructure.dto.PessoaGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPessoaByCpfUseCase {

    private final PessoaRepository pessoaRepository;

    /**
     * Busca uma Pessoa.
     * @param cpf A string cpf da pessoa a ser buscada no armazenamento.
     * @return Optional de Pessoa.
     */
    public Optional<Pessoa> execute(String cpf) {
        log.info("Obtendo pessoa de CPF: {}", cpf);
        return pessoaRepository.findByCpf(cpf);
    }
}
