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
@RequestMapping("/api/v1/empresas")
public class EmpresasController {

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
    //           POR MUNICÍPIO
    // -------------------------------------------
    @GetMapping("/por_municipio/dados_totais/anos/{ano}")
    public ResponseEntity<?> getPorMunicipioDadosTotais(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/empresas/por_municipio/dados_totais/anos/%s_empresas.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/por_municipio/somente_com_baixas/anos/{ano}")
    public ResponseEntity<?> getPorMunicipioSomenteComBaixas(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/empresas/por_municipio/somente_com_baixas/anos/%s_empresas.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //                RECIFE
    // -------------------------------------------
    @GetMapping("/recife/ativas/anos/{ano}")
    public ResponseEntity<?> getRecifeAtivasPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json-gz/empresas/recife/ativas/anos/%s_empresas.json.gz", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/recife/inativas/2020-2024")
    public ResponseEntity<?> getRecifeInativas() {
        String filePath = "data-json-gz/empresas/recife/inativas/2020-2024/empresas.json.gz";
        return loadJsonResponse(filePath);
    }
}
