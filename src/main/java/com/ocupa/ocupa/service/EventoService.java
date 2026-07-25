package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Evento;
import com.ocupa.ocupa.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventoService {
    private final EventoRepository repository;

    public List<Evento> findAll() {
        return repository.findAll();
    }

    public Optional<Evento> findById(Integer id) {
        return repository.findById(id);
    }

    public Evento save(Evento evento) {
        return repository.save(evento);
    }

    public void delete(Evento evento) {
        repository.delete(evento);
    }
}
