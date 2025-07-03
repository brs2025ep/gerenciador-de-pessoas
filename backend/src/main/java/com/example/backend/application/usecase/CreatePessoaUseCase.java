package com.example.backend.application.usecase;

import com.example.backend.application.port.out.PessoaEventPort;
import com.example.backend.domain.model.Endereco;
import com.example.backend.domain.model.Pessoa;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceAlreadyExistsException;
import com.example.backend.infrastructure.adapter.in.web.exception.UnprocessableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;

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

        if (pessoa.getNascimento() != null) {
            if (pessoa.getNascimento().isAfter(LocalDate.now())) {
                log.error("A data de nascimento informada está no futuro!");
                throw new UnprocessableEntity("A data de nascimento informada está no futuro!");
            }
        }

        if (pessoa.getCpf() != null) {
            if (!pessoa.getCpf().matches("^\\d{11}$")) {
                log.error("O CPF deve conter exatamente 11 números!");
                throw new UnprocessableEntity("O CPF deve conter exatamente 11 números!");
            }
        }

        if (pessoa.getCpf() != null && pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new ResourceAlreadyExistsException("Já existe uma pessoa cadastrada com este CPF.");
        }

        if (pessoa.getEndereco() == null) {
            Endereco endereco = new Endereco();
            endereco.setRua("");
            endereco.setEstado("");
            endereco.setCidade("");
            endereco.setCep(0);
            endereco.setNumero(0);
            pessoa.setEndereco(endereco);
        } else {
            Endereco endereco = pessoa.getEndereco();
            endereco.setPessoa(null);
            endereco.setId(null);
            pessoa.setEndereco(endereco);
        }

        Pessoa createdPessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa criada. (id: {}, nome: {})",  createdPessoa.getId(), pessoa.getNome());

        pessoaEventPort.sendPessoaForValidation(createdPessoa);
        log.debug("Pessoa {} enviada para validação na API.", createdPessoa.getId());

        return createdPessoa;
    }
}
