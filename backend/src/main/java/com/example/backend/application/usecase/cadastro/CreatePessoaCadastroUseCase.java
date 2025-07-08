package com.example.backend.application.usecase.cadastro;

import com.example.backend.application.port.out.PessoaEventPort;
import com.example.backend.domain.model.Endereco;
import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceAlreadyExistsException;
import com.example.backend.infrastructure.adapter.in.web.exception.UnprocessableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePessoaCadastroUseCase {

    private final PessoaRepository pessoaRepository;
    private final PessoaEventPort pessoaEventPort;

    /**
     * Cria uma nova Pessoa.
     * @param pessoaCadastro A pessoa de domain model para salvar.
     * @return O domain model da pessoa salva.
     * @throws ResourceAlreadyExistsException Exceção lançada ao tentar criar um recurso já existente
     */
    public PessoaCadastro execute(PessoaCadastro pessoaCadastro) {
        log.info("Salvando pessoa: {}", pessoaCadastro.getNome());

        if (pessoaCadastro.getNascimento() != null) {
            if (pessoaCadastro.getNascimento().isAfter(LocalDate.now())) {
                log.error("A data de nascimento informada está no futuro!");
                throw new UnprocessableEntity("A data de nascimento informada está no futuro!");
            }
        }

        boolean hasCpf = false;

        if (pessoaCadastro.getCpf() != null) {
            if (!pessoaCadastro.getCpf().matches("^\\d{11}$")) {
                log.error("O CPF deve conter exatamente 11 dígitos!");
                throw new UnprocessableEntity("O CPF deve conter exatamente 11 dígitos!");
            }

            if (pessoaRepository.existsByCpf(pessoaCadastro.getCpf())) {
                throw new ResourceAlreadyExistsException("Já existe uma pessoa cadastrada com este CPF.");
            }
            hasCpf = true;
        }

        if (pessoaCadastro.getEndereco() == null) {
            Endereco endereco = new Endereco();
            endereco.setRua("");
            endereco.setEstado("");
            endereco.setCidade("");
            endereco.setCep(0);
            endereco.setNumero(0);
            pessoaCadastro.setEndereco(endereco);
        } else {
            Endereco endereco = pessoaCadastro.getEndereco();
            endereco.setPessoa(null);
            endereco.setId(null);
            pessoaCadastro.setEndereco(endereco);
        }

        PessoaCadastro createdPessoaCadastro = pessoaRepository.save(pessoaCadastro);
        log.info("Pessoa criada. (id: {}, nome: {})",  createdPessoaCadastro.getId(), pessoaCadastro.getNome());

        if (hasCpf) {
            pessoaEventPort.sendPessoaCadastroForIntegracao(createdPessoaCadastro);
            log.debug("Pessoa de cpf {} enviada para validação na API.", createdPessoaCadastro.getCpf());
        }

        return createdPessoaCadastro;
    }
}
