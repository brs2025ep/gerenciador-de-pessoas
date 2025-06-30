package com.example.backend.infrastructure.adapter.out.jms;

import com.example.backend.domain.model.Pessoa;
import com.example.backend.domain.model.SituacaoIntegracao;
import com.example.backend.domain.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductValidationListener {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PessoaRepository pessoaRepository;

    @Value("${api.url}")
    private final String apiUrl;

    @JmsListener(destination = "pessoa.validation.queue")
    public void receivePessoaForValidation(String pessoaJson) {
        try {
            Pessoa pessoa = objectMapper.readValue(pessoaJson, Pessoa.class);
            log.info("A Fila JMS recebeu a Pessoa de ID {} para validação na API.", pessoa.getId());

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, pessoa, String.class);

            SituacaoIntegracao updatedSituacao = SituacaoIntegracao.ERRO;

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                updatedSituacao = SituacaoIntegracao.SUCESSO;
                log.info("Sucesso! A API validou com sucesso a Pessoa de ID {}, que será atualizada com a nova situação: {}",
                        pessoa.getId(), updatedSituacao.getValue());
            } else {
                log.warn("A API não validou com sucesso a Pessoa de ID {}. Status HTTP da resposta: {}. Corpo da resposta: {}",
                        pessoa.getId(), response.getStatusCode(), response.getBody());
            };

            pessoa.setSituacaoIntegracao(updatedSituacao);
            pessoaRepository.save(pessoa);
            log.debug("A pessoa com ID {} recebeu atualização da situação de integração para {}.", pessoa.getId(), updatedSituacao.getValue());
        } catch (Exception e) {
            log.error("Erro ao processar mensagem JMS ou durante requisição HTTP: {}", e.getMessage(), e); // Loga a exceção
            // TODO: Adicionar Dead Letter Queue (DLQ)
        }
    }
}
