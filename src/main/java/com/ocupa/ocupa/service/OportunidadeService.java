package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Oportunidade;
import java.util.List;
import java.util.Optional;

public interface OportunidadeService {
    List<Oportunidade> findAll();
    Optional<Oportunidade> findById(Integer id);
    Oportunidade save(Oportunidade oportunidade);
    void delete(Oportunidade oportunidade);
}
