package com.example.api.domain.repository;

import com.example.api.domain.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
    Optional<Pessoa> findByCpf(String cpf);
    void deleteByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
