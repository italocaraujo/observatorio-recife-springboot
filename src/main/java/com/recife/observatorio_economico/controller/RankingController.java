package com.recife.observatorio_economico.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ranking")
public class RankingController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Função para ler arquivos JSON do classpath
    private List<Map<String, Object>> readJsonFile(String path) throws IOException {
        return objectMapper.readValue(new ClassPathResource(path).getInputStream(), List.class);
    }

    // -------------------------------------------
    //              DIMENSÃO
    // -------------------------------------------
    @GetMapping("/dimensao/anos/{ano}")
    public ResponseEntity<?> getRankingDimensao(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json-gz/ranking/dimensao/anos/%s_ranking_dimensao.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //                GERAL
    // -------------------------------------------
    @GetMapping("/geral/anos/{ano}")
    public ResponseEntity<?> getRankingGeral(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json-gz/ranking/geral/anos/%s_ranking_geral.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //              INDICADOR
    // -------------------------------------------
    @GetMapping("/indicador/anos/{ano}")
    public ResponseEntity<?> getRankingIndicador(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json-gz/ranking/indicador/anos/%s_ranking_indicador.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //               PILARES
    // -------------------------------------------
    @GetMapping("/pilares/anos/{ano}")
    public ResponseEntity<?> getRankingPilares(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json-gz/ranking/pilares/anos/%s_ranking_pilares.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
