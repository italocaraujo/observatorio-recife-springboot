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
@RequestMapping("/api/v1/ipca")
public class IpcaController {

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
    //                  GRUPOS
    // -------------------------------------------
    @GetMapping("/grupos/anos/{ano}")
    public ResponseEntity<?> getGruposPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/ipca/grupos.anos/%s_grupos.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //            ÍNDICE GERAL
    // -------------------------------------------
    @GetMapping("/indice_geral/anos/{ano}")
    public ResponseEntity<?> getIndiceGeralPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/ipca/indice_geral/anos/%s_indice_geral.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //                 TABELAS
    // -------------------------------------------
    @GetMapping("/tabelas/anos/{ano}")
    public ResponseEntity<?> getTabelasPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/ipca/tabelas/anos/%s_tabelas.json.gz", ano);
        return loadJsonResponse(filePath);
    }
}
