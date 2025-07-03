package com.example.backend.application.usecase;

import com.example.backend.domain.model.Pessoa;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPessoaIntegradaByCpfUseCase {

    private final PessoaRepository pessoaRepository;

    /**
     * Busca uma Pessoa.
     * @param cpf A string cpf da pessoa a ser buscada no armazenamento.
     * @return Optional de Pessoa.
     */
    public Pessoa execute(String cpf) {
        log.info("Obtendo pessoa de CPF: {}", cpf);

        if (!pessoaRepository.existsByCpf(cpf)) {
            log.warn("A Pessoa de CPF {} não foi encontrada para realizar a remoção", cpf);
            throw new ResourceNotFoundException("O CPF deve conter exatamente 11 dígitos!");
        }

        Pessoa pessoa = pessoaRepository.findByCpf(cpf).orElseThrow(() -> {
            log.warn("A Pessoa de CPF {} não encontrada", cpf);
            return new ResourceNotFoundException("O CPF deve conter exatamente 11 dígitos!" + cpf);
        });

        return pessoa;
    }
}
