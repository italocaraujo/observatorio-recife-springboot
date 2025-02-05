package com.recife.observatorio_economico.controller;

import com.recife.observatorio_economico.service.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ipca")
@RequiredArgsConstructor
@Slf4j
public class IpcaController {

    private final JsonService jsonService;

    private static final String BASE_PATH = "data-json/ipca";

    // Método auxiliar para carregar e tratar respostas JSON
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

    // -------------------------------------------
    //                  GRUPOS
    // -------------------------------------------
    @GetMapping("/grupos/anos/{ano}")
    public ResponseEntity<?> getGruposPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/grupos/anos/%s.json.gz", BASE_PATH, ano));
    }

    // -------------------------------------------
    //            GERAL
    // -------------------------------------------
    @GetMapping("/geral/anos/{ano}")
    public ResponseEntity<?> getIndiceGeralPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/geral/anos/%s.json.gz", BASE_PATH, ano));
    }

    // -------------------------------------------
    //                 ANALITICO
    // -------------------------------------------
    @GetMapping("/analitico/anos/{ano}")
    public ResponseEntity<?> getAnaliticoPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/analitico/anos/%s.json.gz", BASE_PATH, ano));
    }
}