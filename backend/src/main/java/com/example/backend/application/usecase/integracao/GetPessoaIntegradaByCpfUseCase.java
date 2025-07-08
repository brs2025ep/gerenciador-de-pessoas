package com.example.backend.application.usecase.integracao;

import com.example.backend.application.port.out.PessoaIntegracaoPort;
import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.domain.model.SituacaoIntegracao;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import com.example.backend.infrastructure.dto.integracao.GetPessoaIntegracaoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPessoaIntegradaByCpfUseCase {

    private final PessoaRepository pessoaRepository;
    private final PessoaIntegracaoPort pessoaIntegracaoPort;

    /**
     * Busca uma Pessoa.
     * @param cpf A string cpf da pessoa a ser buscada no armazenamento.
     * @return Optional de Pessoa.
     */
    public GetPessoaIntegracaoResponse execute(String cpf) {
        log.info("Obtendo pessoa de CPF: {}", cpf);

        if (!pessoaRepository.existsByCpf(cpf)) {
            log.warn("A Pessoa de CPF {} não foi encontrada para realizar a remoção", cpf);
            throw new ResourceNotFoundException("O CPF deve conter exatamente 11 dígitos!");
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

        GetPessoaIntegracaoResponse pessoaIntegrada = pessoaIntegracaoPort.getPessoaIntegrada(cpf).orElseThrow(
                () -> {
                    log.warn("A Integração da Pessoa de CPF {} não foi encontrada", cpf);
                    throw new ResourceNotFoundException("O CPF deve conter exatamente 11 dígitos!" + cpf);
                }
        );

        return pessoaIntegrada;
    }
}