package com.example.api.application.usecase;

import com.example.api.domain.model.Pessoa;
import com.example.api.domain.repository.PessoaRepository;
import com.example.api.infrastructure.adapter.out.web.ExternaApiCepValidatorAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePessoaUseCase {

    private final PessoaRepository pessoaRepository;
    private final ExternaApiCepValidatorAdapter externaApiCepValidatorAdapter;

    /**
     * Cria uma nova Pessoa.
     * @param pessoa A pessoa de domain model para salvar.
     * @return O domain model da pessoa salva.
     */
    public Pessoa execute(Pessoa pessoa) {
        log.info("Pessoa de nome {} recebida para ser criada!", pessoa.getNome());

        log.info("Verificando CEP {}!", pessoa.getEndereco().getCep());



        log.info("Salvando pessoa: {}", pessoa.getNome());


        log.info("Salvando pessoa: {}", pessoa.getNome());
        Pessoa createdPessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa criada. (id: {}, nome: {})",  createdPessoa.getId(), pessoa.getNome());

        return createdPessoa;
    }
}
