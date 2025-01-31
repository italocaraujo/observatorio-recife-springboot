package com.recife.observatorio_economico.controller;

import com.recife.observatorio_economico.service.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
public class RankingController {
    private final JsonService jsonService;
    private static final String BASE_PATH = "data-json/ranking";

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
    //              DIMENSÃO
    // -------------------------------------------
    @GetMapping("/dimensao/anos/{ano}")
    public ResponseEntity<?> getRankingDimensao(@PathVariable String ano) {
        String filePath = String.format("%s/dimensao/anos/%s.json.gz", BASE_PATH, ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //                GERAL
    // -------------------------------------------
    @GetMapping("/geral/anos/{ano}")
    public ResponseEntity<?> getRankingGeral(@PathVariable String ano) {
        String filePath = String.format("%s/geral/anos/%s.json.gz", BASE_PATH, ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //              INDICADOR
    // -------------------------------------------
    @GetMapping("/indicador/anos/{ano}")
    public ResponseEntity<?> getRankingIndicador(@PathVariable String ano) {
        String filePath = String.format("%s/indicador/anos/%s.json.gz", BASE_PATH, ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //               PILARES
    // -------------------------------------------
    @GetMapping("/pilares/anos/{ano}")
    public ResponseEntity<?> getRankingPilares(@PathVariable String ano) {
        String filePath = String.format("%s/pilares/anos/%s.json.gz", BASE_PATH, ano);
        return loadJsonResponse(filePath);
    }
}