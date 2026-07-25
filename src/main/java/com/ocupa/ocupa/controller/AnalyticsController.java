package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Analytics;
import com.ocupa.ocupa.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService service;

    @PostMapping
    public Analytics create(@RequestBody Analytics a){
        return service.save(a);
    }

    @GetMapping
    public List<Analytics> all(){
        return service.findAll();
    }
}
