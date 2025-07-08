package com.example.backend.application.usecase.integracao;

import com.example.backend.application.port.out.PessoaIntegracaoPort;
import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.domain.model.SituacaoIntegracao;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import com.example.backend.infrastructure.dto.integracao.DeletePessoaIntegracaoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletePessoaIntegradaByCpfUseCase {

    private final PessoaRepository pessoaRepository;
    private final PessoaIntegracaoPort pessoaIntegracaoPort;

    /**
     * Remove uma nova Pessoa.
     * @param cpf A string cpf da pessoa a ser removida do armazenamento.
     * @return O domain model da pessoa salva.
     */
    public DeletePessoaIntegracaoResponse execute(String cpf) {
        log.info("Removendo pessoa de CPF: {}", cpf);

        if (!pessoaRepository.existsByCpf(cpf)) {
            log.warn("A Pessoa de CPF {} não foi encontrada para realizar a remoção", cpf);
            throw new ResourceNotFoundException("Não foi encontrada pessoa com CPF informado!" + cpf);
        }

        PessoaCadastro pessoaCadastro = pessoaRepository.findByCpf(cpf).orElseThrow(() -> {
            log.warn("A Pessoa de CPF {} não encontrada", cpf);
            return new ResourceNotFoundException("O CPF deve conter exatamente 11 dígitos!" + cpf);
        });

        if (pessoaCadastro.getSituacaoIntegracao() != SituacaoIntegracao.SUCESSO) {
            log.warn("A Pessoa de CPF {} não foi integrada e tem situação {}", cpf, pessoaCadastro.getSituacaoIntegracao());
            throw new ResourceNotFoundException("A pessoa  de " + cpf + " + a ser obtida não foi integrada e está com situação" + pessoaCadastro.getSituacaoIntegracao());
        }

        log.info("Pessoa possui Integração. (id: {}, nome: {})",  pessoaCadastro.getId());

        DeletePessoaIntegracaoResponse response = pessoaIntegracaoPort.deletePessoaIntegrada(cpf);

        return response;
    }
}