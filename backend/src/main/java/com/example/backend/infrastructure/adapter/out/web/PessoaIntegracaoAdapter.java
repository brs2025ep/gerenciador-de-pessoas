package com.example.backend.infrastructure.adapter.out.web;

import com.example.backend.application.port.out.PessoaIntegracaoPort;
import com.example.backend.infrastructure.dto.integracao.DeletePessoaIntegracaoResponse;
import com.example.backend.infrastructure.dto.integracao.GetPessoaIntegracaoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Implementação de PessoaIntegradaPort,
 * com responsabilidade de se comunicar com a api externa da aplicação API para realizar integração.
 */
@Component
public class PessoaIntegracaoAdapter implements PessoaIntegracaoPort {

    private static final Logger log = LoggerFactory.getLogger(PessoaIntegracaoAdapter.class);
    private final RestTemplate restTemplate;

    @Value("${api.url}")
    private String apiUrl;

    public PessoaIntegracaoAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<GetPessoaIntegracaoResponse> getPessoaIntegrada(String cpf) {
        String url = apiUrl + "/" + cpf;
        log.info("Attempting to fetch integrated person from URL: {}", url);

        try {
            GetPessoaIntegracaoResponse response = restTemplate.getForObject(url, GetPessoaIntegracaoResponse.class);

            log.info("Successfully fetched integrated person for CPF {}.", cpf);
            return Optional.ofNullable(response);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Integrated person with CPF {} not found at {}. Status: {}", cpf, url, e.getStatusCode());
                return Optional.empty();
            } else {
                log.error("Client error fetching integrated person for CPF {} from {}: {} - {}",
                        cpf, url, e.getStatusCode(), e.getResponseBodyAsString());
                throw new RuntimeException("Failed to fetch integrated person due to client error: " + e.getMessage(), e);
            }
        } catch (RestClientException e) {
            log.error("Error fetching integrated person for CPF {} from {}: {}", cpf, url, e.getMessage(), e);
            throw new RuntimeException("Failed to connect to integrated person service: " + e.getMessage(), e);
        }
    }

    @Override
    public DeletePessoaIntegracaoResponse deletePessoaIntegrada(String cpf) {
        String url = apiUrl + "/" + cpf;
        log.info("Iniciando remoção da Pessoa na Integração");

        ResponseEntity<DeletePessoaIntegracaoResponse> responseEntity =
                restTemplate.exchange(url, HttpMethod.DELETE, null, DeletePessoaIntegracaoResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("A Pessoa de CPF {} foi removida com sucesso. Status: {}", cpf, responseEntity.getStatusCode());
            return responseEntity.getBody();
        } else {
            log.warn("Ocorreu um erro inesperado ao remover a Pessoa de CPF {} da integração. Status: {}", cpf, responseEntity.getStatusCode());
            throw new RuntimeException("Uma exceção inesperada ocorreu ao remover a pessoa de CPF " + cpf + " da integração. Status: " + responseEntity.getStatusCode());
        }
    }
}