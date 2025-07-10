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
            if (pessoaCadastro.getNascimento().isAfter(LocalDate.now())) { // Verificar se a data de nascimento é futura
                log.error("A data de nascimento informada está no futuro!");
                throw new UnprocessableEntity("A data de nascimento informada está no futuro!");
            }
        }

        if (pessoaCadastro.getCpf() != null) {
            if (!pessoaCadastro.getCpf().matches("^\\d{11}$")) {  // Verificar se o CPF tem 11 dígitos
                log.error("O CPF deve conter exatamente 11 dígitos!");
                throw new UnprocessableEntity("O CPF deve conter exatamente 11 dígitos!");
            }

            if (pessoaRepository.existsByCpf(pessoaCadastro.getCpf())) {
                throw new ResourceAlreadyExistsException("Já existe uma pessoa cadastrada com este CPF.");
            }
        }

        if (pessoaCadastro.getEndereco() == null) { // Verificar se o Endereço é nulo
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

        boolean isPessoaCadastroComplete = // Verificar se a Pessoa tem todas as informações preenchidas
                pessoaCadastro.getNome() != null && !pessoaCadastro.getNome().isEmpty() &&
                        pessoaCadastro.getCpf() != null && pessoaCadastro.getCpf().length() == 11 &&
                        pessoaCadastro.getEmail() != null && !pessoaCadastro.getEmail().isEmpty() &&
                        pessoaCadastro.getEndereco() != null &&
                        pessoaCadastro.getEndereco().getCep() != null &&
                        pessoaCadastro.getEndereco().getRua() != null && !pessoaCadastro.getEndereco().getRua().isEmpty() &&
                        pessoaCadastro.getEndereco().getCidade() != null && !pessoaCadastro.getEndereco().getCidade().isEmpty() &&
                        pessoaCadastro.getEndereco().getEstado() != null && !pessoaCadastro.getEndereco().getCidade().isEmpty() &&
                        pessoaCadastro.getEndereco().getNumero() != null;

        PessoaCadastro createdPessoaCadastro = pessoaRepository.save(pessoaCadastro);
        log.info("Pessoa criada. (id: {}, nome: {})",  createdPessoaCadastro.getId(), pessoaCadastro.getNome());

        if (isPessoaCadastroComplete) {
            pessoaEventPort.sendPessoaCadastroForIntegracao(createdPessoaCadastro);
            log.debug("Pessoa de cpf {} enviada para validação na API.", createdPessoaCadastro.getCpf());
        }

        return createdPessoaCadastro;
    }
}
