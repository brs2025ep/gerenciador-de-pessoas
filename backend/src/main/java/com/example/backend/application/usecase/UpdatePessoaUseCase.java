package com.example.backend.application.usecase;

import com.example.backend.application.port.out.PessoaEventPort;
import com.example.backend.domain.model.Pessoa;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePessoaUseCase {

    private final PessoaRepository pessoaRepository;
    private final PessoaEventPort pessoaEventPort;

    /**
     * Atualiza uma Pessoa.
     * @param id A string cpf da pessoa a ser atualizada.
     * @param editedPessoa A pessoa com dados atualizados.
     * @return Optional da model Pessoa.
     */
    public Pessoa execute(Integer id, Pessoa editedPessoa) {
        log.info("Atualizando pessoa de CPF: {}", id);
        
        Optional<Pessoa> updatedPessoa = pessoaRepository.findById(id).map(existing -> {
            existing.setNome(editedPessoa.getNome());
            existing.setCpf(editedPessoa.getCpf());
            existing.setEndereco(editedPessoa.getEndereco());
            existing.setEmail(editedPessoa.getEmail());
            existing.setNascimento(editedPessoa.getNascimento());

            if (editedPessoa.getEndereco() != null) {
                editedPessoa.getEndereco().setPessoa(existing);
                existing.setEndereco(editedPessoa.getEndereco());
            }

            return pessoaRepository.save(existing);
        });

        updatedPessoa.ifPresent(pessoa -> {
            log.info("Pessoa criada. (id: {})", pessoa.getId());
            pessoaEventPort.sendPessoaForValidation(pessoa);
            log.debug("Pessoa {} enviada para validação na API.", pessoa.getId());

        });

        return updatedPessoa.orElseThrow(() -> new ResourceNotFoundException("Pessoa no Optional tem valor nulo"));
    }
}
