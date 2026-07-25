package com.ocupa.ocupa.service.impl;

import com.ocupa.ocupa.model.Evento;
import com.ocupa.ocupa.repository.EventoRepository;
import com.ocupa.ocupa.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {
    private final EventoRepository repository;

    @Override
    public List<Evento> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Evento> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Evento save(Evento evento) {
        return repository.save(evento);
    }

    @Override
    public void delete(Evento evento) {
        repository.delete(evento);
    }
}
