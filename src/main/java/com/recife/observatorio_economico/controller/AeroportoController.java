package com.recife.observatorio_economico.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api/v1/aeroporto")
public class AeroportoController {

    private static final String BASE_PATH = "src/main/resources/data-json/aeroporto";

    /**
     * Método para enviar arquivos JSON diretamente como resposta.
     */
    private ResponseEntity<Resource> sendJsonFile(String filePath) {
        try {
            // Resolve o caminho do arquivo
            Path path = Paths.get(BASE_PATH).resolve(filePath).normalize();
            Resource resource = new UrlResource(path.toUri());

            // Verifica se o arquivo existe
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Configura os cabeçalhos
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // -------------------------------------------
    //                 ENDPOINTS
    // -------------------------------------------

    @GetMapping("/embarque-desembarque/{ano}")
    public ResponseEntity<Resource> getEmbarqueDesembarque(@PathVariable String ano) {
        String filePath = String.format("embarque_desembarque/%s.json", ano);
        return sendJsonFile(filePath);
    }

    @GetMapping("/aena/carga/anos/{ano}")
    public ResponseEntity<Resource> getAenaCargaPorAno(@PathVariable String ano) {
        String filePath = String.format("aena/carga/anos/%s_aena_carga.json", ano);
        return sendJsonFile(filePath);
    }

    @GetMapping("/aena/passageiro/anos/{ano}")
    public ResponseEntity<Resource> getAenaPassageiroPorAno(@PathVariable String ano) {
        String filePath = String.format("aena/passageiro/anos/%s_aena_passageiros.json", ano);
        return sendJsonFile(filePath);
    }

    @GetMapping("/anac/anos/{year}")
    public ResponseEntity<Resource> getAnacByYear(@PathVariable String year) {
        log.info("Endpoint /anac/anos/{} foi chamado", year);
        String filePath = String.format("anac/anos/%s.json", year);
        return sendJsonFile(filePath);
    }

    @GetMapping("/anac/embarque/{ano}")
    public ResponseEntity<Resource> getAnacEmbarque(@PathVariable String ano) {
        String filePath = String.format("anac/embarque_desembarque/embarque/%s_embarque.json", ano);
        return sendJsonFile(filePath);
    }

    @GetMapping("/anac/desembarque/{ano}")
    public ResponseEntity<Resource> getAnacDesembarque(@PathVariable String ano) {
        String filePath = String.format("anac/embarque_desembarque/desembarque/%s_desembarque.json", ano);
        return sendJsonFile(filePath);
    }
}
