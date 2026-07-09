package com.ocupa.ocupa.repository;

import com.ocupa.ocupa.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
}
