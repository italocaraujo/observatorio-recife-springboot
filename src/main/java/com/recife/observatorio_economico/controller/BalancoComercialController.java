package com.recife.observatorio_economico.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/balanco-comercial")
public class BalancoComercialController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Map<String, Object>> readJsonFile(String path) throws IOException {
        return objectMapper.readValue(new ClassPathResource(path).getInputStream(), List.class);
    }

    // -------------------------------------------
    //             EXPORTAÇÃO POR ANO
    // -------------------------------------------
    @GetMapping("/exportacao/{ano}")
    public ResponseEntity<?> getExportacaoPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json/balanco_comercial/exportacao/anos/%s_exportacao_municipio.json", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //             IMPORTAÇÃO POR ANO
    // -------------------------------------------
    @GetMapping("/importacao/{ano}")
    public ResponseEntity<?> getImportacaoPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json/balanco_comercial/importacao/anos/%s_importacao_municipios.json", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //             DADOS GERAIS POR ANO
    // -------------------------------------------
    @GetMapping("/geral/{ano}")
    public ResponseEntity<?> getDadosGeraisPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json/balanco_comercial/geral/anos/%s_base_dados.json", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
