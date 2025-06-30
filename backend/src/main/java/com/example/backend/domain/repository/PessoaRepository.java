package com.example.backend.domain.repository;

import com.example.backend.domain.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
    @Query(value = "SELECT p FROM Pessoa p LEFT JOIN FETCH p.endereco",
            countQuery = "SELECT count(p) FROM Pessoa p")
    Page<Pessoa> findAllPessoasWithEndereco(Pageable pageable);
    Optional<Pessoa> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    void deleteByCpf(String cpf);
}
