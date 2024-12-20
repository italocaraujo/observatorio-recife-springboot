package com.recife.observatorio_economico.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@RestController
@RequestMapping("/api/v1/aeroporto")
public class AeroportoController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Método para ler e descompactar arquivos JSON.GZ.
     */
    private List<Map<String, Object>> readGzippedJsonFile(String path) throws IOException {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ClassPathResource(path).getInputStream());
             InputStreamReader reader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            return objectMapper.readValue(bufferedReader, List.class);
        }
    }

    /**
     * Método genérico para carregar e retornar JSON de acordo com o caminho do arquivo.
     */
    private ResponseEntity<?> loadJsonResponse(String filePath) {
        try {
            return ResponseEntity.ok(readGzippedJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado ou erro ao descompactar: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //                 AENA - CARGA
    // -------------------------------------------
    @GetMapping("/aena/carga/anos/{ano}")
    public ResponseEntity<?> getAenaCargaPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/aeroporto/aena/carga/anos/%s_aena_carga.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //              AENA - PASSAGEIRO
    // -------------------------------------------
    @GetMapping("/aena/passageiro/anos/{ano}")
    public ResponseEntity<?> getAenaPassageiroPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/aeroporto/aena/passageiro/anos/%s_aena_passageiros.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //                 ANAC
    // -------------------------------------------
    @GetMapping("/anac/anos/{ano}")
    public ResponseEntity<?> getAnacResumoAnual(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/aeroporto/anac/anos/%s_resumo_anual.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/anac/embarque/{ano}")
    public ResponseEntity<?> getAnacEmbarque(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/aeroporto/anac/embarque_desembarque/embarque/%s_embarque.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/anac/desembarque/{ano}")
    public ResponseEntity<?> getAnacDesembarque(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/aeroporto/anac/embarque_desembarque/desembarque/%s_desembarque.json.gz", ano);
        return loadJsonResponse(filePath);
    }
}
