package com.recife.observatorio_economico.controller;

import com.recife.observatorio_economico.service.GZSenderTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/porto")
@RequiredArgsConstructor
public class PortoController {
    private final GZSenderTest jsonService;
    private static final String BASE_PATH = "data-json/porto";

    /**
     * Carrega um arquivo JSON ou GZ como resposta HTTP.
     */
    private ResponseEntity<?> loadJsonResponse(String filePath) {
        try {
            log.debug("Carregando arquivo JSON: {}", filePath);
            if (filePath.endsWith(".gz")) {
                // Retorna o arquivo .gz sem descompactar
                byte[] gzContent = jsonService.readGzFileAsBytes(filePath);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "application/gzip") // Define o tipo de conteúdo como GZIP
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.substring(filePath.lastIndexOf("/") + 1) + "\"")
                        .body(gzContent);
            } else {
                // Retorna o arquivo JSON normalmente
                return ResponseEntity.ok(jsonService.readJsonFile(filePath));
            }
        } catch (RuntimeException e) {
            log.error("Erro ao processar arquivo: {}", filePath, e);
            return ResponseEntity.status(500)
                    .body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/atracacao/{ano}")
    public ResponseEntity<?> getAtracacaoPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/atracacao/anos/%s.json.gz", BASE_PATH, ano));
    }

    @GetMapping("/carga/{ano}")
    public ResponseEntity<?> getCargaPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/carga/anos/%s.json.gz", BASE_PATH, ano));
    }

    @GetMapping("/dictionaries/atracacao/anos/{ano}")
    public ResponseEntity<?> getAtracaoDictionaryPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/dictionaries/atracacao/anos/%s.json", BASE_PATH, ano));
    }

    @GetMapping("/dictionaries/carga/anos/{ano}")
    public ResponseEntity<?> getCargaDictionaryPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/dictionaries/carga/anos/%s.json", BASE_PATH, ano));
    }

    @GetMapping("/dictionaries/origem")
    public ResponseEntity<?> getOrigemDictionary() {
        return loadJsonResponse(String.format("%s/dictionaries/origem/origem.json", BASE_PATH));
    }

    @GetMapping("/dictionaries/destino")
    public ResponseEntity<?> getDestinoDictionary() {
        return loadJsonResponse(String.format("%s/dictionaries/destino/destino.json", BASE_PATH));
    }

    @GetMapping("/dictionaries/mercadoria")
    public ResponseEntity<?> getMercadoriaDictionary() {
        return loadJsonResponse(String.format("%s/dictionaries/mercadoria/mercadoria.json", BASE_PATH));
    }
}