package com.recife.observatorio_economico.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ipca")
public class IpcaController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Map<String, Object>> readJsonFile(String path) throws IOException {
        return objectMapper.readValue(new ClassPathResource(path).getInputStream(), List.class);
    }

    // -------------------------------------------
    //                  GRUPOS
    // -------------------------------------------
    @GetMapping("/grupos/anos/{ano}")
    public ResponseEntity<?> getGruposPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json/ipca/grupos/anos/%s.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //            GERAL
    // -------------------------------------------
    @GetMapping("/geral/anos/{ano}")
    public ResponseEntity<?> getIndiceGeralPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json/ipca/geral/anos/%s.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //                 analitico
    // -------------------------------------------
    @GetMapping("/analitico/anos/{ano}")
    public ResponseEntity<?> getAnaliticoPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json/ipca/analitico/anos/%s.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
