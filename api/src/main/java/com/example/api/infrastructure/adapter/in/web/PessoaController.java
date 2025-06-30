package com.example.api.infrastructure.adapter.in.web;

import com.example.api.application.usecase.*;
import com.example.api.domain.model.Pessoa;
import com.example.api.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import com.example.api.infrastructure.adapter.out.web.exception.CepInvalidoException;
import com.example.api.infrastructure.adapter.out.web.exception.CepSemFormatoValidoException;
import com.example.api.infrastructure.dto.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses
;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "API", description = "Gerenciar informações de Pessoas")
public class PessoaController {

    private final CreatePessoaUseCase createPessoaUseCase;
    private final GetPessoaByCpfUseCase getPessoaByCpfUseCase;
    private final UpdatePessoaByCpfUseCase updatePessoaByCpfUseCase;
    private final DeletePessoaByCpfUseCase deletePessoaByCpfUseCase;

    /**
     * Salva uma nova pessoa.
     * @param request O PessoaRequest DTO contendo informações da Pessoa.
     * @return ResponseEntity com o DTO PessoaSaveResponse.
     * @throws CepInvalidoException exceção lançada para CEP verificado como inválido.
     * @throws CepSemFormatoValidoException exceção lançada para CEP com formatação incorreta.
     */
    @Operation(summary = "Criar uma nova pessoa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Solicitação inválida!"),
            @ApiResponse(responseCode = "422", description = "Requisição negada por CEP inválido")
    })
    @PostMapping
    public ResponseEntity<PessoaSaveResponse> createPessoa(@Valid @RequestBody PessoaRequest request) {
        log.info("Foi recebida a request para criar a pessoa: {}", request.getNome());
        Pessoa pessoa = createPessoaUseCase.execute(request.toDomain());

        PessoaSaveResponse response = new PessoaSaveResponse(
                pessoa.getId(),
                "Sucesso ao salvar Pessoa!"
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Obtém a Pessoa pelo CPF.
     * @param cpf O CPF da Pessoa a ser obitda.
     * @return ResponseEntity com o DTO PessoaGetResponse.
     * @throws ResourceNotFoundException se a Pessoa não foi encontrada.
     */
    @Operation(summary = "Obter uma pessoa por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PessoaGetResponse> getPessoaByCpf(@PathVariable String cpf) {
        log.info("Foi recebida uma nova request para obter a Pessoa de CPF: {}", cpf);
        Optional<Pessoa> pessoa = getPessoaByCpfUseCase.execute(cpf);

        if (pessoa.isPresent()) {
            PessoaGetResponse response = PessoaGetResponse.fromDomain(pessoa.get());
            return ResponseEntity.ok(response);
        } else {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com CPF " + cpf + " não encontrado para remoção.");
        }

    }

    /**
     * Atualiza uma pessoa já existente.
     * @param cpf O CPF da Pessoa a ser atualizada.
     * @param request O DTO PessoaRequest DTO com informações atualizdaas da Pessoa.
     * @return ResponseEntity com o DTO PessoaSaveResponse contendo informações da Pessoa salva.
     * @throws ResourceNotFoundException exceção estourada quando a Pessoa não pôde ser encontrada.
     * @throws CepInvalidoException exceção lançada para CEP verificado como inválido.
     * @throws CepSemFormatoValidoException exceção lançada para CEP com formatação incorreta.
     */
    @Operation(summary = "Atualiza uma pessoa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição com formato de CEP inválido"),
            @ApiResponse(responseCode = "422", description = "Requisição negada por CEP inválido"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    @PutMapping("/{cpf}")
    public ResponseEntity<PessoaSaveResponse> updatePessoaByCpf(@PathVariable String cpf, @Valid @RequestBody PessoaRequest request) {
        log.info("Foi recebida a requisição para atualizar Pessoa com CPF: {}", cpf);
        getPessoaByCpfUseCase.execute(cpf)
                .orElseThrow(() -> {
                    log.warn("Pessoa com CPF {} não encontrada para realizar atualização", cpf);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada com CPF " + cpf);
                });

        Pessoa pessoaAtualizada = updatePessoaByCpfUseCase.execute(cpf, request.toDomain());

        String mensagemString = "A pessoa com CPF: " + cpf + " foi atualizada";

        PessoaSaveResponse response = new PessoaSaveResponse(
                pessoaAtualizada.getId(),
                mensagemString
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Remove uma pessoa por CPF.
     * @param cpf O CPF da pessoa a ser removida.
     * @return ResponseEntity indicando sucesso.
     * @throws ResourceNotFoundException Exceção estourada no caso em que a pessoa não pôde for encontrada.
     */
    @Operation(summary = "Remover uma pessoa por CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<PessoaDeleteResponse> deletePessoaByCpf(@PathVariable String cpf) {
        log.info("Foi recebida a requisição para remover Pessoa com CPF: {}", cpf);
        deletePessoaByCpfUseCase.execute(cpf);

        String mensagem = "A pessoa de CPF "+ cpf + " foi removida com sucesso";

        PessoaDeleteResponse response = new PessoaDeleteResponse(mensagem);
        return ResponseEntity.ok(response);
    }
}
