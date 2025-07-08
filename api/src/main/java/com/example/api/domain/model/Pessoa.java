package com.example.api.domain.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPessoa")
    private Integer id;

    @PastOrPresent(message = "Birthday cannot be in the future")
    @Column(nullable = false)
    private LocalDateTime criacaoRegistro;

    @NotBlank
    @Column(nullable = false)
    private LocalDateTime alteracaoRegistro;

    @Column(nullable = false)
    private String nome;

    // TODO: Teste R2
    @PastOrPresent(message = "Nascimento não pode ser maior que dia atual")
    @Column(nullable = false)
    private LocalDateTime nascimento;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String email;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Valid
    @NotNull(message= "Endereco não pode ser nulo")
    @PrimaryKeyJoinColumn
    private Endereco endereco;


    @PrePersist
    protected void onCreate() {
        this.criacaoRegistro = LocalDateTime.now();
        this.alteracaoRegistro = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.alteracaoRegistro = LocalDateTime.now();
    }

    public String getFormattedBirthDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return this.nascimento.format(formatter);
    }
}
