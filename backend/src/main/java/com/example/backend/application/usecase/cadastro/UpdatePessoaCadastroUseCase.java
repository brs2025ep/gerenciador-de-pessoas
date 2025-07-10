package com.example.backend.application.usecase.cadastro;

import com.example.backend.application.port.out.PessoaEventPort;
import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceAlreadyExistsException;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import com.example.backend.infrastructure.adapter.in.web.exception.UnprocessableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePessoaCadastroUseCase {

    private final PessoaRepository pessoaRepository;
    private final PessoaEventPort pessoaEventPort;

    /**
     * Atualiza uma Pessoa.
     * @param id A string cpf da pessoa a ser atualizada.
     * @param editedPessoaCadastro A pessoa com dados atualizados.
     * @return Optional da model Pessoa.
     */
    public PessoaCadastro execute(Integer id, PessoaCadastro editedPessoaCadastro) {
        log.info("Atualizando pessoa de ID: {}", id);


        if (editedPessoaCadastro.getNascimento() != null) {
            if (editedPessoaCadastro.getNascimento().isAfter(LocalDate.now())) { // Verificar se a data de nascimento é futura
                log.error("A data de nascimento informada está no futuro!");
                throw new UnprocessableEntity("A data de nascimento informada está no futuro!");
            }
        }

        if (editedPessoaCadastro.getCpf() != null) {
            if (!editedPessoaCadastro.getCpf().matches("^\\d{11}$")) {  // Verificar se o CPF tem 11 dígitos
                log.error("O CPF deve conter exatamente 11 dígitos!");
                throw new UnprocessableEntity("O CPF deve conter exatamente 11 dígitos!");
            }

        }

        boolean isPessoaCadastroComplete = // Verificar se a Pessoa tem todas as informações preenchidas
                editedPessoaCadastro.getNome() != null && !editedPessoaCadastro.getNome().isEmpty() &&
                        editedPessoaCadastro.getCpf() != null && editedPessoaCadastro.getCpf().length() == 11 &&
                        editedPessoaCadastro.getEmail() != null && !editedPessoaCadastro.getEmail().isEmpty() &&
                        editedPessoaCadastro.getEndereco() != null &&
                        editedPessoaCadastro.getEndereco().getCep() != null &&
                        editedPessoaCadastro.getEndereco().getRua() != null && !editedPessoaCadastro.getEndereco().getRua().isEmpty() &&
                        editedPessoaCadastro.getEndereco().getCidade() != null && !editedPessoaCadastro.getEndereco().getCidade().isEmpty() &&
                        editedPessoaCadastro.getEndereco().getEstado() != null && !editedPessoaCadastro.getEndereco().getCidade().isEmpty() &&
                        editedPessoaCadastro.getEndereco().getNumero() != null;
        
        Optional<PessoaCadastro> updatedPessoa = pessoaRepository.findById(id).map(existing -> {
            existing.setNome(editedPessoaCadastro.getNome());
            existing.setCpf(editedPessoaCadastro.getCpf());
            existing.setEndereco(editedPessoaCadastro.getEndereco());
            existing.setEmail(editedPessoaCadastro.getEmail());
            existing.setNascimento(editedPessoaCadastro.getNascimento());

            if (editedPessoaCadastro.getEndereco() != null) {
                editedPessoaCadastro.getEndereco().setPessoa(existing);
                existing.setEndereco(editedPessoaCadastro.getEndereco());
            }

            return pessoaRepository.save(existing);
        });

        updatedPessoa.ifPresent(pessoa -> {
            log.info("Pessoa criada. (id: {})", pessoa.getId());
            pessoaEventPort.sendPessoaCadastroForIntegracao(pessoa);
            log.debug("Pessoa {} enviada para validação na API.", pessoa.getId());

        });

        return updatedPessoa.orElseThrow(() -> new ResourceNotFoundException("Pessoa no Optional tem valor nulo"));
    }
}
