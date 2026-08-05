package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.repository.ArtistaRepository;
import com.ocupa.ocupa.repository.EspacoRepository;
import com.ocupa.ocupa.repository.EventoRepository;
import com.ocupa.ocupa.repository.OportunidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final ArtistaRepository artistaRepo;
    private final EspacoRepository espacoRepo;
    private final EventoRepository eventoRepo;
    private final OportunidadeRepository oportunidadeRepo;

    @GetMapping
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("artistas", artistaRepo.count());
        stats.put("espacos", espacoRepo.count());
        stats.put("eventos", eventoRepo.count());
        stats.put("oportunidades", oportunidadeRepo.count());
        return stats;
    }
}
