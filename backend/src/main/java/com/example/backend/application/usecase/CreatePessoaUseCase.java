package com.example.backend.application.usecase;

import com.example.backend.application.port.out.PessoaEventPort;
import com.example.backend.domain.model.Pessoa;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePessoaUseCase {

    private final PessoaRepository pessoaRepository;
    private final PessoaEventPort pessoaEventPort;

    /**
     * Cria uma nova Pessoa.
     * @param pessoa A pessoa de domain model para salvar.
     * @return O domain model da pessoa salva.
     * @throws ResourceAlreadyExistsException Exceção lançada ao tentar criar um recurso já existente
     */
    public Pessoa execute(Pessoa pessoa) {
        log.info("Salvando pessoa: {}", pessoa.getNome());
        if (pessoa.getCpf() != null && pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new ResourceAlreadyExistsException("Já existe uma pessoa cadastrada com este CPF.");
        }

        Pessoa createdPessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa criada. (id: {}, nome: {})",  createdPessoa.getId(), pessoa.getNome());

        pessoaEventPort.sendPessoaForValidation(createdPessoa);
        log.debug("Pessoa {} enviada para validação na API.", createdPessoa.getId());

        return createdPessoa;
    }
}
