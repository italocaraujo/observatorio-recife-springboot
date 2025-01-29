package com.recife.observatorio_economico.controller;

import com.recife.observatorio_economico.service.JsonService;
import com.recife.observatorio_economico.service.FileWatcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/porto")
@RequiredArgsConstructor
public class PortoController {
    private final JsonService jsonService;
    private final FileWatcherService fileWatcherService;
    
    private static final String BASE_PATH = "data-json/porto";
    
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
    
    @GetMapping("/carga")
    public ResponseEntity<?> getPortoCarga(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/carga/porto_cargas.json.gz", 
            BASE_PATH, ano));
    }
    
}
