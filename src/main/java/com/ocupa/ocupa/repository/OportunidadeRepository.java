package com.ocupa.ocupa.repository;

import com.ocupa.ocupa.model.Oportunidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Integer> {
    Optional<Oportunidade> findByTitulo(String titulo);
}
