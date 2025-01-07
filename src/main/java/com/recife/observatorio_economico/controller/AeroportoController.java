package com.recife.observatorio_economico.controller;

import com.recife.observatorio_economico.service.JsonService;
import com.recife.observatorio_economico.service.FileWatcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/aeroporto")
@RequiredArgsConstructor
public class AeroportoController {
    private final JsonService jsonService;
    private final FileWatcherService fileWatcherService;
    
    private static final String BASE_PATH = "data-json/aeroporto";
    
    private ResponseEntity<?> loadJsonResponse(String filePath) {
        try {
            log.debug("Carregando arquivo JSON: {}", filePath);
            return ResponseEntity.ok(jsonService.readJsonFile(filePath));
        } catch (RuntimeException e) {
            log.error("Erro ao processar arquivo: {}", filePath, e);
            return ResponseEntity.status(500)
                .body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }
    
    @GetMapping("/aena/carga/anos/{ano}")
    public ResponseEntity<?> getAenaCargaPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/aena/carga/anos/%s.json", 
            BASE_PATH, ano));
    }
    
    @GetMapping("/aena/passageiro/anos/{ano}")
    public ResponseEntity<?> getAenaPassageiroPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/aena/passageiro/anos/%s.json", 
            BASE_PATH, ano));
    }
    
    @GetMapping("/anac/anos/{ano}")
    public ResponseEntity<?> getAnacResumoAnual(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/anac/anos/%s.json", 
            BASE_PATH, ano));
    }
    
}
