package com.ocupa.ocupa.repository;

import com.ocupa.ocupa.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistaRepository extends JpaRepository<Artista, Integer> {
    Optional<Artista> findByNome(String nome);
}
