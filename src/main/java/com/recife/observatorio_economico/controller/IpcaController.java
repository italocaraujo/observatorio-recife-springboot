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
            String filePath = String.format("data-json/ipca/grupos/anos/%s_grupos.json", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //            ÍNDICE GERAL
    // -------------------------------------------
    @GetMapping("/indice_geral/anos/{ano}")
    public ResponseEntity<?> getIndiceGeralPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json/ipca/indice_geral/anos/%s_indice_geral.json", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //                 TABELAS
    // -------------------------------------------
    @GetMapping("/tabelas/anos/{ano}")
    public ResponseEntity<?> getTabelasPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json/ipca/tabelas.anos/%s_tabelas.json", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
