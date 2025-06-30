package com.example.backend.infrastructure.adapter.in.web;

import com.example.backend.application.usecase.*;
import com.example.backend.domain.model.Pessoa;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceNotFoundException;
import com.example.backend.infrastructure.adapter.in.web.exception.ResourceAlreadyExistsException;
import com.example.backend.infrastructure.dto.PessoaRequest;
import com.example.backend.infrastructure.dto.PessoaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "Backend", description = "Gerenciar informações de Pessoas")
public class PessoaController {

    private final CreatePessoaUseCase createPessoaUseCase;
    private final GetAllPessoasWithEnderecoUseCase getAllPessoasWithEnderecoUseCase;
    private final GetPessoaUseCase getPessoaUseCase;
    private final UpdatePessoaUseCase updatePessoaUseCase;
    private final DeletePessoaUseCase deletePessoaUseCase;

    /**
     * Salva uma nova pessoa.
     *
     * @param request O PessoaRequest DTO contendo informações da Pessoa.
     * @return ResponseEntity with the created PessoaResponse DTO.
     * @throws ResourceAlreadyExistsException Exceção lançada ao tentar criar Pessoa já existente.
     */
    @Operation(summary = "Criar uma nova pessoa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Solicitação inválida!"),
            @ApiResponse(responseCode = "409", description = "Conflito! A pessoa já existe")
    })
    @PostMapping
    public ResponseEntity<PessoaResponse> createPessoa(@Valid @RequestBody PessoaRequest request) {
        log.info("Foi recebida a request para criar a pessoa: {}", request.getNome());
        var pessoa = createPessoaUseCase.execute(request.toDomain());
        return new ResponseEntity<>(PessoaResponse.fromDomain(pessoa), HttpStatus.CREATED);
    }

    /**
     * Obtém a Pessoa pelo ID.
     *
     * @param id O ID da Pessoa a ser obitda.
     * @return ResponseEntity com o DTO PessoaResponse.
     * @throws ResourceNotFoundException se a Pessoa não foi encontrada.
     */
    @Operation(summary = "Obter uma pessoa por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> getPessoa(@PathVariable Integer id) {
        log.info("Foi reciba uma nova request para obter a Pessoa de ID: {}", id);
        var pessoa = getPessoaUseCase.execute(id)
                .orElseThrow(() -> {
                    log.warn("Pessoa com ID {} não encontrado", id);
                    return new ResourceNotFoundException("Pessoa não encontrada com ID: " + id);
                });
        return ResponseEntity.ok(PessoaResponse.fromDomain(pessoa));
    }

    /**
     * Obter todas as pessoas com paginação.
     *
     * @param page O número da página ( 0, por padrão).
     * @param size O número de registros de Pessoa em cada página (10, por padrão).
     * @return ResponseEntity com uma lista de registros de PessoaResponse DTOs.
     */
    @Operation(summary = "Obter todas pessoas com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao obter a lista de Pessoas!")
    })
    @GetMapping
    public ResponseEntity<List<PessoaResponse>> getAllPessoas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Foi recibda a requisição para obter todas as Pessoas (page: {}, size: {})", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Pessoa> pessoaPage = getAllPessoasWithEnderecoUseCase.execute(pageable);
        List<PessoaResponse> pessoaResponses = pessoaPage.getContent().stream()
                .map(PessoaResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pessoaResponses);

    }

    /**
     * Atualiza uma pessoa já existente.
     * @param id O ID da Pessoa a ser atualizada.
     * @param request O DTO PessoaRequest DTO com informações atualizdaas da Pessoa.
     * @return ResponseEntity com o DTO PessoaResponse contendo informações atualizadas da Pessoa.
     * @throws ResourceNotFoundException exceção estourada quano a Pessoa não pôde ser encontrada.
     */
    @Operation(summary = "Atualiza uma pessoa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponse> updatePessoa(@PathVariable Integer id, @Valid @RequestBody PessoaRequest request) {
        log.info("Foi recebida a requisição para atualizar Pessoa com ID: {}", id);
        getPessoaUseCase.execute(id)
                .orElseThrow(() -> {
                    log.warn("Pessoa com ID {} não encontrada para realizar atualização", id);
                    return new ResourceNotFoundException("Pessoa não encontrada com id: " + id);
                });

        Pessoa pessoaAtualizada = updatePessoaUseCase.execute(id, request.toDomain());

        return ResponseEntity.ok(PessoaResponse.fromDomain(pessoaAtualizada));
    }

    /**
     * Remove uma pessoa por ID.
     * @param id O ID da pessoa a ser removida.
     * @return ResponseEntity indicando sucesso.
     * @throws ResourceNotFoundException Exceção estourada no caso em que a pessoa não pôde for encontrada.
     */
    @Operation(summary = "Remover uma pessoa por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pessoa removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePessoa(@PathVariable Integer id) {
        log.info("Foi recebida a requisição para remover Pessoa com ID: {}", id);
        getPessoaUseCase.execute(id)
                .orElseThrow(() -> {
                    log.warn("A Pessoa de ID {} não foi encontrada para realizar a remoção", id);
                    return new ResourceNotFoundException("Ṕessoa não encontrada com ID: " + id);
                });

        deletePessoaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
