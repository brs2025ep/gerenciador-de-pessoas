package com.example.backend.infrastructure.adapter.in.web;

import com.example.backend.application.usecase.DeletePessoaIntegradaByCpfUseCase;
import com.example.backend.application.usecase.GetPessoaIntegradaByCpfUseCase;
import com.example.backend.domain.model.Pessoa;
import com.example.backend.infrastructure.dto.PessoaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoa/{cpf}/integracao")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "Pessoa Integrada", description = "Consultar ou remover pessoas Integradas")
public class PessoaIntegradaController {

    private final GetPessoaIntegradaByCpfUseCase getPessoaIntegradaByCpfUseCase;
    private final DeletePessoaIntegradaByCpfUseCase deletePessoaIntegradaByCpfUseCase;

    /**
     * Obtém a Pessoa pelo ID.
     */
    @Operation(summary = "Obter uma pessoa por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    @GetMapping
    public ResponseEntity<PessoaResponse> getPessoaIntegrada(@PathVariable String cpf) {
        log.info("Foi reciba uma nova request para obter a Pessoa de ID: {}", cpf);
        Pessoa pessoa = getPessoaIntegradaByCpfUseCase.execute(cpf);
        return ResponseEntity.ok(PessoaResponse.fromDomain(pessoa));
    }

    /**
     * Remove uma pessoa por ID.
     */
    @Operation(summary = "Remover uma pessoa por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pessoa removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    @DeleteMapping
    public ResponseEntity<Void> deletePessoaIntegrada(@PathVariable String cpf) {
        log.info("Foi recebida a requisição para remover Pessoa com CPF: {}", cpf);
        deletePessoaIntegradaByCpfUseCase.execute(cpf);
        return ResponseEntity.noContent().build();
    }
}
