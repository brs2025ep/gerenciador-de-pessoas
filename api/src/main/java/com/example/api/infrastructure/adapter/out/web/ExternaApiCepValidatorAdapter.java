package com.example.api.infrastructure.adapter.out.web;

import com.example.api.application.port.out.ExternalApiCepValidatorPort;
import com.example.api.infrastructure.adapter.out.web.exception.CepInvalidoException;
import com.example.api.infrastructure.adapter.out.web.exception.CepSemFormatoValidoException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExternaApiCepValidatorAdapter implements ExternalApiCepValidatorPort {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.cep.validation.url}")
    private final String cepValidationApiUrl;

    /**
     * Verifica a validade do CEP.
     *
     * @param cep O número do código CEP a ser validado.
     * @return String Retorna um json contendo keys do endereço, se o CEP é válido, ou key "erro" se o CEP é inválido.
     * @throws CepInvalidoException Exceção lançada quando o CEP tem formato correto e é inválido.
     * @throws CepSemFormatoValidoException Exceção lançada para cep com formatação incorreta
     */
    @Override
    public void sendCepForValidation(Integer cep) {
        try {
            log.info("O CEP {} foi recebido para validação na API {}", cep, cepValidationApiUrl);

            ResponseEntity<String> response = restTemplate.getForEntity(
                    cepValidationApiUrl,
                    String.class,
                    cep
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String jsonResponse = response.getBody();
                JsonNode rootNode = objectMapper.readTree(jsonResponse);

                if (rootNode.has("erro")) {
                    log.info("O CEP {} foi considerado inválido!", cep);
                    throw new CepInvalidoException("A validação considerou inválido o CEP: " + cep);
                } else {
                    log.info("O CEP {} foi considerado válido!", cep);
                    return;
                }
            }

            log.warn("Resposta inesperada: {}", response.getStatusCode());
            throw new RuntimeException("Resposta inesperada do serviço de validação externa: " + response.getStatusCode());
        } catch (HttpClientErrorException.BadRequest e) {
            log.warn("O CEP {} tem formato inválido!", cep);
            throw new CepSemFormatoValidoException("Validação externa falhou: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.warn("Ocorreu uma exceção: {}", e.getMessage());
            throw new RuntimeException("Falha na comunicação com a API de validação externa: " + e.getMessage(), e);
        }
    }
}