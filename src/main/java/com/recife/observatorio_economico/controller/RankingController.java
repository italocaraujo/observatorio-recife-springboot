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
@RequestMapping("/api/v1/ranking")
public class RankingController {

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
    //              DIMENSÃO
    // -------------------------------------------
    @GetMapping("/dimensao/anos/{ano}")
    public ResponseEntity<?> getRankingDimensao(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/ranking/dimensao/anos/%s_ranking_dimensao.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //                GERAL
    // -------------------------------------------
    @GetMapping("/geral/anos/{ano}")
    public ResponseEntity<?> getRankingGeral(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/ranking/geral/anos/%s_ranking_geral.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //              INDICADOR
    // -------------------------------------------
    @GetMapping("/indicador/anos/{ano}")
    public ResponseEntity<?> getRankingIndicador(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/ranking/indicador/anos/%s_ranking_indicador.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //               PILARES
    // -------------------------------------------
    @GetMapping("/pilares/anos/{ano}")
    public ResponseEntity<?> getRankingPilares(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/ranking/pilares/anos/%s_ranking_pilares.json.gz", ano);
        return loadJsonResponse(filePath);
    }
}
