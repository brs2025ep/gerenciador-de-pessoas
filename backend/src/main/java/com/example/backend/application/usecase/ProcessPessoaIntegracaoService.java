package com.example.backend.application.usecase;

import com.example.backend.application.port.in.ProcessPessoaIntegracaoUseCase;
import com.example.backend.application.port.out.IntegracaoApiPort;
import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.domain.model.SituacaoIntegracao;
import com.example.backend.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessPessoaIntegracaoService implements ProcessPessoaIntegracaoUseCase {

    private final IntegracaoApiPort integracaoApiPort;
    private final PessoaRepository pessoaRepository;

    @Override
    public void processPessoaForIntegration(PessoaCadastro pessoa) {
        log.info("Processar Pessoa de ID {} para API de integração.", pessoa.getId());

        SituacaoIntegracao updatedSituacao = SituacaoIntegracao.ERRO;

        try {
            if (!pessoa.getSituacaoIntegracao().equals(SituacaoIntegracao.SUCESSO)) {
                log.debug("A Pessoa de ID {} está integrada. Realizando atualização via PUT.", pessoa.getId());
                integracaoApiPort.updatePessoa(pessoa);
            } else {
                log.debug("Pessoa de ID {} não foi integrada. Realizando nova integração via POST.", pessoa.getId());
                integracaoApiPort.createPessoa(pessoa);
            }
            updatedSituacao = SituacaoIntegracao.SUCESSO;
            log.info("A API de integração teve êxito para integrar a Pessoa de ID {}. O status será atualizado para SUCCESSO.", pessoa.getId());
        } catch (Exception e) {
            updatedSituacao = SituacaoIntegracao.ERRO;
            log.error("A API de integração falhou na integração da pessoa de ID {}. Erro: {}", pessoa.getId(), e.getMessage());
        } finally {
            pessoa.setSituacaoIntegracao(updatedSituacao);
            pessoaRepository.save(pessoa);
            log.debug("Pessoa com ID {} agora tem status {}.", pessoa.getId(), updatedSituacao.getValue());
        }
    }
}