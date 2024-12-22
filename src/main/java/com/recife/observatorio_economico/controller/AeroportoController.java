package com.recife.observatorio_economico.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/aeroporto")
public class AeroportoController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Cache para armazenar resultados frequentes
    private final Cache<String, List<Map<String, Object>>> cache = Caffeine.newBuilder()
            .maximumSize(100) // Máximo de 100 itens no cache
            .expireAfterWrite(10, TimeUnit.MINUTES) // Cache expira após 10 minutos
            .build();

    // Função para ler arquivos JSON em streaming e utilizar o cache
    private List<Map<String, Object>> readJsonFile(String path) {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            JsonParser parser = objectMapper.getFactory().createParser(inputStream);
            List<Map<String, Object>> result = new ArrayList<>();
            parser.nextToken(); // Avança para o início do array
            while (parser.nextToken() == JsonToken.START_OBJECT) {
                Map<String, Object> item = objectMapper.readValue(parser, Map.class);
                result.add(item);
            }
            return result;
        } catch (IOException e) {
            // Log a exceção e reenvie como RuntimeException
            throw new RuntimeException("Erro ao ler o arquivo JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Método genérico para carregar e retornar JSON de acordo com o caminho do arquivo.
     */
    private ResponseEntity<?> loadJsonResponse(String filePath) {
        try {
            return ResponseEntity.ok(readJsonFile(filePath));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    // -------------------------------------------
    //                 AENA - CARGA
    // -------------------------------------------
    @GetMapping("/aena/carga/anos/{ano}")
    public ResponseEntity<?> getAenaCargaPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json/aeroporto/aena/carga/anos/%s_aena_carga.json", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/aena/carga/embarque/{ano}")
    public ResponseEntity<?> getAenaCargaEmbarque(@PathVariable String ano) {
        String filePath = String.format("data-json/aeroporto/aena/carga/embarque_desembarque/embarque/%s_aena_carga_embarque.json", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/aena/carga/desembarque/{ano}")
    public ResponseEntity<?> getAenaCargaDesembarque(@PathVariable String ano) {
        String filePath = String.format("data-json/aeroporto/aena/carga/embarque_desembarque/desembarque/%s_aena_carga_desembarque.json", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //              AENA - PASSAGEIRO
    // -------------------------------------------
    @GetMapping("/aena/passageiro/anos/{ano}")
    public ResponseEntity<?> getAenaPassageiroPorAno(@PathVariable String ano) {
        String filePath = String.format("data-json/aeroporto/aena/passageiro/anos/%s_aena_passageiros.json", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/aena/passageiro/embarque/{ano}")
    public ResponseEntity<?> getAenaPassageiroEmbarque(@PathVariable String ano) {
        String filePath = String.format("data-json/aeroporto/aena/passageiro/embarque_desembarque/embarque/%s_aena_passageiros_embarque.json", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/aena/passageiro/desembarque/{ano}")
    public ResponseEntity<?> getAenaPassageiroDesembarque(@PathVariable String ano) {
        String filePath = String.format("data-json/aeroporto/aena/passageiro/embarque_desembarque/desembarque/%s_aena_passageiros_desembarque.json", ano);
        return loadJsonResponse(filePath);
    }

    // -------------------------------------------
    //                 ANAC
    // -------------------------------------------
    @GetMapping("/anac/anos/{ano}")
    public ResponseEntity<?> getAnacResumoAnual(@PathVariable String ano) {
        String filePath = String.format("data-json/aeroporto/anac/anos/%s.json", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/anac/embarque/{ano}")
    public ResponseEntity<?> getAnacEmbarque(@PathVariable String ano) {
        String filePath = String.format("data-json/aeroporto/anac/embarque_desembarque/embarque/%s_embarque.json", ano);
        return loadJsonResponse(filePath);
    }

    @GetMapping("/anac/desembarque/{ano}")
    public ResponseEntity<?> getAnacDesembarque(@PathVariable String ano) {
        String filePath = String.format("data-json/aeroporto/anac/embarque_desembarque/desembarque/%s_desembarque.json", ano);
        return loadJsonResponse(filePath);
    }
}
