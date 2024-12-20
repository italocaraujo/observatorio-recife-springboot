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
@RequestMapping("/api/v1/balanco-comercial")
public class BalancoComercialController {

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
    //             EXPORTAÇÃO POR ANO
    // -------------------------------------------
    @GetMapping("/exportacao/{ano}")
    public ResponseEntity<?> getExportacaoPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/balanco_comercial/exportacao/anos/%s_exportacao_municipio.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //             IMPORTAÇÃO POR ANO
    // -------------------------------------------
    @GetMapping("/importacao/{ano}")
    public ResponseEntity<?> getImportacaoPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/balanco_comercial/importacao/anos/%s_importacao_municipios.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //             DADOS GERAIS POR ANO
    // -------------------------------------------
    @GetMapping("/geral/{ano}")
    public ResponseEntity<?> getDadosGeraisPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/balanco_comercial/geral/anos/%s_base_dados.json.gz", ano);
        return loadJsonResponse(filePath);
    }
}
