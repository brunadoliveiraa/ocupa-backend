package com.ocupa.ocupa.repository;

import com.ocupa.ocupa.model.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EspacoRepository extends JpaRepository<Espaco, Integer> {
    Optional<Espaco> findByNome(String nome);
    boolean existsByNome(String nome);
}
