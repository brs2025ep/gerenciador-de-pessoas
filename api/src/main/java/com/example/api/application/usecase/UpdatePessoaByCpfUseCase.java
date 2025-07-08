package com.example.api.application.usecase;

import com.example.api.domain.model.Pessoa;
import com.example.api.domain.repository.PessoaRepository;
import com.example.api.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import com.example.api.infrastructure.adapter.out.web.ExternaApiCepValidatorAdapter;
import com.example.api.infrastructure.adapter.out.web.exception.CepInvalidoException;
import com.example.api.infrastructure.adapter.out.web.exception.CepSemFormatoValidoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePessoaByCpfUseCase {

    private final PessoaRepository pessoaRepository;
    private final ExternaApiCepValidatorAdapter externaApiCepValidatorAdapter;

    /**
     * Atualiza uma Pessoa.
     * @param cpf A string cpf da pessoa a ser atualizada.
     * @param editedPessoa A pessoa com dados atualizados.
     * @return Retorna a model Pessoa.
     * @throws CepInvalidoException exception para CEP inválido.
     * @throws CepSemFormatoValidoException exception para CEP sem formato válido.
     */
    public Pessoa execute(String cpf, Pessoa editedPessoa) {
        log.info("Pessoa de CPF {} recebida para atualização!", cpf);

        externaApiCepValidatorAdapter.sendCepForValidation(editedPessoa.getEndereco().getCep());

        log.info("Atualizando pessoa de CPF: {}", cpf);
        Optional<Pessoa> updatedPessoa = pessoaRepository.findByCpf(cpf).map(existing -> {
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
        });

        return updatedPessoa.orElseThrow(() -> new ResourceNotFoundException("Pessoa no Optional tem valor nulo"));
    }
}
