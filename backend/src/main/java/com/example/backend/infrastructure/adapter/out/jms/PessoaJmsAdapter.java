package com.example.backend.infrastructure.adapter.out.jms;

import com.example.backend.application.port.out.PessoaEventPort;
import com.example.backend.domain.model.Pessoa;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PessoaJmsAdapter implements PessoaEventPort {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendPessoaForValidation(Pessoa pessoa) {
        try {
            String pessoaJson = objectMapper.writeValueAsString(pessoa);
            jmsTemplate.convertAndSend("pessoa.validation.queue", pessoaJson);
            log.info("Pessoa de ID {}  foi enviada para a fila JMS 'pessoa.validation.queue'.", pessoa.getId());
        } catch (Exception e) {
            log.error("Ocorreu um Erro ao enviar produto de ID {} para fila interna: {}", pessoa.getId(), e.getMessage(), e);
            // TODO: Adicionar tratamento de exceção
        }
    }
}