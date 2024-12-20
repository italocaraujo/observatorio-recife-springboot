package com.recife.observatorio_economico.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresasController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Map<String, Object>> readJsonFile(String path) throws IOException {
        return objectMapper.readValue(new ClassPathResource(path).getInputStream(), List.class);
    }

    // -------------------------------------------
    //           POR MUNICÍPIO
    // -------------------------------------------
    @GetMapping("/por_municipio/dados_totais/anos/{ano}")
    public ResponseEntity<?> getPorMunicipioDadosTotais(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json-gz/empresas/por_municipio/dados_totais/anos/%s_empresas.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    @GetMapping("/por_municipio/somente_com_baixas/anos/{ano}")
    public ResponseEntity<?> getPorMunicipioSomenteComBaixas(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json-gz/empresas/por_municipio/somente_com_baixas/anos/%s_empresas.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //                RECIFE
    // -------------------------------------------
    @GetMapping("/recife/ativas/anos/{ano}")
    public ResponseEntity<?> getRecifeAtivasPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json-gz/empresas/recife/ativas/anos/%s_empresas.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    @GetMapping("/recife/inativas/2020-2024")
    public ResponseEntity<?> getRecifeInativas() {
        try {
            String filePath = "data-json-gz/empresas/recife/inativas/2020-2024/empresas.json.gz";
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
