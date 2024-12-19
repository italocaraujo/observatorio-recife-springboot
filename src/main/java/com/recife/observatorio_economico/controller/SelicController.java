package com.recife.observatorio_economico.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/selic")
public class SelicController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Função para ler arquivos JSON do classpath
    private List<Map<String, Object>> readJsonFile(String path) throws IOException {
        return objectMapper.readValue(new ClassPathResource(path).getInputStream(), List.class);
    }

    // -------------------------------------------
    //                TAXA SELIC
    // -------------------------------------------
    @GetMapping("/anos/{ano}")
    public ResponseEntity<?> getTaxaSelicPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json/selic/anos/%s_taxa_selic.json", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
