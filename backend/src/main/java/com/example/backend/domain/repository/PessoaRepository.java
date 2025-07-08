package com.example.backend.domain.repository;

import com.example.backend.domain.model.PessoaCadastro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaCadastro, Integer> {
    @Query(value = "SELECT p FROM Pessoa p LEFT JOIN FETCH p.endereco",
            countQuery = "SELECT count(p) FROM Pessoa p")
    Page<PessoaCadastro> findAllPessoasWithEndereco(Pageable pageable);
    Optional<PessoaCadastro> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    void deleteByCpf(String cpf);
}
