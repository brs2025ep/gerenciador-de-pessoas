package com.example.backend.application.usecase.cadastro;

import com.example.backend.application.port.out.PessoaEventPort;
import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
