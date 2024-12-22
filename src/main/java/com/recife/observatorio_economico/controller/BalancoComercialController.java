package com.recife.observatorio_economico.controller;

import com.recife.observatorio_economico.service.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/balanco-comercial")
public class BalancoComercialController {

    private final JsonService jsonService;

    // -------------------------------------------
    //             EXPORTAÇÃO POR ANO
    // -------------------------------------------
    @GetMapping("/exportacao/{ano}")
    public ResponseEntity<List<Map<String, Object>>> getExportacaoPorAno(@PathVariable String ano) {
        log.info("Fetching export data for year: {}", ano);
        String filePath = String.format("data-json/balanco_comercial/exportacao/anos/%s_exportacao_municipio.json", ano);
        return ResponseEntity.ok(jsonService.readJsonFile(filePath));
    }

    // -------------------------------------------
    //             IMPORTAÇÃO POR ANO
    // -------------------------------------------
    @GetMapping("/importacao/{ano}")
    public ResponseEntity<List<Map<String, Object>>> getImportacaoPorAno(@PathVariable String ano) {
        log.info("Fetching import data for year: {}", ano);
        String filePath = String.format("data-json/balanco_comercial/importacao/anos/%s_importacao_municipios.json", ano);
        return ResponseEntity.ok(jsonService.readJsonFile(filePath));
    }

    // -------------------------------------------
    //             DADOS GERAIS POR ANO
    // -------------------------------------------
    @GetMapping("/geral/{ano}")
    public ResponseEntity<List<Map<String, Object>>> getDadosGeraisPorAno(@PathVariable String ano) {
        log.info("Fetching general data for year: {}", ano);
        String filePath = String.format("data-json/balanco_comercial/geral/anos/%s_base_dados.json", ano);
        return ResponseEntity.ok(jsonService.readJsonFile(filePath));
    }
}
