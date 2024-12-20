package com.recife.observatorio_economico.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pib")
public class PibController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Map<String, Object>> readJsonFile(String path) throws IOException {
        return objectMapper.readValue(new ClassPathResource(path).getInputStream(), List.class);
    }

    // -------------------------------------------
    //              PIB POR MUNICÍPIO
    // -------------------------------------------
    @GetMapping("/municipios/anos/{ano}")
    public ResponseEntity<?> getPibMunicipiosPorAno(@PathVariable String ano) {
        try {
            String filePath = String.format("data-json-gz/pib/municipios/anos/%s_pib_municipios.json.gz", ano);
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
