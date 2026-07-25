package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Evento;
import java.util.List;
import java.util.Optional;

public interface EventoService {
    List<Evento> findAll();
    Optional<Evento> findById(Integer id);
    Evento save(Evento evento);
    void delete(Evento evento);
}
