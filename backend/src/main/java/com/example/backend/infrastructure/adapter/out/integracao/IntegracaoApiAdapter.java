package com.example.backend.infrastructure.adapter.out.integracao;

import com.example.backend.application.port.out.IntegracaoApiPort;
import com.example.backend.domain.model.PessoaCadastro;
import com.example.backend.infrastructure.dto.integracao.SavePessoaIntegracaoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class IntegracaoApiAdapter implements IntegracaoApiPort {

    private final RestTemplate restTemplate;

    @Value("${api.url}")
    private String apiUrl;

    @Override
    public void createPessoa(PessoaCadastro pessoa) {
        SavePessoaIntegracaoRequest saveRequest = SavePessoaIntegracaoRequest.fromDomain(pessoa);
        log.debug("Realizando criação da integração com POST para a API. Pessoa de ID {}.", pessoa.getId());
        ResponseEntity<String> response = restTemplate.postForEntity(
                apiUrl,
                saveRequest,
                String.class
        );
        handleResponse(pessoa.getId(), response);
    }

    @Override
    public void updatePessoa(PessoaCadastro pessoa) {
        SavePessoaIntegracaoRequest saveRequest = SavePessoaIntegracaoRequest.fromDomain(pessoa);
        String putUrl = apiUrl + "/" + pessoa.getCpf();
        log.debug("Realizando atualização da integração com PUT para a API Pessoa de ID {} com endereço: {}", pessoa.getId(), putUrl);
        ResponseEntity<String> response = restTemplate.exchange(
                putUrl,
                HttpMethod.PUT,
                new HttpEntity<>(saveRequest),
                String.class
        );
        handleResponse(pessoa.getId(), response);
    }

    private void handleResponse(Integer pessoaId, ResponseEntity<String> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("API call successful for Pessoa ID {}. Status: {}", pessoaId, response.getStatusCode());
        } else {
            log.error("Ocorreu uma falha na execução da integração da Pessoa de ID {}. Status: {}, Body: {}",
                    pessoaId, response.getStatusCode(), response.getBody());
            throw new RuntimeException("API integration failed with status: " + response.getStatusCode());
        }
    }
}