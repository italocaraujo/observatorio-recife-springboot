package com.recife.observatorio_economico.controller;

import com.recife.observatorio_economico.service.ParquetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/porto")
@RequiredArgsConstructor
public class PortoController {

    private final ParquetService parquetService;
    private static final String BASE_PATH = "data-json/porto";

    private ResponseEntity<?> loadResponse(String filePath, boolean processContent) {
        try {
            log.debug("Carregando arquivo: {}", filePath);

            if (filePath.endsWith(".parquet")) {
                if (processContent) {
                    List<Object> parquetData = parquetService.readParquetFileAsList(filePath);
                    return ResponseEntity.ok(parquetData);
                } else {
                    byte[] parquetContent = parquetService.readParquetFileAsBytes(filePath);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.substring(filePath.lastIndexOf("/") + 1) + "\"")
                            .body(parquetContent);
                }
            } else if (filePath.endsWith(".json")) {
                return ResponseEntity.ok(parquetService.readJsonFile(filePath));
            } else {
                log.error("Formato de arquivo não suportado: {}", filePath);
                return ResponseEntity.status(400)
                        .body("Formato de arquivo não suportado: " + filePath);
            }
        } catch (IOException e) {
            log.error("Erro ao processar arquivo: {}", filePath, e);
            return ResponseEntity.status(500)
                    .body("Erro ao processar o arquivo: " + e.getMessage());
        } catch (URISyntaxException e) {
            log.error("Erro de URI ao processar arquivo: {}", filePath, e);
            return ResponseEntity.status(500)
                    .body("Erro de URI ao processar o arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/atracacao/{ano}")
    public ResponseEntity<?> getAtracacaoPorAno(@PathVariable String ano) {
        return loadResponse(String.format("%s/atracacao/anos/%s.parquet", BASE_PATH, ano), false);
    }

    @GetMapping("/atracacao/{ano}/processed")
    public ResponseEntity<?> getAtracacaoPorAnoProcessed(@PathVariable String ano) {
        return loadResponse(String.format("%s/atracacao/anos/%s.parquet", BASE_PATH, ano), true);
    }

    @GetMapping("/carga/{ano}")
    public ResponseEntity<?> getCargaPorAno(@PathVariable String ano) {
        return loadResponse(String.format("%s/carga/anos/%s.parquet", BASE_PATH, ano), false);
    }

    @GetMapping("/carga/{ano}/processed")
    public ResponseEntity<?> getCargaPorAnoProcessed(@PathVariable String ano) {
        return loadResponse(String.format("%s/carga/anos/%s.parquet", BASE_PATH, ano), true);
    }

    @GetMapping("/dictionaries/atracacao/anos/{ano}")
    public ResponseEntity<?> getAtracaoDictionaryPorAno(@PathVariable String ano) {
        return loadResponse(String.format("%s/dictionaries/atracacao/anos/%s.json", BASE_PATH, ano), false);
    }

    @GetMapping("/dictionaries/carga/anos/{ano}")
    public ResponseEntity<?> getCargaDictionaryPorAno(@PathVariable String ano) {
        return loadResponse(String.format("%s/dictionaries/carga/anos/%s.json", BASE_PATH, ano), false);
    }

    @GetMapping("/dictionaries/origem")
    public ResponseEntity<?> getOrigemDictionary() {
        return loadResponse(String.format("%s/dictionaries/origem/origem.json", BASE_PATH), false);
    }

    @GetMapping("/dictionaries/destino")
    public ResponseEntity<?> getDestinoDictionary() {
        return loadResponse(String.format("%s/dictionaries/destino/destino.json", BASE_PATH), false);
    }

    @GetMapping("/dictionaries/mercadoria")
    public ResponseEntity<?> getMercadoriaDictionary() {
        return loadResponse(String.format("%s/dictionaries/mercadoria/mercadoria.json", BASE_PATH), false);
    }
}