package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Espaco;
import java.util.List;
import java.util.Optional;

public interface EspacoService {
    List<Espaco> findAll();
    Optional<Espaco> findById(Integer id);
    Espaco save(Espaco espaco);
    void delete(Espaco espaco);
}
