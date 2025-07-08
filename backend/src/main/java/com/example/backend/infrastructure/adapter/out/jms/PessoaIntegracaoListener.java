package com.example.backend.infrastructure.adapter.out.jms;

import com.example.backend.application.port.in.ProcessPessoaIntegracaoUseCase;
import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.domain.model.SituacaoIntegracao;
import com.example.backend.domain.repository.PessoaRepository;
import com.example.backend.infrastructure.dto.integracao.SavePessoaIntegracaoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class PessoaIntegracaoListener {

    private final ObjectMapper objectMapper;
    private final ProcessPessoaIntegracaoUseCase processPessoaIntegracaoUseCase;

    @JmsListener(destination = "integracao.queue")
    public void receivePessoaForIntegracao(String pessoaJson) {
        try {
            PessoaCadastro pessoa = objectMapper.readValue(pessoaJson, PessoaCadastro.class);
            log.info("A fila JMS recebeu a Pessoa com ID {} para integração.", pessoa.getId());
            processPessoaIntegracaoUseCase.processPessoaForIntegration(pessoa);
        } catch (Exception e) {
            log.error("Error processing JMS message: {}", e.getMessage(), e);
            // TODO: Criar uma Dead Letter Queue (DLQ) para falhas
        }
    }
}
