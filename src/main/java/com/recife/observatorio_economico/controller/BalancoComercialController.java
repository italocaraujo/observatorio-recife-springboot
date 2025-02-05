package com.recife.observatorio_economico.controller;

import com.recife.observatorio_economico.service.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/balanco-comercial")
@RequiredArgsConstructor
public class BalancoComercialController {

    private final JsonService jsonService;

    private static final String BASE_PATH = "data-json/balanco_comercial";

    /**
     * Método auxiliar para carregar e retornar uma resposta JSON.
     *
     * @param filePath Caminho do arquivo JSON ou JSON.GZ.
     * @return ResponseEntity com os dados do arquivo ou mensagem de erro.
     */
    private ResponseEntity<?> loadJsonResponse(String filePath) {
        try {
            log.debug("Carregando arquivo JSON: {}", filePath);
            return ResponseEntity.ok(jsonService.readJsonFile(filePath));
        } catch (RuntimeException e) {
            log.error("Erro ao processar arquivo: {}", filePath, e);
            return ResponseEntity.status(500)
                    .body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //             DADOS GERAIS POR ANO
    // -------------------------------------------
    @GetMapping("/geral/{ano}")
    public ResponseEntity<?> getDadosGeraisPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/geral/anos/%s.json.gz", BASE_PATH, ano));
    }

    // -------------------------------------------
    //             OUTRAS ROTAS (EXEMPLOS)
    // -------------------------------------------
    @GetMapping("/exportacao/{ano}")
    public ResponseEntity<?> getExportacaoPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/exportacao/anos/%s.json.gz", BASE_PATH, ano));
    }

    @GetMapping("/importacao/{ano}")
    public ResponseEntity<?> getImportacaoPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/importacao/anos/%s.json.gz", BASE_PATH, ano));
    }

    @GetMapping("/balanco-mensal/{ano}")
    public ResponseEntity<?> getBalancoMensalPorAno(@PathVariable String ano) {
        return loadJsonResponse(String.format("%s/balanco-mensal/anos/%s.json.gz", BASE_PATH, ano));
    }
}